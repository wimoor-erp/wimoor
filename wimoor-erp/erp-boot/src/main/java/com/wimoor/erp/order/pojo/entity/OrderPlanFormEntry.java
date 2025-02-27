package com.wimoor.erp.order.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wimoor.erp.common.pojo.entity.ErpBaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="Material对象", description="产品物流对象")
@TableName("t_erp_order_ship_plan_form_entry")
public class OrderPlanFormEntry extends ErpBaseEntity {
    @TableField(value = "materialid")
    private String materialid;

    @TableField(value = "formid")
    private String formid;

    @TableField(value = "quantity")
    private Integer quantity;

}
