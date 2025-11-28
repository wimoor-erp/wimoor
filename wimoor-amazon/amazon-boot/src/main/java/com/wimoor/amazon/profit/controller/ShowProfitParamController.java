package com.wimoor.amazon.profit.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.profit.pojo.dto.ProfitParam;
import com.wimoor.amazon.profit.pojo.dto.ProfitSettingDTO;
import com.wimoor.amazon.profit.pojo.entity.FBAFormat;
import com.wimoor.amazon.profit.pojo.entity.FBASipp;
import com.wimoor.amazon.profit.pojo.entity.InplaceFee;
import com.wimoor.amazon.profit.pojo.entity.InplaceFeeFormat;
import com.wimoor.amazon.profit.pojo.entity.InventoryStorageFee;
import com.wimoor.amazon.profit.pojo.entity.OutboundWeightFormat;
import com.wimoor.amazon.profit.pojo.entity.ProductFormat;
import com.wimoor.amazon.profit.pojo.entity.ProductTier;
import com.wimoor.amazon.profit.pojo.entity.ProfitConfig;
import com.wimoor.amazon.profit.pojo.entity.ReferralFee;
import com.wimoor.amazon.profit.service.IFBASippService;
import com.wimoor.amazon.profit.service.IFbaFormatService;
import com.wimoor.amazon.profit.service.IIndividualFeeService;
import com.wimoor.amazon.profit.service.IInplaceFeeFormatService;
import com.wimoor.amazon.profit.service.IInplaceFeeService;
import com.wimoor.amazon.profit.service.IInventoryStorageFeeService;
import com.wimoor.amazon.profit.service.IOutBoundWeightFormatService;
import com.wimoor.amazon.profit.service.IProductFormatService;
import com.wimoor.amazon.profit.service.IProductTierService;
import com.wimoor.amazon.profit.service.IProfitCfgService;
import com.wimoor.amazon.profit.service.IProfitParameterService;
import com.wimoor.amazon.profit.service.IProfitService;
import com.wimoor.amazon.profit.service.IReferralFeeService;
import com.wimoor.amazon.profit.service.impl.ProfitServiceImpl;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;

@Api(tags = "利润计算参数获取")
@SystemControllerLog( "利润计算方案模块")
@RestController
@RequestMapping("/api/v1/profit/profitParam")
public class ShowProfitParamController {
	@Resource
	private IProfitService profitService;
	@Resource
	IProfitCfgService profitCfgService;
	@Resource
	private IMarketplaceService marketplaceService;
	@Resource
	public IReferralFeeService referralFeeService;
	@Resource
	IProfitParameterService profitParameterService;
	@Autowired
	IProductTierService iProductTierService;
	@Autowired
	IFbaFormatService iFbaFormatService;
	@Autowired
	IProductFormatService iProductFormatService;
	@Autowired
	IReferralFeeService iReferralFeeService;
	@Autowired
	IOutBoundWeightFormatService iOutBoundWeightFormatService;
	@Autowired
	IInventoryStorageFeeService iInventoryStorageFeeService;
	@Autowired
	IInplaceFeeFormatService  inplaceFeeFormatService;
	@Autowired
	IIndividualFeeService individualFeeService;
	@Autowired
	IInplaceFeeService iInplaceFeeService;
	@Autowired
	IFBASippService iFBASippService;
	@SuppressWarnings("unused")
	@GetMapping("/showProfitPage")
    public Result<ProfitParam> showProfitPage(String isSmlAndLight){ 
    	List<ReferralFee> typeList = this.referralFeeService.findAllType();//产品类型列表
    	List<BigDecimal> marginList = this.profitService.findMarginList();//固定利率列表
    	List<String> currencyUnitList = this.profitService.findCurrencyUnitList();
    	UserInfo user= UserInfoContext.get();
    	List<ProfitConfig> profitCfgList = this.profitCfgService.findProfitCfgName(user.getCompanyid());//利润计算方案
    	String defaultPlanId = profitCfgService.findDefaultPlanId(user.getCompanyid());
    	List<Marketplace> itemlist = marketplaceService.findAllMarketplace();//亚马逊站点
    	List<Marketplace> marketlist =new ArrayList<Marketplace>();
    	for(Marketplace mk:itemlist) {
    		if(mk.getDimUnits()!=null) {
    			marketlist.add(mk);
    		}
    	}
       	List<String> categoryList = this.profitService.findCategoryList();//为了计算prep service fee
    	List<String> countryList = new ArrayList<String>();
		if ("true".equals(isSmlAndLight)) {
			countryList = Arrays.asList(ProfitServiceImpl.smlAndLightCountry);
		} else {
			countryList = this.profitService.findCountryList();// 得到需要计算的国家列表
		}
		Map<String,Marketplace> marketmap=new HashMap<String,Marketplace>();
		for(Marketplace market:marketlist) {
			marketmap.put(market.getMarket(), market);
		}
		List<Marketplace> countryMarketList = new ArrayList<Marketplace>();
		for(String country:countryList) {
			Marketplace market=marketmap.get(country);
			countryMarketList.add(market);
		}
    	ProfitParam param=new ProfitParam();
    	param.setTypeList(typeList);
    	param.setMarginList(marginList);
    	param.setCurrencyUnitList(currencyUnitList);
    	param.setProfitCfgList(profitCfgList);
    	param.setDefaultPlanId(defaultPlanId);
    	param.setMarketlist(marketlist);
    	param.setCountryList(countryList);
    	param.setCountryMarketlist(countryMarketList); 
    	return Result.success(param);  
	}
	
	
	/**
	 * 
	 * 获取profitCfg.jsp页面下拉框参数
	 * 
	 * @return
	 * @throws UserException
	 */
	@GetMapping("/storageFee")
	public Result<Map<String, Object>> getConfigAction()  {
		Map<String, Object> resultMap = new HashMap<String,Object>();
		List<BigDecimal> storageFeeMap_UK = profitParameterService.getStorageFeeMap("UK");// 仓储费
		resultMap.put("storageFeeMap_UK", storageFeeMap_UK);
		List<BigDecimal> storageFeeMap_DE = profitParameterService.getStorageFeeMap("DE");// 仓储费
		resultMap.put("storageFeeMap_DE", storageFeeMap_DE);
		List<BigDecimal> storageFeeMap_JP = profitParameterService.getStorageFeeMap("JP");// 仓储费
		resultMap.put("storageFeeMap_JP", storageFeeMap_JP);
		List<BigDecimal> storageFeeMap_CA = profitParameterService.getStorageFeeMap("CA");// 仓储费
		resultMap.put("storageFeeMap_CA", storageFeeMap_CA);
		List<BigDecimal> storageFeeMap_AU = profitParameterService.getStorageFeeMap("AU");// 仓储费
		resultMap.put("storageFeeMap_AU", storageFeeMap_AU);
		List<BigDecimal> storageFeeMap_IN = profitParameterService.getStorageFeeMap("IN");// 仓储费
		resultMap.put("storageFeeMap_IN", storageFeeMap_IN);
		List<BigDecimal> storageFeeMap_MX = profitParameterService.getStorageFeeMap("MX");// 仓储费
		resultMap.put("storageFeeMap_MX", storageFeeMap_MX);
		List<BigDecimal> storageFeeMap_AE = profitParameterService.getStorageFeeMap("AE");// 仓储费
		resultMap.put("storageFeeMap_AE", storageFeeMap_AE);
		List<BigDecimal> storageFeeMap_SA = profitParameterService.getStorageFeeMap("SA");// 仓储费
		resultMap.put("storageFeeMap_SA", storageFeeMap_SA);
		return Result.success(resultMap) ;
	}

	@GetMapping("/getTier")
	public Result<List<ProductTier>> getTierAction(String country)  {
		return Result.success(iProductTierService.lambdaQuery().like(ProductTier::getCountry, "%"+country+"%").list());
	}
	
	
	@PostMapping("/getFBA")
	public Result<Page<FBAFormat>> getFBAAction(@RequestBody ProfitSettingDTO dto)  {
		LambdaQueryWrapper<FBAFormat> query = new LambdaQueryWrapper<FBAFormat>();
		query.like(FBAFormat::getCountry, "%"+dto.getCountry()+"%");
		if(StrUtil.isNotBlank(dto.getTierid())) {
			query.eq(FBAFormat::getProducttierid, dto.getTierid());
		} 
		if(StrUtil.isNotBlank(dto.getDispatchtype())) {
			query.eq(FBAFormat::getDispatchType, dto.getDispatchtype());
		} 
		return Result.success(iFbaFormatService.page(dto.getPage(),query));
	}
	
	@PostMapping("/saveFBA")
	public Result<?> saveFBAAction(@RequestBody FBAFormat fba)  {
		return Result.success(iFbaFormatService.save(fba));
	}
	
	@PostMapping("/updateFBA")
	public Result<?> updateFBAAction(@RequestBody FBAFormat fba)  {
		return Result.success(iFbaFormatService.updateById(fba));
	}
	
	@PostMapping("/deleteFBA")
	public Result<?> deleteFBAAction(@RequestBody FBAFormat fba)  {
		return Result.success(iFbaFormatService.removeById(fba.getId()));
	}
	
	
	@PostMapping("/getTierFormat")
	public Result<Page<ProductFormat>> getTierFormatAction(@RequestBody ProfitSettingDTO dto)  {
		LambdaQueryWrapper<ProductFormat> query = new LambdaQueryWrapper<ProductFormat>();
		query.like(ProductFormat::getCountry, "%"+dto.getCountry()+"%");
		if(StrUtil.isNotBlank(dto.getTierid())) {
			query.eq(ProductFormat::getProducttierid, dto.getTierid());
		} 
		return Result.success(iProductFormatService.page(dto.getPage(),query));
	}
	
	@PostMapping("/saveTierFormat")
	public Result<?> saveTierFormatAction(@RequestBody ProductFormat fba)  {
		return Result.success(iProductFormatService.save(fba));
	}
	
	@PostMapping("/updateTierFormat")
	public Result<?> updateTierFormatAction(@RequestBody ProductFormat fba)  {
		return Result.success(iProductFormatService.updateById(fba));
	}
	
	@PostMapping("/deleteTierFormat")
	public Result<?> deleteTierFormatAction(@RequestBody ProductFormat fba)  {
		if(fba==null||fba.idIsNULL()) {
			return Result.success();
		}
		return Result.success(iProductFormatService.removeById(fba.getId()));
	}
	
	
	@PostMapping("/getTierPage")
	public Result<Page<ProductTier>> getTierPageAction(@RequestBody ProfitSettingDTO dto)  {
		LambdaQueryWrapper<ProductTier> query = new LambdaQueryWrapper<ProductTier>();
		query.like(ProductTier::getCountry, "%"+dto.getCountry()+"%");
		return Result.success(iProductTierService.page(dto.getPage(),query));
	}
	
	@PostMapping("/saveTier")
	public Result<?> saveTierAction(@RequestBody ProductTier fba)  {
		return Result.success(iProductTierService.save(fba));
	}
	
	@PostMapping("/updateTier")
	public Result<?> updateTierAction(@RequestBody ProductTier fba)  {
		return Result.success(iProductTierService.updateById(fba));
	}
	
	@PostMapping("/deleteTier")
	public Result<?> deleteTierFormatAction(@RequestBody ProductTier fba)  {
		return Result.success(iProductTierService.removeById(fba.getId()));
	}
	
	@PostMapping("/getWeightFormat")
	public Result<Page<OutboundWeightFormat>> getWeightFormatAction(@RequestBody ProfitSettingDTO dto)  {
		LambdaQueryWrapper<OutboundWeightFormat> query = new LambdaQueryWrapper<OutboundWeightFormat>();
		if(StrUtil.isNotBlank(dto.getTierid())) {
			query.eq(OutboundWeightFormat::getProducttierid,dto.getTierid());
		}else if(StrUtil.isNotBlank(dto.getCountry())) {
			List<ProductTier> list = iProductTierService.lambdaQuery().eq(ProductTier::getCountry,dto.getCountry()).list();
			if(list!=null&&list.size()>0) {
				List<String> tiers=new LinkedList<String>();
				for(ProductTier item:list) {
					tiers.add(item.getId());
				}
				query.in(OutboundWeightFormat::getProducttierid,tiers);
			}
		}
		Page<OutboundWeightFormat> list = iOutBoundWeightFormatService.page(dto.getPage(),query);
		for(OutboundWeightFormat item:list.getRecords()) {
			ProductTier tier = iProductTierService.getById(item.getProducttierid());
			item.setCountry(tier.getCountry());
			
		}
		return Result.success(list);
	}
	
	@PostMapping("/saveWeightFormat")
	public Result<?> saveWeightFormatAction(@RequestBody OutboundWeightFormat fba)  {
		return Result.success(iOutBoundWeightFormatService.save(fba));
	}
	
	@PostMapping("/updateWeightFormat")
	public Result<?> updateWeightFormatAction(@RequestBody OutboundWeightFormat fba)  {
		return Result.success(iOutBoundWeightFormatService.updateById(fba));
	}
	
	@PostMapping("/deleteWeightFormat")
	public Result<?> deleteWeightFormatAction(@RequestBody OutboundWeightFormat fba)  {
		return Result.success(iOutBoundWeightFormatService.removeById(fba.getId()));
	}
	
	@PostMapping("/getReferralFee")
	public Result<Page<ReferralFee>> getReferralFeeAction(@RequestBody ProfitSettingDTO dto)  {
		LambdaQueryWrapper<ReferralFee> query = new LambdaQueryWrapper<ReferralFee>();
		if(StrUtil.isNotBlank(dto.getCountry())) {
			query.like(ReferralFee::getCountry,"%"+ dto.getCountry()+"%");
			if(dto.getCountry().equals("US")) {
				query.or().eq(ReferralFee::getCountry, "ETC");
			}
		}
		Page<ReferralFee> list = iReferralFeeService.page(dto.getPage(),query);
		return Result.success(list);
	}
	
	@PostMapping("/saveReferralFee")
	public Result<?> saveReferralFeeAction(@RequestBody ReferralFee fba)  {
		return Result.success(iReferralFeeService.save(fba));
	}
	
	@PostMapping("/updateReferralFee")
	public Result<?> updateReferralFeeAction(@RequestBody ReferralFee fba)  {
		return Result.success(iReferralFeeService.updateById(fba));
	}
	
	@PostMapping("/deleteReferralFee")
	public Result<?> deleteReferralFeeAction(@RequestBody ReferralFee fba)  {
		return Result.success(iReferralFeeService.removeById(fba.getId()));
	}
	
	@PostMapping("/getStorageFee")
	public Result<Page<InventoryStorageFee>> getStorageFeeAction(@RequestBody ProfitSettingDTO dto)  {
		LambdaQueryWrapper<InventoryStorageFee> query = new LambdaQueryWrapper<InventoryStorageFee>();
		if(StrUtil.isNotBlank(dto.getCountry())) {
			query.like(InventoryStorageFee::getCountry,"%"+ dto.getCountry()+"%");
		}
		Page<InventoryStorageFee> list = iInventoryStorageFeeService.page(dto.getPage(),query);
		return Result.success(list);
	}
	
	@PostMapping("/saveStorageFee")
	public Result<?> saveStorageFeeAction(@RequestBody InventoryStorageFee fba)  {
		return Result.success(iInventoryStorageFeeService.save(fba));
	}
	
	@PostMapping("/updateStorageFee")
	public Result<?> updateStorageFeeAction(@RequestBody InventoryStorageFee fba)  {
		return Result.success(iInventoryStorageFeeService.updateById(fba));
	}
	
	@PostMapping("/deleteStorageFee")
	public Result<?> deleteStorageFeeAction(@RequestBody InventoryStorageFee fba)  {
		return Result.success(iInventoryStorageFeeService.removeById(fba.getId()));
	}
	
	 
	@PostMapping("/getInplace")
	public Result<Page<InplaceFee>> getInplaceAction(@RequestBody ProfitSettingDTO dto)  {
		LambdaQueryWrapper<InplaceFee> query = new LambdaQueryWrapper<InplaceFee>();
		Page<InplaceFee> list = iInplaceFeeService.page(dto.getPage(),query);
		return Result.success(list);
	}
	
	@PostMapping("/getInplaceList")
	public Result<List<InplaceFee>> getInplaceListAction(@RequestBody ProfitSettingDTO dto)  {
		LambdaQueryWrapper<InplaceFee> query = new LambdaQueryWrapper<InplaceFee>();
		List<InplaceFee> list = iInplaceFeeService.list(query);
		return Result.success(list);
	}
	
	@PostMapping("/saveInplace")
	public Result<?> saveInplaceAction(@RequestBody InplaceFee fba)  {
		return Result.success(iInplaceFeeService.save(fba));
	}
	
	@PostMapping("/updateInplace")
	public Result<?> updateInplaceAction(@RequestBody InplaceFee fba)  {
		return Result.success(iInplaceFeeService.updateById(fba));
	}
	
	@PostMapping("/deleteInplace")
	public Result<?> deleteInplaceAction(@RequestBody InplaceFee fba)  {
		return Result.success(iInplaceFeeService.removeById(fba.getId()));
	}
	 
	 
	@PostMapping("/getInplaceFeeFormat")
	public Result<Page<InplaceFeeFormat>> getInplaceFeeFormatAction(@RequestBody ProfitSettingDTO dto)  {
		LambdaQueryWrapper<InplaceFeeFormat> query = new LambdaQueryWrapper<InplaceFeeFormat>();
		if(StrUtil.isNotBlank(dto.getCountry())) {
			query.eq(InplaceFeeFormat::getCountry, dto.getCountry());
		}
		if(StrUtil.isNotBlank(dto.getTierid())) {
			query.eq(InplaceFeeFormat::getProducttierId, dto.getTierid());
		}
		if(StrUtil.isNotBlank(dto.getDispatchtype())) {
			query.eq(InplaceFeeFormat::getInplacefeeid, dto.getDispatchtype());
		}
		Page<InplaceFeeFormat> list = inplaceFeeFormatService.page(dto.getPage(),query);
		return Result.success(list);
	}
	
	@PostMapping("/saveInplaceFeeFormat")
	public Result<?> saveInplaceFeeFormatAction(@RequestBody InplaceFeeFormat fba)  {
		return Result.success(inplaceFeeFormatService.save(fba));
	}
	
	@PostMapping("/updateInplaceFeeFormat")
	public Result<?> updateInplaceFeeFormatAction(@RequestBody InplaceFeeFormat fba)  {
		return Result.success(inplaceFeeFormatService.updateById(fba));
	}
	
	@PostMapping("/deleteInplaceFeeFormat")
	public Result<?> deleteInplaceFeeFormatAction(@RequestBody InplaceFeeFormat fba)  {
		return Result.success(inplaceFeeFormatService.removeById(fba.getId()));
	}

	
	 
	@PostMapping("/getVariableClosing")
	public Result<Page<InplaceFeeFormat>> getVariableClosingAction(@RequestBody ProfitSettingDTO dto)  {
		LambdaQueryWrapper<InplaceFeeFormat> query = new LambdaQueryWrapper<InplaceFeeFormat>();
		Page<InplaceFeeFormat> list = inplaceFeeFormatService.page(dto.getPage(),query);
		return Result.success(list);
	}
	
	@PostMapping("/saveVariableClosing")
	public Result<?> saveVariableClosingAction(@RequestBody InplaceFeeFormat fba)  {
		return Result.success(inplaceFeeFormatService.save(fba));
	}
	
	@PostMapping("/updateVariableClosing")
	public Result<?> updateVariableClosingAction(@RequestBody InplaceFeeFormat fba)  {
		return Result.success(inplaceFeeFormatService.updateById(fba));
	}
	
	@PostMapping("/deleteVariableClosing")
	public Result<?> deleteVariableClosingAction(@RequestBody InplaceFeeFormat fba)  {
		return Result.success(inplaceFeeFormatService.removeById(fba.getId()));
	}
	 
	 
	@PostMapping("/getFBASipp")
	public Result<Page<FBASipp>> getFBASippAction(@RequestBody ProfitSettingDTO dto)  {
		LambdaQueryWrapper<FBASipp> query = new LambdaQueryWrapper<FBASipp>();
		if(StrUtil.isNotBlank(dto.getCountry())) {
			query.eq(FBASipp::getCountry, dto.getCountry());
		}
		if(StrUtil.isNotBlank(dto.getTierid())) {
			query.eq(FBASipp::getProducttierid, dto.getTierid());
		}
		if(dto.getIsclothing()!=null) {
			if(dto.getIsclothing()==false) {
				query.and(i->i.isNull(FBASipp::getIsclothing).or().eq(FBASipp::getIsclothing, dto.getIsclothing()));
				 
			}else {
				query.eq(FBASipp::getIsclothing, dto.getIsclothing());
			}
		}
		Page<FBASipp> list = iFBASippService.page(dto.getPage(),query);
		return Result.success(list);
	}
	
	@PostMapping("/saveFBASipp")
	public Result<?> saveFBASippAction(@RequestBody FBASipp fba)  {
		return Result.success(iFBASippService.save(fba));
	}
	
	@PostMapping("/updateFBASipp")
	public Result<?> updateFBASippAction(@RequestBody FBASipp fba)  {
		return Result.success(iFBASippService.updateById(fba));
	}
	
	@PostMapping("/deleteFBASipp")
	public Result<?> deleteFBASippAction(@RequestBody FBASipp fba)  {
		return Result.success(iFBASippService.removeById(fba.getId()));
	}
	 
}
