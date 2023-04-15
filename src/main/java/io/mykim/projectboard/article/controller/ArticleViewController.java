package io.mykim.projectboard.article.controller;

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

    @GetMapping
    public String articlesView(Model model) {
        log.info("[GET] /articles  =>  articles View");
        model.addAttribute("articles", List.of());
        return "articles/list";
    }

    @GetMapping("/{articleId}")
    public String articlesDetailView(@PathVariable Long articleId, Model model) {
        log.info("[GET] /articles/{} =>  article Detail View", articleId);
        model.addAttribute("article", new Object());
        model.addAttribute("articleComments", List.of());
        return "articles/detail";
    }

    @GetMapping("/create")
    public String articleCreateView() {
        log.info("[GET] /articles/create =>  article create View");
        return "articles/create";
    }

    @GetMapping("/edit/{articleId}")
    public String articleEditView(@PathVariable Long articleId, Model model) {
        log.info("[GET] /articles/edit/{} =>  article edit View", articleId);
        model.addAttribute("article", new Object());
        return "articles/edit";
    }

}
