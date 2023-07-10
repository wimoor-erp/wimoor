package com.wimoor.amazon.product.pojo.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;

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
 * @since 2022-06-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_amz_product_refresh")
@ApiModel(value="AmzProductRefresh对象", description="")
public class AmzProductRefresh implements Serializable {

    private static final long serialVersionUID=1L;
    @TableId(value="pid")
    private BigInteger pid;

    @TableField(value="amazonauthid")
    private BigInteger amazonauthid;

    @TableField(value="detail_refresh_time")
    private LocalDateTime detailRefreshTime;

    @TableField(value="price_refresh_time")
    private LocalDateTime priceRefreshTime;

    @TableField(value="catalog_refresh_time")
    private LocalDateTime catalogRefreshTime;
    
    @TableField(value="sku")
    private String sku;

    @TableField(value="asin")
    private String asin;

    @TableField(value="marketplaceid")
    private String marketplaceid;

    @TableField(value="isparent")
    private Boolean isparent;
    
    @TableField(value="notfound")
    private Boolean notfound;
    
}
