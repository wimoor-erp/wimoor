package com.wimoor.amazon.inventory.pojo.entity;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.amazon.common.pojo.entity.BaseEntity;

import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * GET_FBA_INVENTORY_AGED_DATA
 * </p>
 *
 * @author wimoor team
 * @since 2022-10-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_amz_rpt_inventory_age")
@ApiModel(value="AmzRptInventoryAge对象", description="GET_FBA_INVENTORY_AGED_DATA")
public class AmzRptInventoryAge extends BaseEntity implements Serializable {

    private static final long serialVersionUID=1L;

	@TableField(value=  "authid")
    private BigInteger authid;
	
	@TableField(value=  "snapshot")
    private LocalDateTime snapshot;

	@TableField(value=  "marketplace")
    private String marketplace;

	@TableField(value=  "sku")
    private String sku;

	@TableField(value=  "fnsku")
    private String fnsku;

	@TableField(value=  "asin")
    private String asin;

	@TableField(value=  "fcondition")
    private String fcondition;

	@TableField(value=  "quantity")
    private Integer quantity;

	@TableField(value=  "qty_with_removals_in_progress")
    private Integer qtyWithRemovalsInProgress;

	@TableField(value=  "inv_age_0_to_90_days")
    private Integer invAge0To90Days;

	@TableField(value=  "inv_age_91_to_180_days")
    private Integer invAge91To180Days;

	@TableField(value=  "inv_age_181_to_270_days")
    private Integer invAge181To270Days;

	@TableField(value=  "inv_age_271_to_365_days")
    private Integer invAge271To365Days;

	@TableField(value=  "inv_age_365_plus_days")
    private Integer invAge365PlusDays;

	@TableField(value=  "currency")
    private String currency;

	@TableField(value=  "qty_to_be_charged_ltsf_6_mo")
    private Integer qtyToBeChargedLtsf6Mo;

	@TableField(value=  "projected_ltsf_6_mo")
    private BigDecimal projectedLtsf6Mo;

	@TableField(value=  "qty_to_be_charged_ltsf_12_mo")
    private Integer qtyToBeChargedLtsf12Mo;

	@TableField(value=  "projected_ltsf_12_mo")
    private BigDecimal projectedLtsf12Mo;

	@TableField(value=  "units_shipped_last_7_days")
    private Integer unitsShippedLast7Days;

	@TableField(value=  "units_shipped_last_30_days")
    private Integer unitsShippedLast30Days;

	@TableField(value=  "units_shipped_last_60_days")
    private Integer unitsShippedLast60Days;

	@TableField(value=  "units_shipped_last_90_days")
    private Integer unitsShippedLast90Days;

	@TableField(value=  "your_price")
    private BigDecimal yourPrice;

	@TableField(value=  "sales_price")
    private BigDecimal salesPrice;

	@TableField(value=  "lowest_price_new")
    private BigDecimal lowestPriceNew;

	@TableField(value=  "lowest_price_used")
    private BigDecimal lowestPriceUsed;

	@TableField(value=  "recommended_action")
    private String recommendedAction;

	@TableField(value=  "days")
    private Integer days;

	@TableField(value=  "removal_quantity")
    private BigDecimal removalQuantity;

	@TableField(value=  "estimated_cost_savings_of_recommended_actions")
    private BigDecimal estimatedCostSavingsOfRecommendedActions;

	@TableField(value=  "sell_through")
    private BigDecimal sellThrough;
	
	@TableField(value=  "item_volume")
    private BigDecimal itemVolume;

	@TableField(value=  "volume_units")
    private String volumeUnits;

	@TableField(value=  "storage_type")
    private String storageType;

}
