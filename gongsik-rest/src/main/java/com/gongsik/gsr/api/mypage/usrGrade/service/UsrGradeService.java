package com.gongsik.gsr.api.mypage.usrGrade.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gongsik.gsr.api.mypage.usrGrade.dto.GradeMstDto;
import com.gongsik.gsr.api.mypage.usrGrade.dto.UsrGradeDto;
import com.gongsik.gsr.api.mypage.usrGrade.entity.GradeMstEntity;
import com.gongsik.gsr.api.mypage.usrGrade.entity.UsrGradeEntity;
import com.gongsik.gsr.api.mypage.usrGrade.repository.GradeMstRepository;
import com.gongsik.gsr.api.mypage.usrGrade.repository.UsrGradeRepository;
import com.gongsik.gsr.global.vo.ResultVO;

@Service
public class UsrGradeService {
	
	@Autowired
	UsrGradeRepository usrGradeRepository;
	
	@Autowired
	GradeMstRepository gradeMstRepository;
	
	public Map<String, Object> usrGradeSelect(Map<String, String> request) {
		Map<String, Object> map = new HashMap<>();
		ResultVO resultVo = new ResultVO();
		
		//등급 내용
		List<GradeMstEntity> gradeMstEntity = gradeMstRepository.findAll();
		List<GradeMstDto> mstDto = new ArrayList<>();
		for(GradeMstEntity entity : gradeMstEntity) {
			GradeMstDto dto = new GradeMstDto();
			dto.setGradeLevel(entity.getGradeLevel());
			dto.setGradeDesc(entity.getGradeDesc());
			resultVo.setCode("success");
			mstDto.add(dto);
		}
		map.put("gradeMst", mstDto);
		
		//회원 등급 조회
		String usrId = request.get("usrId");
		
		Optional<UsrGradeEntity> usrGradeEntity =  usrGradeRepository.findByGradeUsrId(usrId);
		if(usrGradeEntity.isEmpty()) {
			resultVo.setCode("fail");
			resultVo.setMsg("등급이 아직 없습니다.");
			map.put("result", resultVo);
			return map;
		}else {
			UsrGradeDto usrGradeDto = new UsrGradeDto();
			usrGradeDto.setGradeLevel(usrGradeEntity.get().getGradeLevel());
			usrGradeDto.setGradeUsrNm(usrGradeEntity.get().getGradeUsrNm());
			resultVo.setCode("success");
			map.put("usrGradeDto", usrGradeDto);
		}
		map.put("result", resultVo);
		return map;
	}

}
