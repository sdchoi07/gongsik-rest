package com.gongsik.gsr.api.main.categories.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gongsik.gsr.api.account.join.entity.QAccountEntity;
import com.gongsik.gsr.api.main.categories.dto.CategoriesDto;
import com.gongsik.gsr.api.main.categories.dto.QCategoriesDto;
import com.gongsik.gsr.api.main.categories.entity.ChemistryEntity;
import com.gongsik.gsr.api.main.categories.entity.ProductEntity;
import com.gongsik.gsr.api.main.categories.entity.QCategoriesEntity;
import com.gongsik.gsr.api.main.categories.entity.QChemistryEntity;
import com.gongsik.gsr.api.main.categories.entity.QProductEntity;
import com.gongsik.gsr.api.main.categories.entity.QSeedEntity;
import com.gongsik.gsr.api.main.categories.entity.SeedEntity;
import com.gongsik.gsr.api.main.categories.repository.ChemistryRepository;
import com.gongsik.gsr.api.main.categories.repository.ProductRepository;
import com.gongsik.gsr.api.main.categories.repository.SeedRepository;
import com.gongsik.gsr.api.mypage.uscart.entity.CartEntity;
import com.gongsik.gsr.api.mypage.uscart.entity.QCartEntity;
import com.gongsik.gsr.api.mypage.uscart.repository.CartRepository;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CategoriesService {

	@Autowired
	EntityManager em;
	
	@Autowired
	CartRepository cartRepository;
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	SeedRepository seedRepository;
	
	@Autowired
	ChemistryRepository chemistryRepository;
	
	/* 카테고리 조회*/
	public Map<String, Object> categoriesListAll(Map<String, Object> request) {
		
		// 기본 셋팅하기
		String itemNo = request.get("itemkey").toString();
		String usrId = "";
		String orderNo = request.get("orderBy").toString();
		if(!"".equals(request.get("usrId")) && null != request.get("usrId")){
			usrId = request.get("usrId").toString();
		}
		QCategoriesEntity qCategoriesEntity = QCategoriesEntity.categoriesEntity;
		QCartEntity qCartEntity = QCartEntity.cartEntity;
		QAccountEntity qAccountEntity = QAccountEntity.accountEntity;
		QProductEntity qProductEntity = QProductEntity.productEntity;
		QSeedEntity qSeedEntity = QSeedEntity.seedEntity;
		QChemistryEntity qChemistryEntity = QChemistryEntity.chemistryEntity;
        
		//paging 처리
        int page = Integer.parseInt(request.get("currentPage").toString());
        int size = 12;
        int start = (page-1) * size;
		
		

		List<CategoriesDto> list = null;
		//아이템 분류에 따른 쿼리 조회
		if(itemNo.startsWith("1")) {
			list = selectQueryItem(itemNo, usrId, orderNo, page, start, size ,qCategoriesEntity ,qCartEntity ,qAccountEntity ,qProductEntity);
		}else if(itemNo.startsWith("2")){
			list = selectQueryItem(itemNo, usrId, orderNo, page, start, size ,qCategoriesEntity ,qCartEntity ,qAccountEntity ,qSeedEntity);
		}else {
			list = selectQueryItem(itemNo, usrId, orderNo, page, start, size ,qCategoriesEntity ,qCartEntity ,qAccountEntity ,qChemistryEntity);
		}
		
		Map<String,Object> map = new HashMap<>();
		log.info("result : {}" , list);
		log.info("cnt : {}" , list.size() );
		map.put("result", list);
		map.put("cnt", list.size());
		return map;
	}

	/* 약품 조회*/
	private List<CategoriesDto> selectQueryItem(String itemNo, String usrId, String orderNo, int page, int start,
			int size, QCategoriesEntity qCategoriesEntity, QCartEntity qCartEntity, QAccountEntity qAccountEntity,
			QChemistryEntity qChemistryEntity) {
		LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String cur = currentDateTime.format(formatter);
        
        
		JPAQueryFactory query = new JPAQueryFactory(em);
		OrderSpecifier orderSpecifier = createOrderSpecifier(orderNo, qCategoriesEntity, qChemistryEntity);
		List<CategoriesDto> list = query.select(
				new QCategoriesDto(
							 qCategoriesEntity.invenSClsNm
							,qCategoriesEntity.invenSClsNo
							,qChemistryEntity.chemistryPrice
							,qChemistryEntity.chemistrySalesCnt
							,qCategoriesEntity.invenCnt
							,qChemistryEntity.chemistryUrl
							,qCartEntity.delYn
							,qCartEntity.useYn
							)
				)
				.from(qCategoriesEntity)
				.join(qChemistryEntity).on(qCategoriesEntity.invenSClsNo.eq(qChemistryEntity.chemistryNo))
				.leftJoin(qCartEntity).on(qCategoriesEntity.invenSClsNo.eq(qCartEntity.cartItemNo))
				.leftJoin(qAccountEntity).on(qCartEntity.cartUsrId.eq(qAccountEntity.usrId).and(qAccountEntity.usrId.eq(usrId)))
				.where(qCategoriesEntity.invenMClsNo.eq(itemNo), qCategoriesEntity.crgDate.loe(cur), qCategoriesEntity.endDate.eq("99991231"))
				.orderBy(orderSpecifier)
				.offset(start)
			    .limit(page * size)
				.fetch();
		return list;
	}

	/* 씨앗 조회*/
	private List<CategoriesDto> selectQueryItem(String itemNo, String usrId, String orderNo, int page, int start,
			int size, QCategoriesEntity qCategoriesEntity, QCartEntity qCartEntity, QAccountEntity qAccountEntity,
			QSeedEntity qSeedEntity) {
		LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String cur = currentDateTime.format(formatter);
        
        
		JPAQueryFactory query = new JPAQueryFactory(em);
		OrderSpecifier orderSpecifier = createOrderSpecifier(orderNo, qCategoriesEntity, qSeedEntity);

		List<CategoriesDto> list = query.select(
				new QCategoriesDto(
							 qCategoriesEntity.invenSClsNm
							,qCategoriesEntity.invenSClsNo
							,qSeedEntity.seedPrice
							,qSeedEntity.seedSalesCnt
							,qCategoriesEntity.invenCnt
							,qSeedEntity.seedUrl
							,qCartEntity.delYn
							,qCartEntity.useYn
							)
				)
				.from(qCategoriesEntity)
				.join(qSeedEntity).on(qCategoriesEntity.invenSClsNo.eq(qSeedEntity.seedNo))
				.leftJoin(qCartEntity).on(qCategoriesEntity.invenSClsNo.eq(qCartEntity.cartItemNo))
				.leftJoin(qAccountEntity).on(qCartEntity.cartUsrId.eq(qAccountEntity.usrId).and(qAccountEntity.usrId.eq(usrId)))
				.where(qCategoriesEntity.invenMClsNo.eq(itemNo), qCategoriesEntity.crgDate.loe(cur), qCategoriesEntity.endDate.eq("99991231"))
				.orderBy(orderSpecifier)
				.offset(start)
			    .limit(page * size)
				.fetch();
		return list;
	}

	/* 구성품 조회*/
	private List<CategoriesDto> selectQueryItem(String itemNo, String usrId, String orderNo, int page, int start,
			int size, QCategoriesEntity qCategoriesEntity, QCartEntity qCartEntity, QAccountEntity qAccountEntity, QProductEntity qProductEntity) {
		
		LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String cur = currentDateTime.format(formatter);
        
        
		JPAQueryFactory query = new JPAQueryFactory(em);

		OrderSpecifier orderSpecifier = createOrderSpecifier(orderNo, qCategoriesEntity, qProductEntity);
		
		List<CategoriesDto> list = query.select(
				new QCategoriesDto(
							 qCategoriesEntity.invenSClsNm
							,qCategoriesEntity.invenSClsNo
							,qProductEntity.productPrice
							,qProductEntity.productSalesCnt
							,qCategoriesEntity.invenCnt
							,qProductEntity.productUrl
							,qCartEntity.delYn
							,qCartEntity.useYn
							)
				)
				.from(qCategoriesEntity)
				.join(qProductEntity).on(qCategoriesEntity.invenSClsNo.eq(qProductEntity.productNo))
				.leftJoin(qCartEntity).on(qCategoriesEntity.invenSClsNo.eq(qCartEntity.cartItemNo))
				.leftJoin(qAccountEntity).on(qCartEntity.cartUsrId.eq(qAccountEntity.usrId).and(qAccountEntity.usrId.eq(usrId)))
				.where(qCategoriesEntity.invenMClsNo.eq(itemNo), qCategoriesEntity.crgDate.loe(cur), qCategoriesEntity.endDate.eq("99991231"))
				.orderBy(orderSpecifier)
				.offset(start)
			    .limit(page * size)
				.fetch();
		return list;
	}
	
	/* 구성품 순서 */
	private OrderSpecifier createOrderSpecifier(String orderNo, QCategoriesEntity qCategoriesEntity, QProductEntity qProductEntity) {
		 return switch (orderNo) {
         case "1" -> new OrderSpecifier<>(Order.DESC,qCategoriesEntity.invenSClsNm);
         case "2" -> new OrderSpecifier<>(Order.ASC, qProductEntity.productPrice);
         case "3" -> new OrderSpecifier<>(Order.DESC, qProductEntity.productPrice);
         case "4" -> new OrderSpecifier<>(Order.DESC, qProductEntity.productSalesCnt );
         default -> new OrderSpecifier<>(Order.DESC, qCategoriesEntity.invenLClsNm);
     };
	}
	
	/* 씨앗 순서 */
	private OrderSpecifier createOrderSpecifier(String orderNo, QCategoriesEntity qCategoriesEntity,
			QSeedEntity qSeedEntity) {
		 return switch (orderNo) {
         case "1" -> new OrderSpecifier<>(Order.DESC,qCategoriesEntity.invenSClsNm);
         case "2" -> new OrderSpecifier<>(Order.ASC, qSeedEntity.seedPrice);
         case "3" -> new OrderSpecifier<>(Order.DESC, qSeedEntity.seedPrice);
         case "4" -> new OrderSpecifier<>(Order.DESC, qSeedEntity.seedSalesCnt);
         default -> new OrderSpecifier<>(Order.DESC, qCategoriesEntity.invenLClsNm);
		 };
	}
	
	/* 약품 순서 */
	private OrderSpecifier createOrderSpecifier(String orderNo, QCategoriesEntity qCategoriesEntity,
			QChemistryEntity qChemistryEntity) {
		return switch (orderNo) {
        case "1" -> new OrderSpecifier<>(Order.DESC,qCategoriesEntity.invenSClsNm);
        case "2" -> new OrderSpecifier<>(Order.ASC, qChemistryEntity.chemistryPrice);
        case "3" -> new OrderSpecifier<>(Order.DESC, qChemistryEntity.chemistryPrice);
        case "4" -> new OrderSpecifier<>(Order.DESC, qChemistryEntity.chemistrySalesCnt);
        default -> new OrderSpecifier<>(Order.DESC, qCategoriesEntity.invenLClsNm);
		 };
	}
	
	@Transactional
	public Map<String, Object> intoCart(Map<String, Object> request) {
		Map<String, Object> map = new HashMap<>();
		//기본 셋팅
		String cartItemNo = request.get("invenNo").toString();
		String usrId = request.get("usrId").toString();
		String useYn = "Y";
		String delYn = "N";
		String cartSt = "L";
		
		
		//해당 항목 조회
		if(cartItemNo.startsWith("1")){
			Optional<ProductEntity> productEntity = productRepository.findByProductNo(cartItemNo);
			//카트 테이블에 저장
			CartEntity cartEntity = new CartEntity();
			if(productEntity.isPresent()) {
				
				cartEntity.setCartItemNm(productEntity.get().getProductNm());
				cartEntity.setCartItemNo(productEntity.get().getProductNo());
				cartEntity.setCartSt(cartSt);
				cartEntity.setDelYn(delYn);
				cartEntity.setUseYn(useYn);
				cartEntity.setCartUsrId(usrId);
				
				cartRepository.save(cartEntity);
				map.put("code", "success");
				map.put("msg", "찜 리스트에 등록 되었습니다.");
			}
		}else if(cartItemNo.startsWith("2")) {
			Optional<SeedEntity> seedEntity = seedRepository.findBySeedNo(cartItemNo);
			//카트 테이블에 저장
			CartEntity cartEntity = new CartEntity();
			if(seedEntity.isPresent()) {
				cartEntity.setCartItemNm(seedEntity.get().getSeedNm());
				cartEntity.setCartItemNo(seedEntity.get().getSeedNo());
				cartEntity.setCartSt(cartSt);
				cartEntity.setDelYn(delYn);
				cartEntity.setUseYn(useYn);
				cartEntity.setCartUsrId(usrId);
				
				cartRepository.save(cartEntity);
				map.put("code", "success");
				map.put("msg", "찜 리스트에 등록 되었습니다.");
			}
		}else {
			Optional<ChemistryEntity> chemistryEntity = chemistryRepository.findByChemistryNo(cartItemNo);
			//카트 테이블에 저장
			CartEntity cartEntity = new CartEntity();
			if(chemistryEntity.isPresent()) {
				cartEntity.setCartItemNm(chemistryEntity.get().getChemistryNm());
				cartEntity.setCartItemNo(chemistryEntity.get().getChemistryNo());
				cartEntity.setCartSt(cartSt);
				cartEntity.setDelYn(delYn);
				cartEntity.setUseYn(useYn);
				cartEntity.setCartUsrId(usrId);
				
				cartRepository.save(cartEntity);
				map.put("code", "success");
				map.put("msg", "찜 리스트에 등록 되었습니다.");
			}
		}
		
		
		return map;
	}

}
