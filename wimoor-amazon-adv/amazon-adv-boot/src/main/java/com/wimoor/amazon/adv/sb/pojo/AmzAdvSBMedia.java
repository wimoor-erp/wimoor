package com.wimoor.amazon.adv.sb.pojo;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_amz_adv_media_hsa")
public class AmzAdvSBMedia {
	@Id
	@Column(name="mediaid")
    private String mediaid;

	@Column(name="profileid")
    private BigInteger profileid;

	@Column(name="campaignId")
    private BigInteger campaignid;
	
	@Column(name="status")
    private String status;

	@Column(name="statusMetadata")
    private String statusmetadata;

	@Column(name="publishedMediaUrl")
    private String publishedmediaurl;

	@Column(name="operator")
	private String operator;

	@Column(name="opttime")
	private Date opttime;
	
	
    public String getMediaid() {
        return mediaid;
    }

    public void setMediaid(String mediaid) {
        this.mediaid = mediaid == null ? null : mediaid.trim();
    }

    public BigInteger getProfileid() {
        return profileid;
    }

    public void setProfileid(BigInteger profileid) {
        this.profileid = profileid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getStatusmetadata() {
        return statusmetadata;
    }

    public void setStatusmetadata(String statusmetadata) {
        this.statusmetadata = statusmetadata == null ? null : statusmetadata.trim();
    }

    public String getPublishedmediaurl() {
        return publishedmediaurl;
    }

    public void setPublishedmediaurl(String publishedmediaurl) {
        this.publishedmediaurl = publishedmediaurl == null ? null : publishedmediaurl.trim();
    }

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getOpttime() {
		return opttime;
	}

	public void setOpttime(Date opttime) {
		this.opttime = opttime;
	}

	public BigInteger getCampaignid() {
		return campaignid;
	}

	public void setCampaignid(BigInteger campaignid) {
		this.campaignid = campaignid;
	}
    
	
    
}