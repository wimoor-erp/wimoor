package com.wimoor.amazon.adv.common.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="t_amazon_group")
public class AmazonGroup extends BaseObject implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 4632144329348858750L;

	@Column(name = "name")
    private String name;
    
    @Column(name = "shopid")
    private String shopid;

    @Column(name = "profitcfgid")
   	protected String profitcfgid;
    
    @Column(name = "operator")
    private String operator;
    
    @Column(name = "opttime")
    private Date opttime;
    
    @Column(name = "creator")
    private String creator;
    
    @Column(name = "createtime")
    private Date createtime;

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

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid == null ? null : shopid.trim();
    }

	public String getProfitcfgid() {
		return profitcfgid;
	}

	public void setProfitcfgid(String profitcfgid) {
		this.profitcfgid = profitcfgid;
	}
    
}