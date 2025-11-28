package com.wimoor.amazon.adv.common.dao;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.pojo.SummaryObject;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportProductAdsSD;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportProductAdsSDAttributed;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportProductAds;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportProductAdsAttributed;
 
public class AmazonReportAdvSummaryAdsMap {
	Map<String, Map<String, SummaryObject>> summarymap = null;

	public AmazonReportAdvSummaryAdsMap() {
		summarymap = new HashMap<String, Map<String, SummaryObject>>();
	}

	public void addAdvReportSummary(AmzAdvProfile profile,AmzAdvReportProductAds padv, AmzAdvReportProductAdsAttributed padvattr,String asin,String sku,String currency) {
		double ctr = 0;
		double acos = 0;
		BigDecimal cpc = new BigDecimal("0");
		double roas = 0;
		double spc = 0.0d;
		Integer orders = padvattr.getAttributedconversions7d();
		Integer units = padvattr.getAttributedunitsordered7d();
		////// ---------------------------------------------------------------------
		String mainKey = profile.getSellerid() + "#" + padv.getBydate().getTime();
		Map<String, SummaryObject> mainmap = summarymap.get(mainKey);
		if (mainmap == null) {
			mainmap = new HashMap<String, SummaryObject>();
			summarymap.put(mainKey, mainmap);
		}
		String subKey = profile.getMarketplaceid() + "#" + sku;
		SummaryObject summaryobject = mainmap.get(subKey);
		if (summaryobject == null) {
			summaryobject = new SummaryObject();
			mainmap.put(subKey, summaryobject);
		}
		Integer count = 0;
		if (summaryobject.getCount() == null) {
			count = 0;
		} else {
			count = summaryobject.getCount();
		}
		
		int totalImpressions = summaryobject.getImpressions() + padv.getImpressions();
		Integer totalClicks = summaryobject.getClicks() + padv.getClicks();
		BigDecimal totalSpend = summaryobject.getSpend().add(padv.getCost());
		BigDecimal totalSales = summaryobject.getTotalsales().add(padvattr.getAttributedsales7d());
		
		if (totalImpressions != 0) {
			ctr = (totalClicks) / (totalImpressions * 1.0);// CTR
		}
		if (totalClicks != 0) {
			cpc = totalSpend.divide(new BigDecimal(totalClicks.toString()), 4, RoundingMode.HALF_DOWN);// CPC
		}
		if (totalClicks != 0) {
			spc = (totalSales.doubleValue() / totalClicks);
		}
		if (new BigDecimal("0").compareTo(totalSales) != 0) {
			acos = (totalSpend.divide(totalSales, 4, RoundingMode.HALF_DOWN).doubleValue());
		}
		if (new BigDecimal("0").compareTo(totalSpend) != 0) {
			roas = (totalSales.divide(totalSpend, 4, RoundingMode.HALF_DOWN).doubleValue());
		}
		
		summaryobject.setAcos(acos);
		summaryobject.setCpc(cpc);
		summaryobject.setCtr(ctr);
		summaryobject.setRoas(roas);
		summaryobject.setSpc(spc);
		
		summaryobject.setAsin(asin);
		summaryobject.setClicks(totalClicks);
		summaryobject.setCurrency(currency);
		summaryobject.setImpressions(totalImpressions);
		summaryobject.setSpend(totalSpend);
		summaryobject.setTotalsales(totalSales);
		if (orders != null) {
			summaryobject.setOrders(summaryobject.getOrders() + orders);
		}
		if (units != null) {
			summaryobject.setUnits(summaryobject.getUnits() + units);
		}
		summaryobject.setCount(count += 1);
	}

	public Map<String, Map<String, SummaryObject>> getSummarymap() {
		return summarymap;
	}

	public void setSummarymap(Map<String, Map<String, SummaryObject>> summarymap) {
		this.summarymap = summarymap;
	}

	public void addAdvReportSummary(AmzAdvProfile profile, AmzAdvReportProductAdsSD padv, AmzAdvReportProductAdsSDAttributed padvattr,String sku,String asin,String currency) {
		// TODO Auto-generated method stub
		double ctr = 0;
		double acos = 0;
		BigDecimal cpc = new BigDecimal("0");
		double roas = 0;
		double spc = 0.0d;
		Integer orders = padvattr.getAttributedconversions7d();
		Integer units = padvattr.getAttributedunitsordered7d();
		////// ---------------------------------------------------------------------
		String mainKey = profile.getSellerid() + "#" + padv.getBydate().getTime();
		Map<String, SummaryObject> mainmap = summarymap.get(mainKey);
		if (mainmap == null) {
			mainmap = new HashMap<String, SummaryObject>();
			summarymap.put(mainKey, mainmap);
		}
		String subKey = profile.getMarketplaceid() + "#" + sku;
		SummaryObject summaryobject = mainmap.get(subKey);
		if (summaryobject == null) {
			summaryobject = new SummaryObject();
			mainmap.put(subKey, summaryobject);
		}
		Integer count = 0;
		if (summaryobject.getCount() == null) {
			count = 0;
		} else {
			count = summaryobject.getCount();
		}
		
		int totalImpressions = summaryobject.getImpressions() + padv.getImpressions();
		Integer totalClicks = summaryobject.getClicks() + padv.getClicks();
		BigDecimal totalSpend = summaryobject.getSpend().add(padv.getCost());
		BigDecimal totalSales = summaryobject.getTotalsales().add(padvattr.getAttributedsales7d());
		
		if (totalImpressions != 0) {
			ctr = (totalClicks) / (totalImpressions * 1.0);// CTR
		}
		if (totalClicks != 0) {
			cpc = totalSpend.divide(new BigDecimal(totalClicks.toString()), 4, RoundingMode.HALF_DOWN);// CPC
		}
		if (totalClicks != 0) {
			spc = (totalSales.doubleValue() / totalClicks);
		}
		if (new BigDecimal("0").compareTo(totalSales) != 0) {
			acos = (totalSpend.divide(totalSales, 4, RoundingMode.HALF_DOWN).doubleValue());
		}
		if (new BigDecimal("0").compareTo(totalSpend) != 0) {
			roas = (totalSales.divide(totalSpend, 4, RoundingMode.HALF_DOWN).doubleValue());
		}
		
		summaryobject.setAcos(acos);
		summaryobject.setCpc(cpc);
		summaryobject.setCtr(ctr);
		summaryobject.setRoas(roas);
		summaryobject.setSpc(spc);
		
		summaryobject.setAsin(asin);
		summaryobject.setClicks(totalClicks);
		summaryobject.setCurrency(currency);
		summaryobject.setImpressions(totalImpressions);
		summaryobject.setSpend(totalSpend);
		summaryobject.setTotalsales(totalSales);
		if (orders != null) {
			summaryobject.setOrders(summaryobject.getOrders() + orders);
		}
		if (units != null) {
			summaryobject.setUnits(summaryobject.getUnits() + units);
		}
		summaryobject.setCount(count += 1);
	}

}
