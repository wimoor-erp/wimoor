package com.wimoor.amazon.common.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;


@Service("exchangeRateService")
public class ExchangeRateServiceImpl extends ServiceImpl<ExchangeRateMapper, ExchangeRate> implements IExchangeRateService {
		
	@Resource
	ExchangeRateCustomerMapper exchangeRateCustomerMapper;
	@Resource
	ExchangeRateHisMapper exchangeRateHisMapper;
    @Resource
	PBCHttpClientService pBCHttpClientService;
	@Resource
	WHJHttpClientService wHJHttpClientService;


	public List<ExchangeRate>  captureExchangeRateFromBOC() throws Exception {//获取中国银行牌价
		// TODO Auto-generated method stub
		HttpUtils httputils = HttpUtils.getInstance();
		List<ExchangeRate> exchangeRates = new ArrayList<ExchangeRate>();
		Document document = httputils.getHtmlPageResponseAsDocument("https://www.boc.cn/sourcedb/whpj/");
		Elements div = document.getElementsByClass("BOC_main");
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
			query.eq("symbol",name);
			einfo = this.baseMapper.selectOne(query);
			if(einfo==null) {
				continue;
			}
			ExchangeRateHis his = null;
			ExchangeRate exchangeRate = new ExchangeRate();
			exchangeRate.setName(einfo.getName());
			exchangeRate.setPrice(new BigDecimal(price));
			exchangeRate.setType(einfo.getType());
			exchangeRate.setSymbol(einfo.getSymbol());
			exchangeRate.setUtctime(GeneralUtil.getDatez(date));
			exchangeRates.add(exchangeRate);
		}
		return exchangeRates;

	}

	public List<ExchangeRate> captureExchangeRateFromPBC() throws Exception {//获取中国人民银行牌价
		// TODO Auto-generated method stub
		Document document = pBCHttpClientService.fetchDocument("https://camlmac.pbc.gov.cn/zhengcehuobisi/125207/125217/125925/index.html");
		Elements font = document.getElementsByClass("newslist_style");
		Elements a = font.get(0).getElementsByTag("a");
		String href=a.attr("href");
		System.out.println(href);
		document = pBCHttpClientService.fetchDocument("https://camlmac.pbc.gov.cn" + href);
		Element div = document.getElementById("zoom");
		Elements p = div.getElementsByTag("p");
		Element content = p.get(0);
		if(content!=null){
			String contentText = content.text().trim();
			return PBCExchangeRateParser.parseExchangeRateText(contentText);
		}
		return null;
	}

	public List<ExchangeRate> captureExchangeRateFromWHJ() throws Exception {
		String url = "https://www.safe.gov.cn/AppStructured/hlw/exportRMBExcel.do";
		List<ExchangeRate> exchangeRates = wHJHttpClientService.fetchDocument(url);
		return exchangeRates;
	}


	/**
	 * 解析表头，获取币种列表
	 */
	private List<String> parseHeaderCurrencies(Element headerRow) {
		List<String> currencies = new ArrayList<>();
		Elements ths = headerRow.select("th");

		for (int i = 1; i < ths.size(); i++) {
			String text = ths.get(i).text().trim();
			// 清理可能的HTML注释或特殊字符
			text = text.replaceAll("<!--.*?-->", "").trim();
			if (!text.isEmpty() && !text.equals("日期")) {
				currencies.add(text);
			}
		}

		return currencies;
	}

 

	/**
	 * 创建ExchangeRate对象
	 */
	private ExchangeRate createExchangeRate(String symbol, String name,
												String priceValue, String date) {
		ExchangeRate rate = new ExchangeRate();
		rate.setSymbol(symbol);
		rate.setName(name);
		rate.setPrice(new BigDecimal(priceValue));
		rate.setUtctime(GeneralUtil.getDatez(date));
		rate.setType("CNY(100)/" + name);

		return rate;
	}




	@CacheEvict(value = {"exchangeRateCache"}, allEntries = true)
	public void updateExchangeRate() throws BizException {
        try {
			List<ExchangeRate> rates =captureExchangeRateFromWHJ();
			saveParsedRatesToDatabase(rates);
		} catch (Exception e) {
			// TODO Auto-generated catch block
            e.printStackTrace();
		}
	}

	/**
	 * 保存解析后的汇率到数据库
	 */
	private void saveParsedRatesToDatabase(List<ExchangeRate> rates) {
		for (ExchangeRate rate : rates) {
			try {
				// 查询数据库中是否已存在该币种
				QueryWrapper<ExchangeRate> query = new QueryWrapper<>();
				query.eq("name", rate.getName());
				ExchangeRate existingRate = this.baseMapper.selectOne(query);

				if (existingRate != null) {
					// 更新现有记录
					rate.setId(existingRate.getId());
					this.updateById(rate);
					System.out.println("更新汇率: " + rate.getName() + " = " + rate.getPrice());
				} else {
					// 插入新记录
					this.save(rate);
					System.out.println("新增汇率: " + rate.getName() + " = " + rate.getPrice());
				}

				// 保存历史记录
				saveExchangeRateHis(rate);

			} catch (Exception e) {
				System.err.println("保存汇率失败: " + rate.getName());
				e.printStackTrace();
			}
		}
	}

	public void saveExchangeRateHis(ExchangeRate exchangeRate) {
		ExchangeRateHis his = new ExchangeRateHis();
		his.setName(exchangeRate.getName());
		his.setPrice(exchangeRate.getPrice());
		his.setType(exchangeRate.getType());
		his.setSymbol(exchangeRate.getSymbol());
		his.setUtctime(exchangeRate.getUtctime());
		his.setByday(GeneralUtil.Dateformatter(exchangeRate.getUtctime(), "yyyy-MM-dd"));
		LambdaQueryWrapper<ExchangeRateHis> query=new LambdaQueryWrapper<>();
		query.eq(ExchangeRateHis::getName, his.getName());
		query.eq(ExchangeRateHis::getByday, his.getByday());
		ExchangeRateHis one=exchangeRateHisMapper.selectOne(query);
		if(one!=null){
			his.setId(one.getId());
			exchangeRateHisMapper.updateById(his);
        }else{
			exchangeRateHisMapper.insert(his);
		}
	}

	public List<ExchangeRate> getExchangeRateLimit() {
		return this.baseMapper.getExchangeRateLimit();
	}

 
	public List<Map<String, Object>> getMyCurrencyRate(String shopid,String byday) {
		if(GeneralUtil.formatDate(new Date()).equals(byday)) {
			return exchangeRateCustomerMapper.getMyExchangeRate(shopid);
		}else {
			return exchangeRateCustomerMapper.getMyExchangeRateByDay(shopid,byday);
		}
	}
	@CacheEvict(value = {"exchangeRateCache","findSettlementSKUCache"}, allEntries = true)
	public void saveMyCurrencyRate(UserInfo user,ExchangeRateCustomer rate) {
		// TODO Auto-generated method stub
		ExchangeRateCustomer oldrate =null;
		if(!rate.idIsNULL()) {   oldrate = exchangeRateCustomerMapper.selectById(rate.getId());}
		if(oldrate==null) {
			rate.setShopid(user.getCompanyid());
			if(rate.getPrice()==null||rate.getPrice().floatValue()<0.00001) {
				throw new BizException("汇率不能为0");
			}
			rate.setUtctime(new Date());
			rate.setType("CNY(100)/"+rate.getName());
			rate.setOperator(user.getId());
			exchangeRateCustomerMapper.insert(rate);
		} else {
			if(rate.getPrice()==null||rate.getPrice().floatValue()<0.00001) {
				exchangeRateCustomerMapper.deleteById(oldrate.getId());
				return;
			}
			oldrate.setUtctime(new Date());
			oldrate.setOperator(user.getId());
			oldrate.setPrice(rate.getPrice());
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

	@Override
	@Cacheable(value = "exchangeRateCache#20")
	public ExchangeRate selectByName(Date day, String currency) {
		if("CNY".equals(currency)||"RMB".equals(currency)) {
			ExchangeRate exchangeRate=new ExchangeRate();
			exchangeRate.setName(currency);
			exchangeRate.setPrice(new BigDecimal("100"));
			return exchangeRate;
		}
		LambdaQueryWrapper<ExchangeRateHis> query=new LambdaQueryWrapper<>();
		query.eq(ExchangeRateHis::getName,currency);
		query.eq(ExchangeRateHis::getByday,day);
		List<ExchangeRateHis> hisList = exchangeRateHisMapper.selectList(query);
		ExchangeRateHis his=hisList!=null && !hisList.isEmpty() ?hisList.get(0):null;
		if(his==null){
			LambdaQueryWrapper<ExchangeRateHis> query2=new LambdaQueryWrapper<>();
			query2.eq(ExchangeRateHis::getName,currency);
			query2.gt(ExchangeRateHis::getByday,day);
			query2.orderByAsc(ExchangeRateHis::getByday);
			List<ExchangeRateHis> hisList2 = exchangeRateHisMapper.selectList(query2);
			his=hisList2!=null && !hisList2.isEmpty() ?hisList2.get(0):null;
		}
		if(his==null){
			return this.selectByName(currency);
		}
		ExchangeRate exchangeRate=new ExchangeRate();
		BeanUtil.copyProperties(his,exchangeRate);
		return exchangeRate;
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


	@Override
	public void setExcelBook(SXSSFWorkbook workbook, List<Map<String, Object>> list) {
		Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
		titlemap.put("symbol", "币种");
		titlemap.put("name", "货币符号");
		titlemap.put("price", "标准汇率(以100RMB为基准)");
		titlemap.put("myprice", "我的汇率");
		if (list.size() > 0 && list != null) {
			Sheet sheet = workbook.createSheet("sheet1");
			// 在索引0的位置创建行（最顶端的行）
			Row trow = sheet.createRow(0);
			Cell cell = null;
			Object[] titlearray = titlemap.keySet().toArray();
			for (int i = 0; i < titlearray.length; i++) {
				cell = trow.createCell(i); // 在索引0的位置创建单元格(左上端)
				Object value = titlemap.get(titlearray[i].toString());
				cell.setCellValue(value.toString());
			}
			for (int i = 0; i < list.size(); i++) {
				Row row = sheet.createRow(i + 1);
				Map<String, Object> map = list.get(i);
				for (int j = 0; j < titlearray.length; j++) {
					cell = row.createCell(j); // 在索引0的位置创建单元格(左上端)
					Object value = map.get(titlearray[j].toString());
					if (value != null) {
						cell.setCellValue(value.toString());
					}
				}
			}
		} else {
			try {
				throw new Exception("没有数据可导出！");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
