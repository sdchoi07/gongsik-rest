package com.gongsik.gsr.api.posts.service;

import java.text.DecimalFormat;
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

import com.gongsik.gsr.api.account.join.entity.AccountEntity;
import com.gongsik.gsr.api.account.join.repository.AccountRepository;
import com.gongsik.gsr.api.posts.dto.PostsDto;
import com.gongsik.gsr.api.posts.entity.PostsEntity;
import com.gongsik.gsr.api.posts.repository.PostsRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PostsService {

	@Autowired
	PostsRepository postsRepository;

	@Autowired
	AccountRepository accountRepository;

	/* 게시물 조회 */
	public Map<String, Object> selectPosts(Map<String, Object> request) {
		Map<String, Object> map = new HashMap<>();
		List<PostsDto> list = new ArrayList<>();
		String reqGubunNm = "";
		String gubun = "";
		if (request.get("gubun") != null && !request.get("gubun").equals("")) {
			reqGubunNm = request.get("gubun").toString();
			if ("일반".equals(reqGubunNm)) {
				gubun = "01";
			} else if ("정보".equals(reqGubunNm)) {
				gubun = "02";
			} else if ("후기".equals(reqGubunNm)) {
				gubun = "03";
			} else if("질문".equals(reqGubunNm)){
				gubun = "04";
			}else {
				gubun = "05";
			}
		}
		int currentPage = Integer.parseInt(request.get("currentPage").toString());
		int size = 5;
		int total = 0;
		Pageable pageable = PageRequest.of((currentPage - 1), size ,Sort.by("postsNo").descending());

		// 게시물 전체 조회
		if ("undefined".equals(reqGubunNm) || "".equals(reqGubunNm)) {
			total = postsRepository.countByDelYn("N");
			List<PostsEntity> postsEntity = postsRepository.findAllByDelYn("N", pageable);
			for (PostsEntity posts : postsEntity) {
				PostsDto postsDto = new PostsDto();
				postsDto.setPostsNm(posts.getPostsNm());
				postsDto.setPostsGubun(posts.getPostsGubun());
				postsDto.setPostsUsrId(posts.getPostsUsrId());
				postsDto.setPostsUsrNm(posts.getPostsUsrNm());
				postsDto.setPostsNo(posts.getPostsNo());
				int viewCnt = posts.getPostsViewCnt();
				DecimalFormat krFormat = new DecimalFormat("###,###");
				String postsViewCnt = krFormat.format(viewCnt);
				postsDto.setPostsViewCnt(postsViewCnt);
				String postsUrl = posts.getPostsText();
				if (postsUrl.contains("createPost")) {
					postsUrl = postsUrl.substring(postsUrl.indexOf("createPost"), postsUrl.indexOf("\" w"));
					postsDto.setPostsUrl(postsUrl);
				}
				String gubunNm = posts.getPostsGubun();
				if ("01".equals(gubunNm)) {
					postsDto.setPostsGubunNm("일반");
				} else if ("02".equals(gubunNm)) {
					postsDto.setPostsGubunNm("정보");
				} else if ("03".equals(gubunNm)) {
					postsDto.setPostsGubunNm("후기");
				} else {
					postsDto.setPostsGubunNm("질문");
				}

				String postsDt = posts.getPostsDt();
				String postsYMD = postsDt.substring(0, 10).replaceAll("-", ".");
				String postsTime = postsDt.substring(11, 16);
				postsDto.setPostsYMD(postsYMD);
				postsDto.setPostsTime(postsTime);
				list.add(postsDto);

			}
		} else { // 게시물 구분되어 조회
			
			List<PostsEntity> postsEntity = new ArrayList<>();
			if("05".equals(gubun)) {
				String usrId = request.get("usrId").toString();
				total = postsRepository.countByPostsUsrIdAndDelYn(usrId, "N");
				postsEntity = postsRepository.findByPostsUsrIdAndDelYn(usrId, "N",
						pageable);
			}else {
				total = postsRepository.countByPostsGubunAndDelYn(gubun, "N");
				postsEntity = postsRepository.findByPostsGubunAndDelYn(gubun, "N",
					pageable);
			}
			for (PostsEntity posts : postsEntity) {
				PostsDto postsDto = new PostsDto();
				postsDto.setPostsNm(posts.getPostsNm());
				postsDto.setPostsGubun(posts.getPostsGubun());
				postsDto.setPostsUsrId(posts.getPostsUsrId());
				postsDto.setPostsUsrNm(posts.getPostsUsrNm());
				postsDto.setPostsNo(posts.getPostsNo());
				int viewCnt = posts.getPostsViewCnt();
				DecimalFormat krFormat = new DecimalFormat("###,###");
				String postsViewCnt = krFormat.format(viewCnt);
				postsDto.setPostsViewCnt(postsViewCnt);
				String gubunNm = posts.getPostsGubun();
				if ("01".equals(gubunNm)) {
					postsDto.setPostsGubunNm("일반");
				} else if ("02".equals(gubunNm)) {
					postsDto.setPostsGubunNm("정보");
				} else if ("03".equals(gubunNm)) {
					postsDto.setPostsGubunNm("후기");
				} else {
					postsDto.setPostsGubunNm("질문");
				}
				String postsUrl = posts.getPostsText();
				if (postsUrl.contains("createPost")) {
					postsUrl = postsUrl.substring(postsUrl.indexOf("createPost"), postsUrl.indexOf("\" w"));
					postsDto.setPostsUrl(postsUrl);
				}

				String postsDt = posts.getPostsDt();
				String postsYMD = postsDt.substring(0, 10).replaceAll("-", ".");
				String postsTime = postsDt.substring(11, 16);
				System.out.print(postsYMD + " " + postsTime);
				postsDto.setPostsYMD(postsYMD);
				postsDto.setPostsTime(postsTime);
				list.add(postsDto);

			}
		}
		map.put("list", list);
		map.put("cnt", list.size());

		// 인기 게시물 조회
		List<PostsEntity> postsEntity = postsRepository.findTop3ByOrderByPostsViewCntDesc();
		List<PostsDto> popList = new ArrayList<>();
		for (PostsEntity posts : postsEntity) {
			PostsDto postsDto = new PostsDto();
			postsDto.setPostsNm(posts.getPostsNm());
			postsDto.setPostsUsrId(posts.getPostsUsrId());
			postsDto.setPostsNo(posts.getPostsNo());
			String postsUrl = posts.getPostsText();
			if (postsUrl.contains("createPost")) {
				postsUrl = postsUrl.substring(postsUrl.indexOf("createPost"), postsUrl.indexOf("\" w"));
				postsDto.setPostsUrl(postsUrl);
			}

			String postsDt = posts.getPostsDt();
			String postsYMD = postsDt.substring(0, 10).replaceAll("-", ".");
			String postsTime = postsDt.substring(11, 16);
			postsDto.setPostsYMD(postsYMD);
			postsDto.setPostsTime(postsTime);
			popList.add(postsDto);

		}
		map.put("popList", popList);

		int totalPages = total / size;
		// 보정해줘야 할 경우는? 나머지가 0보다 클 때
		if (total % size > 0) {
			// 전체페이지수를 1증가 처리
			totalPages++;
		}
		int startPage = currentPage / size * size + 1;
		// 보정해줘야 할 경우는? 5 / 5 * 5 + 1 => 6 경우처럼
		// 현재페이지 % 5 == 0 일 때
		if (currentPage % size == 0) {
			// startPage = startPage - 5(페이징의 개수)
			startPage -= size;
		}

		// endPage : '이전 [1] [2] [3] [4] [5] 다음' 일때 5을 의미
		int endPage = startPage + size - 1;
		// 보정해줘야 할 경우는? endPage > totalPages 일때
		// endPage를 totalPages로 바꿔줘야 함
		if (endPage > totalPages) {
			endPage = totalPages;
		}
		map.put("totalPages", totalPages);
		map.put("startPage", startPage);
		map.put("endPage", endPage);
		map.put("currentPage", currentPage);

		return map;
	}

	// 게시물 저장
	public Map<String, Object> savePosts(Map<String, Object> request) {
		Map<String, Object> map = new HashMap<>();
		String gubun = request.get("gubun").toString();
		String postsText = request.get("editorData").toString();
		String usrId = request.get("usrId").toString();
		String postsNm = request.get("postNm").toString();

		Optional<AccountEntity> account = accountRepository.findByUsrId(usrId);

		String usrNm = "";
		if (account.isPresent()) {
			usrNm = account.get().getUsrNm();
		}
		int postNo = postsRepository.findOne();

		PostsEntity posts = new PostsEntity();
		posts.setPostsGubun(gubun);
		posts.setPostsText(postsText);
		posts.setPostsNm(postsNm);
		posts.setPostsNo(postNo + 1);
		posts.setPostsUsrId(usrId);
		posts.setPostsUsrNm(usrNm);
		posts.setDelYn("N");

		postsRepository.save(posts);

		map.put("code", "success");
		map.put("msg", "게시물이 등록 되었습니다.");

		return map;
	}

	public Map<String, Object> postsDetail(int postsNo) {
		Map<String, Object> map = new HashMap<>();

		// 게시물 상세 조회
		PostsEntity postsEntity = postsRepository.findByPostsNo(postsNo);
		PostsDto postsDto = new PostsDto();
		postsDto.setPostsNm(postsEntity.getPostsNm());
		postsDto.setPostsGubun(postsEntity.getPostsGubun());
		postsDto.setPostsUsrId(postsEntity.getPostsUsrId());
		postsDto.setPostsUsrNm(postsEntity.getPostsUsrNm());
		postsDto.setPostsNo(postsEntity.getPostsNo());
		postsDto.setPostsText(postsEntity.getPostsText());
		int viewCnt = postsEntity.getPostsViewCnt();
		DecimalFormat krFormat = new DecimalFormat("###,###");
		String postsViewCnt = krFormat.format(viewCnt);
		postsDto.setPostsViewCnt(postsViewCnt);
//				String postsUrl = postsEntity.getPostsText();
//				if (postsUrl.contains("createPost")) {
//					postsUrl = postsUrl.substring(postsUrl.indexOf("createPost"), postsUrl.indexOf("\" w"));
//					postsDto.setPostsUrl(postsUrl);
//				}
		String gubunNm = postsEntity.getPostsGubun();
		if ("01".equals(gubunNm)) {
			postsDto.setPostsGubunNm("일반");
		} else if ("02".equals(gubunNm)) {
			postsDto.setPostsGubunNm("정보");
		} else if ("03".equals(gubunNm)) {
			postsDto.setPostsGubunNm("후기");
		} else {
			postsDto.setPostsGubunNm("질문");
		}

		String postsDt = postsEntity.getPostsDt();
		String postsYMD = postsDt.substring(0, 10).replaceAll("-", ".");
		String postsTime = postsDt.substring(11, 16);
		postsDto.setPostsYMD(postsYMD);
		postsDto.setPostsTime(postsTime);
		
		map.put("result", postsDto);
		return map;
	}

}
