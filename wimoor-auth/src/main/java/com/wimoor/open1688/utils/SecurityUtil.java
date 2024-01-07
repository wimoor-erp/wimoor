package com.wimoor.open1688.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * 安全工具类，包含授权签名所需的hmac_sha1算法，主要用于计算签名
 */
public final class SecurityUtil {

    public static final String HMAC_SHA1 = "HmacSHA1";

    /**
     * HMAC see：http://www.ietf.org/rfc/rfc2104.txt
     */
    public static byte[] hmacSha1(byte[] data, byte[] key, int offset, int len) {
        SecretKeySpec signingKey = new SecretKeySpec(key, HMAC_SHA1);
        Mac mac = null;
        try {
            mac = Mac.getInstance(HMAC_SHA1);
            mac.init(signingKey);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        mac.update(data, offset, len);
        return mac.doFinal();
    }
    
    public static byte[] hmacSha1(byte[][] datas, byte[] key) {
        SecretKeySpec signingKey = new SecretKeySpec(key, HMAC_SHA1);
        Mac mac = null;
        try {
            mac = Mac.getInstance(HMAC_SHA1);
            mac.init(signingKey);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        for (byte[] data : datas) {
            mac.update(data);
        }
        return mac.doFinal();
    }

    public static byte[] hmacSha1(String[] datas, byte[] key) {
        SecretKeySpec signingKey = new SecretKeySpec(key, HMAC_SHA1);
        Mac mac = null;
        try {
            mac = Mac.getInstance(HMAC_SHA1);
            mac.init(signingKey);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        try {
            for (String data : datas) {
                mac.update(data.getBytes(StringUtil.CHARSET_NAME_UTF8));
            }
        } catch (UnsupportedEncodingException e) { 
            throw new RuntimeException(e.getMessage(), e);
        }
        return mac.doFinal();
    }

    public static String hmacSha1ToHexStr(byte[] data, byte[] key, int offset, int len) {
        byte[] rawHmac = hmacSha1(data, key, offset, len);
        return StringUtil.encodeHexStr(rawHmac);
    }
    
    public static String hmacSha1ToHexStr(byte[] data, String key, int offset, int len) {
        try {
            return hmacSha1ToHexStr(data, key.getBytes(StringUtil.CHARSET_NAME_UTF8), offset, len);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    public static String hmacSha1ToHexStr(String str, String key) {
        try {
            byte[] data = str.getBytes(StringUtil.CHARSET_NAME_UTF8);
            return hmacSha1ToHexStr(data, key.getBytes(StringUtil.CHARSET_NAME_UTF8), 0, data.length);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private SecurityUtil() {
    }
}