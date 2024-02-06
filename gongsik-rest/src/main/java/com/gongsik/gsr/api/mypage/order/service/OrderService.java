package com.gongsik.gsr.api.mypage.order.service;

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
	
	public Map<String, Object> orderList(Map<String, String> map) {
		
		Map<String , Object> resultData = new HashMap<String, Object>();
		
		String usrNm = map.get("usrNm");
		String usrId = map.get("usrId");
		String orderCode = map.get("orderDt");
		
		//날짜 구하기
		String orderDt = getDate(orderCode);
		// 페이징 처리를 위한 Pageable 객체 생성
		int pageNumber = 0;
		int pageSize = 10;
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("orderDt"));
		
		Page<OrderDto> entity = orderRepository.findByUsrNmAndUsrIdAndOrderDt( usrId, orderDt, pageable);
		
		List<OrderDto> list = new ArrayList<>();
		for(OrderDto result : entity) {
			OrderDto orderDto = new OrderDto(orderDt, pageSize, orderDt);
			orderDto.setItemNm(result.getItemNm());
			orderDto.setItemCnt(result.getItemCnt());
			orderDto.setOrderSt(result.getOrderSt());
			list.add(orderDto);
		}
		log.info("orderDto : {}" , list);
		resultData.put("result", entity);
		resultData.put("cnt", list.size());
		
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
