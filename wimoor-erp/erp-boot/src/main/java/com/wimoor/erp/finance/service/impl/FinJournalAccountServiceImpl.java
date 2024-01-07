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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.finance.mapper.FinJournalAccountMapper;
import com.wimoor.erp.finance.pojo.entity.FinAccount;
import com.wimoor.erp.finance.pojo.entity.FinJournalAccount;
import com.wimoor.erp.finance.service.IFaccountService;
import com.wimoor.erp.finance.service.IFinJournalAccountService;

import lombok.RequiredArgsConstructor;

@Service("finJournalAccountService")
@RequiredArgsConstructor
public class FinJournalAccountServiceImpl  extends ServiceImpl<FinJournalAccountMapper,FinJournalAccount> implements IFinJournalAccountService {
    final IFaccountService faccountService;
	public List<Map<String, Object>> findDetialByCondition(Map<String, Object> param) {
		return this.baseMapper.findDetialByCondition(param);
	}

	public IPage<Map<String, Object>> findByCondition(Page<?> page,Map<String, Object> param) {
		return this.baseMapper.findSumDayByCondition(page,param);
	}

	public Map<String, Object> findSumByCondition(Map<String, Object> param) {
		return this.baseMapper.findSumByCondition(param);
	}

	public List<Map<String, Object>> paymentSum(String shopid) {
		return this.baseMapper.paymentSum(shopid);
	}

	public List<Map<String, Object>> paymentSumByCondition(Map<String, Object> param) {
		return this.baseMapper.paymentSumByCondition(param);
	}
	
	// 同步添加record记录 账户表 日账单表 月账单类型表
	public boolean saveRecord(FinJournalAccount faccount, FinJournalAccount old,UserInfo info) throws BizException {
			// 操作账户表
			int result = 0;
			FinAccount account = faccountService.getById(faccount.getAcct());
			if(!account.getShopid().equals(info.getCompanyid())) {
				throw new BizException("账户异常，请确认是否切换账号后没有刷新");
			}
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
				result = this.baseMapper.insert(faccount);
			} else {
				result = this.baseMapper.updateById(faccount);
			}
			faccountService.updateFinAfterChange(account, faccount.getProjectid(), faccount.getCreatetime(), amount, ftype);
			return result>0;
		}
		

		public boolean saveData(FinJournalAccount entity, UserInfo user) throws BizException {
			boolean result =false;
			try {
				FinJournalAccount old = this.baseMapper.selectById(entity.getId());
				result = saveRecord(entity, old,user);
			} catch (BizException e) {
				e.printStackTrace();
			} finally {
			}
			return result;
		}
		@Override
		public void cancelData(String id, UserInfo userinfo) {
			// TODO Auto-generated method stub
			FinJournalAccount faccount = this.baseMapper.selectById(id);
			FinAccount account = faccountService.getById(faccount.getAcct());
			if(GeneralUtil.distanceOfDay(faccount.getCreatetime(), new Date())>60) {
				throw new BizException("超过60天无法撤销");
			}
			faccountService.updateFinCancelChange(account, faccount.getProjectid(), faccount.getCreatetime(), faccount.getAmount(), faccount.getFtype());
			faccount.setFtype("cancel");
			this.baseMapper.updateById(faccount);
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
			List<Map<String, Object>> list = this.baseMapper.findByCondition(param);

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


		public List<Map<String, Object>> findAllAcountByType(UserInfo user,String acc, String year, String month) {
			List<Map<String, Object>> list = this.baseMapper.findAllAcountByType(user.getCompanyid(),acc, year, month);
			return list;
		}

		public Map<Integer, Map<String, Object>> findLineDataByYear(UserInfo user,String acc, String year, String month) {
			HashMap<Integer, Map<String, Object>> mapList = new HashMap<Integer, Map<String, Object>>();
			if (month == null) {
				List<Map<String, Object>> list = this.baseMapper.findMonthCharts(user.getCompanyid(),acc, year);
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
				int intmonth = Integer.parseInt(month);
				month=intmonth+"";
				List<Map<String, Object>> list = this.baseMapper.findMonthDetailCharts(user.getCompanyid(),acc, year, month);
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
					maps.put("pay", item.get("pay")!=null?item.get("pay"):"0");
					maps.put("rec", item.get("rec")!=null?item.get("rec"):"0");
					Integer monthint = Integer.parseInt(item.get("month").toString());
					mapList.put(monthint, maps);
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

	
		
}
