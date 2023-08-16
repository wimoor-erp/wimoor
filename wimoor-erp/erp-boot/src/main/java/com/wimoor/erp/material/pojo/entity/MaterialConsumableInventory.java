package com.wimoor.erp.material.pojo.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.BaseEntity;

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
@TableName("t_erp_material_consumable_inventory")
@ApiModel(value="MaterialConsumableInventory对象", description="")
public class MaterialConsumableInventory extends BaseEntity  {

    private static final long serialVersionUID=1L;

    private String warehouseid;

    private String shopid;

    private String materialid;

    private BigDecimal quantity;


}
