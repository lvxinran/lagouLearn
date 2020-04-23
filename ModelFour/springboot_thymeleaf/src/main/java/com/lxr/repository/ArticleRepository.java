package com.lxr.repository;

import com.lxr.pojo.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author lvxinran
 * @date 2020/4/23
 * @discribe
 */
public interface ArticleRepository extends JpaRepository<Article,Integer>, JpaSpecificationExecutor<Article> {

}
