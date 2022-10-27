package com.wimoor.amazon.product.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazon.spapi.model.catalogitems.Dimension;
import com.amazon.spapi.model.catalogitems.Dimensions;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.product.mapper.ProductInOptMapper;
import com.wimoor.amazon.product.mapper.ProductPriceMapper;
import com.wimoor.amazon.product.mapper.ProductRemarkHistoryMapper;
import com.wimoor.amazon.product.pojo.dto.ProductPriceDTO;
import com.wimoor.amazon.product.pojo.entity.ProductInOpt;
import com.wimoor.amazon.product.pojo.entity.ProductPrice;
import com.wimoor.amazon.product.pojo.vo.ProductPriceVo;
import com.wimoor.amazon.product.service.IProductInOptService;
import com.wimoor.amazon.profit.pojo.entity.ProfitConfig;
import com.wimoor.amazon.profit.pojo.entity.ProfitConfigCountry;
import com.wimoor.amazon.profit.pojo.vo.InputDimensions;
import com.wimoor.amazon.profit.pojo.vo.ItemMeasure;
import com.wimoor.amazon.profit.service.IProfitCfgService;
import com.wimoor.amazon.profit.service.IProfitService;
import com.wimoor.amazon.util.AmzDateUtils;

import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 产品信息 服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2022-05-27
 */
@Service
public class ProductInOptServiceImpl extends ServiceImpl<ProductInOptMapper, ProductInOpt> implements IProductInOptService {

	@Resource 
	ProductPriceMapper productPriceMapper;
	@Resource
	IProfitCfgService profitCfgService;
	@Resource
	IProfitService profitService;
	@Autowired
	ProductRemarkHistoryMapper productRemarkHistoryMapper;
	
	@Override
	public void refreshAllProductAdv() {
		// TODO Auto-generated method stub
		this.baseMapper.updateAllOpt();
	}
	public  IPage<ProductPriceVo>  priceQueue(ProductPriceDTO dto){
		if(StrUtil.isNotEmpty(dto.getSearch())) {
			dto.setSearch("%"+dto.getSearch().trim()+"%");
		}else {
			dto.setSearch(null);
		}
		return productPriceMapper.priceQueue(dto.getPage(),dto);
	}
	
	public List<Map<String, Object>> findMaterialSizeByCondition(Map<String, Object> param){
		Object objcountry = param.get("country");
		Object objgroupid = param.get("groupid");
		if (objcountry == null || objgroupid == null) {
			return null;
		}
		String country = objcountry.toString();
		String groupid = objgroupid.toString();
		String profitcfgid = profitCfgService.findDefaultPlanIdByGroup(groupid);// 使用店铺默认计算方案
		ProfitConfig profitcfg = profitCfgService.findConfigAction(profitcfgid);
		ProfitConfigCountry profitConfigCountry = profitcfg.getProfitConfigCountry(country);
		List<Map<String, Object>> totalresult = this.baseMapper.selectMaterialSize(param);
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		result.addAll(totalresult);
		int j = 0;// remove次数
		for (int i = 0; i < totalresult.size(); i++) {
			Map<String, Object> item = totalresult.get(i);
			InputDimensions selfInputDimension = getInputDimesionsBySelf(item);
			InputDimensions countryInputDimension = getInputDimesionsByCountry(item);
			if (countryInputDimension.getLength() == null || countryInputDimension.getWidth() == null
					|| countryInputDimension.getHeight() == null || countryInputDimension.getWeight() == null) {
				j++;// remove次数
				result.remove(i - j + 1);
				continue;
			}
			Object price = item.get("price");// 售价
			BigDecimal sellprice = new BigDecimal(price != null ? price.toString() : "0");
			Object isSmlAndLightObj = item.get("isSmlAndLight");
			boolean isSmlAndLight = isSmlAndLightObj != null ? (Boolean) isSmlAndLightObj : false;// 是否輕小
			String pgroup = item.get("pgroup").toString();// 亚马逊那边产品种类
			String category = item.get("category").toString();// 本地录入的产品种类
			Map<String, String> selfttirMap = profitService.getProductTier
					(profitConfigCountry, selfInputDimension, country, "0", sellprice, isSmlAndLight, category);
			String fcurrency = selfttirMap.get("fcurrency");
			String selfFBA = selfttirMap.get("fba");
			Map<String, String> countrytirMap = null;
			BigDecimal amzFBA = null;
			String productTier = "";
			Object total = item.get("totalfee");
			Object referral = item.get("referralfee");
			Object objproductTier = item.get("productTier");
			Object edfba = item.get("edfba");// fbaFee.getExpectedDomesticFulfillmentFeePerUnit();
			Object efnfba = item.get("efnfba");// fbaFee.getExpectedEfnFulfilmentFeePerUnitUk();
			Object fixclosing = item.get("fixclosing");// estimated-fixed-closing-fee
			Object lastupdate = item.get("lastupdate");
			if (total == null || referral == null) {
				countrytirMap = profitService.getProductTier
						(profitConfigCountry, countryInputDimension, country, "0", sellprice, isSmlAndLight, pgroup);
				productTier = countrytirMap.get("productTier");
				amzFBA = new BigDecimal(countrytirMap.get("fba"));
			} else {// 拿报表数据
				BigDecimal totalfee = new BigDecimal(total.toString());
				BigDecimal referralfee = new BigDecimal(referral.toString());
				BigDecimal fab_report = totalfee.subtract(referralfee);
				String fenpeiType = profitConfigCountry.getFenpeiType();
				if ("EFN".equals(fenpeiType) && !"UK".equals(country)) {
					BigDecimal edfbafee = null;
					BigDecimal efnfbafee = null;
					if (edfba == null) {
						edfbafee = new BigDecimal("0");
					} else {
						edfbafee = new BigDecimal(edfba.toString());
					}
					if (efnfba == null) {
						efnfbafee = new BigDecimal("0");
					} else {
						efnfbafee = new BigDecimal(efnfba.toString());
					}
					amzFBA = fab_report.subtract(edfbafee).add(efnfbafee);
					item.put("fab_domestic", fab_report);
				} else {
					amzFBA = fab_report;
				}
				if ("IN".equals(country) && fixclosing != null) {
					BigDecimal fixclosingFee = new BigDecimal(fixclosing.toString());
					amzFBA = amzFBA.subtract(fixclosingFee);
				}
				if (objproductTier == null) {
					countrytirMap = profitService.getProductTier
							(profitConfigCountry, countryInputDimension, country, "0", sellprice, isSmlAndLight, pgroup);
					productTier = countrytirMap.get("productTier");
				} else {
					productTier = objproductTier.toString();
				}
			}
			float overcharge = 0f;
			Object sales_month = item.get("sales_month");// 月销量
			if (sales_month != null && amzFBA.floatValue() > Float.parseFloat(selfFBA)) {
				overcharge = (amzFBA.floatValue() - Float.parseFloat(selfFBA)) * Float.parseFloat(sales_month.toString());
			}
			String selfsize = getSize(selfInputDimension);
			String countrysize = getSize(countryInputDimension);
			String selfweight = getWeight(selfInputDimension);
			String countryweight = getWeight(countryInputDimension);

			item.put("selfweight", selfweight);
			item.put("countryweight", countryweight);
			item.put("selfsize", selfsize);
			item.put("countrysize", countrysize);
			item.put("selftier", selfttirMap.get("productTier"));
			item.put("countrytier", productTier);
			item.put("selfFBA", selfFBA);
			item.put("amzFBA", amzFBA);
			item.put("fcurrency", fcurrency);
			item.put("canshu", "自测参数<br/>FBA参数");
			item.put("overcharge", overcharge);
			item.put("lastupdate", AmzDateUtils.getDate(lastupdate));
			if ("ltself".equals(param.get("sizetype")) && (selfFBA == null || Float.parseFloat(selfFBA) <= amzFBA.floatValue())) {
				j++;// remove次数
				result.remove(i - j + 1);
				continue;
			}
			if ("etself".equals(param.get("sizetype")) && (selfFBA == null || Float.parseFloat(selfFBA) != amzFBA.floatValue())) {
				j++;// remove次数
				result.remove(i - j + 1);
				continue;
			}
			if ("gtself".equals(param.get("sizetype")) && (selfFBA == null || Float.parseFloat(selfFBA) >= amzFBA.floatValue())) {
				j++;// remove次数
				result.remove(i - j + 1);
				continue;
			}
			if ("true".equals(param.get("isgtself")) && (selfFBA == null || Float.parseFloat(selfFBA) >= amzFBA.floatValue())) {
				j++;// remove次数
				result.remove(i - j + 1);
				continue;
			}
		}
		return result;
	}
	
	private InputDimensions getInputDimesionsBySelf(Map<String, Object> item) {
		InputDimensions inputDimension = new InputDimensions();
		Object lengthobj = item.get("slength");
		Object length_unitsobj = item.get("slength_units");
		Object widthobj = item.get("swidth");
		Object width_unitsobj = item.get("swidth_units");
		Object heightobj = item.get("sheight");
		Object height_unitsobj = item.get("sheight_units");
		Object weightobj = item.get("sweight");
		Object weight_unitsobj = item.get("sweight_units");

		ItemMeasure length = new ItemMeasure();
		length.setValue(lengthobj == null ? "0" : lengthobj.toString());
		length.setUnits(length_unitsobj == null ? "cm" : length_unitsobj.toString());
		inputDimension.setLength(length);

		ItemMeasure width = new ItemMeasure();
		width.setValue(widthobj == null ? "0" : widthobj.toString());
		width.setUnits(width_unitsobj == null ? "cm" : width_unitsobj.toString());
		inputDimension.setWidth(width);

		ItemMeasure height = new ItemMeasure();
		height.setValue(heightobj == null ? "0" : heightobj.toString());
		height.setUnits(height_unitsobj == null ? "cm" : height_unitsobj.toString());
		inputDimension.setHeight(height);

		ItemMeasure weight = new ItemMeasure();
		weight.setValue(weightobj == null ? "0" : weightobj.toString());
		weight.setUnits(weight_unitsobj == null ? "kg" : weight_unitsobj.toString());
		inputDimension.setWeight(weight);
		return inputDimension;
	}
	private InputDimensions getInputDimesionsByCountry(Map<String, Object> item) {
		Object lengthobj = item.get("length");
		Object length_unitsobj = item.get("length_units");
		Object widthobj = item.get("width");
		Object width_unitsobj = item.get("width_units");
		Object heightobj = item.get("height");
		Object height_unitsobj = item.get("height_units");
		Object weightobj = item.get("weight");
		Object weight_unitsobj = item.get("weight_units");
		Dimensions dimensions = new Dimensions();
		if (lengthobj != null && length_unitsobj != null) {
			 
			Dimension length=new Dimension();
			length.setValue(new BigDecimal(lengthobj.toString()));
			length.setUnit(length_unitsobj.toString());
			dimensions.setLength(length);
		}
		if (widthobj != null && width_unitsobj != null) {
			Dimension width=new Dimension();
			width.setValue(new BigDecimal(widthobj.toString()));
			width.setUnit(width_unitsobj.toString());
			dimensions.setWidth(width);
		}
		if (heightobj != null && height_unitsobj != null) {
			Dimension height=new Dimension();
			height.setValue(new BigDecimal(heightobj.toString()));
			height.setUnit(height_unitsobj.toString());
			dimensions.setHeight(height);
		}
		if (weightobj != null && weight_unitsobj != null) {
			Dimension weight=new Dimension();
			weight.setValue(new BigDecimal(weightobj.toString()));
			weight.setUnit(weight_unitsobj.toString());
			dimensions.setWeight(weight);
		}
		InputDimensions temp = InputDimensions.getTrueInputDimesions(dimensions);
		return temp;
	}
	private String getSize(InputDimensions selfInputDimension)  {
		String length = selfInputDimension.getLength() != null ? 
				selfInputDimension.getLength("cm").getValue().setScale(1, RoundingMode.HALF_UP).toString() : "";
		String height = selfInputDimension.getHeight() != null ? 
				selfInputDimension.getHeight("cm").getValue().setScale(1, RoundingMode.HALF_UP).toString() : "";
		String width = selfInputDimension.getWidth() != null ? 
				selfInputDimension.getWidth("cm").getValue().setScale(1, RoundingMode.HALF_UP).toString() : "";
		return length + (width != null ? "×" + width : "") + (height != null ? "×" + height : "");
	}

	private String getWeight(InputDimensions selfInputDimension) {
		String weight = selfInputDimension.getWeight() != null ? 
				selfInputDimension.getWeight("kg").getValue().setScale(2, RoundingMode.HALF_UP).toString() : "";
		return weight;
	}
	
	public List<Map<String, Object>> getProRemarkHis(String pid,String ftype) {
		List<Map<String, Object>> list = productRemarkHistoryMapper.getProRemarkHisByPid(pid, ftype);
		return list;
	}
	
	public List<ProductPrice> findPrice(String pid) {
		List<ProductPrice> list = productPriceMapper.findbyProductID(pid);
		return list;
	}
}
