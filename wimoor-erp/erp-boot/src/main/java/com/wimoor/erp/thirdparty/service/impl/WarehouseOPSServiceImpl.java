package com.wimoor.erp.thirdparty.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.HttpClientUtil;
import com.wimoor.erp.order.mapper.OrderListingMapper;
import com.wimoor.erp.order.pojo.entity.Order;
import com.wimoor.erp.order.pojo.entity.OrderListing;
import com.wimoor.erp.order.pojo.entity.OrderPlatform;
import com.wimoor.erp.order.service.IOrderPlatformService;
import com.wimoor.erp.order.service.IOrderService;
import com.wimoor.erp.thirdparty.mapper.ThirdPartyAPIMapper;
import com.wimoor.erp.thirdparty.mapper.ThirdPartyWarehouseInventoryMapperOps;
import com.wimoor.erp.thirdparty.mapper.ThirdPartyWarehouseMapper;
import com.wimoor.erp.thirdparty.pojo.dto.ThirdpartyWarehouseInvDTO;
import com.wimoor.erp.thirdparty.pojo.entity.ThirdPartyAPI;
import com.wimoor.erp.thirdparty.pojo.entity.ThirdPartyWarehouse;
import com.wimoor.erp.thirdparty.pojo.entity.ThirdPartyWarehouseBind;
import com.wimoor.erp.thirdparty.pojo.entity.ThirdPartyWarehouseInventoryOps;
import com.wimoor.erp.thirdparty.service.IThirdPartyWarehouseBindService;
import com.wimoor.erp.thirdparty.service.IThirdPartyWarehouseService;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service("warehouseOPSService")
@RequiredArgsConstructor
public class WarehouseOPSServiceImpl implements IThirdPartyWarehouseService {
    final ThirdPartyAPIMapper thirdPartyAPIMapper;
    final ThirdPartyWarehouseMapper thirdPartyWarehouseMapper;
    final ThirdPartyWarehouseInventoryMapperOps thirdPartyWarehouseInventoryOpsMapper;
    final IThirdPartyWarehouseBindService iThirdPartyWarehouseBindService;
    final IOrderPlatformService orderPlatformService;
    final IOrderService orderService;
    final OrderListingMapper orderListingMapper;
    final static  String version ="";
    public Map<String,String> getHeader(ThirdPartyAPI api){
        Map<String,String> header=new HashMap<String,String>();
        header.put("Content-Type","application/json;charset=UTF-8");
        header.put("appKey",  api.getAppkey());
        header.put("appToken", api.getAppsecret());
        return header;
    }

    public List<Map<String, Object>> searchStock(ThirdPartyAPI api, ThirdpartyWarehouseInvDTO dto) throws HttpException {
        String houseId=dto.getHouseid();
        String sku=dto.getSku();
        LambdaQueryWrapper<ThirdPartyWarehouse> queryWarehouse=new LambdaQueryWrapper<ThirdPartyWarehouse>();
        queryWarehouse.eq(ThirdPartyWarehouse::getCode,dto.getHouseid());
        ThirdPartyWarehouse warehouse = thirdPartyWarehouseMapper.selectOne(queryWarehouse);
        if(StrUtil.isNotBlank(dto.getAction())&&dto.getAction().equals("sync")){
            String url=api.getApi()+ version +"/edi/web-services/getClientInventory";

            if(StrUtil.isNotBlank(houseId)){
                url=url+(url.contains("?")?"&warehouseCode="+houseId:"?warehouseCode="+houseId);
            }
            if(StrUtil.isNotBlank(sku)){
                url=url+(url.contains("?")?"&sku="+sku:"?sku="+sku);
            }
            String response= null;
            try {
                response = HttpClientUtil.getUrl(url,getHeader(api));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            LambdaQueryWrapper<ThirdPartyWarehouseInventoryOps> query=new LambdaQueryWrapper<ThirdPartyWarehouseInventoryOps>();
            if(StrUtil.isNotBlank(houseId)){
                query.eq(ThirdPartyWarehouseInventoryOps::getHouseid,houseId);
            }
            if(StrUtil.isNotBlank(sku)){
                query.eq(ThirdPartyWarehouseInventoryOps::getSku,sku);
            }
            query.eq(ThirdPartyWarehouseInventoryOps::getApi,api.getId());
            query.eq(ThirdPartyWarehouseInventoryOps::getShopid,api.getShopid());
            thirdPartyWarehouseInventoryOpsMapper.delete(query);
            JSONArray returnDatas = GeneralUtil.getJsonArray(response);
            Map<String,Integer> qtyMap=new HashMap<String,Integer>();
            for(int i=0;returnDatas!=null&&i<returnDatas.size();i++){
                JSONObject data = (JSONObject)returnDatas.get(i);
                String itemsku=data.getString("sku");
                String houseid=dto.getHouseid();
                ThirdPartyWarehouseInventoryOps inventory=new ThirdPartyWarehouseInventoryOps();
                inventory.setHouseid(houseid);
                inventory.setSku(itemsku);
                inventory.setApi(api.getId());
                inventory.setQuantity(Integer.parseInt(data.getString("pkgs")));
                inventory.setRefreshtime(new java.util.Date());
                inventory.setShopid(api.getShopid());
                inventory.setHeight(GeneralUtil.getBigDecimal(data.getString("height")));
                inventory.setWidth(GeneralUtil.getBigDecimal(data.getString("width")));
                inventory.setLength(GeneralUtil.getBigDecimal(data.getString("length")));
                inventory.setWeight(GeneralUtil.getBigDecimal(data.getString("gw")));
                inventory.setVol(GeneralUtil.getBigDecimal(data.getString("vol")));
                inventory.setPackagetype(data.getString("packageType"));
                inventory.setMark(data.getString("mark"));
                inventory.setCname(data.getString("cargoName"));
                inventory.setEname(data.getString("cargoNameEn"));
                inventory.setHousename(warehouse!=null?warehouse.getName():null);
                qtyMap.put(itemsku,inventory.getQuantity());
                thirdPartyWarehouseInventoryOpsMapper.insert(inventory);
                if(warehouse!=null){
                    ThirdPartyWarehouseBind bind = this.iThirdPartyWarehouseBindService.lambdaQuery().eq(ThirdPartyWarehouseBind::getThirdpartyWarehouseId, warehouse.getId()).one();
                    if(bind!=null){
                        OrderListing ol=new OrderListing();
                        ol.setShopid(api.getShopid());
                        ol.setWarehouseid(bind.getLocalWarehouseId());
                        ol.setSku(inventory.getSku());
                        ol.setName(data.getString("cargoName"));
                        ol.setEname(data.getString("cargoNameEn"));
                        ol.setQty(inventory.getQuantity());
                        LambdaQueryWrapper<OrderListing> oquery=new LambdaQueryWrapper<OrderListing>();
                        oquery.eq(OrderListing::getShopid,ol.getShopid());
                        oquery.eq(OrderListing::getWarehouseid,bind.getLocalWarehouseId());
                        oquery.eq(OrderListing::getSku,ol.getSku());
                        OrderListing orderListing = orderListingMapper.selectOne(oquery);
                        if(orderListing==null){
                            orderListingMapper.insert(ol);
                        }else{
                            orderListing.setShopid(api.getShopid());
                            orderListing.setWarehouseid(bind.getLocalWarehouseId());
                            orderListing.setSku(inventory.getSku());
                            orderListing.setName(data.getString("cargoName"));
                            orderListing.setEname(data.getString("cargoNameEn"));
                            orderListing.setQty(inventory.getQuantity());
                            orderListingMapper.update(orderListing,oquery);
                        }
                    }
                }
            }
            if(warehouse!=null){
                iThirdPartyWarehouseBindService.asyncLocalInventory(api.getShopid(),warehouse.getId(),qtyMap);
            }

        }

        return thirdPartyWarehouseInventoryOpsMapper.findByDto(dto);
    }
    // 辅助方法设置Warehouse属性，提高代码复用性和清晰度
    private void setWarehouseProperties(ThirdPartyWarehouse warehouse, JSONObject data, ThirdPartyAPI api) {
        warehouse.setCode(data.getString("code"));
        warehouse.setName(data.getString("name"));
        warehouse.setCountry(null);
        warehouse.setApi(api.getId());
        warehouse.setShopid(api.getShopid());
        warehouse.setExt(data.getString("address"));
    }
    public JSONObject getOrder(ThirdPartyAPI api, ThirdpartyWarehouseInvDTO dto) {
        String url=api.getApi()+ version +"/edi/web-services/wms/getOrders";
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        String createdStartTime=dto.getCreatedStartTime()==null?GeneralUtil.formatDate(cal.getTime(), "yyyy-MM-dd"):dto.getCreatedStartTime();
        String createdEndTime=dto.getCreatedEndTime();
        if(StrUtil.isNotBlank(createdStartTime)){
            url=url+(url.contains("?")?"&createdStartTime="+createdStartTime:"?createdStartTime="+createdStartTime);
        }
        if(StrUtil.isNotBlank(createdEndTime)){
            url=url+(url.contains("?")?"&createdEndTime="+createdEndTime:"?createdEndTime="+createdEndTime);
        }
        url=url+(url.contains("?")?"&pageNum="+(dto.getCurrentpage()-1):"?pageNum="+(dto.getCurrentpage()-1));
        String response= null;
        try {
            response = HttpClientUtil.getUrl(url,getHeader(api));
        } catch (IOException | HttpException e) {
            throw new RuntimeException(e);
        }
        Integer totalPages=0;
        JSONObject data = GeneralUtil.getJsonObject(response);
        if(data!=null&&data.containsKey("totalPages")&&data.getInteger("totalPages")!=1&&data.getInteger("totalPages")>(dto.getCurrentpage()+1)){
            JSONArray orders = data.getJSONArray("orders");
            totalPages=data.getInteger("totalPages");
            if(orders!=null){
                for(int i=0;i<orders.size();i++){
                    JSONObject order = orders.getJSONObject(i);
                    String warehouseCode=order.getString("warehouseCode");
                    String warehouseName=order.getString("warehouseName");
                    ThirdPartyWarehouse warehouse =null;
                    if(StrUtil.isNotBlank(warehouseCode)){
                        warehouse = thirdPartyWarehouseMapper.selectOne(new LambdaQueryWrapper<ThirdPartyWarehouse>()
                                                                        .eq(ThirdPartyWarehouse::getCode, warehouseCode)
                                                                        .eq(ThirdPartyWarehouse::getApi, api.getId())
                                                                        .eq(ThirdPartyWarehouse::getShopid, api.getShopid()));
                    }
                    if(warehouse==null){
                        warehouse = thirdPartyWarehouseMapper.selectOne(new LambdaQueryWrapper<ThirdPartyWarehouse>()
                                                                        .eq(ThirdPartyWarehouse::getName, warehouseName)
                                                                        .eq(ThirdPartyWarehouse::getApi, api.getId())
                                                                        .eq(ThirdPartyWarehouse::getShopid, api.getShopid()));
                    }
                    if(warehouse==null){
                        continue;
                    }
                    List<ThirdPartyWarehouseBind> thridbind = this.iThirdPartyWarehouseBindService.lambdaQuery().eq(ThirdPartyWarehouseBind::getThirdpartyWarehouseId, warehouse.getId()).list();
                    String warehouseid=null;
                    if(thridbind!=null&&thridbind.size()>0){
                        warehouseid=thridbind.get(0).getLocalWarehouseId();
                    }
                    String platform=order.getString("platform");
                    String platformid=null;
                    if(StrUtil.isNotBlank(platform)){
                        OrderPlatform orderPlatform = orderPlatformService.lambdaQuery().eq(OrderPlatform::getName, platform)
                                                                                         .eq(OrderPlatform::getShopid, api.getShopid()).one();
                        if(orderPlatform==null){
                            orderPlatform=new OrderPlatform();
                            orderPlatform.setName(platform);
                            orderPlatform.setShopid(api.getShopid());
                            orderPlatform.setOpttime(new Date());
                            orderPlatform.setOperator(api.getOperator()!=null?api.getOperator().toString():null);
                            orderPlatform.setDisabled(false);
                            orderPlatformService.save(orderPlatform);
                        }
                        platformid=orderPlatform.getId();
                        String poNum=order.getString("poNum");
                        Date createdTime = order.getDate("createdTime");
                        JSONArray cargoList = order.getJSONArray("cargoList");
                        if(cargoList!=null){
                            for(int j=0;j<cargoList.size();j++){
                                JSONObject cargo = cargoList.getJSONObject(j);
                                String sku=cargo.getString("sku");
                                Integer quantity = cargo.getInteger("quantity");
                                BigDecimal usdValuePerUnit = cargo.getBigDecimal("usdValuePerUnit");
                                LambdaQueryWrapper<Order> orderQuery=new LambdaQueryWrapper<Order>();
                                orderQuery.eq(Order::getShopid,api.getShopid());
                                orderQuery.eq(Order::getPlatformId,platformid);
                                orderQuery.eq(Order::getOrderId,poNum);
                                orderQuery.eq(Order::getSku,sku);
                                Order orderObject = orderService.getOne(orderQuery);
                                if(orderObject==null){
                                    orderObject=new Order();
                                    orderObject.setShopid(api.getShopid());
                                    orderObject.setPlatformId(platformid);
                                    orderObject.setOrderId(poNum);
                                    orderObject.setSku(sku);
                                    orderObject.setCountry(order.getString("shipperCountryCode"));
                                    orderObject.setWarehouseid(warehouseid);
                                    orderObject.setIsout(true);
                                    orderObject.setThirdpartyWarehouseid(warehouse.getId());
                                    orderObject.setCurrency(order.getString("sellCur"));
                                    orderObject.setQuantity(quantity);
                                    orderObject.setPrice(usdValuePerUnit);
                                    orderObject.setPurchaseDate(createdTime);
                                    orderObject.setShipFee(BigDecimal.ZERO);
                                    orderObject.setReferralFee(BigDecimal.ZERO);
                                    orderObject.setReferralRate(BigDecimal.ZERO);
                                    orderObject.setOperator(api.getOperator()!=null?api.getOperator().toString():null);
                                    orderService.save(orderObject);
                                }else{
                                    orderObject.setShopid(api.getShopid());
                                    orderObject.setPlatformId(platformid);
                                    orderObject.setOrderId(poNum);
                                    orderObject.setSku(sku);
                                    orderObject.setCountry(order.getString("shipperCountryCode"));
                                    orderObject.setWarehouseid(warehouseid);
                                    orderObject.setIsout(true);
                                    orderObject.setThirdpartyWarehouseid(warehouse.getId());
                                    orderObject.setCurrency(order.getString("sellCur"));
                                    orderObject.setQuantity(quantity);
                                    orderObject.setPrice(usdValuePerUnit);
                                    orderObject.setPurchaseDate(createdTime);
                                    orderObject.setShipFee(BigDecimal.ZERO);
                                    orderObject.setReferralFee(BigDecimal.ZERO);
                                    orderObject.setReferralRate(BigDecimal.ZERO);
                                    orderObject.setOperator(api.getOperator()!=null?api.getOperator().toString():null);
                                    orderService.update(orderObject,orderQuery);
                                }
                                if(StrUtil.isNotBlank(warehouseid)){
                                    OrderListing ol=new OrderListing();
                                    ol.setShopid(api.getShopid());
                                    ol.setCountry(orderObject.getCountry());
                                    ol.setWarehouseid(warehouseid);
                                    ol.setSku(orderObject.getSku());
                                    ol.setName(cargo.getString("cargoName"));
                                    ol.setEname(cargo.getString("cargoNameEn"));
                                    LambdaQueryWrapper<OrderListing> query=new LambdaQueryWrapper<OrderListing>();
                                    query.eq(OrderListing::getShopid,ol.getShopid());
                                    query.eq(OrderListing::getWarehouseid,ol.getWarehouseid());
                                    query.eq(OrderListing::getSku,ol.getSku());
                                    OrderListing orderListing = orderListingMapper.selectOne(query);
                                    if(orderListing==null){
                                        orderListingMapper.insert(ol);
                                    }else{
                                        orderListing.setShopid(api.getShopid());
                                        orderListing.setCountry(orderObject.getCountry());
                                        orderListing.setWarehouseid(warehouseid);
                                        orderListing.setSku(orderObject.getSku());
                                        orderListing.setName(cargo.getString("cargoName"));
                                        orderListing.setEname(cargo.getString("cargoNameEn"));
                                        orderListingMapper.update(orderListing,query);
                                    }
                                }
                            }
                        }

                    }

                }
            }
            if(dto.getCurrentpage()+1<totalPages){
                dto.setCurrentpage(dto.getCurrentpage()+1);
                getOrder(api,dto);
            }
        }
        return data;
    }

    public List<ThirdPartyWarehouse> searchStartHouse(ThirdPartyAPI api) throws HttpException {
        String url=api.getApi()+ version +"/edi/web-services/wms/getDataList?whType=";
        String response= null;
        try {
            response = HttpClientUtil.getUrl(url,getHeader(api));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JSONObject myresult = GeneralUtil.getJsonObject(response);
        JSONArray returnDatas =myresult!=null?myresult.getJSONArray("warehouses"):null;
        List<ThirdPartyWarehouse> list=new ArrayList<ThirdPartyWarehouse>();
        for(int i=0;returnDatas!=null&&i<returnDatas.size();i++){
            JSONObject data = (JSONObject)returnDatas.get(i);
            LambdaQueryWrapper<ThirdPartyWarehouse> query=new LambdaQueryWrapper<ThirdPartyWarehouse>();
            query.eq(ThirdPartyWarehouse::getCode,data.getString("code"));
            ThirdPartyWarehouse warehouse = thirdPartyWarehouseMapper.selectOne(query);
            if(warehouse!=null){
                setWarehouseProperties( warehouse,  data,  api);
                thirdPartyWarehouseMapper.updateById(warehouse);
            }else{
                warehouse=new ThirdPartyWarehouse();
                setWarehouseProperties( warehouse,  data,  api);
                thirdPartyWarehouseMapper.insert(warehouse);
            }
            list.add(warehouse);
        }
        return list;
    }

    @Override
    public List<ThirdPartyWarehouse> listByApi(ThirdPartyAPI api) {
        LambdaQueryWrapper<ThirdPartyWarehouse> query=new LambdaQueryWrapper<ThirdPartyWarehouse>();
        query.eq(ThirdPartyWarehouse::getApi,api.getId());
        query.eq(ThirdPartyWarehouse::getShopid,api.getShopid());
        return thirdPartyWarehouseMapper.selectList(query);
    }


}
