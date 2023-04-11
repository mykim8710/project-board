package io.mykim.projectboard.service;

import io.mykim.projectboard.domain.entity.Article;
import io.mykim.projectboard.dto.request.ArticleCreateDto;
import io.mykim.projectboard.dto.request.ArticleEditDto;
import io.mykim.projectboard.dto.request.ArticleSearchCondition;
import io.mykim.projectboard.dto.response.ResponseArticleFindDto;
import io.mykim.projectboard.dto.response.ResponseArticleListDto;
import io.mykim.projectboard.global.select.pagination.CustomPaginationRequest;
import io.mykim.projectboard.global.select.pagination.CustomPaginationResponse;
import io.mykim.projectboard.global.select.sort.CustomSortingRequest;
import io.mykim.projectboard.global.result.enums.CustomErrorCode;
import io.mykim.projectboard.global.result.exception.NotFoundException;
import io.mykim.projectboard.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    /**
     * 페이징
     *  ㄴ offset : n번째 페이지부터
     *  ㄴ limit : n개 가져오기
     * 정렬 : order by "" asc or desc => id, title, lastModifiedAt
     * 검색 : keyword.contain("") => article_title, article_content, article_hashtag, user_nickname
     */
    @Transactional(readOnly = true)
    public ResponseArticleListDto findAllArticle(CustomPaginationRequest paginationRequest, CustomSortingRequest sortingRequest, ArticleSearchCondition searchCondition) {
        PageRequest pageRequest = PageRequest.of(paginationRequest.getOffset() - 1, paginationRequest.getLimit(), Sort.by(sortingRequest.of()));
        Page<ResponseArticleFindDto> allArticle = articleRepository.findAllArticle(pageRequest, searchCondition);

        return ResponseArticleListDto.builder()
                .responseArticleFindDtos(allArticle.getContent())
                .paginationResponse(CustomPaginationResponse.of(allArticle.getTotalElements(), allArticle.getTotalPages(), allArticle.getNumber()))
                .build();
    }

    @Transactional(readOnly = true)
    public ResponseArticleFindDto findOneArticle(Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new NotFoundException(CustomErrorCode.NOT_FOUND_ARTICLE));
        // entity to dto
        return ResponseArticleFindDto.of(article);
    }

    @Transactional
    public Long createArticle(ArticleCreateDto createDto) {
        // dto to entity
        Article article = Article.of(createDto);
        return articleRepository.save(article).getId();
    }

    @Transactional
    public void editArticle(ArticleEditDto editDto, Long articleId) {
        Article findArticle = articleRepository.findById(articleId).orElseThrow(() -> new NotFoundException(CustomErrorCode.NOT_FOUND_ARTICLE));
        findArticle.editArticle(editDto);
    }

    @Transactional
    public void removeArticle(Long articleId) {
        Article findArticle = articleRepository.findById(articleId).orElseThrow(() -> new NotFoundException(CustomErrorCode.NOT_FOUND_ARTICLE));
        articleRepository.delete(findArticle);
    }

}
