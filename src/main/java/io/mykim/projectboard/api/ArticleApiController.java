package io.mykim.projectboard.api;

import io.mykim.projectboard.dto.request.RequestArticleCreateDto;
import io.mykim.projectboard.dto.request.RequestArticleEditDto;
import io.mykim.projectboard.dto.response.ResponseArticleFindDto;
import io.mykim.projectboard.dto.response.ResponseArticleListDto;
import io.mykim.projectboard.global.pagination.CustomPaginationRequest;
import io.mykim.projectboard.global.pagination.CustomPaginationResponse;
import io.mykim.projectboard.global.pagination.CustomSortingRequest;
import io.mykim.projectboard.global.result.enums.CustomSuccessCode;
import io.mykim.projectboard.global.result.model.CommonResponse;
import io.mykim.projectboard.service.ArticleCommentService;
import io.mykim.projectboard.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ArticleApiController {
    private final ArticleService articleService;
    private final ArticleCommentService articleCommentService;


    /**
     * 게시글 목록조회 (+search +pagination +sort)
     *
     * pagination : offset, limit
     * sort : id,DESC/title,ASC........
     * search : keyword
     * /api/v1/articles?keyword=?&offset=?&limit=?&sort=id,DESC/title,ASC......
     */

    @GetMapping("/api/v1/articles")
    public ResponseEntity<CommonResponse> findAllArticleApi(@ModelAttribute CustomPaginationRequest paginationRequest,
                                                            @ModelAttribute CustomSortingRequest sortingRequest,
                                                            @RequestParam String keyword) {

        log.info("[GET] /api/v1/articles?keyword={}&offset={}&limit={}&sort={}  =>  find all Article api", keyword, paginationRequest.getOffset(), paginationRequest.getLimit(), sortingRequest.getSort());
        CommonResponse commonResponse = new CommonResponse<>(CustomSuccessCode.COMMON_OK, articleService.findAllArticle(paginationRequest, sortingRequest, keyword));
        return ResponseEntity
                .status(commonResponse.getStatus())
                .body(commonResponse);
    }

    // 게시글 단건 조회
    @GetMapping("/api/v1/articles/{articleId}")
    public ResponseEntity<CommonResponse> findOneArticleApi(@PathVariable Long articleId) {
        log.info("[GET] /api/v1/articles/{articleId}  =>  find one Article api, articleId = {}", articleId);
        CommonResponse commonResponse = new CommonResponse<>(CustomSuccessCode.COMMON_OK, articleService.findOneArticle(articleId));
        return ResponseEntity
                .status(commonResponse.getStatus())
                .body(commonResponse);
    }

    // 게시글 저장
    @PostMapping("/api/v1/articles")
    public ResponseEntity<CommonResponse> createArticleApi(@RequestBody @Valid RequestArticleCreateDto createDto) {
        log.info("[POST] /api/v1/articles  =>  create Article api, RequestArticleCreateDto = {}", createDto);
        CommonResponse commonResponse = new CommonResponse<>(CustomSuccessCode.INSERT_OK, articleService.createArticle(createDto));
        return ResponseEntity
                .status(commonResponse.getStatus())
                .body(commonResponse);
    }

    // 게시글 단건 수정
    @PatchMapping("/api/v1/articles/{articleId}")
    public ResponseEntity<CommonResponse> editArticleApi(@PathVariable Long articleId, @RequestBody @Valid RequestArticleEditDto editDto) {
        log.info("[PATCH] /api/v1/articles/{}  =>  edit Article api, RequestArticleEditDto = {}", articleId, editDto);
        articleService.editArticle(editDto, articleId);
        CommonResponse commonResponse = new CommonResponse<>(CustomSuccessCode.UPDATE_OK);
        return ResponseEntity
                .status(commonResponse.getStatus())
                .body(commonResponse);
    }

    // 게시글 단건 삭제
    @DeleteMapping("/api/v1/articles/{articleId}")
    public ResponseEntity<CommonResponse> removeArticleApi(@PathVariable Long articleId) {
        log.info("[DELETE] /api/v1/articles/{}  =>  remove Article api", articleId);
        articleService.removeArticle(articleId);
        CommonResponse commonResponse = new CommonResponse<>(CustomSuccessCode.DELETE_OK);
        return ResponseEntity
                .status(commonResponse.getStatus())
                .body(commonResponse);
    }


    // 댓글 전체 목록조회
    //@GetMapping("/api/v1/article-comments")



    // 댓글 단건 조회
    //@GetMapping("/api/v1/article-comments/{articleCommentId}")



    // 게시글 하부 댓글 전체 목록조회 +search
    //@PostMapping("/api/v1/articles/{articleId}/article-comments")



    // 게시글 하부 댓글 단건 조회
    //@PostMapping("/api/v1/articles/{articleId}/article-comments/{articleCommentId}")



    // 게시글 하부 댓글 저장
    //@PostMapping("/api/v1/articles/{articleId}/article-comments")



    // 게시글 하부 댓글 수정
    //@PatchMapping("/api/v1/articles/{articleId}/article-comments/{articleCommentId}")



    // 게시글 하부 댓글 삭제
    //@DeleteMapping("/api/v1/articles/{articleId}/article-comments/{articleCommentId}")


}
