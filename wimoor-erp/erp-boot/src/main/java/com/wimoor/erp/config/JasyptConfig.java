package com.wimoor.erp.config;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimplePBEConfig;
import org.jasypt.iv.RandomIvGenerator;
import org.jasypt.salt.RandomSaltGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class JasyptConfig {

    @Bean("jasyptStringEncryptor")
    public StandardPBEStringEncryptor stringEncryptor() {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        SimplePBEConfig config = new SimplePBEConfig();
        // 设置加密密码/盐，自定义，可配置在配置中心，但不推荐
        config.setPassword("myVerySecretPassword123!");
        config.setAlgorithm("PBEWithHMACSHA256AndAES_256");
        config.setKeyObtentionIterations(10000); // Higher iterations for better security
        config.setIvGenerator(new RandomIvGenerator()); // Explicit IV generator for CBC mode
        config.setSaltGenerator(new RandomSaltGenerator());
        config.setPoolSize(4); // Pool size for better performance
        encryptor.setConfig(config);
        return encryptor;
    }
}
