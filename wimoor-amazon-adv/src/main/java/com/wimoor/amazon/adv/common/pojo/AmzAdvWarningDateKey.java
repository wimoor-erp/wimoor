package com.wimoor.amazon.adv.common.pojo;


import javax.persistence.Column;
import javax.persistence.Id;

public class AmzAdvWarningDateKey {
	
	@Id
	@Column(name="shopid")
    private String shopid;

	@Id
	@Column(name="recordType")
    private String recordtype;

	@Id
	@Column(name="ftype")
    private String ftype;

	public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    public String getRecordtype() {
        return recordtype;
    }

    public void setRecordtype(String recordtype) {
        this.recordtype = recordtype == null ? null : recordtype.trim();
    }

    public String getFtype() {
        return ftype;
    }

    public void setFtype(String ftype) {
        this.ftype = ftype == null ? null : ftype.trim();
    }
}