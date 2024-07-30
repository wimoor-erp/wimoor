package com.wimoor.sys.gc.util;

import java.util.regex.Pattern;

/**
 * 参数验证工具类
 * @author wangsong
 * @mail 1720696548@qq.com
 * @date 2021/9/13 0013 10:59
 * @version 1.0.1
 */
@SuppressWarnings("all")
public class RegUtil {

    // 用户名
    public static final String USERNAME = "^[a-zA-Z0-9_]\\w{4,17}$";
    public static final String USERNAME_MSG = "用户名长度需在5~18之间,且只能包含字母、数字和下划线";
    // 普通密码
    public static final String PASSWORD = "^.{5,17}$";
    public static final String PASSWORD_MSG = "密码长度需在6~18位之间";
    // 强度中密码
    public static final String STRONG_PASSWORD = "^(?=.*\\d)((?=.*[a-z])|(?=.*[A-Z])).{5,17}$";
    public static final String STRONG_PASSWORD_MSG = "密码必须包含【字母+数字且长度在8-18位之间】";
    // 强密码
    public static final String STRONG_PASSWORD_TWO = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{5,17}$";
    public static final String STRONG_PASSWORD_TWO_MSG = "密码必须包含 【小写字母+大写字母+数字且长度在8-18位之间】";
    // 手机号(11位)
    public static final String PHONE = "^1[3456789]\\d{9}$";
    public static final String PHONE_MSG = "请输入正确的11位手机号";
    // 邮箱
    public static final String EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    public static final String EMAIL_MSG = "请输入正确的邮箱号";
    // 汉字
    public static final String CHINESE = "[\\u4e00-\\u9fa5]+";
    public static final String CHINESE_MSG = "只能输入中文";
    // 身份证
    public static final String ID_CARD = "(^\\d{18}$)|(^\\d{15}$)";
    public static final String ID_CARD_MSG = "请输入正确的身份证号";
    // URL
    public static final String URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
    public static final String URL_MSG = "url不规范";
    // IP地址
    public static final String IP_ADDR = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";
    public static final String IP_ADDR_MSG = "ip地址错误";

    /**
     * 校验用户名
     */
    public static boolean isUsername(String username) {
        return Pattern.matches(USERNAME, username);
    }

    /**
     * 校验密码
     */
    public static boolean isPassword(String password) {
        return Pattern.matches(PASSWORD, password);
    }

    /**
     * 校验强密码
     */
    public static boolean isPasswordStrong(String password) {
        return Pattern.matches(STRONG_PASSWORD, password);
    }

    /**
     * 校验强密码
     */
    public static boolean isPasswordStrongTwo(String password) {
        return Pattern.matches(STRONG_PASSWORD_TWO, password);
    }

    /**
     * 校验手机号
     */
    public static boolean isPhone(String mobile) {
        return Pattern.matches(PHONE, mobile);
    }

    /**
     * 校验邮箱
     */
    public static boolean isEmail(String email) {
        return Pattern.matches(EMAIL, email);
    }

    /**
     * 校验汉字
     */
    public static boolean isChinese(String chinese) {
        return Pattern.matches(CHINESE, chinese);
    }

    /**
     * 校验身份证
     */
    public static boolean isIDCard(String idCard) {
        return Pattern.matches(ID_CARD, idCard);
    }

    /**
     * 校验URL
     */
    public static boolean isUrl(String url) {
        return Pattern.matches(URL, url);
    }

    /**
     * 校验IP地址
     */
    public static boolean isIPAddr(String ipAddr) {
        return Pattern.matches(IP_ADDR, ipAddr);
    }

}
