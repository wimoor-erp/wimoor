package com.wimoor.amazon.common.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.common.mapper.ExchangeRateCustomerMapper;
import com.wimoor.amazon.common.mapper.ExchangeRateHisMapper;
import com.wimoor.amazon.common.mapper.ExchangeRateMapper;
import com.wimoor.amazon.common.pojo.entity.ExchangeRate;
import com.wimoor.amazon.common.pojo.entity.ExchangeRateCustomer;
import com.wimoor.amazon.common.pojo.entity.ExchangeRateHis;
import com.wimoor.amazon.common.service.IExchangeRateService;
import com.wimoor.amazon.util.HttpUtils;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
 

 
@Service("exchangeRateService")
@RequiredArgsConstructor
public class ExchangeRateServiceImpl extends ServiceImpl<ExchangeRateMapper, ExchangeRate> implements IExchangeRateService {
		
	@Resource
	ExchangeRateCustomerMapper exchangeRateCustomerMapper;
	@Resource
	ExchangeRateHisMapper exchangeRateHisMapper;
 
	
	@CacheEvict(value = {"exchangeRateCache"}, allEntries = true)
	public void updateExchangeRate() throws BizException {
		HttpUtils httputils = HttpUtils.getInstance();
		try {
	 
			Document document = httputils.getHtmlPageResponseAsDocument("https://www.boc.cn/sourcedb/whpj/");
			Elements div = document.getElementsByClass("BOC_main");//鑾峰彇鍏冪礌鑺傜偣绛�
			Elements mytables = div.get(0).getElementsByTag("table");
			Elements mytrs = mytables.get(1).getElementsByTag("tr");
			for(int i=1;i<mytrs.size();i++) {
				Element tr = mytrs.get(i);
				Elements tds = tr.getElementsByTag("td");
				String name=tds.get(0).text().trim();//新西兰币
				String price=tds.get(5).text().trim();
				String date=tds.get(6).text().trim();
				if("人民币".equals(name)) continue;
				ExchangeRate einfo=null;
				QueryWrapper<ExchangeRate> query=new 	QueryWrapper<ExchangeRate>();
				query.eq("name",name);
				einfo = this.baseMapper.selectOne(query);
				if(einfo==null) {
					continue;
				}
				ExchangeRateHis his = null;
				ExchangeRate exchangeRate = new ExchangeRate();
				exchangeRate.setName(einfo.getName());
				exchangeRate.setPrice(new BigDecimal(price));
				exchangeRate.setType(einfo.getType());
				exchangeRate.setSymbol(einfo.getName());
				exchangeRate.setUtctime(GeneralUtil.getDatez(date));
				ExchangeRate old = this.baseMapper.selectOne(query);
				if (old!=null) {
					exchangeRate.setId(old.getId());
					this.updateById(exchangeRate);
				} else {
					super.save(exchangeRate);
				}
				saveExchangeRateHis(exchangeRate,his);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
			

		  
	}

	public void saveExchangeRateHis(ExchangeRate exchangeRate,ExchangeRateHis his) {
		his = new ExchangeRateHis();
		his.setName(exchangeRate.getName());
		his.setPrice(exchangeRate.getPrice());
		his.setType(exchangeRate.getType());
		his.setSymbol(exchangeRate.getSymbol());
		his.setByday(GeneralUtil.Dateformatter(exchangeRate.getUtctime(), "yyyy-MM-dd"));
		exchangeRateHisMapper.insert(his);
	}

	public List<ExchangeRate> getExchangeRateLimit() {
		return this.baseMapper.getExchangeRateLimit();
	}

	public List<Map<String, Object>> getMyCurrencyRate(String shopid) {
		return exchangeRateCustomerMapper.getMyExchangeRate(shopid);
	}

	@CacheEvict(value = {"exchangeRateCache","findSettlementSKUCache"}, allEntries = true)
	public void saveMyCurrencyRate(UserInfo user, String name, String value, String id,String symbol) {
		// TODO Auto-generated method stub
		ExchangeRateCustomer oldrate =null;
		if(!StrUtil.isEmpty(id)) {   oldrate = exchangeRateCustomerMapper.selectById(id);}
		if(oldrate==null) {
		    ExchangeRateCustomer rate = new ExchangeRateCustomer();
			rate.setShopid(user.getCompanyid());
			rate.setName(name);
			if(StrUtil.isEmpty(value)) {
				throw new BizException("汇率不能为0");
			}
			rate.setPrice(new BigDecimal(value));
			rate.setUtctime(new Date());
			rate.setType("CNY(100)/"+name);
			rate.setSymbol(symbol);
			rate.setOperator(user.getId());
			exchangeRateCustomerMapper.insert(rate);
		} else {
			if(StrUtil.isEmpty(value)) {
				exchangeRateCustomerMapper.deleteById(oldrate.getId());
				return;
			}
			oldrate.setPrice(new BigDecimal(value));
			oldrate.setUtctime(new Date());
			oldrate.setOperator(user.getId());
			exchangeRateCustomerMapper.updateById(oldrate);
		}
	}

	@Cacheable(value = "exchangeRateCache")
	public ExchangeRate selectByName(String currency) {
		// TODO Auto-generated method stub
		QueryWrapper<ExchangeRate> query =new QueryWrapper<ExchangeRate>();
		query.eq("name", currency);
		return this.baseMapper.selectOne(query );
	}

	@Cacheable(value = "exchangeRateCache")
	public ExchangeRate selectMineByName(String currency, String shopid) {
		// TODO Auto-generated method stub
		ExchangeRate result=null;
		QueryWrapper<ExchangeRateCustomer> query =new QueryWrapper<ExchangeRateCustomer>();
		query.eq("name", currency);
		query.eq("shopid", shopid);
		ExchangeRateCustomer mycu = exchangeRateCustomerMapper.selectOne(query);
		if(mycu==null) {
			 result = selectByName(currency);
		}else {
			result=new ExchangeRate();
			BeanUtil.copyProperties(mycu, result);
		}
		return result;
	}

}
