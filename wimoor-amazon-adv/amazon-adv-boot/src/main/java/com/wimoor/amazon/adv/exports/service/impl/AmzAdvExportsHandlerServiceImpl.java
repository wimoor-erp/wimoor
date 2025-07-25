package com.wimoor.amazon.adv.exports.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wimoor.amazon.adv.exports.service.IAmzAdvExportsHandlerService;
import com.wimoor.amazon.adv.report.pojo.AmzAdvSnapshot;

public abstract class AmzAdvExportsHandlerServiceImpl implements IAmzAdvExportsHandlerService {
    public void treat(AmzAdvSnapshot record, JSONArray a){
        for(int i=0;i<a.size();i++){
            JSONObject item = a.getJSONObject(i);
            treatJSON(record,item);
        }
    }
    public void treatJSON(AmzAdvSnapshot record, JSONObject item){
        if(item.getString("adProduct").equals("SPONSORED_DISPLAY")){
            treatSD(record,item);
        }else if(item.getString("adProduct").equals("SPONSORED_BRANDS")){
            treatSB(record,item);
        }else{
            treatSP(record,item);
        }
    }

    public abstract void treatSP(AmzAdvSnapshot record, JSONObject item)  ;

    public abstract void treatSB(AmzAdvSnapshot record, JSONObject item)  ;

    public abstract void treatSD(AmzAdvSnapshot record, JSONObject item) ;
}
