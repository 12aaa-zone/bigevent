package com.as.oblog.controller;

import com.as.oblog.pojo.Result;
import com.as.oblog.pojo.User;
import com.as.oblog.service.UserService;
import com.as.oblog.utils.JwtUtil;
import com.as.oblog.utils.Md5Util;
import com.as.oblog.utils.ThreadLocalUtil;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.javapoet.ParameterSpec;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * 当在类上使用 @RestController 注解时，它表明这个类是一个控制器
 * 其中的所有方法都会默认返回响应体（response body）
 * 即它们的返回值会自动被转换为JSON或其他适合的响应格式。
 * 本项目里，即指将result对象json序列化返回
 *
 * 这里user业务视图根映射地址为/user
 *
 * @author 12aaa-zone
 */

/**
 * 视图类user-controller，分情况调用业务函数并将其映射至servlet视图
 */
@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    /**
     * 业务类user-service，包装一系列关于service的基础业务函数
     */

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    // 具体正则细节交给前端处理，这里宽松处理
    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^\\S{5,16}$")  String username,
                           @Pattern(regexp = "^\\S{6,32}$")  String password) {

        //查询用户
        User u = userService.findByUserName(username);
        if (u == null) {
            //用户名没有占用，则可以注册
            userService.register(username, password);
            return Result.success();
        } else {
            //占用
            return Result.error("用户名已被占用！");
        }
    }


    @PostMapping("/login")
    public Result<String> login(@Pattern(regexp = "^\\S{5,16}$") String username,
                                @Pattern(regexp = "^\\S{6,32}$") String password) {
        //根据用户名查询用户
        User loginUser = userService.findByUserName(username);
        //判断该用户是否存在
        if (loginUser == null) {
            return Result.error("用户名错误");
        }

        //判断密码是否正确  loginUser对象中的password是密文
        if (Md5Util.getMD5String(password).equals(loginUser.getPassword())) {

            //登录成功
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", loginUser.getId());
            claims.put("username", loginUser.getUsername());
            String token = JwtUtil.genToken(claims);

            //将token存入redis中
            ValueOperations<String,String> operations = stringRedisTemplate.opsForValue();
            operations.set(token,token,24,TimeUnit.HOURS);

            return Result.success(token);
        }


        return Result.error("密码错误");
    }


    @GetMapping("/userInfo")
    public Result<User> userInfo(/*@RequestHeader(name = "Authorization") String token*/){
        Map<String, Object> claims = ThreadLocalUtil.get();
        String username = (String) claims.get("username");
        User user = userService.findByUserName(username);
        return Result.success(user);
    }


    // 全部资源更新， 用put，幂等，不安全
    @PutMapping("/update")
    public Result update(@RequestBody @Validated User user){
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer userId = (Integer) claims.get("id");

        if(!user.getId().equals(userId)){
            return Result.error("请求id与当前登录账号不匹配，请重新登录或更换请求！");
        }
        userService.update(user);
        return Result.success();
    }

    //  部分资源更新， 用patch，非幂等，不安全
    // 这里使用OSS服务存储头像icon实体
    @PatchMapping("/updateAvatar")
    public Result updateAvatar(@RequestParam @URL(message = "Avatar:提交地址不合法") String avatarUrl){
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer id = (Integer) claims.get("id");
        userService.updateAvatar(avatarUrl,id);
        return Result.success();
    }

    @PatchMapping("/updatePwd")
    public Result updatePwd(@RequestBody Map<String, String> params,@RequestHeader("Authorization") String token){
        String oldPwd = params.get("old_pwd");
        String newPwd = params.get("new_pwd");
        String rePwd = params.get("re_pwd");

        // 检测是否为空
        if(!StringUtils.hasLength(oldPwd) || !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd)){
            return Result.error("缺少必要的参数");
        }


        //验证原密码
        Map<String,Object> claims = ThreadLocalUtil.get();
        String username = (String) claims.get("username");
        User loginUser = userService.findByUserName(username);
        if(!loginUser.getPassword().equals(Md5Util.getMD5String(oldPwd))){
            return Result.error("原密码填写不正确");
        }


        //新密码两次输入是否一致
        if(!rePwd.equals(newPwd)){
            return Result.error("两次填写密码不一致");
        }

        //全部通过，更新密码
        userService.updatePwd(rePwd);

        //删除redis中对应的token
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.getOperations().delete(token);

        return Result.success();
    }
}
