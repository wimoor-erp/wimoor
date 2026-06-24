package com.wimoor.finance.closing.service.strategy.impl;

import cn.hutool.core.util.StrUtil;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;
import com.wimoor.finance.api.AmazonClientOneFeignManager;
import com.wimoor.finance.closing.domain.FinClosingTemplate;
import com.wimoor.finance.closing.domain.FinClosingTemplateAmazon;
import com.wimoor.finance.closing.domain.FinClosingTemplateVouchers;
import com.wimoor.finance.closing.service.IFinClosingTemplateAmazonService;
import com.wimoor.finance.closing.service.IFinClosingTemplateService;
import com.wimoor.finance.closing.service.IFinClosingTemplateVouchersService;
import com.wimoor.finance.closing.service.strategy.IFinClosingTemplateStrategy;
import com.wimoor.finance.setting.domain.FinAccountingPeriods;
import com.wimoor.finance.setting.domain.FinAccountingSubjects;
import com.wimoor.finance.setting.domain.FinCurrency;
import com.wimoor.finance.setting.service.IFinAccountingPeriodsService;
import com.wimoor.finance.setting.service.IFinAccountingSubjectsService;
import com.wimoor.finance.setting.service.IFinCurrencyService;
import com.wimoor.finance.voucher.domain.FinVoucherEntries;
import com.wimoor.finance.voucher.domain.FinVouchers;
import com.wimoor.finance.voucher.service.IFinVouchersService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AmzPaymentTemplateStrategy implements IFinClosingTemplateStrategy {

    private static final String FTYPE = "amzpayment";

    @Resource
    IFinClosingTemplateService finClosingTemplateService;
    @Resource
    IFinClosingTemplateAmazonService finClosingTemplateAmazonService;
    @Resource
    AmazonClientOneFeignManager amazonClientOneFeignManager;
    @Resource
    IFinVouchersService iFinVouchersService;
    @Resource
    IFinCurrencyService iFinCurrencyService;
    @Resource
    IFinClosingTemplateVouchersService finClosingTemplateVouchersService;
    @Resource
    IFinAccountingPeriodsService iFinAccountingPeriodsService;
    @Resource
    IFinAccountingSubjectsService finAccountingSubjectsService;
    @Override
    public String getFtype() {
        return FTYPE;
    }


     public void saveTemplateItem(FinClosingTemplate template,String subjectCode,String summary,String amountField,int direction){
         FinAccountingSubjects query=new FinAccountingSubjects();
         query.setSubjectCode(subjectCode);
         query.setGroupid(template.getGroupid());
         List<FinAccountingSubjects> list = finAccountingSubjectsService.selectFinAccountingSubjectsList(query);
         FinAccountingSubjects subjectObject=list!=null&& !list.isEmpty() ?list.get(0):null;
         if(subjectObject!=null){
             FinClosingTemplateAmazon item=new FinClosingTemplateAmazon();
             item.setClosingTemplateId(template.getId());
             item.setModifyBy(template.getModifyBy());
             item.setCreateBy(template.getCreateBy());
             item.setCreateTime(template.getCreateTime());
             item.setUpdatedTime(template.getUpdatedTime());
             item.setAmountField(amountField);
             item.setSummary(summary);
             item.setDirection(direction);
             item.setSubjectId(subjectObject.getSubjectId().toString());
             finClosingTemplateAmazonService.insertFinClosingTemplateAmazon(item);
         }else{
             throw new BizException(summary+amountField+"对应科目"+subjectCode+"不存在");
         }
     }
    public void initTemplateItemSales(FinClosingTemplate template){
        String summary="亚马逊平台收入-FBA/FBM 商品销售";
        String amountField="FBA product sales+Seller fulfilled product sales";
        String subjectCode=null;
        switch (template.getCountry().toUpperCase()) {
            case "CA":
                subjectCode = "50010101";
                break;
            case "US":
                subjectCode = "50010102";
                break;
            case "FR":
            case "NL":
            case "DE":
            case "ES":
            case "IE":
            case "BE":
            case "IT":
            case "EG":
                subjectCode = "50010103";
                break;
            case "UK":
                subjectCode = "50010104";
                break;
            case "PL":
                subjectCode = "50010105";
                break;
            case "SE":
                subjectCode = "50010106";
                break;
            case "TR":
                subjectCode = "50010107";
                break;
            case "MX":
                subjectCode = "50010108";
                break;

        }
        saveTemplateItem( template, subjectCode, summary, amountField,2);

    }
    public void initTemplateItemSalesRefunds(FinClosingTemplate template){
        String summary="亚马逊平台收入-FBA/FBM 商品销售,退款";
        String amountField="FBA product sale refunds+Seller_fulfilled product sale refunds";
        String subjectCode=null;
        switch (template.getCountry().toUpperCase()) {
            case "CA":
                subjectCode = "50010101";
                break;
            case "US":
                subjectCode = "50010102";
                break;
            case "FR":
            case "NL":
            case "DE":
            case "ES":
            case "IE":
            case "BE":
            case "IT":
            case "EG":
                subjectCode = "50010103";
                break;
            case "UK":
                subjectCode = "50010104";
                break;
            case "PL":
                subjectCode = "50010105";
                break;
            case "SE":
                subjectCode = "50010106";
                break;
            case "TR":
                subjectCode = "50010107";
                break;
            case "MX":
                subjectCode = "50010108";
                break;

        }
        saveTemplateItem( template, subjectCode, summary, amountField,2);

    }
    public void initTemplateItemDeliveryCredit(FinClosingTemplate template){
        String summary="亚马逊平台收入-运费收入";
        String amountField="Postage credits";
        String subjectCode=null;
        switch (template.getCountry().toUpperCase()) {
            case "CA":
                subjectCode = "50010501";
                break;
            case "US":
                subjectCode = "50010502";
                break;
            case "FR":
            case "NL":
            case "DE":
            case "ES":
            case "IE":
            case "BE":
            case "IT":
            case "EG":
                subjectCode = "50010503";
                break;
            case "UK":
                subjectCode = "50010504";
                break;
            case "PL":
                subjectCode = "50010505";
                break;
            case "SE":
                subjectCode = "50010506";
                break;
            case "TR":
                subjectCode = "50010507";
                break;
            case "MX":
                subjectCode = "50010508";
                break;
        }
        saveTemplateItem( template, subjectCode, summary, amountField,2);

    }
    public void initTemplateItemDeliveryCreditRefunds(FinClosingTemplate template){
        String summary="亚马逊平台收入-运费收入,退款";
        String amountField="Delivery credit refunds";
        String subjectCode=null;
        switch (template.getCountry().toUpperCase()) {
            case "CA":
                subjectCode = "50010501";
                break;
            case "US":
                subjectCode = "50010502";
                break;
            case "FR":
            case "NL":
            case "DE":
            case "ES":
            case "IE":
            case "BE":
            case "IT":
            case "EG":
                subjectCode = "50010503";
                break;
            case "UK":
                subjectCode = "50010504";
                break;
            case "PL":
                subjectCode = "50010505";
                break;
            case "SE":
                subjectCode = "50010506";
                break;
            case "TR":
                subjectCode = "50010507";
                break;
            case "MX":
                subjectCode = "50010508";
                break;
        }
        saveTemplateItem( template, subjectCode, summary, amountField,2);

    }
    public void initTemplateItemGiftWrap(FinClosingTemplate template){
        String summary="亚马逊平台收入-礼品包装收入";
        String amountField="Gift wrap credits";
        String subjectCode=null;
        switch (template.getCountry().toUpperCase()) {
            case "CA":
                subjectCode = "50010401";
                break;
            case "US":
                subjectCode = "50010402";
                break;
            case "FR":
            case "NL":
            case "DE":
            case "ES":
            case "IE":
            case "BE":
            case "IT":
            case "EG":
                subjectCode = "50010403";
                break;
            case "UK":
                subjectCode = "50010404";
                break;
            case "PL":
                subjectCode = "50010405";
                break;
            case "SE":
                subjectCode = "50010406";
                break;
            case "TR":
                subjectCode = "50010407";
                break;
            case "MX":
                subjectCode = "50010408";
                break;
        }
        saveTemplateItem( template, subjectCode, summary, amountField,2);

    }
    public void initTemplateItemGiftWrapRefunds(FinClosingTemplate template){
        String summary="亚马逊平台收入-礼品包装收入,退款";
        String amountField="Gift wrap credits refunds";
        String subjectCode=null;
        switch (template.getCountry().toUpperCase()) {
            case "CA":
                subjectCode = "50010401";
                break;
            case "US":
                subjectCode = "50010402";
                break;
            case "FR":
            case "NL":
            case "DE":
            case "ES":
            case "IE":
            case "BE":
            case "IT":
            case "EG":
                subjectCode = "50010403";
                break;
            case "UK":
                subjectCode = "50010404";
                break;
            case "PL":
                subjectCode = "50010405";
                break;
            case "SE":
                subjectCode = "50010406";
                break;
            case "TR":
                subjectCode = "50010407";
                break;
            case "MX":
                subjectCode = "50010408";
                break;
        }
        saveTemplateItem( template, subjectCode, summary, amountField,2);

    }
    public void initTemplateItemInventoryCredit(FinClosingTemplate template){
        String summary="亚马逊平台收入-礼品包装收入,退款";
        String amountField="FBA inventory credit";
        String subjectCode=null;
        switch (template.getCountry().toUpperCase()) {
            case "CA":
                subjectCode = "50010201";
                break;
            case "US":
                subjectCode = "50010202";
                break;
            case "FR":
            case "NL":
            case "DE":
            case "ES":
            case "IE":
            case "BE":
            case "IT":
            case "EG":
                subjectCode = "50010203";
                break;
            case "UK":
                subjectCode = "50010204";
                break;
            case "PL":
                subjectCode = "50010205";
                break;
            case "SE":
                subjectCode = "50010206";
                break;
            case "TR":
                subjectCode = "50010207";
                break;
            case "MX":
                subjectCode = "50010208";
                break;
        }
        saveTemplateItem( template, subjectCode, summary, amountField,2);
    }
    public void initTemplateItemLiquidationsProceeds(FinClosingTemplate template){
        String summary="亚马逊平台收入-FBA 清仓收益";
        String amountField="FBA liquidation proceeds";
        String subjectCode=null;
        switch (template.getCountry().toUpperCase()) {
            case "CA":
                subjectCode = "50010301";
                break;
            case "US":
                subjectCode = "50010302";
                break;
            case "FR":
            case "NL":
            case "DE":
            case "ES":
            case "IE":
            case "BE":
            case "IT":
            case "EG":
                subjectCode = "50010303";
                break;
            case "UK":
                subjectCode = "50010304";
                break;
            case "PL":
                subjectCode = "50010305";
                break;
            case "SE":
                subjectCode = "50010306";
                break;
            case "TR":
                subjectCode = "50010307";
                break;
            case "MX":
                subjectCode = "50010308";
                break;
        }
        saveTemplateItem( template, subjectCode, summary, amountField,2);
    }
    public void initTemplateItemLiquidationsAdjustments(FinClosingTemplate template){
        String summary="亚马逊平台收入-FBA 清仓收益,调整";
        String amountField="FBA Liquidations proceeds adjustments";
        String subjectCode=null;
        switch (template.getCountry().toUpperCase()) {
            case "CA":
                subjectCode = "50010301";
                break;
            case "US":
                subjectCode = "50010302";
                break;
            case "FR":
            case "NL":
            case "DE":
            case "ES":
            case "IE":
            case "BE":
            case "IT":
            case "EG":
                subjectCode = "50010303";
                break;
            case "UK":
                subjectCode = "50010304";
                break;
            case "PL":
                subjectCode = "50010305";
                break;
            case "SE":
                subjectCode = "50010306";
                break;
            case "TR":
                subjectCode = "50010307";
                break;
            case "MX":
                subjectCode = "50010308";
                break;
        }
        saveTemplateItem( template, subjectCode, summary, amountField,2);
    }
    public void initTemplateItemPromotional(FinClosingTemplate template){
        String summary="亚马逊平台费用-促销返利";
        String amountField="Promotional rebates*-1";
        String subjectCode=null;
        switch (template.getCountry().toUpperCase()) {
            case "CA":
                subjectCode = "54010401";
                break;
            case "US":
                subjectCode = "54010402";
                break;
            case "FR":
            case "NL":
            case "DE":
            case "ES":
            case "IE":
            case "BE":
            case "IT":
            case "EG":
                subjectCode = "54010403";
                break;
            case "UK":
                subjectCode = "54010404";
                break;
            case "PL":
                subjectCode = "54010405";
                break;
            case "SE":
                subjectCode = "54010406";
                break;
            case "TR":
                subjectCode = "54010407";
                break;
            case "MX":
                subjectCode = "54010408";
                break;
        }
        saveTemplateItem( template, subjectCode, summary, amountField,1);
    }
    public void initTemplateItemPromotionalRefunds(FinClosingTemplate template){
        String summary="亚马逊平台费用-促销返利,退款";
        String amountField="Promotional rebate refunds*-1";
        String subjectCode=null;
        switch (template.getCountry().toUpperCase()) {
            case "CA":
                subjectCode = "54010401";
                break;
            case "US":
                subjectCode = "54010402";
                break;
            case "FR":
            case "NL":
            case "DE":
            case "ES":
            case "IE":
            case "BE":
            case "IT":
            case "EG":
                subjectCode = "54010403";
                break;
            case "UK":
                subjectCode = "54010404";
                break;
            case "PL":
                subjectCode = "54010405";
                break;
            case "SE":
                subjectCode = "54010406";
                break;
            case "TR":
                subjectCode = "54010407";
                break;
            case "MX":
                subjectCode = "54010408";
                break;
        }
        saveTemplateItem( template, subjectCode, summary, amountField,1);
    }
    public void initTemplateItemSellingFeeRefund(FinClosingTemplate template){
        String summary="亚马逊平台费用-FBA/FBM 销售佣金,退款";
        String amountField="Selling fee refunds*-1";
        String subjectCode=null;
        switch (template.getCountry().toUpperCase()) {
            case "CA":
                subjectCode = "54010501";
                break;
            case "US":
                subjectCode = "54010502";
                break;
            case "FR":
            case "NL":
            case "DE":
            case "ES":
            case "IE":
            case "BE":
            case "IT":
            case "EG":
                subjectCode = "54010503";
                break;
            case "UK":
                subjectCode = "54010504";
                break;
            case "PL":
                subjectCode = "54010505";
                break;
            case "SE":
                subjectCode = "54010506";
                break;
            case "TR":
                subjectCode = "54010507";
                break;
            case "MX":
                subjectCode = "54010508";
                break;
        }
        saveTemplateItem( template, subjectCode, summary, amountField,1);
    }
    public void initTemplateItemSellingFee(FinClosingTemplate template){
        String summary="亚马逊平台费用-FBA/FBM 销售佣金";
        String amountField="(FBA selling fees+Seller_fulfilled selling fees)*(-1)";
        String subjectCode=null;
        switch (template.getCountry().toUpperCase()) {
            case "CA":
                subjectCode = "54010501";
                break;
            case "US":
                subjectCode = "54010502";
                break;
            case "FR":
            case "NL":
            case "DE":
            case "ES":
            case "IE":
            case "BE":
            case "IT":
            case "EG":
                subjectCode = "54010503";
                break;
            case "UK":
                subjectCode = "54010504";
                break;
            case "PL":
                subjectCode = "54010505";
                break;
            case "SE":
                subjectCode = "54010506";
                break;
            case "TR":
                subjectCode = "54010507";
                break;
            case "MX":
                subjectCode = "54010508";
                break;
        }
        saveTemplateItem( template, subjectCode, summary, amountField,1);
    }
    public void initTemplateItemFBATransactionFeeRefunds(FinClosingTemplate template){
        String summary="亚马逊平台费用-FBA 交易费（配送费）,退款";
        String amountField="FBA transaction fee refunds*-1";
        String subjectCode=null;
        switch (template.getCountry().toUpperCase()) {
            case "CA":
                subjectCode = "56012101";
                break;
            case "US":
                subjectCode = "56012102";
                break;
            case "FR":
            case "NL":
            case "DE":
            case "ES":
            case "IE":
            case "BE":
            case "IT":
            case "EG":
                subjectCode = "56012103";
                break;
            case "UK":
                subjectCode = "56012104";
                break;
            case "PL":
                subjectCode = "56012105";
                break;
            case "SE":
                subjectCode = "56012106";
                break;
            case "TR":
                subjectCode = "56012107";
                break;
            case "MX":
                subjectCode = "56012108";
                break;
        }
        saveTemplateItem( template, subjectCode, summary, amountField,1);
    }
    public void initTemplateItemFBATransactionFee(FinClosingTemplate template){
        String summary="亚马逊平台费用-FBA 交易费（配送费）";
        String amountField="FBA transaction fees*-1";
        String subjectCode=null;
        switch (template.getCountry().toUpperCase()) {
            case "CA":
                subjectCode = "56012101";
                break;
            case "US":
                subjectCode = "56012102";
                break;
            case "FR":
            case "NL":
            case "DE":
            case "ES":
            case "IE":
            case "BE":
            case "IT":
            case "EG":
                subjectCode = "56012103";
                break;
            case "UK":
                subjectCode = "56012104";
                break;
            case "PL":
                subjectCode = "56012105";
                break;
            case "SE":
                subjectCode = "56012106";
                break;
            case "TR":
                subjectCode = "56012107";
                break;
            case "MX":
                subjectCode = "56012108";
                break;
        }
        saveTemplateItem( template, subjectCode, summary, amountField,1);
    }
    public void initTemplateItemInboundServicesFees(FinClosingTemplate template){
        String summary="亚马逊平台费用-FBA 入库及库存服务费";
        String amountField="FBA inventory and inbound services fees*-1";
        String subjectCode=null;
        switch (template.getCountry().toUpperCase()) {
            case "CA":
                subjectCode = "56012201";
                break;
            case "US":
                subjectCode = "56012202";
                break;
            case "FR":
            case "NL":
            case "DE":
            case "ES":
            case "IE":
            case "BE":
            case "IT":
            case "EG":
                subjectCode = "56012203";
                break;
            case "UK":
                subjectCode = "56012204";
                break;
            case "PL":
                subjectCode = "56012205";
                break;
            case "SE":
                subjectCode = "56012206";
                break;
            case "TR":
                subjectCode = "56012207";
                break;
            case "MX":
                subjectCode = "56012208";
                break;
        }
        saveTemplateItem( template, subjectCode, summary, amountField,1);
    }
    public void initTemplateItemRefundAdministrationFees(FinClosingTemplate template){
        String summary="亚马逊平台费用-退款管理费";
        String amountField="Refund administration fees*-1";
        String subjectCode=null;
        switch (template.getCountry().toUpperCase()) {
            case "CA":
                subjectCode = "56012301";
                break;
            case "US":
                subjectCode = "56012302";
                break;
            case "FR":
            case "NL":
            case "DE":
            case "ES":
            case "IE":
            case "BE":
            case "IT":
            case "EG":
                subjectCode = "56012303";
                break;
            case "UK":
                subjectCode = "56012304";
                break;
            case "PL":
                subjectCode = "56012305";
                break;
            case "SE":
                subjectCode = "56012306";
                break;
            case "TR":
                subjectCode = "56012307";
                break;
            case "MX":
                subjectCode = "56012308";
                break;
        }
        saveTemplateItem( template, subjectCode, summary, amountField,1);
    }
    public void initTemplateItemCostOfAdvertising(FinClosingTemplate template){
        String summary="亚马逊平台费用-广告费";
        String amountField="Cost of Advertising*-1";
        String subjectCode=null;
        switch (template.getCountry().toUpperCase()) {
            case "CA":
                subjectCode = "56012401";
                break;
            case "US":
                subjectCode = "56012402";
                break;
            case "FR":
            case "NL":
            case "DE":
            case "ES":
            case "IE":
            case "BE":
            case "IT":
            case "EG":
                subjectCode = "56012403";
                break;
            case "UK":
                subjectCode = "56012404";
                break;
            case "PL":
                subjectCode = "56012405";
                break;
            case "SE":
                subjectCode = "56012406";
                break;
            case "TR":
                subjectCode = "56012407";
                break;
            case "MX":
                subjectCode = "56012408";
                break;
        }
        saveTemplateItem( template, subjectCode, summary, amountField,1);
    }
    public void initTemplateItemServiceFees(FinClosingTemplate template){
        String summary="亚马逊平台费用-服务费";
        String amountField="Service fees*-1";
        String subjectCode=null;
        switch (template.getCountry().toUpperCase()) {
            case "CA":
                subjectCode = "56012501";
                break;
            case "US":
                subjectCode = "56012502";
                break;
            case "FR":
            case "NL":
            case "DE":
            case "ES":
            case "IE":
            case "BE":
            case "IT":
            case "EG":
                subjectCode = "56012503";
                break;
            case "UK":
                subjectCode = "56012504";
                break;
            case "PL":
                subjectCode = "56012505";
                break;
            case "SE":
                subjectCode = "56012506";
                break;
            case "TR":
                subjectCode = "56012507";
                break;
            case "MX":
                subjectCode = "56012508";
                break;
        }
        saveTemplateItem( template, subjectCode, summary, amountField,1);
    }
    public void initTemplateItemAdjustments(FinClosingTemplate template){
        String summary="亚马逊平台费用-平台调整";
        String amountField="Adjustments*-1";
        String subjectCode=null;
        switch (template.getCountry().toUpperCase()) {
            case "CA":
                subjectCode = "56012601";
                break;
            case "US":
                subjectCode = "56012602";
                break;
            case "FR":
            case "NL":
            case "DE":
            case "ES":
            case "IE":
            case "BE":
            case "IT":
            case "EG":
                subjectCode = "56012603";
                break;
            case "UK":
                subjectCode = "56012604";
                break;
            case "PL":
                subjectCode = "56012605";
                break;
            case "SE":
                subjectCode = "56012606";
                break;
            case "TR":
                subjectCode = "56012607";
                break;
            case "MX":
                subjectCode = "56012608";
                break;
        }
        saveTemplateItem( template, subjectCode, summary, amountField,1);
    }
    public void initTemplateItemLiquidations(FinClosingTemplate template){
        String summary="亚马逊平台费用-FBA 清仓费";
        String amountField="Liquidations fees*-1";
        String subjectCode=null;
        switch (template.getCountry().toUpperCase()) {
            case "CA":
                subjectCode = "56012701";
                break;
            case "US":
                subjectCode = "56012702";
                break;
            case "FR":
            case "NL":
            case "DE":
            case "ES":
            case "IE":
            case "BE":
            case "IT":
            case "EG":
                subjectCode = "56012703";
                break;
            case "UK":
                subjectCode = "56012704";
                break;
            case "PL":
                subjectCode = "56012705";
                break;
            case "SE":
                subjectCode = "56012706";
                break;
            case "TR":
                subjectCode = "56012707";
                break;
            case "MX":
                subjectCode = "56012708";
                break;
        }
        saveTemplateItem( template, subjectCode, summary, amountField,1);
    }
    public void initTemplateItemOthers(FinClosingTemplate template){
        String summary="亚马逊平台费用-其它服务费";
        String amountField="(Others+Other transaction fee refunds+Other transaction fees+Refund for Advertiser)*-1";
        String subjectCode=null;
        switch (template.getCountry().toUpperCase()) {
            case "CA":
                subjectCode = "56012801";
                break;
            case "US":
                subjectCode = "56012802";
                break;
            case "FR":
            case "NL":
            case "DE":
            case "ES":
            case "IE":
            case "BE":
            case "IT":
            case "EG":
                subjectCode = "56012803";
                break;
            case "UK":
                subjectCode = "56012804";
                break;
            case "PL":
                subjectCode = "56012805";
                break;
            case "SE":
                subjectCode = "56012806";
                break;
            case "TR":
                subjectCode = "56012807";
                break;
            case "MX":
                subjectCode = "56012808";
                break;
        }
        saveTemplateItem( template, subjectCode, summary, amountField,1);
    }

    public void initTemplateItemTax(FinClosingTemplate template){
        String summary="应交税费";
        String amountField="Product, delivery and gift wrap taxes collected";
        String subjectCode=null;
        switch (template.getCountry().toUpperCase()) {
            case "CA":
                subjectCode = "22212401";
                break;
            case "US":
                subjectCode = "22212402";
                break;
            case "FR":
            case "NL":
            case "DE":
            case "ES":
            case "IE":
            case "BE":
            case "IT":
            case "EG":
                subjectCode = "22212403";
                break;
            case "UK":
                subjectCode = "22212404";
                break;
            case "PL":
                subjectCode = "22212405";
                break;
            case "SE":
                subjectCode = "22212406";
                break;
            case "TR":
                subjectCode = "22212407";
                break;
            case "MX":
                subjectCode = "22212408";
                break;
        }
        saveTemplateItem( template, subjectCode, summary, amountField,2);
    }

    public void initTemplateItemTaxRefund(FinClosingTemplate template){
        String summary="应交税费-退款";
        String amountField="(Product, delivery and gift wrap taxes refunded+Amazon obligated tax withheld)*-1";
        String subjectCode=null;
        switch (template.getCountry().toUpperCase()) {
            case "CA":
                subjectCode = "22212401";
                break;
            case "US":
                subjectCode = "22212402";
                break;
            case "FR":
            case "NL":
            case "DE":
            case "ES":
            case "IE":
            case "BE":
            case "IT":
            case "EG":
                subjectCode = "22212403";
                break;
            case "UK":
                subjectCode = "22212404";
                break;
            case "PL":
                subjectCode = "22212405";
                break;
            case "SE":
                subjectCode = "22212406";
                break;
            case "TR":
                subjectCode = "22212407";
                break;
            case "MX":
                subjectCode = "22212408";
                break;
        }
        saveTemplateItem( template, subjectCode, summary, amountField,1);
    }
    public void initTemplateItemBank(FinClosingTemplate template){
        String summary="亚马逊平台应收账款,出账";
        String amountField="(Transfers to bank account+Failed transfers to bank account)*-1";
        String subjectCode=null;
        switch (template.getCountry().toUpperCase()) {
            case "CA":
                subjectCode = "11220101";
                break;
            case "US":
                subjectCode = "11220102";
                break;
            case "FR":
            case "NL":
            case "DE":
            case "ES":
            case "IE":
            case "BE":
            case "IT":
            case "EG":
                subjectCode = "11220103";
                break;
            case "UK":
                subjectCode = "11220104";
                break;
            case "PL":
                subjectCode = "11220105";
                break;
            case "SE":
                subjectCode = "11220106";
                break;
            case "TR":
                subjectCode = "11220107";
                break;
            case "MX":
                subjectCode = "11220108";
                break;
        }
        saveTemplateItem( template, subjectCode, summary, amountField,1);
    }
    public void initTemplateItemBank2(FinClosingTemplate template){
        String summary="亚马逊平台应收账款,出账";
        String amountField="(Transfers to bank account+Failed transfers to bank account)*-1";
        String subjectCode=null;
        switch (template.getCountry().toUpperCase()) {
            case "CA":
                subjectCode = "12210101";
                break;
            case "US":
                subjectCode = "12210102";
                break;
            case "FR":
            case "NL":
            case "DE":
            case "ES":
            case "IE":
            case "BE":
            case "IT":
            case "EG":
                subjectCode = "12210103";
                break;
            case "UK":
                subjectCode = "12210104";
                break;
            case "PL":
                subjectCode = "12210105";
                break;
            case "SE":
                subjectCode = "12210106";
                break;
            case "TR":
                subjectCode = "12210107";
                break;
            case "MX":
                subjectCode = "12210108";
                break;
        }
        saveTemplateItem( template, subjectCode, summary, amountField,2);
    }
    public void initTemplateItemTotal(FinClosingTemplate template){
        String summary="亚马逊平台应收账款";
        String amountField="Total Income Expenses Tax";
        String subjectCode=null;
        switch (template.getCountry().toUpperCase()) {
            case "CA":
                subjectCode = "11220101";
                break;
            case "US":
                subjectCode = "11220102";
                break;
            case "FR":
            case "NL":
            case "DE":
            case "ES":
            case "IE":
            case "BE":
            case "IT":
            case "EG":
                subjectCode = "11220103";
                break;
            case "UK":
                subjectCode = "11220104";
                break;
            case "PL":
                subjectCode = "11220105";
                break;
            case "SE":
                subjectCode = "11220106";
                break;
            case "TR":
                subjectCode = "11220107";
                break;
            case "MX":
                subjectCode = "11220108";
                break;
        }
        saveTemplateItem( template, subjectCode, summary, amountField,1);
    }

    public void initTemplateItem(FinClosingTemplate template){
        FinClosingTemplateAmazon query=new FinClosingTemplateAmazon();
        query.setClosingTemplateId(template.getId());
        List<FinClosingTemplateAmazon> list = finClosingTemplateAmazonService.selectFinClosingTemplateAmazonList(query);
        if(list!=null&& !list.isEmpty()){
            List<String> ids = list.stream().map(FinClosingTemplateAmazon::getId).collect(Collectors.toList());
            finClosingTemplateAmazonService.deleteFinClosingTemplateAmazonByIds(ids);
        }
        this.initTemplateItemSales(template);
        this.initTemplateItemSalesRefunds(template);
        this.initTemplateItemDeliveryCreditRefunds(template);
        this.initTemplateItemDeliveryCredit(template);
        this.initTemplateItemGiftWrap(template);
        this.initTemplateItemGiftWrapRefunds(template);
        this.initTemplateItemInventoryCredit(template);
        this.initTemplateItemLiquidations(template);
        this.initTemplateItemLiquidationsProceeds(template);
        this.initTemplateItemLiquidationsAdjustments(template);
        this.initTemplateItemPromotional(template);
        this.initTemplateItemPromotionalRefunds(template);
        this.initTemplateItemSellingFeeRefund(template);
        this.initTemplateItemSellingFee(template);
        this.initTemplateItemFBATransactionFee(template);
        this.initTemplateItemFBATransactionFeeRefunds(template);
        this.initTemplateItemInboundServicesFees(template);
        this.initTemplateItemRefundAdministrationFees(template);
        this.initTemplateItemCostOfAdvertising(template);
        this.initTemplateItemServiceFees(template);
        this.initTemplateItemAdjustments(template);
        this.initTemplateItemTax(template);
        this.initTemplateItemTaxRefund(template);
        this.initTemplateItemBank2(template);
        this.initTemplateItemBank(template);
        this.initTemplateItemOthers(template);
        this.initTemplateItemTotal(template);
    }
    @Override
    public void generateVoucher(UserInfo userInfo,String templateId,String periodCode) {
        FinClosingTemplate template = finClosingTemplateService.selectFinClosingTemplateById(templateId);

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("groupid", template.getGroupid());
        param.put("country", template.getCountry());
        FinAccountingPeriods  period=null;
        if(StrUtil.isNotBlank(periodCode)){
            period= iFinAccountingPeriodsService.selectByPeriod(template.getGroupid(),periodCode);
            if(period==null){
                //请trae 帮我把periodCode转换成日期格式
                String periodDate=periodCode+"01";
                SimpleDateFormat FMT_YMD = new SimpleDateFormat("yyyyMMdd");
                try {
                    period= iFinAccountingPeriodsService.selectFinAccountingPeriodsByDate(template.getGroupid(),FMT_YMD.parse(periodDate));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        }else{
            period= iFinAccountingPeriodsService.getCurrentPeriod(template.getGroupid());
        }
        if(period==null){
            throw new BizException("未找到指定的会计期间");
        }
        if(period.getPeriodStatus()==3){
            throw new BizException("会计期间状态错误");
        }
        Date currentDate =   period.getEndDate();
        LocalDate localDate = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate firstDayMonth = localDate.withDayOfMonth(1);
        LocalDate lastDayMonth = localDate.withDayOfMonth(localDate.lengthOfMonth());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        param.put("fromDate", firstDayMonth.format(formatter));
        param.put("endDate", lastDayMonth.format(formatter) + " 23:59:59");
        Map<String, Object> map = amazonClientOneFeignManager.getMonthReport(param);
        Map<String,Object> env=new HashMap<String,Object>();
        Map<String,Object> env_rmb=new HashMap<String,Object>();
        for(Map.Entry<String,Object> entry:map.entrySet()){
            if(entry.getKey().contains("_rmb")){
                String key=entry.getKey().replace("_rmb","");
                env_rmb.put(key, entry.getValue());
            }else{
                env.put(entry.getKey(), entry.getValue());
            }
        }
        FinClosingTemplateAmazon queryItem = new FinClosingTemplateAmazon();
        queryItem.setClosingTemplateId(templateId);
        List<FinClosingTemplateAmazon> items = finClosingTemplateAmazonService.selectFinClosingTemplateAmazonList(queryItem);
        FinVouchers finVouchers = new FinVouchers();
        finVouchers.setVoucherType(template.getVoucherType());
        finVouchers.setGroupid(template.getGroupid());
        finVouchers.setVoucherStatus(3);
        finVouchers.setDataSource(3); // 结账模版生成
        finVouchers.setVoucherDate(GeneralUtil.getDatez(lastDayMonth.format(formatter)));
        finVouchers.setVoucherNo(iFinVouchersService.selectNextVoucherNo(finVouchers));
        List<FinVoucherEntries> entryList = new ArrayList<FinVoucherEntries>();
        String currency = "";
        if (map.get("currency") != null) {
            currency = map.get("currency").toString();
        }
        FinCurrency finCurrency = this.iFinCurrencyService.selectFinCurrencyByCode(currency, template.getGroupid());
        BigDecimal debitTotal=BigDecimal.ZERO;
        BigDecimal creditTotal=BigDecimal.ZERO;
        BigDecimal originalDebitTotal=BigDecimal.ZERO;
        BigDecimal originalCreditTotal=BigDecimal.ZERO;
        int i=1;
        FinVoucherEntries totalIncomeExpensesTax=null;
        StringBuilder datalog = new StringBuilder();
        for (FinClosingTemplateAmazon item : items) {
            FinVoucherEntries entry = new FinVoucherEntries();
            entry.setSummary(item.getSummary());
            entry.setSubjectId(item.getSubjectId());
            //entry.setExchangeRate(finCurrency.getRate());
            entry.setCurrency(currency);
            entry.setCreateBy(userInfo.getUserName());
            entry.setUpdateBy(userInfo.getUserName());
            String fields = item.getAmountField();
            // 执行计算
            String convertedExpr = FormulaConverter.convertExpression(item.getAmountField());
            Expression compiledExp = AviatorEvaluator.compile(convertedExpr);
            Map<String, Object> convertedEnv = FormulaConverter.convertEnv(env);
            BigDecimal amount = new BigDecimal(compiledExp.execute(convertedEnv).toString());
            Map<String, Object> convertedEnvRMB = FormulaConverter.convertEnv(env_rmb);
            BigDecimal amount_rmb = new BigDecimal(compiledExp.execute(convertedEnvRMB).toString());
            if (amount.floatValue() < 0.000001 && amount.floatValue() > -0.0000001) {
                continue;
            }
            entry.setOriginalAmount(amount.setScale(2, RoundingMode.HALF_UP));
            entry.setEntryNo(Long.valueOf(i));
            i++;
            if (item.getDirection() == 1) {
                if (entry.getOriginalAmount() != null) {
                    entry.setDebitAmount(amount_rmb.setScale(2, RoundingMode.DOWN));
                    entry.setExchangeRate(amount_rmb.divide(amount, 2, RoundingMode.HALF_UP));
                }
            } else {
                if (entry.getOriginalAmount() != null) {
                    entry.setCreditAmount(amount_rmb.setScale(2, RoundingMode.HALF_UP));
                    entry.setExchangeRate(amount_rmb.divide(amount, 2, RoundingMode.HALF_UP));
                }

            }
            if (entry.getOriginalAmount() != null) {
                entryList.add(entry);
            }
            List<String> innerVars = compiledExp.getVariableNames();
            datalog.append(entry.getEntryNo()).append("-[Expression:").append(convertedExpr).append("]");
            for (String var : innerVars){
                datalog.append("(").append(var).append("=").append(convertedEnv.get(var)).append(")");
            }
            datalog.append(";");

            if(entry.getDebitAmount()!=null){
                debitTotal=debitTotal.add(entry.getDebitAmount());
                originalDebitTotal=originalDebitTotal.add(entry.getOriginalAmount());
            }
            if(entry.getCreditAmount()!=null){
                creditTotal=creditTotal.add(entry.getCreditAmount());
                originalCreditTotal=originalCreditTotal.add(entry.getOriginalAmount());
            }
           if(fields.contains("Total Income Expenses Tax")){
                totalIncomeExpensesTax=entry;
           }
        }
        finVouchers.setTotalAmount(debitTotal);
        finVouchers.setEntries(entryList);
        
        // 检查借贷平衡
        if (!originalDebitTotal.equals(originalCreditTotal)) {
            throw new RuntimeException("凭证生成失败：借贷不平衡，借方金额：" + originalDebitTotal + "，贷方金额：" + originalCreditTotal);
        }
        if (!debitTotal.equals(creditTotal)) {
            if(debitTotal.subtract(creditTotal).abs().compareTo(BigDecimal.valueOf(1))>0){
                throw new RuntimeException("凭证生成失败：借贷不平衡，借方金额：" + debitTotal + "，贷方金额：" + creditTotal);
            }else{
                assert totalIncomeExpensesTax != null;
                totalIncomeExpensesTax.setDebitAmount(totalIncomeExpensesTax.getDebitAmount().subtract(debitTotal.subtract(creditTotal)));
                debitTotal=debitTotal.subtract( debitTotal.subtract(creditTotal));
            }
            if(!debitTotal.equals(creditTotal)){
                throw new RuntimeException("凭证生成失败：借贷不平衡，借方金额：" + debitTotal + "，贷方金额：" + creditTotal);
            }
        }
        FinClosingTemplateVouchers queryVouchers = new FinClosingTemplateVouchers();
        queryVouchers.setTemplateId(templateId);
        queryVouchers.setGroupid(template.getGroupid());
        queryVouchers.setVoucherDate(finVouchers.getVoucherDate());
        FinClosingTemplateVouchers existingTemplateVouchers = finClosingTemplateVouchersService.selectByTemplate(queryVouchers);
        // 同步插入或更新 FinClosingTemplateVouchers 数据
        if (existingTemplateVouchers != null) {
            // 更新现有凭证
            FinVouchers oldFinVouchers = iFinVouchersService.selectFinVouchersByVoucherId(Long.valueOf(existingTemplateVouchers.getVourchesId()));
            finVouchers.setVoucherNo(oldFinVouchers.getVoucherNo());
            finVouchers.setUpdateBy(userInfo.getUserName());
            finVouchers.setUpdatedTime(new Date());
            finVouchers.setVoucherId(Long.valueOf(existingTemplateVouchers.getVourchesId()));
            for(FinVoucherEntries entry:finVouchers.getEntries()){
                entry.setVoucherId(Long.valueOf(existingTemplateVouchers.getVourchesId()));
            }
            iFinVouchersService.updateFinVouchers(finVouchers);
            
            // 更新关联记录

            existingTemplateVouchers.setDatalog(datalog.toString());
            existingTemplateVouchers.setUpdateBy(userInfo.getUserName());
            existingTemplateVouchers.setVoucherDate(finVouchers.getVoucherDate());
            existingTemplateVouchers.setUpdatedTime(new Date());
            finClosingTemplateVouchersService.updateFinClosingTemplateVouchers(existingTemplateVouchers);
        } else {
            finVouchers.setCreateBy(userInfo.getUserName());
            finVouchers.setUpdateBy(userInfo.getUserName());
            finVouchers.setCreatedTime(new Date());
            finVouchers.setUpdatedTime(new Date());
            // 插入新凭证
            iFinVouchersService.insertFinVouchers(finVouchers);
            
            // 插入新关联记录
            FinClosingTemplateVouchers templateVouchers = new FinClosingTemplateVouchers();
            templateVouchers.setTemplateId(templateId);
            templateVouchers.setGroupid(template.getGroupid());
            templateVouchers.setVourchesId(finVouchers.getVoucherId().toString());
            templateVouchers.setVoucherDate(finVouchers.getVoucherDate());
            templateVouchers.setCreatedTime(new Date());
            templateVouchers.setUpdatedTime(new Date());
            templateVouchers.setCreateBy(userInfo.getUserName());
            templateVouchers.setUpdateBy(userInfo.getUserName());
            templateVouchers.setDatalog(datalog.toString());
            finClosingTemplateVouchersService.insertFinClosingTemplateVouchers(templateVouchers);
        }
    }
    
    @Override
    public Map<String, Object> getCalculationDetail(String templateId, String periodCode) {
        Map<String, Object> result = new HashMap<>();
        
        FinClosingTemplate template = finClosingTemplateService.selectFinClosingTemplateById(templateId);
        if (template == null) {
            return result;
        }
        
        String groupid = template.getGroupid();

        FinAccountingPeriods period = null;
        if (StrUtil.isNotBlank(periodCode)) {
            period = iFinAccountingPeriodsService.selectByPeriod(groupid, periodCode);
            if (period == null) {
                String periodDate = periodCode + "01";
                SimpleDateFormat FMT_YMD = new SimpleDateFormat("yyyyMMdd");
                try {
                    period = iFinAccountingPeriodsService.selectFinAccountingPeriodsByDate(groupid, FMT_YMD.parse(periodDate));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            period = iFinAccountingPeriodsService.getCurrentPeriod(groupid);
        }
        FinAccountingSubjects querySubject=new FinAccountingSubjects();
        querySubject.setGroupid(template.getGroupid());
        querySubject.setStatus(1);
        List<FinAccountingSubjects> subjects = finAccountingSubjectsService.selectFinAccountingSubjectsList(querySubject);
        Map<String, FinAccountingSubjects> codeMap = new HashMap<>();
        for (FinAccountingSubjects subject : subjects) {
            codeMap.put(subject.getSubjectCode(), subject);
        }
        result.put("templateName", template.getName());
        result.put("periodName", period.getPeriodName());
        result.put("formula", "从亚马逊付款报告读取数据，按模板配置生成凭证");
        result.put("dataSource", "亚马逊付款报告");
        result.put("country", template.getCountry());
        
        // 获取亚马逊模板配置
        FinClosingTemplateAmazon amazonQuery = new FinClosingTemplateAmazon();
        amazonQuery.setClosingTemplateId(templateId);
        List<FinClosingTemplateAmazon> amazonItems = finClosingTemplateAmazonService.selectFinClosingTemplateAmazonList(amazonQuery);
        
        List<Map<String, Object>> itemDetails = new ArrayList<>();
        if (amazonItems != null) {
            for (FinClosingTemplateAmazon item : amazonItems) {
                Map<String, Object> itemDetail = new HashMap<>();
                itemDetail.put("summary", item.getSummary());
                itemDetail.put("subjectId", item.getSubjectId());
                // 查询科目名称
                if (item.getSubjectId() != null) {
                    FinAccountingSubjects subject = finAccountingSubjectsService.getSubjectById(item.getSubjectId());
                    if (subject != null) {
                        itemDetail.put("subjectName", subject.getSubjectCode() + " " +  finAccountingSubjectsService.buildFullSubjectName(subject,codeMap));
                    }
                }
                itemDetail.put("direction", item.getDirection());
                itemDetail.put("amountField", item.getAmountField());
                itemDetails.add(itemDetail);
            }
        }
        result.put("items", itemDetails);
        
        return result;
    }
}
