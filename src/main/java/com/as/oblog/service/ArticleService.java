package com.as.oblog.service;

import com.as.oblog.pojo.Article;
import com.as.oblog.pojo.PageBean;

/**
 * @author 12aaa-zone
 */
public interface ArticleService {
    //新增文章
    void add(Article article);

    //条件分页列表查询
    PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state);
}
