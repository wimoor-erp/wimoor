package com.wimoor.erp.purchase.service.impl;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.common.pojo.entity.ERPBizException;
import com.wimoor.erp.finance.pojo.entity.FinAccount;
import com.wimoor.erp.finance.service.IFaccountService;
import com.wimoor.erp.inventory.service.IInventoryService;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.purchase.mapper.PurchaseFormEntryMapper;
import com.wimoor.erp.purchase.mapper.PurchaseFormMapper;
import com.wimoor.erp.purchase.mapper.PurchaseFormPaymentMapper;
import com.wimoor.erp.purchase.pojo.entity.PurchaseForm;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormEntry;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormPayment;
import com.wimoor.erp.purchase.service.IPurchaseFormPaymentService;

import lombok.RequiredArgsConstructor;

@Service("purchaseFormPaymentService")
@RequiredArgsConstructor
public class PurchaseFormPaymentServiceImpl extends  ServiceImpl<PurchaseFormPaymentMapper,PurchaseFormPayment> implements IPurchaseFormPaymentService {
	@Autowired
	final IMaterialService materialService;
	public static String type_cost = "26138972997300240";
	public static String type_ship = "26138972997300238";
	final PurchaseFormEntryMapper purchaseFormEntryMapper;
	final IFaccountService faccountService;
	final PurchaseFormMapper purchaseFormMapper;
	@Autowired
	IInventoryService inventoryService;
	public int removePay(String id) {
		return this.baseMapper.deleteById(id);
	}
	public int updatePaymentStatus(String paymentid, String status,String remark,UserInfo user,String deliverydate) {
		int result=0;
		//不管是驳回还是通过 需要探一下当前的entry是否还存在状态为2的payment  有的话就需要改变entry的paystatus
		PurchaseFormPayment payment = this.baseMapper.selectById(paymentid);
		if(payment!=null) {
			String entryid = payment.getFormentryid();
			PurchaseFormEntry entry = purchaseFormEntryMapper.selectById(entryid);
			FinAccount account=faccountService.readFinAccount(user.getCompanyid());
			if(GeneralUtil.isNotEmpty(status)) {
				payment.setAuditstatus(Integer.parseInt(status));
			}
			if(entry!=null) {
				if("1".equals(status)) {
					if(deliverydate!=null && deliverydate!="") {
						entry.setDeliverydate(GeneralUtil.getDatez(deliverydate));
					}
					BigDecimal oldpay = entry.getTotalpay();
					PurchaseFormPayment oldpayment = this.baseMapper.selectById(payment.getId());
					if (oldpayment != null) {
						if (oldpay == null) {
							oldpay = new BigDecimal("0");
						}
						if (type_cost.endsWith(payment.getProjectid())) {
							if(status.equals("1")) {
								entry.setTotalpay(oldpay.add(payment.getPayprice()));
							}
						}
					} else {
						if (oldpay == null) {
							oldpay = new BigDecimal("0");
						}
						if (type_cost.endsWith(payment.getProjectid())) {
							if(status.equals("1")) {
								entry.setTotalpay(oldpay.add(payment.getPayprice()));
							}
						}
					}
				}
			}
			payment.setOpttime(new Date());
			payment.setOperator(user.getId());
			payment.setRemark(remark);
			//通过审核的payment需要同步记录到finaccount
			if("1".equals(status)) {
				saveRecord(account, payment, null, user);
			}
			result+=this.baseMapper.updateById(payment);
			
			QueryWrapper<PurchaseFormPayment> queryWrapper = new QueryWrapper<PurchaseFormPayment>();
			queryWrapper.eq("formentryid",entryid);
			queryWrapper.eq("auditstatus",2);
			List<PurchaseFormPayment> paylist = this.baseMapper.selectList(queryWrapper);
			if(paylist==null || paylist.size()<=0) {
				//就需要修改当前entry的paystatus=0
				entry.setPaystatus(0);
				entry.setOpttime(new Date());
				entry.setOperator(user.getId());
			}
			if(purchaseFormEntryMapper.updateById(entry)>0) {
				result++;
			}
			
		}
		return result;
	}
	

	// save一系列finance
	private void saveRecord(FinAccount account, PurchaseFormPayment payment, PurchaseFormPayment oldpayment, UserInfo user) {
		String projectid = payment.getProjectid();
		Date createtime = payment.getCreatedate();
		BigDecimal amount = null;
		if (oldpayment != null) {
			amount = payment.getPayprice().subtract(oldpayment.getPayprice());
		} else {
			amount = payment.getPayprice();
		}
		String ftype = null;
		if (amount.compareTo(new BigDecimal("0")) > 0) {
			ftype = "out";
		} else {
			ftype = "in";
			amount = amount.multiply(new BigDecimal("-1"));
		}
		if(!account.getShopid().equals(user.getCompanyid())) {
			throw new BizException("账户异常，请确认是否切换账号后没有刷新");
		}
		faccountService.updateFinAfterChange(account, projectid, createtime, amount, ftype);
	}

	
	@Override
	public void setPaymentReportExcel(SXSSFWorkbook workbook, Map<String, Object> param) {
		// TODO Auto-generated method stub
		Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
		titlemap.put("number", "订单编码");
		titlemap.put("createdate", "创建日期");
		titlemap.put("cname", "供应商");
		titlemap.put("sku", "SKU");
		titlemap.put("mname", "产品名称");
		titlemap.put("paystatus", "付款状态");
		titlemap.put("purchases", "订单采购量");
		titlemap.put("totalin", "订单已入库");
		titlemap.put("orderprice", "订单采购金额");
		titlemap.put("totalpay", "订单已付款");
		titlemap.put("payment_method", "付款方式");
		titlemap.put("fee_type", "费用类型");
		titlemap.put("payprice", "付款金额");
		titlemap.put("name", "操作人");
		titlemap.put("opttime", "付款日期");
		titlemap.put("remark", "备注");
		titlemap.put("wname", "仓库");
		
		List<Map<String,Object>> list = this.baseMapper.paymentReport(param);
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
				Map<String, Object> map = list.get(i);
				for (int j = 0; j < titlearray.length; j++) {
					Cell cell = row.createCell(j); // 在索引0的位置创建单元格(左上端)
					String fieldname=titlearray[j].toString();
					Object value=map.get(fieldname);
					if(value!=null) {
						if(fieldname.equals("paystatus")) {
							cell.setCellValue(value.toString().equals("1")?"已完成":"未完成");
						}else {
							cell.setCellValue(value.toString());
						}
					}else {
						cell.setCellValue("");
					}
					 
				}
			}
		}
	}

	public IPage<Map<String, Object>> getPaymentReport(Page<?> page, Map<String, Object> param) {
		 IPage<Map<String, Object>>  result = this.baseMapper.paymentReport(page,param);
		 Map<String, Object> summary = this.baseMapper.paymentReportSummary(param);
		if(result.getRecords()!=null&&result.getRecords().size()>0&&summary!=null) {
			result.getRecords().get(0).putAll(summary);
		}
		return result;
	}
	
	@Override
	public void cancelPayment(PurchaseFormPayment payment,String paytype, UserInfo user) {
		// TODO Auto-generated method stub
		if(GeneralUtil.distanceOfDay(payment.getCreatedate(), new Date())>60) {
			throw new BizException("超过60天无法撤销");
		}
		payment.setAuditstatus(0);
		PurchaseFormEntry entry = purchaseFormEntryMapper.selectById(payment.getFormentryid());
		if(entry.getPaystatus()==1) {
			throw new BizException("付款已完成，无法操作，请点击继续付款");
		}
		BigDecimal haspay=new BigDecimal("0");
		LambdaQueryWrapper<PurchaseFormPayment> query=new LambdaQueryWrapper<PurchaseFormPayment>();
		query.eq(PurchaseFormPayment::getFormentryid, payment.getFormentryid());
		List<PurchaseFormPayment> list = this.baseMapper.selectList(query);
		for(PurchaseFormPayment item:list) {
				if (type_cost.endsWith(item.getProjectid())) {
					if(item.getAuditstatus()==1) {
						haspay=haspay.add(item.getPayprice());
					}
				}
		}
		if(entry.getTotalpay()!=null) {
			if(type_cost.endsWith(payment.getProjectid())) {
				entry.setTotalpay(haspay.subtract(payment.getPayprice()));
				purchaseFormEntryMapper.updateById(entry);
			}
			
		}
		this.baseMapper.updateById(payment);
		FinAccount account = faccountService.getById(payment.getAcct());
		String actiontype=payment.getPayprice().floatValue()>0?"out":"in";
		BigDecimal price =payment.getPayprice().floatValue()>0? payment.getPayprice():payment.getPayprice().multiply(new BigDecimal("-1"));
		faccountService.updateFinCancelChange(account, payment.getProjectid(), 	payment.getCreatedate(),price, actiontype);
	}
	

	@Transactional
	public int updatePayment(List<PurchaseFormPayment> paymentlist,PurchaseFormEntry entry,FinAccount account, UserInfo user) throws ERPBizException {
		int result = 0;
		BigDecimal oldpay = entry.getTotalpay();
		if (oldpay == null) {
			oldpay = new BigDecimal("0");
		}
		if(paymentlist!=null&&paymentlist.size()>0) {
			for(PurchaseFormPayment payment:paymentlist) {
				if(account==null) {
					account=faccountService.getById(payment.getAcct());
				}
				PurchaseFormPayment oldpayment = this.baseMapper.selectById(payment.getId());
				if (oldpayment != null) {
					if(this.updateById(payment)) {
						result++ ;
					}
				} else {
					payment.setFormentryid(entry.getId());
					payment.setCreatedate(new Date());
					payment.setAcct(account.getId());
					if(this.save(payment)) {
						result++ ;
					}
				}
				saveRecord(account, payment, oldpayment, user);
			}
			BigDecimal haspay=new BigDecimal("0");
			LambdaQueryWrapper<PurchaseFormPayment> query=new LambdaQueryWrapper<PurchaseFormPayment>();
			query.eq(PurchaseFormPayment::getFormentryid, entry.getId());
			List<PurchaseFormPayment> list = this.baseMapper.selectList(query);
			for(PurchaseFormPayment item:list) {
					if (type_cost.endsWith(item.getProjectid())&&item.getAuditstatus()==1) {
						 haspay=haspay.add(item.getPayprice());
					}
			}
			entry.setTotalpay(haspay);
			if(entry.getTotalpay().compareTo(entry.getOrderprice())>=0) {
				entry.setPaystatus(1);
			}
			if(entry.getPaystatus()==1 && entry.getInwhstatus()==1) {
				entry.setAuditstatus(3);
				//入库和付款都做完了 就可以做加权平均价格
				handleAvgPrice(entry);
			}
			if(purchaseFormEntryMapper.updateById(entry)>0) {
				result ++;
			}
		}
		
		return result;
	}
	
	public boolean restartPay(PurchaseFormEntry entry) {
		// TODO Auto-generated method stub
		if(entry==null)return false;
		entry.setPaystatus(0);
		if (entry.getAuditstatus() == 3) {
			entry.setAuditstatus(2);
		}
		boolean result = purchaseFormEntryMapper.updateById(entry)>0;
		return result;
	}
	
	public int closePay(PurchaseFormEntry entry) throws ERPBizException {
		if(entry==null)return 0;
		entry.setPaystatus(1);
		if (entry.getInwhstatus() == 1) {
			entry.setAuditstatus(3);
		}
		entry.setClosepaydate(new Date());
		int result = 0;
		if(purchaseFormEntryMapper.updateById(entry)>0) {
			result++;
		}
		return result;
	}
	
	 
	
	//处理sku的加权平均价格
		 public void handleAvgPrice(PurchaseFormEntry entry) {
			String materialid = entry.getMaterialid();
			PurchaseForm form = purchaseFormMapper.selectById(entry.getFormid());  
			String shopid= form.getShopid();
			Map<String,Object> maps=new HashMap<String, Object>();
			maps.put("skuid", materialid);
			maps.put("shopid", shopid);
			Map<String, Object> invmap = inventoryService.findSum(maps);
			int totalinv=0;
			if(invmap!=null && invmap.get("fulfillable")!=null) {
				totalinv=Integer.parseInt(invmap.get("fulfillable").toString());
			}
			//本次采购的totalpay  就是商品采购总价
			BigDecimal totalprice = entry.getTotalpay();
			//本次采购的totalin  就是商品采购总数量
			Integer amount = entry.getTotalin();
			BigDecimal  othercost=new BigDecimal(0); 
			if(totalprice.compareTo(new BigDecimal(0))>0 && amount>0) {
				//查询所有的paylist 算出其它总费用（包含运费）
				QueryWrapper<PurchaseFormPayment> query=new QueryWrapper<PurchaseFormPayment>();
				query.eq("formentryid", entry.getId());
				List<PurchaseFormPayment> paylist = this.list(query);
				if(paylist!=null && paylist.size()>0) {
					for (int i = 0; i < paylist.size(); i++) {
						PurchaseFormPayment payitem = paylist.get(i);
						if(type_cost.equals(payitem.getProjectid())) {
							continue;
						}else {
							if(payitem.getPayprice()!=null) {
								othercost=othercost.add(payitem.getPayprice());
							}
						}
					}
				}
				//采购前本地库存数量=现有可用库存-totalin
				int amount_before=0;
				if(totalinv>0) {
					amount_before=totalinv-amount;
				}
				Material material = materialService.getById(materialid);
				BigDecimal avgprice_before = null;
				BigDecimal avgother_before=null;
				//当之前没有加权价格的时候  第一次采购平均单价就是当前这次
				if(material.getPriceWavg()==null || material.getPriceWavg().compareTo(new BigDecimal(0))<=0) {
					//采购前的 平均价格和其它费用
					avgprice_before = totalprice.divide(new BigDecimal(amount),2, RoundingMode.HALF_UP);
				}else {
					//第二次 直接用以前存的值
					avgprice_before = material.getPriceWavg();
				}
				if(material.getPriceShipWavg()==null || material.getPriceShipWavg().compareTo(new BigDecimal(0))<=0) {
					avgother_before = othercost.divide(new BigDecimal(amount),2, RoundingMode.HALF_UP);
				}else {
					avgother_before = material.getPriceShipWavg();
				}
				BigDecimal avgprice_after=new BigDecimal(0);
				BigDecimal avgother_after=new BigDecimal(0);
				if( totalinv!=0) {
					avgprice_after=(avgprice_before.multiply(new BigDecimal(amount_before)).add(totalprice)).divide(new BigDecimal(totalinv),
							2, RoundingMode.HALF_UP);
					avgother_after=(avgother_before.multiply(new BigDecimal(amount_before)).add(othercost)).divide(new BigDecimal(totalinv),
							4, RoundingMode.HALF_UP);
					material.setPriceWavg(avgprice_after);//货物费用加权平均
					if(othercost.compareTo(new BigDecimal(0))>0) {
						material.setPriceShipWavg(avgother_after);//其它费用加权平均
					}
					materialService.updateById(material);
				}
			}
		}
		
	public List<PurchaseFormPayment> getPaymentByEntryid(String id,String paytype) {
		QueryWrapper<PurchaseFormPayment> queryWrapper = new QueryWrapper<PurchaseFormPayment>();
		queryWrapper.eq("formentryid", id);
		if(paytype.equals("out")) {
			queryWrapper.gt("payprice", 0);
		}else if(paytype.equals("in")){
			queryWrapper.lt("payprice", 0);
		}
		queryWrapper.orderByDesc("opttime");
		return this.baseMapper.selectList(queryWrapper);
	}
}
