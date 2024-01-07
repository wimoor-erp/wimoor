package com.wimoor.amazon.common.service.impl;

import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.common.service.IAmazonMoveDataService;
import com.wimoor.amazon.finances.mapper.AmzSettlementAccReportMapper;
import com.wimoor.amazon.finances.mapper.AmzSettlementReportMapper;
import com.wimoor.amazon.finances.mapper.AmzSettlementReportSummaryDayMapper;
import com.wimoor.amazon.finances.mapper.AmzSettlementReportSummaryMonthMapper;
import com.wimoor.amazon.finances.mapper.AmzSettlementSummarySkuMapper;
import com.wimoor.amazon.finances.pojo.entity.AmzSettlementAccReport;
import com.wimoor.amazon.inventory.mapper.InventoryReportMapper;
import com.wimoor.common.GeneralUtil;
@Service("amazonMoveDataService")
public class AmazonMoveDataServiceImpl implements IAmazonMoveDataService{
	@Resource
	private AmzSettlementReportMapper amzSettlementReportMapper;
	@Resource
	private AmzSettlementAccReportMapper amzSettlementAccReportMapper;
	@Resource
	AmzSettlementReportSummaryDayMapper amzSettlementReportSummaryDayMapper;
	@Resource
	AmzSettlementSummarySkuMapper amzSettlementSummarySkuMapper;
	@Resource
	AmzSettlementReportSummaryMonthMapper amzSettlementReportSummaryMonthMapper;
	@Resource
	private InventoryReportMapper inventoryReportMapper;
	@Resource
	private IAmazonAuthorityService iAmazonAuthorityService;
	@Resource
	IMarketplaceService marketplaceService;
	public void moveInventoryHisArchive() {
		
		Calendar c=Calendar.getInstance();
		List<AmazonAuthority> authList = iAmazonAuthorityService.getAllAuth();
		for(int i=0;i<30;i++) {
			c.add(Calendar.DATE, i*-1);
			for(AmazonAuthority auth:authList) {
			    List<Marketplace> marketlist = marketplaceService.findbyauth(auth.getId());
			    for(Marketplace market:marketlist) {
			    	inventoryReportMapper.newestInsertArchive(auth.getId(),market.getMarketplaceid(),GeneralUtil.formatDate(c.getTime()));	
			    }
			}
		}
		
		
	}
	
	public void moveSettlementReportArchive() {
		QueryWrapper<AmzSettlementAccReport> query=new QueryWrapper<AmzSettlementAccReport>();
		Calendar c=Calendar.getInstance();
		c.add(Calendar.MONTH, -3);
		query.lt("deposit_date", c.getTime());
		query.eq("ismoved",false);
		query.orderByDesc("deposit_date");
		List<AmzSettlementAccReport> list = amzSettlementAccReportMapper.selectList(query);
		for(AmzSettlementAccReport acc:list) {
			Integer num = amzSettlementReportMapper.hasDataArchive(acc);
			if(num==null||num==0) {
				amzSettlementReportMapper.moveDataArchive(acc);
				Integer numRpt = amzSettlementReportMapper.hasData(acc);
				if(numRpt==num) {
					acc.setIsmoved(true);
					amzSettlementAccReportMapper.updateById(acc);
				}
				try {
					if(acc.getTotalAmount().intValue()>1000) {
						   Thread.sleep(100000);
					   }
					else if(acc.getTotalAmount().intValue()>100) {
					   Thread.sleep(10000);
				    }
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				Integer numRpt = amzSettlementReportMapper.hasData(acc);
				if(numRpt==num) {
					acc.setIsmoved(true);
					amzSettlementAccReportMapper.updateById(acc);
				}
			}
		}
	}
	
	public void moveSettlementReport() {
		QueryWrapper<AmzSettlementAccReport> query=new QueryWrapper<AmzSettlementAccReport>();
		Calendar c=Calendar.getInstance();
		c.add(Calendar.MONTH, -2);
		query.gt("deposit_date", c.getTime());
		query.eq("ismoved",true);
		query.orderByDesc("deposit_date");
		List<AmzSettlementAccReport> list = amzSettlementAccReportMapper.selectList(query);
		for(AmzSettlementAccReport acc:list) {
			Integer num = amzSettlementReportMapper.hasData(acc);
			if(num==null||num==0) {
				amzSettlementReportMapper.moveData(acc);
				System.out.println(acc.getDepositDate());
				acc.setIsmoved(false);
				amzSettlementAccReportMapper.updateById(acc);
				
//				try {
//					if(acc.getTotalAmount().intValue()>1000) {
//						   Thread.sleep(100000);
//					   }
//					else if(acc.getTotalAmount().intValue()>100) {
//					   Thread.sleep(10000);
//				    }
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}else {
				acc.setIsmoved(false);
				amzSettlementAccReportMapper.updateById(acc);
			}
		}
	}
}
