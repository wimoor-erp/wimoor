package com.wimoor.amazon.adv.exports.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.wimoor.amazon.adv.exports.service.IAmzAdvExportsHandlerService;
import com.wimoor.amazon.adv.report.pojo.AmzAdvSnapshot;
import com.wimoor.amazon.adv.sb.dao.AmzAdvAdsAsinHsaMapper;
import com.wimoor.amazon.adv.sb.dao.AmzAdvAdsHsaMapper;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvAdsAsinHsa;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvAdsHsa;
import com.wimoor.amazon.adv.sd.dao.AmzAdvProductadsSDMapper;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvProductadsSD;
import com.wimoor.amazon.adv.sp.dao.AmzAdvProductadsMapper;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvProductads;
import com.wimoor.amazon.adv.utils.CompareBean;
import com.wimoor.common.GeneralUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@RequiredArgsConstructor
@Service("amzAdvExportsAdsHandlerService")
public class AmzAdvExportsAdsHandlerServiceImpl implements IAmzAdvExportsHandlerService {

    final AmzAdvProductadsMapper amzAdvProductadsMapper;
    final AmzAdvProductadsSDMapper amzAdvProductadsSDMapper;
    final AmzAdvAdsHsaMapper amzAdvAdsHsaMapper;
    final AmzAdvAdsAsinHsaMapper amzAdvAdsAsinHsaMapper;

    @Override
    public int treatJSON(AmzAdvSnapshot record, JSONReader jsonReader){
        int num=0;
        List<AmzAdvProductads> listSP = new ArrayList<AmzAdvProductads>();
        List<AmzAdvProductadsSD> listSD = new ArrayList<AmzAdvProductadsSD>();
        List<AmzAdvAdsHsa> listHSA = new ArrayList<AmzAdvAdsHsa>();
        while(jsonReader.hasNext()){
            String elem = jsonReader.readString();
            JSONObject item = GeneralUtil.getJsonObject(elem);
            String state=item.getString("state");
            if(state!=null&&state.equalsIgnoreCase("enabled")){
                num++;
            }
            if(item.getString("adProduct").equals("SPONSORED_DISPLAY")){
                listSD.add(treatSD(record,item));
            }else if(item.getString("adProduct").equals("SPONSORED_BRANDS")){
                listHSA.add( treatSB(record,item));
            }else{
                listSP.add(treatSP(record,item));
            }
        }
        if(listSP!=null&&listSP.size()>0){
            amzAdvProductadsMapper.insertBatch(listSP);
        }
        if(listSD!=null&&listSD.size()>0){
            amzAdvProductadsSDMapper.insertBatch(listSD);
        }
        if(listHSA!=null&&listHSA.size()>0){
            amzAdvAdsHsaMapper.insertBatch(listHSA);
        }
        return num;
    }

    //@Override
    public AmzAdvProductads treatSP(AmzAdvSnapshot record, JSONObject item) {
        AmzAdvProductads ads = new AmzAdvProductads();
        ads.setAdid(item.getBigInteger("adId"));
        ads.setAdgroupid(item.getBigInteger("adGroupId"));
        ads.setCampaignid(item.getBigInteger("campaignId"));
        String state=item.getString("state");
        ads.setState(state!=null?state.toLowerCase():state);
        ads.setProfileid(record.getProfileid());
        ads.setOpttime(new Date());
        ads.setLastUpdatedDate(item.getDate("lastUpdatedDateTime"));
        JSONObject creative = item.getJSONObject("creative");
        if(creative!=null){
            JSONArray products = creative.getJSONArray("products");
            if(products!=null){
                for (int i = 0; i < products.size(); i++){
                    JSONObject product=products.getJSONObject(i);
                    if(product.getString("productIdType").equals("ASIN")){
                        ads.setAsin(product.getString("productId"));
                    }
                    if(product.getString("productIdType").equals("SKU")){
                        ads.setSku(product.getString("productId"));
                    }
                }
            }
        }
        return ads;
    }

    //@Override
    public AmzAdvAdsHsa treatSB(AmzAdvSnapshot record, JSONObject item) {
        AmzAdvAdsHsa ads = new AmzAdvAdsHsa();
        ads.setAdid(item.getBigInteger("adId"));
        ads.setAdgroupid(item.getBigInteger("adGroupId"));
        ads.setCampaignid(item.getBigInteger("campaignId"));
        if(ads.getAdid()==null){
            ads.setAdid(ads.getAdgroupid()!=null?ads.getAdgroupid():ads.getCampaignid());
        }
        String state=item.getString("state");
        ads.setState(state!=null?state.toLowerCase():state);
        ads.setProfileid(record.getProfileid());
        ads.setOpttime(new Date());
        ads.setName(item.getString("name"));
        JSONObject creative = item.getJSONObject("creative");
        if(creative!=null){
            JSONArray products = creative.getJSONArray("products");
            if(products!=null){
                for (int i = 0; i < products.size(); i++){
                    JSONObject product=products.getJSONObject(i);
                    if(product!=null){
                        if(product.getString("productIdType").equals("ASIN")){
                            String asin=product.getString("productId");
                            Example example=new Example(AmzAdvAdsAsinHsa.class);
                            Example.Criteria crit = example.createCriteria();
                            crit.andEqualTo("adid",ads.getAdid());
                            crit.andEqualTo("asin",asin);
                            AmzAdvAdsAsinHsa old = amzAdvAdsAsinHsaMapper.selectOneByExample(example);
                            if(old==null){
                                old=new AmzAdvAdsAsinHsa();
                                old.setAdid(ads.getAdid());
                                old.setAsin(asin);
                                amzAdvAdsAsinHsaMapper.insert(old);
                            }
                        }
                    }
                }
            }

            ads.setCreative(creative.toString());
        }
        return ads;
    }

    //@Override
    public AmzAdvProductadsSD treatSD(AmzAdvSnapshot record, JSONObject item) {
        AmzAdvProductadsSD ads = new AmzAdvProductadsSD();
        ads.setAdid(item.getBigInteger("adId"));
        ads.setAdgroupid(item.getBigInteger("adGroupId"));
        ads.setCampaignid(item.getBigInteger("campaignId"));
        String state=item.getString("state");
        ads.setState(state!=null?state.toLowerCase():state);
        ads.setProfileid(record.getProfileid());
        ads.setOpttime(new Date());
        ads.setLastUpdatedDate(item.getDate("lastUpdatedDateTime"));
        JSONObject creative = item.getJSONObject("creative");
        if(creative!=null){
            JSONArray products = creative.getJSONArray("products");
            if(products!=null){
                for (int i = 0; i < products.size(); i++){
                    JSONObject product=products.getJSONObject(i);
                    if(product.getString("productIdType").equals("ASIN")){
                        ads.setAsin(product.getString("productId"));
                    }
                    if(product.getString("productIdType").equals("SKU")){
                        ads.setSku(product.getString("productId"));
                    }
                }
            }
        }
        return ads;
    }



}
