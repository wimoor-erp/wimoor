package com.wimoor.amazon.adv.common.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.util.SpringUtil;

@Entity
@Table(name="t_amz_adv_auth")
public class AmzAdvAuth extends BaseObject implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 306265624103951493L;

	@Column(name="groupid")
    private String groupid;
	
	@Column(name="shopid")
	private String shopid;
	
	@Column(name="code")
    private String code;

	@Column(name="region")
    private String region;
	
	@Column(name="access_token")
    private String accessToken;

	@Column(name="refresh_token")
    private String refreshToken;

	@Column(name="token_type")
    private String tokenType;

	@Column(name="creator")
    private String creator;

	@Column(name="opttime")
    private Date opttime;
	
	@Column(name="disable")
	private Boolean disable;
	
	@Column(name="disableTime")
	private Date disableTime;
	
	@Column(name="createtime")
	private Date createtime;
	
    public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Boolean getDisable() {
		if(disable==null)return false;
		return disable;
	}

	public void setDisable(Boolean disable) {
		this.disable = disable;
	}

	public Date getDisableTime() {
		return disableTime;
	}

	public void setDisableTime(Date disableTime) {
		this.disableTime = disableTime;
	}

	public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid == null ? null : groupid.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getAccessToken() {
    	long diff = new Date().getTime() - this.getOpttime().getTime();
		if (diff / (1000 * 60 * 55) >= 1) {
			IAmzAdvAuthService amzAdvAuthService=SpringUtil.getBean("amzAdvAuthService");
			amzAdvAuthService.refreshToken(this);
			return accessToken;
		} else {
			return accessToken;
		}
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken == null ? null : accessToken.trim();
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken == null ? null : refreshToken.trim();
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType == null ? null : tokenType.trim();
    }

 

    public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getOpttime() {
        return opttime;
    }

    public void setOpttime(Date opttime) {
        this.opttime = opttime;
    }

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getShopid() {
		return shopid;
	}

	public void setShopid(String shopid) {
		this.shopid = shopid;
	}
	
    @Transient
	AmzRegion regionObject;

	public AmzRegion getRegionObject() {
		if(this.regionObject==null) {
			IAmzAdvAuthService amzAdvAuthService=SpringUtil.getBean("amzAdvAuthService");
			this.regionObject=amzAdvAuthService.getRegion(this.getRegion());
			if (this.regionObject == null) {
				throw new BaseException("无法获取所在区域");
			}
		}
		return this.regionObject;
	}

	public void setRegionObject(AmzRegion regionObject) {
		this.regionObject = regionObject;
	}
    
    
}