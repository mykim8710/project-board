package io.mykim.projectboard.controller;

import io.mykim.projectboard.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/articles")
public class ArticleViewController {

    private final ArticleService articleService;

    @GetMapping
    public String articlesView(Model model) {
        log.info("[GET] /articles  =>  articles View");
        model.addAttribute("articles", List.of());
        return "articles/index";
    }



}
