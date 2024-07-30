package com.wimoor.sys.gc.util;

import org.apache.commons.lang3.StringUtils;

import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.ResultCode;

/**
 * 参数校检并返回错误信息给用户端
 */
public class ValidUtil {


    /**
     * 条件判断
     * 当判断条件 expression = true, 跳出程序，返回错误信息 errorMsg
     * 当判断条件=false时，不处理
     * @param expression 判断条件结果
     */
    public static void isTrue(boolean expression, String errorMsg) {
        if (expression) {
            throw new BizException(ResultCode.PARAM_ERROR);
        }
    }


    /**
     * 条件判断
     * <P>
     *    当判断条件 expression = true, 跳出程序，返回错误枚举 resultType
     *    当判断条件=false时，不处理
     * </P>
     * @author wangsong
     * @param expression 判断条件结果
     * @param resultType 判断条件为true时返回的错误信息
     * @date 2022/9/7 0007 10:41
     * @return void
     * @version 1.0.0
     */
    public static void isTrue(boolean expression, ResultCode resultType) {
        if (expression) {
            throw new BizException(resultType);
        }
    }


    /**
     * 判空(任意数据类型)
     */
    public static void isNull(Object obj, String errorMsg) {
        if (obj == null) {
            throw new BizException(ResultCode.PARAM_IS_NULL);
        }
    }

    /**
     * 判空或空字符 (string))
     */
    public static void isBlank(String str, String errorMsg) {
        if (StringUtils.isBlank(str)) {
            throw new BizException(ResultCode.PARAM_IS_NULL, errorMsg);
        }
    }


    /**
     * 判断字符串长度
     * @author wangsong
     * @mail 1720696548@qq.com
     * @date 2021/9/30 0030 9:36
     * @version 1.0.1
     */
    public static void isStrLen(String str, Integer min, Integer max, String errorMsg) {
        isBlank(str, errorMsg);
        boolean reg = (str.length() >= min && str.length() <= max);
        if (!reg) {
            throw new BizException(ResultCode.PARAM_ERROR, errorMsg);
        }
    }

    //============================================================
    //======================= 指定参数验证 =========================
    //============================================================

    /**
     * 校验手机号
     */
    public static void isPhone(String mobile, String errorMsg) {
        isBlank(mobile, errorMsg);
        if (!RegUtil.isPhone(mobile)) {
            throw new BizException(ResultCode.PARAM_ERROR, errorMsg);
        }
    }

    /**
     * 校验邮箱
     */
    public static void isEmail(String email, String errorMsg) {
        isBlank(email, errorMsg);
        if (!RegUtil.isEmail(email)) {
            throw new BizException(ResultCode.PARAM_ERROR, errorMsg);
        }
    }


    /**
     * 校验身份证
     */
    public static void isIdCard(String idCard, String errorMsg) {
        isBlank(idCard, errorMsg);
        if (!RegUtil.isIDCard(idCard)) {
            throw new BizException(ResultCode.PARAM_ERROR, errorMsg + ":" + RegUtil.ID_CARD_MSG);
        }
    }


    /**
     * 校验汉字
     * @author wangsong
     * @param chinese
     * @param paramName 参数名称描叙
     * @date 2021/9/30 0030 9:49
     * @return void
     * @version 1.0.1
     */
    public static void isChinese(String chinese, String paramName) {
        isBlank(chinese, paramName);
        if (!RegUtil.isChinese(chinese)) {
            throw new BizException(ResultCode.PARAM_ERROR, paramName + RegUtil.CHINESE_MSG);
        }
    }
}
