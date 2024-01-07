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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.api.AdminClientOneFeignManager;
import com.wimoor.amazon.api.ErpClientOneFeignManager;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
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
import com.wimoor.amazon.product.pojo.dto.PlanDetailDTO;
import com.wimoor.amazon.product.pojo.entity.AmzProductSalesPlan;
import com.wimoor.amazon.product.pojo.entity.AmzProductSalesPlanShipItem;
import com.wimoor.amazon.product.pojo.entity.ProductInAftersale;
import com.wimoor.amazon.product.pojo.entity.ProductInPresale;
import com.wimoor.amazon.product.service.IAmzProductSalesPlanService;
import com.wimoor.amazon.product.service.IAmzProductSalesPlanShipItemService;
import com.wimoor.amazon.product.service.IProductInAftersaleService;
import com.wimoor.amazon.product.service.IProductInPresaleService;
import com.wimoor.amazon.util.AmzDateUtils;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;

import cn.hutool.core.bean.BeanUtil;
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
    final ErpClientOneFeignManager erpClientOneFeign;
    final IProductInAftersaleService iProductInAftersaleService;
    final ShipInboundPlanMapper shipInboundPlanMapper;
    final IProductInPresaleService iProductInPresaleService;
	@Resource
	IAmazonAuthorityService amazonAuthorityService;

    final ProductInOrderMapper productInOrderMapper;
    final IMarketplaceService marketplaceService;
    @Resource
    IDaysalesFormulaService daysalesFormulaService;
    final IAmazonAuthorityService iAmazonAuthorityService;
    final IAmazonGroupService iAmazonGroupService;
    final IShipInboundPlanService shipInboundPlanService;
    final IShipInboundItemService iShipInboundItemService;
    String distribution="EFN";
	final AdminClientOneFeignManager adminClientOneFeign;
	@Autowired
	@Lazy
	IAmzProductSalesPlanShipItemService iAmzProductSalesPlanShipItemService;
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
			Map<String, Integer> recordmap,Integer fulfillable,
			Integer minCycle,Map<String, ProductInPresale> prelist) {
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
			if(deliveryCycle+shipcycle-i>=0) {
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
		 iProductInAftersaleService.insertBatch(salesafter);
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
		 LambdaQueryWrapper<AmzProductSalesPlan> query=new LambdaQueryWrapper<AmzProductSalesPlan>();
		 query.eq(AmzProductSalesPlan::getSku, sku);
		 query.eq(AmzProductSalesPlan::getMarketplaceid, marketplaceid);
		 query.eq(AmzProductSalesPlan::getAmazonauthid, auth.getId());
		 AmzProductSalesPlan old = this.getOne(query);
		 if(old!=null) {
			 this.update(plan, query);
		 }else {
			 this.save(plan);
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
		Calendar c=Calendar.getInstance();
		c.add(Calendar.DATE, -3);
		LambdaQueryWrapper<AmzProductSalesPlan> query=new LambdaQueryWrapper<AmzProductSalesPlan>();
		if(!StrUtil.isBlankOrUndefined(sku)) {
			  query.eq(AmzProductSalesPlan::getSku, sku);
		  }
		if(market.getRegion().equals("EU")) {
			query.eq(AmzProductSalesPlan::getMarketplaceid, "EU");
		}else {
			query.eq(AmzProductSalesPlan::getMarketplaceid, market.getMarketplaceid());
		}
		
		query.eq(AmzProductSalesPlan::getAmazonauthid, auth.getId());
		query.lt(AmzProductSalesPlan::getOpttime, c.getTime());
		this.remove(query);
		if(market.getRegion().equals("EU")) {
			  list = productInOrderMapper.getProductEUSales(param);
		}else {
		      list = productInOrderMapper.getProductCountrySales(param);
		}
		Map<String,Integer> fulfillMap=new HashMap<String,Integer>();
		List<AmzInboundFbaCycle> cycleList=null;
		AmzInboundFbaCycle fbacycle=null;
		DaysalesFormula formula = null;
		Map<String, FBAShipCycle> cycleMap =null;
		Map<String,Integer> skuQty=null;
		for(Map<String, Object> item:list) {
			String psku=item.get("sku").toString();
			String marketplaceid=item.get("marketplaceid").toString();
			String msku=item.get("msku").toString();
			String avgsales="0";
			if(item.get("avgsales")!=null) {
				avgsales=item.get("avgsales").toString();
			}
			int summonth = item.get("summonth") == null ? 0 : Integer.parseInt(item.get("summonth").toString());
		    Map<String, ProductInPresale> prelist = iProductInPresaleService.getPresale(psku,marketplaceid,auth);
			if(summonth==0&&prelist.size()==0) { 
				 AmzProductSalesPlan plan=new AmzProductSalesPlan();
				 plan.setAmazonauthid(new BigInteger(auth.getId()));
				 plan.setDeliveryCycle(null);
				 plan.setGroupid(auth.getGroupid());
				 plan.setMarketplaceid(marketplaceid);
				 plan.setMsku(msku);
				 plan.setNeedpurchase(0);
			     plan.setAvgsales(0);
				 plan.setNeedship(0);
				 plan.setShipMinCycleSale(0);
			     plan.setAvgsales(0); 
				 plan.setSalesday(0);
				 plan.setOpttime(new Date());
				 plan.setSku(psku);
				 plan.setShipday(0);
				 plan.setNeedshipfba(0);
				 plan.setShopid(new BigInteger(auth.getShopId()));
				 plan.setShortTime(null); 
				 LambdaQueryWrapper<AmzProductSalesPlan> updateQuery=new LambdaQueryWrapper<AmzProductSalesPlan>();
				 updateQuery.eq(AmzProductSalesPlan::getSku, psku);
				 updateQuery.eq(AmzProductSalesPlan::getMarketplaceid, marketplaceid);
				 updateQuery.eq(AmzProductSalesPlan::getAmazonauthid, auth.getId());
				 updateQuery.eq(AmzProductSalesPlan::getShopid, auth.getShopId());
				 this.saveOrUpdate(plan, updateQuery);
				 continue;
			}
			
			if(cycleList==null) {
				 cycleList=iAmzInboundFbaCycleService.getInboundFbaCycle(auth.getShopId(),market.getMarketplaceid());
			}
		    if(fbacycle==null) {
		    	fbacycle=iAmzInboundFbaCycleService.getDefaultInboundFbaCycle(cycleList);
		    }
		    if(formula==null) {
		    	formula = daysalesFormulaService.selectByShopid(auth.getShopId());
		    }
		    if(cycleMap==null) {
		    	cycleMap = iFBAShipCycleService.getFbaShipCycle(auth.getGroupid(),market.getMarketplaceid());
		    }
		    if(marketplaceid.equals("EU")) {
				int sumseven = item.get("sumseven") == null ? 0 : Integer.parseInt(item.get("sumseven").toString());// 七日销量
				int sum15 = item.get("sum15") == null ? 0 : Integer.parseInt(item.get("sum15").toString());
				Date openDate = item.get("openDate") == null ? null : AmzDateUtils.getDate(item.get("openDate"));
				Integer qty = DaysalesFormulaServiceImpl.getAvgSales(formula , summonth, sumseven, sum15, openDate);
				avgsales=qty.toString();
			}
		    if(skuQty==null) {
		    	skuQty=getSkuInventry(auth, market.getMarketplaceid(),fulfillMap,sku);
		    }
		    LambdaQueryWrapper<ProductInAftersale> queryDeleteAfter=new LambdaQueryWrapper<ProductInAftersale>();
		    queryDeleteAfter.eq(ProductInAftersale::getGroupid, auth.getGroupid());
		    queryDeleteAfter.eq(ProductInAftersale::getAmazonauthid, auth.getId());
		    queryDeleteAfter.eq(ProductInAftersale::getMarketplaceid,market.getMarketplaceid());
		    queryDeleteAfter.eq(ProductInAftersale::getSku,psku);
			iProductInAftersaleService.remove(queryDeleteAfter);
			
			LambdaQueryWrapper<AmzProductSalesPlan> queryDeletePlan=new LambdaQueryWrapper<AmzProductSalesPlan>();
			queryDeletePlan.eq(AmzProductSalesPlan::getMarketplaceid,market.getMarketplaceid());
			queryDeletePlan.eq(AmzProductSalesPlan::getSku,psku);
			queryDeletePlan.eq(AmzProductSalesPlan::getGroupid, auth.getGroupid());
		    queryDeletePlan.eq(AmzProductSalesPlan::getAmazonauthid, auth.getId());
			this.baseMapper.delete(queryDeletePlan);
			
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
				Result<Map<String, Object>> materialResult = erpClientOneFeign.getMSkuDeliveryAndInv(auth.getShopId(), auth.getGroupid(), msku, market.getMarket(),"true");
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
			try {
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
						mincycle,prelist);
			}catch(Exception e) {
				e.printStackTrace();
				//此处需要收录异常，后续全局异常开发收录异常逻辑
				
			}
			
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
	        	  Marketplace euobj=new Marketplace();
	        	  BeanUtil.copyProperties(eu, euobj);
	        	  euobj.setMarketplaceid("EU");
	        	  euobj.setMarket("EU");
	        	  handlePresaleMarket(auth, euobj,null) ;
	          }
	 
	}
 
 
	@CacheEvict(value = {"PlanModel" }, allEntries = true)
	public void refreshData(String groupid,String marketplaceid,String sku) {
		Marketplace market =null;
		if(marketplaceid.equals("EU")) {
			market =new Marketplace();
			market.setMarketplaceid("EU");
			market.setMarket("EU");
			market.setRegion("EU");
		} else {
			Marketplace catchmarket = marketplaceService.findMapByMarketplaceId().get(marketplaceid);
			if(catchmarket.getRegion().equals("EU")) {
				market =new Marketplace();
				BeanUtil.copyProperties(catchmarket, market);
				market.setMarketplaceid("EU");
				market.setMarket("EU");
				market.setRegion("EU");
			}else {
				market=catchmarket;
			}
		}
		AmazonAuthority auth=iAmazonAuthorityService.selectByGroupAndMarket(groupid,marketplaceid);
		handlePresaleMarket( auth, market ,  sku);
	}
	@CacheEvict(value = {"PlanModel" }, allEntries = true)
	public void refreshData(String groupid) {
			  	List<AmazonAuthority> list =iAmazonAuthorityService.lambdaQuery()
			  			.eq(AmazonAuthority::getGroupid, groupid)
			  			.eq(AmazonAuthority::getDisable, Boolean.FALSE).list();
			  	for(AmazonAuthority auth:list) {
			  		try {
						handlePresale(auth);
					}catch(Exception e) {
						e.printStackTrace();
					}
			  	}
	}
	@CacheEvict(value = {"PlanModel" }, allEntries = true)
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
		//List<Runnable> runnables = new ArrayList<Runnable>();
		for(String shopid:shopset) {
		QueryWrapper<AmazonAuthority> authqueryWrapper=new QueryWrapper<AmazonAuthority>();
		authqueryWrapper.eq("shop_id", shopid);
		List<AmazonAuthority> authList = iAmazonAuthorityService.list(authqueryWrapper);
		if(authList!=null && authList.size()>0) {
			for(AmazonAuthority auth:authList) {
				try {
					handlePresale(auth);
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		 }
		}
	}

	public  void handleTags(PlanDTO dto, IPage<Map<String, Object>> page) {
		if(page!=null&&page.getRecords()!=null&&page.getRecords().size()>0) {
    		List<String> skulist=new ArrayList<String>();
    		for(Map<String, Object> item:page.getRecords()) {
    			 try{
    				 if(item.get("sku")!=null) {
    					 skulist.add(item.get("sku").toString());
    				 }
				 }catch(Exception e) {
					 e.printStackTrace();
				 }
    		 }
    		try{
	    		 Result<Map<String, String>> skutaglist = erpClientOneFeign.getTagsIdsListByMsku(dto.getShopid(), skulist);
				 if(Result.isSuccess(skutaglist)&&skutaglist.getData()!=null) {
						Result<Map<String,Object>> tagnamelistResult=adminClientOneFeign.findTagsName(dto.getShopid());
						if(Result.isSuccess(tagnamelistResult)&&tagnamelistResult.getData()!=null) {
							Map<String, Object> tagsNameMap = tagnamelistResult.getData();
							Map<String,String> mskuTagsIdsMap=skutaglist.getData();
							for(Map<String, Object> record:page.getRecords()) {
								 List<String> tags=new ArrayList<String>();
								     String tagsids = mskuTagsIdsMap.get(record.get("sku").toString());
									if(tagsids!=null) {
										tags.addAll(Arrays.asList(tagsids.split(",")));
									}
									if(tags.size()>0) {
										List<Map<String, Object>> tagNameList = new ArrayList<Map<String,Object>>();
										for(String id:tags) {
											if(StrUtil.isNotBlank(id)) {
												Object tagobj = tagsNameMap.get(id);
												if(tagobj!=null) {
													tagNameList.add(BeanUtil.beanToMap(tagobj));
												}
											}
										}
										if(tagNameList.size()>0) {
											record.put("tagNameList",tagNameList);
										}
									}
								 
				    		 }
						}
				 }
    		 }catch(Exception e) {
				 e.printStackTrace();
			 }
		}
	}
    

public List<Map<String, Object>> handlePurchase(PlanDTO dto, List<Map<String, Object>> list) {
    	if(list!=null&&list.size()>0) {
    		List<String> mskulist=new LinkedList<String>();
    		Map<String,Map<String,Object>> mskuMap=new HashMap<String,Map<String,Object>>();
    		for(Map<String, Object> item:list) {
    			 try{
					 Object mskuobj = item.get("msku");
					 String msku=mskuobj!=null?mskuobj.toString():null;
					 if(msku!=null) {
						 mskulist.add(msku);
						 String newmsku = msku.toLowerCase();
						 mskuMap.put(newmsku, item);
					 }
				 }catch(Exception e) {
					 e.printStackTrace();
				 }
    		 }
    		 if(mskulist.size()>0) {
    			 dto.setMskulist(mskulist);
    			 List<Map<String, Object>> result=new LinkedList<Map<String, Object>>();
    			 Result<List<Map<String,Object>>> feignResult= erpClientOneFeign.getMskuInventory(dto);
				 if(Result.isSuccess(feignResult)&&feignResult.getData()!=null) {
					 for(Map<String, Object> item:feignResult.getData()) {
						   Map<String, Object> skuinfo = mskuMap.get(item.get("sku").toString().toLowerCase());
						   if(skuinfo!=null) {
							   Object id=item.get("id");
							   Object warehouseid=item.get("warehouseid");
							   if(item.get("amount")!=null) {
								   skuinfo.put("amount",item.get("amount"));
							   }
							   if(item.get("reallyamount")!=null) {
								   skuinfo.put("reallyamount",item.get("reallyamount"));
							   }
							   if(item.get("quantity")!=null) {
								   skuinfo.put("quantity",item.get("quantity"));
							   }
							   item.putAll(skuinfo);
							   item.put("id",id);
							   item.put("warehouseid",warehouseid);
							   result.add(item);
						   }
					 }
				 }
				 return result;
             }
    	}
    	return list;
     }

    public void setPurchaseRecord(List<Map<String,Object>> record) {
    	  if(record!=null&&record.size()>0) {
    		  List<String> list=new LinkedList<String>();
          	for(Map<String, Object> item:record) {
          		list.add(item.get("id").toString());
       	     }
          	 Result<?> resultLast = erpClientOneFeign.getLastRecordsAction(list);
 			   if(Result.isSuccess(resultLast)&&resultLast.getData()!=null) {
 				   @SuppressWarnings("unchecked")
				   Map<String,Object> data=(Map<String, Object>) resultLast.getData();
 					for(Map<String, Object> item:record) {
 		          		String id=item.get("id").toString();
 		          		item.put("last",data.get(id));
 		       	     }
 			   } 
          }
    }
    public List<Map<String, Object>> handleShip(PlanDTO dto, List<Map<String, Object>> list) {
   	     String warehouseid=dto.getWarehouseid();
		 List<String> mskulist=new LinkedList<String>();
		
		 Map<String,Map<String,Object>> mskusale=new HashMap<String,Map<String,Object>>();
		 Map<String,Map<String,Object>> mskuplaneditem=new HashMap<String,Map<String,Object>>();
		 List<Map<String, Object>> listplanitem = iAmzProductSalesPlanShipItemService.getPlanedItem(dto) ;
		 if(listplanitem!=null&&listplanitem.size()>0) {
			 for(Map<String, Object> item:listplanitem) {
				 mskuplaneditem.put(item.get("vmsku").toString(), item);
			 }
		 }
			 for(Map<String, Object> item:list) {
				 Object msku = item.get("msku");
				 if(msku!=null) {
					 Map<String, Object> planitem = mskuplaneditem.get(msku);
					 if(planitem!=null) {
						 item.putAll(planitem);
					 }
					 if (dto.getSelected()==true && dto.getPlantype().equals("ship")) {
						 if(planitem!=null&&planitem.get("amount")!=null&&Integer.parseInt(planitem.get("amount").toString())>0) {
							 mskusale.put(msku.toString(),item);
							 mskulist.add(msku.toString());
						 }
					 }else {
						 mskusale.put(msku.toString(),item);
						 mskulist.add(msku.toString());
					 }
				 }
				 item.put("warehouseid", warehouseid);
			 }
			 
		 try {
			 dto.setMskulist(mskulist);
			 List<Map<String,Object>> result=new LinkedList<Map<String,Object>>();
			 if(mskulist.size()>0&&dto.getPlantype().equals("ship")) {
				 dto.setSelected(false);
				 dto.setPlanitem(listplanitem);
				 Result<List<Map<String,Object>>> feignResult= erpClientOneFeign.getMskuInventory(dto);
				 if(Result.isSuccess(feignResult)&&feignResult.getData()!=null) {
					 for(Map<String, Object> item:feignResult.getData()) {
						 if(dto.getIscheck()!=null) {
								 result.add(item);
						 }else {
							 Object id =item.get("id");
							 String msku= item.get("sku").toString();
							 if(item!=null&&mskusale.get(msku)!=null) {
								 item.putAll(mskusale.get(msku));
							 }
							 Object quantityObj=item.get("quantity");
							 Object canAssemblyObj=item.get("canAssembly");
							 int quantity=0;
							 if(quantityObj!=null) {
								 quantity=Integer.parseInt(quantityObj.toString());
							 }
							 if(canAssemblyObj!=null) {
								 quantity=quantity+Integer.parseInt(canAssemblyObj.toString());
							 }
							 item.put("allquantity", quantity);
							 item.put("warehouseid", warehouseid);
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
	
    @Override
	public List<Map<String, Object>> getPlanModel(PlanDTO dto) {
		// TODO Auto-generated method stub
		return this.baseMapper.getPlanModel(dto);
	}

	@Override
	public IPage<Map<String, Object>> getPlanModel(Page<Object> page, PlanDTO dto) {
		// TODO Auto-generated method stub
		return this.baseMapper.getPlanModel(page,dto);
	}
	
public void setShipRecord(Map<String,Object> item,String shopid,String groupid,String marketplaceid,String sku,int puOnDay) {
		List<Map<String, Object>> shipRecord = shipInboundPlanService.getShipRecord(shopid,groupid, marketplaceid, sku);
		if (shipRecord != null && shipRecord.size() > 0) {
			Map<String, Object> ship=shipRecord.get(0);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String nowday = GeneralUtil.getStrDate(new Date());
			String shiprecord = GeneralUtil.formatDate(AmzDateUtils.getDate(ship.get("createdate").toString()), sdf);
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
   
    public void handItem(Map<String, Object> item,Map<String,Map<String,Object>> planmap,PlanDetailDTO dto) {
	    String psku=item.get("sku").toString();
	    String marketplaceid=item.get("marketplaceid").toString();
	    String amazonauthid =item.get("amazonauthid").toString();
	    String groupid=item.get("groupid").toString();
	    String msku=item.get("msku").toString();
	    String key=groupid+amazonauthid+marketplaceid+psku+msku;
	    Map<String, Object> plansku = planmap.get(key);
	    if(plansku!=null) {
	    	item.put("subnum",plansku.get("subnum"));
	    	item.put("aftersalesday",plansku.get("aftersalesday"));
	    	item.put("amount",plansku.get("amount"));
	    	item.put("reallyamount",plansku.get("amount"));
	    } else {
	    	item.put("subnum",0);
	    	item.put("aftersalesday",0);
	    	item.put("amount",item.get("amount"));
	    }
		List<AmzInboundFbaCycle> cycleList = iAmzInboundFbaCycleService.getInboundFbaCycle(dto.getShopid(),marketplaceid);
		AmzInboundFbaCycle myfbacycle =iAmzInboundFbaCycleService.getDefaultInboundFbaCycle(cycleList);
		item.put("defaultTranstype", myfbacycle.getTranstype());
		FBAShipCycle cycle = iFBAShipCycleService.getFbaShipCycle(groupid,marketplaceid,psku);
	    if(cycle!=null&&cycle.getTranstype()!=null) {
        	myfbacycle=iAmzInboundFbaCycleService.getTransInboundFbaCycle(cycleList,cycle.getTranstype());
        }
	    if(dto.getPlantype().equals("ship")&&dto.getIseu()) {
	     Integer inbound = iShipInboundItemService.summaryShipmentSku(groupid, marketplaceid, psku);
	    	if(inbound!=null) {
	    		if(item.get("quantity")==null) {
	    			item.put("oldquantity",  0);
	    			item.put("localinbound",  inbound);
	    			item.put("fbainbound",  0);
	    			item.put("quantity", inbound);
	    			item.put("inbounddiff", inbound);
	    		}else {
	    			Integer quantity= Integer.parseInt(item.get("quantity").toString());
	    			Integer myqty=item.get("localquantity")!=null?Integer.parseInt(item.get("localquantity").toString()):0;
	    			item.put("oldquantity",  quantity);
	    			item.put("localinbound",  inbound);
	    			if(dto.getIseu()==true) {
	    				item.put("fbainbound",  0);
	    				item.put("quantity", quantity+inbound);
	    				item.put("inbounddiff",inbound);
	    				item.put("localquantity",quantity);
	    			}else {
	    				item.put("fbainbound",  quantity-myqty);
	    				item.put("quantity", inbound+myqty);
	    				item.put("inbounddiff", quantity-myqty-inbound);
	    			}
	    		}
	    	}
	    }
		Integer shipcycle=getShipCycle(cycle,myfbacycle);
		Integer mincycle=getMinShipCycle(cycle,myfbacycle);
		item.put("shipday", shipcycle);
		item.put("warehouseid", dto.getWarehouseid());
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
			DaysalesFormula formula = daysalesFormulaService.selectByShopid(dto.getShopid());
			sysavgsales = DaysalesFormulaServiceImpl.getAvgSales(formula , summonth, sumseven, sum15, openDate);
		}
		 String country="EU";
		 if(!marketplaceid.equals("EU")) {
			 Marketplace market = marketplaceService.findMapByMarketplaceId().get(marketplaceid);
			 if(market!=null) {
				 country=market.getMarket();
			 }
			if(market!=null&&item.get("asin")!=null) {
				item.put("url", "https://www."+market.getPointName()+"/dp/"+item.get("asin").toString()+"?th=1&psc=1");
			}
		 }
		Integer overseaqty=0;
		try {
			Result<Map<String, Object>> materialResult = erpClientOneFeign.getMSkuDeliveryAndInv(dto.getShopid(), groupid, msku, country,"false");
		    if(Result.isSuccess(materialResult)&&materialResult.getData()!=null) {
		    	if(materialResult.getData().get("overseaqty")!=null) {
		    		overseaqty=Integer.valueOf(materialResult.getData().get("overseaqty").toString());
		    	}
		    }
		}catch(FeignException e) {
			 e.printStackTrace();
		}
		if(dto.getPlantype().equals("purchase")) {
			Integer needpurchase=item.get("needpurchase")==null?0:Integer.parseInt(item.get("needpurchase").toString());
			if(needpurchase>dto.getAmount()) {
				reallyamount=dto.getAmount();
				dto.setAmount(0);
			}else {
				reallyamount=needpurchase;
				dto.setAmount(dto.getAmount()-needpurchase);
			}
		}
		if(dto.getPlantype().equals("purchase")) {
			item.putAll(this.getAfterSales(psku, marketplaceid,groupid, sysavgsales, fbaquantity, overseaqty, reallyamount)); 
		}else {
			int aftersalesday=item.get("aftersalesday")==null?0:Integer.parseInt(item.get("aftersalesday").toString());
			int salesday=item.get("salesday")==null?0:Integer.parseInt(item.get("salesday").toString());
			if(salesday>aftersalesday) {
				item.put("aftersalesday",salesday);
			}
		}
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
		if(dto.getPlantype().equals("ship")) {
			setShipRecord(item,dto.getShopid(),groupid,marketplaceid,psku,myfbacycle.getPutOnDays());
		}
    }
	@Override
	public List<Map<String,Object>> ExpandCountryDataByGroup(PlanDetailDTO dto) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> result = null;
		Map<String,Map<String,Object>> planmap=new HashMap<String,Map<String,Object>>();
		if(dto.getIseu()==null || dto.getIseu()==false) {
			result=this.baseMapper.ExpandCountryDataByGroup(dto);
			List<Map<String, Object>> planitem = this.iAmzProductSalesPlanShipItemService.hasplanItem(dto);
			for(Map<String, Object> item:planitem) {
				String key=item.get("groupid").toString()+item.get("amazonauthid").toString()+item.get("marketplaceid").toString()+item.get("sku").toString()+item.get("msku").toString();
				planmap.put(key, item);
			}
		}else {
			result=this.baseMapper.ExpandEUCountryDataByGroup(dto);
			List<Map<String, Object>> planitem = this.iAmzProductSalesPlanShipItemService.hasplanItemEu(dto);
			for(Map<String, Object> item:planitem) {
				String key=item.get("groupid").toString()+item.get("amazonauthid").toString()+item.get("marketplaceid").toString()+item.get("sku").toString()+item.get("msku").toString();
				planmap.put(key, item);
			}
		}
		if(dto.getAmount()==null) {
			dto.setAmount(0);
		}
		for(Map<String, Object> item:result) {
			handItem(item,planmap,dto);
		}
		return result;
	}
  
	
	public Map<String,List<Map<String,Object>>> ExpandCountrysDataByGroup(PlanDetailDTO dto) {
		// TODO Auto-generated method stub
		Map<String,List<Map<String,Object>>> result= new HashMap<String,List<Map<String,Object>>>();
		Map<String,Map<String,Object>> planmap=new HashMap<String,Map<String,Object>>();
		List<Map<String, Object>> countryresult =null;
		if(dto.getIseu()==null || dto.getIseu()==false) {
			countryresult= this.baseMapper.ExpandCountryDataByGroup(dto);
			List<Map<String, Object>> planitem = this.iAmzProductSalesPlanShipItemService.hasplanItem(dto);
			for(Map<String, Object> item:planitem) {
				String key=item.get("groupid").toString()+item.get("amazonauthid").toString()+item.get("marketplaceid").toString()+item.get("sku").toString()+item.get("msku").toString();
				planmap.put(key, item);
			}
		}
		if(dto.getAmount()==null) {
			dto.setAmount(0);
		}
		for(Map<String, Object> item:countryresult) {
			String msku=item.get("msku").toString();
			handItem(item,planmap,dto);
			List<Map<String, Object>> itemlist = result.get(msku);
			if(itemlist==null) {
				itemlist=new LinkedList<Map<String,Object>>();
			}
			itemlist.add(item);
			result.put(msku,itemlist);
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
			String avgsalesstr="0";
			if(salesitem.get("avgsales")!=null) {
				avgsalesstr=salesitem.get("avgsales").toString();
			}
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
			Result<Map<String, Object>> materialResult = erpClientOneFeign.getMSkuDeliveryAndInv(auth.getShopId(), auth.getGroupid(), item.getMsku(), country,"false");
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
