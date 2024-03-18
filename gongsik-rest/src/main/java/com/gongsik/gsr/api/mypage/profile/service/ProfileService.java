package com.gongsik.gsr.api.mypage.profile.service;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gongsik.gsr.api.account.join.entity.AuthSMSEntity;
import com.gongsik.gsr.api.account.join.entity.AuthSMSHistEntity;
import com.gongsik.gsr.api.account.join.repository.AuthSMSHistRepository;
import com.gongsik.gsr.api.account.join.repository.AuthSMSRepository;
import com.gongsik.gsr.api.mypage.profile.dto.ProfileDto;
import com.gongsik.gsr.api.mypage.profile.entity.ProfileEntity;
import com.gongsik.gsr.api.mypage.profile.repository.ProfileRepository;
import com.gongsik.gsr.api.mypage.usrGrade.entity.UsrGradeEntity;
import com.gongsik.gsr.api.mypage.usrGrade.repository.UsrGradeRepository;
import com.gongsik.gsr.api.mypage.usrPoint.entity.QUsrPointEntity;
import com.gongsik.gsr.global.vo.ResultVO;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileService {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Autowired
	private ProfileRepository profileepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private AuthSMSRepository authSMSRepository;
	
	@Autowired
	private AuthSMSHistRepository authSMSHistRepository;
	
	@Autowired
	private UsrGradeRepository usrGradeRepository;
	
	@Autowired
	EntityManager em;
	
	/* 회원 정보 조회 */
	public Map<String, Object> profileList(Map<String, String> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ResultVO resultVo = new ResultVO();
		String usrId = map.get("usrId");
		String logTp = map.get("logTp");
		Optional<ProfileEntity> list = profileepository.findByUsrIdAndLogTp(usrId, logTp);
	    ProfileDto data = list.map(profile -> {
	        ProfileDto resultDto = new ProfileDto();
	        resultDto.setUsrNm(profile.getUsrNm());
	        resultDto.setUsrId(profile.getUsrId());
	        resultDto.setLogTp(profile.getLogTp());
	        resultDto.setUsrNo(profile.getUsrNo());
	        resultDto.setUsrPhone(profile.getUsrPhone());
	        resultDto.setUsrSex(profile.getUsrSex());
	        resultDto.setCountryPh(profile.getCountryPh());
	        resultDto.setChatYn(profile.getChatYn());
	        return resultDto;
	        
	    }).orElse(null);
	    Optional<UsrGradeEntity> usrGradeEntity = usrGradeRepository.findByGradeUsrId(usrId);
	    if(usrGradeEntity.isPresent()) {
	    	String usrGrdLevel = usrGradeEntity.get().getGradeLevel();
	    	resultMap.put("usrGrdLevel", usrGrdLevel);
	    }
	    resultMap.put("result", data);
		return resultMap;
	}
	
	/* 회원 수정 */
	@Transactional
	public ResponseEntity<ResultVO> accountModify(Map<String, String> map) {
		ResultVO resultVo = new ResultVO();
		

		try {

			// 가입된 확인인지 체크
			ProfileEntity result = null;
			Optional<ProfileEntity> profileAccount = profileepository.findByUsrId(map.get("usrId"));
			// 웹서버가져온 데이터 entity 옮기기
			ProfileEntity profileEntity = profileAccount.get();
			if (profileAccount.isEmpty()) {
				resultVo.setCode("fail");
				resultVo.setMsg("프로필 수정 다시 요청 드립니다.");
				return ResponseEntity.status(HttpStatus.OK).body(resultVo);
			}
			// 비빌번호 변경 여부 확인
			String rawPassowrd = map.get("password");
			// 인증번호 redis 저장된 값 가져오기
			String redisAuthNo = "";
			String redisPhoneNumber = "";
			if(!"".equals(rawPassowrd) && rawPassowrd != null){
				redisAuthNo = redisTemplate.opsForValue().get("authNo");
				redisPhoneNumber = redisTemplate.opsForValue().get("phoneNumber");
				if (!"".equals(redisPhoneNumber) && !redisPhoneNumber.equals(map.get("phoneNumber"))) {
					resultVo.setCode("fail");
					resultVo.setMsg("다시 인증 요청 해주세요.");
					redisTemplate.delete(redisTemplate.keys("authNo"));
					redisTemplate.delete(redisTemplate.keys("phoneNumber"));
					return ResponseEntity.status(HttpStatus.OK).body(resultVo);
	
				}
				if (!redisAuthNo.equals(map.get("authNo")) && !"".equals(redisAuthNo)) { // 인증번호 다를경우 오류 메세지
					resultVo.setCode("fail");
					resultVo.setMsg("인증번호가 틀렸습니다. 다시 인증 요청 해주세요.");
					redisTemplate.delete(redisTemplate.keys("authNo"));
					redisTemplate.delete(redisTemplate.keys("phoneNumber"));
					return ResponseEntity.status(HttpStatus.OK).body(resultVo);
				}
				
				// 암호화
				if(!"".equals(rawPassowrd)|| rawPassowrd != null){
					String encPassword = bCryptPasswordEncoder.encode(rawPassowrd);
					profileEntity.setUsrPwd(encPassword);
				}
			} 
				
				profileEntity.setUsrNm(map.get("usrNm"));
				profileEntity.setUsrSex(map.get("gender"));
				profileEntity.setUsrNo(map.get("birthDate"));
				profileEntity.setUsrPhone(map.get("phoneNumber"));
				profileEntity.setChatYn(map.get("chatYn"));
				// 회원가입 등록
				result = profileepository.save(profileEntity);
				
				if (result != null) {

					// 문자인증 받은 번호로 select
					Optional<AuthSMSEntity> selectOne = authSMSRepository.findByUsrPhNo(map.get("phoneNumber"));
					if (selectOne.isPresent()) { // 인증요청 한 휴대폰 번호 update
						int resultHist = authHistInsert(selectOne);

						if (resultHist == 1) {
							// 완료시 redis에 저장된 값 삭제
							redisTemplate.delete(redisTemplate.keys("authNo"));
							redisTemplate.delete(redisTemplate.keys("phoneNumber"));
							resultVo.setCode("success");
							resultVo.setMsg("프로필 수정이 완료 되었습니다.");
						} else {
							resultVo.setCode("fail");
							resultVo.setMsg("프로필 수정이 실패 하였습니다.");
						}
					}
				}
			
		} catch (Exception e) {
			e.printStackTrace();
			resultVo.setCode("error");
			resultVo.setMsg("error");
		}

		return ResponseEntity.ok(resultVo);
	}
	
	/* 회원 포인트 */
	public Map<String, Object> pointPt(Map<String, String> request) {
		Map<String, Object> map = new HashMap<>();
		//기본 셋팅
		String usrId = request.get("usrId").toString();
		JPAQueryFactory query = new JPAQueryFactory(em);
		QUsrPointEntity qUsrPointEntity = QUsrPointEntity.usrPointEntity;
		LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String expireDt = currentDateTime.format(formatter);
        
		//총 포인트 구하기
		Integer total = 
				query
	            .select(
	                    Expressions.numberTemplate(Integer.class,
	                            "SUM(CASE WHEN {0} = 'S' THEN {1} ELSE 0 END) - " +
	                            "SUM(CASE WHEN {0} = 'U' THEN {1} ELSE 0 END)",
	                            qUsrPointEntity.pointSt, qUsrPointEntity.pointPt)
	            )
	            .from(qUsrPointEntity)
	            .where(qUsrPointEntity.pointExpireDt.goe(expireDt).and(qUsrPointEntity.pointUsrId.eq(usrId)))
	            .fetchOne();
		total = (total != null) ? total : 0;
		DecimalFormat krFormat = new DecimalFormat("###,###");
		String totalPoint = krFormat.format(total);
		map.put("poinTotal", totalPoint);
		return map;
	}
	
	
	/* 인증요청 이력 */
	public int authHistInsert(Optional<AuthSMSEntity> selectOne) {
		//회원가입 등록 성공시
		AuthSMSEntity resultAuth = null;
		selectOne.get().setAuthYn("Y");
		LocalDateTime date = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formatterDate = date.format(formatter);
		selectOne.get().setConfDt(formatterDate);
		resultAuth = authSMSRepository.save(selectOne.get());
		
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
			authSMSHistRepository.save(authSMSHistEntity);
			return 1;
		}
		return -1;
	}


}
