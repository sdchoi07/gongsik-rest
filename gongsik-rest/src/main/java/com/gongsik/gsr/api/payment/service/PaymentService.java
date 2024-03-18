package com.gongsik.gsr.api.payment.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gongsik.gsr.api.account.join.entity.AccountEntity;
import com.gongsik.gsr.api.account.join.repository.AccountRepository;
import com.gongsik.gsr.api.main.categories.repository.CategoriesRepository;
import com.gongsik.gsr.api.mypage.order.entity.OrderEntity;
import com.gongsik.gsr.api.mypage.order.repository.OrderRepository;
import com.gongsik.gsr.api.mypage.uscart.entity.CartEntity;
import com.gongsik.gsr.api.mypage.uscart.repository.CartRepository;
import com.gongsik.gsr.api.mypage.usrGrade.entity.UsrGradeEntity;
import com.gongsik.gsr.api.mypage.usrGrade.repository.GradeMstRepository;
import com.gongsik.gsr.api.mypage.usrGrade.repository.UsrGradeRepository;
import com.gongsik.gsr.api.mypage.usrPoint.entity.UsrPointEntity;
import com.gongsik.gsr.api.mypage.usrPoint.repository.UsrPointRepository;
import com.gongsik.gsr.api.payment.dto.InvenDto;
import com.gongsik.gsr.api.payment.entity.PaymentEntity;
import com.gongsik.gsr.api.payment.repository.PaymentRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

import jakarta.annotation.PostConstruct;

@Service
public class PaymentService {

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	CategoriesRepository categoriesRepository;

	@Autowired
	UsrPointRepository usrPointRepository;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	CartRepository cartRepository;

	@Autowired
	PaymentRepository paymentRepository;

	@Autowired
	GradeMstRepository gradeMstRepository;

	@Autowired
	UsrGradeRepository usrGradeRepository;

	@Value("${iamport.key}")
	private String restApiKey;
	@Value("${iamport.secret}")
	private String restApiSecret;

	private IamportClient iamportClient;

	@PostConstruct
	public void init() {
		this.iamportClient = new IamportClient(restApiKey, restApiSecret);
	}

	/* 결제자 정보 */
	public Map<String, Object> accountInfo(String usrId) {
		Map<String, Object> map = new HashMap<>();

		Optional<Object[]> accountInfo = accountRepository.findByUsrId2(usrId);
		if (accountInfo.isPresent()) {
			Object selectOne = accountInfo.get();
			map.put("result", selectOne);
			map.put("code", "success");
		} else {
			map.put("code", "fail");
		}
		return map;
	}

	@Transactional
	public Map<String, Object> verify(Map<String, Object> request) throws IamportResponseException, IOException {
		Map<String, Object> map = new HashMap<>();
		// 결제 연동
		IamportResponse<Payment> iamResponse = iamportClient.paymentByImpUid(request.get("imp_uid").toString());
		// 클라이언트 상품 목록 담기
		List<Object> itemList = (List<Object>) request.get("items");
		String usrId = request.get("usrId").toString();
		// 결제 총 금액
		BigDecimal paidAmount = iamResponse.getResponse().getAmount(); // 사용자가 결제한 금액
		// 결제 주문 번호
		String merchantUid = iamResponse.getResponse().getMerchantUid();
		// 결제 타입
		String paymentType = iamResponse.getResponse().getPayMethod();
		// 결제 카드 번호
		String cardNo = iamResponse.getResponse().getCardNumber();
		// 결제 카드 번호
		String cardNm = iamResponse.getResponse().getCardName();
		// 결제 계좌
		String bankNo = iamResponse.getResponse().getBankCode();
		// 은행
		String bankNm = iamResponse.getResponse().getBankName();
		// 배송지
		String addr = iamResponse.getResponse().getBuyerAddr();
		int point = Integer.parseInt(request.get("point").toString()); // 클라이언트 포인트
		LocalDateTime cur = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String curDt = formatter.format(cur);

		// 결제 총 금액 셋팅
		int clientTotalPrice = 0;
		// 유저 이름 가져오기
		Optional<AccountEntity> accountEntity = accountRepository.findByUsrId(usrId);
		String usrNm = accountEntity.get().getUsrNm();
		// 재고 확인 및 재고 변경
		int i = 1;
		for (Object itemObj : itemList) {
			Map<String, Object> item = (Map<String, Object>) itemObj;
			// 구매 상품 고유 번호 추출
			String itemNo = item.get("itemNo").toString();
			String itemQuantity = item.get("itemQuantity").toString();
			// 추출후, 해당 상품 정보 가져오기
			InvenDto dto = categoriesRepository.findByInvneSClsNo(itemNo);

			// 해당 상품 정보 가져온 재고 정보 셋팅
			int itemCnt = dto.getInvenCnt();
			int itemPrice = dto.getItemPrice();
			String url = dto.getItemUrl();
			String itemNm = dto.getItemNm();

			// 배송 업데이트

//			Optional<OrderEntity> orderEntity = orderRepository.findByUsrIdAndOrderNo(usrId, 
//					merchantUid);
				OrderEntity orderEntity = new OrderEntity();
				orderEntity.setOrderSeq(i++);
				orderEntity.setUsrId(usrId);
				orderEntity.setUsrNm(usrNm);
				orderEntity.setItemNm(itemNm);
				orderEntity.setItemCnt(Integer.parseInt(itemQuantity));
				orderEntity.setItemNo(itemNo);
				orderEntity.setItemImg(url);
				DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				String curDt2 = formatter2.format(cur).replaceAll("-", "");
				orderEntity.setOrderDt(curDt2);
				orderEntity.setOrderSt("01");
				orderEntity.setOrderNo(merchantUid);
				orderEntity.setOrderAddr(addr);
				orderRepository.save(orderEntity);

			// 만약 위시리스트 에서 구매할시 삭제
			Optional<CartEntity> entity = cartRepository.findByCartUsrIdAndCartItemNoAndDelYn(usrId, itemNo, "N");
			if (entity.isPresent()) {
				CartEntity nEntity = entity.get();
				if (nEntity.getCartSt().equals("A")) {
					nEntity.setCartSt("L");
					nEntity.setCartItemCnt(0);
					cartRepository.save(nEntity);
				} else {
					nEntity.setDelYn("Y");
					nEntity.setCartItemCnt(0);
					cartRepository.save(nEntity);
				}
			}

			// 재고 부족
			if (itemCnt == 0) {
				CancelData cancelData = new CancelData(iamResponse.getResponse().getImpUid(), true);
				cancelData.setReason(dto.getItemNm() + " 재고가 부족 합니다.");
				iamportClient.cancelPaymentByImpUid(cancelData);
				map.put("code", "outOfStock");
				map.put("msg", dto.getItemNm() + " 재고가 부족 합니다.");
				return map;
			}
			// 재고 변경 (현 재고 - 사용자 구매 갯수)
			categoriesRepository.updateItemCnt(dto.getItemNo(), itemCnt - Integer.parseInt(itemQuantity));

			// 추출한 상품 정보 가격 수량 구해서 총 구매 구하기
			clientTotalPrice += itemPrice * Integer.parseInt(itemQuantity);
		}
		// 사용한 포인트를 총 구매 금액 차감
		clientTotalPrice = clientTotalPrice - point;

		// 사용자가 구매한 총 금액 과 결제 금액 상이
		if (clientTotalPrice != paidAmount.intValue()) {
			CancelData cancelData = new CancelData(iamResponse.getResponse().getImpUid(), true);
			cancelData.setReason("결제한 금액이 상이합니다. 다시 결제 해주세요.");
			iamportClient.cancelPaymentByImpUid(cancelData);
			map.put("code", "diffAmountPrice");
			map.put("msg", "결제한 금액이 상이합니다. 다시 결제 해주세요.");
			return map;
		}

		// 포인트 사용시 차감 후 적립
		// 포인트 적립
		long pointSeq = usrPointRepository.find();
		UsrPointEntity usrPointEntity = new UsrPointEntity();
		usrPointEntity.setPointSeq(pointSeq + 1);
		usrPointEntity.setPointUsrId(usrId);
		usrPointEntity.setPointPt(request.get("totalBenePrice").toString().replaceAll(",", "").replaceAll("적립", ""));
		usrPointEntity.setPointSt("S");
		usrPointEntity.setPointCrtDt(curDt);
		cur.plusYears(1);
		String exDt = formatter.format(cur);
		usrPointEntity.setPointExpireDt(exDt);
		usrPointRepository.save(usrPointEntity);
		// 기존 포인트 사용시 차감
		if (point != 0) {
			usrPointEntity.setPointSeq(pointSeq + 2);
			usrPointEntity.setPointUsrId(usrId);
			usrPointEntity.setPointPt(String.valueOf(point).replaceAll(",", ""));
			usrPointEntity.setPointSt("U");
			usrPointEntity.setPointUsedDt(curDt);
			usrPointRepository.save(usrPointEntity);
		}

		// 결제 업데이트
		PaymentEntity paymentEntity = new PaymentEntity();
		paymentEntity.setPaymentUsrId(usrId);
		paymentEntity.setPaymentUsrNm(usrNm);
		paymentEntity.setPaymentOrderNo(merchantUid);
		paymentEntity.setPaymentSt("01"); // 결제 완료
		paymentEntity.setPaymentType(paymentType);
		paymentEntity.setPaymentAmount(paidAmount.intValue());
		paymentEntity.setPaymentCardNo(cardNo);
		paymentEntity.setPaymentCardType(cardNm);
		paymentEntity.setPaymentBankNo(bankNo);
		paymentEntity.setPaymentBankType(bankNm);
		paymentEntity.setPaymentFinDt(curDt);
		paymentRepository.save(paymentEntity);

		int totalPaidAmount = paymentRepository.findByPaymentUsrIdAndPaymentUsrNm(usrId, usrNm);

		// 누적금액 업데이트
//		int sumAmount = totalPaidAmount + paidAmount.intValue();

		String grdLevel = gradeMstRepository.findByGradePoint(totalPaidAmount);

		Optional<UsrGradeEntity> usrGradeEntity = usrGradeRepository.findByGradeUsrId(usrId);
		if (usrGradeEntity.isPresent()) {
			UsrGradeEntity entity = usrGradeEntity.get();
			entity.setGradeLevel(grdLevel);
			usrGradeRepository.save(entity);
		} else {
			UsrGradeEntity entity = usrGradeEntity.get();
			entity.setGradeUsrId(usrId);
			entity.setGradeLevel(grdLevel);
			entity.setGradeUsrNm(usrNm);
		}
		map.put("code", "success");
		map.put("msg", "결제 완료 되었습니다.");
		return map;
	}

	/* 결제 취소 */
	@Transactional
	public Map<String, Object> cancel(Map<String, Object> request) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();

		String merchantUid = request.get("orderNo").toString();
		String itemNo = request.get("itemNo").toString();
		int itemCnt = Integer.parseInt(request.get("itemCnt").toString());
		int itemPrice = Integer.parseInt(request.get("itemPrice").toString());
		String usrId = request.get("usrId").toString();
		String orderDt = request.get("orderDt").toString();
		// 유저 이름 가져오기
		Optional<AccountEntity> accountEntity = accountRepository.findByUsrId(usrId);
		String usrNm = accountEntity.get().getUsrNm();

		// 배송업데이트
		Optional<OrderEntity> orderEntity = orderRepository.findByUsrIdAndItemNoAndOrderNo(usrId, itemNo, merchantUid);
		if (orderEntity.isPresent()) {
			OrderEntity entity = orderEntity.get();
			LocalDateTime cur = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String curDt = formatter.format(cur).replaceAll("-", "");
			entity.setCancelDt(curDt);
			entity.setOrderSt("03");
			orderRepository.save(entity);
		} else {
			map.put("code", "error");
			map.put("msg", "결제 취소 실패 하였습니다.");
			return map;
		}
		// 재고 업데이트
		// 추출후, 해당 상품 정보 가져오기
		InvenDto dto = categoriesRepository.findByInvneSClsNo(itemNo);
		categoriesRepository.updateItemCnt(dto.getItemNo(), dto.getInvenCnt() + itemCnt);

		// 누적금액 업데이트
		int totalPaidAmount = paymentRepository.findByPaymentUsrIdAndPaymentUsrNm(usrId, usrNm);
		String grdLevel = gradeMstRepository.findByGradePoint(totalPaidAmount);

		Optional<UsrGradeEntity> usrGradeEntity = usrGradeRepository.findByGradeUsrId(usrId);
		if (usrGradeEntity.isPresent()) {
			UsrGradeEntity entity = usrGradeEntity.get();
			entity.setGradeLevel(grdLevel);
			usrGradeRepository.save(entity);
		} else {
			UsrGradeEntity entity = usrGradeEntity.get();
			entity.setGradeUsrId(usrId);
			entity.setGradeLevel(grdLevel);
			entity.setGradeUsrNm(usrNm);
		}

		String accessToken = getToken();

		LocalDateTime cur = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String curDt = formatter.format(cur).replaceAll("-", "");

		orderRepository.updateCancel(usrId, orderDt, itemNo, curDt, "03", merchantUid);
		URL url = new URL("https://api.iamport.kr/payments/cancel");
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

		// 요청 방식을 POST로 설정
		conn.setRequestMethod("POST");

		// 요청의 Content-Type, Accept, Authorization 헤더 설정
		conn.setRequestProperty("Content-type", "application/json");
		conn.setRequestProperty("Accept", "application/json");
		conn.setRequestProperty("Authorization", accessToken);

		// 해당 연결을 출력 스트림(요청)으로 사용
		conn.setDoOutput(true);

		// JSON 객체에 해당 API가 필요로하는 데이터 추가.
		JsonObject json = new JsonObject();
		json.addProperty("merchant_uid", merchantUid);
		json.addProperty("reason", "취소");
		json.addProperty("amount", itemPrice);

		// 출력 스트림으로 해당 conn에 요청
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
		bw.write(json.toString());
		bw.flush();
		bw.close();

		// 입력 스트림으로 conn 요청에 대한 응답 반환
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String responseJson = new BufferedReader(new InputStreamReader(conn.getInputStream())).lines()
				.collect(Collectors.joining("\n"));

		System.out.println("응답 본문: " + responseJson);

		JsonObject jsonResponse = JsonParser.parseString(responseJson).getAsJsonObject();
		String resultCode = jsonResponse.get("code").getAsString();
		
		if (!resultCode.equals("0")) {
			JsonObject responseObj = jsonResponse.getAsJsonObject("response");
			String originalCancelReason = responseObj.get("response").getAsString(); // 원래의 이스케이프된 문자열
			String decodedCancelReason = originalCancelReason.replace("\\", ""); // 이스케이프 문자 제거
			map.put("code", "error");
			map.put("msg", decodedCancelReason);
			return map;
		}

		System.out.println("결과 코드 = " + resultCode);
		
		JsonObject responseObj = jsonResponse.getAsJsonObject("response");

		br.close();

		conn.disconnect();

//		int cancelAmount = responseObj.get("cancel_amount").getAsInt();
		String payMethod = responseObj.get("pay_method").getAsString();
		String cardType = responseObj.get("card_name").getAsString();
		String cardNo = "";
		if("card".equals(cardType)) {
			cardNo = responseObj.get("card_number").getAsString();
		}

		// 결제 취소 업데이트
		PaymentEntity paymentEntity = new PaymentEntity();
		paymentEntity.setPaymentUsrId(usrId);
		paymentEntity.setPaymentUsrNm(usrNm);
		paymentEntity.setPaymentOrderNo(merchantUid);
		paymentEntity.setPaymentSt("02"); // 결제 취소
		paymentEntity.setPaymentType(payMethod);
		paymentEntity.setPaymentAmount(itemPrice);
		paymentEntity.setPaymentCardNo(cardNo);
		paymentEntity.setPaymentCardType(cardType);
		paymentEntity.setPaymentCancelDt(curDt);
		paymentRepository.save(paymentEntity);

		// 주문 업데이트
		map.put("code", "success");
		map.put("msg", "결제 취소 완료 되었습니다.");
		return map;
	}

	/* 결제 취소위한 토큰 얻기 */
	public String getToken() throws IOException {
		URL url = new URL("https://api.iamport.kr/users/getToken");
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

		// 요청 방식을 POST로 설정
		conn.setRequestMethod("POST");

		// 요청의 Content-Type과 Accept 헤더 설정
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Accept", "application/json");

		// 해당 연결을 출력 스트림(요청)으로 사용
		conn.setDoOutput(true);

		// JSON 객체에 해당 API가 필요로하는 데이터 추가.
		JsonObject json = new JsonObject();
		json.addProperty("imp_key", restApiKey);
		json.addProperty("imp_secret", restApiSecret);

		// 출력 스트림으로 해당 conn에 요청
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
		bw.write(json.toString()); // json 객체를 문자열 형태로 HTTP 요청 본문에 추가
		bw.flush(); // BufferedWriter 비우기
		bw.close(); // BufferedWriter 종료

		// 입력 스트림으로 conn 요청에 대한 응답 반환
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		Gson gson = new Gson(); // 응답 데이터를 자바 객체로 변환
		String response = gson.fromJson(br.readLine(), Map.class).get("response").toString();
		String accessToken = gson.fromJson(response, Map.class).get("access_token").toString();
		br.close(); // BufferedReader 종료

		conn.disconnect(); // 연결 종료

		return accessToken;
	}
}
