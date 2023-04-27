package com.wimoor.amazon.common.contorller;
 
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.amazon.common.pojo.dto.SummaryMutilParameterQueryDTO;
import com.wimoor.amazon.common.pojo.dto.UserSalesRankQueryDTO;
import com.wimoor.amazon.common.pojo.entity.SummaryData;
import com.wimoor.amazon.common.service.IExchangeRateHandlerService;
import com.wimoor.amazon.common.service.ISummaryDataService;
import com.wimoor.amazon.common.service.IUserSalesRankService;
import com.wimoor.amazon.orders.pojo.vo.ProductSalesRankVo;
import com.wimoor.amazon.report.pojo.vo.PerformanceVo;
import com.wimoor.amazon.report.service.IReportAmzPerformanceService;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

@Api(tags = "统计数据接口")
@RestController
@RequestMapping("/api/v1/summary")
//@Slf4j
@RequiredArgsConstructor
public class SummaryDataController {
	final ISummaryDataService iSummaryDataService;
	final IUserSalesRankService iUserSalesRankService;
	final IReportAmzPerformanceService reportAmzPerformanceService;
	final IExchangeRateHandlerService exchangeRateHandlerService;
	@ApiOperation(value = "获取统计数据")
    @GetMapping("/list")
    public Result<List<SummaryData>> getAmazonGroupAction() {
    	UserInfo userinfo = UserInfoContext.get();
    	List<SummaryData> result =iSummaryDataService.list(
    			new LambdaQueryWrapper<SummaryData>().eq(SummaryData::getShopid, userinfo.getCompanyid())
    			);
        return Result.success(result);
    }
	
	@ApiOperation(value = "获取销量统计数据")
    @PostMapping("/queryChartSales")
	public Result<Map<String,Object>> selectByMutilParameterAction(@ApiParam("查询DTO")@RequestBody SummaryMutilParameterQueryDTO dto){
		UserInfo userinfo = UserInfoContext.get();
		dto.setShopid(userinfo.getCompanyid());
		if(StrUtil.isEmpty(dto.getGroupid())) {
			dto.setGroupid(null);
		}
		dto.setOrderStatus(Arrays.asList("Shipped","Pending","Shipping","Complete"));
		return Result.success(iSummaryDataService.selectByMutilParameter(dto)) ;
	}
	
	@ApiOperation(value = "退货")
    @PostMapping("/queryChartReturn")
	public Result<Map<String,Object>> queryChartReturnAction(@ApiParam("查询DTO")@RequestBody SummaryMutilParameterQueryDTO dto){
		UserInfo userinfo = UserInfoContext.get();
		dto.setShopid(userinfo.getCompanyid());
		if(StrUtil.isEmpty(dto.getGroupid())) {
			dto.setGroupid(null);
		}
		return Result.success(iSummaryDataService.getReturnOrder(dto)) ;
	}
	
	@ApiOperation(value = "退货")
    @PostMapping("/queryChartReturnSummary")
	public Result<Map<String,Object>> queryChartReturnSummaryAction(@ApiParam("查询DTO")@RequestBody SummaryMutilParameterQueryDTO dto){
		UserInfo userinfo = UserInfoContext.get();
		dto.setShopid(userinfo.getCompanyid());
		if(StrUtil.isEmpty(dto.getGroupid())) {
			dto.setGroupid(null);
		}
		return Result.success(iSummaryDataService.getReturnOrderSummary(dto)) ;
	}
	
	@ApiOperation(value = "获取销量统计数据")
	@PostMapping("/queryChartSalesSummary")
	public Result<Map<String,Object>> selectSumByMutilParameterAction(@ApiParam("查询DTO")@RequestBody SummaryMutilParameterQueryDTO dto){
		UserInfo userinfo = UserInfoContext.get();
		dto.setShopid(userinfo.getCompanyid());
		if(StrUtil.isEmpty(dto.getGroupid())) {
			dto.setGroupid(null);
		}
		dto.setOrderStatus(Arrays.asList("Shipped","Pending","Shipping","Complete"));
		return Result.success(iSummaryDataService.selectSumByMutilParameter(dto)) ;
	}
	
	@ApiOperation(value = "获取销量统计数据")
	@PostMapping("/queryChartMarket")
	public Result<List<Map<String,Object>>> queryChartMarketAction(@ApiParam("查询DTO")@RequestBody SummaryMutilParameterQueryDTO dto){
		UserInfo userinfo = UserInfoContext.get();
		dto.setShopid(userinfo.getCompanyid());
		if(StrUtil.isEmpty(dto.getGroupid())) {
			dto.setGroupid(null);
		}
		dto.setOrderStatus(Arrays.asList("Shipped","Pending","Shipping","Complete"));
		return Result.success(iSummaryDataService.marketSummary(dto)) ;
	}
	
	@ApiOperation(value = "获取销量统计数据")
	@PostMapping("/querySales")
	public Result<IPage<Map<String,Object>>> findRankByShopAction(@ApiParam("查询DTO")@RequestBody UserSalesRankQueryDTO dto){
		UserInfo userinfo = UserInfoContext.get();
		return Result.success(iUserSalesRankService.findRankByShop(dto.getPage(),userinfo.getCompanyid(),dto.getDaytype())) ;
	}
	
	
	@ApiOperation(value = "获取销量统计数据")
	@PostMapping("/top5")
	public Result<List<ProductSalesRankVo>> top5Action(@ApiParam("查询DTO")@RequestBody SummaryMutilParameterQueryDTO dto){
		UserInfo userinfo = UserInfoContext.get();
		dto.setShopid(userinfo.getCompanyid());
		if(StrUtil.isEmpty(dto.getGroupid())) {
			dto.setGroupid(null);
		}
		return Result.success(iSummaryDataService.top5(dto) );
	}
	
	@ApiOperation(value = "获取销量统计数据")
	@PostMapping("/sumPerformance")
	public Result<PerformanceVo> sumPerformance(@ApiParam("查询DTO")@RequestBody SummaryMutilParameterQueryDTO dto){
		UserInfo userinfo = UserInfoContext.get();
		dto.setShopid(userinfo.getCompanyid());
		if(StrUtil.isEmpty(dto.getGroupid())) {
			dto.setGroupid(null);
		}
		return Result.success(reportAmzPerformanceService.getPerformance(userinfo.getCompanyid(),dto.getGroupid()) );
	}
	
	@ApiOperation(value = "获取销量统计数据")
	@GetMapping("/getCurrency/{toCurrency}")
	public Result<Map<String,BigDecimal>> getCurrency(@PathVariable String toCurrency){
		return Result.success(exchangeRateHandlerService.currencyChangeRate(toCurrency));
	}
	
	
}
