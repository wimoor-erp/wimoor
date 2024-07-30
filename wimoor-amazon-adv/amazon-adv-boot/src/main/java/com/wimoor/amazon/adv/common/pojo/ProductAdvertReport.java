package com.wimoor.amazon.adv.common.pojo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;




@Entity
@Table(name="t_amz_advert_report")
public class ProductAdvertReport  {
 
    @Column(name = "id")
	protected String id;
 
	public String getId() {
		if (null == id)
			id = UUID.randomUUID().toString();

		return id;
	}

	public Boolean HasId() {
		return id != null ? true : false;
	}

	public void setId(String id) {
		this.id = id == null ? null : id.trim();
	}
	@Id
	@Column(name="sellerid")
    private String sellerid;

	@Id
	@Column(name="sku")
    private String sku;

	@Column(name="asin")
    private String asin;
	
	@Id
	@Column(name="marketplaceid")
    private String marketplaceid;

	@Id
	@Column(name="bydate")
    private Date bydate;

	@Id
	@Column(name="campaign_name")
	private String campaignName;
	
	@Id
	@Column(name="ad_Group_Name")
	private String adGroupName;

 
    public String getMainKey() {
    	return sellerid+"#"+bydate.getTime();
    }
    
    public  static Map<String,Object> getMainKeyItem(String key) {
    	String[] keys= key.split("#");
    	Map<String,Object> keyMap=new HashMap<String,Object>();
    	keyMap.put("sellerid", keys[0]);
    	keyMap.put("bydate", keys[1]);
    	return keyMap;
    }
    
    public String getSubKey() {
    	return marketplaceid+"#"+sku;
    }
    
    public  static Map<String,Object> getSubKeyItem(String key) {
    	String[] keys= key.split("#");
    	Map<String,Object> keyMap=new HashMap<String,Object>();
    	keyMap.put("marketplaceid", keys[0]);
    	keyMap.put("sku", keys[1]);
    	return keyMap;
    }
    public String getSellerid() {
		return sellerid;
	}

	public void setSellerid(String sellerid) {
		this.sellerid = sellerid;
	}

	public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku == null ? null : sku.trim();
    }

    public String getAsin() {
		return asin;
	}

	public void setAsin(String asin) {
		this.asin = asin;
	}

	public String getMarketplaceid() {
        return marketplaceid;
    }

    public void setMarketplaceid(String marketplaceid) {
        this.marketplaceid = marketplaceid == null ? null : marketplaceid.trim();
    }

	public Date getBydate() {
		return bydate;
	}

	public void setBydate(Date bydate) {
		this.bydate = bydate;
	}
    public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public String getAdGroupName() {
		return adGroupName;
	}

	public void setAdGroupName(String adGroupName) {
		this.adGroupName = adGroupName;
	}

	
	@Column(name="clicks")
    private Integer clicks;

	@Column(name="impressions")
    private Integer impressions;

	@Column(name="ctr")
    private Double ctr;

	@Column(name="currency")
    private String currency;

	@Column(name="spend")
    private BigDecimal spend;

	@Column(name="cpc")
    private BigDecimal cpc;
	
	@Column(name="totalsales")
    private BigDecimal totalsales;
	
	@Column(name="orders")
	private Integer orders;
 
	@Column(name="acos")
    private Double acos;
	
	@Column(name="RoAS")
    private Double roas;
	
	@Column(name="spc")
    private Double spc;
	
	@Column(name="units")
	private Integer units;
	
	public BigDecimal getTotalsales() {
		if(totalsales==null)return new BigDecimal("0");
		return totalsales;
	}

	public void setTotalsales(BigDecimal totalsales) {
		this.totalsales = totalsales;
	}

	public Double getSpc() {
		return spc;
	}

	public void setSpc(Double spc) {
		this.spc = spc;
	}

	public Double getAcos() {
		return acos;
	}

	public void setAcos(Double acos) {
		this.acos = acos;
	}

	public Double getRoas() {
		return roas;
	}

	public void setRoas(Double roas) {
		this.roas = roas;
	}

	public Integer getOrders() {
		return orders;
	}

	public void setOrders(Integer orders) {
		this.orders = orders;
	}

	public Integer getClicks() {
        return clicks;
    }

    public void setClicks(Integer clicks) {
        this.clicks = clicks;
    }

    public Integer getImpressions() {
        return impressions;
    }

    public void setImpressions(Integer impressions) {
        this.impressions = impressions;
    }

    public Double getCtr() {
        return ctr;
    }

    public void setCtr(Double ctr) {
        this.ctr = ctr;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency == null ? null : currency.trim();
    }

	public BigDecimal getSpend() {
		return spend;
	}

	public void setSpend(BigDecimal spend) {
		this.spend = spend;
	}

	public BigDecimal getCpc() {
		return cpc;
	}

	public void setCpc(BigDecimal cpc) {
		this.cpc = cpc;
	}

	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		ProductAdvertReport current=(ProductAdvertReport)o;
        if(current.bydate.after(this.bydate)){
            return 1;
        }else if(current.bydate.equals(this.bydate)){
            return 0;
        }
        return -1;
	}

	public Integer getUnits() {
		return units;
	}

	public void setUnits(Integer units) {
		this.units = units;
	}

   
    

    
}