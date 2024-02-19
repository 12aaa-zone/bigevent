package com.as.oblog.pojo;



import com.as.oblog.anno.State;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author 12aaa-zone
 */
@Data
public class Article {

    private Integer id;//主键ID

    @NotEmpty(message = "文章标题不能为空")
    @Pattern(regexp = "^\\S{1,25}$",message = "文章标题长度必须在1到25个字符之间------ ")
    private String title;//文章标题

    @NotEmpty(message = "文章内容不能为空")
    private String content;//文章内容

    @NotEmpty(message = "封面图像链接不能为空")
    @URL(message = "封面图像URL格式不正确")
    private String coverImg;//封面图像

    @State(message = "state参数的值不符合验证规定")
    private String state;//发布状态 已发布|草稿

    @NotNull(message = "文章分类ID不能为空")
    private Integer categoryId;//文章分类id

    private Integer createUser;//创建人ID

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;//创建时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;//更新时间

    // 2024/01/20新增功能，可能出问题
    private Integer viewCount; //观看数目 必填

    private Integer likeCount;  //点赞数目 必填

    private Boolean commentStatus; //是否启用评论 必填

    private Boolean recommendStatus; //是否推荐  必填

    private String password; //文章密码

    private String tips; //留言

    private Boolean viewStatus; //是否可见 必填

    @NotNull(message = "文章标签ID不能为空")
    private Integer labelId; //标签 必填

}
