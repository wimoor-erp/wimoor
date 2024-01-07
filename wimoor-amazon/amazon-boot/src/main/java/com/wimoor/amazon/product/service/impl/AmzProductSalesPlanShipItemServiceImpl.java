package com.wimoor.amazon.product.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.api.ErpClientOneFeignManager;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.product.mapper.AmzProductSalesPlanShipItemMapper;
import com.wimoor.amazon.product.pojo.dto.PlanDTO;
import com.wimoor.amazon.product.pojo.dto.PlanDetailDTO;
import com.wimoor.amazon.product.pojo.entity.AmzProductSalesPlanShipItem;
import com.wimoor.amazon.product.service.IAmzProductSalesPlanShipItemService;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import feign.FeignException;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2022-12-10
 */
@Service
@RequiredArgsConstructor
public class AmzProductSalesPlanShipItemServiceImpl extends ServiceImpl<AmzProductSalesPlanShipItemMapper, AmzProductSalesPlanShipItem> implements IAmzProductSalesPlanShipItemService {
    final ErpClientOneFeignManager erpClientOneFeign;
    final IMarketplaceService iMarketplaceService;
	public Map<String,Object> getSummary(String shopid,String groupid,String warehouseid){
    	return this.baseMapper.getSummary(shopid, groupid,warehouseid);
    }

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getList(String shopid, String groupid, String warehouseid,String batchnumber) {
		LambdaQueryWrapper<AmzProductSalesPlanShipItem> query=new LambdaQueryWrapper<AmzProductSalesPlanShipItem>();
		query.eq(AmzProductSalesPlanShipItem::getShopid, shopid);
		query.eq(AmzProductSalesPlanShipItem::getWarehouseid, warehouseid);
		query.eq(AmzProductSalesPlanShipItem::getGroupid, groupid);
		query.gt(AmzProductSalesPlanShipItem::getAmount, 0);
		if(StrUtil.isNotBlank(batchnumber)) {
			query.eq(AmzProductSalesPlanShipItem::getBatchnumber, batchnumber);
		}
		List<AmzProductSalesPlanShipItem> list = this.baseMapper.selectList(query);
		PlanDTO dto=new PlanDTO();
		List<String> skulist=new ArrayList<String>();
		dto.setWarehouseid(warehouseid);
		dto.setShopid(shopid);
		dto.setGroupid(groupid);
		dto.setPlantype("ship");
		Map<String, Marketplace> marketMap = iMarketplaceService.findMapByMarketplaceId();
		for(AmzProductSalesPlanShipItem item:list) {
			skulist.add(item.getMsku());
		}
		try {
			dto.setMskulist(skulist);
			Map<String,Map<String,Object>> map=new HashMap<String,Map<String,Object>>();
			if(skulist.size()==0) {
				List<Map<String,Object>> resultdata=new ArrayList<Map<String,Object>>();
		    	return resultdata;
			}
		    Result<List<Map<String, Object>>> result=erpClientOneFeign.getMskuInventory(dto);
		    if(Result.isSuccess(result)&&result.getData()!=null) {
		    	List<Map<String, Object>> data = result.getData();
		    	for(Map<String, Object> item:data) {
		    		map.put(item.get("sku").toString(), item);
		    	}
		    	Map<String,Map<String, Object>> summaryMap=new HashMap<String,Map<String, Object>>();
		    	for(AmzProductSalesPlanShipItem item:list) {
					 String  key=item.getMarketplaceid();
//					 BigDecimal price=new BigDecimal("0");
					 BigDecimal boxcm3=new BigDecimal("0");
					 Integer boxnum=1;
					 BigDecimal pkgcm3=new BigDecimal("0");
					 BigDecimal pkgweight=new BigDecimal("0");
					 BigDecimal pkgvolume=new BigDecimal("0");
					 String sku=item.getSku();
					 String msku=item.getMsku();
					 Map<String, Object> mskuParam = map.get(msku);
					 if(mskuParam!=null) {
						 if(mskuParam.get("pkgcm3")!=null) {
							 pkgcm3=new BigDecimal(mskuParam.get("pkgcm3").toString());
						 }
//						 if(mskuParam.get("price")!=null) {
//							 price=new BigDecimal(mskuParam.get("price").toString());
//						 }
						 if(mskuParam.get("boxnum")!=null) {
							int num=  Integer.parseInt(mskuParam.get("boxnum").toString());
							if(num>0) {
								boxnum=num;
							}
						 }
						 if(mskuParam.get("pkgweight")!=null) {
							 pkgweight=new BigDecimal(mskuParam.get("pkgweight").toString());
						 }
						 if(mskuParam.get("pkgcm3")!=null) {
							 pkgcm3=new BigDecimal(mskuParam.get("pkgcm3").toString());
							 if(pkgcm3.floatValue()>0.0000001) {
								 pkgvolume=pkgcm3.divide(new BigDecimal(5000));
							 }
						 }
						 if(mskuParam.get("boxcm3")!=null) {
							 boxcm3=new BigDecimal(mskuParam.get("boxcm3").toString());
							 if(boxcm3.equals(new BigDecimal("0.0"))) {
								 boxcm3=pkgcm3;
							 }
						 }else {
							 boxcm3=pkgcm3;
						 }
						 mskuParam.put("boxcm3", boxcm3);
						 mskuParam.put("boxnum", boxnum);
						 mskuParam.put("pkgweight", pkgweight);
						 mskuParam.put("pkgvolume", pkgvolume);
						 mskuParam.put("pkgcm3", pkgcm3);
					 }else {
						 throw new BizException(item.getSku()+"本地SKU【"+msku+"】无法找到");
					 }
					 if(item.getTranstype()!=null) {
						 key=key+item.getTranstype();
					 }else {
						 key=key+"#";
					 }
					 if(item.getOverseaid()!=null) {
						 key=key+item.getOverseaid();
					 }else {
						 key=key+"#";
					 }
					 Map<String, Object> summary = summaryMap.get(key);
					 if(summary==null) {
						  summary = new HashMap<String,Object>();
						  summary.put("key", key);
						  summary.put("marketplaceid", item.getMarketplaceid());
						  if(item.getMarketplaceid().equals("EU")) {
							 summary.put("marketname", "FBA-EU-欧洲");
						  }else {
							  Marketplace market = marketMap.get(item.getMarketplaceid());
							  summary.put("marketname", "FBA-"+market.getMarket()+"-"+market.getName());
						  }
						  summary.put("transtype", item.getTranstype());
						  summary.put("overseaid", item.getOverseaid());
						  summaryMap.put(key, summary);
					 }
					 
//					 Object sumNum = summary.get("sumNum");
//					 if(sumNum==null) {
//						 summary.put("sumNum",1);
//					 }else {
//						 summary.put("sumNum",Integer.parseInt(sumNum.toString())+1);
//					 }
//					 setSummary(summary,"sumAmount",new BigDecimal("1"),item.getAmount());
//					 setSummary(summary,"sumPrice",price,item.getAmount());
//					 setSummary(summary,"sumBoxcm3",boxcm3,item.getAmount()/boxnum);
//					 setSummary(summary,"sumWeight",pkgweight,item.getAmount());
//					 setSummary(summary,"sumVolume",pkgvolume,item.getAmount());
					 List<Map<String,Object>> skuMaplist=null;
					 if(summary.get("list")!=null) {
						 skuMaplist=(List<Map<String, Object>>) summary.get("list");
					 }else {
						 skuMaplist=new ArrayList<Map<String,Object>>();
						 summary.put("list",skuMaplist);
					 }
					 Map<String, Object> skumap = BeanUtil.beanToMap(item);
					 if(mskuParam!=null) {
						 skumap.putAll(mskuParam);
						 skumap.put("sku", sku);
						 skumap.put("msku", msku);
						 skumap.put("materialid",skumap.get("id"));
						 skumap.put("id", item.getId());
					 }
					 skuMaplist.add(skumap);
					 
				}
		    	List<Map<String,Object>> resultdata=new ArrayList<Map<String,Object>>();
		    	for(Entry<String, Map<String, Object>> entry:summaryMap.entrySet()) {
		    		resultdata.add(entry.getValue());
		    	}
		    	return resultdata;
		    }
		}catch(FeignException e) {
			e.printStackTrace();
          	throw new BizException(BizException.getMessage(e, "本地产品信息处理异常请联系管理员"));
		}
		return null;
	}
	
 
  public int updateBatch( String id, String batchnumber) {
	  return this.baseMapper.updateBatch(id, batchnumber);
  }
  public int moveBatch(String shopid,String batchnumber) {
	  return this.baseMapper.moveBatch(shopid,batchnumber);
  }
  
  public List<Map<String, Object>> getPlanedItem(PlanDTO dto) {
	return   this.baseMapper.getPlanedItem(dto);
  }
  
  public List<Map<String,Object>> hasplanItem(PlanDetailDTO dto){
	  return this.baseMapper.hasplanItem(dto);
  }
  
  public List<Map<String,Object>> hasplanItemEu(PlanDetailDTO dto){
	  return this.baseMapper.hasplanItemEu(dto);
  }
 
}
