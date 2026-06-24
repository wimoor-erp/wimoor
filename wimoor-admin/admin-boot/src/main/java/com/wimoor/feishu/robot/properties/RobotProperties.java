package com.wimoor.feishu.robot.properties;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * swagger2 配置类（yml 读取）
 * @author wangsong
 * @date 2020/12/11 0011 17:10
 * @return
 * @version 1.0.1
 */
@SuppressWarnings("all")
@Configuration
@ConfigurationProperties(prefix = "robot")
@Data
@Slf4j
public class RobotProperties {
    public RobotProperties(){
        log.info("Robot 机器人助手注册...");
    }

    private RobotFeiShuProperties feishu;
    private RobotWechatProperties wechat;
}

