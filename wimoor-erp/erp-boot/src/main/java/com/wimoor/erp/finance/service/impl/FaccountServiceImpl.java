package com.wimoor.erp.finance.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.finance.mapper.FinAccountMapper;
import com.wimoor.erp.finance.mapper.FinJournalDailyMapper;
import com.wimoor.erp.finance.mapper.FinTypeJournalMonthlyMapper;
import com.wimoor.erp.finance.mapper.FinanceProjectMapper;
import com.wimoor.erp.finance.pojo.entity.FinAccount;
import com.wimoor.erp.finance.pojo.entity.FinJournalDaily;
import com.wimoor.erp.finance.pojo.entity.FinTypeJournalMonthly;
import com.wimoor.erp.finance.pojo.entity.FinanceProject;
import com.wimoor.erp.finance.service.IFaccountService;
import com.wimoor.erp.purchase.mapper.PurchaseFormPaymentMethodMapper;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormPaymentMethod;

import lombok.RequiredArgsConstructor;
 

@Service("faccountService")
@RequiredArgsConstructor
public class FaccountServiceImpl extends ServiceImpl<FinAccountMapper,FinAccount> implements IFaccountService {
	
	 
	 
	final FinAccountMapper finAccountMapper;
	 
	final FinanceProjectMapper financeProjectMapper;
	 
	final FinJournalDailyMapper finJournalDailyMapper;
	 
	final FinTypeJournalMonthlyMapper finTypeJournalMonthlyMapper;
	 
	final PurchaseFormPaymentMethodMapper purchaseFormPaymentMethodMapper;


	public BigDecimal getSummary(UserInfo currUser) {
		FinAccount account = finAccountMapper.findAcBanlance(currUser.getCompanyid());
		BigDecimal result = new BigDecimal("0");
		if (account != null) {
			result = account.getBalance();
		}
		return result;
	}

	
    @Transactional
	public void updateFinAfterChange(FinAccount account, String projectid, Date createtime, BigDecimal amount, String ftype) {
		saveFinAccount(account, amount, ftype);
		saveFinDaily(account, createtime, amount, ftype);
		saveFinMonthly(account, projectid, createtime, amount, ftype);
	}
 
    @Transactional
	public void updateFinCancelChange(FinAccount account, String projectid, Date createtime, BigDecimal amount, String ftype) {
		cancelFinAccount(account, amount, ftype);
		cancelFinDaily(account, createtime, amount, ftype);
		cancelFinMonthly(account, projectid, createtime, amount, ftype);
	}
	
	public void saveFinAccount(FinAccount account, BigDecimal amount, String ftype) {
		BigDecimal balance = account.getBalance();
		BigDecimal nowban = null;
		if(balance==null) {
			balance=new BigDecimal(0);
		}
		if (ftype.equals("in")) {
			nowban = balance.add(amount);
		} else {
			nowban = balance.subtract(amount);
		}
		account.setBalance(nowban);
		if (account.getCreatedate() == null) {
			account.setCreatedate(new Date());
			finAccountMapper.insert(account);
		} else {
			finAccountMapper.updateById(account);
		}
	}

	
	public void cancelFinAccount(FinAccount account, BigDecimal amount, String ftype) {
		BigDecimal balance = account.getBalance();
		BigDecimal nowban = null;
		if (ftype.equals("in")) {
			nowban = balance.subtract(amount);
		} else {
			nowban = balance.add(amount);
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
					if(oldfindaily.getRec()!=null) {
						oldfindaily.setRec(amount.add(oldfindaily.getRec()));
					}else {
						oldfindaily.setRec(amount);
					}
				} else {
					if(oldfindaily.getPay()!=null) {
						oldfindaily.setPay(amount.add(oldfindaily.getPay()));
					}else {
						oldfindaily.setPay(amount);
					}
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
		
	    // 日账单表操作
		public void cancelFinDaily(FinAccount account, Date createtime, BigDecimal amount, String ftype) {
			FinJournalDaily oldfindaily = finJournalDailyMapper.selectByday(GeneralUtil.formatDate(createtime), account.getId());
			List<FinJournalDaily> oldailylist = finJournalDailyMapper.selectAfterByday(GeneralUtil.formatDate(createtime), account.getId());
			if (oldfindaily != null) {
				BigDecimal oldbalance = oldfindaily.getBalance();
				if (ftype.equals("in")) {
					if(oldfindaily.getRec()!=null) {
						oldfindaily.setRec(oldfindaily.getRec().subtract(amount));
						oldbalance=oldbalance.subtract(amount);
					}
				} else {
					if(oldfindaily.getPay()!=null) {
						oldfindaily.setPay(oldfindaily.getPay().subtract(amount));
						oldbalance=oldbalance.add(amount);
					}
				}
				oldfindaily.setBalance(oldbalance);
				finJournalDailyMapper.updateById(oldfindaily);
				if(oldailylist!=null&&oldailylist.size()>0) {
					Map<String,FinJournalDaily> dailymap=new HashMap<String,FinJournalDaily>();
					for(FinJournalDaily item:oldailylist) {
						dailymap.put(GeneralUtil.formatDate(item.getByday()), item);
					}
					Calendar c=Calendar.getInstance();
					c.setTime(createtime);
					Date today = new Date();
					while(c.getTime().before(today)||c.getTime().equals(today)) {
						c.add(Calendar.DATE, 1);
						String key=GeneralUtil.formatDate(c.getTime());
						FinJournalDaily oldone = dailymap.get(key);
						if(oldone!=null) {
							BigDecimal rec = oldone.getRec()!=null?oldone.getRec():new BigDecimal(0);
							BigDecimal pay = oldone.getPay()!=null?oldone.getPay():new BigDecimal(0);
							oldbalance=oldbalance.add(rec).subtract(pay);
							oldone.setBalance(oldbalance);
							finJournalDailyMapper.updateById(oldone);
						}
					}
				}
			} else {
			   throw new BizException("日账错误，无法撤销");
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
				if(oldfinMonthly.getRec()!=null) {
					oldfinMonthly.setRec(oldfinMonthly.getRec().add(amount));
				}else {
					oldfinMonthly.setRec(amount);
				}
			} else {
				if(oldfinMonthly.getPay()!=null) {
					oldfinMonthly.setPay(oldfinMonthly.getPay().add(amount));
				}else {
					oldfinMonthly.setPay(amount);
				}
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

	// 月账单类型表操作
		public void cancelFinMonthly(FinAccount account, String projectid, Date createtime, BigDecimal amount, String ftype) {
			String acct = account.getId();
			String[] dateStr = GeneralUtil.formatDate(createtime).toString().split("-");
			String year = dateStr[0];
			String month = dateStr[1];
			FinTypeJournalMonthly oldfinMonthly = finTypeJournalMonthlyMapper.selectByYearMonth(year, month, projectid, acct);
			if (oldfinMonthly != null) {
				if (ftype.equals("in")) {
					oldfinMonthly.setRec(oldfinMonthly.getRec().subtract(amount));
				} else {
					oldfinMonthly.setPay(oldfinMonthly.getPay().subtract(amount));
				}
				finTypeJournalMonthlyMapper.updateById(oldfinMonthly);
			} else {
				 throw new BizException("取消失败，月度费用错误");
			}
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

	
 

 

	public List<FinanceProject> findProList(String shopid, String search) {
		if (GeneralUtil.isNotEmpty(search)) {
			search = "%" + search + "%";
		} else {
			search = null;
		}
		List<FinanceProject> list = financeProjectMapper.findProList(shopid, search);
		return list;
	}


	public List<PurchaseFormPaymentMethod> findPurchasePayMethod() {
		QueryWrapper<PurchaseFormPaymentMethod> queryWrapper=new QueryWrapper<PurchaseFormPaymentMethod>();
		queryWrapper.isNotNull("id");
		List<PurchaseFormPaymentMethod> list=purchaseFormPaymentMethodMapper.selectList(queryWrapper);
		return list;
	}

	@Override
	public List<FinAccount> findPayAccountByMethod(String paymethod, String shopid) {
		QueryWrapper<FinAccount> queryWrapper=new QueryWrapper<FinAccount>();
		queryWrapper.eq("shopid", shopid);
		queryWrapper.eq("isdelete", false);
		List<FinAccount> list = finAccountMapper.selectList(queryWrapper);
		for(FinAccount item:list) {
			PurchaseFormPaymentMethod meth = purchaseFormPaymentMethodMapper.selectById(item.getPaymeth());
			item.setPaymethName(meth.getName());
		}
		return list;
	}

	@Override
	public FinAccount getAccByMeth(String shopid,String paymethod) {
		// TODO Auto-generated method stub
		QueryWrapper<FinAccount> queryWrapper=new QueryWrapper<FinAccount>();
		queryWrapper.eq("shopid", shopid);
		queryWrapper.eq("paymeth", paymethod);
		queryWrapper.eq("isdefault", true);
		
		FinAccount acc = finAccountMapper.selectOne(queryWrapper);
		if(acc==null) {
			QueryWrapper<FinAccount> queryWrapperName=new QueryWrapper<FinAccount>();
			queryWrapperName.eq("shopid", shopid);
			queryWrapperName.eq("paymeth", paymethod);
			queryWrapperName.eq("name", "系统");
			FinAccount oldone = finAccountMapper.selectOne(queryWrapperName);
			if(oldone!=null) {
				acc=oldone;
				acc.setIsdelete(false);
				acc.setIsdefault(true);
				finAccountMapper.updateById(acc);
			}else {
				acc=new FinAccount();
				acc.setName("系统");
				acc.setPaymeth(Integer.parseInt(paymethod));
				acc.setShopid(shopid);
				acc.setIsdefault(true);
				acc.setIsdelete(false);
				acc.setCreatedate(new Date());
				acc.setBalance(new BigDecimal(0));
				finAccountMapper.insert(acc);
			}
		}else {
			if(acc.getIsdelete()==true) {
				if(acc.getName().equals("系统")) {
					acc.setIsdelete(false);
					finAccountMapper.updateById(acc);
				}else {
					acc.setIsdefault(false);
					finAccountMapper.updateById(acc);
					QueryWrapper<FinAccount> queryWrapperName=new QueryWrapper<FinAccount>();
					queryWrapperName.eq("shopid", shopid);
					queryWrapperName.eq("paymeth", paymethod);
					queryWrapperName.eq("name", "系统");
					FinAccount oldone = finAccountMapper.selectOne(queryWrapperName);
					if(oldone!=null) {
						acc=oldone;
						acc.setIsdelete(false);
						acc.setIsdefault(true);
						finAccountMapper.updateById(acc);
					}else {
						acc=new FinAccount();
						acc.setName("系统");
						acc.setPaymeth(Integer.parseInt(paymethod));
						acc.setShopid(shopid);
						acc.setIsdefault(true);
						acc.setIsdelete(false);
						acc.setCreatedate(new Date());
						acc.setBalance(new BigDecimal(0));
						finAccountMapper.insert(acc);
					}
				}
			}
		}
		return acc;
	}

	@Override
	public List<FinAccount> findAccountAll(String shopid) {
		// TODO Auto-generated method stub
		return this.baseMapper.findAccountAll(shopid);
	}
	@Override
	public List<FinAccount> findAccountArchiveAll(String shopid) {
		// TODO Auto-generated method stub
		return this.baseMapper.findAccountArchiveAll(shopid);
	}

	@Override
	public Boolean saveAccount(FinAccount fin) {
		// TODO Auto-generated method stub
		QueryWrapper<FinAccount> queryWrapperName=new QueryWrapper<FinAccount>();
		queryWrapperName.eq("shopid", fin.getShopid());
		queryWrapperName.eq("paymeth", fin.getPaymeth());
		queryWrapperName.eq("name", fin.getName());
		FinAccount accname = finAccountMapper.selectOne(queryWrapperName);
		if(accname!=null&&accname.getIsdelete()) {
			throw new BizException("添加失败，已存在删除的账户名称");
		} else if(accname!=null) {
			throw new BizException("添加失败，名称不能重复");
		}
		QueryWrapper<FinAccount> queryWrapper=new QueryWrapper<FinAccount>();
		queryWrapper.eq("shopid", fin.getShopid());
		queryWrapper.eq("paymeth", fin.getPaymeth());
		queryWrapper.eq("isdefault", true);
		FinAccount acc = finAccountMapper.selectOne(queryWrapper);
		if(acc!=null) {
			fin.setIsdefault(false);
		}else{
			fin.setIsdefault(true);
		}
		return finAccountMapper.insert(fin)>0;
	}

}
