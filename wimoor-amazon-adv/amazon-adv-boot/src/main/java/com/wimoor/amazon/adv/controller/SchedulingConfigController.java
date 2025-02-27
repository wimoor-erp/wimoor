package com.wimoor.amazon.adv.controller;


import java.math.BigInteger;

import javax.annotation.Resource;

import com.wimoor.amazon.adv.common.pojo.CampaignType;
import com.wimoor.amazon.adv.common.service.*;
import com.wimoor.amazon.adv.exports.service.impl.AdvExportType;
import com.wimoor.amazon.adv.report.service.impl.AmzAdvReportHandlerServiceImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.amazon.adv.common.pojo.AmzAdvAuth;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.report.dao.AmzAdvReportRequestTypeMapper;
import com.wimoor.amazon.adv.report.pojo.AmzAdvReportRequestType;
import com.wimoor.amazon.adv.report.service.IAmzAdvReportHandlerService;
import com.wimoor.amazon.adv.report.service.IAmzAdvReportService;
import com.wimoor.amazon.adv.report.service.IAmzAdvReportTreatService;
import com.wimoor.amazon.adv.exports.service.IAmzAdvSnapshotHandlerService;
import com.wimoor.common.result.Result;
import com.wimoor.util.SpringUtil;


@Component("schedulingConfigController")
@RestController 
@RequestMapping("/api/v1/advschedule")
public class SchedulingConfigController {
	@Resource
	IAmzAdvPortfoliosService amzAdvPortfoliosService;
	@Resource
	IAmzAdvReportService amzAdvReportService;
	@Resource
	IAmzAdvReportHandlerService amzAdvReportHandlerService;
	@Resource
	IAmzAdvRemindService amzAdvRemindService;
    @Resource
    IAmazonReportAdvSummaryService amazonReportAdvSummaryService;
    @Resource
    IAmzAdvSnapshotHandlerService iAmzAdvSnapshotHandlerService;
	@Resource
	AmzAdvReportRequestTypeMapper amzAdvReportRequestTypeMapper;
	@Resource
	IAmzAdvAuthService amzAdvAuthService;
	@Resource
	IAmzAdvertInvoicesService amzAdvertInvoicesService;
	@GetMapping("/refreshListPortfolios")
	public Result<?>  refreshListPortfolios() {
	     // TODO Auto-generated method stub
	     iAmzAdvSnapshotHandlerService.requestStoreBrand();
		//获取广告发票信息
		amzAdvertInvoicesService.amzAdvInvoicesHandle();
		 return Result.success();
	}

	@GetMapping("/requestReport")
	public Result<?> requestReport(){
		 amzAdvReportHandlerService.requestReport();
		 return Result.success();
	}
	@GetMapping("/requestReportByType")
	public Result<?> requestReport(String profileid,String typeid){
		    AmzAdvProfile profile =amzAdvAuthService.getAmzAdvProfileByKey(new BigInteger(profileid));
		    AmzAdvAuth advauth=profile.getAdvAuth();
		    AmzAdvReportRequestType type=amzAdvReportRequestTypeMapper.selectByPrimaryKey(typeid);
			IAmzAdvReportTreatService reportTreat = SpringUtil.getBean(type.getBean());
			reportTreat.requestReport(advauth,profile,type);
		    return Result.success();
	}
	
	
	@GetMapping("/refreshRemaind")
	public Result<?> refreshRemaindAction() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				amazonReportAdvSummaryService.refreshSummary(null);
			}
		}).start();
				// TODO Auto-generated method stub
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
			    amzAdvRemindService.refreshRemaind();
			}
		}).start();
			
	
		return Result.success();
	}
	
	@GetMapping("/refreshSummaryByShopid")
	public Result<?> refreshSummaryAction(String shopid) {
				// TODO Auto-generated method stub
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				amazonReportAdvSummaryService.refreshSummary(shopid);
			}
		}).start();
				
		return Result.success();
	}


	@GetMapping("/requestSnapshot")
	public Result<?> requestSnapshot(){
		iAmzAdvSnapshotHandlerService.requestSnapshot( AdvExportType.campaigns, CampaignType.all);
		iAmzAdvSnapshotHandlerService.requestSnapshot( AdvExportType.adGroups, CampaignType.all);
		iAmzAdvSnapshotHandlerService.requestSnapshot( AdvExportType.ads, CampaignType.all);
		iAmzAdvSnapshotHandlerService.requestSnapshot( AdvExportType.targets, CampaignType.all);
		 return Result.success();
	}

	@GetMapping("/readReport")
	public Result<?> readReport(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				amzAdvReportHandlerService.readReport();
			}
		}).start();
		 return Result.success();
	}
	
	@GetMapping("/readReportById")
	public Result<?> readReport(String id){
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				amzAdvReportHandlerService.readReport(id);
			}
		}).start();
		 return Result.success();
	}
	
	 
	
	@GetMapping("/readSnapshot")
	public Result<?> readSnapshot(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				iAmzAdvSnapshotHandlerService.readSnapshot();
			}
		}).start();
		 return Result.success();
	}
	
	@GetMapping("/readSnapshotById")
	public Result<?> readSnapshotById(String id){
		 iAmzAdvSnapshotHandlerService.readSnapshot(id);
		 return Result.success();
	}
	@GetMapping("/requestApiSnapshot")
	public Result<?> requestHsaVideoSnapshot(){
		iAmzAdvSnapshotHandlerService.requestApiSnapshot();
		 return Result.success();
	}
	@GetMapping("/requestApiCampBudgetRuleSnapshot")
	public Result<?> requestApiCampBudgetRuleSnapshot(){
		iAmzAdvSnapshotHandlerService.requestApiCampBudgetRuleSnapshot();
		 return Result.success();
	}
}
