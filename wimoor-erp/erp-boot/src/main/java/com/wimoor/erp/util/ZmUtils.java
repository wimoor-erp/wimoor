package com.wimoor.erp.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.codec.binary.Base64;

import com.alibaba.fastjson.JSON;

public class ZmUtils {

    private static final char[] DIGITS_LOWER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
            'e', 'f' };

    private static final char[] DIGITS_UPPER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
            'E', 'F' };

    private static Map<String, String> charMap = new HashMap<String, String>() {
        /**
		 * 
		 */
		private static final long serialVersionUID = 6684568673641607032L;

		{
            this.put("a", "-");
            this.put("c", "#");
            this.put("x", "^");
            this.put("M", "$");
        }
    };

    /**
     * 从map 返回int
     *
     * @param map
     * @param key
     * @return
     */
    @SuppressWarnings("rawtypes")
	public static Integer getInteger(Map map, Object key) {
        Number answer = getNumber(map, key);
        if (answer == null)
            return null;
        if (answer instanceof Integer) {
            return ((Integer) answer);
        }
        return   Integer.valueOf(answer.intValue());
    }

    /**
     * 从map 返回 number
     *
     * @param map
     * @param key
     * @return
     */
    @SuppressWarnings("rawtypes")
	private static Number getNumber(Map map, Object key) {
        if (map != null) {
            Object answer = map.get(key);
            if (answer != null) {
                if (answer instanceof Number) {
                    return ((Number) answer);
                }
                if (answer instanceof String) {
                    try {
                        String text = (String) answer;
                        return NumberFormat.getInstance().parse(text);
                    } catch (ParseException e) {
                        // logger.error(e.getMessage(),e);
                    }
                }
            }
        }
        return null;
    }

    /**
     * 从map 返回字符串
     *
     * @param map
     * @param key
     * @return
     */
    @SuppressWarnings("rawtypes")
	public static String getString(Map map, Object key) {
        if (map != null) {
            Object answer = map.get(key);
            if (answer != null) {
                return answer.toString();
            }
        }
        return null;
    }

    /**
     * 判断字符串非空
     *
     * @param str
     * @return
     */
    public static Boolean isNotBlank(String str) {
        if (str == null) {
            return false;
        }
        if (str.trim().length() == 0) {
            return false;
        }
        return true;
    }

    /**
     * 判断字符串 是空的
     *
     * @param str
     * @return
     */
    public static Boolean isBlank(String str) {
        return !isNotBlank(str);
    }

    /**
     * 对象转字符串
     *
     * @param <T>
     * @param t
     * @return
     */
    public static <T> String toJson(T t) {
        if (t == null) {
            return "";
        } else {
            return JSON.toJSON(t) != null ? JSON.toJSON(t).toString() : "";
        }
    }

    /**
     * json 字符串转对象
     *
     * @param <T>
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T json2Object(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

    /**
     * 计算签名
     *
     * @param params
     * @param secret
     * @return
     */
    public static String sign(Map<String, Object> params, String secret) {
        if (params != null && !params.isEmpty()) {
            Set<String> keysSet = params.keySet();
            Object[] keys = keysSet.toArray();
            Arrays.sort(keys);
            StringBuffer stringBuffer = new StringBuffer();
            Object[] var5 = keys;
            int var6 = keys.length;
            for (int var7 = 0; var7 < var6; ++var7) {
                Object key = var5[var7];
                System.out.println("参与计算的key:" + key);
                Object value = params.get(key);
                if (value != null && value.toString().length() > 0) {
                    stringBuffer.append("&").append(key).append("=").append(value);
                }
            }

            if (stringBuffer.length() > 0) {
                return cal(secret, stringBuffer);
            } else {
                return "";
            }
        } else {
            throw new RuntimeException("参数错误.");
        }
    }


    private static String cal(String secret, StringBuffer stringBuffer) {
        if (ZmUtils.isNotBlank(secret)) {
            String ss = stringBuffer.toString().substring(1);
            byte[] bs = null;
            try {
                System.out.println("签名计算字符串"+ss);
                bs = Base64.encodeBase64(ss.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String text = new String(bs);
            System.out.println("签名计算base64字符串"+text);
            String sign=md5Hex(text + secret).toUpperCase();
            System.out.println(sign);
            return sign;
        } else {
            return "";
        }
    }
    //Ym9keTE9eyJjb2lkIjoiVE1FIiwiY2xubyI6IlRNMjAwMDEwTEgiLCJodWJJbkNvZGUiOiJGRUQtTkJGUkQtSVAiLCJqb2JubyI6IlVTRkwyMDEyMjUwMDAwMDAwMyIsInJlQ29tcGFueSI6ImJpbiIsInJlQ291bnRyeUNvZGUiOiJVUyIsInJlQ291bnRyeSI6IlVTIiwicmVTdGF0ZSI6Ik1EIiwicmVBZGRyIjoiMTAxMjAgU3ljYW1vcmUgSG9sbG93IExuIiwicmVBZGRyMiI6IiIsInJlQWRkcjMiOiIiLCJyZVppcCI6IjIwODc2IiwicmVDaXR5IjoiR2VybWFudG93biIsInJlQ29uc2luZWUiOiJiaW4iLCJyZVRlbCI6IjEyMzQ1Njc4OTAiLCJyZUNjdGF4Tm8iOiIiLCJzZENvbXBhbnkiOiJEZWFsYWxpdmUgTExDIiwic2RDb3VudHJ5Q29kZSI6IlVTIiwic2RDb3VudHJ5IjoiVVMiLCJzZFN0YXRlIjoiTkgiLCJzZEFkZHIiOiIxNyBjbGludG9uIGRyIHN0ZSBlIiwic2RBZGRyMiI6bnVsbCwic2RBZGRyMyI6bnVsbCwic2RDaXR5IjoiaG9sbGlzIiwic2RaaXAiOiIwMzA0OSIsInNkTmFtZSI6Imh1aSBsaWFvIiwic2RUZWwiOiI2MDM1MTMwMzA2IiwicGNzIjoiMSIsInNoaXBXZWlnIjowLjUsInBhY2tpbmciOiJXUFgiLCJwYWNrYWdpbmdUeXBlIjoiMTgiLCJkZXNjclR5cGUiOm51bGwsImNvbnRlbnRzIjoiQk9PSzEiLCJjb250ZW50c0NuIjoiVEVTVCIsImRlY1ZhbHVlIjoyMiwiY29kQ2hhcmdlIjpudWxsLCJyZW1hcmsiOm51bGwsImludm9pY2UiOlt7ImVEZXNjck5hbWVlIjoiQk9PSzEiLCJkZXNjck5hbWUiOiJURVNUIiwib3JpZ2luIjoiIiwiaHNDb2RlIjoiIiwicXR5IjoiMiIsInByaWNlIjoxMSwidG90YWxQcmljZSI6MjIsInVuaXQiOiJQQ0UiLCJnV2VpZyI6IjAuMjAwIiwibldlaWciOiIwLjIwMCJ9XSwicGllY2VzIjpbeyJwaWVjZV9pZCI6MCwibl93ZWlnIjoiMC41MDAiLCJnX3dlaWciOiIwLjUwMCIsImxlbmd0aCI6MSwid2lkdGgiOjEsImhlaWdodCI6MX1dfSZub25jZT1zbG5rZGEmdGltZXN0YW1wPTE2MDk0MTA1MjgyNDImdG9rZW49ZjMxNTA2MmItMTc0MS00YjIwLWE2ODUtZjVmNjUxMTJhMzcwJnZlcnNpb249MS4w
    //Ym9keTE9eyJjb2lkIjoiSFlHSiIsImNsbm8iOiJLRU4iLCJodWJJbkNvZGUiOiJGRUQt5LyY5YWI5Z6LIiwiam9ibm8iOiJURVNUMjAyMDIwIiwic2RfYWlycG9ydF9jb2RlIjoiMSIsInNkX29wZXJfY2VudGVyIjoiMiIsInNkQ29tcGFueSI6IktBUEVJWElmZWltZWkiLCJzZE5hbWUiOiJLQVBFSVhJZmVpbWVpIiwic2RhZGRyIjoiSjYgcGFyayIsInNkYWRkcjIiOiIiLCJzZGFkZHIzIjoiIiwgICAgInJlQ29tcGFueSI6IkFteSBIb3dhcmQiLCAgICAicmVDb3VudHJ5IjoiVVMiLCAgICAicmVDb3VudHJ5Q29kZSI6IlVTIiwgICAgInJlU3RhdGUiOiJOWSIsICAgICJyZUFkZHIiOiIxMiBSaWRnZSBSb2FkIiwgICAgInJlQWRkcjIiOiIxMiBSaWRnZSBSb2FkIiwgICAgInJlQWRkcjMiOiIxMiBSaWRnZSBSb2FkIiwgICAgInJlWmlwIjoiMTA5ODciLCAgICAicmVDaXR5IjoiVHV4ZWRvIFBhcmsiLCAgICAicmVDb25zaW5lZSI6IkFteSBIb3dhcmQiLCAgICAicmVUZWwiOiIyNjcyNDI4NDQ5IiwgICAgInJlQ2N0YXhObyI6IiIsInJlX2FpcnBvcnRfY29kZSI6IjEiLCJyZV9vcGVyX2NlbnRlciI6IjIiLCAgICAicGNzIjoiMSIsICAgICJzaGlwV2VpZyI6IjAuNjIiLCAgICAicGFja2luZyI6IlBBSyIsICAgICJwYWNrYWdpbmdUeXBlIjoiIiwgICAgImRlc2NyVHlwZSI6IuaZrui0pyIsICAgICJjb250ZW50cyI6IjEwMCBjb3R0b24gc2hpcnQgc2FtcGxlNjEwNjEwMDA5MCBmb3IgYmVkZGluZyIsICAgICJjb250ZW50czIiOiIiLCAgICAiY29udGVudHMzIjoiIiwgICAgImNvbnRlbnRzQ24iOiLmo4notKjooazooavmoLflk4EiLCAgICAiZGVjVmFsdWUiOiIxNS4wMCIsImRlY1ZhbHVlQ3VyIjoiIiwgICAgImNvZENoYXJnZSI6IiIsImNvZENoYXJnZUN1ciI6IiIsICAgICJjY1BheU1lbnQiOiJQUCIsICAgICJjY3RheCI6IkNDIiwgICAgImRnQ29udGVudElkIjoiIiwgICAgInJlbWFyayI6IiIsImlzX3BsdCI6IiIsICAgICJpbnZvaWNlIjpbeyJlX2Rlc2NyX25hbWVlIjoiMTAwIGNvdHRvbiBzaGlydCBzYW1wbGU2MTA2MTAwMDkwIGZvciBiZWRkaW5nIiwiZGVzY3JfbmFtZSI6Iuajiei0qOihrOihq+agt+WTgSIsInF0eSI6IjEiLCJnX3dlaWciOiIwLjEwMCIsIm5fd2VpZyI6IjAuMDAwIiwicHJpY2UiOiIxNS4wMCIsImhzX2NvZGUiOiI2MTA2MTAwMDkwIiwib3JpZ2luIjoiQ04iLCJ1bml0IjoiUGllY2VzIn1dLCJwYXlfYWNjb3VudF9ubyI6IiIsInNlcnZpY2Vfbm8iOiIifSZub25jZT1zbG5rZGEmdGltZXN0YW1wPTE2MDg4Mzc1NjY2MzgmdG9rZW49OTVjN2IzMDItYzkzMC00Mzg5LWJmNDgtYTI5NmJmMGE2ZmZmJnZlcnNpb249MS4w

    //----------------以下是签名计算相关------------------------------------------------------------------
    @SuppressWarnings("rawtypes")
	public static String encodeMixChar(String source) {
        String encodeStr = null;
        try {
            encodeStr = Base64.encodeBase64String(source.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Entry entry;
        for (Iterator var2 = charMap.entrySet().iterator(); var2.hasNext(); encodeStr = encodeStr
                .replace((CharSequence) entry.getKey(), (CharSequence) entry.getValue())) {
            entry = (Entry) var2.next();
        }
        return encodeStr;
    }

    public static String md5Hex(final String data) {
        byte[] hex = getBytes(data, Charset.forName("UTF-8"));
        return encodeHexString(getDigest("MD5").digest(hex));
    }

    public static MessageDigest getDigest(final String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (final NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static byte[] getBytes(final String string, final Charset charset) {
        if (string == null) {
            return null;
        }
        return string.getBytes(charset);
    }

    public static String encodeHexString(final byte[] data) {
        return new String(encodeHex(data));
    }

    public static char[] encodeHex(final byte[] data) {
        return encodeHex(data, true);
    }

    public static char[] encodeHex(final byte[] data, final boolean toLowerCase) {
        return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    public static char[] encodeHex(final byte[] data, final char[] toDigits) {
        final int l = data.length;
        final char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }
        return out;
    }
//----------------------------------------------------------------------------------
}
