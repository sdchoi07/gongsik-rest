package com.gongsik.gsr.api.account.join.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gongsik.gsr.api.account.join.dto.JoinDto;
import com.gongsik.gsr.api.account.join.entity.AuthSMSEntity;
import com.gongsik.gsr.api.account.join.entity.AuthSMSHistEntity;
import com.gongsik.gsr.api.account.join.repository.AuthSMSHistRepository;
import com.gongsik.gsr.api.account.join.repository.AuthSMSRepository;
import com.gongsik.gsr.api.account.join.repository.InternationalPhoneNumberRepository;
import com.gongsik.gsr.global.vo.ResultVO;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Tag(name = "Menu Controller", description = "회원가입")
public class JoinService {
	
	private final Logger log =LoggerFactory.getLogger(JoinService.class);
	
	@Autowired
	private InternationalPhoneNumberRepository internationalPhoneNumberRepository;
	
	@Autowired
	private AuthSMSRepository authSMSRepository;
	
	@Autowired
	private AuthSMSHistRepository authSMSHistRepository;
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	/* 국제 번호 조회 */
	public List<JoinDto> countryPhNmList() {
		    //조회된 국제번호를 String형 list에 저장
			List<JoinDto> list=  internationalPhoneNumberRepository.findAllOnlyCountryPhNm();
			//저장된 listEntity를 dto 하나씩 저장하여 joinList에 저장
//			List<JoinDto> joinList =  listEntity.stream()
//											   .map(mapEntity -> { 
//												JoinDto dto = new JoinDto(); //entitiy에 저장한 data 를 dto에 저장
//											    dto.setCountryId(mapEntity.getCountryId());
//											    	return dto;
//												   
//											   }).collect(Collectors.toList()); //각각 저장된 dto는 list에 담기 
		return  list;
	}
	
	/* 인증번호 저장 */
	@Transactional
	public ResultVO authNoSave(JoinDto joinDto) {
		ResultVO resultVo = new ResultVO();
		//인증번호 redis 저장
		redisTemplate.opsForValue().set("authNo", joinDto.getAuthNo());
		String answer = redisTemplate.opsForValue().get("authNo");
		System.out.println("redis : " + answer);
		AuthSMSEntity result = null;
		try {
				//해당 아이디로 기존에 인증요청 확인
				Optional<AuthSMSEntity> selectOne = authSMSRepository.findByUsrId(joinDto.getUsrId());
				if(selectOne.isPresent()) { //인증요청 한 아이디일경우 update
					selectOne.get().setReReqAuthCnt(selectOne.get().getReReqAuthCnt()+1 );
					selectOne.get().setReqDt(joinDto.getReqDt().toString());
					
					result = authSMSRepository.save(selectOne.get());
				}else { //인증 요청 처음 일 경우 insert
				
					AuthSMSEntity insertAuthSMSEntity = new AuthSMSEntity();
					
					//회원정보 authSMSEntity 저장
					insertAuthSMSEntity.setAuthId(joinDto.getAuthId());
					insertAuthSMSEntity.setAuthNo(joinDto.getAuthNo());
					insertAuthSMSEntity.setAuthType(joinDto.getAuthType());
					insertAuthSMSEntity.setAuthYn(joinDto.getAuthYn());
					insertAuthSMSEntity.setUsrId(joinDto.getUsrId());
					insertAuthSMSEntity.setUsrPhNo(joinDto.getUsrPhNo());
					insertAuthSMSEntity.setReReqAuthCnt(joinDto.getReReqAuthCnt());
					insertAuthSMSEntity.setCountryPh(joinDto.getCountryPh());
					insertAuthSMSEntity.setReqDt(joinDto.getReqDt().toString());
					
					log.info("authSMSEntity :{} ",insertAuthSMSEntity);
					//auth_sms_inf 테이블 저장 및 update 
					result = authSMSRepository.save(insertAuthSMSEntity);
				}
				
				//회원정보 이력 hist 저장 테이블 초기화
				AuthSMSHistEntity resultHist = null;
				System.out.println("result:dd" );
				log.info("result :{} " , result);
						//auth_sms_inf 테이블에 저장 되었을 경우 hist 저장 시작 
						if(result != null) {
							//authSMsHistEntity 저장
							AuthSMSHistEntity authSMSHistEntity = new AuthSMSHistEntity();
							authSMSHistEntity.setAuthId(joinDto.getAuthId());
							authSMSHistEntity.setAuthNo(joinDto.getAuthNo());
							authSMSHistEntity.setAuthType(joinDto.getAuthType());
							authSMSHistEntity.setAuthYn(joinDto.getAuthYn());
							authSMSHistEntity.setUsrId(joinDto.getUsrId());
							authSMSHistEntity.setUsrPhNo(joinDto.getUsrPhNo());
							authSMSHistEntity.setReReqAuthCnt(joinDto.getReReqAuthCnt());
							//auth_sms_inf_hist 테이블에 저장
							resultHist = authSMSHistRepository.save(authSMSHistEntity);
							
						}else { //auth_sms_inf 테이블에 저장 안됬을경우
							resultVo.setErrCode("fail");
							resultVo.setErrMsg("인증 번호 전송 실패");
							return resultVo;
						}
					//auth_sms_inf_hist 테이블에 저장 되었을경우
					if(resultHist != null) {
						resultVo.setErrCode("done");
						resultVo.setErrMsg("인증 번호 전송 되었습니다.");
						return resultVo;
					}else {//auth_sms_inf_hist 테이블에 저장 실패 일 경우
						resultVo.setErrCode("fail");
						resultVo.setErrMsg("인증 번호 전송 실패");
						return resultVo;
					}
				
		}catch(Exception e){
				resultVo.setErrCode("fail");
				resultVo.setErrMsg("인증 번호 전송 실패");
		}
		return resultVo;
	}

}
