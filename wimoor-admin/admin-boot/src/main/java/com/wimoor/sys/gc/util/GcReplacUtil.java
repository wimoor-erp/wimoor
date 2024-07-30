package com.wimoor.sys.gc.util;


import java.util.Map;

/**
 * 代码生成 动态参数 处理相关
 */
public class GcReplacUtil {


    /**
     * 参数替换
     *
     * @param defaultTemplateParam  默认模板参数
     * @param templateParam 动态模板参数
     * @param path 当前模板路径
     */
    public static String replaceParams(Map<String, String> defaultTemplateParam, Map<String, String> templateParam, String path) {
        for (String param : defaultTemplateParam.keySet()) {
            path = path.replace(param, defaultTemplateParam.get(param));
        }
        for (String param : templateParam.keySet()) {
            path = path.replace(param, templateParam.get(param));
        }
        return path;
    }
}
