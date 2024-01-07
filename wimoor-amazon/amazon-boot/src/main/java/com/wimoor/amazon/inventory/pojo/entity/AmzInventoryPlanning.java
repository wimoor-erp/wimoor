package com.wimoor.amazon.inventory.pojo.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
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
 * @since 2022-12-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_amz_inventory_planning")
@ApiModel(value="AmzInventoryPlanning对象", description="")
public class AmzInventoryPlanning implements Serializable {

    private static final long serialVersionUID=1L;

    private Date snapshotDate;
    
    private String countrycode;
    
    private String amazonauthid;
    
    private String sku;

    private String fnsku;

    @TableField(value= "`asin`")
    private String asin;
    
    @TableField(value= "`condition`")
    private String condition;

    private Integer available;

    private Integer pendingRemovalQuantity;

    private String currency;
    
    @TableField(value= "inv_age_0_to_90_days")
    private Integer invAge0To90Days;

    @TableField(value= "inv_age_91_to_180_days")
    private Integer invAge91To180Days;

    @TableField(value= "inv_age_181_to_270_days")
    private Integer invAge181To270Days;

    @TableField(value= "inv_age_271_to_365_days")
    private Integer invAge271To365Days;

    @TableField(value= "inv_age_365_plus_days")
    private Integer invAge365PlusDays;

    @TableField(value= "qty_to_be_charged_ltsf_11_mo")
    private BigDecimal qtyToBeChargedLtsf11Mo;

    @TableField(value= "projected_ltsf_11_mo")
    private BigDecimal projectedLtsf11Mo;

    @TableField(value= "qty_to_be_charged_ltsf_12_mo")
    private BigDecimal qtyToBeChargedLtsf12Mo;

    private BigDecimal estimatedLtsfNextCharge;

    private Integer unitsShippedT7;

    private Integer unitsShippedT30;

    private Integer unitsShippedT60;

    private Integer unitsShippedT90;

    private String alert;

    private BigDecimal yourPrice;

    private BigDecimal salesPrice;

    private BigDecimal lowestPriceNewPlusShipping;

    private BigDecimal lowestPriceUsed;

    private String recommendedAction;

    private Integer healthyInventoryLevel;

    private BigDecimal recommendedSalesPrice;

    private Integer recommendedSaleDurationDays;

    private Integer recommendedRemovalQuantity;

    private BigDecimal estimatedCostSavingsOfRecommendedActions;

    private BigDecimal sellThrough;

    private BigDecimal itemVolume;

    private String volumeUnitMeasurement;

    private String storageType;

    private BigDecimal storageVolume;

    

    private String productGroup;

    private Integer salesRank;

    private Integer daysOfSupply;

    private Integer estimatedExcessQuantity;

    private Integer weeksOfCoverT30;

    private Integer weeksOfCoverT90;

    private BigDecimal featuredofferPrice;

    
    @TableField(value= "sales_shipped_last_7_days")
    private BigDecimal salesShippedLast7Days;

    @TableField(value= "sales_shipped_last_30_days")
    private BigDecimal salesShippedLast30Days;

    @TableField(value= "sales_shipped_last_60_days")
    private BigDecimal salesShippedLast60Days;

    @TableField(value= "sales_shipped_last_90_days")
    private BigDecimal salesShippedLast90Days;

    @TableField(value= "inv_age_0_to_30_days")
    private Integer invAge0To30Days;

    @TableField(value= "inv_age_31_to_60_days")
    private Integer invAge31To60Days;

    @TableField(value= "inv_age_61_to_90_days")
    private Integer invAge61To90Days;

    @TableField(value= "inv_age_181_to_330_days")
    private Integer invAge181To330Days;

    @TableField(value= "inv_age_331_to_365_days")
    private Integer invAge331To365Days;

    private BigDecimal estimatedStorageCostNextMonth;

    private Integer inboundQuantity;

    private Integer inboundWorking;

    private Integer inboundShipped;

    private Integer inboundReceived;

    @TableField(value= "no_sale_last_6_months")
    private Integer noSaleLast6Months;

    private Integer reservedQuantity;

    private Integer unfulfillableQuantity;
    
    private Integer afnResearchingQuantity;
    
    private Integer afnReservedFutureSupply;
    
    private Integer afnFutureSupplyBuyable;

    @TableField(value= "quantity_to_be_charged_ais_181_210_days")
    private Integer quantityToBeChargedAis181T210Days;
    
    @TableField(value= "quantity_to_be_charged_ais_211_240_days")
    private Integer quantityToBeChargedAis211T240Days;
    
    @TableField(value= "quantity_to_be_charged_ais_241_270_days")
    private Integer quantityToBeChargedAis241T270Days;
    
    @TableField(value= "quantity_to_be_charged_ais_271_300_days")
    private Integer quantityToBeChargedAis271T300Days;
    
    @TableField(value= "quantity_to_be_charged_ais_301_330_days")
    private Integer quantityToBeChargedAis301T330Days;
    
    @TableField(value= "quantity_to_be_charged_ais_331_365_days")
    private Integer quantityToBeChargedAis331T365Days;
    
    @TableField(value= "quantity_to_be_charged_ais_365_plus_days")
    private Integer quantityToBeChargedAis365PlusDays;

    @TableField(value= "estimated_ais_181_210_days")
    private BigDecimal estimatedAis181T210Days;

    @TableField(value= "estimated_ais_211_240_days")
    private BigDecimal estimatedAis211T240Days;

    @TableField(value= "estimated_ais_241_270_days")
    private BigDecimal estimatedAis241T270Days;

    @TableField(value= "estimated_ais_271_300_days")
    private BigDecimal estimatedAis271T300Days;

    @TableField(value= "estimated_ais_301_330_days")
    private BigDecimal estimatedAis301T330Days;
    
    @TableField(value= "estimated_ais_331_365_days")
    private BigDecimal estimatedAis331T365Days;

    @TableField(value= "estimated_ais_365_plus_days")
    private BigDecimal estimatedAis365PlusDays;
    
}
