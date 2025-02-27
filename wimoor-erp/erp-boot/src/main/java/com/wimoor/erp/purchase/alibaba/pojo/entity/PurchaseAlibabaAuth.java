package com.wimoor.erp.purchase.alibaba.pojo.entity;

import java.math.BigInteger;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.ErpBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="PurchaseAlibabaAuth对象", description="1688绑定账号信息")
@TableName("t_erp_purchase_alibaba_auth")
public class PurchaseAlibabaAuth extends ErpBaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8668175862629700307L;

	@ApiModelProperty(value = "公司ID[系统写入]")
    @TableField(value= "shopid")
    private String shopid;

	@ApiModelProperty(value = "系统1688名称")
    @TableField(value= "name")
    private String name;
    
	@ApiModelProperty(value = "1688账号Key[系统写入，可以编辑]")
    @TableField(value= "appkey")
    private String appkey;
    
	@ApiModelProperty(value = "1688账号密钥[系统写入，可以编辑]")
    @TableField(value= "appsecret")
    private String appsecret;

	@ApiModelProperty(value = "当前访问令牌[系统写入]")
    @TableField(value= "access_token")
    private String accessToken;

	@ApiModelProperty(value = "刷新令牌[系统写入]")
    @TableField(value= "refresh_token")
    private String refreshToken;

	@ApiModelProperty(value = "资源所属人[系统写入]")
    @TableField(value= "resource_owner")
    private String resourceOwner;
    
	@ApiModelProperty(value = "1688账号ID[系统写入]")
    @TableField(value= "aliId")
    private BigInteger aliId;
    
	@ApiModelProperty(value = "1688 memberId[系统写入]")
    @TableField(value= "memberId")
    private String memberId;
    
	@ApiModelProperty(value = "刷新令牌过期时间[系统写入]")
    @TableField(value= "refresh_token_timeout")
    private Date refreshTokenTimeout;
    
	@ApiModelProperty(value = "当前令牌过期时间[系统写入]")
    @TableField(value= "access_token_timeout")
    private Date accessTokenTimeout;
    
	@ApiModelProperty(value = "创建人[系统写入]")
    @TableField(value= "creator")
    private String creator;

	@ApiModelProperty(value = "创建时间[系统写入]")
    @TableField(value= "createtime")
    private Date createtime;
    
	@ApiModelProperty(value = "是否删除[编辑]")
    @TableField(value= "isDelete")
    private boolean isDelete;
  
    
}