package com.wimoor.amazon.auth.pojo.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.amazon.common.pojo.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
 
@Data
@ApiModel(value="店铺AmazonGroup对象", description="亚马逊店铺")
@EqualsAndHashCode(callSuper = true)
@TableName("t_amazon_group")
public class AmazonGroup extends BaseEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = 4632144329348858750L;

	@ApiModelProperty(value = "店铺名称")
	@TableField(value =  "name")
    private String name;
    
	@ApiModelProperty(value = "公司ID【系统填写】")
    @TableField(value =  "shopid")
    private String shopid;

	@ApiModelProperty(value = "匹配的利润计算方案ID")
    @TableField(value =  "profitcfgid")
   	protected String profitcfgid;
    
	@ApiModelProperty(value = "排序号")
    @TableField(value =  "findex")
   	protected Integer findex;
	
	@ApiModelProperty(value = "操作人ID【系统填写】")
    @TableField(value =  "operator")
    private String operator;
    
	@ApiModelProperty(value = "操作时间【系统填写】")
    @TableField(value =  "opttime")
    private Date opttime;
    
	@ApiModelProperty(value = "创建人ID【系统填写】")
    @TableField(value =  "creator")
    private String creator;
    
	@ApiModelProperty(value = "创建时间【系统填写】")
    @TableField(value =  "createtime")
    private Date createtime;
	
	@ApiModelProperty(value = "逻辑删除")
    @TableField(value =  "isdelete")
    private Boolean isdelete;
 
}