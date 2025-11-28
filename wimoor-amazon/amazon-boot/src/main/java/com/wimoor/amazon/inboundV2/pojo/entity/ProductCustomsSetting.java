package com.wimoor.amazon.inboundV2.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import java.math.BigDecimal;

@Data
@TableName("t_product_customs_setting")
@ApiModel(value="ProductCustomsSetting对象", description="ProductCustomsSetting")
public class ProductCustomsSetting {

    @TableField(value= "shopid")
    private String shopid;

    @TableField(value= "marketplaceid")
    private String marketplaceid;

    @TableField(value= "price_type")
    private String priceType;

    @TableField(value= "rate")
    private BigDecimal rate;

}
