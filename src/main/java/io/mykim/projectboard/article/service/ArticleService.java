package io.mykim.projectboard.article.service;

import io.mykim.projectboard.article.dto.request.ArticleCreateDto;
import io.mykim.projectboard.article.dto.request.ArticleEditDto;
import io.mykim.projectboard.article.dto.request.ArticleSearchCondition;
import io.mykim.projectboard.article.dto.response.ResponseArticleFindDto;
import io.mykim.projectboard.article.dto.response.ResponseArticleListDto;
import io.mykim.projectboard.article.entity.Article;
import io.mykim.projectboard.article.entity.ArticleHashTag;
import io.mykim.projectboard.article.entity.Hashtag;
import io.mykim.projectboard.article.enums.SearchType;
import io.mykim.projectboard.article.repository.ArticleRepository;
import io.mykim.projectboard.article.repository.HashtagRepository;
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

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
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

    @Transactional
    public Long createArticle(ArticleCreateDto createDto) {
        Set<Hashtag> hashtags = hashtagService.renewHashtags(createDto.getHashtags());

        // create entity
        Article article = Article.createArticle(createDto, hashtags);
        return articleRepository.save(article).getId();
    }










    @Transactional
    public void editArticle(ArticleEditDto editDto, Long articleId) {
        Article findArticle = articleRepository.findById(articleId).orElseThrow(() -> new NotFoundException(CustomErrorCode.NOT_FOUND_ARTICLE));
        confirmArticleCreatedUserId(findArticle.getCreatedBy().getId());
        findArticle.editArticle(editDto);
    }

    @Transactional
    public void removeArticle(Long articleId) {
        Article findArticle = articleRepository.findById(articleId).orElseThrow(() -> new NotFoundException(CustomErrorCode.NOT_FOUND_ARTICLE));
        confirmArticleCreatedUserId(findArticle.getCreatedBy().getId());
        articleRepository.delete(findArticle);
    }

    private Long getSignInUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User)authentication.getPrincipal();
        return user.getId();
    }

    private void confirmArticleCreatedUserId(Long articleCreatedUserId) {
        Long signInUserId = getSignInUserId();
        if(!articleCreatedUserId.equals(signInUserId)) {
            throw new NotAllowedUserException(CustomErrorCode.NOT_ALLOWED_USER);
        }
    }
}
