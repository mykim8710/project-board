package io.mykim.projectboard.article.service;

import io.mykim.projectboard.article.dto.request.ArticleCreateDto;
import io.mykim.projectboard.article.dto.request.ArticleEditDto;
import io.mykim.projectboard.article.dto.request.ArticleSearchCondition;
import io.mykim.projectboard.article.dto.response.ResponseArticleFindDto;
import io.mykim.projectboard.article.dto.response.ResponseArticleForEditDto;
import io.mykim.projectboard.article.dto.response.ResponseArticleListDto;
import io.mykim.projectboard.article.entity.Article;
import io.mykim.projectboard.article.entity.Hashtag;
import io.mykim.projectboard.article.enums.SearchType;
import io.mykim.projectboard.article.repository.ArticleHashtagRepository;
import io.mykim.projectboard.article.repository.ArticleRepository;
import io.mykim.projectboard.global.config.security.dto.PrincipalDetail;
import io.mykim.projectboard.global.pageable.CustomPaginationResponse;
import io.mykim.projectboard.global.pageable.PageableRequestCondition;
import io.mykim.projectboard.global.result.enums.CustomErrorCode;
import io.mykim.projectboard.global.result.exception.NotAllowedUserException;
import io.mykim.projectboard.global.result.exception.NotFoundException;
import io.mykim.projectboard.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleHashtagRepository articleHashtagRepository;
    private final HashtagService hashtagService;

    @Transactional(readOnly = true)
    public ResponseArticleListDto findAllArticle(String searchKeyword, SearchType searchType, Pageable pageable) {
        Page<ResponseArticleFindDto> findArticles = articleRepository.findAllArticle(pageable, new ArticleSearchCondition(searchKeyword, searchType));

        return ResponseArticleListDto.builder()
                                        .responseArticleFindDtos(findArticles.getContent())
                                        .paginationResponse(CustomPaginationResponse.of(findArticles.getTotalElements(), findArticles.getTotalPages(), findArticles.getNumber()))
                                        .pageableRequestCondition(PageableRequestCondition.builder()
                                                .page(pageable.getOffset())
                                                .size(pageable.getPageSize())
                                                .sort(pageable.getSort())
                                                .searchKeyword(searchKeyword)
                                                .searchType(searchType)
                                                .build())
                                        .build();
    }

    @Transactional(readOnly = true)
    public ResponseArticleFindDto findOneArticle(Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new NotFoundException(CustomErrorCode.NOT_FOUND_ARTICLE));

        // entity to dto
        return ResponseArticleFindDto.from(article);
    }

    public long getTotalArticleCount() {
        return articleRepository.count();
    }

    @Transactional(readOnly = true)
    public ResponseArticleForEditDto findOneArticleForEdit(Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new NotFoundException(CustomErrorCode.NOT_FOUND_ARTICLE));
        return ResponseArticleForEditDto.from(article);
    }

    @Transactional
    public Long createArticle(ArticleCreateDto createDto, User user) {
        Set<Hashtag> hashtags = hashtagService.renewHashtags(createDto.getHashtags());

        // create entity
        Article article = Article.createArticle(createDto, hashtags, user);
        return articleRepository.save(article).getId();
    }

    @Transactional
    public void editArticle(ArticleEditDto editDto, Long articleId) {
        Article findArticle = articleRepository.findById(articleId).orElseThrow(() -> new NotFoundException(CustomErrorCode.NOT_FOUND_ARTICLE));
        confirmArticleCreatedUserId(findArticle.getUser().getId());

        // 기존 Article - Hashtag 데이터 삭제
        articleHashtagRepository.deleteAllByArticleId(articleId);
        articleHashtagRepository.flush();

        Set<Hashtag> hashtags = hashtagService.renewHashtags(editDto.getHashtags());
        findArticle.editArticle(editDto, hashtags);
    }

    @Transactional
    public void removeArticle(Long articleId) {
        Article findArticle = articleRepository.findById(articleId).orElseThrow(() -> new NotFoundException(CustomErrorCode.NOT_FOUND_ARTICLE));
        confirmArticleCreatedUserId(findArticle.getUser().getId());

        // todo : 게시글 하부 ArticleComment(자식 ArticleComment,,,,), ArticleHashtag 일일이 개별삭제로 진행되고있음 -> 한방에 지울수있도록 수정필요
        articleRepository.delete(findArticle);
    }

    private Long getSignInUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetail principalDetail = (PrincipalDetail)authentication.getPrincipal();
        return principalDetail.getUser().getId();
    }

    private void confirmArticleCreatedUserId(Long articleCreatedUserId) {
        Long signInUserId = getSignInUserId();
        if(!articleCreatedUserId.equals(signInUserId)) {
            throw new NotAllowedUserException(CustomErrorCode.NOT_ALLOWED_USER);
        }
    }
}
