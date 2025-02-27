package com.wimoor.quote.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@ApiModel(value="t_supply_relationship对象", description="关系表")
@TableName("t_supply_relationship")
public class QuoteSupplyRelationship {

        @ApiModelProperty(value = "买家id")
        @TableField(value =  "buyerid")
        private String buyerid;

        @ApiModelProperty(value = "供应商id")
        @TableField(value =  "supplierid")
        private String supplierid;
}
