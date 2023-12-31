package com.as.oblog.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;


import java.time.LocalDateTime;

/**
 * @author 12aaa-zone
 */

@Data
public class User {

    @NotNull(message = "id不能为空")
    private Integer id;//主键ID

    private String username;//用户名

    @JsonIgnore //在springmvc中转化为json格式时将会无视这个属性，防止响应查询时返回密文为视图
    private String password;//密码


    @NotEmpty(message = "昵称不能为空")
    @Pattern(regexp = "^[\\w\\s\\-\\.\\_\\p{L}\\p{N}\\p{Punct}]{1,50}$", message = "昵称不符合规范")
    private String nickname; // 昵称

    @NotEmpty()
    @Email(message = "不符合邮件地址的格式")
    private String email;//邮箱

    private String userPic;//用户头像地址
    private LocalDateTime createTime;//创建时间
    private LocalDateTime updateTime;//更新时间
}
