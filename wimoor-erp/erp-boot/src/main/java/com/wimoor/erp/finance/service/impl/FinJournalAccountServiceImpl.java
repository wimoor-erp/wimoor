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
import com.wimoor.util.ExcelExportUtil;
import lombok.RequiredArgsConstructor;
import java.util.function.Function;
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
			// 定义表头
			LinkedHashMap<String, String> headers = new LinkedHashMap<>();
			headers.put("name", "项目");
			headers.put("number", "订单编码");
			headers.put("sku", "SKU");
			headers.put("createtime", "日期");
			headers.put("ftype", "支付类型");
			headers.put("amount", "金额（￥）");
			headers.put("remark", "备注");
			headers.put("supplier", "供应商");

			// 获取数据
			List<Map<String, Object>> list = this.baseMapper.findByCondition(param);

			// 定义值转换器
			Map<String, Function<Object, Object>> converters = new HashMap<>();
			converters.put("ftype", value -> {
				if ("out".equals(value)) {
					return "支出";
				} else {
					return "收入";
				}
			});

			// 调用通用导出方法
			ExcelExportUtil.exportToExcel(workbook, "sheet1", headers, list, converters);
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
