package com.lxr.service;

import com.lxr.pojo.Article;
import com.lxr.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lvxinran
 * @date 2020/4/23
 * @discribe
 */
@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    public List<Article> findAllArticle(){
        return articleRepository.findAll();
    }
    public List<Article> findByPage(int page,int size){
        Pageable pageable = PageRequest.of(page,size);
        return articleRepository.findAll(pageable).toList();
    }


}
