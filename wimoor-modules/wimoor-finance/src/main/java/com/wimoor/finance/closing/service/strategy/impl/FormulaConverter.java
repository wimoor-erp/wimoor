package com.wimoor.finance.closing.service.strategy.impl;


import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormulaConverter {

    /**
     * 转换表达式：
     * 1. 去除运算符和括号前后的空格
     * 2. 将变量名中的空格替换为下划线
     */
    public static String convertExpression(String expr) {
        // 第一步：去除运算符和括号前后的空格
        // 注意：减号需要转义，在正则中代表字符 '-'
        String step1 = expr.replaceAll("\\s*([+\\-*/%^()])\\s*", "$1");

        // 第二步：匹配连续的字母、数字、下划线、点、空格、逗号（变量名可能包含这些字符）
        // 但为了简单，这里匹配 [a-zA-Z\s,]+（字母、空格和逗号），如果您的变量名可能包含数字，改为 [a-zA-Z0-9\s,]+ 
        // 使用 Pattern 确保只替换那些真正是变量名的 token（由字母、空格和逗号组成）
        Pattern varPattern = Pattern.compile("([a-zA-Z\\s,]+)");
        Matcher m = varPattern.matcher(step1);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String token = m.group(1);
            // 将 token 中的空格和逗号替换为下划线
            String replaced = token.replace(' ', '_').replace(',', '_');
            m.appendReplacement(sb, replaced);
        }
        m.appendTail(sb);
        return sb.toString();
    }

    /**
     * 转换 env：将每个 key 中的空格和逗号替换为下划线
     */
    public static Map<String, Object> convertEnv(Map<String, Object> originalEnv) {
        Map<String, Object> newEnv = new HashMap<>();
        for (Map.Entry<String, Object> entry : originalEnv.entrySet()) {
            String newKey = entry.getKey().replace(' ', '_').replace(',', '_');
            newEnv.put(newKey, entry.getValue());
        }
        return newEnv;
    }

//    // 测试
//    public static void main(String[] args) {
//        // 原始公式（变量名含空格）
//        String originalExpr = "FBA inventory credit + Postage credits + Promotional rebates * 0.8";
//        // 原始 env
//        Map<String, Object> originalEnv = new HashMap<>();
//        originalEnv.put("FBA inventory credit", 100.0);
//        originalEnv.put("Postage credits", 20.0);
//        originalEnv.put("Promotional rebates", 50.0);
//
//        // 转换
//        String convertedExpr = convertExpression(originalExpr);
//        Map<String, Object> convertedEnv = convertEnv(originalEnv);
//
//        System.out.println("转换后的表达式: " + convertedExpr);
//        System.out.println("转换后的 env: " + convertedEnv);
//
//        // 使用 Aviator 计算
//        // Double result = (Double) AviatorEvaluator.execute(convertedExpr, convertedEnv);
//        // System.out.println("结果: " + result);
//    }
}