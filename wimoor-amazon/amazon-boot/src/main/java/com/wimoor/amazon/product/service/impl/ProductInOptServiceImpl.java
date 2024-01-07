package com.wimoor.amazon.product.service.impl;

import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.amazon.spapi.model.catalogitems.Dimension;
import com.amazon.spapi.model.catalogitems.Dimensions;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.googlecode.aviator.AviatorEvaluator;
import com.wimoor.amazon.api.AdminClientOneFeignManager;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.common.pojo.entity.DaysalesFormula;
import com.wimoor.amazon.common.service.IDaysalesFormulaService;
import com.wimoor.amazon.product.mapper.ProductInOptMapper;
import com.wimoor.amazon.product.mapper.ProductInTagsMapper;
import com.wimoor.amazon.product.mapper.ProductInfoMapper;
import com.wimoor.amazon.product.mapper.ProductPriceMapper;
import com.wimoor.amazon.product.mapper.ProductRemarkHistoryMapper;
import com.wimoor.amazon.product.pojo.dto.ProductPriceDTO;
import com.wimoor.amazon.product.pojo.entity.ProductInOpt;
import com.wimoor.amazon.product.pojo.entity.ProductInTags;
import com.wimoor.amazon.product.pojo.entity.ProductInfo;
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
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
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
	@Autowired
	ProductInTagsMapper productInTagsMapper;
	@Autowired
	AdminClientOneFeignManager adminClientOneFeign;
	@Resource
	IDaysalesFormulaService daysalesFormulaService;
	@Resource
	IMarketplaceService iMarketplaceService;
	@Resource
	ProductInfoMapper productInfoMapper;
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
	
	@Override
	@Cacheable(value = "ProductInOpt#60")
	public ProductInOpt getCacheableById(Serializable id) {
		// TODO Auto-generated method stub
		return super.getById(id);
	}
	
	@CacheEvict(value = "DaysalesFormulaCache",  key="#user.companyid")
	public String saveformulaData(UserInfo user, String formuladata) {
		String formulastr = formuladata.replaceAll("30日销量", "summonth").replaceAll("15日销量", "sumhalfmonth").replaceAll("7日销量", "sumseven").replaceAll("月销量", "summonth");
		String formulastr2 = formuladata.replaceAll("30日销量", "{0}").replaceAll("15日销量", "{1}").replaceAll("7日销量", "{2}").replaceAll("月销量", "{0}");
		try {
			AviatorEvaluator.compile(formulastr);
		} catch (Exception e) {
			throw new BizException("表达式错误!");
		}
		if (GeneralUtil.conValidate(formulastr2)) {
			throw new BizException("含有无法识别的字段!");
		}
		DaysalesFormula formula = daysalesFormulaService.selectByShopid(user.getCompanyid());
		if (formula != null) {
			formula.setFormula(formulastr);
			formula.setFormulaName(formuladata);
			formula.setOperator(user.getId());
			formula.setOpttime(new Date());
			daysalesFormulaService.updateById(formula);
		} else {
			formula = new DaysalesFormula();
			formula.setCreatedate(new Date());
			formula.setCreator(user.getId());
			formula.setOperator(user.getId());
			formula.setOpttime(new Date());
			formula.setShopid(user.getCompanyid());
			formula.setFormula(formulastr);
			formula.setFormulaName(formuladata);
			daysalesFormulaService.save(formula);
		}
		return formuladata;
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
	
	public List<Map<String,Object>> saveTagsByPid(String pid,String tagids,String userid) {
		Set<String> tagsIdsList=new HashSet<String>();
		if(tagids!=null&&tagids.contains(",")) {
			//先删除老的 再save
			QueryWrapper<ProductInTags> queryWrapper=new QueryWrapper<ProductInTags>();
			queryWrapper.eq("pid", pid);
			productInTagsMapper.delete(queryWrapper);
			tagids=tagids.substring(0, tagids.length()-1);
			String[] tagsArray = tagids.split(",");
			for (int i = 0; i < tagsArray.length; i++) {
				String tagid = tagsArray[i];
				ProductInTags entity=new ProductInTags();
				entity.setPid(pid);
				entity.setOperator(userid);
				entity.setOpttime(new Date());
				entity.setTagid(tagid);
				productInTagsMapper.insert(entity);
				tagsIdsList.add(tagid);
			}
		}else {
			//清空了标签
			QueryWrapper<ProductInTags> queryWrapper=new QueryWrapper<ProductInTags>();
			queryWrapper.eq("pid", pid);
			productInTagsMapper.delete(queryWrapper);
		}
		Result<List<Map<String,Object>>> tagnamelistResult=adminClientOneFeign.findTagsNameByIds(tagsIdsList);
		List<Map<String,Object>> tagnamelist=tagnamelistResult.getData();
		return tagnamelist;
	}
	
	@Override
	public String findProductTagsByPid(String pid) {
		String strs="";
		QueryWrapper<ProductInTags> queryWrapper=new QueryWrapper<ProductInTags>();
		queryWrapper.eq("pid", pid);
		List<ProductInTags> list = productInTagsMapper.selectList(queryWrapper);
		if(list!=null && list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				ProductInTags item = list.get(i);
				strs+=(item.getTagid()+",");
			}
		}
		if(strs.contains(",")) {
			strs=strs.substring(0, strs.length()-1);
		}
		return strs;
	}
	@Override
	public String findOwnerById(String pid) {
		ProductInOpt pinfo = this.baseMapper.selectById(pid);
		if(pinfo!=null) {
			return pinfo.getOwner();
		}else {
			return null;
		}
	}
	@Override
	public String updateOwnerById(UserInfo user,String pid, String ownerid) {
		ProductInOpt pinfo = this.baseMapper.selectById(pid);
		if(StrUtil.isNotEmpty(ownerid)) {
			if(pinfo!=null) {
				pinfo.setOwner(ownerid);
				this.baseMapper.updateById(pinfo);
				return "ok"; 
			}else {
				pinfo=new ProductInOpt();
				pinfo.setPid(new BigInteger(pid));
				pinfo.setOperator(new BigInteger(user.getId()));
				pinfo.setLastupdate(new Date());
				pinfo.setDisable(false);
				pinfo.setOwner(ownerid);
				this.baseMapper.insert(pinfo);
				return "ok"; 
			}
		}else {
			throw new BizException("负责人不能为空！");
		}
			
	}
	@Override
	public void setExcelMskuBook(SXSSFWorkbook workbook, Map<String, Object> param) {
		// TODO Auto-generated method stub
		Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
		titlemap.put("sku", "平台SKU");
		titlemap.put("msku", "本地SKU");
		titlemap.put("market", "站点");
		titlemap.put("groupname", "店铺");
		titlemap.put("asin", "ASIN");
		List<Map<String, Object>> list = this.baseMapper.findMSKUByShopid(param);
		Sheet sheet = workbook.createSheet("sheet1");
		sheet.setColumnWidth(0, 30 * 256);
		sheet.setColumnWidth(1, 30 * 256);
		sheet.setColumnWidth(2, 10 * 256);
		sheet.setColumnWidth(3, 20 * 256);
		sheet.setColumnWidth(4, 40 * 256);
		Row trow = sheet.createRow(0);
		Cell cell = null;
		Object[] titlearray = titlemap.keySet().toArray();
		for (int i = 0; i < titlearray.length; i++) {
			cell = trow.createCell(i); // 在索引0的位置创建单元格(左上端)
			Object value = titlemap.get(titlearray[i].toString());
			cell.setCellValue(value.toString());
		}
		cell = trow.createCell(titlearray.length); // 在索引0的位置创建单元格(左上端)
		cell.setCellValue("提示");
		if (list.size() > 0 && list != null) {
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
				cell = row.createCell(titlearray.length); 
				if(map.get("msku")!=null&&map.get("id")==null) {
					cell.setCellValue("手动对应【关系异常,未找到本地SKU】");
				}
				else if(map.get("msku")==null&&map.get("id")!=null) {
					cell.setCellValue("平台SKU自动对应");
				}else if(map.get("msku")!=null&&map.get("id")!=null){
					cell.setCellValue("手动对应");
				}else {
					cell.setCellValue("平台SKU自动对应【关系异常,未找到本地SKU】");
				}
				
			}
		} 
	}
	public int saveOptMsku(String pid, String msku) {
		ProductInOpt opt = this.baseMapper.selectById(pid);
		if (opt != null) {
			opt.setMsku(msku);
			return this.baseMapper.updateById(opt);
		} else {
			opt = new ProductInOpt();
			opt.setPid(new BigInteger(pid));
			opt.setDisable(false);
			opt.setMsku(msku);
			opt.setLastupdate(new Date());
			return this.baseMapper.insert(opt);
		}
	}
	public int updateOptMsku(ProductInfo productInfo, String msku, String shopid) {
		int result = 0;
		if (productInfo != null) {
			String sku = productInfo.getSku();
			String marketplaceid = productInfo.getMarketplaceid();
			  Marketplace market = iMarketplaceService.findMapByMarketplaceId().get(marketplaceid);
			if (market != null && ("EU".equals(market.getRegion())|| "UK".equals(market.getRegion()))) {
				 List<Marketplace> marketEUList = iMarketplaceService.findMarketplaceByRegion(market.getRegion());
				for (int i = 0; i < marketEUList.size(); i++) {
					LambdaQueryWrapper<ProductInfo> query=new LambdaQueryWrapper<ProductInfo>();
					query.eq(ProductInfo::getSku, sku);
					query.eq(ProductInfo::getAmazonAuthId, productInfo.getAmazonAuthId());
					query.eq(ProductInfo::getMarketplaceid, marketEUList.get(i).getMarketplaceid());
					ProductInfo productList = productInfoMapper.selectOne(query);
					if (productList != null) {
						result += saveOptMsku(productList.getId(), msku);
					}
				}
					 
			} else {
				result = saveOptMsku(productInfo.getId(), msku);
			}
		}
		return result;
	}
	public void uploadMskuFile(UserInfo user, InputStream inputStream, Row info) {
		String sku = info.getCell(0).getStringCellValue();
		String msku = info.getCell(1).getStringCellValue();
		String marketname = info.getCell(2).getStringCellValue();
		String groupname = info.getCell(3).getStringCellValue();
		if(StrUtil.isEmpty(msku)) {
			return;
		}
		if(StrUtil.isEmpty(groupname)) {
			throw new BizException("Excel文件中“店铺”必填字段为空！");
		}
		if(StrUtil.isEmpty(marketname)) {
			throw new BizException("Excel文件中“站点”必填字段为空！");
		}
		ProductInfo product = this.baseMapper.findProductInfoByUpload(sku, marketname, user.getCompanyid(), groupname);
		if (product != null) {
			if (StrUtil.isNotEmpty(msku)) {
			    updateOptMsku(product, msku, user.getCompanyid());
			} else {
				throw new BizException("Excel文件中“本地SKU”必填字段为空！");
			}
		} else {
			throw new BizException("Excel文件中此平台SKU在系统中找不到！");
		}
	}
	@Override
	public void updateProductOwner(UserInfo user, String msku, String owner,String oldowner) {
		List<ProductInfo> plist = productInfoMapper.selectByMSku(msku, null, null, user.getCompanyid());
		if(plist!=null && plist.size()>0) {
			for (ProductInfo item:plist) {
				if(item!=null) {
					if(StrUtil.isNotEmpty(oldowner)) {
						ProductInOpt opt = this.baseMapper.selectById(item.getId());
						String poldowner=null;
						if(opt!=null&&StrUtil.isNotBlank(opt.getOwner())) {
							  poldowner=opt.getOwner();
						}
						if(poldowner==null||oldowner.equals(opt.getOwner())) {
							if(opt!=null) {
								opt.setOwner(owner);
								this.baseMapper.updateById(opt);
							}else {
								opt=new ProductInOpt();
								opt.setPid(new BigInteger(item.getId()));
								opt.setOwner(owner);
								opt.setOperator(new BigInteger(user.getId()));
								this.baseMapper.insert(opt);
							}
						}
					}else {
						//沒有old
						ProductInOpt opt = this.baseMapper.selectById(item.getId());
						if(opt!=null) {
							opt.setOwner(owner);
							this.baseMapper.updateById(opt);
						}else {
							opt=new ProductInOpt();
							opt.setPid(new BigInteger(item.getId()));
							opt.setOwner(owner);
							opt.setOperator(new BigInteger(user.getId()));
							this.baseMapper.insert(opt);
						}
					}
				}
			}
		}
	}
	
	public List<Map<String, Object>> getMonthSumNum(Map<String,Object> param){
		return this.baseMapper.getMonthSumNum(param);
	}
 
}
