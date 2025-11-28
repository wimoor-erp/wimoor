package com.wimoor;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients
@EnableCaching
@EnableDiscoveryClient
@MapperScan("com.wimoor.**.mapper")
@SpringBootApplication
@ConditionalOnProperty(name = "swagger.enabled", havingValue = "false", matchIfMissing = true)
public class WimoorGenApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(WimoorGenApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  代码生成模块启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                "          _                            \n" +
                "         (_)                           \n" +
                "__      ___ _ __ ___   ___   ___  _ __ \n" +
                "\\ \\ /\\ / / | '_ ` _ \\ / _ \\ / _ \\| '__|\n" +
                " \\ V  V /| | | | | | | (_) | (_) | |   \n" +
                "  \\_/\\_/ |_|_| |_| |_|\\___/ \\___/|_|   \n");
    }
}
