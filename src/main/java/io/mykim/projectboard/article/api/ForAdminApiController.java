package io.mykim.projectboard.article.api;

import io.mykim.projectboard.article.dto.request.HashtagCreateDto;
import io.mykim.projectboard.article.service.ArticleCommentService;
import io.mykim.projectboard.article.service.ArticleService;
import io.mykim.projectboard.article.service.HashtagService;
import io.mykim.projectboard.global.result.enums.CustomSuccessCode;
import io.mykim.projectboard.global.result.model.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class ForAdminApiController {
    private final ArticleService articleService;
    private final ArticleCommentService articleCommentService;
    private final HashtagService hashtagService;

    // 게시글 목록 조회
    @GetMapping("/articles")
    public ResponseEntity<CommonResponse> findAllArticleApiForAdmin(HttpServletRequest request,
                                                                    @RequestParam(required = false) String searchKeyword,
                                                                    @PageableDefault(page = 0, size = 30, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("[GET] /api/admin/articles?searchKeyword={}&page={}&size={}&sort={} => find all Article for Admin Service api", searchKeyword, pageable.getOffset(), pageable.getPageSize(), pageable.getSort());

        System.out.println("request.getRemotePort() = " + request.getRemotePort());
        
        
        CommonResponse commonResponse = new CommonResponse<>(CustomSuccessCode.COMMON_OK, articleService.findAllArticle(searchKeyword, null, pageable));

        return ResponseEntity
                .status(commonResponse.getStatus())
                .body(commonResponse);
    }

    // 게시글 단건 조회
    @GetMapping("/articles/{articleId}")
    public ResponseEntity<CommonResponse> findOneArticleApiForAdmin(HttpServletRequest request, @PathVariable Long articleId) {
        log.info("[GET] /api/admin/articles/{articleId}  =>  find one Article for Admin Service api, articleId = {}", articleId);
        CommonResponse commonResponse = new CommonResponse<>(CustomSuccessCode.COMMON_OK, articleService.findOneArticle(articleId));
        return ResponseEntity
                .status(commonResponse.getStatus())
                .body(commonResponse);
    }

    // 게시글 단건 삭제
    @DeleteMapping("/articles/{articleId}")
    public ResponseEntity<CommonResponse> removeArticleApiForAdmin(HttpServletRequest request, @PathVariable Long articleId) {
        log.info("[DELETE] /api/admin/articles/{}  =>  remove Article for Admin Service api", articleId);
        articleService.removeArticleFromAdmin(articleId);
        CommonResponse commonResponse = new CommonResponse<>(CustomSuccessCode.DELETE_OK);
        return ResponseEntity
                .status(commonResponse.getStatus())
                .body(commonResponse);
    }

    // 전체 댓글 목록조회
    @GetMapping("/article-comments")
    public ResponseEntity<CommonResponse> findAllArticleCommentForAdminApi(HttpServletRequest request,
                                                                           @RequestParam(required = false) String searchKeyword,
                                                                           @PageableDefault(page = 0, size = 30, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("[GET] /api/admin/article-comments?searchKeyword={}&page={}&size={}&sort={} => find all articleComment for Admin Service api", searchKeyword, pageable.getOffset(), pageable.getPageSize(), pageable.getSort());



        CommonResponse response = new CommonResponse(CustomSuccessCode.COMMON_OK);
        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    // 댓글 단건조회
    @GetMapping("/article-comments/{articleCommentId}")
    public ResponseEntity<CommonResponse> findOneArticleCommentForAdminApi(HttpServletRequest request, @PathVariable Long articleCommentId) {
        log.info("[GET] /api/admin/articles/{articleId}  =>  find one articleComment for Admin Service api, articleCommentId = {}", articleCommentId);
        CommonResponse response = new CommonResponse(CustomSuccessCode.COMMON_OK, articleCommentService.findOneArticleCommentById(articleCommentId));
        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    // 댓글 단건 삭제
    @DeleteMapping("/article-comments/{articleCommentId}")
    public ResponseEntity<CommonResponse> removeArticleCommentApiForAdmin(HttpServletRequest request, @PathVariable Long articleCommentId) {
        log.info("[DELETE] /api/admin/article-comments/{}  =>  remove articleComment for Admin Service api", articleCommentId);
        articleCommentService.removeArticleCommentFromAdmin(articleCommentId);
        CommonResponse commonResponse = new CommonResponse<>(CustomSuccessCode.DELETE_OK);
        return ResponseEntity
                .status(commonResponse.getStatus())
                .body(commonResponse);
    }

    // 전체 해시태그 목록조회(사용여부 파악)
    @GetMapping("/api/admin/hashtags")
    public CommonResponse findAllHashtagsApiForAdmin(HttpServletRequest request,
                                                     @RequestParam(required = false) String searchKeyword,
                                                     @PageableDefault(page = 0, size = 30, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("[GET] /api/admin/hashtags?searchKeyword={}&page={}&size={}&sort={} => find all hashtags for Admin Service api", searchKeyword, pageable.getOffset(), pageable.getPageSize(), pageable.getSort());


        return new CommonResponse(CustomSuccessCode.COMMON_OK);
    }

    // 해시태그 추가
    @PostMapping("/api/admin/hashtags")
    public CommonResponse addNewHashtagApiForAdmin(HttpServletRequest request, @RequestBody HashtagCreateDto hashtagCreateDto) {
        log.info("[POST] /api/admin/hashtags => add new hashtag for Admin Service Api, HashtagCreateDto = {}",  hashtagCreateDto);
        return new CommonResponse(CustomSuccessCode.INSERT_OK, hashtagService.addNewHashtag(hashtagCreateDto.getName().replaceAll("#", "")));
    }


    // 해시태그 삭제(미사용중인 해시태그만)
    @DeleteMapping("/api/admin/hashtags/{hashtagId}")
    public CommonResponse removeHashtagApiForAdmin(HttpServletRequest request, @PathVariable Long hashtagId) {
        log.info("[DELETE] /api/admin/hashtags => remove hashtag for Admin Service Api, hashtagId : {}", hashtagId);
        hashtagService.removeHashtag(hashtagId);
        return new CommonResponse(CustomSuccessCode.DELETE_OK);
    }


    // 전채 회원 목록
    //@GetMapping("/api/admin/users")


}
