package com.wimoor.amazon.product.pojo.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_product_remark_his")
@ApiModel(value="AmzProductRemarkHis对象", description="备注操作记录")
public class ProductRemarkHistory {

	@MppMultiId 
	@TableField("pid")
	private String pid;
	
	@MppMultiId 
	@TableField("ftype")
	private String ftype;
	
	@TableField("opttime")
	private Date opttime;
	
	private String operator;
	
	private String remark;
	
}
