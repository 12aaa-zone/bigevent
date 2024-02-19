package com.as.oblog.service;

import com.as.oblog.pojo.Article;
import com.as.oblog.pojo.PageBean;
import com.as.oblog.pojo.Result;
import com.github.pagehelper.Page;

/**
 * @author 12aaa-zone
 */
public interface ArticleService {

    //新增文章
    Result add(Article article);

    //条件分页列表查询
    PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state);


    /*
    //删除文章
    Result delete(Integer id);

    //更新文章
    Result updates(Article article);


    //根据id获取文章
    Result<Article> getById(Integer id);
    */



}
