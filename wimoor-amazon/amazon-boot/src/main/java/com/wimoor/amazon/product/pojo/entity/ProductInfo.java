package com.wimoor.amazon.product.pojo.entity;

import java.math.BigDecimal;
import java.math.BigInteger;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.amazon.common.pojo.entity.BaseEntity;
import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 产品信息
 * </p>
 *
 * @author wimoor team
 * @since 2022-05-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_product_info")
@ApiModel(value="ProductInfo对象", description="产品信息")
public class ProductInfo extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "唯一码asin")
    @TableField("asin")
    private String asin;

    @ApiModelProperty(value = "用户码sku")
    @TableField("sku")
    private String sku;

    @ApiModelProperty(value = "站点")
    @TableField("marketplaceid")
    private String marketplaceid;

    @ApiModelProperty(value = "产品名称（产品标题）")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "创建日期")
    @TableField("openDate")
    private LocalDateTime openDate;

    @ApiModelProperty(value = "产品尺寸")
    @TableField("itemDimensions")
    private BigInteger itemDimensions;

    @ApiModelProperty(value = "含包装尺寸")
    @TableField("pageDimensions")
    private BigInteger pageDimensions;

    @ApiModelProperty(value = "交付渠道")
    @TableField("fulfillChannel")
    private String fulfillChannel;

    @ApiModelProperty(value = "装订")
    private String binding;

    @ApiModelProperty(value = "卖家数量")
    @TableField("totalOfferCount")
    private Integer totalOfferCount;

    @ApiModelProperty(value = "品牌")
    private String brand;

    @ApiModelProperty(value = "厂商")
    private String manufacturer;

    @ApiModelProperty(value = "分组")
    private String pgroup;

    @ApiModelProperty(value = "分类")
    private String typename;

    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    @ApiModelProperty(value = "照片")
    private BigInteger image;

    @ApiModelProperty(value = "父商品marketplace")
    @TableField("parentMarketplace")
    private String parentMarketplace;

    @ApiModelProperty(value = "父商品asin")
    @TableField("parentAsin")
    private String parentAsin;

    @ApiModelProperty(value = "是否副产品（即不是变体）")
    private Boolean isparent;

    @ApiModelProperty(value = "amz更新时间")
    private Date lastupdate;

    @ApiModelProperty(value = "amz创建时间")
    private Date createdate;
    
    @ApiModelProperty(value = "授权ID")
    @TableField("amazonAuthId")
    private BigInteger amazonAuthId;

    @ApiModelProperty(value = "是否无效")
    private Boolean invalid;

    @ApiModelProperty(value = "是否轻小")
    @TableField("inSnl")
    private Boolean inSnl;
 
    @ApiModelProperty(value = "隐藏")
    @TableField("disable")
    private Boolean disable;
    
    @ApiModelProperty(value = "FNSKU")
    @TableField("fnsku")
    private String fnsku;

    @ApiModelProperty(value = "Condition【new,old]等")
    @TableField("pcondition")
    private String pcondition;
    
    
    @ApiModelProperty(value = "更新时间")
    @TableField("refreshtime")
    private LocalDateTime refreshtime;
    
    /**
     * BUYABLE	清单项目可以由购物者购买。此状态不适用于供应商列表。
     * DISCOVERABLE	商品信息对购物者可见。
     */
    @ApiModelProperty(value = "更新时间")
    @TableField("status")
    private String status;
    
}
