package com.as.oblog.service.impl;

import com.as.oblog.mapper.UserMapper;
import com.as.oblog.pojo.User;
import com.as.oblog.service.UserService;
import com.as.oblog.utils.Md5Util;
import com.as.oblog.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author 12aaa-zone
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByUserName(String username) {
        User user = userMapper.findByUserName(username);
        return user;
    }

    @Override
    public void register(String username, String password) {
        //加密
        String md5String = Md5Util.getMD5String(password);
        //添加
        userMapper.add(username,md5String);
    }

    @Override
    public void update(User user){
        user.setUpdateTime(LocalDateTime.now());
        userMapper.update(user);
    }

    @Override
    public void updateAvatar(String avatarUrl,Integer id){
        userMapper.updateAvatar(avatarUrl,id);
    }

    @Override
    public void updatePwd(String password){

        Map<String,Object> claims = ThreadLocalUtil.get();
        Integer id = (Integer) claims.get("id");
        //加密
        String md5String = Md5Util.getMD5String(password);

        // 添加
        userMapper.updatePwd(id,md5String);
    }

}
