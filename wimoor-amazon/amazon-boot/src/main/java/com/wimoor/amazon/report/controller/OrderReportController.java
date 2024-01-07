package com.wimoor.amazon.report.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.AmazonGroup;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IAmazonGroupService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.common.pojo.dto.SalesChartDTO;
import com.wimoor.amazon.common.pojo.vo.Chart;
import com.wimoor.amazon.common.pojo.vo.ChartLine;
import com.wimoor.amazon.common.service.ISummaryDataService;
import com.wimoor.amazon.product.pojo.entity.ProductInfo;
import com.wimoor.amazon.product.service.IProductInfoService;
import com.wimoor.amazon.util.ChartPoint;
import com.wimoor.amazon.util.ChartPoint.SumType;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@Api(tags = "订单接口")
//@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/report/order")
public class OrderReportController {

	final ISummaryDataService iSummaryDataService;
	 private final IAmazonGroupService iAmazonGroupService;
	 private final IMarketplaceService iMarketplaceService;
	 private final IAmazonAuthorityService iAmazonAuthorityService;
		@Autowired
		IProductInfoService iProductInfoService;
	public int returnCalendarDay(String field) {
		if ("last30".equals(field)) {
			return  30;
		}else  if("last60".equals(field)){
			return 60;
		}
		else if("last90".equals(field)){
			return 90;
		}else {
			return 180;
		}
	}
	
	@PostMapping("/salesLine")
	public Result<Chart> getSalesLineAction(@RequestBody SalesChartDTO dto) {
		UserInfo userinfo = UserInfoContext.get();
		if(StrUtil.isBlank(dto.getGroupid())) {
			dto.setGroupid(null);
		}
		Chart chart=new Chart();
		if(StrUtil.isNotBlank(dto.getSku()) ) {
			List<ChartLine> lines =new ArrayList<ChartLine>();
			ChartLine line= iSummaryDataService.findOrderSummaryBySku(dto.getGroupid(),dto.getAmazonAuthId(), dto.getSku(), dto.getMarketplaceid(), dto.getDaysize(),dto.getLineType(), userinfo);
			lines.add(line);
			chart.setLines(lines);
			Calendar end=Calendar.getInstance();
			end.add(Calendar.DATE,-1);
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, dto.getDaysize()*-1-1);
			chart.setLabels(ChartPoint.getLabels(SumType.Daily, c.getTime(), end.getTime()));
			chart.setLegends(Arrays.asList(dto.getSku()));
			return Result.success(chart);
		}else {
			List<ChartLine> lines =new LinkedList<ChartLine>();
			List<String> legends=new LinkedList<String>();
			List<ProductInfo> groupList = iProductInfoService.selectByMSku(dto.getMsku(),dto.getMarketplaceid(),dto.getGroupid(),userinfo.getCompanyid());
		    if(groupList==null) {
		    	return Result.success(chart);
		    }
		    if(dto.getMarketplaceid()!=null&&dto.getMarketplaceid().equals("EU")) {
		    	List<ProductInfo> infolist=new ArrayList<ProductInfo>();
		    	Set<String> groupset=new HashSet<String>();
		    	for(ProductInfo item:groupList) {
		    		String key=item.getSku()+item.getAmazonAuthId();
		    		 if(groupset.contains(key)) {
		    			 continue;
		    		 }else {
		    			 groupset.add(key);
		    			 infolist.add(item);
		    		 }
		    	}
		    	groupList=infolist;
		    }
			for(ProductInfo item:groupList) {
		    	   AmazonAuthority auth = iAmazonAuthorityService.getById(item.getAmazonAuthId());
		    	   AmazonGroup amzstore = iAmazonGroupService.getById(auth.getGroupid());
		    	   ChartLine line= iSummaryDataService.findOrderSummaryBySku(auth.getGroupid(),item.getAmazonAuthId().toString(), item.getSku(), dto.getMarketplaceid(), dto.getDaysize(),dto.getLineType(), userinfo);
				   lines.add(line);
				   String groupname=amzstore.getName();
				   String marketname=null;
					if(!dto.getMarketplaceid().equals("EU")) {
						Marketplace market = iMarketplaceService.getById(dto.getMarketplaceid());
						if(market!=null) {
							marketname=market.getName();
						}
					}
					if(marketname==null) {
						marketname=dto.getMarketplaceid();
					}
				   if(dto.getGroupid()==null) {
					   String legend = dto.getSku()+"-"+groupname;
					   legends.add(legend);
				   }else {
					   legends.add(dto.getSku());
				   }
		         }
				Calendar end=Calendar.getInstance();
				end.add(Calendar.DATE,-1);
				Calendar c = Calendar.getInstance();
				c.add(Calendar.DATE, dto.getDaysize()*-1-1);
				chart.setLabels(ChartPoint.getLabels(SumType.Daily, c.getTime(), end.getTime()));
				chart.setLines(lines);
				chart.setLegends(legends);
				return Result.success(chart);
		}
	}
	
 
}
