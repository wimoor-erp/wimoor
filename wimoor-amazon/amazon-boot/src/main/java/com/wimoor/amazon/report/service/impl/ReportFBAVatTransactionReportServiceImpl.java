package com.wimoor.amazon.report.service.impl;

import cn.hutool.core.util.StrUtil;
import com.amazon.spapi.model.reports.CreateReportSpecification;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.report.mapper.AmzVatTransactionMapper;
import com.wimoor.amazon.report.pojo.entity.AmzVatTransaction;
import com.wimoor.amazon.report.pojo.entity.ReportType;
import com.wimoor.amazon.util.AmzDateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("reportFBAVatTransactionReportService")
public class ReportFBAVatTransactionReportServiceImpl extends ReportServiceImpl {
    @Resource
    AmzVatTransactionMapper amzVatTransactionMapper;

    private String getStrValue(String[] info, Map<String, Integer> titleList, String key) {
        Integer position = titleList.get(key.toUpperCase());
        if(position==null)return null;
        if(position<info.length) {
            return info[position];
        }
        return null;
    }
    private Integer getIntegerValue(String[] info, Map<String, Integer> titleList, String key) {
        Integer position = titleList.get(key.toUpperCase());
        if(position==null)return null;
        if(position<info.length) {
            return Integer.parseInt(info[position]);
        }
        return null;
    }



    private BigDecimal getBigDecimalValue(String[] info, Map<String, Integer> titleList, String key) {
        Integer position = titleList.get(key.toUpperCase());
        if(position==null)return null;
        if(position<info.length) {
            if(isNumericzidai(info[position])) {
                return new BigDecimal(info[position]);
            }
        }
        return null;
    }

    public static boolean isNumericzidai(String str) {
        if (str == null)
            return false;
        Pattern pattern = Pattern.compile("-?[0-9]+\\.?[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 获取前一个月的第一天
     * @param current 当前日期Calendar对象
     * @return 前一个月第一天的Calendar对象
     */
    public static Calendar getFirstDayOfPreviousMonth(Calendar current) {
        Calendar prevMonth = (Calendar) current.clone();
        // 将月份减1
        prevMonth.add(Calendar.MONTH, -1);
        // 设置为该月第一天
        prevMonth.set(Calendar.DAY_OF_MONTH, 3);
        // 清除时间部分
        clearTime(prevMonth);
        return prevMonth;
    }

    /**
     * 获取前一个月的最后一天
     * @param current 当前日期Calendar对象
     * @return 前一个月最后一天的Calendar对象
     */
    public static Calendar getLastDayOfPreviousMonth(Calendar current) {
        Calendar prevMonth = (Calendar) current.clone();
        // 设置为当前月第一天
        prevMonth.set(Calendar.DAY_OF_MONTH, 1);
        // 减1天得到上个月最后一天
        prevMonth.add(Calendar.DATE, -1);
        // 清除时间部分
        clearTime(prevMonth);
        return prevMonth;
    }

    /**
     * 清除Calendar对象的时间部分
     * @param calendar 要处理的Calendar对象
     */
    private static void clearTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }


    public  void   requestReport(AmazonAuthority amazonAuthority, Calendar cstart, Calendar cend, Boolean ignore) {
        List<Marketplace> marketlist = marketplaceService.findbyauth(amazonAuthority.getId());
        Calendar currentDate = Calendar.getInstance();
        for(Marketplace market:marketlist) {
            if(market.getMarket().equals("DE") || market.getMarket().equals("ES") ||
                    market.getMarket().equals("IT") || market.getMarket().equals("FR") || market.getMarket().equals("UK")) {
                CreateReportSpecification body=new CreateReportSpecification();
                body.setReportType(myReportType());
                Calendar firstDayOfPrevMonth = getFirstDayOfPreviousMonth(currentDate);
                Calendar lastDayOfPrevMonth = getLastDayOfPreviousMonth(currentDate);
                body.setDataStartTime(AmzDateUtils.getOffsetDateTimeUTC(firstDayOfPrevMonth));
                body.setDataEndTime(AmzDateUtils.getOffsetDateTimeUTC(lastDayOfPrevMonth));
                List<String> list=new ArrayList<String>();
                list.add(market.getMarketplaceid());
                body.setMarketplaceIds(list);
                callCreateAPI(this, body, amazonAuthority, market,firstDayOfPrevMonth.getTime(),lastDayOfPrevMonth.getTime());
                break;
            }
        }
    }

    @Override
    public String treatResponse(AmazonAuthority amazonAuthority, BufferedReader br) {
        int lineNumber = 0;
        String line;
        String mlog="";
        Date startDate=null;
        Map<String,Integer> titleList = new HashMap<String,Integer>();
        try {
            while ((line = br.readLine()) != null) {
                String[] info = line.split("\t");
                if (lineNumber == 0) {
                    for (int i = 0; i < info.length; i++) {
                        titleList.put(info[i],i);
                    }
                }else {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    AmzVatTransaction report = new AmzVatTransaction();
                    report.setUniqueAccountIdentifier(getStrValue(info,titleList,"unique_account_identifier"));
                    report.setActivityPeriod(getStrValue(info,titleList,"activity_period"));
                    report.setItemDescription(getStrValue(info,titleList,"item_description"));
                    report.setSalesChannel(getStrValue(info,titleList,"sales_channel"));
                    report.setMarketplace(getStrValue(info,titleList,"marketplace"));
                    report.setProgramType(getStrValue(info,titleList,"program_type"));
                    report.setTransactionType(getStrValue(info,titleList,"transaction_type"));
                    report.setTransactionEventId(getStrValue(info,titleList,"transaction_event_id"));
                    report.setActivityTransactionId(getStrValue(info,titleList,"activity_transaction_id"));
                    if(StrUtil.isNotBlank(getStrValue(info,titleList,"tax_calculation_date"))){
                        report.setTaxCalculationDate(sdf.parse(getStrValue(info,titleList,"tax_calculation_date")));
                    }
                    if(StrUtil.isNotBlank(getStrValue(info,titleList,"transaction_depart_date"))){
                        report.setTransactionDepartDate(sdf.parse(getStrValue(info,titleList,"transaction_depart_date")));
                    }
                    if(StrUtil.isNotBlank(getStrValue(info,titleList,"transaction_arrival_date"))){
                        report.setTransactionArrivalDate(sdf.parse(getStrValue(info,titleList,"transaction_arrival_date")));
                    }
                    if(StrUtil.isNotBlank(getStrValue(info,titleList,"transaction_complete_date"))){
                        report.setTransactionCompleteDate(sdf.parse(getStrValue(info,titleList,"transaction_complete_date")));
                    }
                    if(report.getTransactionCompleteDate()!=null&&(startDate==null|| report.getTransactionCompleteDate().before(startDate))){
                        startDate=report.getTransactionCompleteDate();
                    }
                    report.setSellerSku(getStrValue(info,titleList,"seller_sku"));
                    report.setAsin(getStrValue(info,titleList,"asin"));
                    report.setItemManufactureCountry(getStrValue(info,titleList,"item_manufacture_country"));
                    report.setQty(getIntegerValue(info,titleList,"qty"));
                    report.setItemWeight(getBigDecimalValue(info,titleList,"item_weight"));
                    report.setTotalActivityWeight(getBigDecimalValue(info,titleList,"total_activity_weight"));
                    report.setCostPriceOfItems(getBigDecimalValue(info,titleList,"cost_price_of_items"));
                    report.setPriceOfItemsAmtVatExcl(getBigDecimalValue(info,titleList,"price_of_items_amt_vat_excl"));
                    report.setPromoPriceOfItemsAmtVatExcl(getBigDecimalValue(info,titleList,"promo_price_of_items_amt_vat_excl"));
                    report.setTotalPriceOfItemsAmtVatExcl(getBigDecimalValue(info,titleList,"total_price_of_items_amt_vat_excl"));
                    report.setShipChargeAmtVatExcl(getBigDecimalValue(info,titleList,"ship_charge_amt_vat_excl"));
                    report.setPromoShipChargeAmtVatExcl(getBigDecimalValue(info,titleList,"promo_ship_charge_amt_vat_excl"));
                    report.setTotalShipChargeAmtVatExcl(getBigDecimalValue(info,titleList,"total_ship_charge_amt_vat_excl"));
                    report.setGiftWrapAmtVatExcl(getBigDecimalValue(info,titleList,"gift_wrap_amt_vat_excl"));
                    report.setPromoGiftWrapAmtVatExcl(getBigDecimalValue(info,titleList,"promo_gift_wrap_amt_vat_excl"));
                    report.setTotalGiftWrapAmtVatExcl(getBigDecimalValue(info,titleList,"total_gift_wrap_amt_vat_excl"));
                    report.setTotalActivityValueAmtVatExcl(getBigDecimalValue(info,titleList,"total_activity_value_amt_vat_excl"));
                    report.setPriceOfItemsVatRatePercent(getBigDecimalValue(info,titleList,"price_of_items_vat_rate_percent"));
                    report.setPriceOfItemsVatAmt(getBigDecimalValue(info,titleList,"price_of_items_vat_amt"));
                    report.setPromoPriceOfItemsVatAmt(getBigDecimalValue(info,titleList,"promo_price_of_items_vat_amt"));
                    report.setTotalPriceOfItemsAmtVatExcl(getBigDecimalValue(info,titleList,"total_price_of_items_amt_vat_excl"));
                    report.setShipChargeVatRatePercent(getBigDecimalValue(info,titleList,"ship_charge_vat_rate_percent"));
                    report.setShipChargeVatAmt(getBigDecimalValue(info,titleList,"ship_charge_vat_amt"));
                    report.setPromoShipChargeVatAmt(getBigDecimalValue(info,titleList,"promo_ship_charge_vat_amt"));
                    report.setTotalShipChargeAmtVatExcl(getBigDecimalValue(info,titleList,"total_ship_charge_amt_vat_excl"));
                    report.setGiftWrapVatRatePercent(getBigDecimalValue(info,titleList,"gift_wrap_vat_rate_percent"));
                    report.setGiftWrapVatAmt(getBigDecimalValue(info,titleList,"gift_wrap_vat_amt"));
                    report.setPromoGiftWrapVatAmt(getBigDecimalValue(info,titleList,"promo_gift_wrap_vat_amt"));
                    report.setTotalGiftWrapAmtVatExcl(getBigDecimalValue(info,titleList,"total_gift_wrap_amt_vat_excl"));
                    report.setTotalActivityValueVatAmt(getBigDecimalValue(info,titleList,"total_activity_value_vat_amt"));
                    report.setPriceOfItemsAmtVatIncl(getBigDecimalValue(info,titleList,"price_of_items_amt_vat_incl"));
                    report.setPromoPriceOfItemsAmtVatIncl(getBigDecimalValue(info,titleList,"promo_price_of_items_amt_vat_incl"));
                    report.setTotalPriceOfItemsAmtVatIncl(getBigDecimalValue(info,titleList,"total_price_of_items_amt_vat_incl"));
                    report.setShipChargeAmtVatIncl(getBigDecimalValue(info,titleList,"ship_charge_amt_vat_incl"));
                    report.setPromoShipChargeAmtVatIncl(getBigDecimalValue(info,titleList,"promo_ship_charge_amt_vat_incl"));
                    report.setTotalShipChargeAmtVatIncl(getBigDecimalValue(info,titleList,"total_ship_charge_amt_vat_incl"));
                    report.setGiftWrapAmtVatIncl(getBigDecimalValue(info,titleList,"gift_wrap_amt_vat_incl"));
                    report.setPromoGiftWrapAmtVatIncl(getBigDecimalValue(info,titleList,"promo_gift_wrap_amt_vat_incl"));
                    report.setTotalGiftWrapAmtVatIncl(getBigDecimalValue(info,titleList,"total_gift_wrap_amt_vat_incl"));
                    report.setTotalActivityValueAmtVatIncl(getBigDecimalValue(info,titleList,"total_activity_value_amt_vat_incl"));
                    report.setTransactionCurrencyCode(getStrValue(info,titleList,"transaction_currency_code"));
                    report.setCommodityCode(getStrValue(info,titleList,"commodity_code"));
                    report.setStatisticalCodeDepart(getStrValue(info,titleList,"statistical_code_depart"));
                    report.setStatisticalCodeArrival(getStrValue(info,titleList,"statistical_code_arrival"));
                    report.setCommodityCodeSupplementaryUnit(getStrValue(info,titleList,"commodity_code_supplementary_unit"));
                    report.setItemQtySupplementaryUnit(getStrValue(info,titleList,"item_qty_supplementary_unit"));
                    report.setTotalActivitySupplementaryUnit(getStrValue(info,titleList,"total_activity_supplementary_unit"));
                    report.setProductTaxCode(getStrValue(info,titleList,"product_tax_code"));
                    report.setDepatureCity(getStrValue(info,titleList,"departure_city"));
                    report.setDepartureCountry(getStrValue(info,titleList,"departure_country"));
                    report.setDeparturePostCode(getStrValue(info,titleList,"departure_post_code"));
                    report.setArrivalCity(getStrValue(info,titleList,"arrival_city"));
                    report.setArrivalCountry(getStrValue(info,titleList,"arrival_country"));
                    report.setArrivalPostCode(getStrValue(info,titleList,"arrival_post_code"));
                    report.setSaleDepartCountry(getStrValue(info,titleList,"sale_depart_country"));
                    report.setSaleArrivalCountry(getStrValue(info,titleList,"sale_arrival_country"));
                    report.setTransportationMode(getStrValue(info,titleList,"transportation_mode"));
                    report.setDeliveryConditions(getStrValue(info,titleList,"delivery_conditions"));
                    report.setSellerDepartVatNumberCountry(getStrValue(info,titleList,"seller_depart_vat_number_country"));
                    report.setSellerDepartCountryVatNumber(getStrValue(info,titleList,"seller_depart_country_vat_number"));
                    report.setSellerArrivalVatNumberCountry(getStrValue(info,titleList,"seller_arrival_vat_number_country"));
                    report.setSellerArrivalCountryVatNumber(getStrValue(info,titleList,"seller_arrival_country_vat_number"));
                    report.setTransactionSellerVatNumberCountry(getStrValue(info,titleList,"transaction_seller_vat_number_country"));
                    report.setTransactionSellerVatNumber(getStrValue(info,titleList,"transaction_seller_vat_number"));
                    report.setBuyerVatNumberCountry(getStrValue(info,titleList,"buyer_vat_number_country"));
                    report.setBuyerVatNumber(getStrValue(info,titleList,"buyer_vat_number"));
                    report.setVatCalculationImputationCountry(getStrValue(info,titleList,"vat_calculation_imputation_country"));
                    report.setTaxableJurisdiction(getStrValue(info,titleList,"taxable_jurisdiction"));
                    report.setTaxableJurisdictionLevel(getStrValue(info,titleList,"taxable_jurisdiction_level"));
                    report.setVatInvNumber(getStrValue(info,titleList,"vat_inv_number"));
                    report.setVatInvConvertedAmt(getBigDecimalValue(info,titleList,"vat_inv_converted_amt"));
                    report.setVatInvCurrencyCode(getStrValue(info,titleList,"vat_inv_currency_code"));
                    report.setVatInvExchangeRate(getBigDecimalValue(info,titleList,"vat_inv_exchange_rate"));
                    if(StrUtil.isNotBlank(getStrValue(info,titleList,"vat_inv_exchange_rate_date"))){
                        report.setVatInvExchangeRateDate(sdf.parse(getStrValue(info,titleList,"vat_inv_exchange_rate_date")));
                    }
                    report.setExportOutsideEu(getStrValue(info,titleList,"export_outside_eu"));
                    report.setInvoiceUrl(getStrValue(info,titleList,"invoice_url"));
                    report.setBuyerName(getStrValue(info,titleList,"buyer_name"));
                    report.setArrivalAddress(getStrValue(info,titleList,"arrival_address"));
                    report.setSupplierName(getStrValue(info,titleList,"supplier_name"));
                    report.setSupplierVatNumber(getStrValue(info,titleList,"supplier_vat_number"));
                    report.setTaxReportingScheme(getStrValue(info,titleList,"tax_reporting_scheme"));
                    report.setTaxCollectionResponsibility(getStrValue(info,titleList,"tax_collection_responsibility"));
                    report.setAmazonAuthid(amazonAuthority.getId());
                    LambdaQueryWrapper<AmzVatTransaction> query=new LambdaQueryWrapper<AmzVatTransaction>();
                    query.eq(AmzVatTransaction::getTransactionEventId,report.getTransactionEventId());
                    query.eq(AmzVatTransaction::getActivityTransactionId,report.getActivityTransactionId());
                    query.eq(AmzVatTransaction::getSellerSku,report.getSellerSku());
                    AmzVatTransaction old = amzVatTransactionMapper.selectOne(query);
                    if(old!=null){
                        report.setId(old.getId());
                        amzVatTransactionMapper.updateById(report);
                    }else{
                        amzVatTransactionMapper.insert(report);
                    }
                }
                lineNumber++;
            }
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            mlog+=e.getMessage();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            mlog+=e.getMessage();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            mlog+=e.getMessage();
        } finally {
            if(br!=null) {
                try {
                    br.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    mlog+=e.getMessage();
                }
            }
        }
        return mlog;
    }

    @Override
    public String myReportType() {
        return ReportType.FBAVatTransactionReport;
    }

}