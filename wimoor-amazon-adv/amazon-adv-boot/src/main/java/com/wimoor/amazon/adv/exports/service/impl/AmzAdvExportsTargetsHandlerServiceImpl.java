package com.wimoor.amazon.adv.exports.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wimoor.amazon.adv.report.pojo.AmzAdvSnapshot;
import com.wimoor.amazon.adv.sb.dao.AmzAdvKeywordsHsaMapper;
import com.wimoor.amazon.adv.sb.dao.AmzAdvKeywordsNegativaHsaMapper;
import com.wimoor.amazon.adv.sb.dao.AmzAdvProductTargeHsaMapper;
import com.wimoor.amazon.adv.sb.dao.AmzAdvProductTargeNegativaHsaMapper;
import com.wimoor.amazon.adv.sb.pojo.*;
import com.wimoor.amazon.adv.sd.dao.AmzAdvProductTargeNegativaSDMapper;
import com.wimoor.amazon.adv.sd.dao.AmzAdvProductTargeSDMapper;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvProductTargeNegativaSD;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvProductTargeSD;
import com.wimoor.amazon.adv.sp.dao.*;
import com.wimoor.amazon.adv.sp.pojo.*;
import com.wimoor.amazon.adv.utils.CompareBean;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigInteger;
import java.util.*;

@RequiredArgsConstructor
@Service("amzAdvExportsTargetsHandlerService")
public class AmzAdvExportsTargetsHandlerServiceImpl  extends AmzAdvExportsHandlerServiceImpl  {


    final AmzAdvKeywordsMapper amzAdvKeywordsMapper;
    final AmzAdvKeywordsNegativaMapper amzAdvKeywordsNegativaMapper;
    final AmzAdvProductTargeMapper amzAdvProductTargeMapper;
    final AmzAdvProductTargeNegativaMapper amzAdvProductTargeNegativaMapper;
    final AmzAdvCampKeywordsNegativaMapper amzAdvCampKeywordsNegativaMapper;
    final AmzAdvCampProductTargeNegativaMapper amzAdvCampProductTargeNegativaMapper;

    final AmzAdvKeywordsHsaMapper amzAdvKeywordsHsaMapper;
    final AmzAdvKeywordsNegativaHsaMapper amzAdvKeywordsNegativaHsaMapper;
    final AmzAdvProductTargeHsaMapper amzAdvProductTargeHsaMapper;
    final AmzAdvProductTargeNegativaHsaMapper amzAdvProductTargeNegativaHsaMapper;

    final AmzAdvProductTargeSDMapper amzAdvProductTargeSDMapper;
    final AmzAdvProductTargeNegativaSDMapper amzAdvProductTargeNegativaSDMapper;
    @Override
    public void treatSP(AmzAdvSnapshot record, JSONObject item) {
        String targetType=item.getString("targetType");
        Boolean negative=item.getBoolean("negative");
        if(negative){
            String targetLevel = item.getString("targetLevel");
            if(targetType.equals("KEYWORD")){
                if(targetLevel.equals("AD_GROUP")){
                    treatSPNegativeKeywords(  record,   item);
                }else{
                    treatSPCamsNegativeKeywords(  record,   item);
                }
            }else{
                if(targetLevel.equals("AD_GROUP")){
                    treatSPNegativeTarget(  record,   item);
                }else{
                    treatSPCamsNegativeTarget(  record,   item);
                }
            }
        }else{
            if(targetType.equals("KEYWORD")){

                treatSPKeywords(  record,   item);
            }else{
                treatSPTarget(  record,   item);
            }
        }
    }
    @Override
    public void treatSB(AmzAdvSnapshot record, JSONObject item) {
        String targetType=item.getString("targetType");
        Boolean negative=item.getBoolean("negative");
        if(negative){
            if(targetType.equals("KEYWORD")){
                treatSBNegativeKeywords(  record,   item);
            }else{
                treatSBNegativeTarget(  record,   item);
            }
        }else{
            if(targetType.equals("KEYWORD")){
                treatSBKeywords(  record,   item);
            }else{
                treatSBTarget(  record,   item);
            }
        }
    }


    @Override
    public void treatSD(AmzAdvSnapshot record, JSONObject item) {
        String targetType=item.getString("targetType");
        Boolean negative=item.getBoolean("negative");
        if(negative){
                treatSDNegativeTarget(  record,   item);
        }else{
                treatSDTarget(  record,   item);
        }
    }


    private void treatSPCamsNegativeTarget(AmzAdvSnapshot record, JSONObject item) {
        JSONObject targetDetails = item.getJSONObject("targetDetails");
        AmzAdvCampProductTargeNegativa amzAdvProductTargeNegativa=new AmzAdvCampProductTargeNegativa();
        amzAdvProductTargeNegativa.setCampaignid(new BigInteger(item.getString("campaignId")));
        amzAdvProductTargeNegativa.setProfileid(record.getProfileid());
        amzAdvProductTargeNegativa.setTargetid(new BigInteger(item.getString("targetId")));
        amzAdvProductTargeNegativa.setExpression(item.getString("targetLevel"));
        amzAdvProductTargeNegativa.setOpttime(new Date());
        amzAdvProductTargeNegativa.setExpression(targetDetails.getString("asin"));
        amzAdvProductTargeNegativa.setExpressiontype(targetDetails.getString("matchType"));
        String state=item.getString("state");
        amzAdvProductTargeNegativa.setState(state!=null?state.toLowerCase():state);
        AmzAdvCampProductTargeNegativa oldEntity = amzAdvCampProductTargeNegativaMapper.selectByPrimaryKey(amzAdvProductTargeNegativa.getTargetid());
        if(oldEntity!=null){
            amzAdvProductTargeNegativa.setOpttime(oldEntity.getOpttime());
            try {
                if(CompareBean.isContentEqual(oldEntity,amzAdvProductTargeNegativa)){
                    return;
                }else {
                    amzAdvProductTargeNegativa.setOpttime(new Date());
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            amzAdvCampProductTargeNegativaMapper.updateByPrimaryKey(amzAdvProductTargeNegativa);
        }else{
            amzAdvCampProductTargeNegativaMapper.insert(amzAdvProductTargeNegativa);
        }
    }

    private void treatSPCamsNegativeKeywords(AmzAdvSnapshot record, JSONObject item) {
        AmzAdvCampKeywordsNegativa amzAdvKeywords=new AmzAdvCampKeywordsNegativa();
        JSONObject targetDetails = item.getJSONObject("targetDetails");
        amzAdvKeywords.setCampaignid(new BigInteger(item.getString("campaignId")));
        amzAdvKeywords.setKeywordid(new BigInteger(item.getString("targetId")));
        amzAdvKeywords.setProfileid(record.getProfileid());
        amzAdvKeywords.setKeywordtext(targetDetails.getString("keyword"));
        amzAdvKeywords.setMatchtype(targetDetails.getString("matchType"));
        String state=item.getString("state");
        amzAdvKeywords.setState(state!=null?state.toLowerCase():state);
        AmzAdvCampKeywordsNegativa oldEntity = amzAdvCampKeywordsNegativaMapper.selectByPrimaryKey(amzAdvKeywords.getKeywordid());
        if(oldEntity!=null){
            amzAdvKeywords.setOpttime(oldEntity.getOpttime());
            try {
                if(CompareBean.isContentEqual(oldEntity,amzAdvKeywords)){
                    return;
                }else {
                    amzAdvKeywords.setOpttime(new Date());
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            amzAdvCampKeywordsNegativaMapper.updateByPrimaryKey(amzAdvKeywords);
        }else{
            amzAdvCampKeywordsNegativaMapper.insert(amzAdvKeywords);
        }
    }

    public void treatSPKeywords(AmzAdvSnapshot record, JSONObject item) {
        AmzAdvKeywords amzAdvKeywords=new AmzAdvKeywords();
        JSONObject bid = item.getJSONObject("bid");
        JSONObject targetDetails = item.getJSONObject("targetDetails");
        amzAdvKeywords.setAdgroupid(new BigInteger(item.getString("adGroupId")));
        amzAdvKeywords.setCampaignid(new BigInteger(item.getString("campaignId")));
        amzAdvKeywords.setKeywordid(new BigInteger(item.getString("targetId")));
        amzAdvKeywords.setProfileid(record.getProfileid());
        amzAdvKeywords.setKeywordtext(targetDetails.getString("keyword"));
        amzAdvKeywords.setMatchtype(targetDetails.getString("matchType"));
        if(bid!=null){
            amzAdvKeywords.setBid(bid.getBigDecimal("bid"));
        }
        amzAdvKeywords.setCampaigntype("sp");
        String state=item.getString("state");
        amzAdvKeywords.setState(state!=null?state.toLowerCase():state);
        AmzAdvKeywords oldEntity = amzAdvKeywordsMapper.selectByPrimaryKey(amzAdvKeywords.getKeywordid());
        if(oldEntity!=null){
            amzAdvKeywords.setOpttime(oldEntity.getOpttime());
            try {
                if(CompareBean.isContentEqual(oldEntity,amzAdvKeywords)){
                    return;
                }else {
                    amzAdvKeywords.setOpttime(new Date());
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            amzAdvKeywordsMapper.updateByPrimaryKey(amzAdvKeywords);
        }else{
            amzAdvKeywordsMapper.insert(amzAdvKeywords);
        }

    }
    public void treatSPNegativeKeywords(AmzAdvSnapshot record, JSONObject item) {
        AmzAdvKeywordsNegativa amzAdvKeywords=new AmzAdvKeywordsNegativa();
        JSONObject targetDetails = item.getJSONObject("targetDetails");
        amzAdvKeywords.setAdgroupid(new BigInteger(item.getString("adGroupId")));
        amzAdvKeywords.setCampaignid(new BigInteger(item.getString("campaignId")));
        amzAdvKeywords.setKeywordid(new BigInteger(item.getString("targetId")));
        amzAdvKeywords.setProfileid(record.getProfileid());
        amzAdvKeywords.setKeywordtext(targetDetails.getString("keyword"));
        amzAdvKeywords.setMatchtype(targetDetails.getString("matchType"));
        amzAdvKeywords.setCampaigntype("sp");
        String state=item.getString("state");
        amzAdvKeywords.setState(state!=null?state.toLowerCase():state);
        AmzAdvKeywordsNegativa oldEntity = amzAdvKeywordsNegativaMapper.selectByPrimaryKey(amzAdvKeywords.getKeywordid());
        if(oldEntity!=null){
            amzAdvKeywords.setOpttime(oldEntity.getOpttime());
            try {
                if(CompareBean.isContentEqual(oldEntity,amzAdvKeywords)){
                    return;
                }else {
                    amzAdvKeywords.setOpttime(new Date());
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            amzAdvKeywordsNegativaMapper.updateByPrimaryKey(amzAdvKeywords);
        }else{
            amzAdvKeywordsNegativaMapper.insert(amzAdvKeywords);
        }
    }
    public void treatSPTarget(AmzAdvSnapshot record, JSONObject item) {
        JSONObject targetDetails = item.getJSONObject("targetDetails");
        AmzAdvProductTargeNegativa amzAdvProductTargeNegativa=new AmzAdvProductTargeNegativa();
        amzAdvProductTargeNegativa.setAdgroupid(new BigInteger(item.getString("adGroupId")));
        amzAdvProductTargeNegativa.setCampaignid(new BigInteger(item.getString("campaignId")));
        amzAdvProductTargeNegativa.setProfileid(record.getProfileid());
        amzAdvProductTargeNegativa.setTargetid(new BigInteger(item.getString("targetId")));
        amzAdvProductTargeNegativa.setExpression(item.getString("targetLevel"));
        amzAdvProductTargeNegativa.setOpttime(new Date());
        amzAdvProductTargeNegativa.setExpression(targetDetails.getString("asin"));
        amzAdvProductTargeNegativa.setExpressiontype(targetDetails.getString("matchType"));
        String state=item.getString("state");
        amzAdvProductTargeNegativa.setState(state!=null?state.toLowerCase():state);
        AmzAdvProductTargeNegativa oldEntity = amzAdvProductTargeNegativaMapper.selectByPrimaryKey(amzAdvProductTargeNegativa.getTargetid());
        if(oldEntity!=null){
            amzAdvProductTargeNegativa.setOpttime(oldEntity.getOpttime());
            try {
                if(CompareBean.isContentEqual(oldEntity,amzAdvProductTargeNegativa)){
                    return;
                }else {
                    amzAdvProductTargeNegativa.setOpttime(new Date());
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            amzAdvProductTargeNegativaMapper.updateByPrimaryKey(amzAdvProductTargeNegativa);
        }else{
            amzAdvProductTargeNegativaMapper.insert(amzAdvProductTargeNegativa);
        }

    }
    public void treatSPNegativeTarget(AmzAdvSnapshot record, JSONObject item) {
        JSONObject targetDetails = item.getJSONObject("targetDetails");
        AmzAdvProductTargeNegativa amzAdvProductTargeNegativa=new AmzAdvProductTargeNegativa();
        amzAdvProductTargeNegativa.setAdgroupid(new BigInteger(item.getString("adGroupId")));
        amzAdvProductTargeNegativa.setCampaignid(new BigInteger(item.getString("campaignId")));
        amzAdvProductTargeNegativa.setProfileid(record.getProfileid());
        amzAdvProductTargeNegativa.setTargetid(new BigInteger(item.getString("targetId")));
        amzAdvProductTargeNegativa.setExpression(item.getString("targetLevel"));
        amzAdvProductTargeNegativa.setOpttime(new Date());
        amzAdvProductTargeNegativa.setExpression(targetDetails.getString("asin"));
        amzAdvProductTargeNegativa.setExpressiontype(targetDetails.getString("matchType"));
        String state=item.getString("state");
        amzAdvProductTargeNegativa.setState(state!=null?state.toLowerCase():state);
        AmzAdvProductTargeNegativa oldEntity = amzAdvProductTargeNegativaMapper.selectByPrimaryKey(amzAdvProductTargeNegativa.getTargetid());
        if(oldEntity!=null){
            amzAdvProductTargeNegativa.setOpttime(oldEntity.getOpttime());
            try {
                if(CompareBean.isContentEqual(oldEntity,amzAdvProductTargeNegativa)){
                    return;
                }else {
                    amzAdvProductTargeNegativa.setOpttime(new Date());
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            amzAdvProductTargeNegativaMapper.updateByPrimaryKey(amzAdvProductTargeNegativa);
        }else{
            amzAdvProductTargeNegativaMapper.insert(amzAdvProductTargeNegativa);
        }

    }

    private void treatSBTarget(AmzAdvSnapshot record, JSONObject item) {
        JSONObject targetDetails = item.getJSONObject("targetDetails");
        AmzAdvProductTargeHsa amzAdvProductTargeNegativa=new AmzAdvProductTargeHsa();
        amzAdvProductTargeNegativa.setAdgroupid(new BigInteger(item.getString("adGroupId")));
        amzAdvProductTargeNegativa.setCampaignid(new BigInteger(item.getString("campaignId")));
        amzAdvProductTargeNegativa.setProfileid(record.getProfileid());
        amzAdvProductTargeNegativa.setTargetid(new BigInteger(item.getString("targetId")));
        amzAdvProductTargeNegativa.setExpression(item.getString("targetLevel"));
        amzAdvProductTargeNegativa.setOpttime(new Date());
        amzAdvProductTargeNegativa.setExpression(targetDetails.getString("asin"));
        amzAdvProductTargeNegativa.setExpressiontype(targetDetails.getString("matchType"));
        String state=item.getString("state");
        amzAdvProductTargeNegativa.setState(state!=null?state.toLowerCase():state);
        AmzAdvProductTargeHsa oldEntity = amzAdvProductTargeHsaMapper.selectByPrimaryKey(amzAdvProductTargeNegativa.getTargetid());
        if(oldEntity!=null){
            amzAdvProductTargeNegativa.setOpttime(oldEntity.getOpttime());
            try {
                if(CompareBean.isContentEqual(oldEntity,amzAdvProductTargeNegativa)){
                    return;
                }else {
                    amzAdvProductTargeNegativa.setOpttime(new Date());
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            amzAdvProductTargeHsaMapper.updateByPrimaryKey(amzAdvProductTargeNegativa);
        }else{
            amzAdvProductTargeHsaMapper.insert(amzAdvProductTargeNegativa);
        }


    }

    private void treatSBKeywords(AmzAdvSnapshot record, JSONObject item) {
        AmzAdvKeywordsHsa amzAdvKeywordsHsa=new AmzAdvKeywordsHsa();
        JSONObject targetDetails = item.getJSONObject("targetDetails");
        amzAdvKeywordsHsa.setAdgroupid(new BigInteger(item.getString("adGroupId")));
        amzAdvKeywordsHsa.setCampaignid(new BigInteger(item.getString("campaignId")));
        amzAdvKeywordsHsa.setKeywordid(new BigInteger(item.getString("targetId")));
        amzAdvKeywordsHsa.setProfileid(record.getProfileid());
        amzAdvKeywordsHsa.setKeywordtext(targetDetails.getString("keyword"));
        amzAdvKeywordsHsa.setMatchtype(targetDetails.getString("matchType"));
        amzAdvKeywordsHsa.setCampaigntype("hsa");
        amzAdvKeywordsHsa.setOpttime(new Date());
        if(targetDetails!=null){
            amzAdvKeywordsHsa.setBid(targetDetails.getBigDecimal("bid"));
        }
        String state=item.getString("state");
        amzAdvKeywordsHsa.setState(state!=null?state.toLowerCase():state);
        amzAdvKeywordsHsa.setNativeLanguageKeyword(null);
        AmzAdvKeywordsHsa oldEntity = amzAdvKeywordsHsaMapper.selectByPrimaryKey(amzAdvKeywordsHsa.getKeywordid());
        if(oldEntity!=null){
            amzAdvKeywordsHsa.setOpttime(oldEntity.getOpttime());
            try {
                if(CompareBean.isContentEqual(oldEntity,amzAdvKeywordsHsa)){
                    return;
                }else {
                    amzAdvKeywordsHsa.setOpttime(new Date());
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            amzAdvKeywordsHsaMapper.updateByPrimaryKey(amzAdvKeywordsHsa);
        }else{
            amzAdvKeywordsHsaMapper.insert(amzAdvKeywordsHsa);
        }
    }

    private void treatSBNegativeTarget(AmzAdvSnapshot record, JSONObject item) {
        JSONObject targetDetails = item.getJSONObject("targetDetails");
        AmzAdvProductTargeNegativaHsa amzAdvProductTargeNegativa=new AmzAdvProductTargeNegativaHsa();
        amzAdvProductTargeNegativa.setAdgroupid(new BigInteger(item.getString("adGroupId")));
        amzAdvProductTargeNegativa.setCampaignid(new BigInteger(item.getString("campaignId")));
        amzAdvProductTargeNegativa.setProfileid(record.getProfileid());
        amzAdvProductTargeNegativa.setTargetid(new BigInteger(item.getString("targetId")));
        amzAdvProductTargeNegativa.setExpression(item.getString("targetLevel"));
        amzAdvProductTargeNegativa.setOpttime(new Date());
        amzAdvProductTargeNegativa.setExpression(targetDetails.getString("asin"));
        amzAdvProductTargeNegativa.setExpressiontype(targetDetails.getString("matchType"));
        String state=item.getString("state");
        amzAdvProductTargeNegativa.setState(state!=null?state.toLowerCase():state);
        AmzAdvProductTargeNegativaHsa oldEntity = amzAdvProductTargeNegativaHsaMapper.selectByPrimaryKey(amzAdvProductTargeNegativa.getTargetid());
        if(oldEntity!=null){
            amzAdvProductTargeNegativa.setOpttime(oldEntity.getOpttime());
            try {
                if(CompareBean.isContentEqual(oldEntity,amzAdvProductTargeNegativa)){
                    return;
                }else {
                    amzAdvProductTargeNegativa.setOpttime(new Date());
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            amzAdvProductTargeNegativaHsaMapper.updateByPrimaryKey(amzAdvProductTargeNegativa);
        }else{
            amzAdvProductTargeNegativaHsaMapper.insert(amzAdvProductTargeNegativa);
        }
    }

    private void treatSBNegativeKeywords(AmzAdvSnapshot record, JSONObject item) {
        AmzAdvKeywordsNegativaHsa amzAdvKeywordsHsa=new AmzAdvKeywordsNegativaHsa();
        JSONObject targetDetails = item.getJSONObject("targetDetails");
        amzAdvKeywordsHsa.setAdgroupid(new BigInteger(item.getString("adGroupId")));
        amzAdvKeywordsHsa.setCampaignid(new BigInteger(item.getString("campaignId")));
        amzAdvKeywordsHsa.setKeywordid(new BigInteger(item.getString("targetId")));
        amzAdvKeywordsHsa.setProfileid(record.getProfileid());
        amzAdvKeywordsHsa.setKeywordtext(targetDetails.getString("keyword"));
        amzAdvKeywordsHsa.setMatchtype(targetDetails.getString("matchType"));
        amzAdvKeywordsHsa.setOpttime(new Date());
        String state=item.getString("state");
        amzAdvKeywordsHsa.setState(state!=null?state.toLowerCase():state);
        AmzAdvKeywordsNegativaHsa oldEntity = amzAdvKeywordsNegativaHsaMapper.selectByPrimaryKey(amzAdvKeywordsHsa.getKeywordid());
        if(oldEntity!=null){
            amzAdvKeywordsHsa.setOpttime(oldEntity.getOpttime());
            try {
                if(CompareBean.isContentEqual(oldEntity,amzAdvKeywordsHsa)){
                    return;
                }else {
                    amzAdvKeywordsHsa.setOpttime(new Date());
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            amzAdvKeywordsNegativaHsaMapper.updateByPrimaryKey(amzAdvKeywordsHsa);
        }else{
            amzAdvKeywordsNegativaHsaMapper.insert(amzAdvKeywordsHsa);
        }
    }
    private void treatSDTarget(AmzAdvSnapshot record, JSONObject item) {
        AmzAdvProductTargeSD amzAdvProductTargeSD=new AmzAdvProductTargeSD();
        JSONObject targetDetails = item.getJSONObject("targetDetails");
        amzAdvProductTargeSD.setAdgroupid(new BigInteger(item.getString("adGroupId")));
        amzAdvProductTargeSD.setProfileid(record.getProfileid());
        amzAdvProductTargeSD.setTargetid(new BigInteger(item.getString("targetId")));
        amzAdvProductTargeSD.setOpttime(new Date());
        String state=item.getString("state");
        amzAdvProductTargeSD.setState(state!=null?state.toLowerCase():state);
        if(item.getJSONObject("bid")!=null){
            amzAdvProductTargeSD.setBid(item.getJSONObject("bid").getBigDecimal("bid"));
        }
       if(targetDetails!=null){
           amzAdvProductTargeSD.setExpressiontype(targetDetails.getString("matchType"));
           amzAdvProductTargeSD.setExpression(targetDetails.getString("asin"));
       }
        AmzAdvProductTargeSD oldEntity = amzAdvProductTargeSDMapper.selectByPrimaryKey(amzAdvProductTargeSD.getTargetid());
        if(oldEntity!=null){
            amzAdvProductTargeSD.setOpttime(oldEntity.getOpttime());
            try {
                if(CompareBean.isContentEqual(oldEntity,amzAdvProductTargeSD)){
                    return;
                }else {
                    amzAdvProductTargeSD.setOpttime(new Date());
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            amzAdvProductTargeSDMapper.updateByPrimaryKey(amzAdvProductTargeSD);
        }else{
            amzAdvProductTargeSDMapper.insert(amzAdvProductTargeSD);
        }

    }

    private void treatSDNegativeTarget(AmzAdvSnapshot record, JSONObject item) {
        AmzAdvProductTargeNegativaSD amzAdvProductTargeSD=new AmzAdvProductTargeNegativaSD();
        JSONObject targetDetails = item.getJSONObject("targetDetails");
        amzAdvProductTargeSD.setAdgroupid(new BigInteger(item.getString("adGroupId")));
        amzAdvProductTargeSD.setProfileid(record.getProfileid());
        amzAdvProductTargeSD.setTargetid(new BigInteger(item.getString("targetId")));
        amzAdvProductTargeSD.setOpttime(new Date());

        String state=item.getString("state");
        amzAdvProductTargeSD.setState(state!=null?state.toLowerCase():state);
        if(targetDetails!=null){
            amzAdvProductTargeSD.setExpressiontype(targetDetails.getString("matchType"));
            amzAdvProductTargeSD.setExpression(targetDetails.getString("asin"));
        }

        AmzAdvProductTargeNegativaSD oldEntity = amzAdvProductTargeNegativaSDMapper.selectByPrimaryKey(amzAdvProductTargeSD.getTargetid());
        if(oldEntity!=null){
            amzAdvProductTargeSD.setOpttime(oldEntity.getOpttime());
            try {
                if(CompareBean.isContentEqual(oldEntity,amzAdvProductTargeSD)){
                    return;
                }else {
                    amzAdvProductTargeSD.setOpttime(new Date());
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            amzAdvProductTargeNegativaSDMapper.updateByPrimaryKey(amzAdvProductTargeSD);
        }else{
            amzAdvProductTargeNegativaSDMapper.insert(amzAdvProductTargeSD);
        }
    }


}
