package com.wimoor.amazon.adv.common.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="t_amz_adv_stores")
public class AmzAdvStores extends AmzAdvStoresKey {
	@Column(name="storeName")
    private String storename;

	@Column(name="storePageUrl")
    private String storepageurl;
	
	@Column(name="storePageName")
    private String storepagename;
	
	@Column(name="opptime")
    private Date opptime;
	
    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename == null ? null : storename.trim();
    }

    public String getStorepageurl() {
        return storepageurl;
    }

    public void setStorepageurl(String storepageurl) {
        this.storepageurl = storepageurl == null ? null : storepageurl.trim();
    }

    public String getStorepagename() {
        return storepagename;
    }

    public void setStorepagename(String storepagename) {
        this.storepagename = storepagename == null ? null : storepagename.trim();
    }

    public Date getOpptime() {
        return opptime;
    }

    public void setOpptime(Date opptime) {
        this.opptime = opptime;
    }
}