package com.wimoor.erp.ship.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.erp.api.AmazonClientOneFeign;
import com.wimoor.erp.assembly.pojo.entity.AssemblyForm;
import com.wimoor.erp.assembly.service.IAssemblyFormService;
import com.wimoor.erp.purchase.mapper.PurchaseFormEntryMapper;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormEntry;
import com.wimoor.erp.purchase.pojo.entity.PurchaseWareHouseMaterial;
import com.wimoor.erp.purchase.service.IPurchaseFormService;
import com.wimoor.erp.purchase.service.IPurchaseWareHouseMaterialService;
import com.wimoor.erp.ship.mapper.ShipPlanItemMapper;
import com.wimoor.erp.ship.mapper.ShipPlanMapper;
import com.wimoor.erp.ship.mapper.ShipPlanSubMapper;
import com.wimoor.erp.ship.pojo.entity.ShipPlan;
import com.wimoor.erp.ship.pojo.entity.ShipPlanItem;
import com.wimoor.erp.ship.pojo.entity.ShipPlanSub;
import com.wimoor.erp.ship.pojo.entity.ShipPlanSubEuItem;
import com.wimoor.erp.ship.service.IShipPlanService;
import com.wimoor.erp.ship.service.IShipPlanSubEuItemService;
import com.wimoor.erp.ship.service.IShipPlanSubService;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;

@Service("shipPlanService")
@RequiredArgsConstructor
public class ShipPlanServiceImpl extends  ServiceImpl<ShipPlanMapper,ShipPlan> implements IShipPlanService {
     
	final IAssemblyFormService assemblyFormService;
	final IPurchaseFormService purchaseFormService;
	final PurchaseFormEntryMapper purchaseFormEntryMapper;
	final IPurchaseWareHouseMaterialService purchaseWareHouseMaterialService;
	final ShipPlanItemMapper shipPlanItemMapper;
	final ShipPlanSubMapper shipPlanSubMapper;
    final IShipPlanSubService shipPlanSubService;
    final IShipPlanSubEuItemService shipPlanSubEuItemService;
    final AmazonClientOneFeign amazonClientOneFeign;
	public List<Map<String, Object>> getSummaryPlan(String planid) {
		return shipPlanItemMapper.summaryPlan(planid);
	}
	
	public void afterShipInboundPlanSave(String planid,String planmarketplaceid, Boolean issplit, List<String> skulist) {
		QueryWrapper<ShipPlanSub> subQuery=new QueryWrapper<ShipPlanSub>();
		subQuery.eq("planid", planid);
		subQuery.eq("marketplaceid", planmarketplaceid);
		subQuery.eq("status", 1);
		ShipPlanSub plansub =shipPlanSubMapper.selectOne(subQuery);
		ShipPlan plan = this.baseMapper.selectById(plansub.getPlanid());
		String marketplaceid = plansub.getMarketplaceid();
		if ("EU".equals(planmarketplaceid)) {
			QueryWrapper<ShipPlanSubEuItem> query = new QueryWrapper<ShipPlanSubEuItem>();
			query.eq("plansubid", plansub.getId());
			if ("true".equals(issplit)) {
				query.eq("marketplaceid", marketplaceid);
				shipPlanSubEuItemService.remove(query);
				QueryWrapper<ShipPlanSubEuItem> query2 = new QueryWrapper<ShipPlanSubEuItem>();
				query2.eq("plansubid", plansub.getId());
				long count = shipPlanSubEuItemService.count(query2);
				if (count > 0) {
					return;
				}
			} else {
				shipPlanSubEuItemService.remove(query);
			}
		}
		afterShipPlanItemShiped(plansub,"fba",skulist);
		refreshPlanSummary(plan);
	}
	
	
	 public void afterShipPlanItemShiped(ShipPlanSub plansub,String operate,List<String> skulist){
		    QueryWrapper<ShipPlanItem> query=new QueryWrapper<ShipPlanItem>();
		    query.eq("plansubid",plansub.getId());
		    query.eq("status",1);
			List<ShipPlanItem> itemlist = shipPlanItemMapper.selectList(query);
			if(itemlist!=null && itemlist.size()>0) {
				boolean hasSelfAmount=false;
				boolean hasFbaAmount=false;
				Set<String> skuset=null;
				if(skulist!=null && skulist.size()>0) {
					skuset=new HashSet<String>();
					for(int i=0;i<skulist.size();i++) {
						String sku = skulist.get(i);
						skuset.add(sku+",");
					}
				} 
				for(int i=0;i<itemlist.size();i++) {
					ShipPlanItem item = itemlist.get(i);
					if(skuset!=null && !skuset.contains((item.getSku()+",")))continue;
					if(item!=null&&item.getSelfamount()!=null&&item.getSelfamount()>0) {
						hasSelfAmount=true;
					}
					if(item!=null&&item.getAmount()!=null&&item.getAmount()>0) {
						hasFbaAmount=true;
					}
					if("fba".equals(operate)) {
						item.setAmount(0);
						if(item.getSelfamount()==null||item.getSelfamount()==0) {
							item.setStatus(0);
						}
						shipPlanItemMapper.updateById(item);
						hasFbaAmount=false;
					}else {
						item.setSelfamount(0);
						if(item.getAmount()==null||item.getAmount()==0) {
							item.setStatus(0);
						}
						shipPlanItemMapper.updateById(item);
						hasSelfAmount=false;
					}
				}
				if(skulist==null || skuset==null) {
					if(hasSelfAmount==false&&hasFbaAmount==false) {
						plansub.setStatus(2);
						plansub.setOpttime(new Date());
						QueryWrapper<ShipPlanSub> queryUpdate=new QueryWrapper<ShipPlanSub>();
						queryUpdate.eq("marketplaceid",plansub.getMarketplaceid());
						queryUpdate.eq("status",1);
						queryUpdate.eq("planid", plansub.getPlanid());
						shipPlanSubService.update(plansub,queryUpdate);
						ShipPlanSub plansub2 = new ShipPlanSub();
						plansub2.setPlanid(plansub.getPlanid());
						plansub2.setMarketplaceid(plansub.getMarketplaceid());
						plansub2.setStatus(1);// 可用
						plansub2.setOpttime(new Date());
						shipPlanSubService.save(plansub2);
					}
				}else {
					//skulist去判断 当前plansub的数量=skulist的数量  就可以结束当前plansub  itemlist恒大于等于skulist
					if(skulist.size()==itemlist.size()) {
						if(hasSelfAmount==false&&hasFbaAmount==false) {
							plansub.setStatus(2);
							plansub.setOpttime(new Date());
							QueryWrapper<ShipPlanSub> queryUpdate=new QueryWrapper<ShipPlanSub>();
							queryUpdate.eq("marketplaceid",plansub.getMarketplaceid());
							queryUpdate.eq("status",1);
							queryUpdate.eq("planid", plansub.getPlanid());
							shipPlanSubService.update(plansub,queryUpdate);
							ShipPlanSub plansub2 = new ShipPlanSub();
							plansub2.setPlanid(plansub.getPlanid());
							plansub2.setMarketplaceid(plansub.getMarketplaceid());
							plansub2.setStatus(1);// 可用
							plansub2.setOpttime(new Date());
							shipPlanSubService.save(plansub2);
						}
					}
				}
			}
			
	    }
	 
	public void refreshPlanSummary(ShipPlan plan) {
		List<Map<String, Object>> listmap = getSummaryPlan(plan.getId());
		BigDecimal totalgoodsworth = new BigDecimal("0");
		BigDecimal totalweight = new BigDecimal("0");
		Integer totalamount = 0;
		Integer totalnum = 0;// SKU数量
		if (listmap != null && listmap.size() > 0) {
			for (int i = 0; i < listmap.size(); i++) {
				Map<String, Object> map = listmap.get(i);
				if(map==null) {
					continue;
				}
				Object amount = map.get("amount");
				Object goodsworth = map.get("goodsworth");
				Object weight = map.get("weight");
				Object  num=map.get("totalnum");
				if (amount != null) {
					totalamount += Integer.parseInt(amount.toString().trim());
				}
				if (goodsworth != null) {
					totalgoodsworth = totalgoodsworth.add(new BigDecimal(goodsworth.toString().trim()));
				}
				if (weight != null) {
					totalweight = totalweight.add(new BigDecimal(weight.toString().trim()));
				}
				if (num != null) {
					totalnum = totalnum+Integer.parseInt(num.toString().trim());
				}
			}
		}
		plan.setTotalamount(totalamount);
		plan.setTotalnum(totalnum);
		plan.setGoodsworth(totalgoodsworth);
		plan.setTotalweight(totalweight);
		super.updateById(plan);
	}
	
	@Override
	public IPage<Map<String, Object>> getPlan(Page<Object> page, Map<String, Object> param) {
		// TODO Auto-generated method stub
		IPage<Map<String, Object>> list = getSale(page,param);
		for (int i = 0; i < list.getRecords().size(); i++) {
			Map<String, Object> map1 = list.getRecords().get(i);
			 map1.put("lastorder", getLastForm(map1));
		}
		return list;
	}
	
	public String getLastForm(Map<String, Object> map) {
		Object id = map.get("id");
		Object issfg = map.get("issfg");
		String lastform = "";
		SimpleDateFormat sdf4 = new SimpleDateFormat("MM-dd");
		if ("1".equals(issfg.toString())) {// 组装成品
			AssemblyForm form = assemblyFormService.getLastOneFormByMaterial(id);
			if (form != null) {
				lastform = sdf4.format(form.getCreatedate());
				lastform = lastform + "  " + form.getAmount();
				lastform = lastform + "  <br> " + form.getAuditstatusName();
			}
		} else if ("0".equals(issfg.toString()) || "2".equals(issfg.toString())) {// 单独成品,半成品
			Map<String, Object> data = purchaseFormService.getLastOneFormByMaterial(id);
			if (data != null) {
				Object creatdate = data.get("createdate");
				if (creatdate != null) {
					lastform = sdf4.format((Date) creatdate);
				}
				lastform = lastform + "  " + data.get("amount");
				Object auditstatus = data.get("auditstatus");
				if (auditstatus != null) {
					String entryid = data.get("entryid").toString();
					PurchaseFormEntry entry = purchaseFormEntryMapper.selectById(entryid);
					lastform = lastform + " <br> " + PurchaseFormEntry.getAuditstatusName(entry);
				}
			}
		}
		return lastform;
	}
	
	public IPage<Map<String, Object>> getSale(Page<?> page,Map<String, Object> map) {
		Object sortparam = map.get("sortparam");
		Object orderparam = map.get("orderparam");
		Object defoutwarehouseid = map.get("defoutwarehouseid");
		String planid = map.get("planid").toString();
		if (sortparam != null && GeneralUtil.checkParamSql(sortparam.toString()) == false) {
			map.put("sortparam", null);
		}
		if (orderparam != null && GeneralUtil.checkParamSql(orderparam.toString()) == false) {
			map.put("orderparam", null);
		}
		if(defoutwarehouseid != null && StrUtil.isNotEmpty(defoutwarehouseid.toString())) {
			List<String> materialList = new ArrayList<String>();
			List<PurchaseWareHouseMaterial> list = purchaseWareHouseMaterialService.getPurchaseForPlanIdAndWareHouseId(planid, defoutwarehouseid.toString());
			if(list != null && list.size() > 0) {
				for(PurchaseWareHouseMaterial purchaseWareHouseMaterial : list) {
					materialList.add(purchaseWareHouseMaterial.getMaterialid());
				}
				map.put("materialList", materialList);
			} 
		}
		return this.baseMapper.getPlan(page,map);
	}


 
}
