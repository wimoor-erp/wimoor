package com.wimoor.amazon.report.pojo.entity;


import java.io.Serializable;
import java.util.Date;

 

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;

import io.swagger.annotations.ApiModel;
import lombok.Data;

 
@Data
@TableName("t_amz_product_active_daynum")  
@ApiModel(value="AmzProductActiveDayNum对象", description="产品")
public class AmzProductActiveDayNum implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5333314807495620167L;

	@MppMultiId
	@TableField(value=  "amazonauthid")
    private String amazonauthid;

	@MppMultiId
	@TableField(value=  "marketplaceid")
    private String marketplaceid;

	@MppMultiId
	@TableField(value=  "byday")
    private Date byday;
	
	@TableField(value=  "num")
    private Integer num;
 
}