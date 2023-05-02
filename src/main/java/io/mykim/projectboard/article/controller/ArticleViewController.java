package io.mykim.projectboard.article.controller;

import io.mykim.projectboard.article.dto.request.ArticleCreateDto;
import io.mykim.projectboard.article.dto.request.ArticleEditDto;
import io.mykim.projectboard.article.dto.response.ResponseArticleFindDto;
import io.mykim.projectboard.article.enums.SearchType;
import io.mykim.projectboard.article.service.ArticleService;
import io.mykim.projectboard.global.result.enums.CustomErrorCode;
import io.mykim.projectboard.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/articles")
public class ArticleViewController {
    private final ArticleService articleService;

    @GetMapping
    public String articlesView(@RequestParam(required = false) String searchKeyword,
                               @RequestParam(required = false, name = "searchType") SearchType searchType,
                               @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                               Model model) {

        log.info("[GET] /articles?searchKeyword={}&searchType={}&page={}&size={}&sort={} => articles View", searchKeyword, searchType, pageable.getOffset(), pageable.getPageSize(), pageable.getSort());
        model.addAttribute("articleSearchTypes", SearchType.values());
        model.addAttribute("hashtagSearchType", SearchType.HASHTAG.name());
        model.addAttribute("articles", articleService.findAllArticle(searchKeyword, searchType, pageable));

        return "articles/list";
    }

    @GetMapping("/hashtag")
    public String articlesHashtagSearchView(@RequestParam(required = false) String searchKeyword,
                                            @RequestParam(required = false, name = "searchType") SearchType searchType,
                                            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        log.info("[GET] /articles/hashtag?searchKeyword={}&searchType={}&page={}&size={}&sort={} => articles by Hashtag search View", searchKeyword, searchType, pageable.getOffset(), pageable.getPageSize(), pageable.getSort());

        model.addAttribute("hashtagSearchType", SearchType.HASHTAG.name());
        model.addAttribute("articles", articleService.findAllArticle(searchKeyword, searchType, pageable));

        return "articles/search-hashtag";
    }

    @GetMapping("/{articleId}")
    public String articlesDetailView(@PathVariable Long articleId, Model model) {
        log.info("[GET] /articles/{} =>  article Detail View", articleId);
        model.addAttribute("article", articleService.findOneArticle(articleId));
        return "articles/detail";
    }

    @GetMapping("/create")
    public String articleCreateView(Model model) {
        log.info("[GET] /articles/create =>  article create form View");
        model.addAttribute("article", new ArticleCreateDto());
        return "articles/create";
    }

    @PostMapping("/create")
    public String articleCreate(@Validated @ModelAttribute("article") ArticleCreateDto articleCreateDto,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {

        log.info("[POST] /articles/create  =>  article create, ArticleCreateDto = {}", articleCreateDto);

        if(bindingResult.hasErrors()) {
            log.info("validation errors = {}", bindingResult);
            return "articles/create";
        }

        Long articleId = articleService.createArticle(articleCreateDto);
        redirectAttributes.addAttribute("articleId", articleId);

        return "redirect:/articles/{articleId}";
    }

    @GetMapping("/{articleId}/edit")
    public String articleEditView(@PathVariable Long articleId,
                                  @AuthenticationPrincipal User user,
                                  Model model, HttpServletResponse response) throws IOException {
        log.info("[GET] /articles/{}/edit => article edit View", articleId);

        // 해당 게시글 작성자의 id와 현재 로그인 한 사용자의 id가 같은지 비교할 필요가 있음 : 본인이 작성한 글만 수정 및 삭제가 가능하다.
        ResponseArticleFindDto findArticleDto = articleService.findOneArticle(articleId);
        if(!findArticleDto.getUserId().equals(user.getId())) {
            response.sendError(HttpStatus.FORBIDDEN.value(), CustomErrorCode.NOT_ALLOWED_USER.getMessage());
        }

        model.addAttribute("article", findArticleDto);
        return "articles/edit";
    }

    @PostMapping("/{articleId}/edit")
    public String articleEdit(@PathVariable Long articleId, @AuthenticationPrincipal User user,
                              @Validated @ModelAttribute("article") ArticleEditDto articleEditDto,
                              BindingResult bindingResult, HttpServletResponse response) throws IOException {

        log.info("[POST] /articles/{}/edit => article edit, ArticleCreateDto = {}", articleId, articleEditDto);

        // 해당 게시글 작성자의 id와 현재 로그인 한 사용자의 id가 같은지 비교할 필요가 있음 : 본인이 작성한 글만 수정 및 삭제가 가능하다.
        ResponseArticleFindDto findArticleDto = articleService.findOneArticle(articleId);
        if(!findArticleDto.getUserId().equals(user.getId())) {
            response.sendError(HttpStatus.FORBIDDEN.value(), CustomErrorCode.NOT_ALLOWED_USER.getMessage());
        }

        if(bindingResult.hasErrors()) {
            log.info("validation errors = {}", bindingResult);

            bindingResult.getFieldErrors().forEach(e -> {
                log.error("error field = {}", e.getField());
                log.error("error message = {}", e.getDefaultMessage());
            });

            return "articles/edit";
        } else {
            articleService.editArticle(articleEditDto, articleId);
            return "redirect:/articles/{articleId}";
        }
    }

    @PostMapping("/{articleId}/delete")
    public void articleRemove(@PathVariable Long articleId, @AuthenticationPrincipal User user, HttpServletResponse response) throws IOException {
        log.info("[POST] /articles/{}/delete => article delete");

        // 해당 게시글 작성자의 id와 현재 로그인 한 사용자의 id가 같은지 비교할 필요가 있음 : 본인이 작성한 글만 수정 및 삭제가 가능하다.
        ResponseArticleFindDto findArticleDto = articleService.findOneArticle(articleId);
        if(!findArticleDto.getUserId().equals(user.getId())) {
            System.out.println("do this ");
            response.sendError(HttpStatus.FORBIDDEN.value(), CustomErrorCode.NOT_ALLOWED_USER.getMessage());
        } else {
            articleService.removeArticle(articleId);
            response.sendRedirect("/articles");
        }
    }

}
