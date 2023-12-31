package com.as.oblog.service.impl;

import com.as.oblog.mapper.ArticleMapper;
import com.as.oblog.pojo.Article;
import com.as.oblog.pojo.PageBean;
import com.as.oblog.service.ArticleService;
import com.as.oblog.utils.ThreadLocalUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author 12aaa-zone
 */

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public void add(Article article) {
        //补充属性值
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());

        Map<String,Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        article.setCreateUser(userId);

        articleMapper.add(article);
    }

    @Override
    public PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state) {
        //1.创建PageBean对象
        PageBean<Article> pageBean = new PageBean<>();

        //2.开启mybatis分页查询 PageHelper
        //使用了一种称为"ThreadLocal"的机制来确保它仅对紧随其后的第一个MyBatis查询操作生效
        //而不影响全局或之后的其他查询。
        PageHelper.startPage(pageNum,pageSize);

        //3.调用mapper
        Map<String,Object> claims = ThreadLocalUtil.get();
        Integer userId = (Integer) claims.get("id");

        List<Article> articles = articleMapper.list(userId,categoryId,state);

        //Page中提供了方法,可以获取PageHelper分页查询后 得到的总记录条数和当前页数据
        Page<Article> page = (Page<Article>) articles;

        //把数据填充到PageBean对象中
        pageBean.setTotal(page.getTotal());
        pageBean.setItems(page.getResult());
        return pageBean;
    }
}
