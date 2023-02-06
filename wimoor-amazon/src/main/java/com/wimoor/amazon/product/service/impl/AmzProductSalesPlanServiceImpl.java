package com.wimoor.amazon.product.service.impl;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.api.ErpClientOneFeign;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.AmazonGroup;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IAmazonGroupService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.common.pojo.entity.DaysalesFormula;
import com.wimoor.amazon.common.service.IDaysalesFormulaService;
import com.wimoor.amazon.common.service.impl.DaysalesFormulaServiceImpl;
import com.wimoor.amazon.inbound.mapper.ShipInboundPlanMapper;
import com.wimoor.amazon.inbound.pojo.entity.AmzInboundFbaCycle;
import com.wimoor.amazon.inbound.pojo.entity.FBAShipCycle;
import com.wimoor.amazon.inbound.service.IAmzInboundFbaCycleService;
import com.wimoor.amazon.inbound.service.IFBAShipCycleService;
import com.wimoor.amazon.inbound.service.IShipInboundItemService;
import com.wimoor.amazon.inbound.service.IShipInboundPlanService;
import com.wimoor.amazon.inventory.mapper.InventoryReportMapper;
import com.wimoor.amazon.product.mapper.AmzProductSalesPlanMapper;
import com.wimoor.amazon.product.mapper.ProductInOrderMapper;
import com.wimoor.amazon.product.pojo.dto.PlanDTO;
import com.wimoor.amazon.product.pojo.entity.AmzProductSalesPlan;
import com.wimoor.amazon.product.pojo.entity.AmzProductSalesPlanShipItem;
import com.wimoor.amazon.product.pojo.entity.ProductInAftersale;
import com.wimoor.amazon.product.pojo.entity.ProductInPresale;
import com.wimoor.amazon.product.service.IAmzProductSalesPlanService;
import com.wimoor.amazon.product.service.IProductInAftersaleService;
import com.wimoor.amazon.product.service.IProductInPresaleService;
import com.wimoor.amazon.product.service.IProductInfoService;
import com.wimoor.amazon.util.AmzDateUtils;
import com.wimoor.amazon.util.UUIDUtil;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;

import cn.hutool.core.util.StrUtil;
import feign.FeignException;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 *  服务实现类 计算 
 *  第一步：遍历站点与店铺
 *  第二步：准备销售数据，准备库存数据，准备周期
 *  第三步：准备海外仓数据
 *  第四步：计算
 *  
 * </p>
 *
 * @author wimoor team
 * @since 2022-11-28
 */
@Service
@RequiredArgsConstructor
public class AmzProductSalesPlanServiceImpl extends ServiceImpl<AmzProductSalesPlanMapper, AmzProductSalesPlan> implements IAmzProductSalesPlanService {
    final IFBAShipCycleService iFBAShipCycleService;
    final IAmzInboundFbaCycleService iAmzInboundFbaCycleService;
    final InventoryReportMapper inventoryReportMapper;
    final ErpClientOneFeign erpClientOneFeign;
    final IProductInAftersaleService iProductInAftersaleService;
    final ShipInboundPlanMapper shipInboundPlanMapper;
    final IProductInPresaleService iProductInPresaleService;
	@Resource
	IAmazonAuthorityService amazonAuthorityService;
    final IProductInfoService iProductInfoService;
    final ProductInOrderMapper productInOrderMapper;
    final IMarketplaceService marketplaceService;
    @Resource
    IDaysalesFormulaService daysalesFormulaService;
    final IAmazonAuthorityService iAmazonAuthorityService;
    final IAmazonGroupService iAmazonGroupService;
    final IShipInboundPlanService shipInboundPlanService;
    final IShipInboundItemService iShipInboundItemService;
    String distribution="EFN";
	
/**
 *  * 核心方法-------------------------
 * 处理未来180的销量
 * @param auth 授权
 * @param market 站点
 * @param sku 平台SKU
 * @param msku 本地SKU
 * @param marketplaceid 对应仓库
 * @param avgsales 平均销量
 * @param fbaInventoryQty FBA仓库库存
 * @param shipcycle 备货周期
 * @param deliveryCycle 供货周期
 * @param overseaqty 海外仓库存
 * @param recordmap 发货记录
 * @param fulfillable fba可用库存不含待入库
 */
public void handlePresaleSKU(AmazonAuthority auth,Marketplace market,
			String sku,String msku,String marketplaceid,
			Integer avgsales,Integer fbaInventoryQty,Integer shipcycle,
			Integer deliveryCycle,Integer overseaqty,
			Map<String, Integer> recordmap,Integer fulfillable,Integer minCycle) {
		    Map<String, ProductInPresale> prelist = iProductInPresaleService.getPresale(sku,marketplaceid,auth);
		    Calendar c=Calendar.getInstance();
		    Calendar cship=Calendar.getInstance();
		    int needpurchase=0;
		    int needship=0;
		    int needshipfba=0;
		    int totalqty=overseaqty+fbaInventoryQty;
		    int totalpurchaseinv=overseaqty+fbaInventoryQty;
		    int sales=0;
		    int minCycleSale=0;
		    Integer salesday=0;
		    int sumavgsale=0;
		    Date shorttime=null;
		    List<ProductInAftersale> salesafter=new LinkedList<ProductInAftersale>();
		    for(int i=1;i<=180||i<=(deliveryCycle+shipcycle+minCycle);i++) {
			   ProductInPresale old = prelist.get(GeneralUtil.formatDate(c.getTime()));
			if(old==null||old.getQuantity()==null) {
				sales=avgsales;
			} else {
				sales=old.getQuantity();
			}
			if(deliveryCycle+shipcycle-i>0) {
				totalpurchaseinv=totalpurchaseinv-sales;
				if(totalpurchaseinv<0) {
					needpurchase=totalpurchaseinv*-1;
				}
			}
			if(i>shipcycle&&i<=shipcycle+minCycle) {
				minCycleSale=minCycleSale+sales;
			}
			String daykey=GeneralUtil.formatDate(c.getTime());
			if(recordmap.get(daykey)!=null) {
				fulfillable=fulfillable+recordmap.get(daykey);
			}
			if(fulfillable-sales<0&&shorttime==null) {
				shorttime=c.getTime();
			}
			fulfillable=fulfillable-sales;
			if(totalqty>0) {
				salesday++;
			}
			if(shipcycle-i>=0) {
				totalqty=totalqty-sales;
				sumavgsale=sumavgsale+sales;
				fbaInventoryQty=fbaInventoryQty-sales;
				if(totalqty<0) {
					needship=totalqty*-1;
				}
				if(fbaInventoryQty<0) {
					needshipfba=fbaInventoryQty*-1;
				}
			}else {
				if(totalqty-sales<=0) {
					int needqty=0;
					if(totalqty>0) {
						needqty=sales-totalqty;
						totalqty=totalqty-sales;
					}else {
						needqty=sales;
					}
					if(needqty>0) {
						ProductInAftersale after=new ProductInAftersale();
						after.setAmazonauthid(new BigInteger(auth.getId()));
						after.setGroupid(new BigInteger(auth.getGroupid()));
						after.setMarketplaceid(marketplaceid);
						after.setDate(c.getTime());
						after.setSku(sku);
						after.setQuantity(needqty);
						after.setId(UUIDUtil.getUUIDshort());
						if(needqty>0) {
							salesafter.add(after);
						}
					}
					cship.add(Calendar.DATE, 1);
				}else {
					totalqty=totalqty-sales;	
				}
			}
			c.add(Calendar.DATE, 1);
		}
		 iProductInAftersaleService.saveBatch(salesafter);
		 AmzProductSalesPlan plan=new AmzProductSalesPlan();
		 plan.setAmazonauthid(new BigInteger(auth.getId()));
		 plan.setDeliveryCycle(deliveryCycle);
		 plan.setGroupid(auth.getGroupid());
		 plan.setMarketplaceid(marketplaceid);
		 plan.setMsku(msku);
		 plan.setNeedpurchase(needpurchase);
	     plan.setAvgsales(avgsales);
		 if(needship>0) {
			 plan.setNeedship(needship+minCycleSale);
			 plan.setShipMinCycleSale(minCycleSale);
		 }else {
			 plan.setNeedship(0);
			 plan.setShipMinCycleSale(0);
		 }
		 if(shipcycle>0) {
			 sumavgsale=sumavgsale/shipcycle;
			 plan.setAvgsales(sumavgsale); 
		 }
		 plan.setSalesday(salesday);
		 plan.setOpttime(new Date());
		 plan.setSku(sku);
		 plan.setShipday(shipcycle);
		 plan.setNeedshipfba(needshipfba);
		 plan.setShopid(new BigInteger(auth.getShopId()));
		 plan.setShortTime(AmzDateUtils.getLocalDate(shorttime)); 
		 LambdaQueryWrapper<AmzProductSalesPlan> updateQuery=new LambdaQueryWrapper<AmzProductSalesPlan>();
		 updateQuery.eq(AmzProductSalesPlan::getSku, sku);
		 updateQuery.eq(AmzProductSalesPlan::getMsku, msku);
		 updateQuery.eq(AmzProductSalesPlan::getMarketplaceid, marketplaceid);
		 updateQuery.eq(AmzProductSalesPlan::getAmazonauthid, auth.getId());
		 updateQuery.eq(AmzProductSalesPlan::getShopid, auth.getShopId());
		 if(plan.getNeedpurchase()>0||plan.getNeedship()>0||plan.getNeedshipfba()>0||plan.getSalesday()>0) {
			 this.saveOrUpdate(plan, updateQuery);
		 }
}

	/**
	 * 获取商品对应站点的备货周期，当没有时使用对应站点配置上的。
	 * @param auth
	 * @param marketplaceid
	 * @param sku
	 * @param fbacycle
	 * @return
	 */
	public Integer getShipCycle(FBAShipCycle cycle,AmzInboundFbaCycle fbacycle) {
		//获取产品自身设置的备货周期
	    Integer shipcycle=0;
    	if(cycle!=null&&cycle.getStockingcycle()!=null) {
    		shipcycle=cycle.getStockingcycle();
    	}else if(fbacycle!=null&&fbacycle.getStockingCycle()!=null) {
    		shipcycle=fbacycle.getStockingCycle();
    	}
		if(fbacycle!=null) {
			shipcycle=shipcycle+fbacycle.getPutOnDays()+fbacycle.getFirstLegDays();
		}
		return shipcycle;
	}
	
	public Integer getMinShipCycle(FBAShipCycle cycle,AmzInboundFbaCycle fbacycle) {
		//获取产品自身设置的备货周期
	    Integer mincycle=0;
    	if(cycle!=null&&cycle.getMinCycle()!=null) {
    		mincycle=cycle.getMinCycle();
    	}else if(fbacycle!=null&&fbacycle.getMinCycle()!=null) {
    		mincycle=fbacycle.getMinCycle();
    	}
		return mincycle;
	}
	
	/**
	 * 获取站点下面的FBA库存
	 * @param auth
	 * @param marketplaceid
	 * @return
	 */
	public Map<String,Integer> getSkuInventry(AmazonAuthority auth,String marketplaceid,Map<String,Integer> fulfillMap,String sku){
		List<Map<String, Object>> listqty=inventoryReportMapper.findFBAWithoutUnsellable(auth.getId(), marketplaceid,sku);
		HashMap<String,Integer> result=new HashMap<String,Integer>();
		for(Map<String, Object> item:listqty) {
			if(item.get("quantity")!=null) {
				result.put(item.get("sku").toString(),Integer.valueOf(item.get("quantity").toString()));
			}
			if(item.get("fulfillable")!=null&&fulfillMap!=null) {
				fulfillMap.put(item.get("sku").toString(),Integer.valueOf(item.get("fulfillable").toString()));
			}
		}
		return result;
	}
	

	/**
	 * 获取商品的发货记录
	 * @param auth
	 * @param marketplaceid
	 * @param sku
	 * @return
	 */
	public Map<String,Integer> getShipRecord(AmazonAuthority auth,String marketplaceid,String sku){
		List<Map<String, Object>> shipRecord = shipInboundPlanMapper.getShipArrivalTimeRecord(auth.getShopId(), marketplaceid, sku);
		Map<String,Integer> map=new HashMap<String,Integer>();
		for(Map<String, Object> item:shipRecord) {
			Date date=AmzDateUtils.getDate(item.get("arrivalTime"));
			Integer shipQuantity=Integer.parseInt(item.get("shipQuantity").toString());
			map.put(GeneralUtil.formatDate(date), shipQuantity);
		}
		return map;
	}
	
	/**
	 * 准备计算的数据
	 * @param auth
	 * @param market
	 * @param distribution
	 * @param sku
	 */
	public void handlePresaleMarket(AmazonAuthority auth,Marketplace market ,String sku) {
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("marketplaceid", market.getMarketplaceid());
		param.put("amazonauthid",auth.getId());
		  if(sku!=null) {
			  param.put("sku",sku);
	    }else {
	    	  param.put("sku",null);
	    }
		List<Map<String, Object>> list =null;
		if(market.getRegion().equals("EU")) {
			  list = productInOrderMapper.getProductEUSales(param);
		}else {
		      list = productInOrderMapper.getProductCountrySales(param);
		}
		Map<String,Integer> fulfillMap=new HashMap<String,Integer>();
		Map<String,Integer> skuQty=getSkuInventry(auth, market.getMarketplaceid(),fulfillMap,sku);
		List<AmzInboundFbaCycle> cycleList=iAmzInboundFbaCycleService.getInboundFbaCycle(auth.getShopId(),market.getMarketplaceid());
		AmzInboundFbaCycle fbacycle=iAmzInboundFbaCycleService.getDefaultInboundFbaCycle(cycleList);
	    LambdaQueryWrapper<ProductInAftersale> queryDeleteAfter=new LambdaQueryWrapper<ProductInAftersale>();
	    queryDeleteAfter.eq(ProductInAftersale::getGroupid, auth.getGroupid());
	    queryDeleteAfter.eq(ProductInAftersale::getAmazonauthid, auth.getId());
	    queryDeleteAfter.eq(ProductInAftersale::getMarketplaceid,market.getMarketplaceid());
	    if(sku!=null) {
	    	  queryDeleteAfter.eq(ProductInAftersale::getSku,sku);
	    }
		iProductInAftersaleService.remove(queryDeleteAfter);
		LambdaQueryWrapper<AmzProductSalesPlan> queryDeletePlan=new LambdaQueryWrapper<AmzProductSalesPlan>();
		queryDeletePlan.eq(AmzProductSalesPlan::getAmazonauthid, auth.getId());
		queryDeletePlan.eq(AmzProductSalesPlan::getMarketplaceid,market.getMarketplaceid());
		if(sku!=null) {
			queryDeletePlan.eq(AmzProductSalesPlan::getSku,sku);
	    }
		this.remove(queryDeletePlan);
		Set<String> skuSet=new HashSet<String>();
		DaysalesFormula formula = daysalesFormulaService.selectByShopid(auth.getShopId());
		Map<String, FBAShipCycle> cycleMap = iFBAShipCycleService.getFbaShipCycle(auth.getGroupid(),market.getMarketplaceid());
		for(Map<String, Object> item:list) {
			String psku=item.get("sku").toString();
			skuSet.add(psku);
			String marketplaceid=item.get("marketplaceid").toString();
			String msku=item.get("msku").toString();
			String avgsales=item.get("avgsales").toString();
			if(marketplaceid.equals("EU")) {
				int sumseven = item.get("sumseven") == null ? 0 : Integer.parseInt(item.get("sumseven").toString());// 七日销量
				int summonth = item.get("summonth") == null ? 0 : Integer.parseInt(item.get("summonth").toString());
				int sum15 = item.get("sum15") == null ? 0 : Integer.parseInt(item.get("sum15").toString());
				Date openDate = item.get("openDate") == null ? null : AmzDateUtils.getDate(item.get("openDate"));
				Integer qty = DaysalesFormulaServiceImpl.getAvgSales(formula , summonth, sumseven, sum15, openDate);
				avgsales=qty.toString();
			}
			Integer inventoryQty=0;
			Integer fulfillable=0;
			if(skuQty.containsKey(psku)) {
				inventoryQty=skuQty.get(psku);
			}
			if(fulfillMap.containsKey(psku)) {
				fulfillable=fulfillMap.get(psku);
			}
			Integer deliveryCycle=0;
			Integer overseaqty=0;
			try {
				Result<Map<String, Object>> materialResult = erpClientOneFeign.getMSkuDeliveryAndInv(auth.getShopId(), auth.getGroupid(), msku, market.getMarket());
			    if(Result.isSuccess(materialResult)&&materialResult.getData()!=null) {
			    	if(materialResult.getData().get("deliveryCycle")!=null) {
			    		deliveryCycle=Integer.valueOf(materialResult.getData().get("deliveryCycle").toString());
			    	}
			    	if(materialResult.getData().get("overseaqty")!=null) {
			    		overseaqty=Integer.valueOf(materialResult.getData().get("overseaqty").toString());
			    	}
			    }
			}catch(Exception e ) {
				e.printStackTrace();
			}
			Map<String, Integer> recordmap = getShipRecord(auth,marketplaceid,psku);
	        FBAShipCycle cycle = cycleMap.get(psku);
	        AmzInboundFbaCycle myfbacycle = fbacycle;
	        if(cycle!=null&&cycle.getTranstype()!=null) {
	        	myfbacycle=iAmzInboundFbaCycleService.getTransInboundFbaCycle(cycleList,cycle.getTranstype());
	        }
	            
			Integer shipcycle=getShipCycle(cycle,myfbacycle);
			Integer mincycle=getMinShipCycle(cycle,myfbacycle);
			handlePresaleSKU(auth,
					market,
					psku,
					msku,
					marketplaceid,
					Integer.parseInt(avgsales),
					inventoryQty,
					shipcycle,
					deliveryCycle,
					overseaqty,
					recordmap,
					fulfillable,
					mincycle);
		}
		List<ProductInPresale> prelist = iProductInPresaleService.selectByGroup(sku, market.getMarketplaceid(), auth.getGroupid());
		for(ProductInPresale  item:prelist) {
			String psku=item.getSku();
			if(skuSet.contains(psku)) {
				continue;
			}
			skuSet.add(psku);
			String marketplaceid= market.getMarketplaceid();
			String msku=iProductInfoService.findMSKUBySKUMarket(psku, market.getMarketplaceid(), auth.getId());
			String avgsales="0";
			Integer inventoryQty=0;
			Integer fulfillable=0;
			if(skuQty.containsKey(psku)) {
				inventoryQty=skuQty.get(psku);
			}
			if(fulfillMap.containsKey(psku)) {
				fulfillable=fulfillMap.get(psku);
			}
			Integer deliveryCycle=0;
			Integer overseaqty=0;
			try {
				Result<Map<String, Object>> materialResult = erpClientOneFeign.getMSkuDeliveryAndInv(auth.getShopId(), auth.getGroupid(), msku, market.getMarket());
			    if(Result.isSuccess(materialResult)&&materialResult.getData()!=null) {
			    	if(materialResult.getData().get("deliveryCycle")!=null) {
			    		deliveryCycle=Integer.valueOf(materialResult.getData().get("deliveryCycle").toString());
			    	}
			    	if(materialResult.getData().get("overseaqty")!=null) {
			    		overseaqty=Integer.valueOf(materialResult.getData().get("overseaqty").toString());
			    	}
			    }
			}catch(Exception e ) {
				e.printStackTrace();
			}
			Map<String, Integer> recordmap = getShipRecord(auth,marketplaceid,psku);
	        FBAShipCycle cycle = cycleMap.get(psku);
			Integer shipcycle=getShipCycle(cycle,fbacycle);
			Integer mincycle=getMinShipCycle(cycle,fbacycle);
			handlePresaleSKU(auth,
					market,
					psku,
					msku,
					marketplaceid,
					Integer.parseInt(avgsales),
					inventoryQty,
					shipcycle,
					deliveryCycle,
					overseaqty,
					recordmap,
					fulfillable,
					mincycle);
		}
	}
	
 
 
	public void handlePresale(AmazonAuthority auth) {
			this.iProductInPresaleService.refreshData(auth);
			List<Marketplace> marketlist = marketplaceService.findbyauth(auth.getId());
		    Marketplace eu=null;
	          for(Marketplace market:marketlist) {
		    		 if(market.getRegion().equals("EU")) {
		    			 eu=market;
		    			 continue;
		    		 }else {
		    			handlePresaleMarket(auth, market,null) ;
		    		 }
		    	 }
	          if(eu!=null) {
	        	  eu.setMarketplaceid("EU");
	        	  eu.setMarket("EU");
	        	  handlePresaleMarket(auth, eu,null) ;
	          }
	 
	}
 
	public Runnable handlePresaleThread(AmazonAuthority auth) {
		return new Runnable() {
			public void run() {
				try {
					handlePresale(auth);
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		};
	}
	public void refreshData(String groupid,String marketplaceid,String sku) {
		Marketplace market =null;
		if(marketplaceid.equals("EU")) {
			market =new Marketplace();
			market.setMarketplaceid("EU");
			market.setMarket("EU");
			market.setRegion("EU");
		} else {
			market = marketplaceService.findMapByMarketplaceId().get(marketplaceid);
		}
		AmazonAuthority auth=iAmazonAuthorityService.selectByGroupAndMarket(groupid,marketplaceid);
		handlePresaleMarket( auth, market ,  sku);
	}
	public void refreshData(String groupid) {
		      AmazonGroup group = iAmazonGroupService.getById(groupid);
			  	List<AmazonAuthority> list =iAmazonAuthorityService.selectByGroupId(group.getId());
			  	for(AmazonAuthority auth:list) {
			  		 handlePresaleThread(auth);
			  	}
	}
	
	public void refreshData(Set<String> shopset) {
		//productInfoReviewService.updateRunsAsin();
		if(shopset==null||shopset.size()==0) {
			shopset=new HashSet<String>();
			QueryWrapper<AmazonAuthority> queryWrapper=new QueryWrapper<AmazonAuthority>();
			queryWrapper.eq("disable", false);
			List<AmazonAuthority> list=amazonAuthorityService.list(queryWrapper);
			for (AmazonAuthority auth : list) {
				shopset.add(auth.getShopId());
			}
		}
		List<Runnable> runnables = new ArrayList<Runnable>();
		for(String shopid:shopset) {
		QueryWrapper<AmazonAuthority> authqueryWrapper=new QueryWrapper<AmazonAuthority>();
		authqueryWrapper.eq("shop_id", shopid);
		List<AmazonAuthority> authList = iAmazonAuthorityService.list(authqueryWrapper);
		if(authList!=null && authList.size()>0) {
			for(AmazonAuthority auth:authList) {
				runnables.add(handlePresaleThread(auth));
			}
		 }
		}
		amazonAuthorityService.executThread(runnables, "anlyway");
	}

 
	@Override
	public List<Map<String, Object>> getPlanModel(PlanDTO dto) {
		// TODO Auto-generated method stub
		 String warehouseid=dto.getWarehouseid();
		 if(StrUtil.isAllBlank(dto.getSearch())) {
			 dto.setSearch(null);
		 }else {
			 dto.setSearch("%"+dto.getSearch().trim()+"%");
		 }
		 if(StrUtil.isAllBlank(dto.getMarketplaceid())) {
			 dto.setMarketplaceid(null);
		 }
		 if(dto.getTags()!=null&&dto.getTags().size()>0) {
			List<String> pidlist = iProductInfoService.getPidListByTagList(dto.getTags(),dto.getShopid(),null,dto.getGroupid(),null,dto.getMarketplaceid());
			dto.setPidlist(pidlist);
		 }
	     if(dto.getMarketplaceid()!=null&&dto.getMarketplaceid().equals("IEU")) {
			 dto.setMarketplaceid("EU");
		 }
		 if(StrUtil.isAllBlank(dto.getStatus())) {
			 dto.setStatus(null);
		 }
		 if(StrUtil.isAllBlank(dto.getCurrentRank())) {
			 dto.setCurrentRank(null);
		 }
		 if(StrUtil.isAllBlank(dto.getShortdays())) {
			 dto.setShortdays(null);
		 }
		 if(dto.getIscheck()!=null&&dto.getIscheck().equals("true")) {
			 dto.setSelected(true);
		 }else {
			 dto.setIscheck(null);
		 }
		 if(StrUtil.isAllBlank(dto.getSkuarray())) {
			 dto.setSkulist(null);
		 }else {
			 dto.setSkulist(Arrays.asList(dto.getSkuarray().split(",")));
		 }
		 
		 List<Map<String, Object>> list = this.baseMapper.getPlanModel(dto);
		 List<String> mskulist=new LinkedList<String>();
		 Map<String,Map<String,Object>> mskusale=new HashMap<String,Map<String,Object>>();
		 for(Map<String, Object> item:list) {
			 Object msku = item.get("msku");
			 if(msku!=null) {
				 mskusale.put(msku.toString(),item);
				 mskulist.add(msku.toString());
			 }
			 item.put("warehouseid", warehouseid);
		 }
		 try {
			 dto.setMskulist(mskulist);
			 List<Map<String,Object>> result=new LinkedList<Map<String,Object>>();
			 if(mskulist.size()>0) {
				 Result<List<Map<String,Object>>> feignResult= erpClientOneFeign.getMskuInventory(dto);
				 if(Result.isSuccess(feignResult)&&feignResult.getData()!=null) {
					 for(Map<String, Object> item:feignResult.getData()) {
						 Object id =item.get("id");
						 String msku= item.get("sku").toString();
						 Object warehouseidObj=item.get("warehouseid");
						 if(dto.getIscheck()!=null&&dto.getPlantype().equals("ship")) {
							 Object quantityObj=item.get("quantity");
							 Object canAssemblyObj=item.get("canAssembly");
							 int quantity=0;
							 int amount=0;
							 if(quantityObj!=null) {
								 quantity=Integer.parseInt(quantityObj.toString());
							 }
							 if(canAssemblyObj!=null) {
								 quantity=quantity+Integer.parseInt(canAssemblyObj.toString());
							 }
							 if(item!=null&&mskusale.get(msku)!=null) {
								 Map<String, Object> map = mskusale.get(msku);
								 Object amountObj=map.get("amount");
								 if(amountObj!=null) {
									 amount= Integer.parseInt(amountObj.toString());
								 }
								 if(amount>quantity) {
									 item.putAll(mskusale.get(msku));
									 item.put("id", id);
									 result.add(item);
								 }
							 }
						 }else {
							 if(item!=null&&mskusale.get(msku)!=null) {
								 item.putAll(mskusale.get(msku));
							 }
							 if(dto.getPlantype().equals("purchase")) {
								 item.put("warehouseid", warehouseidObj);
							 }else {
								 item.put("warehouseid", warehouseid);
							 }
							 item.put("id", id);
							 result.add(item);
						 }
					 }
				 }
			 }
			 return result;
		 }catch(Exception e) {
			 e.printStackTrace();
			 throw new BizException("获取本地产品失败");
		 }
	}

public void setShipRecord(Map<String,Object> item,String shopid,String marketplaceid,String sku,int puOnDay) {
		List<Map<String, Object>> shipRecord = shipInboundPlanService.getShipRecord(shopid, marketplaceid, sku);
		if (shipRecord != null && shipRecord.size() > 0) {
			Map<String, Object> ship=shipRecord.get(0);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String nowday = GeneralUtil.getStrDate(new Date());
			String shiprecord = GeneralUtil.formatDate(GeneralUtil.getDatez(ship.get("createdate").toString()), sdf);
			if (nowday.indexOf(shiprecord) >= 0) {
				shiprecord = "今日";
				item.put("dayship", "istoday");
			} else {
				item.put("dayship", "notoday");
			}
			item.put("shipRecordQuantity", ship.get("Quantity").toString());
			item.put("shipRecordStatusName", ship.get("statusName").toString());
			item.put("shipRecordStatus", ship.get("status").toString());
			item.put("shipRecordDay", shiprecord);// 开始只展示一条记录，点击记录会显示最近3条发货记录
			if (ship.get("arrivalTime") != null) {
				Calendar today = Calendar.getInstance();
				Calendar cal = today;
				cal.setTime(GeneralUtil.getDatez(ship.get("arrivalTime").toString()));
				cal.add(Calendar.DAY_OF_MONTH, puOnDay);// 预计上架时间
				double arrivalday = GeneralUtil.distanceOfDay(cal, today);
				item.put("shipRecordArrivalday", arrivalday);
			}
		}
   }
   
	@Override
	public List<Map<String,Object>> ExpandCountryDataByGroup(String shopid,String groupid,String warehouseid, String msku,String plantype,Boolean iseu,Integer amount) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> result = null;
		if(iseu==null || iseu==false) {
			result=this.baseMapper.ExpandCountryDataByGroup(shopid,groupid,warehouseid,msku,plantype);
		}else {
			result=this.baseMapper.ExpandEUCountryDataByGroup(shopid,groupid,warehouseid,msku,plantype);
		}
		if(amount==null) {
			amount=0;
		}
		for(Map<String, Object> item:result) {
			    String psku=item.get("sku").toString();
			    String marketplaceid=item.get("marketplaceid").toString();
			    groupid=item.get("groupid").toString();
				List<AmzInboundFbaCycle> cycleList = iAmzInboundFbaCycleService.getInboundFbaCycle(shopid,marketplaceid);
				AmzInboundFbaCycle myfbacycle =iAmzInboundFbaCycleService.getDefaultInboundFbaCycle(cycleList);
				item.put("defaultTranstype", myfbacycle.getTranstype());
				FBAShipCycle cycle = iFBAShipCycleService.getFbaShipCycle(groupid,marketplaceid,psku);
			    if(cycle!=null&&cycle.getTranstype()!=null) {
		        	myfbacycle=iAmzInboundFbaCycleService.getTransInboundFbaCycle(cycleList,cycle.getTranstype());
		        }
			    if(iseu!=null&&iseu==true) {
			    	Integer inbound = iShipInboundItemService.summaryShipmentSku(groupid, marketplaceid, psku);
			    	if(inbound!=null) {
			    		if(item.get("quantity")==null) {
			    			item.put("quantity", inbound);
			    		}else {
			    			Integer myqty=Integer.parseInt(item.get("quantity").toString());
			    			item.put("quantity", inbound+myqty);
			    		}
			    	}
			    }
				Integer shipcycle=getShipCycle(cycle,myfbacycle);
				Integer mincycle=getMinShipCycle(cycle,myfbacycle);
				item.put("shipday", shipcycle);
				item.put("warehouseid", warehouseid);
				item.put("mincycle", mincycle);
				myfbacycle.setMinCycle(mincycle);
				if(cycle!=null) {
					if(cycle.getStockingcycle()!=null) {
						myfbacycle.setStockingCycle(cycle.getStockingcycle());
					}
					if(cycle.getTranstype()!=null) {
						myfbacycle.setTranstype(cycle.getTranstype());
					}
				}
				Integer sysavgsales=item.get("sysavgsales") == null ? 0 : Integer.parseInt(item.get("sysavgsales").toString());
				Integer fbaquantity=item.get("quantity") == null ? 0 : Integer.parseInt(item.get("quantity").toString());
				Integer reallyamount=item.get("reallyamount") == null ? 0 : Integer.parseInt(item.get("reallyamount").toString());
				if(marketplaceid.equals("EU")) {
					int sumseven = item.get("sumseven") == null ? 0 : Integer.parseInt(item.get("sumseven").toString());// 七日销量
					int summonth = item.get("summonth") == null ? 0 : Integer.parseInt(item.get("summonth").toString());
					int sum15 = item.get("sum15") == null ? 0 : Integer.parseInt(item.get("sum15").toString());
					Date openDate = item.get("openDate") == null ? null : AmzDateUtils.getDate(item.get("openDate"));
					DaysalesFormula formula = daysalesFormulaService.selectByShopid(shopid);
					sysavgsales = DaysalesFormulaServiceImpl.getAvgSales(formula , summonth, sumseven, sum15, openDate);
				}
				 String country="EU";
				 if(!marketplaceid.equals("EU")) {
					 Marketplace market = marketplaceService.findMapByMarketplaceId().get(marketplaceid);
					 if(market!=null) {
						 country=market.getMarket();
					 }
				 }
				Integer overseaqty=0;
				try {
					Result<Map<String, Object>> materialResult = erpClientOneFeign.getMSkuDeliveryAndInv(shopid, groupid, msku, country);
				    if(Result.isSuccess(materialResult)&&materialResult.getData()!=null) {
				    	if(materialResult.getData().get("overseaqty")!=null) {
				    		overseaqty=Integer.valueOf(materialResult.getData().get("overseaqty").toString());
				    	}
				    }
				}catch(FeignException e) {
					 e.printStackTrace();
				}
				if(plantype.equals("purchase")) {
					Integer needpurchase=item.get("needpurchase")==null?0:Integer.parseInt(item.get("needpurchase").toString());
					if(needpurchase>amount) {
						reallyamount=amount;
						amount=0;
					}else {
						reallyamount=needpurchase;
						amount=amount-needpurchase;
					}
				}
				item.putAll(this.getAfterSales(psku, marketplaceid, groupid, sysavgsales, fbaquantity, overseaqty, reallyamount)); 
				item.put("overseaqty", overseaqty);
				item.put("sysavgsales", sysavgsales);
				item.put("cycle", myfbacycle);
				item.put("transtype", myfbacycle.getTranstype());
				if(cycle!=null&&cycle.getTranstype()!=null) {
					item.put("settranstype", cycle.getTranstype());
				}else {
					item.put("settranstype", "");
				}
				if(cycle!=null&&cycle.getStockingcycle()!=null) {
					item.put("setstockingcycles", cycle.getStockingcycle());
				}else {
					item.put("setstockingcycles", "");
				}
				item.put("statuscolor",GeneralUtil.getColorType(item.get("statuscolor")));
				setShipRecord(item,shopid,marketplaceid,psku,myfbacycle.getPutOnDays());
		}
		return result;
	}
  
	@Override
	public Integer getAfterSales(AmzProductSalesPlanShipItem item) {
		// TODO Auto-generated method stub
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("marketplaceid", item.getMarketplaceid());
		param.put("amazonauthid",item.getAmazonauthid());
	    param.put("sku",item.getSku());
		List<Map<String, Object>> list =null;
		if(item.getMarketplaceid().equals("EU")) {
			  list = productInOrderMapper.getProductEUSales(param);
		}else {
		      list = productInOrderMapper.getProductCountrySales(param);
		}
	   int avgsales=0;
		if(list!=null&&list.size()>0) {
			Map<String, Object> salesitem = list.get(0);
			String avgsalesstr=salesitem.get("avgsales").toString();
			if(item.getMarketplaceid().equals("EU")) {
				int sumseven = salesitem.get("sumseven") == null ? 0 : Integer.parseInt(salesitem.get("sumseven").toString());// 七日销量
				int summonth = salesitem.get("summonth") == null ? 0 : Integer.parseInt(salesitem.get("summonth").toString());
				int sum15 = salesitem.get("sum15") == null ? 0 : Integer.parseInt(salesitem.get("sum15").toString());
				Date openDate = salesitem.get("openDate") == null ? null : AmzDateUtils.getDate(salesitem.get("openDate"));
				DaysalesFormula formula = daysalesFormulaService.selectByShopid(item.getShopid().toString());
				Integer qty = DaysalesFormulaServiceImpl.getAvgSales(formula , summonth, sumseven, sum15, openDate);
				avgsalesstr=qty.toString();
			}
			if(StrUtil.isNotBlank(avgsalesstr)) {
				avgsales=Integer.valueOf(avgsalesstr);
			}
		}
		AmazonAuthority auth = iAmazonAuthorityService.getById(item.getAmazonauthid());
		Map<String,Integer> skuQty=getSkuInventry(auth, item.getMarketplaceid(),null,item.getSku());
		List<AmzInboundFbaCycle> cycleList = iAmzInboundFbaCycleService.getInboundFbaCycle(auth.getShopId(),item.getMarketplaceid());
		FBAShipCycle cycle = iFBAShipCycleService.getFbaShipCycle(item.getGroupid().toString(),item.getMarketplaceid(),item.getSku());
	    AmzInboundFbaCycle myfbacycle =iAmzInboundFbaCycleService.getDefaultInboundFbaCycle(cycleList);
        if(cycle!=null&&cycle.getTranstype()!=null) {
        	myfbacycle=iAmzInboundFbaCycleService.getTransInboundFbaCycle(cycleList,cycle.getTranstype());
        }
		Integer shipcycle=getShipCycle(cycle,myfbacycle);
		Integer mincycle=getMinShipCycle(cycle,myfbacycle);
	    Map<String, ProductInPresale> prelist = iProductInPresaleService.getPresale(item.getSku(),item.getMarketplaceid(),auth);
		int afterSales=0;
		Integer fbaInventoryQty=0;
		if(skuQty.containsKey(item.getSku())) {
			fbaInventoryQty=skuQty.get(item.getSku());
		}
		Integer overseaqty=0;
		try {
			String country="";
			if(item.getMarketplaceid().equals("EU")) {
				country="EU";
			}else {
				Marketplace market = marketplaceService.findMapByMarketplaceId().get(item.getMarketplaceid());
				if(market!=null) {
					country=market.getMarket();
				}
			}
			Result<Map<String, Object>> materialResult = erpClientOneFeign.getMSkuDeliveryAndInv(auth.getShopId(), auth.getGroupid(), item.getMsku(), country);
		    if(Result.isSuccess(materialResult)&&materialResult.getData()!=null) {
		    	if(materialResult.getData().get("overseaqty")!=null) {
		    		overseaqty=Integer.valueOf(materialResult.getData().get("overseaqty").toString());
		    	}
		    }
		}catch(Exception e ) {
			e.printStackTrace();
		}
		Calendar c=Calendar.getInstance();
		 int totalqty=overseaqty+fbaInventoryQty+item.getAmount();
		 for(int i=1;i<=180||i<=(shipcycle+mincycle);i++) {
			   ProductInPresale old = prelist.get(GeneralUtil.formatDate(c.getTime()));
			int sales;
			if(old==null) {
				sales=avgsales;
			} else {
				sales=old.getQuantity();
			}
			totalqty=totalqty-sales;
			if(totalqty>=0) {
				afterSales++;
			}else {
				break;
			}
		 }
		return afterSales;
	}

	public Map<String,Object> getAfterSales(String sku,String marketplaceid,String groupid,int avgsales,int fbaInventoryQty,Integer overseaqty,int amount) {
		// TODO Auto-generated method stub
	    Map<String, ProductInPresale> prelist = iProductInPresaleService.getPresale(sku,marketplaceid,groupid);
	    int afterSales=0;
	    int salesday=0;
		Calendar c=Calendar.getInstance();
		 int totalqty=overseaqty+fbaInventoryQty+amount;
		 int totalqtyinv=overseaqty+fbaInventoryQty;
		 for(int i=1;i<=180;i++) {
			ProductInPresale old = prelist.get(GeneralUtil.formatDate(c.getTime()));
			int sales;
			if(old==null) {
				sales=avgsales;
			} else {
				sales=old.getQuantity();
			}
			totalqty=totalqty-sales;
			totalqtyinv=totalqtyinv-sales;
			if(totalqtyinv>0) {
				salesday++;
			}
			if(totalqty>0) {
				afterSales++;
			}else {
				break;
			}
			c.add(Calendar.DATE, 1);
		 }
		Map<String,Object> result=new HashMap<String,Object>();
		result.put("salesday", salesday);
		result.put("aftersalesday", afterSales);
		return result;
	}
}
