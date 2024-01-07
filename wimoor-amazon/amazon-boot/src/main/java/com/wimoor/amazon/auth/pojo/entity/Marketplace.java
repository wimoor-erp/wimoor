package com.wimoor.amazon.auth.pojo.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("t_marketplace")
public class Marketplace implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5558303729383204310L;

	@TableId(value = "marketplaceId" )
    private String marketplaceid;

	@TableField(value =  "market")
    private String market;

	@TableField(value =  "name")
    private String name;

	@TableField(value =  "region_name")
    private String regionName;

	@TableField(value =  "region")
    private String region;

	@TableField(value =  "end_point")
    private String endPoint;

	@TableField(value =  "point_name")	
    private String pointName;

	@TableField(value =  "accessKey")	
    private String accesskey;

	@TableField(value =  "secretKey")	
    private String secretkey;

	@TableField(value =  "dim_units")	
    private String dimUnits;

	@TableField(value =  "weight_units")	
    private String weightUnits;

	@TableField(value =  "currency")	
    private String currency;

	@TableField(value =  "findex")	
    private Integer findex;
	
	@TableField(value =  "adv_end_point")	
    private String advEndPoint;
	
	@TableField(value =  "aws_access_key")	
    private String AwsAccessKey;
	
	@TableField(value =  "aws_secret_key")	
    private String AwsSecretKey;
	
	@TableField(value =  "associate_tag")	
    private String AssociateTag;
	
	@TableField(value =  "developer_url")	
    private String developerUrl;
	
	@TableField(value =  "dev_account_num")	
    private String devAccountNum;
	
	@TableField(value =  "bytecode")	
	private String bytecode;
	
	@TableField(value =  "sp_api_endpoint")	
	private String spApiEndpoint;
	
	@TableField(value =  "aws_region")	
	private String awsRegion;
}