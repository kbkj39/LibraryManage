package com.ksamar.library.controller;

import com.ksamar.library.entitys.VerifyCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

@Api(tags = "验证码")
@RestController
@CrossOrigin
@RequestMapping("/code")
public class VerifyController {



    @ApiOperation(value = "验证码生成")
    @GetMapping("/verify")
    public void verify(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 创建验证码并生成图像
        VerifyCode code = new VerifyCode();
        BufferedImage image = code.createImage();

        // 获取当前会话，如果不存在则创建一个新会话
        HttpSession session = request.getSession(true);
        session.setMaxInactiveInterval(30 * 60); // 设置为30分钟

        // 将验证码文本存储到会话中
        session.setAttribute("verifyCode", code.getText());

        //验证码
        System.err.println(code.getText());



        // 设置响应内容类型为 JPEG 图像
        response.setContentType("image/jpeg");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // 不缓存

        // 输出验证码图像
        ImageIO.write(image, "jpg", response.getOutputStream());
    }
    // 你可以添加一个方法用于验证验证码


        @PostMapping("/verify/check")
        public ResponseEntity<Boolean> checkVerifyCode(@RequestBody Map<String, String> requestBody, HttpServletRequest request) {
            HttpSession session = request.getSession(false);
            if (session == null) {
                return ResponseEntity.badRequest().body(false);
            }

            String userInput = requestBody.get("code");
            if (userInput == null || userInput.isEmpty()) {
                return ResponseEntity.badRequest().body(false);
            }

            String correctCode = (String) session.getAttribute("verifyCode");
            if (correctCode != null && userInput.equalsIgnoreCase(correctCode)) {
                // 验证码正确
                return ResponseEntity.ok(true);
            } else {
                // 验证码错误
                return ResponseEntity.ok(false);
            }
        }
    }
