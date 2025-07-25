package com.wimoor.quote.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@ApiModel(value="SupplyRelationship", description="关系")
@TableName("t_supply_relationship")
public class SupplyRelationship {
    @ApiModelProperty(value = "Buyer")
    @TableField(value =  "buyerid")
    private String buyerid;

    @ApiModelProperty(value = "Supplier")
    @TableField(value =  "supplierid")
    private String supplierid;
}
