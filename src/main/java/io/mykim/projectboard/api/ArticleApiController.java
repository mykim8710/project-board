package io.mykim.projectboard.api;

import io.mykim.projectboard.service.ArticleCommentService;
import io.mykim.projectboard.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ArticleApiController {
    private final ArticleService articleService;
    private final ArticleCommentService articleCommentService;


    // 게시글 목록조회 +search + pagination + sort + size
    //@GetMapping("/api/v1/articles")



    // 게시글 단건 조회
    //@GetMapping("/api/v1/articles/{articleId}")



    // 게시글 저장
    //@PostMapping("/api/v1/articles")



    // 게시글 수정
    //@PatchMapping("/api/v1/articles/{articleId}")



    // 게시글 삭제
    //@DeleteMapping("/api/v1/articles/{articleId}")



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
