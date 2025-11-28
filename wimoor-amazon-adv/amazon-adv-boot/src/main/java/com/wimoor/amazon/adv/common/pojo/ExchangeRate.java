package com.wimoor.amazon.adv.common.pojo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="t_exchangerate")
public class ExchangeRate  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8607931287118935365L;

	@Id
    @Column(name = "id")
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
    private Integer id;

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

    @Column(name = "volume")
    private Integer volume;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }
}