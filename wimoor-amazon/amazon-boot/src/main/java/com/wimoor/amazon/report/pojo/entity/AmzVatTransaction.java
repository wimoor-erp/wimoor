package com.wimoor.amazon.report.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.amazon.common.pojo.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_amz_vat_transaction")
public class AmzVatTransaction extends BaseEntity {

    @TableField(value = "unique_account_identifier")
    private String uniqueAccountIdentifier;

    @TableField(value = "amazonAuthid")
    private String amazonAuthid;


    @TableField(value = "activity_period")
    private String activityPeriod;


    @TableField(value = "sales_channel")
    private String salesChannel;

    @TableField(value = "marketplace")
    private String marketplace;

    @TableField(value = "program_type")
    private String programType;

    @TableField(value = "transaction_type")
    private String transactionType;

    @TableField(value = "transaction_event_id")
    private String transactionEventId;

    @TableField(value = "activity_transaction_id")
    private String activityTransactionId;

    @TableField(value = "tax_calculation_date")
    private Date taxCalculationDate;

    @TableField(value = "transaction_depart_date")
    private Date transactionDepartDate;

    @TableField(value = "transaction_arrival_date")
    private Date transactionArrivalDate;

    @TableField(value = "transaction_complete_date")
    private Date transactionCompleteDate;

    @TableField(value = "seller_sku")
    private String sellerSku;

    @TableField(value = "asin")
    private String asin;

    @TableField(value = "item_description")
    private String itemDescription;

    @TableField(value = "item_manufacture_country")
    private String itemManufactureCountry;

    @TableField(value = "qty")
    private Integer qty;

    @TableField(value = "item_weight")
    private BigDecimal itemWeight;

    @TableField(value = "total_activity_weight")
    private BigDecimal totalActivityWeight;

    @TableField(value = "cost_price_of_items")
    private BigDecimal costPriceOfItems;

    @TableField(value = "price_of_items_amt_vat_excl")
    private BigDecimal priceOfItemsAmtVatExcl;

    @TableField(value = "promo_price_of_items_amt_vat_excl")
    private BigDecimal promoPriceOfItemsAmtVatExcl;

    @TableField(value = "total_price_of_items_amt_vat_excl")
    private BigDecimal TotalPriceOfItemsAmtVatExcl;

    @TableField(value = "ship_charge_amt_vat_excl")
    private BigDecimal shipChargeAmtVatExcl;

    @TableField(value = "promo_ship_charge_amt_vat_excl")
    private BigDecimal promoShipChargeAmtVatExcl;

    @TableField(value = "total_ship_charge_amt_vat_excl")
    private BigDecimal totalShipChargeAmtVatExcl;

    @TableField(value = "gift_wrap_amt_vat_excl")
    private BigDecimal giftWrapAmtVatExcl;

    @TableField(value = "promo_gift_wrap_amt_vat_excl")
    private BigDecimal promoGiftWrapAmtVatExcl;

    @TableField(value = "total_gift_wrap_amt_vat_excl")
    private BigDecimal totalGiftWrapAmtVatExcl;

    @TableField(value = "total_activity_value_amt_vat_excl")
    private BigDecimal totalActivityValueAmtVatExcl;

    @TableField(value = "price_of_items_vat_rate_percent")
    private BigDecimal priceOfItemsVatRatePercent;

    @TableField(value = "price_of_items_vat_amt")
    private BigDecimal priceOfItemsVatAmt;

    @TableField(value = "promo_price_of_items_vat_amt")
    private BigDecimal promoPriceOfItemsVatAmt;

    @TableField(value = "total_price_of_items_vat_amt")
    private BigDecimal totalPriceOfItemsVatAmt;

    @TableField(value = "ship_charge_vat_rate_percent")
    private BigDecimal shipChargeVatRatePercent;

    @TableField(value = "ship_charge_vat_amt")
    private BigDecimal shipChargeVatAmt;

    @TableField(value = "promo_ship_charge_vat_amt")
    private BigDecimal promoShipChargeVatAmt;

    @TableField(value = "total_ship_charge_vat_amt")
    private BigDecimal totalShipChargeVatAmt;

    @TableField(value = "gift_wrap_vat_rate_percent")
    private BigDecimal giftWrapVatRatePercent;

    @TableField(value = "gift_wrap_vat_amt")
    private BigDecimal giftWrapVatAmt;

    @TableField(value = "promo_gift_wrap_vat_amt")
    private BigDecimal promoGiftWrapVatAmt;

    @TableField(value = "total_gift_wrap_vat_amt")
    private BigDecimal totalGiftWrapVatAmt;

    @TableField(value = "total_activity_value_vat_amt")
    private BigDecimal totalActivityValueVatAmt;

    @TableField(value = "price_of_items_amt_vat_incl")
    private BigDecimal priceOfItemsAmtVatIncl;

    @TableField(value = "promo_price_of_items_amt_vat_incl")
    private BigDecimal promoPriceOfItemsAmtVatIncl;

    @TableField(value = "total_price_of_items_amt_vat_incl")
    private BigDecimal totalPriceOfItemsAmtVatIncl;

    @TableField(value = "ship_charge_amt_vat_incl")
    private BigDecimal shipChargeAmtVatIncl;

    @TableField(value = "promo_ship_charge_amt_vat_incl")
    private BigDecimal promoShipChargeAmtVatIncl;

    @TableField(value = "total_ship_charge_amt_vat_incl")
    private BigDecimal totalShipChargeAmtVatIncl;

    @TableField(value = "gift_wrap_amt_vat_incl")
    private BigDecimal giftWrapAmtVatIncl;

    @TableField(value = "promo_gift_wrap_amt_vat_incl")
    private BigDecimal promoGiftWrapAmtVatIncl;

    @TableField(value = "total_gift_wrap_amt_vat_incl")
    private BigDecimal totalGiftWrapAmtVatIncl;

    @TableField(value = "total_activity_value_amt_vat_incl")
    private BigDecimal totalActivityValueAmtVatIncl;

    @TableField(value = "transaction_currency_code")
    private String transactionCurrencyCode;

    @TableField(value = "commodity_code")
    private String commodityCode;

    @TableField(value = "statistical_code_depart")
    private String statisticalCodeDepart;

    @TableField(value = "statistical_code_arrival")
    private String statisticalCodeArrival;

    @TableField(value = "commodity_code_supplementary_unit")
    private String commodityCodeSupplementaryUnit;

    @TableField(value = "item_qty_supplementary_unit")
    private String itemQtySupplementaryUnit;

    @TableField(value = "total_activity_supplementary_unit")
    private String totalActivitySupplementaryUnit;

    @TableField(value = "product_tax_code")
    private String productTaxCode;

    @TableField(value = "depature_city")
    private String depatureCity;

    @TableField(value = "departure_country")
    private String departureCountry;
    @TableField(value = "departure_post_code")
    private String departurePostCode;
    @TableField(value = "arrival_city")
    private String arrivalCity;
    @TableField(value = "arrival_country")
    private String arrivalCountry;
    @TableField(value = "arrival_post_code")
    private String arrivalPostCode;
    @TableField(value = "sale_depart_country")
    private String saleDepartCountry;
    @TableField(value = "sale_arrival_country")
    private String saleArrivalCountry;
    @TableField(value = "transportation_mode")
    private String transportationMode;
    @TableField(value = "delivery_conditions")
    private String deliveryConditions;
    @TableField(value = "seller_depart_vat_number_country")
    private String sellerDepartVatNumberCountry;
    @TableField(value = "seller_depart_country_vat_number")
    private String sellerDepartCountryVatNumber;
    @TableField(value = "seller_arrival_vat_number_country")
    private String sellerArrivalVatNumberCountry;
    @TableField(value = "seller_arrival_country_vat_number")
    private String sellerArrivalCountryVatNumber;
    @TableField(value = "transaction_seller_vat_number_country")
    private String transactionSellerVatNumberCountry;
    @TableField(value = "transaction_seller_vat_number")
    private String transactionSellerVatNumber;
    @TableField(value = "buyer_vat_number_country")
    private String buyerVatNumberCountry;
    @TableField(value = "buyer_vat_number")
    private String buyerVatNumber;
    @TableField(value = "vat_calculation_imputation_country")
    private String vatCalculationImputationCountry;
    @TableField(value = "taxable_jurisdiction")
    private String taxableJurisdiction;
    @TableField(value = "taxable_jurisdiction_level")
    private String taxableJurisdictionLevel;
    @TableField(value = "vat_inv_number")
    private String vatInvNumber;
    @TableField(value = "vat_inv_converted_amt")
    private BigDecimal vatInvConvertedAmt;
    @TableField(value = "vat_inv_currency_code")
    private String vatInvCurrencyCode;
    @TableField(value = "vat_inv_exchange_rate")
    private BigDecimal vatInvExchangeRate;
    @TableField(value = "vat_inv_exchange_rate_date")
    private Date vatInvExchangeRateDate;
    @TableField(value = "export_outside_eu")
    private String exportOutsideEu;
    @TableField(value = "invoice_url")
    private String invoiceUrl;
    @TableField(value = "buyer_name")
    private String buyerName;
    @TableField(value = "arrival_address")
    private String arrivalAddress;
    @TableField(value = "supplier_name")
    private String supplierName;
    @TableField(value = "supplier_vat_number")
    private String supplierVatNumber;
    @TableField(value = "tax_reporting_scheme")
    private String taxReportingScheme;
    @TableField(value = "tax_collection_responsibility")
    private String taxCollectionResponsibility;


}
