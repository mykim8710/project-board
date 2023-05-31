package io.mykim.projectboard.global.admin.endpoint;

import io.mykim.projectboard.article.dto.request.HashtagCreateDto;
import io.mykim.projectboard.article.enums.SearchType;
import io.mykim.projectboard.article.repository.ArticleCommentRepository;
import io.mykim.projectboard.article.repository.ArticleRepository;
import io.mykim.projectboard.article.repository.HashtagRepository;
import io.mykim.projectboard.article.service.ArticleCommentService;
import io.mykim.projectboard.article.service.ArticleService;
import io.mykim.projectboard.article.service.HashtagService;
import io.mykim.projectboard.global.result.enums.CustomSuccessCode;
import io.mykim.projectboard.global.result.model.CommonResponse;
import io.mykim.projectboard.user.repository.UserRepository;
import io.mykim.projectboard.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class ForAdminApiController {
    private final ArticleService articleService;
    private final ArticleCommentService articleCommentService;
    private final HashtagService hashtagService;
    private final UserService userService;

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;
    private final HashtagRepository hashtagRepository;
    private final UserRepository userRepository;

    // 게시글 목록 조회
    @GetMapping("/articles")
    public ResponseEntity<CommonResponse> findAllArticleApiForAdmin(HttpServletRequest request,
                                                                    @RequestParam(required = false) String searchKeyword,
                                                                    @RequestParam(required = false) SearchType searchType,
                                                                    @PageableDefault(page = 0, size = 30, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("[GET] /api/admin/articles?searchType={}&searchKeyword={}&page={}&size={}&sort={} => find all Article for Admin Service api", searchType, searchKeyword, pageable.getOffset(), pageable.getPageSize(), pageable.getSort());
        CommonResponse commonResponse = new CommonResponse<>(CustomSuccessCode.COMMON_OK, articleService.findAllArticle(searchKeyword, searchType, pageable));
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
    public ResponseEntity<CommonResponse> removeArticleApiForAdmin(HttpServletRequest request,
                                                                   @PathVariable Long articleId) {
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
        CommonResponse response = new CommonResponse(CustomSuccessCode.COMMON_OK, articleCommentService.findAllArticleCommentFromAdmin(searchKeyword, pageable));
        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    // 댓글 단건조회
    @GetMapping("/article-comments/{articleCommentId}")
    public ResponseEntity<CommonResponse> findOneArticleCommentForAdminApi(HttpServletRequest request,
                                                                           @PathVariable Long articleCommentId) {
        log.info("[GET] /api/admin/articles/{articleId}  =>  find one articleComment for Admin Service api, articleCommentId = {}", articleCommentId);
        CommonResponse response = new CommonResponse(CustomSuccessCode.COMMON_OK, articleCommentService.findOneArticleCommentById(articleCommentId));
        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    // 댓글 단건 삭제
    @DeleteMapping("/article-comments/{articleCommentId}")
    public ResponseEntity<CommonResponse> removeArticleCommentApiForAdmin(HttpServletRequest request,
                                                                          @PathVariable Long articleCommentId) {
        log.info("[DELETE] /api/admin/article-comments/{}  =>  remove articleComment for Admin Service api", articleCommentId);
        articleCommentService.removeArticleCommentFromAdmin(articleCommentId);
        CommonResponse commonResponse = new CommonResponse<>(CustomSuccessCode.DELETE_OK);
        return ResponseEntity
                .status(commonResponse.getStatus())
                .body(commonResponse);
    }

    // 전체 해시태그 목록조회
    @GetMapping("/hashtags")
    public ResponseEntity<CommonResponse> findAllHashtagsApiForAdmin(HttpServletRequest request,
                                                     @RequestParam(required = false) String searchKeyword,
                                                     @PageableDefault(page = 0, size = 30, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("[GET] /api/admin/hashtags?searchKeyword={}&page={}&size={}&sort={} => find all hashtags for Admin Service api", searchKeyword, pageable.getOffset(), pageable.getPageSize(), pageable.getSort());
        CommonResponse response = new CommonResponse(CustomSuccessCode.COMMON_OK, hashtagService.findAllHashtagForAdmin(pageable, searchKeyword));
        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    // 해시태그 추가
    @PostMapping("/hashtags")
    public ResponseEntity<CommonResponse> addNewHashtagApiForAdmin(HttpServletRequest request,
                                                                   @RequestBody HashtagCreateDto hashtagCreateDto) {
        log.info("[POST] /api/admin/hashtags => add new hashtag for Admin Service Api, HashtagCreateDto = {}",  hashtagCreateDto);
        CommonResponse response = new CommonResponse(CustomSuccessCode.INSERT_OK, hashtagService.addNewHashtag(hashtagCreateDto.getName().replaceAll("#", "")));
        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    // 해시태그 삭제(미사용 중인 해시태그만)
    @DeleteMapping("/hashtags/{hashtagId}")
    public ResponseEntity<CommonResponse> removeHashtagApiForAdmin(HttpServletRequest request,
                                                                   @PathVariable Long hashtagId) {
        log.info("[DELETE] /api/admin/hashtags => remove hashtag for Admin Service Api, hashtagId : {}", hashtagId);
        hashtagService.removeHashtag(hashtagId);
        CommonResponse response = new CommonResponse(CustomSuccessCode.DELETE_OK);
        return ResponseEntity
                    .status(response.getStatus())
                    .body(response);
    }

    // 전체 회원 목록
    @GetMapping("/users")
    public ResponseEntity<CommonResponse> findAllUserApiForAdmin(HttpServletRequest request,
                                                                 @RequestParam(required = false) String searchKeyword,
                                                                 @PageableDefault(page = 0, size = 30, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("[GET] /api/admin/users?searchKeyword={}&page={}&size={}&sort={} => find all users for Admin Service api", searchKeyword, pageable.getOffset(), pageable.getPageSize(), pageable.getSort());
        CommonResponse response = new CommonResponse(CustomSuccessCode.COMMON_OK, userService.findAllUser(pageable, searchKeyword));
        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    // 각각 전체 카운트 조회
    @GetMapping("/dashboard/total-count")
    public Map<String, Long> findTotalCount() {
        log.info("[GET] /api/admin//dashboard/total-count");

        long articlesTotalCount = articleRepository.count();
        long articleCommentTotalCount = articleCommentRepository.count();
        long hashtagTotalCount = hashtagRepository.count();
        long userTotalCount = userRepository.count();

        return Map.of("articlesTotalCount", articlesTotalCount, "articleCommentTotalCount", articleCommentTotalCount, "hashtagTotalCount", hashtagTotalCount, "userTotalCount", userTotalCount);
    }
}
