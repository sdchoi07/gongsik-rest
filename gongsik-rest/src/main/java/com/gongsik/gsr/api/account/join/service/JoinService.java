package com.gongsik.gsr.api.account.join.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gongsik.gsr.api.account.join.dto.JoinDto;
import com.gongsik.gsr.api.account.join.entity.AccountEntity;
import com.gongsik.gsr.api.account.join.entity.AccountMultiKey;
import com.gongsik.gsr.api.account.join.entity.AuthSMSEntity;
import com.gongsik.gsr.api.account.join.entity.AuthSMSHistEntity;
import com.gongsik.gsr.api.account.join.repository.AccountRepository;
import com.gongsik.gsr.api.account.join.repository.AuthSMSHistRepository;
import com.gongsik.gsr.api.account.join.repository.AuthSMSRepository;
import com.gongsik.gsr.api.account.join.repository.InternationalPhoneNumberRepository;
import com.gongsik.gsr.global.vo.ResultVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JoinService {
	
	private final Logger log =LoggerFactory.getLogger(JoinService.class);
	
	@Autowired
	private InternationalPhoneNumberRepository internationalPhoneNumberRepository;
	
	@Autowired
	private AuthSMSRepository authSMSRepository;
	
	@Autowired
	private AuthSMSHistRepository authSMSHistRepository;

	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
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
	
	
	/* 이메일 중복 체크  */
	public ResultVO selectChkusrId(String usrId) {
		ResultVO  resultVo = new ResultVO();
		long result = accountRepository.countByUsrId(usrId);
		if(result == 0) {
			resultVo.setCode("success");
			resultVo.setMsg("사용 할 수 있는 아이디 입니다.");
		}else {
			resultVo.setCode("fail");
			resultVo.setMsg("이미 사용중인 아이디 입니다.");
		}
		return resultVo;
	}
	
	/* 인증번호 저장 */
	@Transactional
	public ResultVO authNoSave(JoinDto joinDto) {
		ResultVO resultVo = new ResultVO();
		
		//기존 redis 정장된 값 삭제
		redisTemplate.delete(redisTemplate.keys("authNo"));
		//인증번호 redis 저장
		redisTemplate.opsForValue().set("authNo", joinDto.getAuthNo());
		
		AuthSMSEntity result = null;
		try {
				//해당 아이디로 기존에 인증요청 확인
				Optional<AuthSMSEntity> selectOne = authSMSRepository.findByUsrPhNo(joinDto.getUsrPhNo());
				if(selectOne.isPresent()) { //인증요청 한 아이디일경우 update
					selectOne.get().setReReqAuthCnt(selectOne.get().getReReqAuthCnt()+1 );
					selectOne.get().setReqDt(joinDto.getReqDt().toString());
					selectOne.get().setAuthNo(joinDto.getAuthNo());
					result = authSMSRepository.save(selectOne.get());
				}else { //인증 요청 처음 일 경우 insert
				
					AuthSMSEntity insertAuthSMSEntity = new AuthSMSEntity();
					
					//회원정보 authSMSEntity 저장
					insertAuthSMSEntity.setAuthNo(joinDto.getAuthNo());
					insertAuthSMSEntity.setAuthType(joinDto.getAuthType());
					insertAuthSMSEntity.setAuthYn(joinDto.getAuthYn());
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
				log.info("result :{} " , result);
						//auth_sms_inf 테이블에 저장 되었을 경우 hist 저장 시작 
						if(result != null) {
							//authSMsHistEntity 저장
							AuthSMSHistEntity authSMSHistEntity = new AuthSMSHistEntity();
							authSMSHistEntity.setAuthNo(result.getAuthNo());
							authSMSHistEntity.setAuthType(result.getAuthType());
							authSMSHistEntity.setAuthYn(result.getAuthYn());
							authSMSHistEntity.setUsrPhNo(result.getUsrPhNo());
							authSMSHistEntity.setReReqAuthCnt(result.getReReqAuthCnt());
							authSMSHistEntity.setCountryPh(result.getCountryPh());
							authSMSHistEntity.setReqDt(result.getReqDt());
							//auth_sms_inf_hist 테이블에 저장
							resultHist = authSMSHistRepository.save(authSMSHistEntity);
							
						}else { //auth_sms_inf 테이블에 저장 안됬을경우
							resultVo.setCode("fail");
							resultVo.setMsg("AUTH_SMS_INF 저장 실패");
							return resultVo;
						}
					//auth_sms_inf_hist 테이블에 저장 되었을경우
					if(resultHist != null) {
						resultVo.setCode("success");
						resultVo.setMsg("AUTH_SMS_INF 저장 성공");
						return resultVo;
					}else {//auth_sms_inf_hist 테이블에 저장 실패 일 경우
						resultVo.setCode("fail");
						resultVo.setMsg("AUTH_SMS_INF_hist 저장 실패");
						return resultVo;
					}
				
		}catch(Exception e){
				resultVo.setCode("fail");
				resultVo.setMsg("전체 오류");
		}
		return resultVo;
	}
	
	/* 회원가입  */
	@Transactional
	public ResponseEntity<ResultVO> joinForm(Map<String,String> map) {
		ResultVO resultVo = new ResultVO();
		//입력한 정보가 잘못되어 있는지 검증
		joinFormVaild(map);
		
		try {
			
			//가입된 확인인지 체크 
			AccountEntity result = null;
			Optional<AccountEntity> joinedUsrId = accountRepository.findByUsrId(map.get("usrId"));
			if(joinedUsrId.isPresent()) {
				resultVo.setCode("fail");
				resultVo.setMsg("이미 가입된 회원 입니다.");
				return ResponseEntity.status(HttpStatus.OK).body(resultVo);
			}
				//인증번호 redis 저장된 값 가져오기
				String redisAuthNo = redisTemplate.opsForValue().get("authNo");
				if(!redisAuthNo.equals(map.get("authNo")) && !"".equals(redisAuthNo)){ //인증번호 다를경우 오류 메세지
					resultVo.setCode("fail");
					resultVo.setMsg("인증번호가 틀렸습니다. 다시 인증 요청 해주세요.");
					return ResponseEntity.status(HttpStatus.OK).body(resultVo);
				
				}else {
			
					//웹서버가져온 데이터 entity 옮기기
					AccountEntity accountEntity = new AccountEntity();
					
					accountEntity.setUsrId(map.get("usrId"));
					//암호화
					String rawPassowrd = map.get("password");
					String encPassword = bCryptPasswordEncoder.encode(rawPassowrd);
					
					accountEntity.setUsrPwd(encPassword);
					accountEntity.setUsrNm(map.get("usrNm"));
					accountEntity.setUsrSex(map.get("gender"));
					accountEntity.setUsrNo(map.get("birthDate"));
					accountEntity.setCountryPh(map.get("countryPhNo"));
					accountEntity.setUsrPhone(map.get("phoneNumber"));
					accountEntity.setUsrGrade("1");
					accountEntity.setUsrStatus("");
					accountEntity.setUsrRole("USER");
					//회원가입 등록
					result = accountRepository.save(accountEntity);
					
					if(result != null) {
						//회원가입 등록 성공시
						AuthSMSEntity resultAuth = null;
						//문자인증 받은 번호로 select
						Optional<AuthSMSEntity> selectOne = authSMSRepository.findByUsrPhNo(map.get("phoneNumber"));
						if(selectOne.isPresent()) { //인증요청 한 휴대폰 번호 update
							selectOne.get().setAuthYn("Y");
							LocalDateTime date = LocalDateTime.now();
							DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
							String formatterDate = date.format(formatter);
							selectOne.get().setConfDt(formatterDate);
							resultAuth = authSMSRepository.save(selectOne.get());
							
							AuthSMSHistEntity resultHist = null;
							if(resultAuth != null) {
								//authSMsHistEntity 저장
								AuthSMSHistEntity authSMSHistEntity = new AuthSMSHistEntity();
								authSMSHistEntity.setAuthNo(resultAuth.getAuthNo());
								authSMSHistEntity.setAuthType(resultAuth.getAuthType());
								authSMSHistEntity.setAuthYn(resultAuth.getAuthYn());
								authSMSHistEntity.setUsrPhNo(resultAuth.getUsrPhNo());
								authSMSHistEntity.setReReqAuthCnt(resultAuth.getReReqAuthCnt());
								authSMSHistEntity.setCountryPh(resultAuth.getCountryPh());
								authSMSHistEntity.setConfDt(resultAuth.getConfDt());
								//auth_sms_inf_hist 테이블에 저장
								resultHist = authSMSHistRepository.save(authSMSHistEntity);
							}
						
								if(resultHist != null) {
									//완료시 redis에 저장된 값 삭제
									redisTemplate.delete(redisTemplate.keys("authNo"));
									resultVo.setCode("success");
									resultVo.setMsg("회원가입 완료되었습니다.");
								}else {
									resultVo.setCode("fail");
									resultVo.setMsg("회원가입이 실패 하였습니다.");
								}
							}
					}
				}
			}catch(Exception e) {
				e.printStackTrace();
				resultVo.setCode("error");
				resultVo.setMsg("error");
			}
			
			return ResponseEntity.ok(resultVo) ;
		}
	
	/* 회원가입시 검증 체크 */
	public ResponseEntity<ResultVO> joinFormVaild(Map<String,String> map) {
		ResultVO resultVo = new ResultVO();
		String usrId = map.get("usrId");
		String usrPwd = map.get("password");
		String countryPh = map.get("countryPhNo");
		String usrPhNo = map.get("phoneNumber");
		String usrNo = map.get("usrNo");
		if("".equals(usrId)) {
			resultVo.setCode("fail");
			resultVo.setMsg("이메일 다시 한 번 확인 해주세요.");
			return ResponseEntity.status(HttpStatus.OK).body(resultVo);
		}
		if("".equals(usrPwd)) {
			resultVo.setCode("fail");
			resultVo.setMsg("비밀번호를 다시 입력 해주세요.");
			return ResponseEntity.status(HttpStatus.OK).body(resultVo);
		}
		if("".equals(usrNo)) {
			resultVo.setCode("fail");
			resultVo.setMsg("생년월일 다시 입력 해주세요.");
			return ResponseEntity.status(HttpStatus.OK).body(resultVo);
		}
		if("".equals(countryPh)) {
			resultVo.setCode("fail");
			resultVo.setMsg("국제번호를 다시 선택 해주세요.");
			return ResponseEntity.status(HttpStatus.OK).body(resultVo);
		}
		if("".equals(usrPhNo)) {
			resultVo.setCode("fail");
			resultVo.setMsg("휴대폰 번호를 다시 입력 해주세요.");
			return ResponseEntity.status(HttpStatus.OK).body(resultVo);
		}
		return null;
		
	}
}
