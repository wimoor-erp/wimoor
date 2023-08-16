package com.wimoor.erp.material.pojo.entity;

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
 * @since 2023-05-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_erp_material_consumable_safety_stock")
@ApiModel(value="ErpMaterialConsumableSafetyStock对象", description="")
public class MaterialConsumableSafetyStock extends ErpBaseEntity {

    private static final long serialVersionUID=1L;

    private String shopid;

    private Integer amount;



}
