package com.wimoor.amazon.common.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;

public class FBAEstimatedFeeKey {
	@TableId(value="sku")
    private String sku;

	@TableId(value="asin")
    private String asin;

	@TableId(value="amazonauthid")
    private String amazonauthid;

	@TableId(value="marketplaceid")
    private String marketplaceid;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku == null ? null : sku.trim();
    }

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin == null ? null : asin.trim();
    }

    public String getAmazonauthid() {
        return amazonauthid;
    }

    public void setAmazonauthid(String amazonauthid) {
        this.amazonauthid = amazonauthid == null ? null : amazonauthid.trim();
    }

    public String getMarketplaceid() {
        return marketplaceid;
    }

    public void setMarketplaceid(String marketplaceid) {
        this.marketplaceid = marketplaceid == null ? null : marketplaceid.trim();
    }
}