package io.mykim.projectboard.article.api;

import io.mykim.projectboard.article.dto.request.ArticleCommentCreateDto;
import io.mykim.projectboard.article.dto.request.ArticleCommentEditDto;
import io.mykim.projectboard.article.service.ArticleCommentService;
import io.mykim.projectboard.global.config.security.dto.PrincipalDetail;
import io.mykim.projectboard.global.result.enums.CustomSuccessCode;
import io.mykim.projectboard.global.result.model.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ArticleCommentApiController {

    private final ArticleCommentService articleCommentService;

    // 게시글 하부 댓글 목록조회
    @GetMapping("/api/v1/articles/{articleId}/article-comments")
    public ResponseEntity<CommonResponse> findAllArticleCommentUnderArticleApi(@PathVariable Long articleId) {
        log.info("[GET] /api/v1/articles/{}/article-comments => find all ArticleComment under Article api", articleId);
        CommonResponse response = new CommonResponse(CustomSuccessCode.COMMON_OK, articleCommentService.findAllArticleCommentUnderArticle(articleId));
        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    // 게시글 하부 댓글 단건 조회 : not used
    @GetMapping("/api/v1/articles/{articleId}/article-comments/{articleCommentId}")
    public ResponseEntity<CommonResponse> findOneArticleCommentUnderArticleApi(@PathVariable Long articleId, @PathVariable Long articleCommentId) {
        log.info("[GET] /api/v1/articles/{}/article-comments/{}  =>  find One ArticleComment under Article api", articleId, articleCommentId);
        CommonResponse response = new CommonResponse(CustomSuccessCode.COMMON_OK, articleCommentService.findOneArticleCommentByIdUnderArticle(articleId, articleCommentId));
        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    // 게시글 하부 댓글 저장
    @PostMapping("/api/v1/articles/{articleId}/article-comments")
    public ResponseEntity<CommonResponse> createArticleCommentApi(@PathVariable Long articleId,
                                                                  @RequestBody @Valid ArticleCommentCreateDto createDto,
                                                                  @AuthenticationPrincipal PrincipalDetail principalDetail) {
        log.info("[POST] /api/v1/articles/{}/article-comments  =>  create ArticleComment api, ArticleCommentCreateDto = {}", articleId, createDto);
        CommonResponse response = new CommonResponse(CustomSuccessCode.INSERT_OK, articleCommentService.createNewArticleComment(createDto, articleId, principalDetail.getUser()));
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
        log.info("[DELETE] /api/v1/articles/{}/article-comments/{}  =>  remove ArticleComment api", articleId, articleCommentId);
        articleCommentService.removeArticleComment(articleId, articleCommentId);
        CommonResponse response = new CommonResponse(CustomSuccessCode.DELETE_OK);
        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

}
