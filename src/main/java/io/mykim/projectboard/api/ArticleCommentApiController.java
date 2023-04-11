package io.mykim.projectboard.api;

import io.mykim.projectboard.dto.request.ArticleCommentCreateDto;
import io.mykim.projectboard.dto.request.ArticleCommentEditDto;
import io.mykim.projectboard.global.result.enums.CustomSuccessCode;
import io.mykim.projectboard.global.result.model.CommonResponse;
import io.mykim.projectboard.service.ArticleCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ArticleCommentApiController {

    private final ArticleCommentService articleCommentService;

    // 댓글 전체 목록조회 +search +pagination +sorting
    //@GetMapping("/api/v1/article-comments")



    // 댓글 단건 조회
    //@GetMapping("/api/v1/article-comments/{articleCommentId}")



    // 게시글 하부 댓글 전체 목록조회 +search +pagination +sorting
    //@GetMapping("/api/v1/articles/{articleId}/article-comments")



    // 게시글 하부 댓글 단건 조회
    //@GetMapping("/api/v1/articles/{articleId}/article-comments/{articleCommentId}")




    // 게시글 하부 댓글 저장
    @PostMapping("/api/v1/articles/{articleId}/article-comments")
    public ResponseEntity<CommonResponse> createArticleCommentApi(@PathVariable Long articleId, @RequestBody @Valid ArticleCommentCreateDto createDto) {
        log.info("[POST] /api/v1/articles/{}/article-comments  =>  create ArticleComment api, ArticleCommentCreateDto = {}", articleId, createDto);
        CommonResponse response = new CommonResponse(CustomSuccessCode.INSERT_OK, articleCommentService.createNewArticleComment(createDto, articleId));
        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    // 게시글 하부 댓글 수정
    @PatchMapping("/api/v1/articles/{articleId}/article-comments/{articleCommentId}")
    public ResponseEntity<CommonResponse> editArticleCommentApi(@PathVariable Long articleId, @PathVariable Long articleCommentId, @RequestBody @Valid ArticleCommentEditDto editDto) {
        log.info("[PATCH] /api/v1/articles/{}/article-comments/{}  =>  edit ArticleComment api, ArticleCommentEditDto = {}", articleId, articleCommentId, editDto);
        articleCommentService.editArticleComment(editDto, articleId, articleCommentId);
        CommonResponse response = new CommonResponse(CustomSuccessCode.UPDATE_OK);
        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    // 게시글 하부 댓글 삭제
    @DeleteMapping("/api/v1/articles/{articleId}/article-comments/{articleCommentId}")
    public ResponseEntity<CommonResponse> removeArticleCommentApi(@PathVariable Long articleId, @PathVariable Long articleCommentId) {
        log.info("[DELETE] /api/v1/articles/{}/article-comments/{}  =>  remove ArticleComment api, ArticleCommentEditDto = {}", articleId, articleCommentId);
        articleCommentService.removeArticleComment(articleId, articleCommentId);
        CommonResponse response = new CommonResponse(CustomSuccessCode.DELETE_OK);
        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

}
