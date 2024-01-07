package com.wimoor.amazon.product.pojo.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用于记录调价
 * </p>
 *
 * @author wimoor team
 * @since 2023-06-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_amz_product_price_record")
@ApiModel(value="AmzProductPriceRecord对象", description="用于记录调价")
public class AmzProductPriceRecord implements Serializable {

    private static final long serialVersionUID=1L;
    @TableField(value="pid")
    private String pid;

    private Date opttime;

    private BigDecimal lowestprice;

    private BigDecimal price;

    private BigDecimal refprice;

    private BigDecimal oldprice;

    private BigDecimal shipprice;

    private BigDecimal oldshipprice;

    private Date startdate;

    private Date enddate;

    @ApiModelProperty(value = "1:永久调价，2，临时调价，3，商务调价")
    private Integer ftype;

    private String remark;
    
    private String operator;


}
