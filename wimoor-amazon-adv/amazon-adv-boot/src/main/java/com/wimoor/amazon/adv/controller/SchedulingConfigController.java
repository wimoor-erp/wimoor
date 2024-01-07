package com.wimoor.amazon.adv.controller;


import java.math.BigInteger;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.amazon.adv.common.pojo.AmzAdvAuth;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.service.IAmazonReportAdvSummaryService;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.common.service.IAmzAdvPortfoliosService;
import com.wimoor.amazon.adv.common.service.IAmzAdvRemindService;
import com.wimoor.amazon.adv.report.dao.AmzAdvReportRequestTypeMapper;
import com.wimoor.amazon.adv.report.pojo.AmzAdvReportRequestType;
import com.wimoor.amazon.adv.report.service.IAmzAdvReportHandlerService;
import com.wimoor.amazon.adv.report.service.IAmzAdvReportService;
import com.wimoor.amazon.adv.report.service.IAmzAdvReportTreatService;
import com.wimoor.amazon.adv.report.service.IAmzAdvSnapshotHandlerService;
import com.wimoor.amazon.adv.report.service.impl.AmzAdvReportHandlerServiceImpl.AdvRecordType;
import com.wimoor.amazon.adv.sp.service.impl.AmzAdvCampaignServiceImpl.CampaignType;
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
	@GetMapping("/refreshListPortfolios")
	public Result<?>  refreshListPortfolios() {
	     // TODO Auto-generated method stub
	     iAmzAdvSnapshotHandlerService.requestStoreBrand();
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
		     iAmzAdvSnapshotHandlerService.requestSnapshot(AdvRecordType.campaigns, CampaignType.sd);
		     iAmzAdvSnapshotHandlerService.requestSnapshot(AdvRecordType.campaigns, CampaignType.sp);
		     iAmzAdvSnapshotHandlerService.requestSnapshot(AdvRecordType.campaigns, CampaignType.hsa);
		     iAmzAdvSnapshotHandlerService.readSnapshot();
		     iAmzAdvSnapshotHandlerService.requestSnapshot(AdvRecordType.adGroups, CampaignType.sp);
		     iAmzAdvSnapshotHandlerService.requestSnapshot(AdvRecordType.adGroups, CampaignType.sd);
			 iAmzAdvSnapshotHandlerService.readSnapshot();
			 iAmzAdvSnapshotHandlerService.requestSnapshot(AdvRecordType.productAds, CampaignType.sp);
			 iAmzAdvSnapshotHandlerService.requestSnapshot(AdvRecordType.productAds, CampaignType.sd);
			 iAmzAdvSnapshotHandlerService.requestSnapshot(AdvRecordType.keywords, CampaignType.hsa);
			 iAmzAdvSnapshotHandlerService.requestSnapshot(AdvRecordType.keywords, CampaignType.sp);
			 iAmzAdvSnapshotHandlerService.readSnapshot();
			 iAmzAdvSnapshotHandlerService.requestSnapshot(AdvRecordType.targets,CampaignType.sd);
			 iAmzAdvSnapshotHandlerService.requestSnapshot(AdvRecordType.targets, CampaignType.sp);
			 iAmzAdvSnapshotHandlerService.requestSnapshot(AdvRecordType.negativeTargets, CampaignType.sp);
			 iAmzAdvSnapshotHandlerService.requestSnapshot(AdvRecordType.negativeKeywords, CampaignType.sp);
			 iAmzAdvSnapshotHandlerService.requestSnapshot(AdvRecordType.campaignNegativeKeywords, CampaignType.sp);
			 iAmzAdvSnapshotHandlerService.readSnapshot();
 	         iAmzAdvSnapshotHandlerService.readSnapshot();
			
			// iAmzAdvSnapshotHandlerService.requestHsaVideoSnapshot();
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
}
