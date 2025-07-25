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
import com.wimoor.erp.thirdparty.mapper.ThirdPartyWarehouseInventoryMapperK5;
import com.wimoor.erp.thirdparty.mapper.ThirdPartyWarehouseMapper;
import com.wimoor.erp.thirdparty.pojo.dto.ThirdpartyWarehouseInvDTO;
import com.wimoor.erp.thirdparty.pojo.entity.ThirdPartyAPI;
import com.wimoor.erp.thirdparty.pojo.entity.ThirdPartyWarehouseBind;
import com.wimoor.erp.thirdparty.pojo.entity.ThirdPartyWarehouseInventoryK5;
import com.wimoor.erp.thirdparty.pojo.entity.ThirdPartyWarehouse;
import com.wimoor.erp.thirdparty.service.IThirdPartyWarehouseBindService;
import com.wimoor.erp.thirdparty.service.IThirdPartyWarehouseService;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service("warehouseK5Service")
@RequiredArgsConstructor
public class WarehouseK5ServiceImpl implements IThirdPartyWarehouseService {
    final ThirdPartyAPIMapper thirdPartyAPIMapper;
    final ThirdPartyWarehouseMapper thirdPartyWarehouseMapper;
    final ThirdPartyWarehouseInventoryMapperK5 thirdPartyWarehouseInventoryK5Mapper;
    final IThirdPartyWarehouseBindService iThirdPartyWarehouseBindService;
    final IOrderPlatformService orderPlatformService;
    final IOrderService orderService;
    final static String postMethod = "/PostInterfaceService?method=";
    final OrderListingMapper orderListingMapper;
    public Map<String, String> getHeader() {
        Map<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json;charset=UTF-8");
        return header;
    }

    public JSONObject getParam(ThirdPartyAPI api) {
        JSONObject param = new JSONObject();
        JSONObject verify = new JSONObject();
        verify.put("Clientid", api.getAppkey());
        verify.put("Token", api.getAppsecret());
        param.put("Verify", verify);
        return param;
    }

    public List<Map<String, Object>> searchStock(ThirdPartyAPI api, ThirdpartyWarehouseInvDTO dto) throws HttpException {
        String houseId = dto.getHouseid();
        String sku = dto.getSku();
        LambdaQueryWrapper<ThirdPartyWarehouse> queryWarehouse=new LambdaQueryWrapper<ThirdPartyWarehouse>();
        queryWarehouse.eq(ThirdPartyWarehouse::getCode,dto.getHouseid());
        ThirdPartyWarehouse warehouse = thirdPartyWarehouseMapper.selectOne(queryWarehouse);
        if (StrUtil.isNotBlank(dto.getAction()) && dto.getAction().equals("sync")) {
            String url = api.getApi() + postMethod + "searchStock";
            JSONObject param = getParam(api);
            if (StrUtil.isNotBlank(houseId)) {
                param.put("houseId", houseId);
            }
            if (StrUtil.isNotBlank(sku)) {
                param.put("sku", sku);
            }
            String response = HttpClientUtil.postUrl(url, param.toString(), getHeader());
            JSONObject myresult = GeneralUtil.getJsonObject(response);
            JSONArray returnDatas = myresult != null ? myresult.getJSONArray("returnDatas") : null;
            LambdaQueryWrapper<ThirdPartyWarehouseInventoryK5> query = new LambdaQueryWrapper<ThirdPartyWarehouseInventoryK5>();
            query.eq(ThirdPartyWarehouseInventoryK5::getApi, api.getId());
            query.eq(ThirdPartyWarehouseInventoryK5::getShopid, api.getShopid());
            if (StrUtil.isNotBlank(houseId)) {
                query.eq(ThirdPartyWarehouseInventoryK5::getHouseid, houseId);
            }
            if (StrUtil.isNotBlank(sku)) {
                query.eq(ThirdPartyWarehouseInventoryK5::getSku, sku);
            }

            thirdPartyWarehouseInventoryK5Mapper.delete(query);
            Map<String, Integer> qtyMap = new HashMap<String, Integer>();
            for (int i = 0; returnDatas != null && i < returnDatas.size(); i++) {
                JSONObject data = (JSONObject) returnDatas.get(i);
                String itemsku = data.getString("sku");
                String houseid = data.getString("houseid");
                ThirdPartyWarehouseInventoryK5 inventory = new ThirdPartyWarehouseInventoryK5();
                inventory.setHouseid(houseid);
                inventory.setSku(itemsku);
                inventory.setApi(api.getId());
                inventory.setNum(Integer.parseInt(data.getString("num")));
                inventory.setRefreshtime(new java.util.Date());
                inventory.setShopid(api.getShopid());
                inventory.setLocknum(Integer.parseInt(data.getString("lockNum")));
                inventory.setOutnum(Integer.parseInt(data.getString("outNum")));
                inventory.setCname(data.getString("cname"));
                inventory.setEname(data.getString("ename"));
                inventory.setHousename(data.getString("houseName"));
                thirdPartyWarehouseInventoryK5Mapper.insert(inventory);
                if(warehouse!=null){
                    ThirdPartyWarehouseBind bind = this.iThirdPartyWarehouseBindService.lambdaQuery().eq(ThirdPartyWarehouseBind::getThirdpartyWarehouseId, warehouse.getId()).one();
                    if(bind!=null){
                        OrderListing ol=new OrderListing();
                        ol.setShopid(api.getShopid());
                        ol.setWarehouseid(bind.getLocalWarehouseId());
                        ol.setSku(inventory.getSku());
                        ol.setName(data.getString("cname"));
                        ol.setEname(data.getString("ename"));
                        ol.setQty(inventory.getNum());
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
                            orderListing.setName(data.getString("cname"));
                            orderListing.setEname(data.getString("ename"));
                            orderListing.setQty(inventory.getNum());
                            orderListingMapper.update(orderListing,oquery);
                        }
                    }
                }
            }
            iThirdPartyWarehouseBindService.asyncLocalInventory(api.getShopid(), houseId, qtyMap);
        }

        return thirdPartyWarehouseInventoryK5Mapper.findByDto(dto);
    }

    // 辅助方法设置Warehouse属性，提高代码复用性和清晰度
    private void setWarehouseProperties(ThirdPartyWarehouse warehouse, JSONObject data, ThirdPartyAPI api) {
        warehouse.setCode(data.getString("code"));
        warehouse.setName(data.getString("name"));
        warehouse.setCountry(data.getString("country"));
        warehouse.setApi(api.getId());
        warehouse.setShopid(api.getShopid());
        warehouse.setExt(data.getString("channelDatas"));
    }

    public List<ThirdPartyWarehouse> searchStartHouse(ThirdPartyAPI api) throws HttpException {
        String url = api.getApi() + postMethod + "searchStartHouse";
        JSONObject param = getParam(api);
        String response = HttpClientUtil.postUrl(url, param.toString(), getHeader());
        JSONObject myresult = GeneralUtil.getJsonObject(response);
        JSONArray returnDatas = myresult != null ? myresult.getJSONArray("returnDatas") : null;
        List<ThirdPartyWarehouse> list = new ArrayList<ThirdPartyWarehouse>();
        for (int i = 0; returnDatas != null && i < returnDatas.size(); i++) {
            JSONObject data = (JSONObject) returnDatas.get(i);
            LambdaQueryWrapper<ThirdPartyWarehouse> query = new LambdaQueryWrapper<ThirdPartyWarehouse>();
            query.eq(ThirdPartyWarehouse::getCode, data.getString("code"));
            ThirdPartyWarehouse warehouse = thirdPartyWarehouseMapper.selectOne(query);
            if (warehouse != null) {
                setWarehouseProperties(warehouse, data, api);
                thirdPartyWarehouseMapper.updateById(warehouse);
            } else {
                warehouse = new ThirdPartyWarehouse();
                setWarehouseProperties(warehouse, data, api);
                thirdPartyWarehouseMapper.insert(warehouse);
            }
            list.add(warehouse);
        }
        return list;
    }

    @Override
    public List<ThirdPartyWarehouse> listByApi(ThirdPartyAPI api) {
        LambdaQueryWrapper<ThirdPartyWarehouse> query = new LambdaQueryWrapper<ThirdPartyWarehouse>();
        query.eq(ThirdPartyWarehouse::getApi, api.getId());
        query.eq(ThirdPartyWarehouse::getShopid, api.getShopid());
        return thirdPartyWarehouseMapper.selectList(query);
    }

    @Override
    public JSONObject getOrder(ThirdPartyAPI api, ThirdpartyWarehouseInvDTO dto) {
        String url = api.getApi() + postMethod + "searchWSOrder";
        JSONObject param = getParam(api);
        JSONObject myresult = null;
        String response = null;
        try {
            param.put("OutDataBeg", dto.getCreatedStartTime());
            if (dto.getCreatedEndTime() != null) {
                param.put("OutDataEnd", dto.getCreatedEndTime());
            }
            param.put("OutStatus", "1");
            response = HttpClientUtil.postUrl(url, param.toString(), getHeader());
            myresult = GeneralUtil.getJsonObject(response);
            JSONArray SorderDatas = myresult != null ? myresult.getJSONArray("SorderDatas") : null;
            for (int i = 0; SorderDatas != null && i < SorderDatas.size(); i++) {
                JSONObject order = (JSONObject) SorderDatas.get(i);
                String warehouseCode = order.getString("HouseId");
                ThirdPartyWarehouse warehouse = null;
                if (StrUtil.isNotBlank(warehouseCode)) {
                    warehouse = thirdPartyWarehouseMapper.selectOne(new LambdaQueryWrapper<ThirdPartyWarehouse>()
                            .eq(ThirdPartyWarehouse::getCode, warehouseCode)
                            .eq(ThirdPartyWarehouse::getApi, api.getId())
                            .eq(ThirdPartyWarehouse::getShopid, api.getShopid()));
                }
                if (warehouse == null) {
                    continue;
                }
                List<ThirdPartyWarehouseBind> thridbind = this.iThirdPartyWarehouseBindService.lambdaQuery().eq(ThirdPartyWarehouseBind::getThirdpartyWarehouseId, warehouse.getId()).list();
                String warehouseid = null;
                if (thridbind != null && thridbind.size() > 0) {
                    warehouseid = thridbind.get(0).getLocalWarehouseId();
                }
                String platform = order.getString("ChannelId");
                String platformid = null;
                if (StrUtil.isNotBlank(platform)) {
                    OrderPlatform orderPlatform = orderPlatformService.lambdaQuery().eq(OrderPlatform::getName, platform)
                            .eq(OrderPlatform::getShopid, api.getShopid()).one();
                    if (orderPlatform == null) {
                        orderPlatform = new OrderPlatform();
                        orderPlatform.setName(platform);
                        orderPlatform.setShopid(api.getShopid());
                        orderPlatform.setOpttime(new Date());
                        orderPlatform.setOperator(api.getOperator() != null ? api.getOperator().toString() : null);
                        orderPlatform.setDisabled(false);
                        orderPlatformService.save(orderPlatform);
                    }
                    platformid=orderPlatform.getId();
                }
                String poNum=order.getString("CorpBillid");
                Date createdTime = order.getDate("OutData");
                JSONArray cargoList = order.getJSONArray("OrderItems");
                if(cargoList!=null){
                    for(int j=0;j<cargoList.size();j++){
                        JSONObject cargo = cargoList.getJSONObject(j);
                        String sku=cargo.getString("SKU");
                        Integer quantity = cargo.getInteger("Num");
                        String name=cargo.getString("ProductName");
                        String ename=cargo.getString("ProductName");
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
                            orderObject.setCountry(order.getString("Country"));
                            orderObject.setWarehouseid(warehouseid);
                            orderObject.setIsout(true);
                            orderObject.setThirdpartyWarehouseid(warehouse.getId());
                            orderObject.setQuantity(quantity);
                            orderObject.setPrice(null);
                            orderObject.setPurchaseDate(createdTime);
                            orderObject.setShipFee(BigDecimal.ZERO);
                            orderObject.setReferralFee(BigDecimal.ZERO);
                            orderObject.setReferralRate(BigDecimal.ZERO);
                            orderObject.setOperator(api.getOperator()!=null?api.getOperator().toString():null);
                            orderService.save(orderObject);
                        }else{
                            orderObject.setCountry(order.getString("Country"));
                            orderObject.setWarehouseid(warehouseid);
                            orderObject.setIsout(true);
                            orderObject.setThirdpartyWarehouseid(warehouse.getId());
                            orderObject.setQuantity(quantity);
                            orderObject.setPrice(null);
                            orderObject.setPurchaseDate(createdTime);
                            orderObject.setShipFee(BigDecimal.ZERO);
                            orderObject.setReferralFee(BigDecimal.ZERO);
                            orderObject.setReferralRate(BigDecimal.ZERO);
                            orderObject.setOperator(api.getOperator()!=null?api.getOperator().toString():null);
                            orderService.updateById(orderObject);
                        }
                        if(StrUtil.isNotBlank(warehouseid)){
                            OrderListing ol=new OrderListing();
                            ol.setShopid(api.getShopid());
                            ol.setCountry(orderObject.getCountry());
                            ol.setWarehouseid(warehouseid);
                            ol.setEname(cargo.getString("cargoNameEn"));
                            ol.setSku(orderObject.getSku());
                            ol.setName(name);
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
                                orderListing.setEname(cargo.getString("cargoNameEn"));
                                orderListing.setSku(orderObject.getSku());
                                orderListing.setName(name);
                                orderListingMapper.update(orderListing,query);
                            }
                        }
                    }
                }
            }
        } catch (HttpException e) {
            throw new RuntimeException(e);
        }
        return myresult;
    }
}
