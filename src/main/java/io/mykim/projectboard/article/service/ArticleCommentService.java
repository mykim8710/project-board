package io.mykim.projectboard.article.service;

import io.mykim.projectboard.article.dto.request.ArticleCommentCreateDto;
import io.mykim.projectboard.article.dto.request.ArticleCommentEditDto;
import io.mykim.projectboard.article.dto.response.ResponseArticleCommentFindDto;
import io.mykim.projectboard.article.dto.response.ResponseArticleCommentListDto;
import io.mykim.projectboard.article.entity.Article;
import io.mykim.projectboard.article.entity.ArticleComment;
import io.mykim.projectboard.article.repository.ArticleCommentRepository;
import io.mykim.projectboard.article.repository.ArticleRepository;
import io.mykim.projectboard.global.result.enums.CustomErrorCode;
import io.mykim.projectboard.global.result.exception.NotAllowedUserException;
import io.mykim.projectboard.global.result.exception.NotFoundException;
import io.mykim.projectboard.global.result.exception.UnAuthorizedException;
import io.mykim.projectboard.global.select.pagination.CustomPaginationRequest;
import io.mykim.projectboard.global.select.pagination.CustomPaginationResponse;
import io.mykim.projectboard.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.mykim.projectboard.global.result.enums.CustomErrorCode.NOT_FOUND_ARTICLE_COMMENT;

@Slf4j
@RequiredArgsConstructor
@Service
public class ArticleCommentService {
    private final ArticleCommentRepository articleCommentRepository;
    private final ArticleRepository articleRepository;


    /**
     * 게시글 하부 댓글 목록
     * pagination
     *  offset : 1 ~
     *  limit : 5(default)
     *
     *   select *
     *     from article_comment
     *    where article_id = ?
     * order by id Desc
     *
     */
    @Transactional(readOnly = true)
    public ResponseArticleCommentListDto findAllArticleCommentUnderArticle(CustomPaginationRequest paginationRequest,
                                                                           Long articleId) {

        PageRequest pageRequest = PageRequest.of(paginationRequest.getOffset() - 1, paginationRequest.getLimit());
        Page<ResponseArticleCommentFindDto> allArticleCommentUnderArticle = articleCommentRepository.findAllArticleCommentUnderArticle(pageRequest, articleId);

        return ResponseArticleCommentListDto.builder()
                .responseArticleCommentFindDtos(allArticleCommentUnderArticle.getContent())
                .paginationResponse(CustomPaginationResponse.of(allArticleCommentUnderArticle.getTotalElements(), allArticleCommentUnderArticle.getTotalPages(), allArticleCommentUnderArticle.getNumber()))
                .build();
    }

    @Transactional(readOnly = true)
    public ResponseArticleCommentFindDto findOneArticleCommentByIdUnderArticle(Long articleId, Long articleCommentId) {
        ArticleComment articleComment = articleCommentRepository.findArticleCommentByIdAndArticleId(articleId, articleCommentId).orElseThrow(() -> new NotFoundException(NOT_FOUND_ARTICLE_COMMENT));
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
        confirmArticleCreatedUserId(articleComment.getCreatedBy().getId());
        articleComment.editArticleComment(editDto.getContent());
    }

    @Transactional
    public void removeArticleComment(Long articleId, Long articleCommentId) {
        ArticleComment articleComment = articleCommentRepository.findArticleCommentByIdAndArticleId(articleId, articleCommentId).orElseThrow(() -> new NotFoundException(NOT_FOUND_ARTICLE_COMMENT));
        confirmArticleCreatedUserId(articleComment.getCreatedBy().getId());
        articleCommentRepository.delete(articleComment);
    }

    private Long getSignInUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User)authentication.getPrincipal();
        return user.getId();
    }

    private void confirmArticleCreatedUserId(Long articleCommentCreatedUserId) {
        Long signInUserId = getSignInUserId();
        if(!articleCommentCreatedUserId.equals(signInUserId)) {
            throw new NotAllowedUserException(CustomErrorCode.NOT_ALLOWED_USER);
        }
    }
}
