package com.gongsik.gsr.api.payment.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
			long orderSeq = orderRepository.find();
			OrderEntity orderEntity = new OrderEntity();
			orderEntity.setOrderSeq(orderSeq);
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
					cartRepository.save(nEntity);
				} else {
					nEntity.setDelYn("Y");
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
		usrPointEntity.setPointPt(request.get("totalBenePrice").toString().replaceAll("적립", ""));
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
			usrPointEntity.setPointPt(String.valueOf(point));
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

}
