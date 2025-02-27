package com.wimoor.sys.gc.model.core;

import java.lang.annotation.*;

/**
 * 指定请求和响应参数 自动解密or加密注解
 *
 * @author wangsong
 * @version 1.0.1
 * @email 1720696548@qq.com
 * @date 2021/4/10 0010 15:59
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface XjSecret {

    /**
     * 是否存在下级数据（存在下级数据将对下级数据 进行递归 加密or解密 操作）
     *
     * @return boolean
     * @version 1.0.0
     */
    boolean isNext() default false;

    /**
     * 类型
     * 1-请求参数使用base64解密，响应参数使用base64加密
     * 2-只对请求参数使用base64解密
     * 3-只对响应参数使用base64加密
     * 4-数据脱敏(需指定, 脱敏索引位置)
     */
    int type() default 1;


    /**
     * 脱敏索引 (默认为3-7, 标准的隐藏手机号中间4位)
     */
    int[] desensitizedIndex() default {3, 7};

}

