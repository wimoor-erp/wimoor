package com.wimoor.amazon.profit.pojo.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import lombok.Data;
 

@Data
@TableName("t_producttier")  
@ApiModel(value="ProductTier对象", description="产品尺寸")
public class ProductTier implements Serializable{
	/**
	 * 
	 */
	@TableField(exist=false)
	private static final long serialVersionUID = -2427268901117385097L;

    @TableId(value= "id")
    private String id;

	@TableField(value= "name")
    private String name;

	@TableField(value= "isStandard")
    private Boolean isstandard;

	@TableField(value= "country")
    private String country;
    
	@TableField(value= "box_weight")
    private BigDecimal boxWeight;
	
	@TableField(value= "amz_name")
    private String amzName;

    public String getAmzName() {
		return amzName;
	}

	public void setAmzName(String amzName) {
		this.amzName = amzName;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Boolean getIsstandard() {
        return isstandard;
    }

    public void setIsstandard(Boolean isstandard) {
        this.isstandard = isstandard;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country == null ? null : country.trim();
    }

	public BigDecimal getBoxWeight() {
		return boxWeight;
	}

	public void setBoxWeight(BigDecimal boxWeight) {
		this.boxWeight = boxWeight;
	}
    
}