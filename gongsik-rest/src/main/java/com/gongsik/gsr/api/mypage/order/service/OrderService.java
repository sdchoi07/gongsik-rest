package com.gongsik.gsr.api.mypage.order.service;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.gongsik.gsr.api.mypage.order.dto.OrderDto;
import com.gongsik.gsr.api.mypage.order.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
	@Autowired
	private OrderRepository orderRepository;
	
	public Map<String, Object> orderList(Map<String, Object> map) {
		
		Map<String , Object> resultData = new HashMap<String, Object>();
		
		//String usrNm = map.get("usrNm").toString();
		String usrId = map.get("usrId").toString();
		String orderCode = map.get("orderDt").toString();
		
		//날짜 구하기
		String orderDt = getDate(orderCode);
		// 페이징 처리를 위한 Pageable 객체 생성
		int currentPage = Integer.parseInt(map.get("currentPage").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		
		Pageable pageable = PageRequest.of((currentPage -1), pageSize, Sort.by("ORDER_DT","ORDER_NO").descending());
		
		int totalCnt = orderRepository.findByUsrId(usrId);
		
		Page<Object[]> entity = orderRepository.findByUsrIdAndOrderDt( usrId, orderDt, pageable);
		log.info("entity 결과 : {}" , entity.get().toArray());
		ArrayList<OrderDto> list = new ArrayList<>();
		for(Object[] result : entity) {
			OrderDto orderDto = new OrderDto();
			orderDto.setItemNm(result[0].toString());
			orderDto.setItemCnt(Integer.parseInt(result[1].toString()));
			
			String afterOrderDt = result[2].toString();
			afterOrderDt = afterOrderDt.substring(0, 4) + "." + afterOrderDt.substring(4, 6) + "." + afterOrderDt.substring(6,8);
			orderDto.setOrderDt(afterOrderDt);
			
			orderDto.setItemImg(result[3].toString());
			orderDto.setOrderStNm(result[4].toString());
			orderDto.setOrderSt(result[5].toString());
			int price = Integer.parseInt(result[6].toString());
			price = price * orderDto.getItemCnt();
			DecimalFormat krFormat = new DecimalFormat("###,###원");
			String cartPrice = krFormat.format(price);
			orderDto.setItemPrice(cartPrice);
			orderDto.setOrderNo(result[7].toString());
			orderDto.setItemNo(result[8].toString());
			list.add(orderDto);
		}
		log.info("orderDto : {}" , list);
		resultData.put("result", list);
		resultData.put("cnt", list.size());
		resultData.put("totalCnt", totalCnt);
		
		return resultData;
		
	}
	public String getDate(String orderCode) {
		// 현재 날짜 및 시간 가져오기
        LocalDateTime currentDateTime = LocalDateTime.now();
        String orderDt = "";
        LocalDateTime pastDate = currentDateTime.minusMonths(1);
        
        if("1".equals(orderCode)) {//1달
        	 pastDate = currentDateTime.minusMonths(1);
        	 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        	 orderDt = pastDate.format(formatter);
        	return  orderDt; 
        }else if("2".equals(orderCode)) {//3달
        	pastDate = currentDateTime.minusMonths(3);
	       	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
	       	orderDt = pastDate.format(formatter);
        	return orderDt;
        }else if("3".endsWith(orderCode)) {//6달
        	pastDate = currentDateTime.minusMonths(6);
	       	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
	       	orderDt = pastDate.format(formatter);
        	return orderDt;
        }else { //1년
        	pastDate = currentDateTime.minusYears(1);
	       	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
	       	orderDt = pastDate.format(formatter);
        	return orderDt;
        }
        
        
	}

}
