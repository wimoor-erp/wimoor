package com.wimoor.amazon.follow.pojo.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author wimoor team
 * @since 2023-06-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_amz_v2_product_info_follow")
@ApiModel(value="ProductInfoFollow对象", description="")
public class ProductInfoFollow implements Serializable {

    private static final long serialVersionUID=1L;
    
	@TableId(value = "pid" )
    private String pid;

    private String statusUpload;
    private String statusPrice;

    private Integer ordersSum;

    private Date lastOrderTime;

    private Integer quantity;

    private BigDecimal oldprice;

    private BigDecimal price;

    private BigDecimal assumeprice;

    private Date executeStart;
    
    private Date executeEnd;

    private Integer deliveryCycle;
    
    private Integer  maxOrderQuantity;
    private Integer  lowestQuantity;
    private String timeid;
    
    private String remark;
    
    @TableField(exist=false)
    private String amazonauthid;
    
    private String operator;
    
    private Date opttime;
    
    private String creator;
    
    private Date createtime;
    
    private String name;
    
    private String errormsg;
    
    private String image;
    
    private String brand;
    
    private String templateid;
    
    private Date refreshtime;
    
}
