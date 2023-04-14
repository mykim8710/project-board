package io.mykim.projectboard.article.service;

import io.mykim.projectboard.article.entity.Article;
import io.mykim.projectboard.article.entity.ArticleComment;
import io.mykim.projectboard.article.dto.request.ArticleCommentCreateDto;
import io.mykim.projectboard.article.dto.request.ArticleCommentEditDto;
import io.mykim.projectboard.article.dto.response.ResponseArticleCommentFindDto;
import io.mykim.projectboard.article.dto.response.ResponseArticleCommentListDto;
import io.mykim.projectboard.global.result.enums.CustomErrorCode;
import io.mykim.projectboard.global.result.exception.NotFoundException;
import io.mykim.projectboard.global.select.pagination.CustomPaginationRequest;
import io.mykim.projectboard.global.select.pagination.CustomPaginationResponse;
import io.mykim.projectboard.article.repository.ArticleCommentRepository;
import io.mykim.projectboard.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.mykim.projectboard.global.result.enums.CustomErrorCode.NOT_FOUND_ARTICLE_COMMENT;

@Slf4j
@RequiredArgsConstructor
@Service
public class ArticleCommentService {
    private final ArticleCommentRepository articleCommentRepository;
    private final ArticleRepository articleRepository;

//    @Transactional(readOnly = true)
//    public List<ResponseArticleCommentFindDto> findAllArticleComment() {
//
//        return null;
//    }
//
//    @Transactional(readOnly = true)
//    public ResponseArticleCommentFindDto findOneArticleCommentById(Long articleCommentId) {
//        ArticleComment articleComment = articleCommentRepository.findById(articleCommentId).orElseThrow(() -> new NotFoundException(CustomErrorCode.NOT_FOUND_ARTICLE_COMMENT));
//        return ResponseArticleCommentFindDto.of(articleComment);
//    }


    /**
     * 게시글 하부 댓글 목록
     * pagination
     *  offset : 1 ~
     *  limit : 5(default)
     * Sorting : lastModifiedAt Desc
     * Search : content....
     *
     *   select *
     *     from article_comment
     *    where article_id = ?
     *      and content like %keyword%
     * order by lastModifiedAt Desc
     *
     */
    @Transactional(readOnly = true)
    public ResponseArticleCommentListDto findAllArticleCommentUnderArticle(CustomPaginationRequest paginationRequest,
                                                                           Long articleId, String keyword) {

        // todo :
        //  현재 sorting은 lastModifiedAt Desc로 고정
        //  sorting, paging(limit : 5로 고정), search에 대해 뷰 개발하면서 수정가능성이 있음
        PageRequest pageRequest = PageRequest.of(paginationRequest.getOffset() - 1, 5);
        Page<ResponseArticleCommentFindDto> allArticleCommentUnderArticle = articleCommentRepository.findAllArticleCommentUnderArticle(pageRequest, articleId, keyword);

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
        articleComment.editArticleComment(editDto.getContent());
    }

    @Transactional
    public void removeArticleComment(Long articleId, Long articleCommentId) {
        ArticleComment articleComment = articleCommentRepository.findArticleCommentByIdAndArticleId(articleId, articleCommentId).orElseThrow(() -> new NotFoundException(NOT_FOUND_ARTICLE_COMMENT));
        articleCommentRepository.delete(articleComment);
    }
}
