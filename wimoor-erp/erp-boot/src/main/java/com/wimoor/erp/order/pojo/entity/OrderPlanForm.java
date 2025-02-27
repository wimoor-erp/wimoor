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
import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="Material对象", description="产品物流对象")
@TableName("t_erp_order_ship_plan_form")
public class OrderPlanForm extends ErpBaseEntity {
    @TableField(value = "shopid")
    private String shopid;

    @TableField(value = "warehouseid")
    private String warehouseid;

    @TableField(value = "auditstatus")
    private Integer auditstatus;

    @TableField(value = "number")
    private String number;

    @TableField(value = "creator")
    private String creator;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "createtime")
    private Date createtime;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "audittime")
    private Date audittime;

    @TableField(exist=false)
    List<OrderPlanFormEntry> entryList;
}
