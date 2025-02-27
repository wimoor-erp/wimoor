package com.wimoor.amazon.adv.exports.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wimoor.amazon.adv.report.pojo.AmzAdvSnapshot;
import com.wimoor.amazon.adv.sb.dao.AmzAdvAdgroupsHsaMapper;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvAdgroupsHsa;
import com.wimoor.amazon.adv.sd.dao.AmzAdvAdgroupsSDMapper;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvAdgroupsSD;
import com.wimoor.amazon.adv.sp.dao.AmzAdvAdgroupsMapper;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvAdgroups;
import com.wimoor.amazon.adv.utils.CompareBean;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@RequiredArgsConstructor
@Service("amzAdvExportsAdGroupsHandlerService")
public class AmzAdvExportsAdGroupsHandlerServiceImpl  extends AmzAdvExportsHandlerServiceImpl  {

    final AmzAdvAdgroupsMapper amzAdvAdgroupsMapper;
    final AmzAdvAdgroupsSDMapper amzAdvAdgroupsSDMapper;
    final AmzAdvAdgroupsHsaMapper amzAdvAdgroupsHsaMapper;

    @Override
    public void treatSP(AmzAdvSnapshot record, JSONObject item) {
        AmzAdvAdgroups adgroups=new AmzAdvAdgroups();
        JSONObject bid = item.getJSONObject("bid");
        adgroups.setAdgroupid(item.getBigInteger("adGroupId"));
        adgroups.setCampaignid(item.getBigInteger("campaignId"));
        adgroups.setName(item.getString("name"));
        String state=item.getString("state");
        adgroups.setState(state!=null?state.toLowerCase():state);
        adgroups.setOpttime(new Date());
        adgroups.setProfileid(record.getProfileid());
        if(bid!=null){
            BigDecimal defaultBid = bid.getBigDecimal("defaultBid");
            adgroups.setDefaultbid(defaultBid);
        }
        AmzAdvAdgroups oldEntity = amzAdvAdgroupsMapper.selectByPrimaryKey(adgroups.getAdgroupid());
        if(oldEntity!=null){
            adgroups.setOpttime(oldEntity.getOpttime());
            try {
                if(CompareBean.isContentEqual(oldEntity,adgroups)){
                    return;
                }else {
                    adgroups.setOpttime(new Date());
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            amzAdvAdgroupsMapper.updateByPrimaryKey(adgroups);
        }else{
            amzAdvAdgroupsMapper.insert(adgroups);
        }
    }

    @Override
    public void treatSB(AmzAdvSnapshot record, JSONObject item) {

        AmzAdvAdgroupsHsa adgroups=new AmzAdvAdgroupsHsa();
        adgroups.setAdgroupid(item.getBigInteger("adGroupId"));
        adgroups.setCampaignid(item.getBigInteger("campaignId"));
        adgroups.setName(item.getString("name"));
        String state=item.getString("state");
        adgroups.setState(state!=null?state.toLowerCase():state);
        adgroups.setOpttime(new Date());
        adgroups.setProfileid(record.getProfileid());
        AmzAdvAdgroupsHsa oldEntity = amzAdvAdgroupsHsaMapper.selectByPrimaryKey(adgroups.getAdgroupid());
        if(oldEntity!=null){
            adgroups.setOpttime(oldEntity.getOpttime());
            try {
                if(CompareBean.isContentEqual(oldEntity,adgroups)){
                    return;
                }else {
                    adgroups.setOpttime(new Date());
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            amzAdvAdgroupsHsaMapper.updateByPrimaryKey(adgroups);
        }else{
            amzAdvAdgroupsHsaMapper.insert(adgroups);
        }
    }

    @Override
    public void treatSD(AmzAdvSnapshot record, JSONObject item) {
        AmzAdvAdgroupsSD adgroups=new AmzAdvAdgroupsSD();
        JSONObject bid = item.getJSONObject("bid");

        adgroups.setAdgroupid(item.getBigInteger("adGroupId"));
        adgroups.setCampaignid(item.getBigInteger("campaignId"));
        adgroups.setName(item.getString("name"));
        String state=item.getString("state");
        adgroups.setState(state!=null?state.toLowerCase():state);
        adgroups.setOpttime(new Date());
        adgroups.setProfileid(record.getProfileid());
        if(bid!=null){
            BigDecimal defaultBid = bid.getBigDecimal("defaultBid");
            adgroups.setDefaultbid(defaultBid);
        }
        AmzAdvAdgroupsSD oldEntity = amzAdvAdgroupsSDMapper.selectByPrimaryKey(adgroups.getAdgroupid());
        if(oldEntity!=null){
            adgroups.setOpttime(oldEntity.getOpttime());
            try {
                if(CompareBean.isContentEqual(oldEntity,adgroups)){
                    return;
                }else {
                    adgroups.setOpttime(new Date());
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            amzAdvAdgroupsSDMapper.updateByPrimaryKey(adgroups);
        }else{
            amzAdvAdgroupsSDMapper.insert(adgroups);
        }
    }


}
