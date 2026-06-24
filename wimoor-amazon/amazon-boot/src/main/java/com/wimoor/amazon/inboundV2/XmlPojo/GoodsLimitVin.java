package com.wimoor.amazon.inboundV2.XmlPojo;


import lombok.Data;

import java.math.BigDecimal;

/**
     * 许可证VIN信息
     */
@Data
public  class GoodsLimitVin {
        private String licenceNo;              // 许可证编号
        private String licTypeCode;            // 许可证类别代码
        private String vinNo;                  // VIN序号
        private String billLadDate;            // 提/运单日期
        private String qualityQgp;             // 质量保质期
        private String motorNo;                // 发动机号或电机号
        private String vinCode;                // 车辆识别代码（VIN）
        private String chassisNo;              // 底盘(车架)号
        private BigDecimal invoiceNum;         // 发票所列数量
        private String prodCnnm;               // 品名（中文名称）
        private String prodEnnm;               // 品名（英文名称）
        private String modelEn;                // 型号（英文）
        private BigDecimal pricePerUnit;       // 单价
        private String invoiceNo;              // 发票号
    }