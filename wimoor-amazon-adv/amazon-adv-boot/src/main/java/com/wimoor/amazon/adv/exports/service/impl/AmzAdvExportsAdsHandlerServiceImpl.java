package com.wimoor.amazon.adv.exports.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.math.BigInteger;
import java.util.*;

@RequiredArgsConstructor
@Service("amzAdvExportsAdsHandlerService")
public class AmzAdvExportsAdsHandlerServiceImpl  extends AmzAdvExportsHandlerServiceImpl  {

    final AmzAdvProductadsMapper amzAdvProductadsMapper;
    final AmzAdvProductadsSDMapper amzAdvProductadsSDMapper;
    final AmzAdvAdsHsaMapper amzAdvAdsHsaMapper;
    final AmzAdvAdsAsinHsaMapper amzAdvAdsAsinHsaMapper;
    @Override
    public void treatSP(AmzAdvSnapshot record, JSONObject item) {
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
        AmzAdvProductads oldEntity = amzAdvProductadsMapper.selectByPrimaryKey(ads.getAdid());
        if(oldEntity!=null){
            ads.setOpttime(oldEntity.getOpttime());
            try {
                if(CompareBean.isContentEqual(oldEntity,ads)){
                    return;
                }else {
                    ads.setOpttime(new Date());
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            amzAdvProductadsMapper.updateByPrimaryKey(ads);
        }else{
            amzAdvProductadsMapper.insert(ads);
        }


    }

    @Override
    public void treatSB(AmzAdvSnapshot record, JSONObject item) {
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
        AmzAdvAdsHsa oldEntity = amzAdvAdsHsaMapper.selectByPrimaryKey(ads.getAdid());
        if(oldEntity!=null){
            ads.setOpttime(oldEntity.getOpttime());
            try {
                if(CompareBean.isContentEqual(oldEntity,ads)){
                    return;
                }else {
                    ads.setOpttime(new Date());
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            amzAdvAdsHsaMapper.updateByPrimaryKey(ads);
        }else{
            amzAdvAdsHsaMapper.insert(ads);
        }

    }

    @Override
    public void treatSD(AmzAdvSnapshot record, JSONObject item) {
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
        AmzAdvProductadsSD oldEntity = amzAdvProductadsSDMapper.selectByPrimaryKey(ads.getAdid());
        if(oldEntity!=null){
            ads.setOpttime(oldEntity.getOpttime());
            try {
                if(CompareBean.isContentEqual(oldEntity,ads)){
                    return;
                }else {
                    ads.setOpttime(new Date());
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            amzAdvProductadsSDMapper.updateByPrimaryKey(ads);
        }else{
            amzAdvProductadsSDMapper.insert(ads);
        }
    }



}
