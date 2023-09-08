package com.wimoor.feishu.robot;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wimoor.feishu.robot.properties.RobotFeiShuProperties;
import com.wimoor.feishu.robot.properties.RobotProperties;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * 飞书 群机器人
 */
@Slf4j
@Component
public class FenShuApi {

    @Autowired
    private RobotProperties robotProperties;

    public void sendMsg(Map<String, Object> contentMap) {
        RobotFeiShuProperties feiShuProperties = robotProperties.getFeishu();
        // 发送消息
        // 加签
        long timestamp = System.currentTimeMillis() / 1000;
        String sign = genSign(feiShuProperties.getSecret(), timestamp);
        // 拼接消息体
        Map<String, Object> paramMap = new HashMap<>(4);
        paramMap.put("timestamp", timestamp);
        paramMap.put("sign", sign);
        paramMap.put("msg_type", "text");
        paramMap.put("content", contentMap);
        // 请求
        HttpRequest.post(feiShuProperties.getUrl())
                .header("Content-Type", "application/json;charset=utf-8")
                .body(JSONUtil.toJsonStr(paramMap))
                .execute().body();
    }

    /**
     * 签名
     *
     * @param secret 秘密
     * @param timestamp 时间戳
     * @return {@link String}
     * @throws NoSuchAlgorithmException 没有这样算法异常
     * @throws InvalidKeyException 无效关键例外
     */
    private static String genSign(String secret, long timestamp) {
        //把timestamp+"\n"+密钥当做签名字符串
        String stringToSign = timestamp + "\n" + secret;

        //使用HmacSHA256算法计算签名
        Mac mac = null;
        try {
            mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(stringToSign.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        byte[] signData = mac.doFinal(new byte[]{});
        return Base64Encoder.encode(signData);
    }
}
