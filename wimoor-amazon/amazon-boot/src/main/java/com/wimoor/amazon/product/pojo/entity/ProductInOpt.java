package com.wimoor.amazon.product.pojo.entity;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.io.Serializable;
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
@TableName("t_product_in_opt")
@ApiModel(value="ProductInOpt对象", description="产品信息")
public class ProductInOpt implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "产品ID")
    @TableId
    private BigInteger pid;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "价格公告")
    private String priceremark;

    @ApiModelProperty(value = "采购单价")
    private BigDecimal buyprice;

    @ApiModelProperty(value = "销售价格")
    private BigDecimal businessprice;
    
    @ApiModelProperty(value = "价格类型")
    private String businesstype;

    @ApiModelProperty(value = "价格")
    private String businesslist;

    @ApiModelProperty(value = "隐藏")
    private Boolean disable;

    @ApiModelProperty(value = "手动输入的预估销量")
    private Integer presales;

    @ApiModelProperty(value = "更新时间")
    private Date lastupdate;

    @ApiModelProperty(value = "备注")
    private String remarkAnalysis;

    @ApiModelProperty(value = "本地SKU")
    private String msku;

    @ApiModelProperty(value = "FNSKU")
    private String fnsku;

    @ApiModelProperty(value = "评论刷新时间")
    private Integer reviewDailyRefresh;

    @ApiModelProperty(value = "对应")
    private BigInteger profitid;

    @ApiModelProperty(value = "自发货可用库存")
    private Integer fulfillmentAvailability;
    
    @ApiModelProperty(value = "产品状态 0备货 1维持 2提升 3促销  4停售 5清仓 6删除")
    private Integer status;
    
    @ApiModelProperty(value = "运营负责人")
    private String owner;

    @ApiModelProperty(value = "操作人")
    private BigInteger operator;

    String merchantShippingGroup;
}
