package com.wimoor.amazon.product.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author wimoor team
 * @since 2023-07-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_amz_merchant_shipping_group")
@ApiModel(value="MerchantShippingGroup对象", description="")
public class MerchantShippingGroup implements Serializable {

    private static final long serialVersionUID=1L;

    private String id;

    private BigInteger amazonauthid;

    private String marketplaceid;

    private String name;
    
    private Date refreshtime;


}
