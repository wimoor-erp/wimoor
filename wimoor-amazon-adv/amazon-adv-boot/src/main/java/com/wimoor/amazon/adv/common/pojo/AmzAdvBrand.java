package com.wimoor.amazon.adv.common.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="t_amz_adv_brand")
public class AmzAdvBrand extends AmzAdvBrandKey {
	@Column(name="brandRegistryName")
    private String brandregistryname;

	@Column(name="opttime")
    private Date opttime;

    public String getBrandregistryname() {
        return brandregistryname;
    }

    public void setBrandregistryname(String brandregistryname) {
        this.brandregistryname = brandregistryname == null ? null : brandregistryname.trim();
    }

	public Date getOpttime() {
		return opttime;
	}

	public void setOpttime(Date opttime) {
		this.opttime = opttime;
	}
   
}