package com.wimoor.erp.thirdparty.service.impl;

import java.util.Locale;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

public  class Md5Srv2 {

        private static final ObjectMapper SORTED_MAPPER = new ObjectMapper();
        static {
            SORTED_MAPPER.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        }

        /**
         * key按字典顺序排序，第一个字母相同，就比较第二个字母，以此类推
         *
         * @Author lml
         * @Date 2022/5/26 下午7:51
         * @param node
         * @return com.fasterxml.jackson.databind.JsonNode
         */
        private static JsonNode sortedNode(final JsonNode node) throws JsonProcessingException {
            final Object obj = SORTED_MAPPER.treeToValue(node, Object.class);
            final String jsonStr = SORTED_MAPPER.writeValueAsString(obj);
            ObjectMapper mapper = new ObjectMapper();
            final JsonNode jsonNode =  mapper.readTree(jsonStr);
            return jsonNode;
        }
    public static String geAuthCode(final String securityClientCode, final String securityClientSecret,
                                    final long reqTime, final Map<String, Object> reqDataMap) throws JsonProcessingException {
        final Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("appKey", securityClientCode);
        dataMap.put("reqTime", reqTime + "");
        if (null != reqDataMap) {
            for (Map.Entry<String, Object> entry : reqDataMap.entrySet()) {
                final String key = entry.getKey();
                if (!key.equals("authcode") && !key.equals("appKey") && !key.equals("appSecret") && !key.equals("reqTime")){
                  //data的key按字典顺序排序，第一个字母相同，就比较第二个字母，以此类推
                ObjectMapper mapper = new ObjectMapper();
                String str = mapper.writeValueAsString(entry.getValue());
                final JsonNode node = mapper.readTree(str);
                JsonNode data = sortedNode(node);

                dataMap.put(key.toLowerCase(), data);
            }
        }
    }

    final SortedMap<String, Object> sortedMap = new TreeMap<String, Object>();
        sortedMap.putAll(dataMap);

    List<String> keys = new ArrayList<String>(sortedMap.keySet());
        Collections.sort(keys);
       String bMd5Str = "";
        for (String key : keys) {
        String value = dataMap.get(key).toString();
        if (null != value && !"".equals(value)) {
            bMd5Str += value;
        }
    }
    final String authCode = sha256_HMAC(bMd5Str, securityClientSecret);
        return authCode;
}


    /**
     * sha256_HMAC加密
     *
     * @param message 消息
     * @param secret  秘钥
     * @return 加密后字符串
     */
    public static String sha256_HMAC(String message, String secret) {
        String hash = "";
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] bytes = sha256_HMAC.doFinal(message.getBytes());
            hash = byteArrayToHexString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hash;
    }

    /**
     * 将加密后的字节数组转换成字符串
     *
     * @param b 字节数组
     * @return 字符串
     */
    public static String byteArrayToHexString(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toLowerCase(Locale.ENGLISH);
    }

}
