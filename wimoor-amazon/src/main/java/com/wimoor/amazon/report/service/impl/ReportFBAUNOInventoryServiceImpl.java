package com.wimoor.amazon.report.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.amazon.spapi.api.ReportsApi;
import com.amazon.spapi.client.ApiCallback;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.reports.CreateReportResponse;
import com.amazon.spapi.model.reports.CreateReportSpecification;
import com.amazon.spapi.model.reports.ReportOptions;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.product.mapper.ProductInfoMapper;
import com.wimoor.amazon.product.pojo.entity.ProductInfo;
import com.wimoor.amazon.profit.service.impl.ProfitServiceImpl;
import com.wimoor.amazon.report.pojo.entity.ReportType;
import com.wimoor.amazon.util.AmzDateUtils;
import com.wimoor.common.GeneralUtil;

import lombok.extern.slf4j.Slf4j;
 

@Slf4j
@Service("reportFBAUNOInventoryService")
public class ReportFBAUNOInventoryServiceImpl extends ReportServiceImpl {
	@Resource
	private IMarketplaceService marketplaceService;
	@Resource
	private ProductInfoMapper productInfoMapper;

	
	 
	public void   requestReport(AmazonAuthority amazonAuthority,Calendar cstart,Calendar cend,Boolean ignore) {
		          amazonAuthority.setUseApi("createReport");
				  ReportsApi api = apiBuildService.getReportsApi(amazonAuthority);
				  List<Marketplace> marketlist = marketplaceService.findbyauth(amazonAuthority.getId());
				  for(Marketplace market:marketlist) {
					  CreateReportSpecification body=new CreateReportSpecification();
					  if(!Arrays.asList(ProfitServiceImpl.smlAndLightCountry).contains(market.getMarket())) {
						  continue;
					  }
					  body.setReportType(myReportType());
					  body.setDataStartTime(AmzDateUtils.getOffsetDateTimeUTC(cstart));
					  body.setDataEndTime(AmzDateUtils.getOffsetDateTimeUTC(cend));
					  ReportOptions reportOptions=getMyOptions();
					  if(reportOptions!=null) {
						body.setReportOptions(reportOptions);  
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
						  if(lastupdate!=null&&GeneralUtil.distanceOfHour(lastupdate, new Date())<6) {
							  continue;
						  }
					  }
					  body.setMarketplaceIds(list);
					  try {
							  ApiCallback<CreateReportResponse> callback = new ApiCallbackReportCreate(this,amazonAuthority,market,cstart.getTime(),cend.getTime());
						      api.createReportAsync(body,callback);
						  } catch (ApiException e) {
							  amazonAuthority.setApiRateLimit( null,e);
							  e.printStackTrace();
					    }
				  }
	     }
   
	
	/**
	 * SKU FNSKU ASIN Product Name Enrolled in SnL? Marketplace Your SnL Price
	 * Inventory in SnL FC (units) Inventory in Non-SnL FC (units)
	 */
	@SuppressWarnings("unused")
	public String treatResponse(AmazonAuthority amazonAuthority, BufferedReader br)  {
		String mlog = "";
		int lineNumber = 0;
		String line;
		try {
			while ((line = br.readLine()) != null) {
				String[] info = line.split("\t");
				int length = info.length;
				
				if (lineNumber != 0 && length > 1) {
					int i = 0;
					String sku = i < length ? info[i++] : null;
					String fnsku = i < length ? info[i++] : null;
					String asin = i < length ? info[i++] : null;
					String product_name = i < length ? info[i++] : null;
					String inSnl = i < length ? info[i++] : null;
					String marketplace = i < length ? info[i++] : null;// US,UK,JP
					String yourprice = i < length ? info[i++] : null;// 可能出现unsellable
					String inventory_SnL = i < length ? info[i++] : null;
					String inventory_Non_SnL = i < length ? info[i++] : null;
 
					Marketplace marketplaceEntity = marketplaceService.findMarketplaceByCountry(marketplace);
					if(lineNumber==1) {
					   productInfoMapper.clearInSnl(marketplaceEntity.getMarketplaceid(), amazonAuthority.getId());
					}
					if (marketplaceEntity != null) {
						List<ProductInfo> record = productInfoMapper.selectBySku(sku, marketplaceEntity.getMarketplaceid(), amazonAuthority.getId());
						if (record != null && record.size() > 0) {
							ProductInfo item = record.get(0);
							if (inSnl != null && "yes".equalsIgnoreCase(inSnl)) {
								if (item.getInSnl() != null && item.getInSnl()) {
									lineNumber++;
									continue;
								}
								item.setInSnl(true);
							} else {
								if (item.getInSnl() != null && !item.getInSnl()) {
									lineNumber++;
									continue;
								}
								item.setInSnl(false);
							}
							try {
								productInfoMapper.updateById(item);
							} catch (Exception e) {
								log.error(e.getMessage());
								mlog += "SKU:" + sku + "," + e.getClass() + "," + e.getMessage();
							} finally {
								item=null;
								record.clear();
								record=null;
							}
								
						}
					}
				}
				info=null;
				lineNumber++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 mlog=mlog+e.getMessage();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					mlog=mlog+e.getMessage();
				}
			}
	 
		}
		if (lineNumber <= 1) {
			 mlog=mlog+"没有正常下载到数据";
		}
		return mlog;
	}

	@Override
	public String myReportType() {
		return ReportType.FBAUNOInventoryReport;
	}

}
