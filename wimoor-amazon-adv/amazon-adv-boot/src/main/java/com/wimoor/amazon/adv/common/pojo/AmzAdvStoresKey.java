package com.wimoor.amazon.adv.common.pojo;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Id;

public class AmzAdvStoresKey {
	@Id
	@Column(name="entityId")
    private String entityid;

	@Id
	@Column(name="brandEntityId")
    private String brandentityid;

	@Id
	@Column(name="profileid")
    private BigInteger profileid;

	@Id
	@Column(name="storePageId")
    private String storepageid;

    public String getEntityid() {
        return entityid;
    }

    public void setEntityid(String entityid) {
        this.entityid = entityid == null ? null : entityid.trim();
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

    public String getStorepageid() {
        return storepageid;
    }

    public void setStorepageid(String storepageid) {
        this.storepageid = storepageid == null ? null : storepageid.trim();
    }
}