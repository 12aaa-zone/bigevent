package com.as.oblog.controller;

import com.as.oblog.pojo.Article;
import com.as.oblog.pojo.PageBean;
import com.as.oblog.pojo.Result;
import com.as.oblog.service.ArticleService;
import com.as.oblog.utils.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author 12aaa-zone
 */

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    // 测试接口
    @GetMapping("/list")
    public Result<String> list(@RequestHeader(name = "Authorization") String token,
                               HttpServletResponse response){
        try{
            Map<String, Object> claims = JwtUtil.parseToken(token);
        } catch (Exception e){
            response.setStatus(401);
            return Result.error("未登录，或者验证失败");
        }
        return Result.success("测试接口，返回一些数据....");
    }



    @PostMapping
    public Result add(@RequestBody @Validated Article article) {
        articleService.add(article);
        return Result.success();
    }

    @GetMapping
    public Result<PageBean<Article>> list(
            Integer pageNum,
            Integer pageSize,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String state
    ) {
        PageBean<Article> pb =  articleService.list(pageNum,pageSize,categoryId,state);
        return Result.success(pb);
    }

}
