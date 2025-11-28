package com.wimoor.amazon.adv.exports.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.wimoor.amazon.adv.exports.service.IAmzAdvExportsHandlerService;
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
import com.wimoor.common.GeneralUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigInteger;
import java.util.*;

@RequiredArgsConstructor
@Service("amzAdvExportsTargetsHandlerService")
public class AmzAdvExportsTargetsHandlerServiceImpl implements IAmzAdvExportsHandlerService  {


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

    public int treatJSON(AmzAdvSnapshot record, JSONReader jsonReader){
        int num=0;
        List<AmzAdvProductTargeNegativaSD> listAmzAdvProductTargeNegativaSD=new ArrayList<AmzAdvProductTargeNegativaSD>();
        List<AmzAdvProductTargeSD> listAmzAdvProductTargeSD=new ArrayList<AmzAdvProductTargeSD>();
        List<AmzAdvKeywordsNegativaHsa> listAmzAdvKeywordsNegativaHsa=new ArrayList<AmzAdvKeywordsNegativaHsa>();
        List<AmzAdvProductTargeNegativaHsa> listAmzAdvProductTargeNegativaHsa=new ArrayList<AmzAdvProductTargeNegativaHsa>();
        List<AmzAdvKeywordsHsa> listAmzAdvKeywordsHsa=new ArrayList<AmzAdvKeywordsHsa>();
        List<AmzAdvProductTargeHsa> listAmzAdvProductTargeHsa=new ArrayList<AmzAdvProductTargeHsa>();
        List<AmzAdvKeywordsNegativa> listAmzAdvKeywordsNegativa=new ArrayList<AmzAdvKeywordsNegativa>();
        List<AmzAdvCampKeywordsNegativa> listAmzAdvCampKeywordsNegativa=new ArrayList<AmzAdvCampKeywordsNegativa>();
        List<AmzAdvProductTargeNegativa> listAmzAdvProductTargeNegativa=new ArrayList<AmzAdvProductTargeNegativa>();
        List<AmzAdvCampProductTargeNegativa> listAmzAdvCampProductTargeNegativa=new ArrayList<AmzAdvCampProductTargeNegativa>();
        List<AmzAdvKeywords> listAmzAdvKeywords=new ArrayList<AmzAdvKeywords>();
        List<AmzAdvProductTarge> listAmzAdvProductTarge=new ArrayList<AmzAdvProductTarge>();

        while(jsonReader.hasNext()){
            String elem = jsonReader.readString();
            JSONObject item = GeneralUtil.getJsonObject(elem);
            String state=item.getString("state");
            if(state!=null&&state.equalsIgnoreCase("enabled")){
                num++;
            }
            if(item.getString("adProduct").equals("SPONSORED_DISPLAY")){
               //SD
                String targetType=item.getString("targetType");
                Boolean negative=item.getBoolean("negative");
                if(negative){
                    AmzAdvProductTargeNegativaSD e = treatSDNegativeTarget(record, item);
                    listAmzAdvProductTargeNegativaSD.add(e);
                }else{
                    AmzAdvProductTargeSD e = treatSDTarget(  record,   item);
                    listAmzAdvProductTargeSD.add(e);
                }
            }else if(item.getString("adProduct").equals("SPONSORED_BRANDS")){
                //SB
                String targetType=item.getString("targetType");
                Boolean negative=item.getBoolean("negative");
                if(negative){
                    if(targetType.equals("KEYWORD")){
                        AmzAdvKeywordsNegativaHsa e = treatSBNegativeKeywords(  record,   item);
                        listAmzAdvKeywordsNegativaHsa.add(e);
                    }else{
                        AmzAdvProductTargeNegativaHsa e = treatSBNegativeTarget(  record,   item);
                        listAmzAdvProductTargeNegativaHsa.add(e);
                    }
                }else{
                    if(targetType.equals("KEYWORD")){
                        AmzAdvKeywordsHsa e = treatSBKeywords(  record,   item);
                        listAmzAdvKeywordsHsa.add(e);
                    }else{
                        AmzAdvProductTargeHsa e = treatSBTarget(  record,   item);
                        listAmzAdvProductTargeHsa.add(e);
                    }
                }
            }else{
                //SP
                String targetType=item.getString("targetType");
                Boolean negative=item.getBoolean("negative");
                if(negative){
                    String targetLevel = item.getString("targetLevel");
                    if(targetType.equals("KEYWORD")){
                        if(targetLevel.equals("AD_GROUP")){
                            AmzAdvKeywordsNegativa e= treatSPNegativeKeywords(  record,   item);
                            listAmzAdvKeywordsNegativa.add(e);
                        }else{
                            AmzAdvCampKeywordsNegativa e= treatSPCamsNegativeKeywords(  record,   item);
                            listAmzAdvCampKeywordsNegativa.add(e);
                        }
                    }else{
                        if(targetLevel.equals("AD_GROUP")){
                            AmzAdvProductTargeNegativa e= treatSPNegativeTarget(  record,   item);
                            listAmzAdvProductTargeNegativa.add(e);
                        }else{
                            AmzAdvCampProductTargeNegativa e= treatSPCamsNegativeTarget(  record,   item);
                            listAmzAdvCampProductTargeNegativa.add(e);
                        }
                    }
                }else{
                    if(targetType.equals("KEYWORD")){
                        AmzAdvKeywords e=treatSPKeywords(  record,   item);
                        listAmzAdvKeywords.add(e);
                    }else{
                        AmzAdvProductTarge e=treatSPTarget(  record,   item);
                        listAmzAdvProductTarge.add(e);
                    }
                }
            }
        }
//        amzAdvProductTargeNegativaSDMapper.insertBatch(listAmzAdvProductTargeNegativaSD);
//        amzAdvProductTargeSDMapper.insertBatch(listAmzAdvProductTargeSD);
//        amzAdvKeywordsNegativaHsaMapper.insertBatch(listAmzAdvKeywordsNegativaHsa);
//        amzAdvProductTargeNegativaHsaMapper.insertBatch(listAmzAdvProductTargeNegativaHsa);
//        amzAdvKeywordsHsaMapper.insertBatch(listAmzAdvKeywordsHsa);
//        amzAdvProductTargeHsaMapper.insertBatch(listAmzAdvProductTargeHsa);
//        amzAdvKeywordsNegativaMapper.insertBatch(listAmzAdvKeywordsNegativa);
//        amzAdvCampKeywordsNegativaMapper.insertBatch(listAmzAdvCampKeywordsNegativa);
//        amzAdvProductTargeNegativaMapper.insertBatch(listAmzAdvProductTargeNegativa);
//        amzAdvCampProductTargeNegativaMapper.insertBatch(listAmzAdvCampProductTargeNegativa);
//        amzAdvKeywordsMapper.insertBatch(listAmzAdvKeywords);
//        amzAdvProductTargeMapper.insertBatch(listAmzAdvProductTarge);
        if (listAmzAdvProductTargeNegativaSD != null && listAmzAdvProductTargeNegativaSD.size() > 0) {
            amzAdvProductTargeNegativaSDMapper.insertBatch(listAmzAdvProductTargeNegativaSD);
        }
        if (listAmzAdvProductTargeSD != null && listAmzAdvProductTargeSD.size() > 0) {
            amzAdvProductTargeSDMapper.insertBatch(listAmzAdvProductTargeSD);
        }
        if (listAmzAdvKeywordsNegativaHsa != null && listAmzAdvKeywordsNegativaHsa.size() > 0) {
            amzAdvKeywordsNegativaHsaMapper.insertBatch(listAmzAdvKeywordsNegativaHsa);
        }
        if (listAmzAdvProductTargeNegativaHsa != null && listAmzAdvProductTargeNegativaHsa.size() > 0) {
            amzAdvProductTargeNegativaHsaMapper.insertBatch(listAmzAdvProductTargeNegativaHsa);
        }
        if (listAmzAdvKeywordsHsa != null && listAmzAdvKeywordsHsa.size() > 0) {
            amzAdvKeywordsHsaMapper.insertBatch(listAmzAdvKeywordsHsa);
        }
        if (listAmzAdvProductTargeHsa != null && listAmzAdvProductTargeHsa.size() > 0) {
            amzAdvProductTargeHsaMapper.insertBatch(listAmzAdvProductTargeHsa);
        }
        if (listAmzAdvKeywordsNegativa != null && listAmzAdvKeywordsNegativa.size() > 0) {
            amzAdvKeywordsNegativaMapper.insertBatch(listAmzAdvKeywordsNegativa);
        }
        if (listAmzAdvCampKeywordsNegativa != null && listAmzAdvCampKeywordsNegativa.size() > 0) {
            amzAdvCampKeywordsNegativaMapper.insertBatch(listAmzAdvCampKeywordsNegativa);
        }
        if (listAmzAdvProductTargeNegativa != null && listAmzAdvProductTargeNegativa.size() > 0) {
            amzAdvProductTargeNegativaMapper.insertBatch(listAmzAdvProductTargeNegativa);
        }
        if (listAmzAdvCampProductTargeNegativa != null && listAmzAdvCampProductTargeNegativa.size() > 0) {
            amzAdvCampProductTargeNegativaMapper.insertBatch(listAmzAdvCampProductTargeNegativa);
        }
        if (listAmzAdvKeywords != null && listAmzAdvKeywords.size() > 0) {
            amzAdvKeywordsMapper.insertBatch(listAmzAdvKeywords);
        }
        if (listAmzAdvProductTarge != null && listAmzAdvProductTarge.size() > 0) {
            amzAdvProductTargeMapper.insertBatch(listAmzAdvProductTarge);
        }
        return num;
    }



    private AmzAdvCampProductTargeNegativa treatSPCamsNegativeTarget(AmzAdvSnapshot record, JSONObject item) {
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
        return amzAdvProductTargeNegativa;
    }

    private AmzAdvCampKeywordsNegativa treatSPCamsNegativeKeywords(AmzAdvSnapshot record, JSONObject item) {
        AmzAdvCampKeywordsNegativa amzAdvKeywords=new AmzAdvCampKeywordsNegativa();
        JSONObject targetDetails = item.getJSONObject("targetDetails");
        amzAdvKeywords.setCampaignid(new BigInteger(item.getString("campaignId")));
        amzAdvKeywords.setKeywordid(new BigInteger(item.getString("targetId")));
        amzAdvKeywords.setProfileid(record.getProfileid());
        amzAdvKeywords.setKeywordtext(targetDetails.getString("keyword"));
        amzAdvKeywords.setMatchtype(targetDetails.getString("matchType"));
        String state=item.getString("state");
        amzAdvKeywords.setState(state!=null?state.toLowerCase():state);
       return amzAdvKeywords;
    }

    public AmzAdvKeywords treatSPKeywords(AmzAdvSnapshot record, JSONObject item) {
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
        amzAdvKeywords.setOpttime(new Date());
        return amzAdvKeywords;
    }
    public AmzAdvKeywordsNegativa treatSPNegativeKeywords(AmzAdvSnapshot record, JSONObject item) {
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
        return amzAdvKeywords;
    }
    public AmzAdvProductTarge treatSPTarget(AmzAdvSnapshot record, JSONObject item) {
        JSONObject targetDetails = item.getJSONObject("targetDetails");
        AmzAdvProductTarge amzAdvProductTarge=new AmzAdvProductTarge();
        amzAdvProductTarge.setAdgroupid(new BigInteger(item.getString("adGroupId")));
        amzAdvProductTarge.setCampaignid(new BigInteger(item.getString("campaignId")));
        amzAdvProductTarge.setProfileid(record.getProfileid());
        amzAdvProductTarge.setTargetid(new BigInteger(item.getString("targetId")));
        amzAdvProductTarge.setExpression(item.getString("targetLevel"));
        amzAdvProductTarge.setOpttime(new Date());
        amzAdvProductTarge.setExpression(targetDetails.getString("asin"));
        amzAdvProductTarge.setExpressiontype(targetDetails.getString("matchType"));
        String state=item.getString("state");
        amzAdvProductTarge.setState(state!=null?state.toLowerCase():state);
         return amzAdvProductTarge;
    }
    public AmzAdvProductTargeNegativa treatSPNegativeTarget(AmzAdvSnapshot record, JSONObject item) {
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
      return amzAdvProductTargeNegativa;
    }

    private AmzAdvProductTargeHsa treatSBTarget(AmzAdvSnapshot record, JSONObject item) {
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
        return amzAdvProductTargeNegativa;
    }

    private AmzAdvKeywordsHsa treatSBKeywords(AmzAdvSnapshot record, JSONObject item) {
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
        return amzAdvKeywordsHsa;
    }

    private AmzAdvProductTargeNegativaHsa treatSBNegativeTarget(AmzAdvSnapshot record, JSONObject item) {
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
        return amzAdvProductTargeNegativa;
    }

    private AmzAdvKeywordsNegativaHsa treatSBNegativeKeywords(AmzAdvSnapshot record, JSONObject item) {
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
        return amzAdvKeywordsHsa;
    }
    private AmzAdvProductTargeSD treatSDTarget(AmzAdvSnapshot record, JSONObject item) {
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
        return amzAdvProductTargeSD;
    }

    private AmzAdvProductTargeNegativaSD treatSDNegativeTarget(AmzAdvSnapshot record, JSONObject item) {
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
        return amzAdvProductTargeSD;
    }


}
