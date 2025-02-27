package com.wimoor.erp.purchase.alibaba.pojo.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="PurchaseAlibabaAuth对象", description="1688绑定账号信息")
@TableName("t_erp_purchase_alibaba_message")
public class  Open1688Message  implements Serializable{
	
	/** 
	 * 
	 */
	private static final long serialVersionUID = -8668175862629700307L;

	@TableId(value = "id",type=IdType.AUTO )
	@ApiModelProperty(value = "ID")
    String id;
	
	
	@ApiModelProperty(value = "系统写入")
    @TableField(value= "content")
    private String content;
 
	@ApiModelProperty(value = "系统写入")
    @TableField(value= "signature")
    private String signature;
	
	
	@ApiModelProperty(value = "系统写入")
    @TableField(value= "opttime")
    private Date opttime;
    
}