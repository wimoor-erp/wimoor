package com.wimoor.amazon.inboundV2.XmlPojo;


import lombok.Data;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
     * 报关单表头数据类
     */
@Data
public  class DeclarationHeader {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
        // 基本信息
        private String seqNo;                  // 数据中心统一编号
        private String ieFlag = "E";           // 进出口标志 I-进口 E-出口
        private String type = "";              // 单据类型
        private String customMaster;           // 申报地海关
        private String iePort;                 // 进出口岸
        private String agentCode;              // 申报单位代码
        private String agentName;              // 申报单位名称

        // 企业信息
        private String tradeCode;              // 境内收发货人编号
        private String tradeName;              // 境内收发货人名称
        private String ownerCode;              // 消费使用/生产销售单位代码
        private String ownerName;              // 消费使用/生产销售单位名称
        private String copCode;                // 录入单位代码
        private String copName;                // 录入单位名称

        // 运输信息
        private String billNo;                 // 提单号
        private String contrNo;                // 合同号
        private String trafMode;               // 运输方式代码
        private String trafName;               // 运输工具代码及名称
        private String tradeCountry;           // 启运国/运抵国
        private String tradeMode = "9610";     // 贸易方式
        private String distinatePort;          // 经停港/指运港
        private String transMode = "1";        // 成交方式

        // 费用信息
        private String feeMark = "3";          // 运费标记
        private BigDecimal feeRate = BigDecimal.ZERO; // 运费／率
        private String feeCurr = "142";        // 运费币制
        private String insurMark = "3";        // 保险费标记
        private BigDecimal insurRate = BigDecimal.ZERO; // 保险费／率
        private String insurCurr = "142";      // 保险费币制
        private String otherMark = "3";        // 杂费标记
        private BigDecimal otherRate = BigDecimal.ZERO; // 杂费／率
        private String otherCurr = "142";      // 杂费币制

        // 货物信息
        private BigDecimal grossWet;           // 毛重
        private BigDecimal netWt;              // 净重
        private Integer packNo;                // 件数
        private String wrapType = "1";         // 包装种类

        // 日期信息
        private String ieDate;                 // 进出口日期
        private String pDate;                  // 打印日期

        // 备案和许可
        private String manualNo;               // 备案号
        private String licenseNo;              // 许可证编号

        // 其他
        private String cutMode;                // 征免性质
        private String apprNo;                 // 批准文号
        private String noteS;                  // 备注

        // 申报信息
        private String entryId;                // 海关编号
        private String preEntryId;             // 预录入编号
        private String ediId = "1";            // 报关标志
        private String risk;                   // 风险评估参数
        private String entryType = "M";        // 报关单类型 M-无纸化通关
        private String typistNo;               // 录入员IC卡号
        private String inputerName;            // 录入员名称
        private String partenerId;             // 申报人标识

        // 新增字段（跨境电商相关）
        private String tradeAreaCode = "142";  // 贸易国别
        private String markNo;                 // 标记唛码
        private String despPortCode;           // 启运港代码
        private String entyPortCode;           // 入境口岸代码
        private String goodsPlace;             // 存放地点
        private String blNo;                   // B/L号
        private String declareName;            // 申报人员姓名
        private String noOtherPack = "0";      // 无其他包装
        private String orgCode;                // 检验检疫受理机关

        // 境外企业信息
        private String overseasConsignorCode;  // 境外发货人代码
        private String overseasConsignorCname; // 境外收发货人名称
        private String overseasConsignorEname; // 境外发货人名称（外文）
        private String overseasConsignorAddr;  // 境外收发货人地址
        private String overseasConsigneeCode;  // 境外收货人编码
        private String overseasConsigneeEname; // 境外收货人名称(外文)
        private String domesticConsigneeEname; // 境内收发货人名称（外文）

        // 检验检疫编码
        private String tradeCiqCode;           // 境内收发货人检验检疫编码
        private String ownerCiqCode;           // 生产/消费使用单位检验检疫编码
        private String declCiqCode;            // 申报单位检验检疫编码

        // 构造函数
        public DeclarationHeader() {
        }
        public DeclarationHeader(String customMaster, String iePort, String agentCode,
                                 String agentName, String tradeCode, String tradeName) {
            this.customMaster = customMaster;
            this.iePort = iePort;
            this.agentCode = agentCode;
            this.agentName = agentName;
            this.tradeCode = tradeCode;
            this.tradeName = tradeName;

            // 设置默认日期
            this.ieDate = DATE_FORMAT.format(new Date());
            this.pDate = DATE_FORMAT.format(new Date());
        }

        // Getter和Setter方法
        // ... (为节省篇幅，这里省略getter/setter，实际使用时需要补充)
    }