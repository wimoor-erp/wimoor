package com.wimoor.amazon.inbound.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author wimoor team
 * @since 2022-12-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_amz_ship_fulfillment_center")
@ApiModel(value="AmzShipFulfillmentCenter对象", description="")
public class AmzShipFulfillmentCenter implements Serializable {

    private static final long serialVersionUID=1L;

    private String code;

    private String country;

    private String addressName;

    private String city;

    private String state;

    private String zip;


}
