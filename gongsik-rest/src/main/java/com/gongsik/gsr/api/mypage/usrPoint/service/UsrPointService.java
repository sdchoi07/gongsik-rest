package com.gongsik.gsr.api.mypage.usrPoint.service;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gongsik.gsr.api.main.categories.entity.QChemistryEntity;
import com.gongsik.gsr.api.main.categories.entity.QProductEntity;
import com.gongsik.gsr.api.main.categories.entity.QSeedEntity;
import com.gongsik.gsr.api.mypage.usrPoint.dto.QUsrPointDto;
import com.gongsik.gsr.api.mypage.usrPoint.dto.UsrPointDto;
import com.gongsik.gsr.api.mypage.usrPoint.entity.QUsrPointEntity;
import com.gongsik.gsr.api.mypage.usrPoint.repository.UsrPointRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UsrPointService {
	
	@Autowired
	EntityManager em;
	
	@Autowired
	UsrPointRepository usrPointRepositry;

	public Map<String, Object> pointList(Map<String, Object> request) {
		//기본 셋팅
		String usrId = request.get("usrId").toString();
		String pointSt = "";
		if (request.get("pointSt") != null && !request.get("pointSt").equals("")) {
			pointSt = request.get("pointSt").toString();
		}
		String searchDate = "";
		
		if (request.get("searchDate") != null && !request.get("searchDate").equals("")) {
			searchDate = request.get("searchDate").toString();
		}
		//날짜 구하기
		String searchDt = getDate(searchDate);
		LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String expireDt = currentDateTime.format(formatter);
		
        //page
        int page = Integer.parseInt(request.get("currentPage").toString());
        int size = 10;
		
		Map<String, Object> map = new HashMap<>();
		QUsrPointEntity qUsrPointEntity = QUsrPointEntity.usrPointEntity;
		JPAQueryFactory query = new JPAQueryFactory(em);
			
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

		
		log.info("total : {}" , total );
		total = (total != null) ? total : 0;
		DecimalFormat krFormat = new DecimalFormat("###,###");
		String totalPoint = krFormat.format(total);
//		if(total <=0) {
//			total = 0;
//		}
		//분류 상태가 전체일경우
		BooleanBuilder builder = new BooleanBuilder();
		builder.and(qUsrPointEntity.pointExpireDt.goe(expireDt))
		       .and(qUsrPointEntity.pointUsrId.eq(usrId))
		       .and(qUsrPointEntity.pointCrtDt.goe(searchDt).or(qUsrPointEntity.pointUsedDt.goe(searchDt)));
		
		List<UsrPointDto> result = null;
		//분류 상태가 사용 또는 적립일 경우
		if (!"A".equals(pointSt)) {
		    builder.and(qUsrPointEntity.pointSt.eq(pointSt));
		}
		//포인트 내역 조회 
		result = query.select(
				new QUsrPointDto(
				new CaseBuilder().when(qUsrPointEntity.pointSt.eq("S")).then(Expressions.stringTemplate("CONCAT('+', {0})",qUsrPointEntity.pointPt)).otherwise(Expressions.stringTemplate("CONCAT('- ', {0})",qUsrPointEntity.pointPt)).as("POINT_PT"),
		        qUsrPointEntity.pointSt,
		        new CaseBuilder().when(qUsrPointEntity.pointSt.eq("S")).then("적립").otherwise("사용").as("POINT_ST_NM"),
		        new CaseBuilder().when(qUsrPointEntity.pointSt.eq("S")).then(qUsrPointEntity.pointCrtDt).otherwise(qUsrPointEntity.pointUsedDt).as("POINT_DT")
		        )
			)
		    .from(qUsrPointEntity)
		    .where(builder)
		    .orderBy(qUsrPointEntity.pointCrtDt.desc())
		    .offset(0)
		    .limit(page * size)
		    .fetch();
		
		log.info("result : {}" , result );
		
		map.put("total", totalPoint);
		map.put("result", result);
		return map;
	}

	public String getDate(String pointCode) {
		// 현재 날짜 및 시간 가져오기
        LocalDateTime currentDateTime = LocalDateTime.now();
        String expiredDt = "";
        LocalDateTime pastDate = currentDateTime.minusMonths(1);
        
        if("1".equals(pointCode)) {//1달
        	 pastDate = currentDateTime.minusMonths(1);
        	 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        	 expiredDt = pastDate.format(formatter);
        	return  expiredDt; 
        }else if("2".equals(pointCode)) {//3달
        	pastDate = currentDateTime.minusMonths(3);
	       	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	       	expiredDt = pastDate.format(formatter);
        	return expiredDt;
        }else if("3".equals(pointCode)) {//6달
        	pastDate = currentDateTime.minusMonths(6);
	       	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	       	expiredDt = pastDate.format(formatter);
        	return expiredDt;
        }else { //1년
        	pastDate = currentDateTime.minusYears(1);
	       	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	       	expiredDt = pastDate.format(formatter);
        	return expiredDt;
        }
        
        
	}
}
