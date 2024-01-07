package com.wimoor.erp.purchase.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.common.pojo.entity.ERPBizException;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormEntry;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormReceive;
import com.wimoor.erp.purchase.pojo.vo.PurchaseFormReceiveVo;

public interface IPurchaseFormReceiveService  extends IService<PurchaseFormReceive> {
	public List<Map<String, Object>>  selectByEntryid(String id,String paytype) ;
	PurchaseFormEntry deleteReceive(String entryid,  UserInfo userinfo);
	void setReceiveReportExcelBook(SXSSFWorkbook workbook, Map<String, Object> param);
	public int closeRec(UserInfo user,PurchaseFormEntry entry) throws ERPBizException;	
    public int restartRec(UserInfo user,PurchaseFormEntry entry);
	int sysAutoRec(UserInfo user, List<String> ids);	
	public PurchaseFormEntry clearReceiveItem(UserInfo user,String recid);
	int removeRec(String id, UserInfo user) throws ERPBizException ;
	IPage<PurchaseFormReceiveVo> getReceiveReport(Page<?> page,Map<String, Object> param);
	PurchaseFormEntry clearReceive(String entryid, UserInfo user);
	void saveReceive(UserInfo user,PurchaseFormReceive rec, PurchaseFormEntry entry) throws ERPBizException;
	public Integer refreshInbound(String warehouseid,String materialid);
}
