package com.wimoor.erp.material.pojo.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("t_erp_material_his")
public class ERPMaterialHistory {
 
	@TableField(value= "id")
    private String id;
 
	@TableField(value= "opttime")
    private Date opttime;

	
    @TableField(value= "sku")
    private String sku;
    
    @TableField(value= "name")
    private String name;
    
    @TableField(value= "shopid")
    private String shopid;
    
    @TableField(value= "upc")
    private String upc;
    
    @TableField(value= "brand")
    private String brand;
    
    @TableField(value= "image")
    private String image;
    
    @TableField(value= "itemDimensions")
    private String itemdimensions;
    
    @TableField(value= "pkgDimensions")
    private String pkgdimensions;
    
    @TableField(value= "boxDimensions")
    private String boxdimensions;
    
    @TableField(value= "boxnum")
    private Integer boxnum;
    
    @TableField(value= "specification")
    private String specification;
    
    @TableField(value= "supplier")
    private String supplier;
    
    @TableField(value= "productCode")
    private String productcode;
    
    @TableField(value= "delivery_cycle")
    private Integer deliveryCycle;
    
    @TableField(value= "other_cost")
    private BigDecimal otherCost;
    
    @TableField(value= "MOQ")
    private int MOQ;
    
    @TableField(value= "purchaseUrl")
    private String purchaseurl;
    
    @TableField(value= "remark")
    private String remark;
    
    @TableField(value= "categoryid")
    private String categoryid;
    
    @TableField(value= "issfg")
    private String issfg;
    
    @TableField(value= "color")
    private String color;
    
    @TableField(value= "owner")
    private String owner;
    
    @TableField(value= "operator")
    private String operator;
    
    @TableField(value= "price")
    private BigDecimal price;
    
    @TableField(value= "createdate")
    private Date createdate;
    
    @TableField(value= "creator")
    private String creator;
    
    @TableField(value= "parentid")
    private String parentid;
    
    @TableField(value= "effectivedate")
    private Date effectivedate;
    
    @TableField(value= "isSmlAndLight")
    private Boolean isSmlAndLight;
    
    @TableField(value= "assembly_time")
    private Integer assemblyTime;

     
}