package com.lxr.pojo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author lvxinran
 * @date 2020/4/23
 * @discribe
 */
@Component
public class Page {
    private int currentPage=1;    //当前页数
    private int totalPages;       //总页数
    private int totalArticle;//记录总行数

    @Value("${page.size}")
    private int pageSize=2;    //每页记录行数
    private int nextPage;        //下一页
    private int prefPage;       //前一页

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        totalPages = totalArticle % pageSize == 0 ? totalArticle / pageSize : totalArticle / pageSize + 1;

        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalArticle() {
        return totalArticle;
    }

    public void setTotalArticle(int totalArticle) {
        this.totalArticle = totalArticle;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getNextPage() {
        if (currentPage < totalPages) {
            nextPage = currentPage + 1;
        } else {
            nextPage = currentPage;
        }
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getPrefPage() {
        if (currentPage > 1) {
            prefPage = currentPage - 1;
        } else {
            prefPage = currentPage;
        }
        return prefPage;
    }

    public void setPrefPage(int prefPage) {
        this.prefPage = prefPage;
    }
}
