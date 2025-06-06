package com.ksamar.library.controller;

import com.alibaba.fastjson.JSONObject;
import com.ksamar.library.entitys.User;
import com.ksamar.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

/**
 * 登录控制器
 * @author KSaMar
 */
@RestController
@RequestMapping("/login")
public class LoginHandler {
    /**
     * 用户数据库操作接口
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * 管理员登录
     * @param user 用户信息
     * @return 登录信息
     */

    @PostMapping("/admin")
    public JSONObject adminLogin(@RequestBody User user,   HttpServletRequest request) {

        return login(user, "admin", request);
    }

    /**
     * 用户登录
     * @param user 用户信息
     * @return 登录信息
     */
    @PostMapping("/user")
    public JSONObject userLogin(@RequestBody User user,HttpServletRequest request) {
        return login(user, "user",request);
    }

    /**
     * 普通用户登录
     * 0 - 账号或密码错误
     * 1 - 登录成功
     * 2 - 账号冻结无法登录
     * @param user 用户表单
     * @return 登录状态
     */
    public JSONObject login(User user, String userGroups, HttpServletRequest request) {
        // 验证验证码
        System.err.println("*");



        // 封装表单
        Example<User> userExample = Example.of(user);
        // 搜索用户
        Optional<User> userOptional = userRepository.findOne(userExample);

        JSONObject loginMessage = new JSONObject();

        // 判断是否存在该用户信息
        if (userOptional.isPresent()) {
            User userTemp = userOptional.get();
            // 判断用户是否为指定用户组
            if (userGroups.equals(userTemp.getGroups())) {
                if (userTemp.getState() == 1) {
                    // 判断用户账号密码是否正确
                    if (user.getUsername().equals(userTemp.getUsername()) && user.getPassword().equals(userTemp.getPassword())) {
                        loginMessage.put("statusCode", 1);
                        loginMessage.put("username", user.getUsername());
                        loginMessage.put("message", "登录成功");
                        return loginMessage;
                    } else {
                        loginMessage.put("statusCode", 0);
                        loginMessage.put("message", "账号或密码错误");
                        return loginMessage;
                    }
                } else {
                    loginMessage.put("statusCode", 2);
                    loginMessage.put("message", "账号已被冻结");
                    return loginMessage;
                }
            } else {
                loginMessage.put("statusCode", 0);
                loginMessage.put("message", "用户组不匹配");
                return loginMessage;
            }
        } else {
            loginMessage.put("statusCode", 0);
            loginMessage.put("message", "用户不存在");
            return loginMessage;
        }
    }

}
