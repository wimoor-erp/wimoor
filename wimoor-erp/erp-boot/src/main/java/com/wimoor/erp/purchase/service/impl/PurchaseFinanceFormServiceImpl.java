package com.wimoor.erp.purchase.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.finance.mapper.FinanceProjectMapper;
import com.wimoor.erp.finance.pojo.entity.FinAccount;
import com.wimoor.erp.finance.pojo.entity.FinanceProject;
import com.wimoor.erp.finance.service.IFaccountService;
import com.wimoor.erp.purchase.mapper.PurchaseFinanceFormMapper;
import com.wimoor.erp.purchase.mapper.PurchaseFinanceFormPaymentMapper;
import com.wimoor.erp.purchase.mapper.PurchaseFormEntryMapper;
import com.wimoor.erp.purchase.pojo.dto.FinanceFormPayMethDTO;
import com.wimoor.erp.purchase.pojo.dto.PaymentSaveDTO;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFinanceForm;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFinanceFormPayment;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormEntry;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormPayment;
import com.wimoor.erp.purchase.service.IPurchaseFinanceFormService;
import com.wimoor.erp.purchase.service.IPurchaseFormPaymentService;
import com.wimoor.erp.purchase.service.IPurchaseFormService;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;

@Service("purchaseFinanceFormService")
@RequiredArgsConstructor
public class PurchaseFinanceFormServiceImpl extends  ServiceImpl<PurchaseFinanceFormMapper,PurchaseFinanceForm> implements IPurchaseFinanceFormService {
	
	final PurchaseFinanceFormPaymentMapper purchaseFinanceFormPaymentMapper;
	final ISerialNumService serialNumService;
	final IFaccountService faccountService;
	final FinanceProjectMapper financeProjectMapper;
	final PurchaseFormEntryMapper purchaseFormEntryMapper;
	final IPurchaseFormService purchaseFormService;
	final IPurchaseFormPaymentService iPurchaseFormPaymentService;
	@Override
	@Transactional
	public Map<String, Object> applyPayment(UserInfo user, PurchaseFormEntry entry, PaymentSaveDTO dto) {
		Map<String, Object> maps=new HashedMap<String, Object>();
		//先保存form 再保存payment
		String shopid=user.getCompanyid();
		String number=null;
		try {
			number = serialNumService.readSerialNumber(shopid, "PA");
		} catch (Exception e) {
			e.printStackTrace();
			throw new BizException("系统编码出错！");
		}
		Integer paymethodValue=null;
		if(StrUtil.isNotEmpty(dto.getPaymethod())) {
			paymethodValue=Integer.parseInt(dto.getPaymethod());
		}
		PurchaseFinanceForm form=new PurchaseFinanceForm();
		form.setOpttime(new Date());
		form.setCreatetime(new Date());
		form.setCreator(user.getId());
		form.setOperator(user.getId());
		form.setEntryid(entry.getId());
		form.setShopid(shopid);
		form.setNumber(number);
		form.setPaymentMethod(paymethodValue);
		form.setRemark(dto.getRemark());
		form.setAuditstatus(0);//未审核
		int isformok = this.baseMapper.insert(form);
		if(isformok>0) {
			//处理feelist和cost ship的payment
			JSONArray feeArray = null;
			if(dto.getFeelist().contains("{")) {
				feeArray=GeneralUtil.getJsonArray("["+dto.getFeelist()+"]");	
			}
			//做cost和ship的payment新增
			String payacc=dto.getPayacc();
			if(StrUtil.isBlankOrUndefined(payacc)) {
				FinAccount account =faccountService.getAccByMeth(shopid,dto.getPaymethod());
				payacc=account.getId();
			} 
			PurchaseFinanceFormPayment paymentcost = null;
			PurchaseFinanceFormPayment paymentship = null;
			if (StrUtil.isNotEmpty(dto.getCostamount()) && !dto.getCostamount().equals("0")) {
				try {
					BigDecimal iamount = new BigDecimal(dto.getCostamount().trim());
					paymentcost = new PurchaseFinanceFormPayment();
					paymentcost.setFormentryid(entry.getId());
					paymentcost.setProjectid(PurchaseFormPaymentServiceImpl.type_cost);
					paymentcost.setPayprice(iamount);
					paymentcost.setRemark(null);
					paymentcost.setFormid(form.getId());
					paymentcost.setOpttime(new Date());
					paymentcost.setCreatedate(new Date());
					paymentcost.setOperator(user.getId());
					paymentcost.setAcct(payacc);
					purchaseFinanceFormPaymentMapper.insert(paymentcost);
				} catch (Exception e) {
					throw new BizException("付款金额不能包含非数字字符");
				}
			}
			if (StrUtil.isNotEmpty(dto.getShipamount()) && !dto.getShipamount().equals("0")) {
				try {
					BigDecimal iamount = new BigDecimal(dto.getShipamount().trim());
					paymentship = new PurchaseFinanceFormPayment();
					paymentship.setFormentryid(entry.getId());
					paymentship.setProjectid(PurchaseFormPaymentServiceImpl.type_ship);
					paymentship.setPayprice(iamount);
					paymentship.setRemark(null);
					paymentship.setOpttime(new Date());
					paymentship.setOperator(user.getId());
					paymentship.setAcct(payacc);
					paymentship.setCreatedate(new Date());
					paymentship.setFormid(form.getId());
					purchaseFinanceFormPaymentMapper.insert(paymentship);
				} catch (Exception e) {
					throw new BizException("付款金额不能包含非数字字符");
				}
			}
			
			//做新增其它项目费用feelist操作
			if(feeArray!=null && feeArray.size()>0) {
				for(int i=0;i<feeArray.size();i++) {
					JSONObject obj = feeArray.getJSONObject(i);
					BigDecimal amount = obj.getBigDecimal("amount");
				    if(amount.compareTo(new BigDecimal("0"))==0) {
				    	continue;
				    }
				    PurchaseFinanceFormPayment paymentOther =new PurchaseFinanceFormPayment();
					String objectid = obj.getString("objectid");
					paymentOther.setProjectid(objectid);
					paymentOther.setFormentryid(entry.getId());
					paymentOther.setFormid(form.getId());
					paymentOther.setPayprice(amount);
					paymentOther.setOpttime(new Date());
					paymentOther.setOperator(user.getId());
					paymentOther.setRemark(null);
					paymentOther.setAcct(payacc);
					paymentOther.setCreatedate(new Date());
					if(paymentOther!=null) {
						purchaseFinanceFormPaymentMapper.insert(paymentOther);
					}
				}
			}
		}
		
		maps.put("isok", "true");
		return maps;
	}

	@Override
	public IPage<Map<String, Object>> findByCondition(Page<?> page, Map<String, Object> param) {
		return this.baseMapper.findByCondition(page, param);
	}

	@Override
	public Map<String, Object> getDetailMap(String formid, String shopid) {
		Map<String,Object> map=new HashMap<String, Object>();
		Map<String,Object> formData = this.baseMapper.findFormId(formid);
		QueryWrapper<PurchaseFinanceFormPayment> queryWrapper=new QueryWrapper<PurchaseFinanceFormPayment>();
		queryWrapper.eq("formid", formid);
		List<PurchaseFinanceFormPayment> paylist = purchaseFinanceFormPaymentMapper.selectList(queryWrapper);
		if(paylist!=null && paylist.size()>0) {
			for (int i = 0; i < paylist.size(); i++) {
				PurchaseFinanceFormPayment payitem = paylist.get(i);
				if(payitem.getProjectid()!=null) {
					FinanceProject project = financeProjectMapper.selectById(payitem.getProjectid());
					if(project!=null) {
						payitem.setProjectname(project.getName());
					}
				}
			}
		}
		map.put("form", formData);
		map.put("paylist", paylist);
		return map;
	}

	@Override
	public Map<String, Object> approve(List<String> idsList, UserInfo user) {
		Map<String, Object> map=new HashMap<String, Object>();
		if(idsList!=null && idsList.size()>0) {
			int count=0;
			for (int i = 0; i < idsList.size(); i++) {
				String formid = idsList.get(i);
				PurchaseFinanceForm form = this.baseMapper.selectById(formid);
				if(form.getAuditstatus()==0) {
					form.setAuditstatus(1);
					form.setOpttime(new Date());
					form.setAudittime(new Date());
					form.setOperator(user.getId());
					form.setAuditor(user.getId());
					count+=this.baseMapper.updateById(form);
				}
			}
			if(count>0) {
				map.put("isok", true);
			}
			return map;
		}else {
			return null;
		}
	}

	@Override
	public Map<String, Object> updatePay(FinanceFormPayMethDTO dto, UserInfo user) {
		Map<String, Object> map=new HashMap<String, Object>();
		if(dto.getEntryList()!=null && dto.getEntryList().size()>0) {
			int count=0;
			String paymethod =dto.getPaymethod();
			String payacc=dto.getPayacc();
			if(StrUtil.isBlankOrUndefined(payacc)) {
				FinAccount account =faccountService.getAccByMeth(user.getCompanyid(),paymethod);
				payacc=account.getId();
			} 
			for (int i = 0; i < dto.getEntryList().size(); i++) {
				String formid = dto.getEntryList().get(i);
				PurchaseFinanceForm form = this.baseMapper.selectById(formid);
				form.setOpttime(new Date());
				form.setOperator(user.getId());
				form.setPaymentMethod(Integer.parseInt(paymethod));
				count+=this.baseMapper.updateById(form);
			}
			if(count>0) {
				map.put("isok", true);
			}
			return map;
		}else {
			return null;
		}
	}

	@Override
	public Map<String, Object> updateRemark(String id,String remark, UserInfo userinfo) {
		Map<String, Object> map=new HashMap<String, Object>();
		PurchaseFinanceForm form = this.baseMapper.selectById(id);
		form.setRemark(remark);
		int count = this.baseMapper.updateById(form);
		if(count>0) {
			map.put("isok", true);
			return map;
		}else {
			return null;
		}
	}

	@Override
	public Map<String, Object> approveReturn(List<String> idsList, UserInfo user) {
		Map<String, Object> map=new HashMap<String, Object>();
		if(idsList!=null && idsList.size()>0) {
			int count=0;
			for (int i = 0; i < idsList.size(); i++) {
				String formid = idsList.get(i);
				PurchaseFinanceForm form = this.baseMapper.selectById(formid);
				if(form.getAuditstatus()==0 || form.getAuditstatus()==1) {
					form.setAuditstatus(3);
					form.setOpttime(new Date());
					form.setAudittime(new Date());
					form.setOperator(user.getId());
					form.setAuditor(user.getId());
					count+=this.baseMapper.updateById(form);
					//最后检查一下还有没有finpayform  没有的话entry的paystatus的3就为0
					PurchaseFormEntry entry = purchaseFormEntryMapper.selectById(form.getEntryid());
					if(entry!=null && entry.getPaystatus()==3) {
						List<String> querylist=new ArrayList<String>();
						querylist.add("0");
						querylist.add("1");
						QueryWrapper<PurchaseFinanceForm> queryWrappers = new QueryWrapper<PurchaseFinanceForm>();
						queryWrappers.eq("entryid",entry.getId());
						queryWrappers.in("auditstatus", querylist);
						List<PurchaseFinanceForm> paylist = this.baseMapper.selectList(queryWrappers);
						if(paylist==null || paylist.size()<=0) {
							//就需要修改当前entry的paystatus=0
							entry.setPaystatus(0);
							entry.setOpttime(new Date());
							entry.setOperator(user.getId());
						}
						purchaseFormEntryMapper.updateById(entry);
					}
				}
			}
			if(count>0) {
				map.put("isok", true);
			}
			return map;
		}else {
			return null;
		}
	}

	@Override
	public List<Map<String, Object>> getDetailData(List<String> ids,String shopid) {
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("shopid", shopid);
		map.put("idlist", ids);
		List<Map<String, Object>> list=this.baseMapper.getDataList(map);
		return list;
	}

	@Override
	@Transactional
	public Map<String, Object> paymentForm(List<String> ids, UserInfo user) {
		Map<String, Object> map=new HashMap<String, Object>();
		if(ids!=null && ids.size()>0) {
			int result=0;
			for (int i = 0; i < ids.size(); i++) {
				String finformid = ids.get(i);
				PurchaseFinanceForm finform = this.baseMapper.selectById(finformid);
				if(finform!=null && finform.getAuditstatus()==1) {
					finform.setAuditstatus(2);
					finform.setOpttime(new Date());
					finform.setOperator(user.getId());
					int count = this.baseMapper.updateById(finform);
					if(count>0) {
						handleEntryPayment(finform,user);
						result++;
					}
				}
			}
			if(result>0) {
				map.put("isok",true);
			}
			return map;
		}else {
			return null;
		}
	}

	//同步操作purchase的entry
	private void handleEntryPayment(PurchaseFinanceForm finform,UserInfo user) {
		QueryWrapper<PurchaseFinanceFormPayment> queryWrapper=new QueryWrapper<PurchaseFinanceFormPayment>();
		queryWrapper.eq("formid", finform.getId());
		List<PurchaseFinanceFormPayment> finpaylist = purchaseFinanceFormPaymentMapper.selectList(queryWrapper);
		if(finpaylist!=null && finpaylist.size()>0) {
			//finpaylist转paymentlist
			PurchaseFormEntry entry = purchaseFormEntryMapper.selectById(finform.getEntryid());
			List<PurchaseFormPayment> paymentlist = finconvertPayment(finpaylist,user);
			if(paymentlist!=null && paymentlist.size()>0) {
				
				iPurchaseFormPaymentService.updatePayment(paymentlist, entry, null,user);
				//最后检查一下还有没有finpayform  没有的话entry的paystatus的3就为0
				if(entry.getPaystatus()==3) {
					List<String> querylist=new ArrayList<String>();
					querylist.add("0");
					querylist.add("1");
					QueryWrapper<PurchaseFinanceForm> queryWrappers = new QueryWrapper<PurchaseFinanceForm>();
					queryWrappers.eq("entryid",entry.getId());
					queryWrappers.in("auditstatus", querylist);
					List<PurchaseFinanceForm> paylist = this.baseMapper.selectList(queryWrappers);
					if(paylist==null || paylist.size()<=0) {
						//就需要修改当前entry的paystatus=0
						entry.setPaystatus(0);
						entry.setOpttime(new Date());
						entry.setOperator(user.getId());
					}
					purchaseFormEntryMapper.updateById(entry);
				}
			}else {
				throw new BizException("确认付款操作失败！");
			}
		}
	}
	
	private List<PurchaseFormPayment> finconvertPayment(List<PurchaseFinanceFormPayment> finpaylist,UserInfo user) {
		if(finpaylist!=null && finpaylist.size()>0) {
			List<PurchaseFormPayment> paymentlist=new ArrayList<PurchaseFormPayment>();
			for (int i = 0; i < finpaylist.size(); i++) {
				PurchaseFinanceFormPayment finpay = finpaylist.get(i);
				PurchaseFinanceForm finform = this.baseMapper.selectById(finpay.getFormid());
				FinAccount account =faccountService.getAccByMeth(user.getCompanyid(),finform.getPaymentMethod().toString());
				String payacc = account.getId();
				if(finform!=null) {
					PurchaseFormPayment payment=new PurchaseFormPayment();
					payment.setAuditstatus(1);
					payment.setFormentryid(finpay.getFormentryid());
					payment.setProjectid(finpay.getProjectid());
					payment.setPayprice(finpay.getPayprice());
					payment.setRemark(finpay.getRemark());
					payment.setOpttime(new Date());
					payment.setOperator(user.getId());
					payment.setPaymentMethod(finform.getPaymentMethod());
					payment.setAcct(payacc);
					paymentlist.add(payment);
				}
			}
			return paymentlist;
		}
		return null;
	}

	
}
