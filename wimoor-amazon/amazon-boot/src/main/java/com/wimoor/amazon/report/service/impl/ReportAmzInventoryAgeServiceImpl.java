package com.wimoor.amazon.report.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.amazon.spapi.api.ReportsApi;
import com.amazon.spapi.client.ApiCallback;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.reports.CreateReportResponse;
import com.amazon.spapi.model.reports.CreateReportSpecification;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.inventory.mapper.AmzRptInventoryAgeMapper;
import com.wimoor.amazon.inventory.pojo.entity.AmzRptInventoryAge;
import com.wimoor.amazon.report.pojo.entity.ReportType;
import com.wimoor.amazon.report.service.IReportAmzInventoryAgeService;
import com.wimoor.amazon.util.AmzDateUtils;
import com.wimoor.amazon.util.ReportFieldUtil;
import com.wimoor.common.GeneralUtil;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
 
@Slf4j
@Service("reportAmzInventoryAgeService")
public class ReportAmzInventoryAgeServiceImpl extends ReportServiceImpl  implements IReportAmzInventoryAgeService {
 
	@Resource
	private AmzRptInventoryAgeMapper amzRptInventoryAgeMapper;
	 
	public  void   requestReport(AmazonAuthority amazonAuthority,Calendar cstart,Calendar cend,Boolean ignore) {
		  ReportsApi api = apiBuildService.getReportsApi(amazonAuthority);
		  List<Marketplace> marketlist = marketplaceService.findbyauth(amazonAuthority.getId());
		  boolean iseu=false;
		  for(Marketplace market:marketlist) {
			  CreateReportSpecification body=new CreateReportSpecification();
			  body.setReportType(myReportType());
			  body.setDataStartTime(AmzDateUtils.getOffsetDateTimeUTC(cstart));
			  body.setDataEndTime(AmzDateUtils.getOffsetDateTimeUTC(cend));
			  if(market.getRegion().equals("EU")) {
				  if(iseu) {
					  return;
				  }else {
					  iseu=true;
				  }
			  }
			  List<String> list=new ArrayList<String>();
			  list.add(market.getMarketplaceid());
			  amazonAuthority.setMarketPlace(market);
			  if(ignore==null||ignore==false) {
				  Map<String,Object> param=new HashMap<String,Object>();
				  param.put("sellerid", amazonAuthority.getSellerid());
				  param.put("reporttype", this.myReportType());
				  param.put("marketplacelist", list);
				  Date lastupdate= iReportRequestRecordService.lastUpdateRequestByType(param);  
				  if(lastupdate!=null&&GeneralUtil.distanceOfHour(lastupdate, new Date())<5) {
					  continue;
				  }
			  }
			  body.setMarketplaceIds(list);
			  try {
					  ApiCallback<CreateReportResponse> response = new ApiCallbackReportCreate(this,amazonAuthority,market,cstart.getTime(),cend.getTime());
				      api.createReportAsync(body,response);
				  } catch (ApiException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			    }
		  }
}
	/**
	 * afn-researching-quantity	afn-reserved-future-supply	afn-future-supply-buyable
	 */
	public synchronized String treatResponse(AmazonAuthority amazonAuthority, BufferedReader br)  {
		String mlog="";
		int lineNumber = 0;
		Map<String,Integer> titleList = new HashMap<String,Integer>();
		AmzRptInventoryAge record = null;
		String line;
		try {
			while ((line = br.readLine()) != null) {
				String[] info = line.split("\t");
				if (lineNumber == 0) {
					for (int i = 0; i < info.length; i++) {
						titleList.put(info[i],i);
					}
					System.out.println(line);
				}else {
					record=new AmzRptInventoryAge();
					record.setSku(GeneralUtil.getStrValue(info,titleList,"sku"));
					DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd",Locale.CHINA);
					String snapshot = ReportFieldUtil.getStrValue(info,titleList,"snapshot-date");
					LocalDateTime sp = LocalDate.parse(snapshot,df).atStartOfDay();
					record.setSnapshot(sp);
					record.setMarketplace(ReportFieldUtil.getStrValue(info,titleList,"marketplace"));
					String marketname=record.getMarketplace();
					record.setAsin(ReportFieldUtil.getStrValue(info,titleList,"asin"));
					record.setFnsku(ReportFieldUtil.getStrValue(info,titleList,"fnsku"));
					record.setFcondition(ReportFieldUtil.getStrValue(info,titleList,"condition"));
					record.setQuantity(ReportFieldUtil.getIntegerValue(info,titleList,"avaliable-quantity(sellable)"));
					record.setQtyWithRemovalsInProgress(ReportFieldUtil.getIntegerValue(info, titleList, "qty-with-removals-in-progress"));
					record.setInvAge0To90Days(ReportFieldUtil.getIntegerValue(info, titleList, "inv-age-0-to-90-days"));
					record.setInvAge91To180Days(ReportFieldUtil.getIntegerValue(info, titleList, "inv-age-91-to-180-days"));
					record.setInvAge181To270Days(ReportFieldUtil.getIntegerValue(info, titleList, "inv-age-181-to-270-days"));
					record.setInvAge271To365Days(ReportFieldUtil.getIntegerValue(info, titleList, "inv-age-271-to-365-days"));	
					record.setInvAge365PlusDays(ReportFieldUtil.getIntegerValue(info, titleList, "inv-age-365-plus-days"));	
					record.setCurrency(GeneralUtil.getStrValue(info,titleList,"currency"));
					String currency=record.getCurrency();
					record.setQtyToBeChargedLtsf6Mo(ReportFieldUtil.getIntegerValue(info, titleList, "qty-to-be-charged-ltsf-6-mo"));	
					record.setQtyToBeChargedLtsf12Mo(ReportFieldUtil.getIntegerValue(info, titleList, "qty-to-be-charged-ltsf-12-mo"));	
					record.setProjectedLtsf6Mo(ReportFieldUtil.getBigDecimalValue(info, titleList, "projected-ltsf-6-mo"));	
					record.setProjectedLtsf12Mo(ReportFieldUtil.getBigDecimalValue(info, titleList, "projected-ltsf-12-mo"));	
					record.setUnitsShippedLast7Days(ReportFieldUtil.getIntegerValue(info, titleList, "units-shipped-last-7-days"));	
					record.setUnitsShippedLast30Days(ReportFieldUtil.getIntegerValue(info, titleList, "units-shipped-last-30-days"));
					record.setUnitsShippedLast30Days(ReportFieldUtil.getIntegerValue(info, titleList, "units-shipped-last-60-days"));
					record.setUnitsShippedLast30Days(ReportFieldUtil.getIntegerValue(info, titleList, "units-shipped-last-90-days"));
					record.setYourPrice(ReportFieldUtil.getBigDecimalValue(info, titleList, "your-price"));	
					record.setSalesPrice(ReportFieldUtil.getBigDecimalValue(info, titleList, "sales_price"));	
					record.setLowestPriceNew(ReportFieldUtil.getBigDecimalValue(info, titleList, "lowest_price_new"));	
					record.setLowestPriceUsed(ReportFieldUtil.getBigDecimalValue(info, titleList, "lowest_price_used"));
					record.setRecommendedAction(ReportFieldUtil.getStrValue(info, titleList, "Recommended action"));
					record.setDays(ReportFieldUtil.getIntegerValue(info, titleList, "Healthy Inventory Level	Recommended sales price	Recommended sale duration (days)"));
					record.setRecommendedAction(ReportFieldUtil.getStrValue(info, titleList, "Recommended Removal Quantity"));
					record.setEstimatedCostSavingsOfRecommendedActions(ReportFieldUtil.getBigDecimalValue(info, titleList, "estimated-cost-savings-of-recommended-actions"));
					record.setSellThrough(ReportFieldUtil.getBigDecimalValue(info, titleList, "sell-through"));
					record.setItemVolume(ReportFieldUtil.getBigDecimalValue(info, titleList, "item-volume"));
					record.setVolumeUnits(ReportFieldUtil.getStrValue(info, titleList, "volume-units"));
					record.setStorageType(ReportFieldUtil.getStrValue(info, titleList, "storage-type"));
					record.setAuthid(new BigInteger(amazonAuthority.getId()));
					if (StrUtil.isEmpty(marketname) && StrUtil.isNotEmpty(currency)) {
						// 此处考虑部分用户，没有在我们系统绑定对应国家，但是却有对应店铺，系统抓取的时候该国家的数据也会过来。
						// 而且有很多数据上面是没有marketplace point信息的。所以此处只能写成静态的根据币种转换。
						if ("USD".equals(currency)) {
							marketname = "Amazon.com";
						} else if ("CAD".equals(currency)) {
							marketname = "Amazon.ca";
						} else if ("GBP".equals(currency)) {
							marketname = "Amazon.co.uk";
						} else if ("INR".equals(currency)) {
							marketname = "Amazon.in";
						} else if ("JPY".equals(currency)) {
							marketname = "Amazon.co.jp";
						} else if ("AUD".equals(currency)) {
							marketname = "Amazon.com.au";
						} else if ("MXN".equals(currency)) {
							marketname = "Amazon.com.mx";
						} else if ("AED".equals(currency)) {
							marketname = "Amazon.ae";
						}else if ("SAR".equals(currency)) {
							marketname = "Amazon.sa";
						}else if("PLN".equals(currency)) {
							marketname = "Amazon.pl";
						}else if("SEK".equals(currency)) {
							marketname = "Amazon.se";
						}else if ("EUR".equals(currency)) {
							List<Marketplace> market = marketplaceService.findbyauth(amazonAuthority.getId());
							for (Marketplace item : market) {
								if (currency != null && currency.equals(item.getCurrency())) {
									marketname = item.getPointName();
									break;
								}
							}
							if (StrUtil.isEmpty(marketname)) {
								if (amazonAuthority.getMarketPlace().getCurrency().equals(currency)) {
									marketname = amazonAuthority.getMarketPlace().getPointName();
								} else {
									marketname = "Amazon.co.uk";
								}
							}
						}
						record.setMarketplace(marketname);
					}
					QueryWrapper<AmzRptInventoryAge> query=new QueryWrapper<AmzRptInventoryAge>();
					query.eq("authid", amazonAuthority.getId());
					query.eq("marketplace", record.getMarketplace());
					query.eq("sku", record.getSku());
					query.eq("fcondition", record.getFcondition());
                    AmzRptInventoryAge old = amzRptInventoryAgeMapper.selectOne(query);
                    if(old!=null) {
                    	record.setId(old.getId());
                    	amzRptInventoryAgeMapper.updateById(record);
                    }else {
                    	amzRptInventoryAgeMapper.insert(record);
                    }
				}
				lineNumber++;
			}
	
			  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.info("ReportAmzInventoryService:"+e.getMessage());
			e.printStackTrace();
		}  finally {
				if(br!=null) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
	}
      return mlog;
	}
	

  

	@Override
	public String myReportType() {
		return  ReportType.InventoryAge;
	}

 
	
}
