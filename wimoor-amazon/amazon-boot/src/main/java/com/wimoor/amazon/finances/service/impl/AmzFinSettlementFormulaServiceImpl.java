package com.wimoor.amazon.finances.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.googlecode.aviator.AviatorEvaluator;
import com.wimoor.amazon.finances.mapper.AmzFinSettlementFormulaMapper;
import com.wimoor.amazon.finances.pojo.entity.AmzFinSettlementFormula;
import com.wimoor.amazon.finances.service.IAmzFinSettlementFormulaService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;
import com.wimoor.amazon.finances.service.IAmzFinUserItemService;
import cn.hutool.core.util.StrUtil;
@Service
public class AmzFinSettlementFormulaServiceImpl extends ServiceImpl<AmzFinSettlementFormulaMapper, AmzFinSettlementFormula> implements IAmzFinSettlementFormulaService {
    @Autowired
    IAmzFinUserItemService IAmzFinUserItemService;
    
	public Map<String,Object> getAmzFinSettlementFormulaConvert(String shopid) {
		LambdaQueryWrapper<AmzFinSettlementFormula> query = new LambdaQueryWrapper<AmzFinSettlementFormula>();
		Map<String,Object> map=new HashMap<String,Object>();
		query.eq(AmzFinSettlementFormula::getShopid, shopid);
		List<AmzFinSettlementFormula> list = this.baseMapper.selectList(query);
		if (list != null && list.size() > 0) {
			AmzFinSettlementFormula formula = list.get(0);
			Map<String, String> titleBasemap = findSysFinMap();
			Map<String, String> finItemMap = findFinMapByShopid(shopid);
			String formulastr = formula.getFormula();
			for (Entry<String, String> entry : finItemMap.entrySet()) {
				if (formulastr.contains("field" + entry.getKey())) {
					formulastr = formulastr.replace("field" + entry.getKey(), entry.getValue());
				}
			}
			for (Entry<String, String> entry : titleBasemap.entrySet()) {
				if (formulastr.contains(entry.getKey())) {
					formulastr = formulastr.replace(entry.getKey(), entry.getValue());
				}
			}
			map.put("formulaData", formulastr);
			map.put("pricetype", formula.getPricetype());
			return map;
		} else {
			map.put("formulaData", "(运费预估+其它成本+采购成本+仓储费+长期仓储费+广告费用+VAT税费)");
			map.put("pricetype", 1);
			return map;
		}
	}
	
	

	@CacheEvict(value = { "findSettlementSKUCache" }, allEntries = true)
	public String saveformulaData(UserInfo user, String formuladata,String pricetype) {
		String formulastr = formuladata;
		String formulastr2 = formuladata;
		String formulafield = null;
		Map<String, String> titlemapBase = findSysFinMap();
		Map<String, String> titlemapFinItem = findFinMapByShopid(user.getCompanyid());
		for (Entry<String, String> entry : titlemapFinItem.entrySet()) {
			if (formulastr.contains(entry.getValue())) {
				formulastr = formulastr.replace(entry.getValue(), "field" + entry.getKey());
				formulastr2 = formulastr2.replace(entry.getValue(), "");
				if (formulafield != null) {
					formulafield = formulafield + "," + entry.getKey();
				} else {
					formulafield = entry.getKey();
				}
			}
		}
		for (Entry<String, String> entry : titlemapBase.entrySet()) {
			if (formulastr.contains(entry.getValue())) {
				if(formulastr.indexOf("+"+entry.getValue())>0) {
					formulastr = formulastr.replace("+"+entry.getValue(), "+"+entry.getKey());
					formulastr2 = formulastr2.replace("+"+entry.getValue(), "");
				}else {
					formulastr = formulastr.replace(entry.getValue(), entry.getKey());
					formulastr2 = formulastr2.replace(entry.getValue(), "");
				}
				
			
				if (formulafield != null) {
					formulafield = formulafield + "," + entry.getKey();
				} else {
					formulafield = entry.getKey();
				}
			}
		}
		try {
			AviatorEvaluator.compile(formulastr);
		} catch (Exception e) {
			throw new BizException("表达式错误");
		}
		if (GeneralUtil.conValidate(formulastr2)) {
			throw new BizException("含有无法识别的字段");
		}
		AmzFinSettlementFormula formula = getAmzFinSettlementFormula(user.getCompanyid());
		if (formula.getShopid() != null) {
			formula.setFormula(formulastr);
			formula.setOperator(user.getId());
			formula.setOpttime(new Date());
			formula.setField(formulafield);
			if(StrUtil.isNotBlank(pricetype)) {
				formula.setPricetype(Integer.parseInt(pricetype));
			}
			this.baseMapper.updateById(formula);
		} else {
			formula = new AmzFinSettlementFormula();
			formula.setCreatedate(new Date());
			formula.setCreator(user.getId());
			formula.setOperator(user.getId());
			formula.setOpttime(new Date());
			formula.setShopid(user.getCompanyid());
			formula.setFormula(formulastr);
			formula.setField(formulafield);
			if(StrUtil.isNotBlank(pricetype)) {
				formula.setPricetype(Integer.parseInt(pricetype));
			}
			this.baseMapper.insert(formula);
		}
		return formuladata;
	}
	
	

	public static Map<String, String> findSysFinMap() {
		Map<String, String> titlemap = new HashMap<String, String>();
		titlemap.put("setincome", "结算收入");
		titlemap.put("spend", "广告费用");
		titlemap.put("price", "采购成本");
		titlemap.put("othersfee", "其它成本");
		titlemap.put("principal", "销售额");
		titlemap.put("salenum", "销售数量");
		titlemap.put("ordernum", "订单数量");
		titlemap.put("avgprice", "平均售价");
		titlemap.put("commission", "销售佣金");
		titlemap.put("fbafee", "FBA费用");
		
		titlemap.put("shipping", "买家运费");
		titlemap.put("promotion", "促销");
		titlemap.put("otherfee", "其它收支");
		
		titlemap.put("refund", "退款金额");
		titlemap.put("refundnum", "退款数量");

		titlemap.put("shipmentfee", "运费预估");
		titlemap.put("storagefee", "仓储费");
		titlemap.put("longTermFee", "长期仓储费");
		titlemap.put("firstShipment", "货件头程运费");
		titlemap.put("profit_vat", "VAT税费");
		titlemap.put("reimbursementsFee", "赔偿费用");
		
		
		titlemap.put("share_storagefee", "店铺分摊仓储费");
		titlemap.put("share_longstoragefee", "店铺分摊长期仓储费");
		titlemap.put("share_advspendfee",  "店铺分摊广告费");
		titlemap.put("share_couponredemptionfee",  "店铺分摊折扣券");
		titlemap.put("share_reservefee", "店铺分摊预留金");
		titlemap.put("share_reimbursementfee", "店铺分摊赔偿");
		titlemap.put("share_shopotherfee",  "店铺分摊其它收支");
      
    			
		titlemap.put("returntax", "退税");
 		
  
		titlemap.put("profit_otherfee","预估其他");
		titlemap.put("profit_marketfee", "预估市场费用");
		titlemap.put("profit_costrate", "预估其他成本");
		titlemap.put("profit_exchangelost", "预估汇率损耗");
		titlemap.put("profit_customstax", "预估关税");
		titlemap.put("profit_companytax","预估所得税");
    	
		titlemap.put("fifo_shipmentFee", "先进显出运费成本");
		titlemap.put("fifo_cost", "先进先出采购成本");
		return titlemap;
	}

	public static Map<String, String> findSysNeedDisplayFinMap() {
		Map<String, String> titlemap = new HashMap<String, String>();
		titlemap.put("price", "采购成本");
		titlemap.put("shipmentfee", "运费预估");
		titlemap.put("othersfee", "其它成本");
		titlemap.put("firstShipment", "货件头程运费");
		titlemap.put("vat", "VAT税费");
		titlemap.put("itemshopfee", "店铺费用");
		return titlemap;
	}

	
	public Map<String, String> findFinMapByShopid(String shopid) {
		Map<String, String> titlemap = new HashMap<String, String>();
		List<Map<String, Object>> list = IAmzFinUserItemService.findFinListByShopid(shopid);
		for (Map<String, Object> item : list) {
			titlemap.put(item.get("id").toString(), item.get("name").toString());
		}
		return titlemap;
	}

	public AmzFinSettlementFormula getAmzFinSettlementFormula(String shopid) {
		LambdaQueryWrapper<AmzFinSettlementFormula> query = new LambdaQueryWrapper<AmzFinSettlementFormula>();
		query.eq(AmzFinSettlementFormula::getShopid, shopid);
		List<AmzFinSettlementFormula> list = this.baseMapper.selectList(query);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			AmzFinSettlementFormula formula = new AmzFinSettlementFormula();
			formula.setFormula("(shipmentfee+othersfee+price+spend+storagefee+longTermFee+vat)");
			formula.setField("shipmentfee,storagefee,longTermFee,othersfee,price,spend,vat");
			formula.setPricetype(1);
			return formula;
		}
	}

	public Map<String, String> getformulaTitle(String shopid) {
		AmzFinSettlementFormula formula = getAmzFinSettlementFormula(shopid);
		String field = formula.getField();
		String[] fields = field.split(",");
		Map<String, String> titlemap1 = new HashMap<String, String>();
		Map<String, String> titlemap2 = findSysNeedDisplayFinMap();
		List<Map<String, Object>> list = IAmzFinUserItemService.findFinListByShopid(shopid);
		for (Map<String, Object> item : list) {
			titlemap1.put(item.get("id").toString(), item.get("name").toString());
		}
		Map<String, String> result = new HashMap<String, String>();
		for (String field2 : fields) {
			if (titlemap2.get(field2) != null) {
				result.put(field2, titlemap2.get(field2));
			} else if (titlemap1.get(field2) != null) {
				result.put("field" + field2, titlemap1.get(field2));
			}
		}
		return result;
	}
}

