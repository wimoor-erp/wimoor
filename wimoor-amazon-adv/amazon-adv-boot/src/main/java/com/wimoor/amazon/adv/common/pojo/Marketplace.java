package com.wimoor.amazon.adv.common.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_marketplace")
public class Marketplace implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5558303729383204310L;

	@Id
    @Column(name = "marketplaceId")
    private String marketplaceid;

	@Column(name = "market")
    private String market;

	@Column(name = "name")
    private String name;

	@Column(name = "region_name")
    private String regionName;

	@Column(name = "region")
    private String region;

	@Column(name = "end_point")
    private String endPoint;

	@Column(name = "point_name")	
    private String pointName;

	@Column(name = "accessKey")	
    private String accesskey;

	@Column(name = "secretKey")	
    private String secretkey;

	@Column(name = "dim_units")	
    private String dimUnits;

	@Column(name = "weight_units")	
    private String weightUnits;

	@Column(name = "currency")	
    private String currency;

	@Column(name = "findex")	
    private Integer findex;
	
	@Column(name = "adv_end_point")	
    private String advEndPoint;
	
	@Column(name = "aws_access_key")	
    private String AwsAccessKey;
	
	@Column(name = "aws_secret_key")	
    private String AwsSecretKey;
	
	@Column(name = "associate_tag")	
    private String AssociateTag;
	
	@Column(name = "developer_url")	
    private String developerUrl;
	
	@Column(name = "dev_account_num")	
    private String devAccountNum;
	
	@Column(name = "bytecode")	
	private String bytecode;
	
	
    public String getBytecode() {
		return bytecode;
	}

	public void setBytecode(String bytecode) {
		this.bytecode = bytecode;
	}

	public String getAssociateTag() {
		return AssociateTag;
	}

	public void setAssociateTag(String associateTag) {
		AssociateTag = associateTag;
	}

	public String getAdvEndPoint() {
		return advEndPoint;
	}

	public void setAdvEndPoint(String advEndPoint) {
		this.advEndPoint = advEndPoint;
	}

	public String getAwsAccessKey() {
		return AwsAccessKey;
	}

	public void setAwsAccessKey(String awsAccessKey) {
		AwsAccessKey = awsAccessKey;
	}

	public String getAwsSecretKey() {
		return AwsSecretKey;
	}

	public void setAwsSecretKey(String awsSecretKey) {
		AwsSecretKey = awsSecretKey;
	}

	public Integer getFindex() {
		return findex;
	}

	public void setFindex(Integer findex) {
		this.findex = findex;
	}

	public String getMarketplaceid() {
        return marketplaceid;
    }

    public void setMarketplaceid(String marketplaceid) {
        this.marketplaceid = marketplaceid == null ? null : marketplaceid.trim();
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market == null ? null : market.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName == null ? null : regionName.trim();
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region == null ? null : region.trim();
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint == null ? null : endPoint.trim();
    }

    public String getPointName() {
        return pointName;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName == null ? null : pointName.trim();
    }

    public String getAccesskey() {
        return accesskey;
    }

    public void setAccesskey(String accesskey) {
        this.accesskey = accesskey == null ? null : accesskey.trim();
    }

    public String getSecretkey() {
        return secretkey;
    }

    public void setSecretkey(String secretkey) {
        this.secretkey = secretkey == null ? null : secretkey.trim();
    }

    public String getDimUnits() {
        return dimUnits;
    }

    public void setDimUnits(String dimUnits) {
        this.dimUnits = dimUnits == null ? null : dimUnits.trim();
    }

    public String getWeightUnits() {
        return weightUnits;
    }

    public void setWeightUnits(String weightUnits) {
        this.weightUnits = weightUnits == null ? null : weightUnits.trim();
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency == null ? null : currency.trim();
    }

	public String getDeveloperUrl() {
		return developerUrl;
	}

	public void setDeveloperUrl(String developerUrl) {
		this.developerUrl = developerUrl;
	}

	public String getDevAccountNum() {
		return devAccountNum;
	}

	public void setDevAccountNum(String devAccountNum) {
		this.devAccountNum = devAccountNum;
	}

	@Override
	public String toString() {
		return "Marketplace [marketplaceid=" + marketplaceid + ", market=" + market + ", name=" + name + ", regionName="
				+ regionName + ", region=" + region + ", endPoint=" + endPoint + ", pointName=" + pointName
				+ ", accesskey=" + accesskey + ", secretkey=" + secretkey + ", dimUnits=" + dimUnits + ", weightUnits="
				+ weightUnits + ", currency=" + currency + ", findex=" + findex + ", advEndPoint=" + advEndPoint
				+ ", AwsAccessKey=" + AwsAccessKey + ", AwsSecretKey=" + AwsSecretKey + ", AssociateTag=" + AssociateTag
				+ ", developerUrl=" + developerUrl + ", devAccountNum=" + devAccountNum + ", bytecode=" + bytecode
				+ "]";
	}
    
}