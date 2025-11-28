package com.wimoor.erp.thirdparty.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.inventory.service.IInventoryService;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.order.service.IOrderPlanService;
import com.wimoor.erp.order.service.IOrderService;
import com.wimoor.erp.stock.pojo.entity.StockTaking;
import com.wimoor.erp.stock.pojo.entity.StockTakingItem;
import com.wimoor.erp.stock.pojo.entity.StocktakingWarehouse;
import com.wimoor.erp.stock.service.IStockTakingItemService;
import com.wimoor.erp.stock.service.IStockTakingService;
import com.wimoor.erp.thirdparty.mapper.ThirdPartyWarehouseBindMapper;
import com.wimoor.erp.thirdparty.mapper.ThirdPartyWarehouseMapper;
import com.wimoor.erp.thirdparty.pojo.dto.ThirdpartyWarehouseInvDTO;
import com.wimoor.erp.thirdparty.pojo.entity.ThirdPartyAPI;
import com.wimoor.erp.thirdparty.pojo.entity.ThirdPartyWarehouse;
import com.wimoor.erp.thirdparty.pojo.entity.ThirdPartyWarehouseBind;
import com.wimoor.erp.thirdparty.service.*;
import com.wimoor.erp.warehouse.pojo.entity.Warehouse;
import com.wimoor.erp.warehouse.service.IWarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;

@Service("thirdPartyWarehouseBindService")
@RequiredArgsConstructor
public class ThirdPartyWarehouseBindServiceImpl extends ServiceImpl<ThirdPartyWarehouseBindMapper, ThirdPartyWarehouseBind> implements IThirdPartyWarehouseBindService {
 final IWarehouseService warehouseService;
 final IInventoryService inventoryService;
 final IStockTakingService stockTakingService;
 final ISerialNumService serialNumService;
 final IStockTakingItemService stockTakingItemService;
 final IMaterialService materialService;
 final IOrderService iOrderService;
 final ThirdPartyWarehouseMapper thirdPartyWarehouseMapper;
 private final IOrderPlanService orderPlanService;
 @Autowired
 ThirdPartyWarehouseBindMapper thirdPartyWarehouseBindMapper;
 @Autowired
 @Lazy
 IThirdPartyAPIService iThirdPartyAPIService;

 @Autowired
 @Lazy
 IThirdPartyWarehouseService warehouseK5Service;

 @Autowired
 @Lazy
 IThirdPartyWarehouseService warehouseOPSService;

 @Autowired
 @Lazy
 IThirdPartyWarehouseService warehouseXLSService;

@Autowired
@Lazy
IThirdPartyWarehouseService warehouseYCService;
    @Override
    public List<Map<String, Object>> findByConditon(String shopid,ThirdPartyWarehouseBind dto) {
        Map<String,Object> param= BeanUtil.beanToMap(dto);
        param.put("shopid",shopid);
        return this.thirdPartyWarehouseBindMapper.findByConditon(param);
    }

    @Override
    public boolean saveBind(ThirdPartyWarehouseBind dto) {
        if(dto==null|| StrUtil.isBlankOrUndefined(dto.getLocalWarehouseId())||StrUtil.isBlankOrUndefined(dto.getThirdpartyWarehouseId())){
            throw new BizException("仓库不能为空");
        }
        LambdaQueryWrapper<ThirdPartyWarehouseBind> query=new LambdaQueryWrapper<ThirdPartyWarehouseBind>();
        query.eq(ThirdPartyWarehouseBind::getLocalWarehouseId,dto.getLocalWarehouseId());
        query.eq(ThirdPartyWarehouseBind::getThirdpartyWarehouseId,dto.getThirdpartyWarehouseId());
        ThirdPartyWarehouseBind one = this.baseMapper.selectOne(query);
        if(one==null){
            return this.baseMapper.insert(dto)>0;
        }else{
            return this.baseMapper.update(dto,query)>0;
        }
    }

    @Override
    public boolean deleteBind(ThirdPartyWarehouseBind dto) {
        LambdaQueryWrapper<ThirdPartyWarehouseBind> query=new LambdaQueryWrapper<ThirdPartyWarehouseBind>();
        query.eq(ThirdPartyWarehouseBind::getLocalWarehouseId,dto.getLocalWarehouseId());
        query.eq(ThirdPartyWarehouseBind::getThirdpartyWarehouseId,dto.getThirdpartyWarehouseId());
        return this.baseMapper.delete(query)>0;
    }


    @Override
    public boolean asyncLocalInventory(String shopid,String thirdpartyWarehouseid,Map<String,Integer> thirdPartyQty){
        LambdaQueryWrapper<ThirdPartyWarehouseBind> query=new LambdaQueryWrapper<ThirdPartyWarehouseBind>();
        query.eq(ThirdPartyWarehouseBind::getThirdpartyWarehouseId,thirdpartyWarehouseid);
        List<ThirdPartyWarehouseBind> list = this.baseMapper.selectList(query);
        if(list==null||list.size()==0){return false;}
        for(ThirdPartyWarehouseBind item:list) {
            try {
                String localWarehouseId = item.getLocalWarehouseId();
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("warehouseid", localWarehouseId);
                param.put("shopid", shopid);
                List<Map<String, Object>> invlist = inventoryService.findLocalInventory(param);
                UserInfo user = new UserInfo();
                user.setId(item.getOperator());
                user.setCompanyid(shopid);
                Map<String, Integer> qtyMap = new HashMap<String, Integer>();
                qtyMap.putAll(thirdPartyQty);
                StockTaking stocktaking = new StockTaking();
                stocktaking.setId(warehouseService.getUUID());
                stocktaking.setWarehouseid(localWarehouseId);
                stocktaking.setShopid(shopid);
                stocktaking.setOperator(item.getOperator());
                stocktaking.setFtype(1);
                String number = null;
                try {
                    number = (serialNumService.readSerialNumber(user.getCompanyid(), "K"));
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new BizException("编码获取失败,请联系管理员");
                }
                // 获取仓库总库存和库存总值
                stocktaking.setNumber(number);
                stocktaking.setIsworking(false);// 正在盘点
                stocktaking.setCreator(user.getId());
                List<StocktakingWarehouse> warehouselist = new ArrayList<StocktakingWarehouse>();
                StocktakingWarehouse iteme = new StocktakingWarehouse();
                iteme.setStocktakingid(new BigInteger(stocktaking.getId()));
                iteme.setWarehouseid(new BigInteger(localWarehouseId));
                warehouselist.add(iteme);
                stocktaking.setWarehouselist(warehouselist);
                stocktaking.setCreatedate(new Date());
                stocktaking.setOperator(user.getId());
                stocktaking.setOpttime(new Date());
                stocktaking.setRemark("海外仓同步库存");
                boolean result = stockTakingService.save(stocktaking);
                Map<String, Map<String, Object>> needRemove = new HashMap<String, Map<String, Object>>();
                List<StockTakingItem> itemlist = new java.util.ArrayList<StockTakingItem>();
                for (Map<String, Object> inv : invlist) {
                    String sku = inv.get("sku").toString();
                    if (qtyMap.containsKey(sku)) {
                        StockTakingItem e = new StockTakingItem();
                        e.setMaterialid(inv.get("materialid").toString());
                        e.setWarehouseid(localWarehouseId);
                        e.setStocktakingid(stocktaking.getId());
                        e.setAmount(qtyMap.get(inv.get("sku")));
                        Integer fulfillable = inv.get("fulfillable") != null ? Integer.parseInt(inv.get("fulfillable").toString()) : 0;
                        qtyMap.remove(inv.get("sku"));
                        if (fulfillable < e.getAmount()) {
                            e.setOveramount(e.getAmount() - fulfillable);
                        } else if (fulfillable > e.getAmount()) {
                            e.setLossamount(fulfillable - e.getAmount());
                        } else {
                            continue;
                        }
                        itemlist.add(e);
                    } else {
                        StockTakingItem e = new StockTakingItem();
                        e.setMaterialid(inv.get("materialid").toString());
                        e.setWarehouseid(localWarehouseId);
                        e.setStocktakingid(stocktaking.getId());
                        e.setAmount(0);
                        Integer fulfillable = inv.get("fulfillable") != null ? Integer.parseInt(inv.get("fulfillable").toString()) : 0;
                        if (fulfillable < e.getAmount()) {
                            e.setOveramount(e.getAmount() - fulfillable);
                        } else if (fulfillable > e.getAmount()) {
                            e.setLossamount(fulfillable - e.getAmount());
                        } else {
                            continue;
                        }
                        itemlist.add(e);
                    }
                }
                if (qtyMap != null && qtyMap.size() > 0) {
                    qtyMap.forEach((k, v) -> {
                        StockTakingItem e = new StockTakingItem();
                        Material m = materialService.getBySku(shopid, k);
                        if (m == null) {
                            return;
                        } else {
                            e.setMaterialid(m.getId());
                        }
                        e.setOveramount(v);
                        e.setWarehouseid(localWarehouseId);
                        e.setStocktakingid(stocktaking.getId());
                        e.setAmount(v);
                        itemlist.add(e);
                    });
                }
                if (itemlist != null && itemlist.size() > 0) {
                    stockTakingItemService.saveBatch(itemlist);
                    stockTakingService.updateTotalProfitAndLoss(stocktaking);
                    stockTakingService.endAction(stocktaking, user);
                }
            }finally {
                Warehouse warehouse = warehouseService.getById(item.getLocalWarehouseId());
                if(warehouse.getIsstocktaking()){
                    warehouse.setIsstocktaking(false);
                    warehouseService.updateById(warehouse);
                }
            }
        }
        return true;
    }


    public void runTask(){
        List<ThirdPartyAPI> apiList = iThirdPartyAPIService.lambdaQuery().in(ThirdPartyAPI::getSystem,
                "K5","OPS","XL","YC").list();
        Calendar c=Calendar.getInstance();
        c.add(Calendar.DATE,-5);
        Set<String> shopidSet=new HashSet<String>();
        for(ThirdPartyAPI api:apiList){
                List<ThirdPartyWarehouse> list=null;
                 shopidSet.add(api.getShopid());
                if(api.getSystem().equals("K5")){
                    try {
                         list= warehouseK5Service.searchStartHouse(api);
                    } catch (Exception e) {
                         e.printStackTrace();
                    }
                }else if(api.getSystem().equals("OPS")){
                    try {
                        list= warehouseOPSService.searchStartHouse(api);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else if(api.getSystem().equals("XL")){
                    try {
                       list= warehouseXLSService.searchStartHouse(api);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else if(api.getSystem().equals("YC")){
                    try {
                        list= warehouseYCService.searchStartHouse(api);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            LambdaQueryWrapper<ThirdPartyWarehouse> query = new LambdaQueryWrapper<ThirdPartyWarehouse>();
            query.eq(ThirdPartyWarehouse::getApi, api.getId());
             list = thirdPartyWarehouseMapper.selectList(query);
             if(list==null){
                 continue;
             }
                for(ThirdPartyWarehouse warehouse:list){
                    ThirdpartyWarehouseInvDTO dto=new ThirdpartyWarehouseInvDTO();
                    dto.setShopid(api.getShopid());
                    dto.setApi(api.getApi());
                    dto.setHouseid(warehouse.getCode());
                    dto.setAction("sync");
                    if(api.getSystem().equals("K5")){
                        try {
                            warehouseK5Service.searchStock(api,dto);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if(api.getSystem().equals("OPS")){
                        try {
                             warehouseOPSService.searchStock(api,dto);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if(api.getSystem().equals("XL")){
                        try {
                             warehouseXLSService.searchStock(api,dto);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if(api.getSystem().equals("YC")){
                        try {
                            warehouseYCService.searchStock(api,dto);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            ThirdpartyWarehouseInvDTO dto=new ThirdpartyWarehouseInvDTO();
            dto.setShopid(api.getShopid());
            dto.setApi(api.getApi());
            dto.setAction("sync");
            dto.setCreatedStartTime(LocalDateTime.of(c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DATE),1,1,1).toString()+".550Z");
            dto.setCreatedEndTime(null);
            if(api.getSystem().equals("K5")){
                try {
                    warehouseK5Service.getOrder(api,dto);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if(api.getSystem().equals("OPS")){
                try {
                    warehouseOPSService.getOrder(api,dto);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if(api.getSystem().equals("XL")){
                try {
                    warehouseXLSService.getOrder(api,dto);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if(api.getSystem().equals("YC")){
                try {
                    warehouseYCService.getOrder(api,dto);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }
        try {
            iOrderService.summary();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            for(String shopid:shopidSet){
                Map<String,Object> param=new HashMap<String,Object>();
                param.put("shopid",shopid);
                orderPlanService.refreshData(param);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
