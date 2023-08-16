package com.wimoor.amazon.product.pojo.entity;

import java.math.BigDecimal;

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
 * 
 * </p>
 *
 * @author wimoor team
 * @since 2023-06-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_product_in_autoprice")
@ApiModel(value="ProductInAutoprice对象", description="")
public class ProductInAutoprice implements Serializable {

    private static final long serialVersionUID=1L;
    @TableId(value="pid")
    private String pid;

    @ApiModelProperty(value = "1代表最低价，2代表购物车")
    private Integer ftype;

    @ApiModelProperty(value = "0元 代表停用")
    private BigDecimal lowestprice;

    @ApiModelProperty(value = "false代表启动，true代表停用")
    private Boolean disable;

    private Boolean buyboxLowestFirst;

    private Boolean buyboxLowestNext;

    private BigDecimal downStepPrice;

    private BigDecimal downStepRate;

    private BigDecimal upStepPrice;

    private String operator;

    private String creator;

    private Date opttime;

    private Date createtime;


}
