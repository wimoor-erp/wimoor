package com.wimoor.erp.purchase.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.common.pojo.entity.ERPBizException;
import com.wimoor.erp.common.pojo.entity.EnumByInventory;
import com.wimoor.erp.common.pojo.entity.Operate;
import com.wimoor.erp.common.pojo.entity.Status;
import com.wimoor.erp.inventory.pojo.entity.InventoryParameter;
import com.wimoor.erp.inventory.service.IInventoryService;
import com.wimoor.erp.purchase.mapper.PurchaseFormEntryMapper;
import com.wimoor.erp.purchase.mapper.PurchaseFormMapper;
import com.wimoor.erp.purchase.mapper.PurchaseFormReceiveMapper;
import com.wimoor.erp.purchase.pojo.entity.PurchaseForm;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormEntry;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormReceive;
import com.wimoor.erp.purchase.pojo.vo.PurchaseFormReceiveVo;
import com.wimoor.erp.purchase.service.IPurchaseFormPaymentService;
import com.wimoor.erp.purchase.service.IPurchaseFormReceiveService;

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;

@Service("purchaseFormReceiveService")
@RequiredArgsConstructor
public class PurchaseFormReceiveServiceImpl extends  ServiceImpl<PurchaseFormReceiveMapper,PurchaseFormReceive> implements IPurchaseFormReceiveService {
	@Autowired
	IInventoryService inventoryService;
	final PurchaseFormEntryMapper purchaseFormEntryMapper;
	final PurchaseFormMapper purchaseFormMapper;
	@Autowired
	@Lazy
    IPurchaseFormPaymentService iPurchaseFormPaymentService;
	
	@Transactional
	public int sysAutoRec(UserInfo user,  List<String> ids) {
		int result=0;
		for (int i=0;i<ids.size();i++) {
			String id=ids.get(i);
			PurchaseFormEntry entry = purchaseFormEntryMapper.selectById(id);
			if(entry==null) {
				continue;
			}
			PurchaseForm form = purchaseFormMapper.selectById(entry.getFormid());
			if (entry.getAmount() > entry.getTotalin()) {
				PurchaseFormReceive rec = new PurchaseFormReceive();
				rec.setFormentryid(id);
				rec.setAmount(entry.getAmount() - entry.getTotalin());
				rec.setWarehouseid(form.getWarehouseid());
				rec.setOpttime(new Date());
				rec.setOperator(user.getId());
				rec.setFtype("in");
				rec.setRemark("批量完成收货");
				this.saveReceive(user, rec,entry);
			}else {
				this.closeRec(user,entry);
			}
			result+=1;
			entry.setInwhstatus(1);
			if (entry.getPaystatus() == 1) {
				entry.setAuditstatus(3);
			}
			purchaseFormEntryMapper.updateById(entry);
		}
		return result;
	}
	

	@Transactional
	public int closeRec(UserInfo user,PurchaseFormEntry entry) throws ERPBizException {
		Integer oldin = entry.getTotalin();
		if (oldin == null) {
			oldin = 0;
		}
		entry.setInwhstatus(1);
		if (entry.getPaystatus() == 1) {
			entry.setAuditstatus(3);
		}
		if (oldin < entry.getAmount()) {
			InventoryParameter invpara = new InventoryParameter();
			invpara.setFormid(entry.getFormid());
			invpara.setMaterial(entry.getMaterialid());
			invpara.setOperator(user.getId());
			invpara.setOpttime(new Date());
			invpara.setStatus(EnumByInventory.alReady);
			invpara.setShopid(user.getCompanyid());
			invpara.setFormtype("purchase");
			PurchaseForm ph = purchaseFormMapper.selectById(entry.getFormid());
			invpara.setNumber(ph.getNumber());
			invpara.setAmount(entry.getAmount() - oldin);
			invpara.setWarehouse(ph.getWarehouseid());
		    inventoryService.SubStockByStatus(invpara, Status.inbound, Operate.out);
		} 
		entry.setCloserecdate(new Date());
		int result = 0;
		if(purchaseFormEntryMapper.updateById(entry)>0) {
			result++;
		}
		return result;
	}
	
	@Transactional
   public int restartRec(UserInfo user,PurchaseFormEntry entry) {
	   Integer oldin = entry.getTotalin();
		if (oldin == null) {
			oldin = 0;
		}
		entry.setInwhstatus(0);
		if (entry.getAuditstatus() == 3) {
			entry.setAuditstatus(2);
		}
		InventoryParameter invpara = new InventoryParameter();
		invpara.setFormid(entry.getFormid());
		invpara.setMaterial(entry.getMaterialid());
		invpara.setOperator(user.getId());
		invpara.setOpttime(new Date());
		invpara.setStatus(EnumByInventory.alReady);
		invpara.setShopid(user.getCompanyid());
		invpara.setFormtype("purchase");
		PurchaseForm ph =purchaseFormMapper.selectById(entry.getFormid());
		invpara.setNumber(ph.getNumber());
		if (oldin < entry.getAmount()) {
			invpara.setAmount(entry.getAmount() - oldin);
		}else {
			invpara.setAmount(0);
		}
		invpara.setWarehouse(ph.getWarehouseid());
	    inventoryService.AddStockByStatus(invpara, Status.inbound, Operate.in);
		int result = 0;
		if(purchaseFormEntryMapper.updateById(entry)>0) {
			result++;
		}
		return result;
   }

	public void setReceiveReportExcelBook(SXSSFWorkbook workbook, Map<String, Object> param) {
		Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
		titlemap.put("number", "订单编码");
		titlemap.put("sku", "SKU");
		titlemap.put("ftype", "操作");
		titlemap.put("amount", "入库数量");
		titlemap.put("purchases", "订单采购量");
		titlemap.put("purchaseprice", "订单采购金额");
		titlemap.put("mname", "产品名称");
		titlemap.put("name", "操作人");
		titlemap.put("createdate", "创建日期");
		titlemap.put("opttime", "入库日期");
		titlemap.put("remark", "备注");
		titlemap.put("wname", "仓库");
		titlemap.put("cname", "供应商");
		List<PurchaseFormReceiveVo> list = this.baseMapper.receiveReport(param);
		Sheet sheet = workbook.createSheet("sheet1");
		// 在索引0的位置创建行（最顶端的行）
		Row trow = sheet.createRow(0);
		Object[] titlearray = titlemap.keySet().toArray();
		for (int i = 0; i < titlearray.length; i++) {
			Cell cell = trow.createCell(i); // 在索引0的位置创建单元格(左上端)
			Object value = titlemap.get(titlearray[i].toString());
			cell.setCellValue(value.toString());
		}
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Row row = sheet.createRow(i + 1);
				PurchaseFormReceiveVo vo = list.get(i);
				  Map<String, Object> map = BeanUtil.beanToMap(vo);
				for (int j = 0; j < titlearray.length; j++) {
					Cell cell = row.createCell(j); // 在索引0的位置创建单元格(左上端)
					Object value=map.get(titlearray[j]);
					if(value!=null) {
						cell.setCellValue(value.toString());
					}else {
						cell.setCellValue("");
					}
					 
				}
			}
		}
	}
	

	public int removeRec(String id, UserInfo user) throws ERPBizException {
		PurchaseFormReceive oldrec = this.baseMapper.selectById(id);
		PurchaseFormEntry entry = purchaseFormEntryMapper.selectById(oldrec.getFormentryid());

		PurchaseForm form = purchaseFormMapper.selectById(entry.getFormid());
		InventoryParameter invpara = new InventoryParameter();
		invpara.setFormid(entry.getFormid());
		invpara.setMaterial(entry.getMaterialid());
		invpara.setWarehouse(form.getWarehouseid());
		invpara.setOperator(entry.getOperator());
		invpara.setOpttime(new Date());
		invpara.setStatus(EnumByInventory.alReady);
		invpara.setShopid(form.getShopid());
		invpara.setFormtype("purchase");
		PurchaseForm ph = purchaseFormMapper.selectById(entry.getFormid());
		invpara.setNumber(ph.getNumber());
		if ("in".equals(oldrec.getFtype())) {
			Integer newin = entry.getTotalin() - oldrec.getAmount();
			entry.setTotalin(Integer.parseInt(newin.toString()));
			invpara.setAmount(Integer.parseInt(oldrec.getAmount().toString()));
			inventoryService.AddStockByStatus(invpara, Status.inbound, Operate.cancel);
			Integer amount = invpara.getAmount();
			invpara.setAmount(amount);
			invpara.setWarehouse(oldrec.getWarehouseid());
			inventoryService.SubStockByStatus(invpara, Status.fulfillable, Operate.cancel);
			invpara.setAmount(amount * -1);
		}
		if ("re".equals(oldrec.getFtype())) {
			Integer newre = entry.getTotalre() - oldrec.getAmount();
			entry.setTotalre(Integer.parseInt(newre.toString()));
			invpara.setAmount(Integer.parseInt(oldrec.getAmount().toString()));
			inventoryService.SubStockByStatus(invpara, Status.inbound, Operate.cancel);
			Integer amount = invpara.getAmount();
			invpara.setAmount(amount);
			invpara.setWarehouse(oldrec.getWarehouseid());
			inventoryService.AddStockByStatus(invpara, Status.fulfillable, Operate.cancel);
			invpara.setAmount(amount * -1);
		}
		if ("ch".equals(oldrec.getFtype())) {
			Integer newch = entry.getTotalch() - oldrec.getAmount();
			entry.setTotalch(Integer.parseInt(newch.toString()));
		}
		purchaseFormEntryMapper.updateById(entry);
		return this.baseMapper.deleteById(id);
	}
	
	public IPage<PurchaseFormReceiveVo> getReceiveReport(Page<?> page,Map<String, Object> param) {
		IPage<PurchaseFormReceiveVo> result = this.baseMapper.receiveReport(page,param);
		Integer summary=this.baseMapper.receiveReportSummary(param);
		if(result.getRecords()!=null&&result.getRecords().size()>0) {
			for(PurchaseFormReceiveVo item:result.getRecords()) {
				item.setTotalamount(summary);
			}
		}
		return result;
	}

	@Transactional
	public PurchaseFormEntry clearReceiveItem(UserInfo user,String recid) {
		///// 更新entry,totalin,totalre置为0，将totalin的可用库存减掉，待入库改为采购数量，加入收退货记录（t_erp_purchase_form_receive）
		PurchaseFormReceive item=this.baseMapper.selectById(recid);
		PurchaseFormEntry purchaseFormEntry = purchaseFormEntryMapper.selectById(item.getFormentryid());
		PurchaseForm form=purchaseFormMapper.selectById(purchaseFormEntry.getFormid());
		if(purchaseFormEntry.getTotalin()==0) {
			throw new ERPBizException("请确认是否已经完成撤销操作，当前没有可以撤销的库存");
		}
		if(purchaseFormEntry.getInwhstatus()==1) {
			throw new ERPBizException("已经完成入库无法撤销，请点击继续收货后执行");
		}
		InventoryParameter invpara = new InventoryParameter();
		invpara.setFormid(purchaseFormEntry.getFormid());
		invpara.setMaterial(purchaseFormEntry.getMaterialid());
		invpara.setOperator(user.getId());
		invpara.setOpttime(new Date());
		invpara.setStatus(EnumByInventory.alReady);
		invpara.setShopid(user.getCompanyid());
		invpara.setFormtype("purchase");
	 
		PurchaseForm ph = purchaseFormMapper.selectById(purchaseFormEntry.getFormid());
		invpara.setNumber(ph.getNumber());
		invpara.setWarehouse(ph.getWarehouseid());
		
		if("in".equals(item.getFtype())) {
			invpara.setWarehouse(item.getWarehouseid());
			invpara.setAmount(item.getAmount());
			inventoryService.SubStockByStatus(invpara, Status.fulfillable, Operate.out);
			item.setFtype("clear");
			item.setRemark("已撤销【通过撤销入库操作】");
			this.baseMapper.updateById(item);
			
			invpara.setWarehouse(ph.getWarehouseid());
			
			int nowtotalin=purchaseFormEntry.getTotalin()-item.getAmount();
			if (nowtotalin<=purchaseFormEntry.getAmount()) {
                    int needinbound=purchaseFormEntry.getAmount()-nowtotalin;
                    invpara.setAmount(needinbound);
                    invpara.setWarehouse(form.getWarehouseid());
				    inventoryService.AddStockByStatus(invpara, Status.inbound, Operate.in);
			}  
			purchaseFormEntry.setTotalin(nowtotalin);
		}else if("out".equals(item.getFtype())) {
			invpara.setWarehouse(item.getWarehouseid());
			invpara.setAmount(item.getAmount());
			inventoryService.AddStockByStatus(invpara, Status.fulfillable, Operate.in);
			item.setFtype("clear");
			item.setRemark("已撤销【通过撤销出库操作】");
			this.baseMapper.updateById(item);
			
			int nowtotalin=purchaseFormEntry.getTotalin()+item.getAmount();
			if (purchaseFormEntry.getTotalin()<=purchaseFormEntry.getAmount()) {
				  if(nowtotalin<=purchaseFormEntry.getAmount()) {
					    invpara.setAmount(item.getAmount());
					    invpara.setWarehouse(form.getWarehouseid());
					    inventoryService.SubStockByStatus(invpara, Status.inbound, Operate.in);
				  }else {
					  int needinbound=purchaseFormEntry.getAmount()-purchaseFormEntry.getTotalin();
					  invpara.setAmount(needinbound);
					  invpara.setWarehouse(form.getWarehouseid());
					  inventoryService.SubStockByStatus(invpara, Status.inbound, Operate.in);
				  }
			}  
			purchaseFormEntry.setTotalin(nowtotalin);
		}
	 
		if(purchaseFormEntry.getTotalin()<purchaseFormEntry.getAmount()) {
			purchaseFormEntry.setInwhstatus(0);
			purchaseFormEntry.setAuditstatus(2);
		}
		purchaseFormEntry.setOperator(user.getId());
		purchaseFormEntry.setOpttime(new Date());
		purchaseFormEntryMapper.updateById(purchaseFormEntry);
		return purchaseFormEntry;
	}
	
	@Transactional
	public PurchaseFormEntry clearReceive(String entryid, UserInfo user) {
		///// 更新entry,totalin,totalre置为0，将totalin的可用库存减掉，待入库改为采购数量，加入收退货记录（t_erp_purchase_form_receive）
		PurchaseFormEntry purchaseFormEntry = purchaseFormEntryMapper.selectById(entryid);
		PurchaseForm form=purchaseFormMapper.selectById(purchaseFormEntry.getFormid());
		if(purchaseFormEntry.getTotalin()==0) {
			throw new ERPBizException("请确认是否已经完成撤销操作，当前没有可以撤销的库存");
		}
		if(purchaseFormEntry.getInwhstatus()==1) {
			throw new ERPBizException("已经完成入库无法撤销，请点击继续收货后执行");
		}
		int amount = purchaseFormEntry.getTotalin();
		InventoryParameter invpara = new InventoryParameter();
		invpara.setFormid(purchaseFormEntry.getFormid());
		invpara.setMaterial(purchaseFormEntry.getMaterialid());
		invpara.setOperator(user.getId());
		invpara.setOpttime(new Date());
		invpara.setStatus(EnumByInventory.alReady);
		invpara.setShopid(user.getCompanyid());
		invpara.setFormtype("purchase");
	 
		PurchaseForm ph = purchaseFormMapper.selectById(purchaseFormEntry.getFormid());
		invpara.setNumber(ph.getNumber());
		invpara.setWarehouse(ph.getWarehouseid());
		if (amount > purchaseFormEntry.getAmount()) {
			invpara.setAmount(purchaseFormEntry.getAmount());
		} else {
			invpara.setAmount(amount);
		}
	
		if (amount > 0) {
			 QueryWrapper<PurchaseFormReceive> queryWrapper = new QueryWrapper<PurchaseFormReceive>();
			 queryWrapper.eq("formentryid", purchaseFormEntry.getId());
			 queryWrapper.orderByDesc("opttime");
		     List<PurchaseFormReceive> reclist = this.baseMapper.selectList(queryWrapper);
			for(PurchaseFormReceive item:reclist) {
				if("in".equals(item.getFtype())) {
					invpara.setWarehouse(item.getWarehouseid());
					invpara.setAmount(item.getAmount());
					inventoryService.SubStockByStatus(invpara, Status.fulfillable, Operate.out);
					item.setFtype("clear");
					item.setRemark("已撤销【通过撤销入库操作】");
					this.baseMapper.updateById(item);
				}else if("out".equals(item.getFtype())) {
					invpara.setWarehouse(item.getWarehouseid());
					invpara.setAmount(item.getAmount());
					inventoryService.AddStockByStatus(invpara, Status.fulfillable, Operate.in);
					item.setFtype("clear");
					item.setRemark("已撤销【通过撤销出库操作】");
					this.baseMapper.updateById(item);
				}
			
			}
		}
		if (amount > purchaseFormEntry.getAmount()) {
			invpara.setAmount(purchaseFormEntry.getAmount());
		} else {
			invpara.setAmount(amount);
		}
		
		invpara.setWarehouse(form.getWarehouseid());
		inventoryService.AddStockByStatus(invpara, Status.inbound, Operate.in);
		purchaseFormEntry.setAuditstatus(2);
		purchaseFormEntry.setInwhstatus(0);
		purchaseFormEntry.setTotalin(0);
		purchaseFormEntry.setTotalre(0);
		purchaseFormEntry.setOperator(user.getId());
		purchaseFormEntry.setOpttime(new Date());
		purchaseFormEntryMapper.updateById(purchaseFormEntry);
		return purchaseFormEntry;
	}
	
	@Override
	@Transactional
	public PurchaseFormEntry deleteReceive(String entryid,  UserInfo userinfo) {
		// TODO Auto-generated method stub
		PurchaseFormEntry purchaseFormEntry = purchaseFormEntryMapper.selectById(entryid);
		if(purchaseFormEntry.getTotalin()==0) {
			throw new BizException("请确认是否已经完成撤销操作，当前没有可以撤销的库存");
		}
		if(purchaseFormEntry.getInwhstatus()==1) {
			throw new BizException("已经完成入库无法撤销，请点击继续收货后执行");
		}
		int amount = purchaseFormEntry.getTotalin();
		InventoryParameter invpara = new InventoryParameter();
		invpara.setFormid(purchaseFormEntry.getFormid());
		invpara.setMaterial(purchaseFormEntry.getMaterialid());
		invpara.setOperator(userinfo.getId());
		invpara.setStatus(EnumByInventory.alReady);
		invpara.setShopid(userinfo.getCompanyid());
		invpara.setFormtype("purchase");
		PurchaseForm ph = purchaseFormMapper.selectById(purchaseFormEntry.getFormid());
		invpara.setNumber(ph.getNumber());
		invpara.setWarehouse(ph.getWarehouseid());
		 Calendar c=Calendar.getInstance();
		if (amount > 0) {
			 LambdaQueryWrapper<PurchaseFormReceive> query =new  LambdaQueryWrapper<PurchaseFormReceive>();
			 query.eq(PurchaseFormReceive::getFormentryid,  purchaseFormEntry.getId());
			 query.orderByDesc(PurchaseFormReceive::getOpttime);
		     List<PurchaseFormReceive> reclist = this.baseMapper.selectList(query);
			for(PurchaseFormReceive item:reclist) {
				c.add(Calendar.SECOND, 2);
				invpara.setOpttime(c.getTime()); 
				if("in".equals(item.getFtype())) {
					invpara.setWarehouse(item.getWarehouseid());
					invpara.setAmount(item.getAmount());
					System.out.println(invpara.getOpttime());
					inventoryService.SubStockByStatus(invpara, Status.fulfillable, Operate.out);
					item.setFtype("clear");
					item.setRemark("已撤销【通过撤销入库操作】");
					this.baseMapper.updateById(item);
				}else if("out".equals(item.getFtype())) {
					invpara.setOpttime(c.getTime()); 
					invpara.setWarehouse(item.getWarehouseid());
					invpara.setAmount(item.getAmount());
					inventoryService.AddStockByStatus(invpara, Status.fulfillable, Operate.in);
					item.setFtype("clear");
					item.setRemark("已撤销【通过撤销出库操作】");
					this.baseMapper.updateById(item);
				}
			
			}
		}
		invpara.setNumber(ph.getNumber());
		invpara.setWarehouse(ph.getWarehouseid());
		c.add(Calendar.SECOND, 2);
		invpara.setOpttime(c.getTime()); 
		if (amount > purchaseFormEntry.getAmount()) {
			invpara.setAmount(purchaseFormEntry.getAmount());
		} else {
			invpara.setAmount(amount);
		}
		inventoryService.AddStockByStatus(invpara, Status.inbound, Operate.in);
		purchaseFormEntry.setAuditstatus(2);
		purchaseFormEntry.setInwhstatus(0);
		purchaseFormEntry.setTotalin(0);
		purchaseFormEntry.setTotalre(0);
		purchaseFormEntry.setOperator(userinfo.getId());
		purchaseFormEntry.setOpttime(new Date());
		purchaseFormEntryMapper.updateById(purchaseFormEntry);
		return purchaseFormEntry;
	}
	
	@Transactional
	public void saveReceive(UserInfo user, PurchaseFormReceive rec,PurchaseFormEntry entry) throws ERPBizException {
		if (entry == null) {
			return;
		}
		Integer oldin = entry.getTotalin();
		if (oldin == null) {
			oldin = 0;
		}
		Integer changeAmount = rec.getAmount();
		if(changeAmount==null) {
			changeAmount=0;
		}
		InventoryParameter invpara = new InventoryParameter();
		invpara.setFormid(entry.getFormid());
		invpara.setMaterial(entry.getMaterialid());
		invpara.setOperator(user.getId());
		invpara.setOpttime(new Date());
		invpara.setStatus(EnumByInventory.alReady);
		invpara.setShopid(user.getCompanyid());
		invpara.setFormtype("purchase");
		PurchaseForm ph =purchaseFormMapper.selectById(entry.getFormid());
		invpara.setNumber(ph.getNumber());
		 if ("in".equals(rec.getFtype())) {// 入库
			entry.setTotalin(oldin + changeAmount);
			invpara.setAmount(0);
			if (entry.getInwhstatus() != 1) {
					if (entry.getTotalin() <= entry.getAmount()) {
						invpara.setAmount(Integer.parseInt(changeAmount.toString()));
					} else if (entry.getAmount() > oldin) {
						invpara.setAmount(entry.getAmount() - oldin);
					} else {
						invpara.setAmount(0);
					}
			} else {
				invpara.setAmount(0);
			}
			
			invpara.setWarehouse(ph.getWarehouseid());
			inventoryService.SubStockByStatus(invpara, Status.inbound, Operate.out);
			////////////// 实际入库仓库与数量，修改与记录//////////////////
			invpara.setAmount(Integer.parseInt(changeAmount.toString()));
			invpara.setWarehouse(rec.getWarehouseid());
			inventoryService.AddStockByStatus(invpara, Status.fulfillable, Operate.in);
		} else {// 退货
			if (oldin - changeAmount < 0) {
				throw new ERPBizException("退货数量不可以大于入库数量");
			}
			Integer oldre = entry.getTotalre();
			if (oldre == null) {
				oldre = 0;
			}
			entry.setTotalre((int) (oldre + changeAmount));
			entry.setTotalin((int) (oldin - changeAmount));

			invpara.setWarehouse(ph.getWarehouseid());
			if (entry.getInwhstatus() != 1) {
					if (entry.getTotalin() < entry.getAmount()) {
						if (oldin > entry.getAmount()) {
							invpara.setAmount(entry.getAmount() - entry.getTotalin());
						} else {
							invpara.setAmount(changeAmount);
						}
					} else {
						invpara.setAmount(0);
					}
					inventoryService.AddStockByStatus(invpara, Status.inbound, Operate.in);
			} else {
				invpara.setAmount(0);
			}
			////////////// 实际入库仓库与数量，修改与记录//////////////////
			invpara.setAmount(Integer.parseInt(changeAmount.toString()));
			invpara.setWarehouse(rec.getWarehouseid());
			inventoryService.SubStockByStatus(invpara, Status.fulfillable, Operate.out);
		}
		if(entry.getAmount()<=entry.getTotalin()) {
			entry.setInwhstatus(1);
			if(entry.getPaystatus()==1) {
				entry.setAuditstatus(3);
				iPurchaseFormPaymentService.handleAvgPrice(entry);
			}
		}
		purchaseFormEntryMapper.updateById(entry);
		if(rec.getAmount()!=0) {
			this.baseMapper.insert(rec);
		}
	}
	
	public List<Map<String, Object>>  selectByEntryid(String id,String paytype) {
		return this.baseMapper.selectByEntryid(id,paytype);
	}
	
	public Integer refreshInbound(String warehouseid,String materialid) {
		return this.baseMapper.refreshInbound(warehouseid, materialid);
	}
	
}
