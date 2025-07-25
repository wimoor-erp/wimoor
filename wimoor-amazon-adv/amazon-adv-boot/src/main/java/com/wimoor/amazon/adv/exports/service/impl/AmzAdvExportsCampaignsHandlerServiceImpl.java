package com.wimoor.amazon.adv.exports.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wimoor.amazon.adv.report.pojo.AmzAdvSnapshot;
import com.wimoor.amazon.adv.sb.dao.AmzAdvCampaignsHsaMapper;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvCampaignsHsa;
import com.wimoor.amazon.adv.sd.dao.AmzAdvCampaignsSDMapper;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvCampaignsSD;
import com.wimoor.amazon.adv.sp.dao.AmzAdvCampaignsMapper;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvCampaigns;
import com.wimoor.amazon.adv.utils.CompareBean;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

@RequiredArgsConstructor
@Service("amzAdvExportsCampaignsHandlerService")
public class AmzAdvExportsCampaignsHandlerServiceImpl extends AmzAdvExportsHandlerServiceImpl {

    final AmzAdvCampaignsMapper amzAdvCampaignsMapper;
    final AmzAdvCampaignsSDMapper amzAdvCampaignsSDMapper;
    final AmzAdvCampaignsHsaMapper amzAdvCampaignsHsaMapper;

    @Override
    public void treatSP(AmzAdvSnapshot record, JSONObject item) {
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
            AmzAdvCampaigns old  = amzAdvCampaignsMapper.selectByPrimaryKey(amzAdvCampaigns.getCampaignid());
            if(old!=null){
                amzAdvCampaigns.setOpttime(old.getOpttime());
                try {
                    if(CompareBean.isContentEqual(old,amzAdvCampaigns)){
                        return;
                    }else {
                        amzAdvCampaigns.setOpttime(new Date());
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                amzAdvCampaignsMapper.updateByPrimaryKey(amzAdvCampaigns);
            }else{
                amzAdvCampaignsMapper.insert(amzAdvCampaigns);
            }
        }

    @Override
    public void treatSB(AmzAdvSnapshot record, JSONObject item) {
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
        AmzAdvCampaignsHsa old  = amzAdvCampaignsHsaMapper.selectByPrimaryKey(amzAdvCampaigns.getCampaignid());
        if(old!=null){
            amzAdvCampaigns.setOpttime(old.getOpttime());
            try {
                if(CompareBean.isContentEqual(old,amzAdvCampaigns)){
                    return;
                }else {
                    amzAdvCampaigns.setOpttime(new Date());
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            amzAdvCampaignsHsaMapper.updateByPrimaryKey(amzAdvCampaigns);
        }else{
            amzAdvCampaignsHsaMapper.insert(amzAdvCampaigns);
        }
    }

    @Override
    public void treatSD(AmzAdvSnapshot record, JSONObject item) {
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
        AmzAdvCampaignsSD old  = amzAdvCampaignsSDMapper.selectByPrimaryKey(amzAdvCampaigns.getCampaignid());
        if(old!=null){
            amzAdvCampaigns.setOpttime(old.getOpttime());
            try {
                if(CompareBean.isContentEqual(old,amzAdvCampaigns)){
                    return;
                }else {
                    amzAdvCampaigns.setOpttime(new Date());
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            amzAdvCampaignsSDMapper.updateByPrimaryKey(amzAdvCampaigns);
        }else{
            amzAdvCampaignsSDMapper.insert(amzAdvCampaigns);
        }
    }

}
