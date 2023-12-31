package com.as.oblog.interceptors;

import com.as.oblog.utils.JwtUtil;
import com.as.oblog.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

/**
 * @author 12aaa-zone
 */

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        //令牌验证
        String token = request.getHeader("Authorization");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        //验证token
        try {
            //从redis获得相同的token
            ValueOperations<String,String> operations = stringRedisTemplate.opsForValue();
            String redisToken = operations.get(token);
            if(redisToken == null){
                throw new RuntimeException("token timed out！");
            }

            // 解析成功不抛出异常，则放行
            Map<String, Object> claims = JwtUtil.parseToken(redisToken);

            //把用户数据存储到ThreadLocal中
            ThreadLocalUtil.set(claims);
            return true;
        } catch (Exception e) {
            //http响应状态码为401
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 设置状态码为401
            response.getWriter().write("Unauthorized: Invalid or expired token."); // 发送错误消息
            return false;
        }
    }



    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) throws Exception {
        //清空ThreadLocal中的数据
        ThreadLocalUtil.remove();
    }
}
