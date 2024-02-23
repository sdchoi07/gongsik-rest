package com.gongsik.gsr.api.main.categories.service;

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
import org.springframework.transaction.annotation.Transactional;

import com.gongsik.gsr.api.account.join.entity.AccountEntity;
import com.gongsik.gsr.api.account.join.repository.AccountRepository;
import com.gongsik.gsr.api.main.categories.dto.CategoriesDto;
import com.gongsik.gsr.api.main.categories.entity.CategoriesEntity;
import com.gongsik.gsr.api.main.categories.entity.ChemistryEntity;
import com.gongsik.gsr.api.main.categories.entity.ProductEntity;
import com.gongsik.gsr.api.main.categories.entity.SeedEntity;
import com.gongsik.gsr.api.main.categories.repository.CategoriesRepository;
import com.gongsik.gsr.api.main.categories.repository.ChemistryRepository;
import com.gongsik.gsr.api.main.categories.repository.ProductRepository;
import com.gongsik.gsr.api.main.categories.repository.SeedRepository;
import com.gongsik.gsr.api.mypage.uscart.entity.CartEntity;
import com.gongsik.gsr.api.mypage.uscart.repository.CartRepository;
import com.gongsik.gsr.global.vo.ResultVO;

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

	@Autowired
	CategoriesRepository categoriesRepository;

	@Autowired
	AccountRepository accountRepository;

	/* 카테고리 조회 */
	public Map<String, Object> categoriesListAll(Map<String, Object> request) {

		// 기본 셋팅하기
		String itemNo = request.get("itemkey").toString();
		String endDate = "99991231";
		String usrId = "";
		String orderNo = request.get("orderBy").toString();
		if (!"".equals(request.get("usrId")) && null != request.get("usrId")) {
			usrId = request.get("usrId").toString();
		}

		// paging 처리
		int page = Integer.parseInt(request.get("currentPage").toString());
		int size = 12;
		int start = page;

		List<CategoriesDto> list = null;
		// 아이템 분류에 따른 쿼리 조회
		if (itemNo.startsWith("1")) {
			list = selectQueryProduct(itemNo, usrId, orderNo, page, start, size, endDate);
		} else if (itemNo.startsWith("2")) {
			list = selectQuerySeed(itemNo, usrId, orderNo, page, start, size, endDate);
		} else {
			list = selectQueryChemistry(itemNo, usrId, orderNo, page, start, size, endDate);
		}

		Map<String, Object> map = new HashMap<>();
		log.info("result : {}", list);
		log.info("cnt : {}", list.size());
		map.put("result", list);
		map.put("cnt", list.size());
		return map;
	}

	/* 약품 조회 */
	private List<CategoriesDto> selectQueryChemistry(String itemNo, String usrId, String orderNo, int page, int start,
			int size, String endDate) {
		LocalDateTime currentDateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String cur = currentDateTime.format(formatter);

		Pageable pageable;
		// order by 순서
		switch (orderNo) {
		case "1" -> pageable = PageRequest.of((start - 1), size, Sort.by("INVEN_S_CLS_NM").descending());
		case "2" -> pageable = PageRequest.of((start - 1), size, Sort.by("D.CHEMISTRY_PRICE").ascending());
		case "3" -> pageable = PageRequest.of((start - 1), size, Sort.by("D.CHEMISTRY_PRICE").descending());
		case "4" -> pageable = PageRequest.of((start - 1), size, Sort.by("D.CHEMISTRY_SALES_CNT").descending());
		default -> pageable = PageRequest.of((start - 1), size, Sort.by("INVEN_L_CLS_NM").descending());
		}

		// 조회
		List<Object[]> entityList = categoriesRepository.findAllChemistry(usrId, itemNo, cur, endDate, pageable);

		ArrayList<CategoriesDto> list = new ArrayList<>();
		for (Object[] result : entityList) {
			CategoriesDto categoriesDto = new CategoriesDto();
			categoriesDto.setInvenSClsNm(result[0].toString());
			categoriesDto.setInvenSClsNo(result[1].toString());

			int price = Integer.parseInt(result[2].toString());
			DecimalFormat krFormat = new DecimalFormat("###,###원");
			String cartPrice = krFormat.format(price);
			categoriesDto.setInvenPrice(cartPrice);

			categoriesDto.setInvenSaelsCnt(Integer.parseInt(result[3].toString()));
			categoriesDto.setInvenCnt(Integer.parseInt(result[4].toString()));
			categoriesDto.setInvenUrl(result[5].toString());
			if (result[6] != null) {
				categoriesDto.setDelYn(result[6].toString());
			}
			if (result[7] != null) {
				categoriesDto.setUseYn(result[7].toString());
			}
			list.add(categoriesDto);
		}
		return list;
	}

	/* 씨앗 조회 */
	private List<CategoriesDto> selectQuerySeed(String itemNo, String usrId, String orderNo, int page, int start,
			int size, String endDate) {
		LocalDateTime currentDateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String cur = currentDateTime.format(formatter);

		Pageable pageable;
		// order by 순서
		switch (orderNo) {
		case "1" -> pageable = PageRequest.of((start - 1), size, Sort.by("INVEN_S_CLS_NM").descending());
		case "2" -> pageable = PageRequest.of((start - 1), size, Sort.by("D.SEED_PRICE").ascending());
		case "3" -> pageable = PageRequest.of((start - 1), size, Sort.by("D.SEED_PRICE").descending());
		case "4" -> pageable = PageRequest.of((start - 1), size, Sort.by("D.SEED_SALES_CNT").descending());
		default -> pageable = PageRequest.of((start - 1), size, Sort.by("INVEN_L_CLS_NM").descending());
		}

		// 조회
		List<Object[]> entityList = categoriesRepository.findAllSeed(usrId, itemNo, cur, endDate, pageable);

		ArrayList<CategoriesDto> list = new ArrayList<>();
		for (Object[] result : entityList) {
			CategoriesDto categoriesDto = new CategoriesDto();
			categoriesDto.setInvenSClsNm(result[0].toString());
			categoriesDto.setInvenSClsNo(result[1].toString());

			int price = Integer.parseInt(result[2].toString());
			DecimalFormat krFormat = new DecimalFormat("###,###원");
			String cartPrice = krFormat.format(price);
			categoriesDto.setInvenPrice(cartPrice);

			categoriesDto.setInvenSaelsCnt(Integer.parseInt(result[3].toString()));
			categoriesDto.setInvenCnt(Integer.parseInt(result[4].toString()));
			categoriesDto.setInvenUrl(result[5].toString());
			if (result[6] != null) {
				categoriesDto.setDelYn(result[6].toString());
			}
			if (result[7] != null) {
				categoriesDto.setUseYn(result[7].toString());
			}
			list.add(categoriesDto);
		}
		return list;
	}

	/* 구성품 조회 */
	private List<CategoriesDto> selectQueryProduct(String itemNo, String usrId, String orderNo, int page, int start,
			int size, String endDate) {

		LocalDateTime currentDateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String cur = currentDateTime.format(formatter);

		Pageable pageable;
		// order by 순서
		switch (orderNo) {
		case "1" -> pageable = PageRequest.of((start - 1), size, Sort.by("INVEN_S_CLS_NM").descending());
		case "2" -> pageable = PageRequest.of((start - 1), size, Sort.by("D.PRODUCT_PRICE").ascending());
		case "3" -> pageable = PageRequest.of((start - 1), size, Sort.by("D.PRODUCT_PRICE").descending());
		case "4" -> pageable = PageRequest.of((start - 1), size, Sort.by("D.PRODUCT_SALES_CNT").descending());
		default -> pageable = PageRequest.of((start - 1), size, Sort.by("INVEN_L_CLS_NM").descending());
		}

		// 조회
		List<Object[]> entityList = categoriesRepository.findAllProduct(usrId, itemNo, cur, endDate, pageable);

		ArrayList<CategoriesDto> list = new ArrayList<>();
		for (Object[] result : entityList) {
			CategoriesDto categoriesDto = new CategoriesDto();
			categoriesDto.setInvenSClsNm(result[0].toString());
			categoriesDto.setInvenSClsNo(result[1].toString());

			int price = Integer.parseInt(result[2].toString());
			DecimalFormat krFormat = new DecimalFormat("###,###원");
			String cartPrice = krFormat.format(price);
			categoriesDto.setInvenPrice(cartPrice);

			categoriesDto.setInvenSaelsCnt(Integer.parseInt(result[3].toString()));
			categoriesDto.setInvenCnt(Integer.parseInt(result[4].toString()));
			categoriesDto.setInvenUrl(result[5].toString());
			if (result[6] != null) {
				categoriesDto.setDelYn(result[6].toString());
			}
			if (result[7] != null) {
				categoriesDto.setUseYn(result[7].toString());
			}
			list.add(categoriesDto);
		}

//		List<CategoriesDto> list = query.select(
//				new QCategoriesDto(
//						 qCategoriesEntity.invenSClsNm
//						,qCategoriesEntity.invenSClsNo
//						,qProductEntity.productPrice
//						,qProductEntity.productSalesCnt
//						,qCategoriesEntity.invenCnt
//						,qProductEntity.productUrl
//						,qCartEntity.delYn
//						,qCartEntity.useYn
//						)
//			)
//			.from(qCategoriesEntity)
//			.join(qProductEntity).on(qCategoriesEntity.invenSClsNo.eq(qProductEntity.productNo))
//			.leftJoin(
//				        JPAExpressions.select(qCartEntity.useYn, qCartEntity.delYn, qCartEntity.cartItemNo, qCartEntity.cartUsrId)
//				            .from(qCartEntity)
//				            .join(qAccountEntity)
//				            .on(qCartEntity.cartUsrId.eq(qAccountEntity.usrId).and(qAccountEntity.usrId.eq(usrId)))
//				    ).as(qCartEntity)
//			.on(qCategoriesEntity.invenSClsNo.eq(qCartEntity.cartItemNo))
//			.where(qCategoriesEntity.invenMClsNo.eq(itemNo), qCategoriesEntity.crgDate.loe(cur), qCategoriesEntity.endDate.eq("99991231"))
//			.orderBy(orderSpecifier)
//			.offset(start)
//		    .limit(page * size)
//			.fetch();
		return list;
	}

	@Transactional
	public Map<String, Object> intoCart(Map<String, Object> request) {
		Map<String, Object> map = new HashMap<>();
		// 기본 셋팅
		String cartItemNo = request.get("invenNo").toString();
		String usrId = request.get("usrId").toString();
		String useYn = "";
		String delYn = "";
		String cartSt = request.get("cartSt").toString();
		if ("Y".equals(request.get("useYn"))) {
			useYn = "N";
			delYn = "Y";
			// 카트 테이블에 저장
			Optional<CartEntity> entity = cartRepository
					.findByCartItemNoAndCartUsrIdAndUseYnAndDelYn(cartItemNo, usrId, "Y", "N");
			if (entity.isPresent()) {
				CartEntity cartEntity = entity.get();
				String st = entity.get().getCartSt();
				if("A".equals(st)) {
					cartEntity.setCartSt("W");
				}else {
					cartEntity.setDelYn(delYn);
					cartEntity.setUseYn(useYn);
				}
				cartRepository.save(cartEntity);
				map.put("code", "success");
				map.put("msg", "찜 등록 취소 되었습니다.");
			}
		} else {
			// cartNo 최신 값 가져오기
			int cartNo = cartRepository.findOne();
			useYn = "Y";
			delYn = "N";
			// 해당 항목 조회
			if (cartItemNo.startsWith("1")) {
				Optional<ProductEntity> productEntity = productRepository.findByProductNo(cartItemNo);
				// 카트 테이블에 저장
				CartEntity cartEntity = new CartEntity();
				if (productEntity.isPresent()) {

					cartEntity.setCartItemNm(productEntity.get().getProductNm());
					cartEntity.setCartItemNo(productEntity.get().getProductNo());
					cartEntity.setCartSt(cartSt);
					cartEntity.setDelYn(delYn);
					cartEntity.setUseYn(useYn);
					cartEntity.setCartUsrId(usrId);
					cartEntity.setCartNo(cartNo + 1);
					if("W".equals(cartSt)) {
						int count = Integer.parseInt(request.get("count").toString());
						cartEntity.setCartItemCnt(count);
					}

					cartRepository.save(cartEntity);
					map.put("code", "success");
					map.put("msg", "장바구니 리스트에 등록 되었습니다.");
				}
			} else if (cartItemNo.startsWith("2")) {
				Optional<SeedEntity> seedEntity = seedRepository.findBySeedNo(cartItemNo);
				// 카트 테이블에 저장
				CartEntity cartEntity = new CartEntity();
				if (seedEntity.isPresent()) {
					cartEntity.setCartItemNm(seedEntity.get().getSeedNm());
					cartEntity.setCartItemNo(seedEntity.get().getSeedNo());
					cartEntity.setCartSt(cartSt);
					cartEntity.setDelYn(delYn);
					cartEntity.setUseYn(useYn);
					cartEntity.setCartUsrId(usrId);
					cartEntity.setCartNo(cartNo + 1);
					if("W".equals(cartSt)) {
						int count = Integer.parseInt(request.get("count").toString());
						cartEntity.setCartItemCnt(count);
					}
					cartRepository.save(cartEntity);
					map.put("code", "success");
					map.put("msg", "장바구니 리스트에 등록 되었습니다.");
				}
			} else {
				Optional<ChemistryEntity> chemistryEntity = chemistryRepository.findByChemistryNo(cartItemNo);
				// 카트 테이블에 저장
				CartEntity cartEntity = new CartEntity();
				if (chemistryEntity.isPresent()) {
					cartEntity.setCartItemNm(chemistryEntity.get().getChemistryNm());
					cartEntity.setCartItemNo(chemistryEntity.get().getChemistryNo());
					cartEntity.setCartSt(cartSt);
					cartEntity.setDelYn(delYn);
					cartEntity.setUseYn(useYn);
					cartEntity.setCartUsrId(usrId);
					cartEntity.setCartNo(cartNo + 1);
					if("W".equals(cartSt)) {
						int count = Integer.parseInt(request.get("count").toString());
						cartEntity.setCartItemCnt(count);
					}
					cartRepository.save(cartEntity);
					map.put("code", "success");
					map.put("msg", "장바구니 리스트에 등록 되었습니다.");
				}
			}
		}
		return map;
	}

	public Map<String, Object> categorieDetail(Map<String, Object> request) {
		Map<String, Object> map = new HashMap<>();
		CategoriesDto dto = new CategoriesDto();
		int gradePrice = 0;
		String gdPrice = "";
		String itemKey = request.get("itemKey").toString();
		String usrId = "";
		if (request.get("usrId") != null && !"".equals(request.get("usrId"))) {
			usrId = request.get("usrId").toString();
		}
		if (itemKey.startsWith("1")) {
			Optional<ProductEntity> entity = productRepository.findByProductNo(itemKey);
			dto.setInvenUrl(entity.get().getProductUrl());
			dto.setInvenSClsNm(entity.get().getProductNm());
			dto.setInvenText(entity.get().getProductText());

			int price = entity.get().getProductPrice();
			DecimalFormat krFormat = new DecimalFormat("###,###원");
			String itemPrice = krFormat.format(price);

			dto.setInvenPrice(itemPrice);
			if (request.get("usrId") != null && !"".equals(request.get("usrId"))) {
				Optional<AccountEntity> accountEntity = accountRepository.findByUsrId(usrId);
				String usrGrade = accountEntity.get().getUsrGrade();

				krFormat = new DecimalFormat("###,###p 적립");
				switch (usrGrade) {
				case "1":
					gradePrice = (int) (price * 0.01);
					gdPrice = krFormat.format(gradePrice);
					break;
				case "2":
					gradePrice = (int) (price * 0.02);
					gdPrice = krFormat.format(gradePrice);
					break;
				case "3":
					gradePrice = (int) (price * 0.05);
					gdPrice = krFormat.format(gradePrice);
					break;
				case "4":
					gradePrice = (int) (price * 0.1);
					gdPrice = krFormat.format(gradePrice);
					break;
				case "5":
					gradePrice = (int) (price * 0.2);
					gdPrice = krFormat.format(gradePrice);
					break;
				default:
					gdPrice = "0";
				}
			}
			Optional<CategoriesEntity> categoriesEntity = categoriesRepository.findByInvenSClsNo(itemKey);
			dto.setInvenCnt(categoriesEntity.get().getInvenCnt());
			
		}else if(itemKey.startsWith("2")) {
			Optional<SeedEntity> entity = seedRepository.findBySeedNo(itemKey);
			dto.setInvenUrl(entity.get().getSeedUrl());
			dto.setInvenSClsNm(entity.get().getSeedNm());
			dto.setInvenText(entity.get().getSeedText());

			int price = entity.get().getSeedPrice();
			DecimalFormat krFormat = new DecimalFormat("###,###원");
			String itemPrice = krFormat.format(price);

			dto.setInvenPrice(itemPrice);
			if (request.get("usrId") != null && !"".equals(request.get("usrId"))) {
				Optional<AccountEntity> accountEntity = accountRepository.findByUsrId(usrId);
				String usrGrade = accountEntity.get().getUsrGrade();

				krFormat = new DecimalFormat("###,###p 적립");
				switch (usrGrade) {
				case "1":
					gradePrice = (int) (price * 0.01);
					gdPrice = krFormat.format(gradePrice);
					break;
				case "2":
					gradePrice = (int) (price * 0.02);
					gdPrice = krFormat.format(gradePrice);
					break;
				case "3":
					gradePrice = (int) (price * 0.05);
					gdPrice = krFormat.format(gradePrice);
					break;
				case "4":
					gradePrice = (int) (price * 0.1);
					gdPrice = krFormat.format(gradePrice);
					break;
				case "5":
					gradePrice = (int) (price * 0.2);
					gdPrice = krFormat.format(gradePrice);
					break;
				default:
					gdPrice = "0";
				}
			}
			Optional<CategoriesEntity> categoriesEntity = categoriesRepository.findByInvenSClsNo(itemKey);
			dto.setInvenCnt(categoriesEntity.get().getInvenCnt());
		}else {
			Optional<ChemistryEntity> entity = chemistryRepository.findByChemistryNo(itemKey);
			dto.setInvenUrl(entity.get().getChemistryUrl());
			dto.setInvenSClsNm(entity.get().getChemistryNm());
			dto.setInvenText(entity.get().getChemistryText());

			int price = entity.get().getChemistryPrice();
			DecimalFormat krFormat = new DecimalFormat("###,###원");
			String itemPrice = krFormat.format(price);

			dto.setInvenPrice(itemPrice);
			if (request.get("usrId") != null && !"".equals(request.get("usrId"))) {
				Optional<AccountEntity> accountEntity = accountRepository.findByUsrId(usrId);
				String usrGrade = accountEntity.get().getUsrGrade();

				krFormat = new DecimalFormat("###,###p 적립");
				switch (usrGrade) {
				case "1":
					gradePrice = (int) (price * 0.01);
					gdPrice = krFormat.format(gradePrice);
					break;
				case "2":
					gradePrice = (int) (price * 0.02);
					gdPrice = krFormat.format(gradePrice);
					break;
				case "3":
					gradePrice = (int) (price * 0.05);
					gdPrice = krFormat.format(gradePrice);
					break;
				case "4":
					gradePrice = (int) (price * 0.1);
					gdPrice = krFormat.format(gradePrice);
					break;
				case "5":
					gradePrice = (int) (price * 0.2);
					gdPrice = krFormat.format(gradePrice);
					break;
				default:
					gdPrice = "0";
				}
			}
			Optional<CategoriesEntity> categoriesEntity = categoriesRepository.findByInvenSClsNo(itemKey);
			dto.setInvenCnt(categoriesEntity.get().getInvenCnt());
		}

		map.put("result", dto);
		map.put("benfit", gdPrice);
		return map;
	}



}
