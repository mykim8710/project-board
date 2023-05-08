package io.mykim.projectboard.article.service;

import io.mykim.projectboard.article.dto.request.ArticleCommentCreateDto;
import io.mykim.projectboard.article.dto.request.ArticleCommentEditDto;
import io.mykim.projectboard.article.dto.response.ResponseArticleCommentFindDto;
import io.mykim.projectboard.article.entity.Article;
import io.mykim.projectboard.article.entity.ArticleComment;
import io.mykim.projectboard.article.repository.ArticleCommentRepository;
import io.mykim.projectboard.article.repository.ArticleRepository;
import io.mykim.projectboard.global.pageable.CustomPaginationResponse;
import io.mykim.projectboard.global.result.enums.CustomErrorCode;
import io.mykim.projectboard.global.result.exception.NotAllowedUserException;
import io.mykim.projectboard.global.result.exception.NotFoundException;
import io.mykim.projectboard.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static io.mykim.projectboard.global.result.enums.CustomErrorCode.NOT_FOUND_ARTICLE_COMMENT;
import static io.mykim.projectboard.global.result.enums.CustomErrorCode.NOT_FOUND_PARENT_ARTICLE_COMMENT;

@Slf4j
@RequiredArgsConstructor
@Service
public class ArticleCommentService {
    private final ArticleCommentRepository articleCommentRepository;
    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public List<ResponseArticleCommentFindDto> findAllArticleCommentUnderArticle(Long articleId) {
        return organizeChildComments(articleCommentRepository.findAllArticleCommentUnderArticle(articleId));
    }

    @Transactional(readOnly = true)
    public ResponseArticleCommentFindDto findOneArticleCommentByIdUnderArticle(Long articleId, Long articleCommentId) {
        ArticleComment articleComment = articleCommentRepository.findArticleCommentByIdAndArticleId(articleId, articleCommentId).orElseThrow(() -> new NotFoundException(NOT_FOUND_ARTICLE_COMMENT));
        return ResponseArticleCommentFindDto.of(articleComment);
    }

    @Transactional
    public Long createNewArticleComment(ArticleCommentCreateDto createDto, Long articleId) {
        Article findArticle = articleRepository.findById(articleId).orElseThrow(() -> new NotFoundException(CustomErrorCode.NOT_FOUND_ARTICLE));
        ArticleComment articleComment = ArticleComment.of(createDto.getContent(), findArticle);

        if(createDto.getParentArticleCommentId() != null) {
            ArticleComment parentArticleComment = articleCommentRepository.findById(createDto.getParentArticleCommentId()).orElseThrow(() -> new NotFoundException(NOT_FOUND_PARENT_ARTICLE_COMMENT));
            parentArticleComment.addChildArticleComment(articleComment);
        }

        articleCommentRepository.save(articleComment);
        return articleComment.getId();
    }

    @Transactional
    public void editArticleComment(ArticleCommentEditDto editDto, Long articleId, Long articleCommentId) {
        ArticleComment articleComment = articleCommentRepository.findArticleCommentByIdAndArticleId(articleId, articleCommentId).orElseThrow(() -> new NotFoundException(NOT_FOUND_ARTICLE_COMMENT));

        // 본인이 작성한 댓글만 수정 가능
        confirmArticleCommentCreatedUserId(articleComment.getCreatedBy().getId());

        articleComment.editArticleComment(editDto.getContent());
    }

    @Transactional
    public void removeArticleComment(Long articleId, Long articleCommentId) {
        ArticleComment articleComment = articleCommentRepository.findArticleCommentByIdAndArticleId(articleId, articleCommentId).orElseThrow(() -> new NotFoundException(NOT_FOUND_ARTICLE_COMMENT));

        // 본인이 작성한 댓글만 삭제 가능
        confirmArticleCommentCreatedUserId(articleComment.getCreatedBy().getId());

        // todo : 자식댓글이 일일이 개별삭제로 진행되고있음 -> 한방에 지울수있도록 수정필요
        articleCommentRepository.delete(articleComment);
    }

    private Long getSignInUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User)authentication.getPrincipal();
        return user.getId();
    }

    private void confirmArticleCommentCreatedUserId(Long articleCommentCreatedUserId) {
        Long signInUserId = getSignInUserId();
        if(!articleCommentCreatedUserId.equals(signInUserId)) {
            throw new NotAllowedUserException(CustomErrorCode.NOT_ALLOWED_USER);
        }
    }

    private List<ResponseArticleCommentFindDto> organizeChildComments(List<ResponseArticleCommentFindDto> articleCommentFindDtos) {
        Map<Long, ResponseArticleCommentFindDto> map = new HashMap<>();

        articleCommentFindDtos.forEach(articleCommentDto -> {
            Long articleCommentId = articleCommentDto.getArticleCommentId();
            if(map.put(articleCommentId, articleCommentDto) != null) {
                throw new IllegalArgumentException("Duplicate Key!!!");
            }
        });

        for (ResponseArticleCommentFindDto articleCommentFindDto : map.values()) {
            if(articleCommentFindDto.hasParentArticleComment()) {
                // 부모 댓글이 있다면
                Long parentArticleCommentId = articleCommentFindDto.getParentArticleCommentId();        // 부모댓글 id
                ResponseArticleCommentFindDto parentArticleComment = map.get(parentArticleCommentId);   // 부모댓글 객체
                parentArticleComment.getChildArticleComments().add(articleCommentFindDto);
            }
        }

        List<ResponseArticleCommentFindDto> result = new ArrayList<>();
        for (ResponseArticleCommentFindDto comment : map.values()) {
            if(!comment.hasParentArticleComment()) {
                result.add(comment);
            }
        }

        return result;
    }
}
