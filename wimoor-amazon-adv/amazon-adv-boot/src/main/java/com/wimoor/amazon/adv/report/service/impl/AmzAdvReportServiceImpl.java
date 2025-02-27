package com.wimoor.amazon.adv.report.service.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.common.dao.AmzAdvAuthMapper;
import com.wimoor.amazon.adv.common.pojo.AmzAdvAuth;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.report.service.IAmzAdvReportService;
import com.wimoor.amazon.adv.report.service.IAmzAdvSumAllTypeService;
import com.wimoor.amazon.adv.sb.dao.AmzAdvReportCampaignsHsaMapper;
import com.wimoor.amazon.adv.sb.dao.AmzAdvReportCampaignsPlaceHsaMapper;
import com.wimoor.amazon.adv.sb.dao.AmzAdvReportKeywordsHsaMapper;
import com.wimoor.amazon.adv.sb.dao.AmzAdvReportKeywordsQueryHsaMapper;
import com.wimoor.amazon.adv.sb.dao.AmzAdvReportProductTargeHsaMapper;
import com.wimoor.amazon.adv.sp.dao.AmzAdvProductTargeMapper;
import com.wimoor.amazon.adv.sp.dao.AmzAdvReportAdgroupsMapper;
import com.wimoor.amazon.adv.sp.dao.AmzAdvReportAsinsMapper;
import com.wimoor.amazon.adv.sp.dao.AmzAdvReportCompaignsMapper;
import com.wimoor.amazon.adv.sp.dao.AmzAdvReportCompaignsPlaceMapper;
import com.wimoor.amazon.adv.sp.dao.AmzAdvReportKeywordsMapper;
import com.wimoor.amazon.adv.sp.dao.AmzAdvReportKeywordsQueryMapper;
import com.wimoor.amazon.adv.sp.dao.AmzAdvReportProductAdsMapper;
import com.wimoor.amazon.adv.sp.dao.AmzAdvReportTargetQueryMapper;
import com.wimoor.amazon.adv.utils.AdvUtils;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.GeneralUtil;

import tk.mybatis.mapper.util.StringUtil;

@Service("amzAdvReportService")
public class AmzAdvReportServiceImpl implements IAmzAdvReportService {
	@Resource
	AmzAdvReportAdgroupsMapper amzAdvReportAdgroupsMapper;
	@Resource
	AmzAdvReportAsinsMapper amzAdvReportAsinsMapper;
	@Resource
	AmzAdvReportCompaignsMapper amzAdvReportCompaignsMapper;
	@Resource
	AmzAdvReportCompaignsPlaceMapper amzAdvReportCompaignsPlaceMapper;
	@Resource
	AmzAdvReportKeywordsMapper amzAdvReportKeywordsMapper;
	@Resource
	AmzAdvReportKeywordsQueryMapper amzAdvReportKeywordsQueryMapper;
	@Resource
	AmzAdvReportProductAdsMapper amzAdvReportProductAdsMapper;
	@Resource
	AmzAdvReportCampaignsHsaMapper amzAdvReportCampaignsHsaMapper;
	@Resource
	AmzAdvReportCampaignsPlaceHsaMapper amzAdvReportCampaignsPlaceHsaMapper;
	@Resource
	AmzAdvReportKeywordsHsaMapper amzAdvReportKeywordsHsaMapper;
	@Resource
	AmzAdvProductTargeMapper amzAdvProductTargeMapper;
	@Resource
	AmzAdvReportTargetQueryMapper amzAdvReportTargetQueryMapper;
	@Resource
	AmzAdvReportProductTargeHsaMapper amzAdvReportProductTargeHsaMapper;
    @Resource
    IAmzAdvSumAllTypeService amzAdvSumAllTypeService;
    @Resource
    AmzAdvReportKeywordsQueryHsaMapper amzAdvReportKeywordsQueryHsaMapper;
    @Resource
    AmzAdvAuthMapper amzAdvAuthMapper;
    @Resource
    IAmzAdvAuthService amzAdvAuthService;
    
	public static final class DownloadReport {
		public static final String Campaign = "Campaign";
		public static final String Campaign_placement = "Campaign-placement";
		public static final String Keyword = "Targeting-Keyword";
		public static final String Keyword_query = "Search term-Keyword";
		public static final String productAd = "Advertised product";
		public static final String Asins = "Purchased product";
		public static final String Target = "Targeting-ProductTarget";
		public static final String Target_query = "Search term-ProductTarget";
		public static final String Sponsored_Products = "Sponsored Products";
		public static final String Sponsored_Brands = "Sponsored Brands";
	}

	public String parGetName(String fieldName) {
		if (null == fieldName || "".equals(fieldName)) {
			return null;
		}
		return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
	}

	public boolean checkGetMet(Method[] methods, String fieldGetMet) {
		for (Method met : methods) {
			if (fieldGetMet.equals(met.getName())) {
				return true;
			}
		}
		return false;
	}

	public Map<String, Object> getFieldObjectLinkMap(Object bean) {
		Class<?> cls = bean.getClass();
		Map<String, Object> valueMap = new LinkedHashMap<String, Object>();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		// 取出bean里的所有方法
		Method[] methods = cls.getDeclaredMethods();
		Field[] fields = cls.getDeclaredFields();
		for (Field field : fields) {
			try {
				String fieldGetName = parGetName(field.getName());
				if (!checkGetMet(methods, fieldGetName)) {
					continue;
				}
				Method fieldGetMet = cls.getMethod(fieldGetName, new Class[] {});
				Object fieldVal = fieldGetMet.invoke(bean, new Object[] {});
				if (!"opttime".equals(field.getName()) && !"campaignid".equals(field.getName()) && !"adid".equals(field.getName())
						&& !"adgroupid".equals(field.getName()) && !"keywordid".equals(field.getName()) && !"profileid".equals(field.getName())) {
					if ("bydate".equals(field.getName())) {
						String bydate = ((DateFormat) formatter).format(fieldVal);
						valueMap.put(field.getName(), bydate);
					} else {
						valueMap.put(field.getName(), fieldVal);
					}
				}
			} catch (Exception e) {
				continue;
			}
		}
		return valueMap;
	}

	public List<Map<String, Object>> getDate(Map<String, Object> param) {
		String reporttype = param.get("reporttype").toString();
		String campaigntype = param.get("campaigntype").toString();
		List<Map<String, Object>> list =null;
		if (DownloadReport.Sponsored_Products.equals(campaigntype)) {
			if (DownloadReport.Campaign.equals(reporttype)) {
				list= amzAdvReportCompaignsMapper.getCampaigns(param);
			} else if (DownloadReport.Campaign_placement.equals(reporttype)) {
				list = amzAdvReportCompaignsPlaceMapper.getCampaignsPlace(param);
			} else if (DownloadReport.Keyword.equals(reporttype)) {
				list = amzAdvReportKeywordsMapper.getKeywords(param);
			} else if (DownloadReport.Keyword_query.equals(reporttype)) {
				list = amzAdvReportKeywordsQueryMapper.getKeywordsQuery(param);
			} else if (DownloadReport.productAd.equals(reporttype)) { 
				list = amzAdvReportProductAdsMapper.getProductAds(param);
			} else if (DownloadReport.Asins.equals(reporttype)) { 
				list = amzAdvReportAsinsMapper.getAsins(param);
			} else if (DownloadReport.Target.equals(reporttype)) {
				list = amzAdvProductTargeMapper.getTargetReport(param);
			} else if (DownloadReport.Target_query.equals(reporttype)) {
				list = amzAdvReportTargetQueryMapper.getTargetQueryReport(param);
			}
		} else {
			if (DownloadReport.Campaign.equals(reporttype)) {
				list = amzAdvReportCampaignsHsaMapper.getCampaignsHsa(param);
			} else if (DownloadReport.Campaign_placement.equals(reporttype)) {
				list = amzAdvReportCampaignsPlaceHsaMapper.getCampaignsPlaceHsa(param);
			} else if(DownloadReport.Keyword_query.equals(reporttype)) {
				list = amzAdvReportKeywordsQueryHsaMapper.getKeywordsQueryHsa(param);
			} else if (DownloadReport.Keyword.equals(reporttype)) {
				list = amzAdvReportKeywordsHsaMapper.getKeywordsHsa(param);
			} else if (DownloadReport.Target.equals(reporttype)) {
				list = amzAdvReportProductTargeHsaMapper.getTargetHsaReport(param);
			}
		}
		return list;
	}

	public SXSSFWorkbook setExcelBook(Map<String, Object> param) {
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		List<Map<String, Object>> maplist = getDate(param);
 
		String campaigntype = param.get("campaigntype").toString();
		String groupname = param.get("groupname").toString();
		String region  = param.get("marketplacename").toString();
		String currency = param.get("currency").toString();
		if (maplist != null && maplist.size() > 0) {
			Sheet sheet = workbook.createSheet("sheet1");
			// 在索引0的位置创建行（最顶端的行）
			Row trow = sheet.createRow(0);
			// Set set = list.get(0).entrySet();
			Map<String, Object> linkmap = maplist.get(0);
			Object[] titlearray = linkmap.keySet().toArray();
			// Object[] titlearray = list.get(0).keySet().toArray();
			Cell cell = trow.createCell(0); // 在索引0的位置创建单元格(左上端)
			cell.setCellValue("campaigntype");
			cell = trow.createCell(1); // 在索引1的位置创建单元格(左上端)
			cell.setCellValue("groupname");
			cell = trow.createCell(2); // 在索引2的位置创建单元格(左上端)
			cell.setCellValue("region");
			cell = trow.createCell(3); // 在索引3的位置创建单元格(左上端)
			cell.setCellValue("currency");
			for (int i = 0; i < titlearray.length; i++) {
				cell = trow.createCell(i+4); // 在索引0的位置创建单元格(左上端)
				Object value = titlearray[i].toString();
				cell.setCellValue(value.toString());
			}
			for (int i = 0; i < maplist.size(); i++) {
				Row row = sheet.createRow(i + 1);
				Map<String, Object> map = maplist.get(i);
				cell = row.createCell(0); // 在索引0的位置创建单元格(左上端)
				cell.setCellValue(campaigntype);
				cell = row.createCell(1); // 在索引1的位置创建单元格(左上端)
				cell.setCellValue(groupname);
				cell = row.createCell(2); // 在索引2的位置创建单元格(左上端)
				cell.setCellValue(region);
				cell = row.createCell(3); // 在索引3的位置创建单元格(左上端)
				cell.setCellValue(currency);
				for (int j = 0; j < titlearray.length; j++) {
					cell = row.createCell(j+4); // 在索引0的位置创建单元格(左上端)
					Object value = map.get(titlearray[j].toString());
					if (value != null && StringUtil.isNotEmpty(value.toString())) {
						if(AdvUtils.isNumeric(value.toString()) || GeneralUtil.isDouble(value.toString())) {
							if("Click Thru Rate (CTR)".equals(titlearray[j].toString())  
									|| "Total Advertising Cost of Sales (ACoS)".equals(titlearray[j].toString())
									|| "7 Day Conversion Rate".equals(titlearray[j].toString())
									|| "14 Day Conversion Rate".equals(titlearray[j].toString())) {
								if(Double.parseDouble(value.toString()) == 0) {
									cell.setCellValue("");
								}else {
									BigDecimal bnumber = new BigDecimal(value.toString()).multiply(new BigDecimal("100"));
									double number = bnumber.setScale(4, RoundingMode.HALF_UP).doubleValue();
									cell.setCellValue(number + "%");
								}
							}else {
								cell.setCellValue(GeneralUtil.formatterNum(value));
							}
						}else {
							cell.setCellValue(value.toString());
						}
					}
				}
			}
		} else {
			throw new BaseException("没有数据可导出！");
		}
		return workbook;
	}
	
	public Map<String,Object> getTypeNumber(UserInfo user){
		return amzAdvSumAllTypeService.getTypeNumber(user.getCompanyid());
	}
	
	@Cacheable(value = "amzAdvProfileCache")
	public List<Map<String,Object>> getTop5(BigInteger profileid,String ftype){
		if("keywords".equals(ftype)) {
			return amzAdvReportKeywordsMapper.top5(profileid);	
		}else if("adgroups".equals(ftype)) {
			return amzAdvReportAdgroupsMapper.top5(profileid);
		}else {
			return amzAdvReportProductAdsMapper.top5(profileid);
		}
	}
	
	public Map<String, Object> getKeywordsWarningIndicator(Map<String,Object> map){
		String ftype = map.get("ftype").toString();
		if("co".equals(ftype)) {
			return amzAdvReportKeywordsMapper.getWarningIndicatorForCo(map);
		}
		else if("sequent".equals(ftype)) {
			return amzAdvReportKeywordsMapper.getWarningIndicatorForSequent(map);
		}else {
			return amzAdvReportKeywordsMapper.getWarningIndicatorForYesterday(map);
		}
	}
	
	public PageList<Map<String, Object>> getKeywordsWarningIndicator(Map<String,Object> map,PageBounds pagebounds) {
		return amzAdvReportKeywordsMapper.getWarningIndicatorDetail(map,pagebounds);
	}
	
	public Map<String, Object> getProductAdsWarningIndicator(Map<String,Object> map) {
		String ftype = map.get("ftype").toString();
		if("co".equals(ftype)) {
			return amzAdvReportProductAdsMapper.getWarningIndicatorForCo(map);
		}
		else if("sequent".equals(ftype)) {
			return amzAdvReportProductAdsMapper.getWarningIndicatorForSequent(map);
		}else {
			return amzAdvReportProductAdsMapper.getWarningIndicatorForYesterday(map);
		}
	}
	
	public PageList<Map<String, Object>> getProductAdsWarningIndicator(Map<String,Object> map,PageBounds pagebounds) {
		return amzAdvReportProductAdsMapper.getWarningIndicatorDetail(map,pagebounds);
	}

	public void refreshAmzadvertWarningData() {
		List<AmzAdvAuth> authlist = amzAdvAuthMapper.selectNotDisableList();
		for(int i=0;i<authlist.size();i++) {
			AmzAdvAuth auth = authlist.get(i);
			List<AmzAdvProfile> list = amzAdvAuthService.getProfileByAuth(auth.getId());
			if(list!=null&&list.size()>0) {
               for(int j=0;j<list.size();j++) {
                   AmzAdvProfile profile = list.get(j);       
                   Map<String, Object> param=new HashMap<String, Object>();
       			   param.put("profileid", profile.getId());
            	   amzAdvReportProductAdsMapper.refreshAdvertWarningData(param);
            	   amzAdvReportKeywordsMapper.refreshAdvertWarningData(param);
               }
			}
		}
	}
	
}
