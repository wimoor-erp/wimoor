package com.wimoor.amazon.adv.common.pojo;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name="t_amz_adv_api_pages")
public class AmzAdvApiPages {
	@Id
	@Column(name="profileid")
    private BigInteger profileid;

	@Id
	@Column(name="apipath")
    private String apipath;

	@Column(name="pages")
    private Integer pages;

	@Column(name="flog")
    private String flog;
 
	@Column(name="nexttoken")
    private String nexttoken;
	
	@Column(name="opttime")
    private Date opttime;
	 
}