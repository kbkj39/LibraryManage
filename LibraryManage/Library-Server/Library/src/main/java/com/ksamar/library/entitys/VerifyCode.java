package com.ksamar.library.entitys;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

public class VerifyCode {
    // 宽和高
    private int w = 85;
    private int h = 23;

    private Random r = new Random();
    // 定义字体
    private String[] fontNames = { "宋体", "华文楷体", "黑体", "微软雅黑", "楷体_GB2312" };
    // 定义验证码字符
    private String codes = "23456789abcdefghjkmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ";
    // 背景颜色
    private Color bgColor = new Color(243, 246, 246);
    // 用于获取生成的验证码文本
    private String text;

    // 生成随机颜色
    private Color randomColor() {
        int red = r.nextInt(256);
        int green = r.nextInt(256);
        int blue = r.nextInt(256);
        return new Color(66,161,255);
    }

    // 生成随机字体
    private Font randomFont() {
        int index = r.nextInt(fontNames.length);
        String fontName = fontNames[index];
        int style = r.nextInt(4);
        int size = r.nextInt(5) + 24;

        return new Font(fontName, style, size);
    }

    // 画干扰线
    private void drawLine(BufferedImage image) {
        int num = 1; // 干扰线数量
        Graphics2D g2 = (Graphics2D) image.getGraphics();

        for (int i = 0; i < num; i++) {
            int x1 = r.nextInt(w);
            int y1 = r.nextInt(h);
            int x2 = r.nextInt(w);
            int y2 = r.nextInt(h);
            g2.setStroke(new BasicStroke(1.5F));
            g2.setColor(randomColor()); // 使用随机颜色
            g2.drawLine(x1, y1, x2, y2);
        }
    }

    // 生成随机字符
    private char randomChar() {
        int index = r.nextInt(codes.length());
        return codes.charAt(index);
    }

    // 创建验证码图片
    public BufferedImage createImage() {
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        g2.setColor(bgColor); // 设置背景色
        g2.fillRect(0, 0, w, h); // 填充背景

        StringBuilder sb = new StringBuilder();
        // 向图中画四个字符
        for (int i = 0; i < 4; i++) {
            String s = String.valueOf(randomChar());
            sb.append(s);
            float x = i * 1.0F * w / 4;
            g2.setFont(randomFont());
            g2.setColor(randomColor());
            g2.drawString(s, x, h - 5);
        }
        this.text = sb.toString();
        drawLine(image); // 画干扰线

        g2.dispose(); // 释放图形上下文
        return image;
    }

    // 获取验证码文本
    public String getText() {
        return text;
    }

    // 输出图片
    public static void output(BufferedImage bi, OutputStream fos) throws IOException {
        ImageIO.write(bi, "JPEG", fos);
    }
}