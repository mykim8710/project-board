package io.mykim.projectboard.article.api;

import io.mykim.projectboard.article.dto.request.ArticleCreateDto;
import io.mykim.projectboard.article.dto.request.ArticleEditDto;
import io.mykim.projectboard.article.dto.request.ArticleSearchCondition;
import io.mykim.projectboard.global.result.enums.CustomSuccessCode;
import io.mykim.projectboard.global.result.model.CommonResponse;
import io.mykim.projectboard.global.select.pagination.CustomPaginationRequest;
import io.mykim.projectboard.global.select.sort.CustomSortingRequest;
import io.mykim.projectboard.article.service.ArticleCommentService;
import io.mykim.projectboard.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
     * search : keyword, searchType
     * /api/v1/articles?keyword=?&searchType=?&offset=?&limit=?&sort=id,DESC/title,ASC......
     */
    @GetMapping("/api/v1/articles")
    public ResponseEntity<CommonResponse> findAllArticleApi(@ModelAttribute CustomPaginationRequest paginationRequest,
                                                            @ModelAttribute CustomSortingRequest sortingRequest,
                                                            @ModelAttribute ArticleSearchCondition searchCondition) {

        log.info("[GET] /api/v1/articles?keyword={}&searchType={}&offset={}&limit={}&sort={}  =>  find all Article api", searchCondition.getKeyword(), searchCondition.getSearchType(), paginationRequest.getOffset(), paginationRequest.getLimit(), sortingRequest.getSort());
        CommonResponse commonResponse = new CommonResponse<>(CustomSuccessCode.COMMON_OK, articleService.findAllArticle(paginationRequest, sortingRequest, searchCondition));
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
    public ResponseEntity<CommonResponse> createArticleApi(@RequestBody @Valid ArticleCreateDto createDto) {
        log.info("[POST] /api/v1/articles  =>  create Article api, ArticleCreateDto = {}", createDto);
        CommonResponse commonResponse = new CommonResponse<>(CustomSuccessCode.INSERT_OK, articleService.createArticle(createDto));
        return ResponseEntity
                .status(commonResponse.getStatus())
                .body(commonResponse);
    }

    // 게시글 단건 수정
    @PatchMapping("/api/v1/articles/{articleId}")
    public ResponseEntity<CommonResponse> editArticleApi(@PathVariable Long articleId, @RequestBody @Valid ArticleEditDto editDto) {
        log.info("[PATCH] /api/v1/articles/{}  =>  edit Article api, ArticleEditDto = {}", articleId, editDto);
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


}