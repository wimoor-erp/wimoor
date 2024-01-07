package com.wimoor.amazon.util;

import org.apache.commons.lang3.StringUtils;

public class EmojiFilterUtils {
	  private static boolean isEmojiCharacter(char codePoint) {
	        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
	                || (codePoint == 0xD)
	                || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
	                || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
	                || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
	    }

	    /**
	     * 过滤emoji 或者 其他非文字类型的字符
	     *
	     * @param source
	     * @return
	     */
	    public static String filterEmoji(String source) {
	        if (StringUtils.isBlank(source)) {
	            return source;
	        }
	        StringBuilder buf = null;
	        int len = source.length();
	        for (int i = 0; i < len; i++) {
	            char codePoint = source.charAt(i);
	            if (isEmojiCharacter(codePoint)) {
	                if (buf == null) {
	                    buf = new StringBuilder(source.length());
	                }
	                buf.append(codePoint);
	            }
	        }
	        if (buf == null) {
	            return "";
	        } else {
	            if (buf.length() == len) {
	                buf = null;
	                return source;
	            } else {
	                return buf.toString();
	            }
	        }
	    }
	

}
