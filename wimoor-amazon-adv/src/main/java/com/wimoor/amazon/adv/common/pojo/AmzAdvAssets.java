package com.wimoor.amazon.adv.common.pojo;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_amz_adv_assets")
public class AmzAdvAssets {
	
	@Id
	@Column(name="assetId")
    private String assetid;

	@Id
	@Column(name="brandEntityId")
    private String brandentityid;
	
	@Id
	@Column(name="profileid")
    private BigInteger profileid;

	@Column(name="mediaType")
    private String mediatype;

	@Column(name="name")
    private String name;

	@Column(name="url")
    private String url;

	@Column(name="opptime")
    private Date opptime;

    public BigInteger getProfileid() {
		return profileid;
	}

	public void setProfileid(BigInteger profileid) {
		this.profileid = profileid;
	}

	public String getAssetid() {
        return assetid;
    }

    public void setAssetid(String assetid) {
        this.assetid = assetid == null ? null : assetid.trim();
    }

    public String getBrandentityid() {
        return brandentityid;
    }

    public void setBrandentityid(String brandentityid) {
        this.brandentityid = brandentityid == null ? null : brandentityid.trim();
    }

    public String getMediatype() {
        return mediatype;
    }

    public void setMediatype(String mediatype) {
        this.mediatype = mediatype == null ? null : mediatype.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public Date getOpptime() {
        return opptime;
    }

    public void setOpptime(Date opptime) {
        this.opptime = opptime;
    }
}