package com.gongsik.gsr.api.posts.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gongsik.gsr.api.posts.service.PostsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/posts")
@Tag(name = "Posts Controller", description = "게시물 내역")
@RequiredArgsConstructor
public class PostsController {
	
	@Autowired
	private PostsService postsService;
	
	@PostMapping("/selectPosts")
	@Operation(summary = "게시물 조회", description = "게시물 죄회 하기")
	public ResponseEntity<Map<String, Object>> selectPosts(@RequestBody Map<String, Object> request) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = postsService.selectPosts(request);
		return ResponseEntity.ok(map);
	}

	
	@PostMapping("/savePosts")
	@Operation(summary = "게시물 저장", description = "게시물 저장 하기")
	@Parameters({
        @Parameter(description = "사용자 아이디", name = "usrId", example = "test"),
        @Parameter(description = "사용자 이름", name = "usrNm", example = "test@gmail.com"),
	})
	@ApiResponses(value = {
			 @ApiResponse(
		               responseCode = "200",
		               description = "게시물 저장  성공",
		               content = @Content(
		                    schema = @Schema(implementation = Map.class)))
		})
	public ResponseEntity<Map<String, Object>> savePosts(@RequestBody Map<String, Object> request) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = postsService.savePosts(request);
		return ResponseEntity.ok(map);
	}
	
	@GetMapping("/postsDetail/{itemKey}")
	@Operation(summary = "게시물 상세 조회", description = "게시물 상세조회 하기")
	@Parameters({
        @Parameter(description = "게시물 번호", name = "postNo", example = "1"),
	})
	public ResponseEntity<Map<String, Object>> postsDetail(@PathVariable("itemKey") int postsNo) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = postsService.postsDetail(postsNo);
		return ResponseEntity.ok(map);
	}
	
	@GetMapping("/postsDel/{itemKey}")
	@Operation(summary = "게시물 삭제", description = "게시물 삭제 하기")
	@Parameters({
        @Parameter(description = "게시물 번호", name = "postNo", example = "1"),
	})
	public ResponseEntity<Map<String, Object>> postsDel(@PathVariable("itemKey") int postsNo) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = postsService.postsDel(postsNo);
		return ResponseEntity.ok(map);
	}

		
	@GetMapping("/selectReview/{itemKey}")
	@Operation(summary = "댓글 조회", description = "댓글 조회 하기")
	public ResponseEntity<Map<String, Object>> selectPosts(@PathVariable("itemKey") int postsNo) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = postsService.selectPosts(postsNo);
		return ResponseEntity.ok(map);
	}

	
	@PostMapping("/reviewSave")
	@Operation(summary = "댓글 저장", description = "댓굴 저장 하기")
	@Parameters({
		@Parameter(description = "게시물 번호", name = "postNo", example = "1"),
		@Parameter(description = "댓글 내용", name = "reviewText", example = "댓글완료"),
	})
	@ApiResponses(value = {
			 @ApiResponse(
		               responseCode = "200",
		               description = "댓글 저장  성공",
		               content = @Content(
		                    schema = @Schema(implementation = Map.class)))
		})
	public ResponseEntity<Map<String, Object>> reviewSave(@RequestBody Map<String, Object> request) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = postsService.reviewSave(request);
		return ResponseEntity.ok(map);
	}
	
	@PostMapping("/replyDel")
	@Operation(summary = "댓글 및 대댓글 삭제", description = "댓글 및 대댓글 삭제")
	@Parameters({
        @Parameter(description = "게시물 번호", name = "postNo", example = "1"),
        @Parameter(description = "댓글 번호", name = "replyNo", example = "1"),
        @Parameter(description = "대댓글 번호", name = "replyMiniNo", example = "1"),
	})
	public ResponseEntity<Map<String, Object>> replyDel(@RequestBody Map<String, Object> request) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = postsService.replyDel(request);
		return ResponseEntity.ok(map);
	}
}
