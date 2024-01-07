package com.wimoor.amazon.report.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IAmazonGroupService;
import com.wimoor.amazon.common.service.IUserSalesRankService;
import com.wimoor.amazon.inbound.service.IShipInboundShipmentService;
import com.wimoor.amazon.product.service.IProductFollowHandlerService;
import com.wimoor.amazon.product.service.IProductInPresaleService;
import com.wimoor.amazon.report.mapper.ReportRequestRecordMapper;
import com.wimoor.amazon.report.pojo.entity.ReportRequestRecord;
import com.wimoor.amazon.report.pojo.entity.ReportType;
import com.wimoor.amazon.report.service.IHandlerReportService;
import com.wimoor.amazon.report.service.IReportRequestTypeService;
import com.wimoor.amazon.summary.service.IAmazonSettlementAnalysisService;
import com.wimoor.amazon.summary.service.ISummaryOrderReportService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
@Api(tags = "报表接口")
@RestController
@Component("reportController")
@Slf4j
@RequestMapping("/api/v1/report")
public class ReportController {
	@Autowired
	IHandlerReportService handlerReportService;
	@Resource
	ISummaryOrderReportService summaryOrderReportService;
	@Resource
	IUserSalesRankService userSalesRankService;
    @Resource
    IAmazonSettlementAnalysisService amazonSettlementAnalysisAgentService;
    @Resource
    ReportRequestRecordMapper reportRequestRecordMapper;
	@Autowired
	IAmazonAuthorityService amazonAuthorityService;
	@Autowired
	IProductInPresaleService iProductInPresaleService;
	@Autowired
	IProductFollowHandlerService iProductFollowHandlerService;
	@Autowired
	IReportRequestTypeService iReportRequestTypeService;
	@Autowired
	IAmazonGroupService iAmazonGroupService;
	@Autowired
	IShipInboundShipmentService iShipInboundShipmentService;
    /**
     * 提供用于用户登录认证信息
     */
    @ApiOperation(value = "根据id 获取产品信息")
    @ApiImplicitParam(name = "type", value = "报表code", required = true, paramType = "path", dataType = "String")
    @GetMapping("/requestReport/{type}")
    public Result<?> requestReportAction(@PathVariable String type) {
		log.info("申请报表-----"+new Date()+"----------"+type);
    	handlerReportService.requestReport(type,false); 
    	return Result.success();
    }

    /**
     * 提供用于用户登录认证信息
     */
    @ApiOperation(value = "报表分类")
    @GetMapping("/reportTypeList")
    public Result<?> reportTypeListAction() {
    	return Result.success(iReportRequestTypeService.list());
    }
    /**
     * 提供用于用户登录认证信息
     */
    @ApiOperation(value = "根据id 获取产品信息")
    @ApiImplicitParam(name = "type", value = "报表code", required = true, paramType = "path", dataType = "String")
    @GetMapping("/requestReportAuth/{type}/{id}")
    public Result<?> requestReportAuthAction(@PathVariable String type,@PathVariable String id) {
		log.info("申请报表-----"+new Date()+"----------"+type);
    	handlerReportService.requestAuthReport("groupid",id,type,false); 
    	return Result.success();
    }

    
    /**
     * 提供用于用户登录认证信息
     */
    @ApiOperation(value = "根据id 获取产品信息")
    @ApiImplicitParam(name = "type", value = "报表code", required = true, paramType = "path", dataType = "String")
    @GetMapping("/requestReportIgnore/{type}")
    public Result<?> requestReportIgnoreAction(@PathVariable String type) {
    	handlerReportService.requestReport(type,true); 
        return Result.success();
    }
 
    
    /**
     * 提供用于用户登录认证信息
     */
    @ApiOperation(value = "根据id 获取产品信息")
    @ApiImplicitParam(name = "type", value = "报表code", required = true, paramType = "path", dataType = "String")
    @GetMapping("/requestReportParam")
    public Result<?> requestReportParamAction(String type,String start,String end,String ignore) {
    	boolean myignore=false;
    	if(ignore!=null&&ignore.equals("true")) {
    		myignore=true;
    	}
    	handlerReportService.requestReport(type,start, end,myignore) ;
        return Result.success();
    }

    @ApiOperation(value = "根据id 获取产品信息")
    @ApiImplicitParam(name = "type", value = "报表code", required = true, paramType = "path", dataType = "String")
    @GetMapping("/requestReportParamAuth")
    public Result<?> requestReportParamAction(String authid,String type,String start,String end,String ignore) {
    	boolean myignore=false;
    	if(ignore!=null&&ignore.equals("true")) {
    		myignore=true;
    	}
    	Calendar cstart=Calendar.getInstance();
    	cstart.setTime(GeneralUtil.getDatez(start));
    	Calendar cend=Calendar.getInstance();
    	cend.setTime(GeneralUtil.getDatez(end));
    	handlerReportService.requestReport(authid,type,cstart, cend,myignore) ;
        return Result.success();
    }
   
    /**
     * 提供用于用户登录认证信息
     */
    @ApiOperation(value = "根据店铺和类型获取报表")
    @GetMapping("/requestGroupReport")
    public Result<?> requestGroupReport(String groupid,String reportType,String start,String end,String ignore) {
    	boolean myignore=false;
    	if(ignore!=null&&ignore.equals("true")) {
    		myignore=true;
    	}
    	Date startDate=GeneralUtil.getDatez(start);
    	Date endDate=GeneralUtil.getDatez(end);
    	List<AmazonAuthority> amazonAuthorityList=amazonAuthorityService.selectByGroupId(groupid);
    	handlerReportService.requestReport(amazonAuthorityList,  reportType, startDate,endDate,myignore) ;
        return Result.success();
    }
    
    /**
     * 提供用于用户登录认证信息
     */
    @ApiOperation(value = "更新报表")
    @ApiImplicitParams({
    	 @ApiImplicitParam(name = "type", value = "报表code", required = true, paramType = "path", dataType = "String")
    })
    @GetMapping("/refreshReportType/{type}")
    public Result<?> refreshReportAction(@PathVariable String type) {
        handlerReportService.refreshGetReportList(type);
        return Result.success();
    }
    
    /**
     * 提供用于用户登录认证信息
     */
    @ApiOperation(value = "汇总")
    @ApiImplicitParams({
    	 @ApiImplicitParam(name = "days", value = "汇总days", required = true, paramType = "path", dataType = "String")
    })
    @GetMapping("/refreshAll")
    public Result<?> refreshAll() {
    	Calendar c=Calendar.getInstance();
    	c.add(Calendar.DATE, -10);
    	summaryOrderReportService.refreshAll(c.getTime());
        return Result.success();
    }
    
    /**
     * 提供用于用户登录认证信息
     */
    @ApiOperation(value = "汇总")
    @GetMapping("/refreshAllByAuth")
    public Result<?> refreshAllByAuth(String authid) {
    	summaryOrderReportService.refreshAll(authid);
        return Result.success();
    }
    
    
    /**
     * 提供用于用户登录认证信息
     */
    @ApiOperation(value = "更新报表")
    @GetMapping("/refreshReport")
    public Result<?> refreshReportAction() {
    	log.info("更新报表-----"+new Date());
    	handlerReportService.refreshGetReportList(null);
    	return Result.success();
    }
    /**
     * 提供用于用户登录认证信息
     */
    @ApiOperation(value = "读取报表")
    @GetMapping("/listReport")
    public Result<?> listReportAction(String reporttype,String authid) {
    	log.info("更新报表-----"+new Date());
    	AmazonAuthority auth = amazonAuthorityService.getById(authid);
    	Calendar cstart=Calendar.getInstance();
    	cstart.add(Calendar.DATE, -10);
    	Calendar cend=Calendar.getInstance();
    	return Result.success(handlerReportService.listReportList(auth,reporttype,cstart,cend,null));
    }
    /**
     * 提供用于用户登录认证信息
     */
    @ApiOperation(value = "更新报表")
    @GetMapping("/refreshSingleReport")
    public Result<?> refreshSingleReportAction() {
    	log.info("更新报表-----"+new Date());
    	handlerReportService.refreshSingleGetReportList(null);
    	return Result.success();
    } 
    
    /**
     * 提供用于用户登录认证信息
     */
    @ApiOperation(value = "更新报表")
    @ApiImplicitParams({
    	 @ApiImplicitParam(name = "type", value = "报表code", required = true, paramType = "path", dataType = "String")
    })
    @GetMapping("/processReportType/{type}")
    public Result<?> processReportTypeAction(@PathVariable String type) {
        handlerReportService.refreshProcessReportList(type);
        return Result.success();
    }
    
    /**
     * 提供用于用户登录认证信息
     */
    @ApiOperation(value = "更新报表")
    @GetMapping("/processReport")
    public Result<?> processReportAction() {
    	log.info("处理报表-----"+new Date());
        handlerReportService.refreshProcessReportList(null);
        return Result.success();
    }
    
    @ApiOperation(value = "更新报表")
    @GetMapping("/refreshReportById")
    public Result<?> refreshReportByIdAction(String id) {
        handlerReportService.refreshProcessReportById(id);
        return Result.success();
    }
    
    @ApiOperation(value = "更新报表")
    @GetMapping("/dataMove")
    public Result<?> dataMoveAction() {
    	summaryOrderReportService.dataMove();
        return Result.success();
    }
    @ApiOperation(value = "更新SellerName")
    @GetMapping("/refreshSellerName")
    public Result<?> refreshSellerNameAction() {
    	try {
			iProductFollowHandlerService.recordFollowOfferSellerName();
		}catch(Exception e) {
			e.printStackTrace();
		}
        return Result.success();
    }
	
    @ApiOperation(value = "汇总orders报表数据")
    @GetMapping("/summaryOrderReport")
    public Result<?> summaryOrderReportAction() {
    	new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				log.info("汇总报表-----"+new Date());
				try {
					summaryOrderReportService.summaryOrderReport();
				}catch(Exception e) {
					e.printStackTrace();
				}
				try {
					amazonSettlementAnalysisAgentService.refreshAll();
				}catch(Exception e) {
					e.printStackTrace();
				}
				try {
					iProductFollowHandlerService.recordFollowOfferSellerName();
				}catch(Exception e) {
					e.printStackTrace();
				}
				
			}
    	}).start();
    	new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				log.info("汇总报表-----"+new Date());
				try {
					iShipInboundShipmentService.saveTransTrance();
				}catch(Exception e) {
					e.printStackTrace();
				}
				 
				
			}
    	}).start();
    	
    	return Result.success();
    }
    
    @ApiOperation(value = "产品利润计算")
    @GetMapping("/dataAnalysis")
    public Result<?> dataAnalysisAction() {
    	Set<String> shopset=new HashSet<String>();
    	shopset.add("26138972975530085");
    	summaryOrderReportService.dataAnalysis(shopset);
    	return Result.success();
    }
    @ApiOperation(value = "月度退货")
    @GetMapping("/saveMainReturnReport")
    public Result<?> saveMainReturnReportAction() {
    	summaryOrderReportService.saveMainReturnReport(null);
    	return Result.success();
    }
 
    @ApiOperation(value = "汇总计算")
    @GetMapping("/summaryMain")
    public Result<?> summaryMainAction() {
    	try {
    		summaryOrderReportService.saveMainReport(null);
        	summaryOrderReportService.summary(null);
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	return Result.success();
    }
    
    @ApiOperation(value = "账期重新汇总计算")
    @GetMapping("/settlementRefreshAll")
    public Result<?> settlementRefreshAllAction() {
        amazonSettlementAnalysisAgentService.refreshAll();
    	return Result.success();
    }
    @ApiOperation(value = "账期重新汇总计算")
    @GetMapping("/settlementRefreshAllByManual")
    public Result<?> settlementRefreshAllByManualAction() {
        amazonSettlementAnalysisAgentService.refreshAllByManual();
    	return Result.success();
    }
   
    @ApiOperation(value = "账期重新汇总计算")
    @GetMapping("/checkSettlementSummary")
    public Result<?> checkSettlementSummaryAction() {
        amazonSettlementAnalysisAgentService.checkSummaryData();
    	return Result.success();
    }

    @ApiOperation(value = "人员销售排名计算")
    @GetMapping("/refreshUserRank")
    public Result<?> refreshUserRank() {
    	  Date refreshdate=userSalesRankService.getSummaryLastDate();
    		 if(refreshdate==null||!GeneralUtil.formatDate(refreshdate).equals(GeneralUtil.formatDate(new Date()))) {
    			 List<ReportRequestRecord> list = reportRequestRecordMapper.selectNoTreatReport(ReportType.OrdersByOrderDateReport,null);
    				 if(list==null||list.size()==0) {
    						userSalesRankService.summaryAllUserSales();
    			     }
    		 }
    	return Result.success();
    }
    
    //根据shopid查询grouplist
    @GetMapping("/selectGroupList")
    public Result<?> selectGroupListAction(String shopid) {
    	return Result.success(iAmazonGroupService.selectByShopId(shopid));
    }
    
    //查询任务进度列表
    @GetMapping("/selectTaskInfoList")
    public Result<?> selectTaskInfoListAction(String sellerid,String marketplaceid) {
    	return Result.success(iAmazonGroupService.selectTaskInfoList(sellerid,marketplaceid));
    }
    
}
