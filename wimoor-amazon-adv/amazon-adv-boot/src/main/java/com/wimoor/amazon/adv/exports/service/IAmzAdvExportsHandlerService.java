package com.wimoor.amazon.adv.exports.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wimoor.amazon.adv.report.pojo.AmzAdvSnapshot;

public interface IAmzAdvExportsHandlerService {
    void treat(AmzAdvSnapshot record, JSONArray a);
    void treatJSON(AmzAdvSnapshot record, JSONObject item);
}
