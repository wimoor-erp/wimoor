package com.wimoor.amazon.adv.exports.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.wimoor.amazon.adv.exports.service.IAmzAdvExportsHandlerService;
import com.wimoor.amazon.adv.report.pojo.AmzAdvSnapshot;
import com.wimoor.amazon.adv.sb.dao.AmzAdvCampaignsHsaMapper;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvCampaignsHsa;
import com.wimoor.amazon.adv.sd.dao.AmzAdvCampaignsSDMapper;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvCampaignsSD;
import com.wimoor.amazon.adv.sp.dao.AmzAdvCampaignsMapper;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvCampaigns;
import com.wimoor.amazon.adv.utils.CompareBean;
import com.wimoor.common.GeneralUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

@RequiredArgsConstructor
@Service("amzAdvExportsCampaignsHandlerService")
public class AmzAdvExportsCampaignsHandlerServiceImpl implements IAmzAdvExportsHandlerService {

    final AmzAdvCampaignsMapper amzAdvCampaignsMapper;
    final AmzAdvCampaignsSDMapper amzAdvCampaignsSDMapper;
    final AmzAdvCampaignsHsaMapper amzAdvCampaignsHsaMapper;

    public int treatJSON(AmzAdvSnapshot record, JSONReader jsonReader){
        int num=0;
        List<AmzAdvCampaignsSD> listSD=new ArrayList<AmzAdvCampaignsSD>();
        List<AmzAdvCampaignsHsa> listHsa=new ArrayList<AmzAdvCampaignsHsa>();
        List<AmzAdvCampaigns> listSP=new ArrayList<AmzAdvCampaigns>();
        while(jsonReader.hasNext()){
            String elem = jsonReader.readString();
            JSONObject item = GeneralUtil.getJsonObject(elem);
            String state=item.getString("state");
            if(state!=null&&state.equalsIgnoreCase("enabled")){
                num++;
            }
            if(item.getString("adProduct").equals("SPONSORED_DISPLAY")){
                AmzAdvCampaignsSD e=treatSD(record,item);
                listSD.add(e);
            }else if(item.getString("adProduct").equals("SPONSORED_BRANDS")){
                AmzAdvCampaignsHsa e=treatSB(record,item);
                listHsa.add(e);
            }else{
                AmzAdvCampaigns e=treatSP(record,item);
                listSP.add(e);
            }
        }
        if(listSD!=null&&listSD.size()>0){
            amzAdvCampaignsSDMapper.insertBatch(listSD);
        }
        if(listHsa!=null&&listHsa.size()>0){
            amzAdvCampaignsHsaMapper.insertBatch(listHsa);
        }
        if(listSP!=null&&listSP.size()>0){
            amzAdvCampaignsMapper.insertBatch(listSP);
        }
        return num;
    }


    //@Override
    public AmzAdvCampaigns treatSP(AmzAdvSnapshot record, JSONObject item) {
            String campaignType = "SP";
            BigInteger portfolioId = item.getBigInteger("portfolioId");
            BigInteger campaignId = item.getBigInteger("campaignId");
            String name = item.getString("name");
            Date startDate=item.getDate("startDate");
            String state=item.getString("state");
            //String deliveryStatus = item.getString("deliveryStatus");
            JSONObject optimization = item.getJSONObject("optimization");
            String targetingSettings = item.getString("targetingSettings");
            JSONObject budgetCaps = item.getJSONObject("budgetCaps");
            JSONObject budgetValue = budgetCaps.getJSONObject("budgetValue");
            JSONObject monetaryBudget= budgetValue.getJSONObject("monetaryBudget");
            BigDecimal dailyBudget = monetaryBudget.getBigDecimal("amount");

            AmzAdvCampaigns amzAdvCampaigns = new AmzAdvCampaigns();
            amzAdvCampaigns.setCampaignid(campaignId);
            amzAdvCampaigns.setPortfolioid(portfolioId);
            amzAdvCampaigns.setCampaignType(campaignType);
            amzAdvCampaigns.setDailybudget(dailyBudget);
            amzAdvCampaigns.setName(name);
            amzAdvCampaigns.setBidding(optimization.toString());
            amzAdvCampaigns.setState(state!=null?state.toLowerCase():state);
            amzAdvCampaigns.setPremiumbidadjustment(null);
            amzAdvCampaigns.setStartDate(startDate);
            amzAdvCampaigns.setTargetingType(targetingSettings);
            amzAdvCampaigns.setEndDate(null);
            amzAdvCampaigns.setProfileid(record.getProfileid());
            amzAdvCampaigns.setOpttime(new Date());
            return amzAdvCampaigns;
        }

    //@Override
    public AmzAdvCampaignsHsa treatSB(AmzAdvSnapshot record, JSONObject item) {
        BigInteger portfolioId = item.getBigInteger("portfolioId");
        BigInteger campaignId = item.getBigInteger("campaignId");
        String name = item.getString("name");
        Date startDate=item.getDate("startDate");
        String state=item.getString("state");
        String brandEntityId=item.getString("brandEntityId");
        //String deliveryStatus = item.getString("deliveryStatus");
        JSONObject optimization = item.getJSONObject("optimization");
        JSONObject budgetCaps = item.getJSONObject("budgetCaps");
        JSONObject budgetValue = budgetCaps.getJSONObject("budgetValue");
        JSONObject monetaryBudget= budgetValue.getJSONObject("monetaryBudget");
        BigDecimal dailyBudget = monetaryBudget.getBigDecimal("amount");
        String costType=item.getString("costType");
        Boolean isMultiAdGroupsEnabled = item.getBoolean("isMultiAdGroupsEnabled");

        AmzAdvCampaignsHsa amzAdvCampaigns = new AmzAdvCampaignsHsa();
        amzAdvCampaigns.setCampaignid(campaignId);
        amzAdvCampaigns.setPortfolioid(portfolioId);
        amzAdvCampaigns.setBudget(dailyBudget);
        amzAdvCampaigns.setName(name);
        amzAdvCampaigns.setState(state!=null?state.toLowerCase():state);
        amzAdvCampaigns.setBrandEntityId(brandEntityId);
        amzAdvCampaigns.setBidding(optimization.toString());
        amzAdvCampaigns.setCostType(costType);
        amzAdvCampaigns.setStartDate(startDate);
        amzAdvCampaigns.setEndDate(null);
        amzAdvCampaigns.setMultiAdGroupsEnabled(isMultiAdGroupsEnabled);
        amzAdvCampaigns.setProfileid(record.getProfileid());
        amzAdvCampaigns.setBudgetType(budgetCaps.getString("recurrenceTimePeriod"));
        amzAdvCampaigns.setOpttime(new Date());
       return amzAdvCampaigns;
    }

    //@Override
    public AmzAdvCampaignsSD treatSD(AmzAdvSnapshot record, JSONObject item) {
        BigInteger portfolioId = item.getBigInteger("portfolioId");
        BigInteger campaignId = item.getBigInteger("campaignId");
        String name = item.getString("name");
        Date startDate=item.getDate("startDate");
        String state=item.getString("state");
        String costType=item.getString("costType");
        //String deliveryStatus = item.getString("deliveryStatus");
        String targetingSettings = item.getString("targetingSettings");
        JSONObject budgetCaps = item.getJSONObject("budgetCaps");
        JSONObject budgetValue = budgetCaps.getJSONObject("budgetValue");
        JSONObject monetaryBudget= budgetValue.getJSONObject("monetaryBudget");
        BigDecimal dailyBudget = monetaryBudget.getBigDecimal("amount");

        AmzAdvCampaignsSD amzAdvCampaigns = new AmzAdvCampaignsSD();
        amzAdvCampaigns.setCampaignid(campaignId);
        amzAdvCampaigns.setPortfolioId(portfolioId);
        amzAdvCampaigns.setBudget(dailyBudget);
        amzAdvCampaigns.setName(name);
        amzAdvCampaigns.setState(state!=null?state.toLowerCase():state);
        amzAdvCampaigns.setStartDate(startDate);
        amzAdvCampaigns.setTactic(targetingSettings);
        amzAdvCampaigns.setBudgettype(budgetCaps.getString("recurrenceTimePeriod"));
        amzAdvCampaigns.setEndDate(null);
        amzAdvCampaigns.setProfileid(record.getProfileid());
        amzAdvCampaigns.setOpttime(new Date());
        amzAdvCampaigns.setCosttype(costType);
         return amzAdvCampaigns;
    }

}
