package com.wimoor.amazon.inboundV2.XmlPojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wimoor.amazon.inboundV2.util.XmlHelper;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 清单数据
 */
@Data
public  class InventoryData {
    // 清单表头
    private String guid;
    private String appType = "1";      // 1-新增
    private String appTime;
    private String appStatus = "2";    // 2-申报
    private String customsCode;        // 申报地海关
    private String ebpCode;
    private String ebpName;
    private String orderNo;
    private String logisticsCode;
    private String logisticsName;
    private String logisticsNo;
    private String copNo;              // 企业唯一编号
    private String preNo;              // 预录入编号（可选）
    private String invtNo;             // 清单编号（可选，海关审结后才有）
    private String ieFlag = "E";       // E-出口
    private String portCode;           // 出口口岸
    private String ieDate;             // 出口日期
    private String statisticsFlag = "A"; // A-简化申报
    private String agentCode;          // 报关企业代码
    private String agentName;          // 报关企业名称
    private String ebcCode;
    private String ebcName;
    private String ownerCode;          // 生产销售单位代码
    private String ownerName;          // 生产销售单位名称
    private String iacCode;            // 区内企业代码（特殊区域出口模式必填）
    private String iacName;            // 区内企业名称
    private String emsNo;              // 账册编号（特殊区域出口模式必填）
    private String tradeMode = "9810"; // 9610-一般出口
    private String trafMode = "6";     // 6-邮件
    private String trafName;           // 运输工具名称
    private String voyageNo;           // 航班航次号
    private String billNo;             // 提运单号
    private String totalPackageNo;     // 总包号
    private String loctNo;             // 监管场所代码
    private String licenseNo;          // 许可证号
    private String country;            // 运抵国
    private String pod;                // 指运港代码
    private BigDecimal freight = BigDecimal.ZERO;
    private String fCurrency ;
    private String fFlag = "3";        // 3-总价
    private BigDecimal insuredFee = BigDecimal.ZERO;
    private String iCurrency = "142";
    private String iFlag = "3";
    private String wrapType = "1";     // 1-纸箱
    private int packNo = 1;            // 件数
    private BigDecimal grossWeight;    // 毛重
    private BigDecimal netWeight;      // 净重
    private String note;
    @JsonProperty("fCurrency")
    public String getFCurrency() {
        return fCurrency;
    }
    @JsonProperty("fCurrency")
    public void setFCurrency(String fCurrency) {
        this.fCurrency = fCurrency;
    }
    @JsonProperty("fFlag")
    public String getFFlag() {
        return fFlag;
    }
    @JsonProperty("fFlag")
    public void setFFlag(String fFlag) {
        this.fFlag = fFlag;
    }

    public String getICurrency() {
        return iCurrency;
    }
    @JsonProperty("iCurrency")
    public void setICurrency(String iCurrency) {
        this.iCurrency = iCurrency;
    }
    @JsonProperty("iFlag")
    public String getIFlag() {
        return iFlag;
    }
    @JsonProperty("iFlag")
    public void setIFlag(String iFlag) {
        this.iFlag = iFlag;
    }

    // 清单商品列表
    private List<InventoryItem> inventoryItems;

    // BaseTransfer信息
    private String copCode;
    private String copName;
    private String dxpId;
    private String baseNote;

    public InventoryData() {
    }
    // 构造函数
    public InventoryData(String customsCode, String ebpCode, String ebpName,
                         String orderNo, String logisticsCode, String logisticsName,
                         String logisticsNo, String copNo, String ebcCode, String ebcName,
                         String ownerCode, String ownerName) {
        this.guid = XmlHelper.generateGuid();
        this.appTime = XmlHelper.getCurrentAppTime();
        this.customsCode = customsCode;
        this.ebpCode = ebpCode;
        this.ebpName = ebpName;
        this.orderNo = orderNo;
        this.logisticsCode = logisticsCode;
        this.logisticsName = logisticsName;
        this.logisticsNo = logisticsNo;
        this.copNo = copNo;
        this.ebcCode = ebcCode;
        this.ebcName = ebcName;
        this.ownerCode = ownerCode;
        this.ownerName = ownerName;
        this.ieDate = XmlHelper.DATE_FORMAT.format(new Date());
    }

    // ... getter/setter
}