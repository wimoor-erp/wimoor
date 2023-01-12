package com.wimoor.erp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.*;
import cn.hutool.extra.qrcode.QrConfig;

@Configuration
public class QRCode {
    @SuppressWarnings("deprecation")
	@Bean
    public QrConfig qrConfig(){
        QrConfig qrConfig=new QrConfig();
        qrConfig.setBackColor(Color.white.getRGB());
        qrConfig.setForeColor(Color.black.getRGB());
        return qrConfig;
    }
}
 