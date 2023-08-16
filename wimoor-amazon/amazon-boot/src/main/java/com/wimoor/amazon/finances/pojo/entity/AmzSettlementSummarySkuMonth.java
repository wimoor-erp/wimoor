package com.wimoor.amazon.finances.pojo.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
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
 * @since 2023-07-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_amz_settlement_summary_sku_month")
@ApiModel(value="SettlementSummarySkuMonth对象", description="")
public class AmzSettlementSummarySkuMonth implements Serializable {

    private static final long serialVersionUID=1L;

    private String id;

    @TableField("amazonAuthId")
    private String amazonAuthId;

    private String sku;

    private String asin;
    
    private String groupid;
    
    private String parentasin;
    
    private String categoryid;

    private String msku;

    private String owner;

    private String currency;

    private String pid;

    private String mid;

    private String marketplaceName;

    private Date postedDate;

    @ApiModelProperty(value = "订单量")
    private Integer orderAmount;

    @ApiModelProperty(value = "销量")
    private Integer sales;

    private Integer refundsales;

    private Integer refundorder;

    @ApiModelProperty(value = "销售额")
    private BigDecimal principal;

    @ApiModelProperty(value = "销售佣金")
    private BigDecimal commission;

    @ApiModelProperty(value = "FBA费用")
    private BigDecimal fbafee;

    @ApiModelProperty(value = "退款金额")
    private BigDecimal refund;

    private BigDecimal shipping;

    private BigDecimal promotion;

    private BigDecimal tax;

    @ApiModelProperty(value = "其它")
    private BigDecimal otherfee;

    private BigDecimal shareStorageFee;

    private BigDecimal shareLongStorageFee;

    private BigDecimal shareAdvSpendFee;

    private BigDecimal shareCouponRedemptionFee;

    private BigDecimal shareReserveFee;

    private BigDecimal shareShopOtherFee;

	@TableField(value="share_reimbursement_fee")
	private BigDecimal shareReimbursementFee;
	
	@TableField(value="local_price")
    private BigDecimal localPrice;

	@TableField(value="local_other_cost")
    private BigDecimal localOtherCost;
    
	@TableField(value="local_return_tax")
    private BigDecimal localReturnTax;

	@TableField(value="local_fifo_shipment_fee")
    private BigDecimal localFifoShipmentFee;
	
	@TableField(value="local_fifo_cost")
    private BigDecimal localFifoCost;
	
	
	@TableField(value="profit_local_shipmentfee")
	private BigDecimal profitLocalShipmentfee;
	
	@TableField(value="profit_vat")
	private BigDecimal profitVat;
	
	@TableField(value="profit_companytax")
	private BigDecimal profitCompanytax;
	
	@TableField(value="profit_customstax")
	private BigDecimal profitCustomstax;
	
	@TableField(value="profit_exchangelost")
	private BigDecimal profitExchangelost;
	
	@TableField(value="profit_marketfee")
	private BigDecimal profitMarketfee;
	
	@TableField(value="profit_lostrate")
	private BigDecimal profitLostrate;
 
	@TableField(value="profit_otherfee")
	private BigDecimal profitOtherfee;

    private BigDecimal localShipmentItemFee;

    private BigDecimal rptStorageFee;

    private BigDecimal rptLongStorageFee;

    private BigDecimal rptAdvSpendFee;
    
    private BigDecimal rptAdvSales;
    
    private Integer rptAdvUnits;

    private BigDecimal rptReimbursementsFee;
    
    private BigDecimal finSumFee;
    
    private BigDecimal profit ;
    @TableField(exist=false)
    private List<Map<String,Object>> finDataList;
}
