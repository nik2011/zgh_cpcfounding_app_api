package com.yitu.cpcFounding.api.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 生成验证码的工具类
 *
 * @author pangshihe
 * @date 2020/12/7 18:30
 */
@Slf4j
public class RandomValidateCodeUtil {


    public static final String RANDOMCODEKEY = "RANDOMVALIDATECODEKEY";//放到session中的key
    public static final String PHONE_RANDOM_CODE_KEY = "PHONERANDOMCODEKEY";//手机验证码放到session中的key
    private String randString = "0123456789";//随机产生只有数字的字符串 private String
    //private String randString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";//随机产生只有字母的字符串
    //private String randString = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";//随机产生数字与字母组合的字符串
    private int width = 95;// 图片宽
    private int height = 25;// 图片高
    private int lineSize = 40;// 干扰线数量
    private int stringNum = 4;// 随机产生字符数量


    private Random random = new Random();

    /**
     * 获得字体
     */
    private Font getFont() {
        return new Font("Fixedsys", Font.CENTER_BASELINE, 18);
    }

    /**
     * 获得颜色
     */
    private Color getRandColor(int fc, int bc) {
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc - 16);
        int g = fc + random.nextInt(bc - fc - 14);
        int b = fc + random.nextInt(bc - fc - 18);
        return new Color(r, g, b);
    }

    /**
     * 生成随机图片
     */
    public void getRandcode(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        // BufferedImage类是具有缓冲区的Image类,Image类是用于描述图像信息的类
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();// 产生Image对象的Graphics对象,改对象可以在图像上进行各种绘制操作
        g.fillRect(0, 0, width, height);//图片大小
        g.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 18));//字体大小
        g.setColor(getRandColor(110, 133));//字体颜色
        // 绘制干扰线
        for (int i = 0; i <= lineSize; i++) {
            drowLine(g);
        }
        // 绘制随机字符
        String randomString = "";
        for (int i = 1; i <= stringNum; i++) {
            randomString = drowString(g, randomString, i);
        }
//        logger.info(randomString);
        //将生成的随机字符串保存到session中
        session.removeAttribute(RANDOMCODEKEY);
        session.setAttribute(RANDOMCODEKEY, randomString);
        g.dispose();
        try {
            // 将内存中的图片通过流动形式输出到客户端
            ImageIO.write(image, "JPEG", response.getOutputStream());
        } catch (Exception e) {
            log.debug("验证码生成失败{}", "将内存中的图片通过流动形式输出到客户端失败>>>>   ");
        }

    }

    /**
     * 绘制字符串
     */
    private String drowString(Graphics g, String randomString, int i) {
        g.setFont(getFont());
        g.setColor(new Color(random.nextInt(101), random.nextInt(111), random
                .nextInt(121)));
        String rand = String.valueOf(getRandomString(random.nextInt(randString
                .length())));
        randomString += rand;
        g.translate(random.nextInt(3), random.nextInt(3));
        g.drawString(rand, 13 * i, 16);
        return randomString;
    }

    /**
     * 绘制干扰线
     */
    private void drowLine(Graphics g) {
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        int xl = random.nextInt(13);
        int yl = random.nextInt(15);
        g.drawLine(x, y, x + xl, y + yl);
    }

    /**
     * 获取随机的字符
     */
    public String getRandomString(int num) {
        return String.valueOf(randString.charAt(num));
    }

    /**
     * @param length
     * @return java.lang.String
     * @Description: 生成随机码
     * @author liuzhaowei
     * @date 2020/12/25
     */
    public String generateCode(int length) {
        String content = "";
        for (int i = 0; i < length; i++) {
            int num = RandomUtils.nextInt(0, 10);
            content += getRandomString(num);
        }
        return content;
    }

    /**
     * @param length
     * @return java.lang.String
     * @Description: 生成随机数字
     * @author liuzhaowei
     * @date 2020/12/25
     */
    public static String generateNum(int length) {
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int num = RandomUtils.nextInt(0, 10);
            content.append(num);
        }
        return content.toString();
    }

    /***
     * @description: 订单号生成
     * @param
     * @return: java.lang.String
     * @author: luzhichao
     * @date: 2021/1/26 15:14
     */
    public String generateOrderNo(long userId, long prizeId) {
        StringBuilder str = new StringBuilder();
        String pre1 = DateTimeUtil.formatCurrent(DateTimeUtil.YYYYMMDD);
        String pre2 = String.format("%07d", userId);
        str.append(pre1).append(pre2).append(prizeId);
        return str.toString();
    }
}
