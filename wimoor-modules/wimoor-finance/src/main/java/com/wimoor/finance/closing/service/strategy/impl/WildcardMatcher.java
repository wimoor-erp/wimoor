package com.wimoor.finance.closing.service.strategy.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * 通配符匹配器（支持 * ? [] {} 转义，无路径限制）
 */
public class WildcardMatcher {
    private static final ConcurrentHashMap<String, Pattern> CACHE = new ConcurrentHashMap<>();

    /**
     * 将通配符表达式编译为正则 Pattern
     * @param wildcard 支持 * ? [abc] [a-z] [!abc] {a,b,c} 以及反斜杠转义
     */
    public static Pattern compile(String wildcard) {
        return CACHE.computeIfAbsent(wildcard, w -> {
            StringBuilder regex = new StringBuilder();
            int len = w.length();
            boolean inBrace = false;   // 是否在 { } 分组内
            boolean inBracket = false; // 是否在 [ ] 内
            int i = 0;
            while (i < len) {
                char c = w.charAt(i);

                // 1. 转义字符 \x
                if (c == '\\') {
                    if (i + 1 < len) {
                        char next = w.charAt(i + 1);
                        // 对正则元字符转义，其他字符直接输出
                        if ("\\[]^$.|+(){}".indexOf(next) >= 0) {
                            regex.append('\\');
                        }
                        regex.append(next);
                        i += 2;
                        continue;
                    } else {
                        throw new IllegalArgumentException("转义符后缺少字符");
                    }
                }

                // 2. 字符类 [ ]
                if (!inBrace && c == '[' && !inBracket) {
                    inBracket = true;
                    regex.append('[');
                    i++;
                    continue;
                }
                if (inBracket) {
                    regex.append(c);
                    if (c == ']') {
                        inBracket = false;
                    }
                    i++;
                    continue;
                }

                // 3. 多选分组 { }
                if (!inBracket && c == '{' && !inBrace) {
                    inBrace = true;
                    regex.append('(');
                    i++;
                    continue;
                }
                if (inBrace) {
                    if (c == '}') {
                        inBrace = false;
                        regex.append(')');
                    } else if (c == ',') {
                        regex.append('|');
                    } else {
                        regex.append(c);
                    }
                    i++;
                    continue;
                }

                // 4. 星号 *  匹配任意字符（0个或多个，包括换行）
                if (c == '*') {
                    regex.append(".*");
                    i++;
                    continue;
                }

                // 5. 问号 ?  匹配任意单个字符
                if (c == '?') {
                    regex.append(".");
                    i++;
                    continue;
                }

                // 6. 普通字符：转义正则元字符
                if ("\\[]^$.|+(){}".indexOf(c) >= 0) {
                    regex.append('\\');
                }
                regex.append(c);
                i++;
            }

            if (inBracket) throw new IllegalArgumentException("字符类未闭合: 缺少 ]");
            if (inBrace) throw new IllegalArgumentException("多选分组未闭合: 缺少 }");

            return Pattern.compile(regex.toString());
        });
    }

    /**
     * 执行通配符匹配
     * @param wildcard 通配符表达式
     * @param input    待匹配字符串
     * @return 是否完全匹配
     */
    public static boolean match(String wildcard, String input) {
        if (wildcard == null || input == null) return false;
        try {
            return compile(wildcard).matcher(input).matches();
        } catch (IllegalArgumentException e) {
            return false; // 表达式不合法则匹配失败
        }
    }

    // 清空缓存（如动态加载大量表达式时可用）
    public static void clearCache() {
        CACHE.clear();
    }
}