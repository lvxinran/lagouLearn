package com.lxr.controller;

import com.lxr.pojo.Article;
import com.lxr.pojo.Page;
import com.lxr.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


/**
 * @author lvxinran
 * @date 2020/4/22
 * @discribe
 */
@Controller
@RequestMapping("/")
public class LoginController {


    @Autowired
    private ArticleService articleService;

    @Autowired
    Page p;
    @RequestMapping(value = "/main",method = RequestMethod.GET)
    public String toMainPage(Model model,int page){
        p.setTotalArticle(articleService.findAllArticle().size());
        p.setCurrentPage(page);
        List<Article> articleList = articleService.findByPage(page - 1,p.getPageSize());
        model.addAttribute("allArticle",articleList);
        model.addAttribute("currPage",p);
        return "index";
    }
    @RequestMapping(value = "/show",method = RequestMethod.GET)
    public String goToMainPage(RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("page",1);
        return "redirect:main";
    }
}
