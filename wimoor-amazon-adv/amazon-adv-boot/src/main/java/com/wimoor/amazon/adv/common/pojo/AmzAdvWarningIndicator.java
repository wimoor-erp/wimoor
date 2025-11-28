package com.wimoor.amazon.adv.common.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_amz_adv_warning_indicator")
public class AmzAdvWarningIndicator {
	@Id
	@Column(name="shopid")
    private String shopid;

	@Column(name="impressions")
    private Float impressions;

	@Column(name="clicks")
    private Float clicks;

	@Column(name="cr")
    private Float cr;

	@Column(name="acos")
    private Float acos;

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid == null ? null : shopid.trim();
    }

    public Float getImpressions() {
        return impressions;
    }

    public void setImpressions(Float impressions) {
        this.impressions = impressions;
    }

    public Float getClicks() {
        return clicks;
    }

    public void setClicks(Float clicks) {
        this.clicks = clicks;
    }

    public Float getCr() {
        return cr;
    }

    public void setCr(Float cr) {
        this.cr = cr;
    }

    public Float getAcos() {
        return acos;
    }

    public void setAcos(Float acos) {
        this.acos = acos;
    }
}