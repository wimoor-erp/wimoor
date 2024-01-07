package com.wimoor.amazon.adv.common.pojo;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
@Entity
@Table(name="t_exchangerate_customer")
public class ExchangeRateCustomer extends BaseObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2361231061507591530L;

	@Column(name = "shopid")
    private String shopid;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "type")
    private String type;

    @Column(name = "utctime")
    private Date utctime;

    @Column(name = "operator")
    private String operator;

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol == null ? null : symbol.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public Date getUtctime() {
        return utctime;
    }

    public void setUtctime(Date utctime) {
        this.utctime = utctime;
    }

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

  
}