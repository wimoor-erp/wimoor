package com.wimoor.common.pojo.entity;

import java.math.BigDecimal;
 

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="Picture对象", description="图片")
@TableName("t_picture")
public class Picture  extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8701654826643572943L;
	@ApiModelProperty(value = "外部链接")
	@TableField(value= "url")
    private String url;

	@ApiModelProperty(value = "内部路径")
	@TableField(value= "location")
    private String location;

	@ApiModelProperty(value = "高度")
	@TableField(value= "height")
    private BigDecimal height;

	@ApiModelProperty(value = "高度单位")
	@TableField(value= "height_units")
    private String heightUnits;

	@ApiModelProperty(value = "宽度")
	@TableField(value= "width")
    private BigDecimal width;
	
	@ApiModelProperty(value = "宽度单位")
	@TableField(value= "width_units")
    private String widthUnits;

 
   
}