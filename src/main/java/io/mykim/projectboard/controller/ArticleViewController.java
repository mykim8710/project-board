package io.mykim.projectboard.controller;

import io.mykim.projectboard.domain.entity.Article;
import io.mykim.projectboard.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/articles")
public class ArticleViewController {

    //private final ArticleService articleService;

    @GetMapping
    public String articlesView(Model model) {
        log.info("[GET] /articles  =>  articles View");
        model.addAttribute("articles", List.of());
        return "articles/index";
    }

    @GetMapping("/{articleId}")
    public String articlesDetailView(@PathVariable Long articleId, Model model) {
        log.info("[GET] /articles/{} =>  article Detail View", articleId);
        model.addAttribute("article", new Object());
        model.addAttribute("articleComments", List.of());
        return "articles/detail";
    }



}
