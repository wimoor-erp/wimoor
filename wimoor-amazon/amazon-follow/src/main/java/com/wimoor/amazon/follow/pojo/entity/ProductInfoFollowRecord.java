package com.wimoor.amazon.follow.pojo.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
@TableName("t_amz_v2_product_info_follow_record")
@ApiModel(value="ProductInfoFollow对象", description="")
public class ProductInfoFollowRecord implements Serializable {

    private static final long serialVersionUID=1L;
    
    private String pid;

    private String statusUpload;
    
    private String statusPrice;
    
    private Integer ordersSum;

    private Date lastOrderTime;

    private Integer quantity;

    private BigDecimal oldprice;

    private BigDecimal price;

    private BigDecimal assumeprice;

    private BigDecimal lowprice;

    private BigDecimal shopprice;

    private Date pricetime;
    
    private Date executeStart;
    
    private Date executeEnd;
    
    private Integer deliveryCycle;

    private String timeid;
    
    private String remark;
    
    private String errormsg;
    
    private String operator;
    
    private Date opttime;
    
    private String creator;
    
    private Date createtime;
    
    private String templateid;
}
