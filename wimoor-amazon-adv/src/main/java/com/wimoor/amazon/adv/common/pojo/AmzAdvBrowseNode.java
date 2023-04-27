package com.wimoor.amazon.adv.common.pojo;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
@Entity
@Table(name="t_amz_adv_browsenode")
public class AmzAdvBrowseNode {
	@Id
	@Column(name="id")
    private BigInteger id;

	@Column(name="name")
    private String name;

	@Column(name="parentid")
    private BigInteger parentid;

	@Column(name="country")
    private String country;

	@Column(name="is_category_root")
    private Boolean isCategoryRoot;

	@Column(name="refreshtime")
    private Date refreshtime;
	
	@Column(name="level")
    private Integer level;
	
    @Transient
    Map<BigInteger,AmzAdvBrowseNode> childrenMap;

    @Transient
    public Map<BigInteger, AmzAdvBrowseNode> getChildrenMap() {
		return childrenMap;
	}

    @Transient
	public void addChildrenMap(AmzAdvBrowseNode child) {
		if(childrenMap==null) {
			childrenMap=new HashMap<BigInteger,AmzAdvBrowseNode>();
		}
		 childrenMap.put(child.getId(), child);
	}
    @Transient
	public void addAllChildrenMap(Map<BigInteger,AmzAdvBrowseNode> allchild) {
		if(childrenMap==null) {
			childrenMap=new HashMap<BigInteger,AmzAdvBrowseNode>();
		}
		 childrenMap.putAll(allchild);;
	}
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public BigInteger getParentid() {
		return parentid;
	}

	public void setParentid(BigInteger parentid) {
		this.parentid = parentid;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

 

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country == null ? null : country.trim();
    }

    public Boolean getIsCategoryRoot() {
        return isCategoryRoot;
    }

    public void setIsCategoryRoot(Boolean isCategoryRoot) {
        this.isCategoryRoot = isCategoryRoot;
    }

    public Date getRefreshtime() {
        return refreshtime;
    }

    public void setRefreshtime(Date refreshtime) {
        this.refreshtime = refreshtime;
    }
}