package com.wimoor.amazon.report.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
		Chart chart=new Chart();
		List<ChartLine> lines =new ArrayList<ChartLine>();
		ChartLine line= iSummaryDataService.findOrderSummaryBySku(dto.getGroupid(),dto.getAmazonAuthId(), dto.getSku(), dto.getMarketplaceid(), dto.getDaysize(), userinfo);
		lines.add(line);
		chart.setLines(lines);
		Calendar c = Calendar.getInstance();
		Calendar end=Calendar.getInstance();
		end.add(Calendar.DATE, dto.getDaysize()-1);
		chart.setLabels(ChartPoint.getLabels(SumType.Daily, c.getTime(), end.getTime()));
		chart.setLegends(Arrays.asList(dto.getSku()));
		return Result.success(chart);
	}
	
	@GetMapping("/salesMutiLine")
	public Result<Chart> getSalesChartAction(@RequestBody List<SalesChartDTO> dtos) {
		UserInfo userinfo = UserInfoContext.get();
		Chart chart=new Chart();
		List<ChartLine> lines =new LinkedList<ChartLine>();
		List<String> legends=new LinkedList<String>();
		if(dtos!=null&&dtos.size()>0) {
			for(SalesChartDTO dto:dtos) {
				   List<ProductInfo> groupList = iProductInfoService.selectByMSku(dto.getMsku(),dto.getMarketplaceid(),dto.getGroupid(),userinfo.getCompanyid());
			       for(ProductInfo item:groupList) {
			    	   ChartLine line= iSummaryDataService.findOrderSummaryBySku(dto.getGroupid(),item.getAmazonAuthId().toString(), item.getSku(), dto.getMarketplaceid(), dto.getDaysize(), userinfo);
						lines.add(line);
						AmazonGroup amzstore = iAmazonGroupService.getById(dto.getGroupid());
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
						String legend = dto.getSku()+"-"+groupname;
						legends.add(legend);
			       }
				
			}
			Calendar c = Calendar.getInstance();
			Calendar end=Calendar.getInstance();
			end.add(Calendar.DATE, dtos.get(0).getDaysize()-1);
			chart.setLabels(ChartPoint.getLabels(SumType.Daily, c.getTime(), end.getTime()));
			chart.setLines(lines);
			chart.setLegends(legends);
			return Result.success(chart);
	  }else {
		  return Result.success();
	 }
	}
}
