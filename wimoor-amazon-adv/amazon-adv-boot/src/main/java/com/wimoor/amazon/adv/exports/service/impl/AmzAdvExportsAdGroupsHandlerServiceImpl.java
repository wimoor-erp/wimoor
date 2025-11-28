package com.wimoor.amazon.adv.exports.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.wimoor.amazon.adv.exports.service.IAmzAdvExportsHandlerService;
import com.wimoor.amazon.adv.report.pojo.AmzAdvSnapshot;
import com.wimoor.amazon.adv.sb.dao.AmzAdvAdgroupsHsaMapper;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvAdgroupsHsa;
import com.wimoor.amazon.adv.sd.dao.AmzAdvAdgroupsSDMapper;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvAdgroupsSD;
import com.wimoor.amazon.adv.sp.dao.AmzAdvAdgroupsMapper;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvAdgroups;
import com.wimoor.amazon.adv.utils.CompareBean;
import com.wimoor.common.GeneralUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@RequiredArgsConstructor
@Service("amzAdvExportsAdGroupsHandlerService")
public class AmzAdvExportsAdGroupsHandlerServiceImpl implements IAmzAdvExportsHandlerService {

    final AmzAdvAdgroupsMapper amzAdvAdgroupsMapper;
    final AmzAdvAdgroupsSDMapper amzAdvAdgroupsSDMapper;
    final AmzAdvAdgroupsHsaMapper amzAdvAdgroupsHsaMapper;

    public int treatJSON(AmzAdvSnapshot record, JSONReader jsonReader){
        int num=0;
        List<AmzAdvAdgroups> listSP = new ArrayList<AmzAdvAdgroups>();
        List<AmzAdvAdgroupsSD> listSD = new ArrayList<AmzAdvAdgroupsSD>();
        List<AmzAdvAdgroupsHsa> listHSA = new ArrayList<AmzAdvAdgroupsHsa>();
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
                listHSA.add(treatSB(record,item));
            }else{
                listSP.add(treatSP(record,item));
            }
        }
        if(listSP!=null && listSP.size()>0){
            amzAdvAdgroupsMapper.insertBatch(listSP);
        }
        if(listSD!=null && listSD.size()>0){
            amzAdvAdgroupsSDMapper.insertBatch(listSD);
        }
        if(listHSA!=null && listHSA.size()>0){
            amzAdvAdgroupsHsaMapper.insertBatch(listHSA);
        }
        return num;
    }
    //@Override
    public AmzAdvAdgroups treatSP(AmzAdvSnapshot record, JSONObject item) {
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
         return adgroups;
    }

   // @Override
    public AmzAdvAdgroupsHsa treatSB(AmzAdvSnapshot record, JSONObject item) {
        AmzAdvAdgroupsHsa adgroups=new AmzAdvAdgroupsHsa();
        adgroups.setAdgroupid(item.getBigInteger("adGroupId"));
        adgroups.setCampaignid(item.getBigInteger("campaignId"));
        adgroups.setName(item.getString("name"));
        String state=item.getString("state");
        adgroups.setState(state!=null?state.toLowerCase():state);
        adgroups.setOpttime(new Date());
        adgroups.setProfileid(record.getProfileid());
        return adgroups;
    }

    //@Override
    public AmzAdvAdgroupsSD treatSD(AmzAdvSnapshot record, JSONObject item) {
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
        return adgroups;
    }


}
