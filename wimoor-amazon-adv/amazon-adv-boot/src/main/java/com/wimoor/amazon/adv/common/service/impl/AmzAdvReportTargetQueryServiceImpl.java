package com.wimoor.amazon.adv.common.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.common.service.IAmzAdvReportTargetQueryService;
import com.wimoor.amazon.adv.sp.dao.AmzAdvReportTargetQueryMapper;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportTargetQuery;
import com.wimoor.amazon.adv.utils.AdvUtils;
import com.wimoor.amazon.base.BaseService;
import com.wimoor.common.GeneralUtil;

@Service("amzAdvReportTargetQueryService")
public class AmzAdvReportTargetQueryServiceImpl extends BaseService<AmzAdvReportTargetQuery> implements IAmzAdvReportTargetQueryService{
	@Resource
	AmzAdvReportTargetQueryMapper amzAdvReportTargetQueryMapper;

	public PageList<Map<String, Object>> getProductTargeQueryList(Map<String, Object> map, PageBounds pageBounds) {
		if(map.get("campaignType") != null) {
			String campaignType = map.get("campaignType").toString();
			if("HSA".equals(campaignType)) {
				return null;
			}
		}
		return amzAdvReportTargetQueryMapper.getProductTargeQueryList(map, pageBounds);
	}

	public Map<String, Object> getProductTargeQueryChart(Map<String, Object> map) {
		if(map.get("campaignType") != null) {
			String campaignType = map.get("campaignType").toString();
			if("HSA".equals(campaignType)) {
				return null;
			}
		}
		getSerchStr(map);
		List<Map<String,Object>> list = amzAdvReportTargetQueryMapper.getProductTargeQueryChart(map);
		return getChartData(list,null,map);
	}
	
	public Map<String, Object> getChartData(List<Map<String, Object>> list, List<Map<String, Object>> listHsa, Map<String, Object> map) {
		if (list == null && listHsa == null ) {
			return null;
		}
		String serch1 = (String) map.get("value1");
	
		Calendar c = Calendar.getInstance();
		Calendar cTime = Calendar.getInstance();
		String fromDate = (String) map.get("fromDate");
		String endDate = (String) map.get("endDate");
		if(fromDate!=null&&fromDate.contains("-")) {
			cTime.setTime(GeneralUtil.StringfromDate(fromDate, "yyyy-MM-dd"));
		}else {
			cTime.setTime(GeneralUtil.StringfromDate(fromDate, "yyyy/MM/dd"));
		}
		Date beginDateplus = null;
		beginDateplus = cTime.getTime();
		if(endDate!=null&&endDate.contains("-")) {
			cTime.setTime(GeneralUtil.StringfromDate(endDate, "yyyy-MM-dd"));
		}else {
			cTime.setTime(GeneralUtil.StringfromDate(endDate, "yyyy/MM/dd"));
		}
		Date endDateplus = null;
		endDateplus = cTime.getTime();
		List<String> listLabel = new ArrayList<String>();
		List<Object> listData1 = new ArrayList<Object>();
	 
		Map<String, Object> mapSp = new HashMap<String, Object>();
		Map<String, Object> mapHsa = new HashMap<String, Object>();
		Map<String, Object> mapSD = new HashMap<String, Object>();
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				String bydate = list.get(i).get("bydate") == null ? null : list.get(i).get("bydate").toString();
				String value = list.get(i).get(serch1) == null ? null : list.get(i).get(serch1).toString();
				 
				if (value == null) {
					mapSp.put(bydate, null);
				} else {
					mapSp.put(bydate, value );
				}
			}
		}
		if (listHsa != null) {
			for (int i = 0; i < listHsa.size(); i++) {
				String bydate = listHsa.get(i).get("bydate") == null ? null : listHsa.get(i).get("bydate").toString();
				String value = listHsa.get(i).get(serch1) == null ? null : listHsa.get(i).get(serch1).toString();
				if (value == null) {
					mapHsa.put(bydate, null);
				} else {
					mapHsa.put(bydate, value );
				}
			}
		}
		for (c.setTime(beginDateplus); AdvUtils.checkTimeType(map, c, beginDateplus,
				endDateplus); AdvUtils.step(map, c, beginDateplus, endDateplus)) {
			String tempkey = AdvUtils.getKeyByTimeType(map, c);
			String value = (String) mapSp.get(tempkey);
			String value2 = (String) mapHsa.get(tempkey);
			String value3 = (String) mapSD.get(tempkey);
			listLabel.add(tempkey);
			BigDecimal sp=new BigDecimal("0");
			BigDecimal sb=new BigDecimal("0");
			BigDecimal sd=new BigDecimal("0");
            if ( value != null) {
					sp=new BigDecimal(value);
			} 
			if ( value2 != null) {
					sb=new BigDecimal(value2);
			} 
			if (value3 != null) {
				sd=new BigDecimal(value3);
			} 
		    listData1.add(sp.add(sb).add(sd));
		}
		Map<String, Object> allmap = new HashMap<String, Object>();
		allmap.put("labels", listLabel);
		allmap.put("listdata1", listData1);
		return allmap;
	}
	
	public void getSerchStr(Map<String,Object> map) {
		String serch = (String) map.get("searchlist");
		String[] serchArray = serch.split(",");
		String serchlist = "";
		for (int i = 0; i < serchArray.length; i++) {
				if ("ACOS".equals(serchArray[i])) {
					serchlist = serchlist + "ifnull(sum(cost) / sum(attributedSales7d),0) ACOS ,";
				} else if ("ROAS".equals(serchArray[i])) {
					serchlist = serchlist + "ifnull(sum(attributedSales7d) / sum(cost),0) ROAS ,";
				} else if ("CSRT".equals(serchArray[i])) {
					serchlist = serchlist + "ifnull(sum(attributedConversions7d) / sum(clicks),0) CSRT ,";
				} else if ("avgcost".equals(serchArray[i])) {
					serchlist = serchlist + "ifnull((sum(cost) / sum(clicks)),0) avgcost ,";
				} else if ("CTR".equals(serchArray[i])) {
					serchlist = serchlist + "ifnull(sum(clicks) / sum(impressions),0) CTR ,";
				}else if ("sumUnits".equals(serchArray[i])) {
					 serchlist = serchlist +"sum(attributedUnitsOrdered7d) sumUnits,";
				} else {
					serchlist = serchlist +"sum(" + serchArray[i] + ") " + serchArray[i] + ",";
				}
		}
		if(serchlist.contains(",")) {
			serchlist=serchlist.substring(0, serchlist.length()-1);
		}
		map.put("serchlist", serchlist);
		map.put("value1", serchArray[0]);
	}
}
