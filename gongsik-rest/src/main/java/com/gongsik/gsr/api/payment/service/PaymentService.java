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

import com.gongsik.gsr.api.account.join.repository.AccountRepository;
import com.gongsik.gsr.api.main.categories.repository.CategoriesRepository;
import com.gongsik.gsr.api.mypage.usrPoint.entity.UsrPointEntity;
import com.gongsik.gsr.api.mypage.usrPoint.repository.UsrPointRepository;
import com.gongsik.gsr.api.payment.dto.InvenDto;
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
		IamportResponse<Payment> iamResponse = iamportClient.paymentByImpUid(request.get("imp_uid").toString());
		BigDecimal paidAmount = iamResponse.getResponse().getAmount(); // 사용자가 결제한 금액
//		 checkDuplicatePayment(paymentReq);
		// DB에 있는 금액과 사용자가 결제한 금액이 같은지 확인
//	        int amountForPay = fromDB; //db에서 가져온 금액
//		String itemNm = iamResponse.getResponse().getName(); // 클라이언트 아이템명

		String itemNo = request.get("itemKey").toString(); // 클라이언트 아이템번호
		String usrId = request.get("usrId").toString();
		// String[] itemNoArr = itemNo.split(",");
//		List<String> itemNoList = new ArrayList<>();
//		for(String arr : itemNoList) {
//			itemNoList.add(arr);
//		}

		int count = Integer.parseInt(request.get("count").toString()); // 클라이언트 갯수
		int point = Integer.parseInt(request.get("point").toString().replaceAll(",", "").substring(0,
				request.get("point").toString().indexOf("p"))); // 클라이언트 포인트

		List<InvenDto> invenDto = categoriesRepository.findByInvneSClsNo(itemNo);
		int clientTotalPrice = 0;

		for (InvenDto dto : invenDto) {

			int itemCnt = dto.getInvenCnt();
			// 재고 부족
			if (itemCnt == 0) {
				CancelData cancelData = new CancelData(iamResponse.getResponse().getImpUid(), true);
				cancelData.setReason(dto.getItemNm() + " 재고가 부족 합니다.");
				iamportClient.cancelPaymentByImpUid(cancelData);
				map.put("code", "outOfStock");
				map.put("msg", dto.getItemNm() + " 재고가 부족 합니다.");
				return map;
			}
			// 재고 변경
			categoriesRepository.updateItemCnt(dto.getItemNo(), dto.getInvenCnt() - count);

			clientTotalPrice += clientTotalPrice * count;
		}
		clientTotalPrice = clientTotalPrice - point;

		// 결제 금액 상이
		if (clientTotalPrice != paidAmount.intValue()) {
			CancelData cancelData = new CancelData(iamResponse.getResponse().getImpUid(), true);
			cancelData.setReason("결제한 금액이 상이합니다. 다시 결제 해주세요.");
			iamportClient.cancelPaymentByImpUid(cancelData);
			map.put("code", "diffAmountPrice");
			map.put("msg", "결제한 금액이 상이합니다. 다시 결제 해주세요.");
			return map;
		}

		LocalDateTime cur = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String curDt = formatter.format(cur);
		// 포인트 사용시 차감 후 적립
		UsrPointEntity usrPointEntity = usrPointRepository.findByPointUsrId(usrId);
		usrPointEntity.setPointPt("50");
		usrPointEntity.setPointSt("S");
		usrPointEntity.setPointCrtDt(curDt);
		usrPointRepository.save(usrPointEntity);
		
		if (point != 0) {
			usrPointEntity.setPointPt(String.valueOf(point));
			usrPointEntity.setPointSt("U");
			usrPointEntity.setPointUsedDt(curDt);
			usrPointRepository.save(usrPointEntity);
		}
		
		//배송 업데이트
		
		//만약 위시리스트 에서 구매할시 삭제 
		
		//결제 업데이트
		
		//누적금액 업데이트
		return map;
	}

}
