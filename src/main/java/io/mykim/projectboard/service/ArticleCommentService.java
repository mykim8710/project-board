package io.mykim.projectboard.service;

import io.mykim.projectboard.domain.entity.Article;
import io.mykim.projectboard.domain.entity.ArticleComment;
import io.mykim.projectboard.dto.request.ArticleCommentCreateDto;
import io.mykim.projectboard.dto.request.ArticleCommentEditDto;
import io.mykim.projectboard.dto.response.ResponseArticleCommentFindDto;
import io.mykim.projectboard.global.result.enums.CustomErrorCode;
import io.mykim.projectboard.global.result.exception.NotFoundException;
import io.mykim.projectboard.repository.ArticleCommentRepository;
import io.mykim.projectboard.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.mykim.projectboard.global.result.enums.CustomErrorCode.NOT_FOUND_ARTICLE_COMMENT;

@Slf4j
@RequiredArgsConstructor
@Service
public class ArticleCommentService {
    private final ArticleCommentRepository articleCommentRepository;
    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public ResponseArticleCommentFindDto findOneArticleCommentByIdUnderArticle(Long articleId, Long articleCommentId) {
        ArticleComment articleComment = articleCommentRepository.findArticleCommentByIdAndArticleId(articleId, articleCommentId).orElseThrow(() -> new NotFoundException(NOT_FOUND_ARTICLE_COMMENT));
        return ResponseArticleCommentFindDto.of(articleComment);
    }

    @Transactional(readOnly = true)
    public ResponseArticleCommentFindDto findOneArticleCommentById(Long articleCommentId) {
        ArticleComment articleComment = articleCommentRepository.findById(articleCommentId).orElseThrow(() -> new NotFoundException(CustomErrorCode.NOT_FOUND_ARTICLE_COMMENT));
        return ResponseArticleCommentFindDto.of(articleComment);
    }

    @Transactional
    public Long createNewArticleComment(ArticleCommentCreateDto createDto, Long articleId) {
        Article findArticle = articleRepository.findById(articleId).orElseThrow(() -> new NotFoundException(CustomErrorCode.NOT_FOUND_ARTICLE));

        // create ArticleComment entity
        ArticleComment articleComment = ArticleComment.of(createDto.getContent(),findArticle);
        articleCommentRepository.save(articleComment);
        return articleComment.getId();
    }

    @Transactional
    public void editArticleComment(ArticleCommentEditDto editDto, Long articleId, Long articleCommentId) {
        ArticleComment articleComment = articleCommentRepository.findArticleCommentByIdAndArticleId(articleId, articleCommentId).orElseThrow(() -> new NotFoundException(NOT_FOUND_ARTICLE_COMMENT));
        articleComment.editArticleComment(editDto.getContent());
    }

    @Transactional
    public void removeArticleComment(Long articleId, Long articleCommentId) {
        ArticleComment articleComment = articleCommentRepository.findArticleCommentByIdAndArticleId(articleId, articleCommentId).orElseThrow(() -> new NotFoundException(NOT_FOUND_ARTICLE_COMMENT));
        articleCommentRepository.delete(articleComment);
    }
}
