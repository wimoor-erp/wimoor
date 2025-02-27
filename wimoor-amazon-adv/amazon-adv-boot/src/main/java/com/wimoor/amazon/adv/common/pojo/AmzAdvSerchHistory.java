package com.wimoor.amazon.adv.common.pojo;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_amz_adv_serch_history")
public class AmzAdvSerchHistory {
	
	@Id
	@Column(name="id")
    private BigInteger id;

	@Column(name="userId")
    private String userid;

	@Column(name="fcondition")
    private String fcondition;
	
	@Column(name="ftype")
	private String ftype;

	@Column(name="opttime")
    private Date opttime;

	public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
	 
    public String getFcondition() {
		return fcondition;
	}

	public void setFcondition(String fcondition) {
		this.fcondition = fcondition;
	}

	public Date getOpttime() {
        return opttime;
    }

    public void setOpttime(Date opttime) {
        this.opttime = opttime;
    }

	public String getFtype() {
		return ftype;
	}

	public void setFtype(String ftype) {
		this.ftype = ftype;
	}
    
}