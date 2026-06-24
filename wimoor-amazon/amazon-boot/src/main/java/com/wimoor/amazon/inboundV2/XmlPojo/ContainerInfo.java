package com.wimoor.amazon.inboundV2.XmlPojo;


import lombok.Data;

import java.math.BigDecimal;

/**
     * 集装箱信息类
     */
@Data
public class ContainerInfo {
        private String containerId;            // 集装箱号
        private String containerMd;            // 集装箱规格
        private String goodsNo;                // 商品项号
        private String lclFlag = "0";          // 拼箱标识 0-否 1-是
        private BigDecimal goodsContaWt;       // 箱货重量
        private BigDecimal containerWt;        // 自重
    public ContainerInfo() {
    }
        public ContainerInfo(String containerId, String containerMd, String goodsNo) {
            this.containerId = containerId;
            this.containerMd = containerMd;
            this.goodsNo = goodsNo;
        }
    }
