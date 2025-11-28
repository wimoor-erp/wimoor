package com.wimoor.open1688.utils;


import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * String工具类，包含base64编解码以及16进制转换算法，主要用于计算签名
 */
public final class StringUtil {

    public static final String CHARSET_NAME_UTF8 = "UTF-8";
    public static final char[] digital = "0123456789ABCDEF".toCharArray();
    public static final String DEFAULT_DATA_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    
    public static String format(Date date){
        SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATA_TIME_FORMAT);
        return format.format(date);
    }
    
    public static String encodeHexStr(final byte[] bytes){
        if(bytes == null){
            return null;
        }
        char[] result = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            result[i*2] = digital[(bytes[i] & 0xf0) >> 4];
            result[i*2 + 1] = digital[bytes[i] & 0x0f];
        }
        return new String(result);
    }
    
    public static byte[] decodeHexStr(final String str) {
        if(str == null){
            return null;
        }
        char[] charArray = str.toCharArray();
        if(charArray.length%2 != 0){
            throw new RuntimeException("hex str length must can mod 2, str:" + str);
        }
        byte[] bytes = new byte[charArray.length/2];
        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            int b;
            if(c >= '0' && c <= '9'){
                b = (c-'0')<<4;
            }else if(c >= 'A' && c <= 'F'){
                b = (c-'A'+10)<<4;
            }else{
                throw new RuntimeException("unsport hex str:" + str);
            }
            c = charArray[++i];
            if(c >= '0' && c <= '9'){
                b |= c-'0';
            }else if(c >= 'A' && c <= 'F'){
                b |= c-'A'+10;
            }else{
                throw new RuntimeException("unsport hex str:" + str);
            }
            bytes[i/2] = (byte)b;
        }
        return bytes;
    }
    
    public static String toString(final byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        try {
            return new String(bytes, CHARSET_NAME_UTF8);
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String toString(final byte[] bytes, String charset) {
        if (bytes == null) { 
            return null;
        }
        try {
            return new String(bytes, charset);
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static byte[] toBytes(final String str) {
        if (str == null) {
            return null;
        }
        try {
            return str.getBytes(CHARSET_NAME_UTF8);
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private StringUtil() {
    }
}
				