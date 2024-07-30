package com.wimoor.erp.purchase.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.ErpBaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author wimoor team
 * @since 2023-05-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_erp_v3_purchase_plan_consumable_item")
@ApiModel(value="PurchasePlanConsumableItem对象", description="")
public class PurchasePlanConsumableItem extends ErpBaseEntity{

    private static final long serialVersionUID=1L;

    private String materialid;

    private String warehouseid;

    private Integer amount;

    private String shopid;

}
