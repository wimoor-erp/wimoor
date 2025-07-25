package com.wimoor.amazon.feed.pojo.entity;


import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@TableName("t_erp_amazon_feedstatus")  
@ApiModel(value="AmazonFeedStatus对象", description="AmazonFeedStatus")
public class AmazonFeedStatus implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = -6732500143285424759L;
	@NotNull(message="状态不能为空")
	@Size(min=1,max=50,message="状态的长度不能超过50个字符")
	@TableId(value= "status")
    private String status;

	@NotNull(message="名称不能为空")
	@Size(min=1,max=50,message="名称的长度不能超过50个字符")
	@TableField(value= "name")
    private String name;

	@Size(max=100,message="备注的长度不能超过100个字符")
	@TableField(value= "remark")
    private String remark;

 
}
