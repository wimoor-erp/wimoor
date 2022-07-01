package com.wimoor.erp.finance.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.config.IniConfig;
import com.wimoor.erp.finance.mapper.FinAccountMapper;
import com.wimoor.erp.finance.mapper.FinJournalAccountMapper;
import com.wimoor.erp.finance.mapper.FinJournalDailyMapper;
import com.wimoor.erp.finance.mapper.FinTypeJournalMonthlyMapper;
import com.wimoor.erp.finance.mapper.FinanceProjectMapper;
import com.wimoor.erp.finance.pojo.entity.FinAccount;
import com.wimoor.erp.finance.pojo.entity.FinJournalAccount;
import com.wimoor.erp.finance.pojo.entity.FinJournalDaily;
import com.wimoor.erp.finance.pojo.entity.FinTypeJournalMonthly;
import com.wimoor.erp.finance.pojo.entity.FinanceProject;
import com.wimoor.erp.finance.service.IFaccountService;
import com.wimoor.erp.purchase.mapper.PurchaseFormPaymentMethodMapper;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormPaymentMethod;

import lombok.RequiredArgsConstructor;
 

@Service("faccountService")
@RequiredArgsConstructor
public class FaccountServiceImpl extends ServiceImpl<FinJournalAccountMapper,FinJournalAccount> implements IFaccountService {
	 
	FinJournalAccountMapper finJournalAccountMapper;
	 
	FinAccountMapper finAccountMapper;
	 
	FinanceProjectMapper financeProjectMapper;
	 
	FinJournalDailyMapper finJournalDailyMapper;
	 
	FinTypeJournalMonthlyMapper finTypeJournalMonthlyMapper;
	 
	PurchaseFormPaymentMethodMapper purchaseFormPaymentMethodMapper;

	public List<Map<String, Object>> findDetialByCondition(Map<String, Object> param) {
		return finJournalAccountMapper.findDetialByCondition(param);
	}

	public List<Map<String, Object>> findByCondition(Map<String, Object> param) {
		return finJournalAccountMapper.findSumDayByCondition(param);
	}

	public Map<String, Object> findSumByCondition(Map<String, Object> param) {
		return finJournalAccountMapper.findSumByCondition(param);
	}

	public List<Map<String, Object>> paymentSum(String shopid) {
		return finJournalAccountMapper.paymentSum(shopid);
	}

	public List<Map<String, Object>> paymentSumByCondition(Map<String, Object> param) {
		return finJournalAccountMapper.paymentSumByCondition(param);
	}

	public void setExcelBook(SXSSFWorkbook workbook, Map<String, Object> param) {
		Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
		titlemap.put("name", "项目");
		titlemap.put("number", "订单编码");
		titlemap.put("sku", "SKU");
		titlemap.put("createtime", "日期");
		titlemap.put("ftype", "支付类型");
		titlemap.put("amount", "金额（￥）");
		titlemap.put("remark", "备注");
		List<Map<String, Object>> list = finJournalAccountMapper.findByCondition(param);

		Sheet sheet = workbook.createSheet("sheet1");
		// 在索引0的位置创建行（最顶端的行）
		Row trow = sheet.createRow(0);
		Object[] titlearray = titlemap.keySet().toArray();
		for (int i = 0; i < titlearray.length; i++) {
			Cell cell = trow.createCell(i); // 在索引0的位置创建单元格(左上端)
			Object value = titlemap.get(titlearray[i].toString());
			cell.setCellValue(value.toString());
		}
		for (int i = 0; i < list.size(); i++) {
			Row row = sheet.createRow(i + 1);
			Map<String, Object> map = list.get(i);
			for (int j = 0; j < titlearray.length; j++) {
				Cell cell = row.createCell(j); // 在索引0的位置创建单元格(左上端)
				Object value = map.get(titlearray[j].toString());
				if (value != null) {
					if ("ftype".equals(titlearray[j].toString())) {
						if ("out".equals(value)) {
							value = "支出";
						} else {
							value = "收入";
						}
					}
					cell.setCellValue(value.toString());
				}
			}
		}
	}

	public BigDecimal getSummary(UserInfo currUser) {
		FinAccount account = finAccountMapper.findAcBanlance(currUser.getCompanyid());
		BigDecimal result = new BigDecimal("0");
		if (account != null) {
			result = account.getBalance();
		}
		return result;
	}

	// 同步添加record记录 账户表 日账单表 月账单类型表
	public boolean saveRecord(FinJournalAccount faccount, FinJournalAccount old) throws BizException {
		// 操作账户表
		boolean result = false;
		FinAccount account = readFinAccount(faccount.getShopid());
		BigDecimal banlance = account.getBalance();
		BigDecimal amount = null;
		String ftype = faccount.getFtype();
		if (old != null) {
			amount = old.getAmount().subtract(faccount.getAmount());
			if (amount.compareTo(new BigDecimal("0")) < 0) {
				ftype = "in".equals(ftype) ? "out" : "in";
			}
		} else {
			amount = faccount.getAmount();
		}
		BigDecimal nowban = null;
		if (ftype.equals("in")) {
			nowban = banlance.add(amount);
		} else {
			nowban = banlance.subtract(amount);
		}
		faccount.setBalance(nowban);
		if (old == null) {
			faccount.setAcct(account.getId());
			result = super.save(faccount);
		} else {
			result = super.updateById(faccount);
		}
		updateFinAfterChange(account, faccount.getProjectid(), faccount.getCreatetime(), amount, ftype);
		return result;
	}

	public void updateFinAfterChange(FinAccount account, String projectid, Date createtime, BigDecimal amount, String ftype) {
		saveFinAccount(account, amount, ftype);
		saveFinDaily(account, createtime, amount, ftype);
		saveFinMonthly(account, projectid, createtime, amount, ftype);
	}

	public FinAccount readFinAccount(String shopid) {
		QueryWrapper<FinAccount> queryWrapper=new QueryWrapper<FinAccount>();
		queryWrapper.eq("shopid", shopid);
		List<FinAccount> list = finAccountMapper.selectList(queryWrapper);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			FinAccount record = new FinAccount();
			record.setBalance(new BigDecimal("0"));
			record.setShopid(shopid);
			return record;
		}
	}

	public void saveFinAccount(FinAccount account, BigDecimal amount, String ftype) {
		BigDecimal banlance = account.getBalance();
		BigDecimal nowban = null;
		if (ftype.equals("in")) {
			nowban = banlance.add(amount);
		} else {
			nowban = banlance.subtract(amount);
		}
		account.setBalance(nowban);
		if (account.getCreatedate() == null) {
			account.setCreatedate(new Date());
			finAccountMapper.insert(account);
		} else {
			finAccountMapper.updateById(account);
		}
	}

	// 日账单表操作
	public void saveFinDaily(FinAccount account, Date createtime, BigDecimal amount, String ftype) {
		BigDecimal nowban = account.getBalance();
		FinJournalDaily oldfindaily = finJournalDailyMapper.selectByday(GeneralUtil.formatDate(createtime), account.getId());
		if (oldfindaily != null) {
			oldfindaily.setBalance(nowban);
			if (ftype.equals("in")) {
				oldfindaily.setRec(amount.add(oldfindaily.getRec()));
			} else {
				oldfindaily.setPay(amount.add(oldfindaily.getPay()));
			}
			finJournalDailyMapper.updateById(oldfindaily);
		} else {
			FinJournalDaily finDaily = new FinJournalDaily();
			finDaily.setAcct(account.getId());
			finDaily.setBalance(account.getBalance());
			String dateStr = GeneralUtil.formatDate(createtime);
			finDaily.setByday(GeneralUtil.getDatez(dateStr));
			if (ftype.equals("in")) {
				finDaily.setRec(amount);
			} else {
				finDaily.setPay(amount);
			}
			finJournalDailyMapper.insert(finDaily);
		}
	}

	// 月账单类型表操作
	public void saveFinMonthly(FinAccount account, String projectid, Date createtime, BigDecimal amount, String ftype) {
		String acct = account.getId();
		String[] dateStr = GeneralUtil.formatDate(createtime).toString().split("-");
		String year = dateStr[0];
		String month = dateStr[1];
		FinTypeJournalMonthly oldfinMonthly = finTypeJournalMonthlyMapper.selectByYearMonth(year, month, projectid, acct);
		if (oldfinMonthly != null) {
			if (ftype.equals("in")) {
				oldfinMonthly.setRec(oldfinMonthly.getRec().add(amount));
			} else {
				oldfinMonthly.setPay(oldfinMonthly.getPay().add(amount));
			}
			finTypeJournalMonthlyMapper.updateById(oldfinMonthly);
		} else {
			FinTypeJournalMonthly finmonthly = new FinTypeJournalMonthly();
			finmonthly.setAcct(account.getId());
			finmonthly.setMonth(Integer.parseInt(month));
			finmonthly.setProjectid(projectid);
			finmonthly.setYear(Integer.parseInt(year));
			if (ftype.equals("in")) {
				finmonthly.setRec(amount);
			} else {
				finmonthly.setPay(amount);
			}
			finTypeJournalMonthlyMapper.insert(finmonthly);
		}
	}

	public List<FinanceProject> findProject(String shopid) {
		List<FinanceProject> list = financeProjectMapper.findProject(shopid);
		if (list.size() > 0 && list != null) {
			return list;
		} else {
			return null;
		}
	}

	public List<Map<String, Object>> findProList(String shopid, String search) {
		if (GeneralUtil.isNotEmpty(search)) {
			search = "%" + search + "%";
		} else {
			search = null;
		}
		List<Map<String, Object>> list = financeProjectMapper.findProList(shopid, search);
		return list;
	}

	public Map<String, Object> saveProject(String name, UserInfo user) {
		Map<String, Object> map = new HashMap<String, Object>();
		if ("商品运费".equals(name) || "采购商品".equals(name)) {
			map.put("msg", "添加失败!该公司下已有该项目!");
			return map;
		}
		QueryWrapper<FinanceProject> queryWrapper=new QueryWrapper<FinanceProject>();
		queryWrapper.eq("shopid", user.getCompanyid());
		queryWrapper.eq("name", name);
		List<FinanceProject> list = financeProjectMapper.selectList(queryWrapper);
		if (list.size() > 0 && list != null) {
			map.put("msg", "添加失败!该公司下已有该项目!");
		} else {
			FinanceProject project = new FinanceProject();
			Date nowdate = new Date();
			project.setCreatedate(nowdate);
			project.setCreator(user.getId());
			project.setIssys(false);
			project.setName(name);
			project.setOperator(user.getId());
			project.setOpttime(nowdate);
			project.setShopid(user.getCompanyid());
	
			int result = financeProjectMapper.insert(project);
			if (result > 0) {
				map.put("msg", "添加成功!");
			} else {
				map.put("msg", "添加失败!");
			}
		}
		return map;
	}

	public Map<String, Object> updateProject(String id, String name, UserInfo user) {
		if (IniConfig.isDemo()) {
			throw new BizException("演示环境不能修改！");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if ("商品运费".equals(name) || "采购商品".equals(name)) {
			map.put("msg", "添加失败!该公司下已有该项目!");
			return map;
		}
		if (GeneralUtil.isNotEmpty(id)) {
			QueryWrapper<FinanceProject> queryWrapper=new QueryWrapper<FinanceProject>();
			queryWrapper.eq("shopid", user.getCompanyid());
			queryWrapper.eq("name", name);
			List<FinanceProject> list = financeProjectMapper.selectList(queryWrapper);
			if (list.size() > 0 && list != null) {
				if (list.get(0).getId().equals(id)) {
					FinanceProject oldpro = financeProjectMapper.selectById(id);
					oldpro.setOperator(user.getId());
					oldpro.setOpttime(new Date());
					oldpro.setName(name);
					int result = financeProjectMapper.updateById(oldpro);
					if (result > 0) {
						map.put("msg", "更新成功!");
					} else {
						map.put("msg", "更新失败!");
					}
				} else {
					map.put("msg", "更新失败!该公司下已有该项目!");
				}
			} else {
				FinanceProject oldpro = financeProjectMapper.selectById(id);
				oldpro.setOperator(user.getId());
				oldpro.setOpttime(new Date());
				oldpro.setName(name);
				int result = financeProjectMapper.updateById(oldpro);
				if (result > 0) {
					map.put("msg", "更新成功!");
				} else {
					map.put("msg", "更新失败!");
				}
			}
		} else {
			map.put("msg", "更新失败!");
		}
		return map;
	}

	public Map<String, Object> delProject(String id) {
		if (IniConfig.isDemo()) {
			throw new BizException("演示环境不能修改！");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if (GeneralUtil.isNotEmpty(id)) {
			FinanceProject finpro = financeProjectMapper.selectById(id);
			QueryWrapper<FinJournalAccount> queryWrapper=new QueryWrapper<FinJournalAccount>();
			queryWrapper.eq("projectid", finpro.getId());
			List<FinJournalAccount> oldlist = finJournalAccountMapper.selectList(queryWrapper);
			if (oldlist != null && oldlist.size() > 0) {
				map.put("msg", "删除失败!该项目已存在记录！");
				map.put("isok", false);
			} else {
				int result = financeProjectMapper.deleteById(finpro.getId());
				if (result > 0) {
					map.put("msg", "删除成功!");
					map.put("isok", true);
				} else {
					map.put("msg", "删除失败!");
					map.put("isok", false);
				}
			}
		} else {
			map.put("msg", "删除失败!");
			map.put("isok", false);
		}
		return map;
	}

	public boolean saveDate(FinJournalAccount entity, UserInfo user) throws BizException {
		boolean result =false;
//		int i=10;
//		while(i>0){
//			Object flag =session.getAttribute("FinJournal" + user.getCompanyid());
//			if (flag != null) {
//				try {
//					Thread.sleep(3*1000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				i--;
//				if(i==0){
//					throw new BizException("系统繁忙，请稍后再试！");
//				}
//			} else {
//				session.setAttribute("FinJournal" + user.getCompanyid(), "isOpt");
//				i=0;
//			}
//		}
		try {
			FinJournalAccount old = super.getById(entity.getId());
			result = saveRecord(entity, old);
		} catch (BizException e) {
			e.printStackTrace();
		} finally {
			//session.removeAttribute("FinJournal" + user.getCompanyid());
		}
		return result;
	}

	public List<Map<String, Object>> findAllAcountByType(UserInfo user, String year, String month) {
		FinAccount acount = readFinAccount(user.getCompanyid());
		List<Map<String, Object>> list = finJournalAccountMapper.findAllAcountByType(acount.getId(), year, month);
		return list;
	}

	public Map<Integer, Map<String, Object>> findLineDataByYear(UserInfo user, String year, String month) {
		HashMap<Integer, Map<String, Object>> mapList = new HashMap<Integer, Map<String, Object>>();
		FinAccount acount = readFinAccount(user.getCompanyid());
		if (month == null) {
			List<Map<String, Object>> list = finJournalAccountMapper.findMonthCharts(acount.getId(), year);
			for (Map<String, Object> item : list) {
				Map<String, Object> maps = new HashMap<String, Object>();
				maps.put("pay", item.get("pay"));
				maps.put("rec", item.get("rec"));
				mapList.put(Integer.parseInt(item.get("month").toString()), maps);
			}
			for (Integer i = 1; i <= 12; i++) {
				if (mapList.containsKey(i)) {
					continue;
				}
				Map<String, Object> maps = new HashMap<String, Object>();
				maps.put("pay", "0");
				maps.put("rec", "0");
				mapList.put(i, maps);
			}
			return mapList;
		} else {
			List<Map<String, Object>> list = finJournalAccountMapper.findMonthDetailCharts(acount.getId(), year, month);
			Integer index = 0;
			if (Integer.parseInt(year) % 4 == 0 && "2".equals(month)) {
				index = 29;
			} else if (Integer.parseInt(year) % 4 != 0 && "2".equals(month)) {
				index = 28;
			} else if ("4".equals(month) || "6".equals(month) || "9".equals(month) || "11".equals(month)) {
				index = 30;
			} else {
				index = 31;
			}
			for (Map<String, Object> item : list) {
				Map<String, Object> maps = new HashMap<String, Object>();
				maps.put("pay", item.get("pay"));
				maps.put("rec", item.get("rec"));
				mapList.put(Integer.parseInt(item.get("month").toString()), maps);
			}
			for (Integer i = 1; i <= index; i++) {
				if (mapList.containsKey(i)) {
					continue;
				}
				Map<String, Object> maps = new HashMap<String, Object>();
				maps.put("pay", "0");
				maps.put("rec", "0");
				mapList.put(i, maps);
			}
			return mapList;
		}
	}

	public List<PurchaseFormPaymentMethod> findPurchasePayMethod() {
		QueryWrapper<PurchaseFormPaymentMethod> queryWrapper=new QueryWrapper<PurchaseFormPaymentMethod>();
		queryWrapper.isNotNull("id");
		List<PurchaseFormPaymentMethod> list=purchaseFormPaymentMethodMapper.selectList(queryWrapper);
		return list;
	}

}
