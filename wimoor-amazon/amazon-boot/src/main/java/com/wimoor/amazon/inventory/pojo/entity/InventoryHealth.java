package com.wimoor.amazon.inventory.pojo.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author wimoor team
 * @since 2022-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_amz_inventory_health")
@ApiModel(value="InventoryHealth对象",description="FBA滞销报表")
public class InventoryHealth implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "公司ID")
    private String shopid;

    @ApiModelProperty(value = "站点ID")
    private String marketplaceid;

    @ApiModelProperty(value = "SKU区分大小写")
    private String sku;

    @ApiModelProperty(value = "授权ID等价sellerid")
    private String authid;

    @ApiModelProperty(value = "报表更新时间")
	@TableField(value="snapshot_date")
    private Date snapshotDate;

    @ApiModelProperty(value = "仓库SKUID")
	@TableField(value="fnsku")
    private String fnsku;

    @ApiModelProperty(value = "ASIN产品销售ID")
    @TableField(value="asin")
    private String asin;

    @ApiModelProperty(value = "名称")
    @TableField(value="name")
    private String name;

    @ApiModelProperty(value = "是否新旧")
    private String fcondition;

    @ApiModelProperty(value = "销售排名")
	@TableField(value="sales_rank")
    private String salesRank;

    @ApiModelProperty(value = "产品分组")
	@TableField(value="product_group")
    private String productGroup;

    @ApiModelProperty(value = "当前总库存")
	@TableField(value="total_quantity")
    private String totalQuantity;

    @ApiModelProperty(value = "可销售数量")
	@TableField(value="sellable_quantity")
    private String sellableQuantity;

    @ApiModelProperty(value = "不可售数量")
	@TableField(value="unsellable_quantity")
    private String unsellableQuantity;

    @ApiModelProperty(value = "90天以内库龄数量")
   	@TableField(value="inv_age_0to90days")
    private Integer invAge0to90days;

    @ApiModelProperty(value = "91-180天以内库龄数量")
	@TableField(value="inv_age_91to180days")
    private Integer invAge91to180days;

    @ApiModelProperty(value = "181-270天以内库龄数量")
	@TableField(value="inv_age_181to270days")
    private Integer invAge181to270days;

    @ApiModelProperty(value = "271-365天以内库龄数量")
	@TableField(value="inv_age_271to365days")
    private Integer invAge271to365days;

    @ApiModelProperty(value = "365天以上库龄数量")
    @TableField(value="inv_age_365plusdays")
    private Integer invAge365plusdays;

    @ApiModelProperty(value = "最近24小时发货")
	@TableField(value="units_shipped_last24hrs")
    private String unitsShippedLast24hrs;

    @ApiModelProperty(value = "最近7天发货")
	@TableField(value="units_shipped_last7days")
    private String unitsShippedLast7days;

    @ApiModelProperty(value = "最近30天发货")
	@TableField(value="units_shipped_last30days")
    private String unitsShippedLast30days;

    @ApiModelProperty(value = "最近90天发货")
    @TableField(value="units_shipped_last90days")
    private String unitsShippedLast90days;

    @ApiModelProperty(value = "最近180天发货")
	@TableField(value="units_shipped_last180days")
    private String unitsShippedLast180days;

    @ApiModelProperty(value = "最近365天发货")
	@TableField(value="units_shipped_last365days")
    private String unitsShippedLast365days;

    @ApiModelProperty(value = "7天周转")
	@TableField(value="weeks_of_cover_t7")
    private String weeksOfCoverT7;

    @ApiModelProperty(value = "30天周转")
	@TableField(value="weeks_of_cover_t30")
    private String weeksOfCoverT30;

    @ApiModelProperty(value = "90天周转")
	@TableField(value="weeks_of_cover_t90")
    private String weeksOfCoverT90;

    @ApiModelProperty(value = "180天周转")
    @TableField(value="weeks_of_cover_t180")
    private String weeksOfCoverT180;

    @ApiModelProperty(value = "360天周转")
    @TableField(value="weeks_of_cover_t365")
    private String weeksOfCoverT365;

    @ApiModelProperty(value = "新产品的卖家数量")
    @TableField(value="num_afn_new_sellers")
    private String numAfnNewSellers;

    @ApiModelProperty(value = "旧产品的卖家数量")
    @TableField(value="num_afn_used_sellers")
    private String numAfnUsedSellers;

    @ApiModelProperty(value = "币种")
    private String currency;

    @ApiModelProperty(value = "你的售价")
    @TableField(value="your_price")
    private String yourPrice;

    @ApiModelProperty(value = "售价")
    @TableField(value="sales_price")
    private String salesPrice;

    @ApiModelProperty(value = "新产品最低售价")
    @TableField(value="lowest_afn_new_price")
    private String lowestAfnNewPrice;

    @ApiModelProperty(value = "旧产品最低售价")
    @TableField(value="lowest_afn_used_price")
    private String lowestAfnUsedPrice;

    @ApiModelProperty(value = "新产品自发货最低售价")
    @TableField(value="lowest_mfn_new_price")
    private String lowestMfnNewPrice;

    @ApiModelProperty(value = "旧产品自发货最低售价")
    @TableField(value="lowest_mfn_used_price")
    private String lowestMfnUsedPrice;

    @ApiModelProperty(value = "6个月变化")
    @TableField(value="qty_to_be_charged_ltsf_12mo")
    private String qtyToBeChargedLtsf12mo;

    @ApiModelProperty(value = "长期仓库数量")
    @TableField(value="qty_in_long_term_storage_program")
    private String qtyInLongTermStorageProgram;

    @ApiModelProperty(value = "正在移除的数量")
    @TableField(value="qty_with_removals_in_progress")
    private String qtyWithRemovalsInProgress;

    @ApiModelProperty(value = "12个月被保护数量")
    @TableField(value="projected_ltsf_12_mo")
    private String projectedLtsf12Mo;

    @ApiModelProperty(value = "库容")
    @TableField(value="per_unit_volume")
    private String perUnitVolume;

    @ApiModelProperty(value = "是否危险品")
    @TableField(value="is_hazmat")
    private String isHazmat;

    @ApiModelProperty(value = "待入库数量")
    @TableField(value="in_bound_quantity")
    private String inBoundQuantity;

    @ApiModelProperty(value = "产品限制")
    @TableField(value="asin_limit")
    private String asinLimit;

    @ApiModelProperty(value = "建议待入库数量（即发货量）")
	@TableField(value="inbound_recommend_quantity")
    private String inboundRecommendQuantity;

    @ApiModelProperty(value = "6个月变化")
	@TableField(value="qty_to_be_charged_ltsf_6mo")
    private String qtyToBeChargedLtsf6mo;

    @ApiModelProperty(value = "6个月被保护数量")
	@TableField(value="projected_ltsf_6mo")
    private String projectedLtsf6mo;
    
    @ApiModelProperty(value = "系统时间")
 	@TableField(value="opttime")
     private Date opttime;
  
}
