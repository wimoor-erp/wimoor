package com.wimoor.amazon.inboundV2.XmlPojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.wimoor.amazon.inboundV2.XmlGenerator.CustomsDeclarationHelper;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 报关单商品项数据类
 */

public   class DeclarationItem {
    private int gNo;                       // 商品序号
    private String codeTS;                 // 商品编号
    private String gName;                  // 商品名称
    private String gModel;                 // 商品规格、型号
    private BigDecimal gQty;               // 申报数量（成交计量单位）
    private String gUnit;                  // 申报计量单位
    private BigDecimal firstQty;           // 第一法定数量
    private String firstUnit;              // 第一计量单位
    private BigDecimal secondQty;          // 第二法定数量
    private String secondUnit;             // 第二计量单位
    private String originCountry;          // 原产地
    private String tradeCurr = "142";      // 成交币制
    private BigDecimal declPrice;          // 申报单价
    private BigDecimal declTotal;          // 申报总价
    private String useTo;                  // 用途/生产厂家
    private String dutyMode = "1";         // 征减免税方式
    private String destinationCountry;     // 最终目的国（地区）

    // 跨境电商新增字段
    private String ciqCode;                // CIQ代码
    private String declGoodsEname;         // 申报货物名称（外文）
    private String origPlaceCode;          // 原产地区代码
    private String purpose;                // 用途代码
    private String goodsAttr;              // 货物属性代码
    private String districtCode;           // 境内目的地/境内货源地

    // 其他字段
    private String classMark;              // 归类标志
    private String contrItem;              // 备案序号
    private BigDecimal factor;             // 申报计量单位与法定单位比例因子
    private BigDecimal workUsd;            // 工缴费
    private String exgNo;                  // 货号
    private BigDecimal exgVersion;         // 版本号
    private String prodValidDt;            // 产品有效期
    private String prodQgp;                // 产品保质期
    private String stuff;                  // 成份/原料/组份
    private String uncode;                 // UN编码
    private String dangName;               // 危险货物名称
    private String dangPackType;           // 危包类别
    private String dangPackSpec;           // 危包规格
    private String engManEntCnm;           // 境外生产企业名称
    private String noDangFlag;             // 非危险化学品
    private String destCode;               // 目的地代码
    private String goodsSpec;              // 检验检疫货物规格
    private String goodsModel;             // 货物型号
    private String goodsBrand;             // 货物品牌
    private String produceDate;            // 生产日期
    private String prodBatchNo;            // 生产批号
    private String ciqName;                // 检验检疫名称
    private String mnufctrRegno;           // 生产单位注册号
    private String mnufctrRegName;         // 生产单位名称
    private String sku;
    // 许可证信息列表
    private List<GoodsLimit> goodsLimits = new ArrayList<>();
    public DeclarationItem() {
    }
    public DeclarationItem(int gNo, String codeTS, String gName, BigDecimal gQty,
                           String gUnit, BigDecimal declPrice) {
        this.gNo = gNo;
        this.codeTS = codeTS;
        this.gName = gName;
        this.gQty = gQty;
        this.gUnit = gUnit;
        this.declPrice = declPrice;
        this.declTotal = gQty.multiply(declPrice);
    }
    @JsonProperty("gNo")
    public int getGNo() {
        return gNo;
    }
    @JsonProperty("gNo")
    public void setGNo(int gNo) {
        this.gNo = gNo;
    }
    @JsonProperty("codeTS")
    public String getCodeTS() {
        return codeTS;
    }
    @JsonProperty("codeTS")
    public void setCodeTS(String codeTS) {
        this.codeTS = codeTS;
    }
    @JsonProperty("gName")
    public String getGName() {
        return gName;
    }
    @JsonProperty("gName")
    public void setGName(String gName) {
        this.gName = gName;
    }
    @JsonProperty("gModel")
    public String getGModel() {
        return gModel;
    }
    @JsonProperty("gModel")
    public void setGModel(String gModel) {
        this.gModel = gModel;
    }
    @JsonProperty("gQty")
    public BigDecimal getGQty() {
        return gQty;
    }
    @JsonProperty("gQty")
    public void setGQty(BigDecimal gQty) {
        this.gQty = gQty;
    }
    @JsonProperty("gUnit")
    public String getGUnit() {
        return gUnit;
    }
    @JsonProperty("gUnit")
    public void setGUnit(String gUnit) {
        this.gUnit = gUnit;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getFirstQty() {
        return firstQty;
    }

    public void setFirstQty(BigDecimal firstQty) {
        this.firstQty = firstQty;
    }

    public String getFirstUnit() {
        return firstUnit;
    }

    public void setFirstUnit(String firstUnit) {
        this.firstUnit = firstUnit;
    }

    public BigDecimal getSecondQty() {
        return secondQty;
    }

    public void setSecondQty(BigDecimal secondQty) {
        this.secondQty = secondQty;
    }

    public String getSecondUnit() {
        return secondUnit;
    }

    public void setSecondUnit(String secondUnit) {
        this.secondUnit = secondUnit;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    public String getTradeCurr() {
        return tradeCurr;
    }

    public void setTradeCurr(String tradeCurr) {
        this.tradeCurr = tradeCurr;
    }

    public BigDecimal getDeclPrice() {
        return declPrice;
    }

    public void setDeclPrice(BigDecimal declPrice) {
        this.declPrice = declPrice;
    }

    public BigDecimal getDeclTotal() {
        return declTotal;
    }

    public void setDeclTotal(BigDecimal declTotal) {
        this.declTotal = declTotal;
    }

    public String getUseTo() {
        return useTo;
    }

    public void setUseTo(String useTo) {
        this.useTo = useTo;
    }

    public String getDutyMode() {
        return dutyMode;
    }

    public void setDutyMode(String dutyMode) {
        this.dutyMode = dutyMode;
    }

    public String getDestinationCountry() {
        return destinationCountry;
    }

    public void setDestinationCountry(String destinationCountry) {
        this.destinationCountry = destinationCountry;
    }

    public String getCiqCode() {
        return ciqCode;
    }

    public void setCiqCode(String ciqCode) {
        this.ciqCode = ciqCode;
    }

    public String getDeclGoodsEname() {
        return declGoodsEname;
    }

    public void setDeclGoodsEname(String declGoodsEname) {
        this.declGoodsEname = declGoodsEname;
    }

    public String getOrigPlaceCode() {
        return origPlaceCode;
    }

    public void setOrigPlaceCode(String origPlaceCode) {
        this.origPlaceCode = origPlaceCode;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getGoodsAttr() {
        return goodsAttr;
    }

    public void setGoodsAttr(String goodsAttr) {
        this.goodsAttr = goodsAttr;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getClassMark() {
        return classMark;
    }

    public void setClassMark(String classMark) {
        this.classMark = classMark;
    }

    public String getContrItem() {
        return contrItem;
    }

    public void setContrItem(String contrItem) {
        this.contrItem = contrItem;
    }

    public BigDecimal getFactor() {
        return factor;
    }

    public void setFactor(BigDecimal factor) {
        this.factor = factor;
    }

    public BigDecimal getWorkUsd() {
        return workUsd;
    }

    public void setWorkUsd(BigDecimal workUsd) {
        this.workUsd = workUsd;
    }

    public String getExgNo() {
        return exgNo;
    }

    public void setExgNo(String exgNo) {
        this.exgNo = exgNo;
    }

    public BigDecimal getExgVersion() {
        return exgVersion;
    }

    public void setExgVersion(BigDecimal exgVersion) {
        this.exgVersion = exgVersion;
    }

    public String getProdValidDt() {
        return prodValidDt;
    }

    public void setProdValidDt(String prodValidDt) {
        this.prodValidDt = prodValidDt;
    }

    public String getProdQgp() {
        return prodQgp;
    }

    public void setProdQgp(String prodQgp) {
        this.prodQgp = prodQgp;
    }

    public String getStuff() {
        return stuff;
    }

    public void setStuff(String stuff) {
        this.stuff = stuff;
    }

    public String getUncode() {
        return uncode;
    }

    public void setUncode(String uncode) {
        this.uncode = uncode;
    }

    public String getDangName() {
        return dangName;
    }

    public void setDangName(String dangName) {
        this.dangName = dangName;
    }

    public String getDangPackType() {
        return dangPackType;
    }

    public void setDangPackType(String dangPackType) {
        this.dangPackType = dangPackType;
    }

    public String getDangPackSpec() {
        return dangPackSpec;
    }

    public void setDangPackSpec(String dangPackSpec) {
        this.dangPackSpec = dangPackSpec;
    }

    public String getEngManEntCnm() {
        return engManEntCnm;
    }

    public void setEngManEntCnm(String engManEntCnm) {
        this.engManEntCnm = engManEntCnm;
    }

    public String getNoDangFlag() {
        return noDangFlag;
    }

    public void setNoDangFlag(String noDangFlag) {
        this.noDangFlag = noDangFlag;
    }

    public String getDestCode() {
        return destCode;
    }

    public void setDestCode(String destCode) {
        this.destCode = destCode;
    }

    public String getGoodsSpec() {
        return goodsSpec;
    }

    public void setGoodsSpec(String goodsSpec) {
        this.goodsSpec = goodsSpec;
    }

    public String getGoodsModel() {
        return goodsModel;
    }

    public void setGoodsModel(String goodsModel) {
        this.goodsModel = goodsModel;
    }

    public String getGoodsBrand() {
        return goodsBrand;
    }

    public void setGoodsBrand(String goodsBrand) {
        this.goodsBrand = goodsBrand;
    }

    public String getProduceDate() {
        return produceDate;
    }

    public void setProduceDate(String produceDate) {
        this.produceDate = produceDate;
    }

    public String getProdBatchNo() {
        return prodBatchNo;
    }

    public void setProdBatchNo(String prodBatchNo) {
        this.prodBatchNo = prodBatchNo;
    }

    public String getCiqName() {
        return ciqName;
    }

    public void setCiqName(String ciqName) {
        this.ciqName = ciqName;
    }

    public String getMnufctrRegno() {
        return mnufctrRegno;
    }

    public void setMnufctrRegno(String mnufctrRegno) {
        this.mnufctrRegno = mnufctrRegno;
    }

    public String getMnufctrRegName() {
        return mnufctrRegName;
    }

    public void setMnufctrRegName(String mnufctrRegName) {
        this.mnufctrRegName = mnufctrRegName;
    }

    public List<GoodsLimit> getGoodsLimits() {
        return goodsLimits;
    }

    public void setGoodsLimits(List<GoodsLimit> goodsLimits) {
        this.goodsLimits = goodsLimits;
    }
}
