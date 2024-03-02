package com.gongsik.gsr.api.mypage.uscart.service;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.gongsik.gsr.api.common.entity.CommonEntity;
import com.gongsik.gsr.api.mypage.uscart.dto.CartDto;
import com.gongsik.gsr.api.mypage.uscart.entity.CartEntity;
import com.gongsik.gsr.api.mypage.uscart.repository.CartRepository;
import com.gongsik.gsr.global.vo.ResultVO;

@Service
public class CartService {
	
	@Autowired
	CartRepository cartRepository;
	
	public Map<String, Object> wishList(Map<String, Object> request) {
		Map<String, Object> map = new HashMap<>();
		ResultVO resultVo = new ResultVO();
		
		//장바구니 조회
		String usrId = request.get("usrId").toString();
		String cartSt = request.get("cartSt").toString();
		String delYn = "N";
		String useYn = "Y";
		int start = Integer.parseInt(request.get("currentPage").toString());
		int size = 5;
		Pageable pageable = PageRequest.of((start - 1), size, Sort.by("CHG_DT").descending());
		
		List<Object[]> cartEntity =  cartRepository.findByCartUsrIdAndCartStAndDelYnAndUseYnOrderByCartNoDesc(usrId, cartSt, delYn, useYn, pageable);
		
		int totalCnt = cartRepository.findByCartUsrIdAndCartSt(usrId, cartSt);
		map.put("totalCnt", totalCnt);
		if(cartEntity.isEmpty()) {
			resultVo.setCode("fail");
			resultVo.setMsg("장바구니 내역이 없습니다.");
			map.put("result", resultVo);
			map.put("cnt", 0);
			return map;
		}else {
			List<CartDto> list = new ArrayList<>();
			for(Object[] object: cartEntity) {
				CartDto cartDto = new CartDto();
				cartDto.setCartNo(Integer.parseInt(object[0].toString()));
				cartDto.setCartUsrId(object[1].toString());
				cartDto.setCartItemCnt(Integer.parseInt(object[2].toString()));
				cartDto.setCartItemNm(object[3].toString());
				cartDto.setCartItemNo(object[4].toString());
				cartDto.setCartSt(object[5].toString());
				cartDto.setCartUrl(object[6].toString());
				int price = Integer.parseInt(object[7].toString());
				if(cartDto.getCartSt().equals("W")) {
					price = price * Integer.parseInt(object[2].toString());
				}
				DecimalFormat krFormat = new DecimalFormat("###,###원");
				String cartPrice = krFormat.format(price);
				cartDto.setCartPrice(cartPrice);
				
				cartDto.setUseYn(object[8].toString());
				cartDto.setDelYn(object[9].toString());;
				list.add(cartDto);
			}
			map.put("result", list);
			map.put("cnt", list.size());
		}
		
		return map;
	}
	
	/* 장바구니 삭제 */
	public Map<String, Object> cartDel(Map<String,Object> request) {
		Map<String, Object> map = new HashMap<>();
		ResultVO resultVo = new ResultVO();
		int cartNo = Integer.parseInt(request.get("cartNo").toString());
		String usrId = request.get("usrId").toString();
		String cartSt = request.get("cartSt").toString();
		
		Optional<CartEntity> cartEntity = cartRepository.findByCartUsrIdAndCartNoAndDelYnAndUseYn(usrId, cartNo, "N" ,"Y");
		String entitiyCartSt = cartEntity.get().getCartSt();
		CartEntity entity = cartEntity.get();
		if("A".equals(entitiyCartSt)) {
			if(cartSt.contains("L")) {
				entity.setCartSt("W");
			}else {
				entity.setCartSt("L");
				entity.setCartItemCnt(0);
			}
		}else {
			entity.setDelYn("Y");
			entity.setUseYn("N");
		}
		CartEntity result = cartRepository.save(entity);
		
		if(result != null) {
			resultVo.setCode("success");
			resultVo.setMsg("삭제 되었습니다.");
			
		}else {
			resultVo.setMsg("");
		}
		map.put("result", resultVo);
		return map;

	}
	
	/* 장바구니 추가 */
	public Map<String, Object> wishAdd(String usrId, long cartNo) {
		Map<String, Object> map = new HashMap<>();
		ResultVO resultVo = new ResultVO();
		
		LocalDateTime date = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formatterDate = date.format(formatter);
		Optional<CartEntity> cartEntity = cartRepository.findByCartUsrIdAndCartNoAndDelYnAndUseYn(usrId, cartNo, "N" ,"Y");
		if(cartEntity.isPresent()) {
			CartEntity entity = cartEntity.get();
			entity.setCartSt("A");
			entity.setCartItemCnt(cartEntity.get().getCartItemCnt()+1);
			CommonEntity commonEntity = entity.getCommonEntity();
			commonEntity.setChgDt(formatterDate);
			entity.setCommonEntity(commonEntity);
		
			CartEntity result = cartRepository.save(entity);
		
			if(result != null) {
				resultVo.setCode("success");
				resultVo.setMsg("장바구니에 추가 되었습니다.");
				
			}else {
				resultVo.setMsg("");
			}
		}else {
			CartEntity entity = cartEntity.get();
			entity.setCartSt("A");
			entity.setCartItemCnt(1);
			entity.setUseYn("Y");
			entity.setDelYn("N");
			CommonEntity commonEntity = entity.getCommonEntity();
			commonEntity.setChgDt(formatterDate);
			entity.setCommonEntity(commonEntity);
			int newCartNo = cartRepository.findOne();
			
			entity.setCartNo(newCartNo+1);
			
			CartEntity result = cartRepository.save(entity);
		
			if(result != null) {
				resultVo.setCode("success");
				resultVo.setMsg("장바구니에 추가 되었습니다.");
				
			}else {
				resultVo.setMsg("");
			}
		}
		map.put("result", resultVo);
		return map;
	}


}
