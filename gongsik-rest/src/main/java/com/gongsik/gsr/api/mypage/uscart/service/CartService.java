package com.gongsik.gsr.api.mypage.uscart.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		String delYn = "N";
		String useYn = "Y";
		
		List<Object[]> cartEntity =  cartRepository.findByCartUsrIdAndCartStAndDelYnAndUseYnOrderByCartNoDesc(usrId, cartSt, delYn, useYn);
		
		
		
		if(cartEntity.isEmpty()) {
			resultVo.setCode("fail");
			resultVo.setMsg("장바구니 내역이 없습니다.");
			map.put("result", resultVo);
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

	public Map<String, Object> cartDel(String usrId, long cartNo) {
		Map<String, Object> map = new HashMap<>();
		ResultVO resultVo = new ResultVO();
	
		Optional<CartEntity> cartEntity = cartRepository.findByCartUsrIdAndCartNo(usrId, cartNo);
		CartEntity entity = cartEntity.get();
		entity.setDelYn("Y");
		entity.setUseYn("N");
		
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
