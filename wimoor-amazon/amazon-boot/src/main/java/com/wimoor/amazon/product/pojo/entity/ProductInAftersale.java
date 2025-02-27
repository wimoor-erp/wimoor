package com.wimoor.amazon.product.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;
import java.io.Serializable;
import java.math.BigInteger;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author wimoor team
 * @since 2022-11-28
 */
@Data
@TableName("t_product_in_aftersale")
@ApiModel(value="ProductInAftersale对象", description="")
public class ProductInAftersale implements Serializable {
	private static final long serialVersionUID=1L;
	
	
	@TableId(value = "id" )
    String id;

    private BigInteger groupid;

    private BigInteger amazonauthid;

    private String marketplaceid;

    private String sku;

    private Date date;

    private Integer quantity;


}
