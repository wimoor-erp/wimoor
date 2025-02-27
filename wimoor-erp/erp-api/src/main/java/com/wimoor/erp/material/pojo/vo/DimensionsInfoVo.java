package com.wimoor.erp.material.pojo.vo;

import java.math.BigDecimal;
import java.util.Map;
 

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.common.pojo.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="DimensionsInfo对象", description="尺寸重量对象")
@TableName("t_dimensions")
public class DimensionsInfoVo extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2142092340585757903L;

	@ApiModelProperty(value = "长度")
	@TableField(value= "length")
	private BigDecimal length;

	@ApiModelProperty(value = "长度单位")
	@TableField(value= "length_units")
	private String lengthUnits;

	@ApiModelProperty(value = "宽度")
	@TableField(value= "width")
	private BigDecimal width;

	@ApiModelProperty(value = "宽度单位")
	@TableField(value= "width_units")
	private String widthUnits;

	@ApiModelProperty(value = "高度")
	@TableField(value= "height")
	private BigDecimal height;

    @ApiModelProperty(value = "高度单位")
	@TableField(value= "height_units")
	private String heightUnits;

    @ApiModelProperty(value = "重量")
	@TableField(value= "weight")
	private BigDecimal weight;

    @ApiModelProperty(value = "重量单位")
	@TableField(value= "weight_units")
	private String weightUnits;

	// 构造函数
	public DimensionsInfoVo() {

	}

	// 构造函数
	public DimensionsInfoVo(Map<String, BigDecimal> map) {
		this.setLength(map.get("length"));
		this.setLengthUnits("cm");
		this.setWidth(map.get("width"));
		this.setWidthUnits("cm");
		this.setHeight(map.get("height"));
		this.setHeightUnits("cm");
		this.setWeight(map.get("weight"));
		this.setWeightUnits("kg");
	}

	  
}