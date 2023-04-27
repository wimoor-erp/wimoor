package com.wimoor.amazon.finances.service.impl;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.common.service.IExchangeRateHandlerService;
import com.wimoor.amazon.finances.mapper.AmzSettlementAccStatementMapper;
import com.wimoor.amazon.finances.pojo.entity.AmzSettlementAccStatement;
import com.wimoor.amazon.finances.service.IAmzSettlementAccStatementService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;
@Service
public class AmzSettlementAccStatementImpl extends ServiceImpl<AmzSettlementAccStatementMapper, AmzSettlementAccStatement> implements IAmzSettlementAccStatementService {
   @Autowired
   IExchangeRateHandlerService exchangeRateHandlerService;
 	@Override
 	public Map<String, Object> saveFinStatement(UserInfo user,Map<String, Object> map, List<Map<String, Object>> list, Map<String, String> field) {
 		Map<String, Object> res=new HashMap<String, Object>();
 		if(list!=null && list.size()>0) {
 			int result=0;
 		    List<Object> myList=new LinkedList<Object>();
             for(Map<String, Object> item:list) {
             	myList.add(item);
             }
 			JSONArray json =new JSONArray(myList);
 			String jsonstr = json.toString();
 			AmzSettlementAccStatement record=new AmzSettlementAccStatement();
 		    try {
 				record.setListdata(jsonstr.getBytes("utf-8"));
 			} catch (UnsupportedEncodingException e1) {
 				 // TODO Auto-generated catch block
 				e1.printStackTrace();
 			}
 		    String currency = map.get("currency").toString();
 			record.setCurrency(currency);
 			record.setShopid(new BigInteger(user.getCompanyid()));
 			
 			if(map.get("groupid")==null||"all".equals(map.get("groupid").toString())) {
 				record.setGroupid(null);
 			}else {
 				record.setGroupid(new BigInteger(map.get("groupid").toString()));
 			}
 			if(map.get("marketplaceid")==null||"all".equals(map.get("marketplaceid").toString())) {
 				record.setMarketplaceid(null);
 			}else {
 				record.setMarketplaceid(map.get("marketplaceid").toString());
 			}
 			record.setOperator(new BigInteger(user.getId()));
 			record.setOpttime(new Date());
 			Map<String, Object> sumdata=(Map<String, Object>)map.get("summarydata");
 			JSONObject sumjson =new JSONObject(sumdata);
 			 try {
 				record.setSummaryall(sumjson.toString().getBytes("utf-8"));
 			} catch (UnsupportedEncodingException e) {
 				 // TODO Auto-generated catch block
 				e.printStackTrace();
 			}
 			 Map<String, Object> fielddata=new HashMap<String,Object>();
 			 for(Entry<String, String> entry:field.entrySet()) {
 				 fielddata.put(entry.getKey(),entry.getValue());
 			 }
 		
 			JSONObject fieldjson =new JSONObject(fielddata);
 			record.setFielddata(fieldjson.toString());
 			 
 			record.setStartdate(GeneralUtil.getDatez(map.get("fromDate").toString()));
 			record.setEnddate(GeneralUtil.getDatez(map.get("endDate").toString()));
 			if(map.get("datetype")==null) {
 				record.setDatetype(null);
 			}else {
 				record.setDatetype(map.get("datetype").toString());
 			}
 			Map<String,Object> param=new HashMap<String,Object>();
 			param.put("groupid", record.getGroupid());
 			param.put("marketplaceid", record.getMarketplaceid());
 			param.put("startdate", record.getStartdate());
 			param.put("enddate", record.getEnddate());
 			param.put("shopid", record.getShopid());
 			System.out.println(record.getCurrency());
 			List<Map<String,Object>> mlist=this.baseMapper.existByKey(param);
 			if(mlist.size()>0) {
 				throw new BizException("您的日期区间存在已经结账的记录。");
 			}else {
 				result+=this.baseMapper.insert(record);
 			}
 			if(result>0) {
 				res.put("code", "isok");
 				res.put("msg", "操作成功!");		
 			}else {
 				res.put("code", "isfail");
 				res.put("msg", "操作失败!");
 			}
 		}else {
 			res.put("code", "isfail");
 			res.put("msg", "暂无匹配记录!");
 		}
 		return res;
 	}
 	
 	
	@Override
	public List<Map<String, Object>> findAmzSettlementAccStatement(String shopid) {
		return this.baseMapper.findAll(shopid);
	}

	@Override
	public Integer deleteAmzSettlementAccStatement(String id) {
		return this.baseMapper.deleteById(new BigInteger(id));
	}
	
	@Override
	public AmzSettlementAccStatement findCommodityStatement(String id) {
		return this.baseMapper.selectById(new BigInteger(id));
	}
}

