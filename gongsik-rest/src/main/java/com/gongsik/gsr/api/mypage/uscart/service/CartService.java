package com.gongsik.gsr.api.mypage.uscart.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gongsik.gsr.api.mypage.delivery.entity.DeliveryEntity;
import com.gongsik.gsr.api.mypage.uscart.dto.CartDto;
import com.gongsik.gsr.api.mypage.uscart.entity.CartEntity;
import com.gongsik.gsr.api.mypage.uscart.repository.CartRepository;
import com.gongsik.gsr.global.vo.ResultVO;

@Service
public class CartService {

	@Autowired
	CartRepository cartRepository;
	
	public Map<String, Object> wishList(Map<String, String> request) {
		Map<String, Object> map = new HashMap<>();
		ResultVO resultVo = new ResultVO();
		
		//장바구니 조회
		String usrId = request.get("usrId");
		String cartSt = request.get("cartSt");
		
		List<CartEntity> cartEntity =  cartRepository.findByCartUsrIdAndCartStAndDelYnOrderByCartNoDesc(usrId, cartSt, "N");
		
		if(cartEntity.isEmpty()) {
			resultVo.setCode("fail");
			resultVo.setMsg("장바구니 내역이 없습니다.");
			map.put("result", resultVo);
			return map;
		}else {
			List<CartDto> list = new ArrayList<>();
			for(CartEntity entity : cartEntity) {
				CartDto cartDto = new CartDto();
				cartDto.setCartItemCnt(entity.getCartItemCnt());
				cartDto.setCartItemNm(entity.getCartItemNm());
				cartDto.setCartNo(entity.getCartNo());
				list.add(cartDto);
			}
			map.put("result", list);
			map.put("cnt", list.size());
		}
		
		return map;
	}

	public Map<String, Object> cartDel(String usrId, long cartNo) {
		Map<String, Object> map = new HashMap<>();
		ResultVO resultVo = new ResultVO();
	
		Optional<CartEntity> cartEntity = cartRepository.findByCartUsrIdAndCartNo(usrId, cartNo);
		CartEntity entity = cartEntity.get();
		entity.setDelYn("Y");
		
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

	public Map<String, Object> wishAdd(String usrId, long cartNo) {
		Map<String, Object> map = new HashMap<>();
		ResultVO resultVo = new ResultVO();
	
		Optional<CartEntity> cartEntity = cartRepository.findByCartUsrIdAndCartNo(usrId, cartNo);
		CartEntity entity = cartEntity.get();
		entity.setCartSt("W");
		
		CartEntity result = cartRepository.save(entity);
		
		if(result != null) {
			resultVo.setCode("success");
			resultVo.setMsg("장바구니에 추가 되었습니다.");
			
		}else {
			resultVo.setMsg("");
		}
		map.put("result", resultVo);
		return map;
	}

}
