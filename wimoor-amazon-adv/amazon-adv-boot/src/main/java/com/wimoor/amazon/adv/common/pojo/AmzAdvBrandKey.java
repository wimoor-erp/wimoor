package com.wimoor.amazon.adv.common.pojo;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Id;

public class AmzAdvBrandKey {
	@Id
	@Column(name="brandId")
    private String brandid;

	@Id
	@Column(name="brandEntityId")
    private String brandentityid;

	@Id
	@Column(name="profileid")
    private BigInteger profileid;

    public String getBrandid() {
        return brandid;
    }

    public void setBrandid(String brandid) {
        this.brandid = brandid == null ? null : brandid.trim();
    }

    public String getBrandentityid() {
        return brandentityid;
    }

    public void setBrandentityid(String brandentityid) {
        this.brandentityid = brandentityid == null ? null : brandentityid.trim();
    }

    public BigInteger getProfileid() {
        return profileid;
    }

    public void setProfileid(BigInteger profileid) {
        this.profileid = profileid;
    }
}