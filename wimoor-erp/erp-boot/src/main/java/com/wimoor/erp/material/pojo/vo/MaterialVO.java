package com.wimoor.erp.material.pojo.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.ErpBaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
 

/**
 * 用于存储产品信息
 * @author Administrator
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="Material对象", description="产品物流对象")
@TableName("t_erp_material")  
public class MaterialVO  extends ErpBaseEntity{
 
	private static final long serialVersionUID = -7177885212581975582L;
	@ApiModelProperty(value = "SKU编码【不能重复】")
	@NotNull(message="SKU不能为空")
	@Size(min=1,max=50,message="SKU的长度不能超过50个字符")
    @TableField(value = "sku")
    private String sku;

	@ApiModelProperty(value = "产品名称")
	@NotNull(message="名称不能为空")
	@Size(min=1,max=1000,message="名称的长度不能超过1000个字符")
    @TableField(value = "name")
    private String name;

	@ApiModelProperty(value = "公司ID【系统填写】")
    @TableField(value = "shopid")
    private String shopid;

	@ApiModelProperty(value = "upc【非必填】")
	@Size(max=50,message="条码的长度不能超过50个字符")
    @TableField(value = "upc")
    private String upc;

	@ApiModelProperty(value = "图片ID")
    @TableField(value = "image")
    private String image;
    
    @ApiModelProperty(value = "品牌")
	@Size(min=0,max=40,message="品牌的长度不能超过40个字符")
    @TableField(value = "brand")
    private String brand;
    
    private String brandid;
    
    @ApiModelProperty(value = "产品尺寸重量ID")
	@TableField(value = "itemDimensions")
    private String itemdimensions;

    @ApiModelProperty(value = "代包装的产品尺寸重量ID")
    @TableField(value = "pkgDimensions")
    private String pkgdimensions;
    
    @ApiModelProperty(value = "箱子尺寸重量ID")
    @TableField(value = "boxDimensions")
    private String boxdimensions;
    
    @ApiModelProperty(value = "单箱数量")
    @TableField(value = "boxnum")
    private Integer boxnum;

    @ApiModelProperty(value = "规格【非必填】")
	@Size( max=50,message="编码的长度不能超过50个字符")
    @TableField(value = "specification")
    private String specification;
    
    @ApiModelProperty(value = "供应商ID")
    @TableField(value = "supplier")
    private String supplier;
    
    @ApiModelProperty(value = "次品率")
    @TableField(value="badrate")
    private Float badrate;
    
    @ApiModelProperty(value = "vat税率")
    @TableField(value="vatrate")
    private Float vatrate;
    
    @ApiModelProperty(value = "产品编码")
	@Size( max=36,message="编码的长度不能超过36个字符")
    @TableField(value = "productCode")
    private String productCode;

    @ApiModelProperty(value = "采购周期")
    @TableField(value = "delivery_cycle")
    private Integer deliveryCycle;
    
    @ApiModelProperty(value = "组装周期")
    @TableField(value = "assembly_time")
    private Integer assemblyTime;

    @ApiModelProperty(value = "其他费用")
	@TableField(value = "other_cost")
    private BigDecimal otherCost;
    
    @ApiModelProperty(value = "MOQ")
    @TableField(value = "MOQ")
    private int MOQ;

    @ApiModelProperty(value = "采购链接")
	@Size(max=500,message="采购链接的长度不能超过500个字符")
    @TableField(value = "purchaseUrl")
    private String purchaseUrl;
    
    @ApiModelProperty(value = "备注")
	@Size(max=2000,message="备注的长度不能超过2000个字符")
    @TableField(value = "remark")
    private String remark;

    @ApiModelProperty(value = "是否组合产品【系统填写】")
    @TableField(value = "issfg")
    private String issfg;
    
    @ApiModelProperty(value = "颜色")
    @TableField(value="color")
    private String color;
    
    @ApiModelProperty(value = "分类ID")
    @TableField(value="categoryid")
    private String categoryid;

    @ApiModelProperty(value = "价格")
    @TableField(value="price")
    private BigDecimal price;
    
    @ApiModelProperty(value = "价格")
    @TableField(value="price_wavg")
    private BigDecimal priceWavg;
    
    @ApiModelProperty(value = "价格")
    @TableField(value="price_ship_wavg")
    private BigDecimal priceShipWavg;
    
    @ApiModelProperty(value = "创建人ID【系统填写】")
    @TableField(value = "creator")
    private String creator;

    @ApiModelProperty(value = "创建时间【系统填写】")
    @TableField(value = "createdate")
    private Date createdate;
    
    @ApiModelProperty(value = "产品负责人")
    @TableField(value = "owner")
    private String owner;
    
    @ApiModelProperty(value = "产品生效日期")
    @TableField(value = "effectivedate")
    private Date effectivedate;
    
    @ApiModelProperty(value = "是否轻小【已废弃】")
    @TableField(value = "isSmlAndLight")
    private Boolean isSmlAndLight;
    
    @ApiModelProperty(value = "是否归档")
    @TableField(value = "isDelete")
    private Boolean isDelete;
    
    
    @ApiModelProperty(value = "是否辅料")
    @TableField(value = "mtype")
    private Integer mtype;
    
    @TableField(exist=false)
    private String category;
    
    @TableField(exist=false)
    private String ownername;
    
    @TableField(exist=false)
    private String supplierId;
    
    @TableField(exist=false)
    @ApiModelProperty(value = "产品标签列表数据")
	String taglist;
    
    @TableField(exist=false)
    @ApiModelProperty(value = "产品标签名称列表数据")
    List<Map<String,Object>> tagNameList;
    
    @TableField(exist=false)
    @ApiModelProperty(value = "产品图片ID")
    String imageid;
    
    @TableField(exist=false)
    @ApiModelProperty(value = "产品图片数据")
    Object imgfile;
 
    @TableField(exist=false)
    @ApiModelProperty(value = "待入库")
    Integer inbound;
    
    @TableField(exist=false)
    @ApiModelProperty(value = "待出库")
    Integer outbound;
    
    @TableField(exist=false)
    @ApiModelProperty(value = "可用库存")
    Integer fulfillable;
    
 
}