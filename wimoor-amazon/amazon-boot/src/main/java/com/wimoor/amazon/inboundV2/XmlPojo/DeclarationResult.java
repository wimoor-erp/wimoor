package com.wimoor.amazon.inboundV2.XmlPojo;


import lombok.Data;

import java.util.Date;

/**
     * 报关单回执数据
     */
@Data
public  class DeclarationResult {
        private String seqNo;                  // 数据中心统一编号
        private String entryId;                // 海关编号
        private Date noticeDate;               // 通知时间
        private String channel;                // 处理结果
        private String note;                   // 审核备注
        private String declPort;               // 申报口岸
        private String agentName;              // 申报单位
        private String declareNo;              // 报关单员代码
        private String tradeCo;                // 境内收发货人代码
        private String customsField;           // 货场代码
        private String bondedNo;               // 保税仓库代码
        private Date iEDate;                   // 进出口日期
        private Integer packNo;                // 件数
        private String billNo;                 // 提单号
        private String trafMode;               // 运输方式
        private String voyageNo;               // 航班号
        private Double netWt;                  // 净重
        private Double grossWt;                // 毛重
        private Date dDate;                    // 申报日期
        private String resultInfo;             // 处理结果文字信息

        // 状态码含义
        public String getChannelDescription() {
            switch(channel) {
                case "2": return "QP接收成功，上载发往数据中心";
                case "3": return "上载成功";
                case "4": return "上载失败";
                case "6": return "QP接收成功，申报发往数据中心";
                case "7": return "申报成功";
                case "8": return "上载未申报（无申报权限）";
                case "9": return "上载申报失败（与数据中心联系）";
                case "L": return "海关已接收";
                case "B": return "担保放行";
                case "E": return "不被受理";
                case "G": return "报关单已审结";
                case "F": return "放行交单";
                case "T": return "需交税费";
                case "Y": return "申报失败";
                case "D": return "海关删单";
                case "M": return "报关单重审";
                case "R": return "已结关";
                case "W": return "进出口审结/查验/放行通知";
                case "I": return "进出口审结/查验/放行通知";
                case "C": return "出口查验通知";
                case "S": return "施检";
                case "a": return "签证";
                case "Z": return "退回修改";
                default: return "未知状态";
            }
        }

        public boolean isSuccess() {
            return "7".equals(channel) || "G".equals(channel) ||
                    "F".equals(channel) || "R".equals(channel);
        }

        public boolean isRejected() {
            return "Z".equals(channel) || "Y".equals(channel) ||
                    "E".equals(channel) || "D".equals(channel);
        }

        // Getter和Setter方法
        // ... (为节省篇幅，这里省略)
    }
