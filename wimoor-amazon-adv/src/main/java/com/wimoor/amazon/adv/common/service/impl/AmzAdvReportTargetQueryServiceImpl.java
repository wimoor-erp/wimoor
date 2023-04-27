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
		if(list == null && listHsa == null) {
			return null;
		}
		String serch1 = (String) map.get("value1");
		String serch2 = (String) map.get("value2");
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
		List<Object> listData2 = new ArrayList<Object>();
		Map<String, Object> mapSp = new HashMap<String, Object>();
		Map<String, Object> mapHsa = new HashMap<String, Object>();
		if(list != null) {
			for (int i = 0; i < list.size(); i++) {
				String bydate = list.get(i).get("bydate") == null? null : list.get(i).get("bydate").toString();
				String value = list.get(i).get(serch1) == null? null : list.get(i).get(serch1).toString();
				String value2 = list.get(i).get(serch2) == null? null : list.get(i).get(serch2).toString();
				if(value == null && value2 == null) {
					mapSp.put(bydate, null);
				}else {
					mapSp.put(bydate, value + "#" + value2);
				}
			}
		}
		if(listHsa != null) {
			for (int i = 0; i < listHsa.size(); i++) {
				String bydate = listHsa.get(i).get("bydate") == null? null :  listHsa.get(i).get("bydate").toString();
				String value = listHsa.get(i).get(serch1) == null? null : listHsa.get(i).get(serch1).toString();
				String value2 = listHsa.get(i).get(serch2) == null? null : listHsa.get(i).get(serch2).toString();
				if(value == null && value2 == null) {
					mapHsa.put(bydate, null);
				}else {
					mapHsa.put(bydate, value + "#" + value2);
				}
			}
		}
		for (c.setTime(beginDateplus); AdvUtils.checkTimeType(map, c, beginDateplus,
				endDateplus); AdvUtils.step(map, c, beginDateplus, endDateplus)) {
			String tempkey = AdvUtils.getKeyByTimeType(map, c);
			String value = (String) mapSp.get(tempkey);
			String value2 = (String) mapHsa.get(tempkey);
			listLabel.add(tempkey);
			if (value == null && value2 == null) {
				listData1.add(new BigDecimal("0"));
				listData2.add(new BigDecimal("0"));
			} 
			else if (value == null && value2 != null) {
				String[] valueArray = value2.split("#");
				if(valueArray[0] == null) {
					listData1.add(new BigDecimal("0"));
				}else {
					listData1.add(new BigDecimal(valueArray[0]));
				}
				if(valueArray[1] == null) {
					listData2.add(new BigDecimal("0"));
				}else {
					listData2.add(new BigDecimal(valueArray[1]));
				}
			} 
			else if (value2 == null && value != null) {
				String[] valueArray = value.split("#");
				if(valueArray[0] == null) {
					listData1.add(new BigDecimal("0"));
				}else {
					listData1.add(new BigDecimal(valueArray[0]));
				}
				if(valueArray[1] == null) {
					listData2.add(new BigDecimal("0"));
				}else {
					listData2.add(new BigDecimal(valueArray[1]));
				}
			} 
			else if (value2 != null && value != null) {
				String[] valueArray = value.split("#");
				String[] valueArray2 = value2.split("#");
				listData1.add(new BigDecimal(valueArray[0]).add(new BigDecimal(valueArray2[0])));
				listData2.add(new BigDecimal(valueArray[1]).add(new BigDecimal(valueArray2[1])));
			}
		}
		Map<String, Object> allmap = new HashMap<String, Object>();
		allmap.put("labels", listLabel);
		allmap.put("listdata1", listData1);
		allmap.put("listdata2", listData2);
		return allmap;
	}
	
	public void getSerchStr(Map<String,Object> map) {
		String serch = (String) map.get("serchlist");
		String[] serchArray = serch.split(",");
		String serchlist = "";
		for (int i = 0; i < serchArray.length; i++) {
			if (i > 0) {
				if ("ACOS".equals(serchArray[i])) {
					String HSAcsrt = serchlist + "ifnull(sum(cost) / sum(attributedSales14d),0) ACOS ";
					map.put("HSAserchlist", HSAcsrt.replace("7", "14"));
					serchlist = serchlist + "ifnull(sum(cost) / sum(attributedSales7d),0) ACOS ";
					map.put("serchlist", serchlist);
				} else if ("ROAS".equals(serchArray[i])) {
					String HSAcsrt = serchlist + "ifnull(sum(attributedSales14d) / sum(cost),0) ROAS ";
					map.put("HSAserchlist", HSAcsrt.replace("7", "14"));
					serchlist = serchlist + "ifnull(sum(attributedSales7d) / sum(cost),0) ROAS ";
					map.put("serchlist", serchlist);
				} else if ("CSRT".equals(serchArray[i])) {
					String HSAcsrt = serchlist + "ifnull(sum(attributedConversions14d) / sum(clicks),0) CSRT ";
					map.put("HSAserchlist", HSAcsrt.replace("7", "14"));
					serchlist = serchlist + "ifnull(sum(attributedConversions7d) / sum(clicks),0) CSRT ";
					map.put("serchlist", serchlist);
				} else if ("avgcost".equals(serchArray[i])) {
					serchlist = serchlist + "ifnull((sum(cost) / sum(clicks)),0) avgcost ";
					map.put("HSAserchlist", serchlist.replace("7", "14"));
					map.put("serchlist", serchlist);
				} else if ("CTR".equals(serchArray[i])) {
					serchlist = serchlist + "ifnull(sum(clicks) / sum(impressions),0) CTR ";
					map.put("HSAserchlist", serchlist.replace("7", "14"));
					map.put("serchlist", serchlist);
				}
			} else {
				if ("sumUnits".equals(serchArray[i])) {
					serchlist = "sum(attributedUnitsOrdered7d) sumUnits,";
				} else {
					serchlist = "sum(" + serchArray[i] + ") " + serchArray[i] + ",";
				}
			}
		}
		map.put("value1", serchArray[0]);
		map.put("value2", serchArray[1]);
	}
}
