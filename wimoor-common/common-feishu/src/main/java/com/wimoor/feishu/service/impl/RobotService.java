package com.wimoor.feishu.service.impl;

 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wimoor.feishu.robot.FenShuApi;
import com.wimoor.feishu.robot.properties.RobotProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * 机器人服务
 *
 * @author wangsong
 * @date 2023/05/17
 */
@Service
public class RobotService {

    @Autowired
    private FenShuApi fenShuApi;

    @Autowired
    private RobotProperties robotProperties;

    /**
     * 发送机器人信息
     *
     * @return boolean
     */
    public boolean sendMsg(String content) {
        // 发送飞书消息
        if (robotProperties.getFeishu() != null
                && robotProperties.getFeishu().getUrl() != null && !"".equals(robotProperties.getFeishu().getUrl())
                && robotProperties.getFeishu().getSecret() != null && !"".equals(robotProperties.getFeishu().getSecret())) {
            Map<String, Object> contentMap = new HashMap<>(4);
            contentMap.put("text", content);
            fenShuApi.sendMsg(contentMap);
        }
        return true;
    }
}
