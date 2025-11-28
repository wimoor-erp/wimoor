package com.wimoor.erp.thirdparty.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.HttpClientUtil;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.order.mapper.OrderListingMapper;
import com.wimoor.erp.order.pojo.entity.Order;
import com.wimoor.erp.order.pojo.entity.OrderListing;
import com.wimoor.erp.order.pojo.entity.OrderPlatform;
import com.wimoor.erp.order.service.IOrderPlatformService;
import com.wimoor.erp.order.service.IOrderService;
import com.wimoor.erp.thirdparty.mapper.*;
import com.wimoor.erp.thirdparty.pojo.dto.ThirdpartyWarehouseInvDTO;
import com.wimoor.erp.thirdparty.pojo.entity.*;
import com.wimoor.erp.thirdparty.service.IThirdPartyWarehouseBindService;
import com.wimoor.erp.thirdparty.service.IThirdPartyWarehouseService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Hex;
import org.apache.http.HttpException;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.security.Key;
import java.time.Instant;
import java.util.*;

@Service("warehouseXLSService")
@RequiredArgsConstructor
public class WarehouseXLServiceImpl implements IThirdPartyWarehouseService {
    final ThirdPartyAPIMapper thirdPartyAPIMapper;
    final ThirdPartyWarehouseMapper thirdPartyWarehouseMapper;
    final ThirdPartyWarehouseInventoryMapperXL thirdPartyWarehouseInventoryXLMapper;
    final IMaterialService materialService;
    final IThirdPartyWarehouseBindService iThirdPartyWarehouseBindService;
    final OrderListingMapper orderListingMapper;
    final IOrderService orderService;
    final IOrderPlatformService orderPlatformService;
    final static  String postMethod="";
    public Map<String,String> getHeader(){
        Map<String,String> header=new HashMap<String,String>();
        header.put("Content-Type","application/json;charset=UTF-8");
        return header;
    }
    public Map<String,Object> getParam(ThirdPartyAPI api, JSONObject data){
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("appKey",api.getAppkey());
        if(data!=null){
            param.put("data",data);
        }
        long unixTimestamp = Instant.now().getEpochSecond();
        param.put("reqTime",unixTimestamp);
        return param;
    }
    public List<Map<String, Object>> searchStock(ThirdPartyAPI api, ThirdpartyWarehouseInvDTO dto) throws HttpException {
        String houseId=dto.getHouseid();
        String sku=dto.getSku();
        Integer totalPages=0;
        if(StrUtil.isNotBlank(dto.getAction())&&dto.getAction().equals("sync")) {
            LambdaQueryWrapper<ThirdPartyWarehouse> queryWarehouse = new LambdaQueryWrapper<ThirdPartyWarehouse>();
            queryWarehouse.eq(ThirdPartyWarehouse::getCode, dto.getHouseid());
            ThirdPartyWarehouse warehouse = thirdPartyWarehouseMapper.selectOne(queryWarehouse);
            String url = api.getApi() + postMethod + "/openapi/v1/integratedInventory/pageOpen";
            JSONObject data = new JSONObject();
            if (StrUtil.isNotBlank(dto.getHouseid())) {
                data.put("whCodeList", houseId);
            }
            if (StrUtil.isNotBlank(sku)) {
                data.put("skuList", sku);
            }
            data.put("page", dto.getCurrentpage());
            data.put("pageSize",dto.getPagesize()>100?100:dto.getPagesize());
            Map<String, Object> param = getParam(api, data);
            String authcode = null;
            try {
                authcode = Md5Srv2.geAuthCode(api.getAppkey(), api.getAppsecret(), Long.parseLong(param.get("reqTime").toString()), param);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            LambdaQueryWrapper<ThirdPartyWarehouseInventoryXL> query = new LambdaQueryWrapper<ThirdPartyWarehouseInventoryXL>();
            if (StrUtil.isNotBlank(houseId)) {
                query.eq(ThirdPartyWarehouseInventoryXL::getHouseid, houseId);
            }
            if (StrUtil.isNotBlank(sku)) {
                query.eq(ThirdPartyWarehouseInventoryXL::getSku, sku);
            }
            query.eq(ThirdPartyWarehouseInventoryXL::getApi, api.getId());
            query.eq(ThirdPartyWarehouseInventoryXL::getShopid, api.getShopid());
            thirdPartyWarehouseInventoryXLMapper.delete(query);
            String response = HttpClientUtil.postUrl(url + "?authcode=" + authcode, JSONUtil.parse(param).toString(), getHeader());
            JSONObject myresult = GeneralUtil.getJsonObject(response);
            if (myresult != null && myresult.containsKey("data")) {
                JSONObject resultdata = myresult.getJSONObject("data");
                Map<String, Integer> qtyMap = new HashMap<String, Integer>();
                if (resultdata != null && resultdata.containsKey("records")) {
                    JSONArray records = resultdata.getJSONArray("records");
                    totalPages=resultdata.getInteger("pages");
                    for (int i = 0; i < records.size(); i++) {
                        JSONObject dataitem =   records.getJSONObject(i);
                        String itemsku = dataitem.getString("sku");
                        Integer amount = dataitem.containsKey("productTotalAmount")? dataitem.getInteger("productTotalAmount") : 0;
                        qtyMap.put(itemsku, amount);
                        Map<String, Object> mparam = new HashMap<String, Object>();
                        mparam.put("skulist", Arrays.asList(itemsku));
                        mparam.put("shopid", api.getShopid());
                        Map<String, Object> result = materialService.findMaterialMapBySku(mparam);
                        if (result != null && result.get(itemsku) != null) {
                            dataitem.put("info", result.get(itemsku));
                        }
                        sku = itemsku;
                        String houseid = dto.getHouseid();
                        ThirdPartyWarehouseInventoryXL inventory = new ThirdPartyWarehouseInventoryXL();
                        inventory.setHouseid(houseid);
                        inventory.setSku(itemsku);
                        inventory.setApi(api.getId());
                        inventory.setHouseid(dataitem.getString("whCode"));
                        inventory.setRefreshtime(new java.util.Date());
                        inventory.setShopid(api.getShopid());
                        inventory.setFbaQuantity(dataitem.getInteger("fbaReturnTotalAmount"));
                        inventory.setStockQuantity(dataitem.getInteger("productTotalAmount"));
                        JSONObject productStockDtl = dataitem.getJSONObject("productStockDtl");
                        if (productStockDtl != null) {
                            inventory.setStockAvailable(productStockDtl.getInteger("availableAmount"));
                            inventory.setStockLock(productStockDtl.getInteger("lockAmount"));
                            inventory.setStockTransport(productStockDtl.getInteger("transportAmount"));
                        }
                        JSONObject fbaReturnStockDtl = dataitem.getJSONObject("fbaReturnStockDtl");
                        if (fbaReturnStockDtl != null) {
                            inventory.setFbaAvailable(fbaReturnStockDtl.getInteger("availableAmount"));
                            inventory.setFbaLock(fbaReturnStockDtl.getInteger("lockAmount"));
                            inventory.setFbaTransport(fbaReturnStockDtl.getInteger("transportAmount"));
                        }
                        inventory.setName(dataitem.getString("productName"));
                        inventory.setHousename(dataitem.getString("whName"));
                        LambdaQueryWrapper<ThirdPartyWarehouseInventoryXL> queryOne = new LambdaQueryWrapper<ThirdPartyWarehouseInventoryXL>();
                        queryOne.eq(ThirdPartyWarehouseInventoryXL::getHouseid, houseId);
                        queryOne.eq(ThirdPartyWarehouseInventoryXL::getSku, sku);
                        queryOne.eq(ThirdPartyWarehouseInventoryXL::getApi, api.getId());
                        queryOne.eq(ThirdPartyWarehouseInventoryXL::getShopid, api.getShopid());
                        ThirdPartyWarehouseInventoryXL one=thirdPartyWarehouseInventoryXLMapper.selectOne(queryOne);
                        if(one==null){
                            thirdPartyWarehouseInventoryXLMapper.insert(inventory);
                        }else{
                            thirdPartyWarehouseInventoryXLMapper.update(inventory,queryOne);
                        }
                        if (warehouse != null) {
                            ThirdPartyWarehouseBind bind = this.iThirdPartyWarehouseBindService.lambdaQuery().eq(ThirdPartyWarehouseBind::getThirdpartyWarehouseId, warehouse.getId()).one();
                            if (bind != null) {
                                OrderListing ol = new OrderListing();
                                ol.setShopid(api.getShopid());
                                ol.setWarehouseid(bind.getLocalWarehouseId());
                                ol.setSku(inventory.getSku());
                                ol.setName(dataitem.getString("productName"));
                                ol.setQty(inventory.getStockQuantity());
                                LambdaQueryWrapper<OrderListing> oquery = new LambdaQueryWrapper<OrderListing>();
                                oquery.eq(OrderListing::getShopid, ol.getShopid());
                                oquery.eq(OrderListing::getWarehouseid, bind.getLocalWarehouseId());
                                oquery.eq(OrderListing::getSku, ol.getSku());
                                OrderListing orderListing = orderListingMapper.selectOne(oquery);
                                if (orderListing == null) {
                                    orderListingMapper.insert(ol);
                                } else {
                                    orderListing.setShopid(api.getShopid());
                                    orderListing.setWarehouseid(bind.getLocalWarehouseId());
                                    orderListing.setSku(inventory.getSku());
                                    orderListing.setName(dataitem.getString("productName"));
                                    orderListing.setQty(inventory.getStockQuantity());
                                    orderListingMapper.update(orderListing, oquery);
                                }
                            }
                        }
                    }
                }
                if (StrUtil.isNotBlank(dto.getAction()) && dto.getAction().equals("sync")&&warehouse!=null) {
                    iThirdPartyWarehouseBindService.asyncLocalInventory(api.getShopid(), warehouse.getId(), qtyMap);
                }
                if(dto.getCurrentpage()<totalPages){
                    dto.setCurrentpage(dto.getCurrentpage()+1);
                    searchStock(  api,   dto);
                }
            }
        }
        return thirdPartyWarehouseInventoryXLMapper.findByDto(dto);

    }
    // 辅助方法设置Warehouse属性，提高代码复用性和清晰度
    private void setWarehouseProperties(ThirdPartyWarehouse warehouse, JSONObject data, ThirdPartyAPI api) {
        warehouse.setCode(data.getString("whCode"));
        warehouse.setName(data.getString("whNameCn"));
        warehouse.setCountry(data.getString("countryCode"));
        warehouse.setApi(api.getId());
        warehouse.setShopid(api.getShopid());
        warehouse.setExt(data.getString("channelDatas"));
    }

    public List<ThirdPartyWarehouse> searchStartHouse(ThirdPartyAPI api) throws HttpException {
        String url=api.getApi()+postMethod+"/openapi/v1/warehouse/options";
        Map<String,Object> param=getParam(api,null);
        String authcode=null;
        try {
              authcode=Md5Srv2.geAuthCode(api.getAppkey(),api.getAppsecret(),Long.parseLong(param.get("reqTime").toString()),null);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        String response=  HttpClientUtil.postUrl(url+"?authcode="+ authcode,JSONUtil.parse(param).toString(),getHeader());
        JSONObject myresult = GeneralUtil.getJsonObject(response);
        JSONArray returnDatas =myresult!=null?myresult.getJSONArray("data"):null;
        List<ThirdPartyWarehouse> list=new ArrayList<ThirdPartyWarehouse>();
        for(int i=0;returnDatas!=null&&i<returnDatas.size();i++){
            JSONObject data = (JSONObject)returnDatas.get(i);
            LambdaQueryWrapper<ThirdPartyWarehouse> query=new LambdaQueryWrapper<ThirdPartyWarehouse>();
            query.eq(ThirdPartyWarehouse::getCode,data.getString("whCode"));
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

    private String getAuth(JSONObject param  ,ThirdPartyAPI api) {
        String message=param.getString("appKey");
        message =message+(param.containsKey("data")?param.getJSONObject("data").toJSONString():"");
        message =message+param.getString("reqTime");
        String key=api.getAppsecret();
        key=encrypt(message,key);
        return "?authcode="+key;
    }
    private   String encrypt(String message, String secretKey){
        Key key = new SecretKeySpec(secretKey.getBytes(), "");
        byte[] shaDigest = null;
        try {
            shaDigest = mac("HmacSHA256", key, message.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Hex.encodeHexString(shaDigest);
    }
    public   byte[] mac(String algorithm, Key key, byte[] data) throws Exception {
        Mac mac = Mac.getInstance(algorithm);
        //这里是关键，需要一个key（这里就是和普通的消息摘要的区别点）
        mac.init(key);
        byte[] result = mac.doFinal(data);
        return result;
    }

    @Override
    public List<ThirdPartyWarehouse> listByApi(ThirdPartyAPI api) {
        LambdaQueryWrapper<ThirdPartyWarehouse> query=new LambdaQueryWrapper<ThirdPartyWarehouse>();
        query.eq(ThirdPartyWarehouse::getApi,api.getId());
        query.eq(ThirdPartyWarehouse::getShopid,api.getShopid());
        return thirdPartyWarehouseMapper.selectList(query);
    }

    @Override
    public JSONObject getOrder(ThirdPartyAPI api, ThirdpartyWarehouseInvDTO dto)  {
        String houseId=dto.getHouseid();
        String sku=dto.getSku();
        String url=api.getApi()+postMethod+"/openapi/v1/integratedInventory/pageStockFlow";
        JSONObject data=new JSONObject();
        if(StrUtil.isNotBlank(dto.getHouseid())){
            data.put("whCodeList",houseId);
        }
        if(StrUtil.isNotBlank(sku)){
            data.put("skuList",sku);
        }
        data.put("page",dto.getCurrentpage());
        data.put("pageSize",dto.getPagesize()>100?100:dto.getPagesize());
         int totalPages=0;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        String createdStartTime=dto.getCreatedStartTime()==null?GeneralUtil.formatDate(cal.getTime(), "yyyy-MM-dd"):dto.getCreatedStartTime();
        String createdEndTime=dto.getCreatedEndTime();
        if(StrUtil.isNotBlank(createdStartTime)){
            data.put("startTime",createdStartTime);
        }
        Map<String,Object> param=getParam(api,data);
        String authcode=null;
        try {
            authcode=Md5Srv2.geAuthCode(api.getAppkey(),api.getAppsecret(),Long.parseLong(param.get("reqTime").toString()),param);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        String response= null;
        try {
            response = HttpClientUtil.postUrl(url+"?authcode="+authcode, JSONUtil.parse(param).toString(),getHeader());
        } catch (HttpException e) {
            throw new RuntimeException(e);
        }
        JSONObject myResult =response!=null? GeneralUtil.getJsonObject(response):null;
        if(myResult!=null&&myResult.containsKey("data")) {
            JSONObject dataResult = myResult.getJSONObject("data");
            if (dataResult.containsKey("records")) {
                JSONArray records = dataResult.getJSONArray("records");
                totalPages=dataResult.getInteger("pages");
                for (int j = 0; j < records.size(); j++) {
                    JSONObject item = records.getJSONObject(j);
                    String warehouseCode = item.getString("whCode");
                    String warehouseName = item.getString("whName");
                    ThirdPartyWarehouse warehouse = null;
                    if (StrUtil.isNotBlank(warehouseCode)) {
                        warehouse = thirdPartyWarehouseMapper.selectOne(new LambdaQueryWrapper<ThirdPartyWarehouse>()
                                .eq(ThirdPartyWarehouse::getCode, warehouseCode)
                                .eq(ThirdPartyWarehouse::getApi, api.getId())
                                .eq(ThirdPartyWarehouse::getShopid, api.getShopid()));
                    }
                    if (warehouse == null) {
                        warehouse = thirdPartyWarehouseMapper.selectOne(new LambdaQueryWrapper<ThirdPartyWarehouse>()
                                .eq(ThirdPartyWarehouse::getName, warehouseName)
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
                    String platform = item.getString("relateOrderTypeName");
                    if(platform!=null&&!platform.equals("出库单")){continue;}
                    String poNum = item.getString("relateOrderNo");
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
                        platformid = orderPlatform.getId();
                        sku = item.getString("sku");
                        Date createdTime = item.getDate("operateTime");
                        Integer quantity = item.getInteger("changeAmount")*-1;
                        if(quantity<0){
                            continue;
                        }
                        BigDecimal usdValuePerUnit = item.getBigDecimal("usdValuePerUnit");
                        LambdaQueryWrapper<Order> orderQuery = new LambdaQueryWrapper<Order>();
                        orderQuery.eq(Order::getShopid, api.getShopid());
                        orderQuery.eq(Order::getPlatformId, platformid);
                        orderQuery.eq(Order::getOrderId, poNum);
                        orderQuery.eq(Order::getSku, sku);
                        Order orderObject = orderService.getOne(orderQuery);
                        if (orderObject == null) {
                            orderObject = new Order();
                            orderObject.setShopid(api.getShopid());
                            orderObject.setPlatformId(platformid);
                            orderObject.setOrderId(poNum);
                            orderObject.setSku(sku);
                            if (warehouse != null) {
                                orderObject.setCountry(warehouse.getCountry());
                            }
                            orderObject.setWarehouseid(warehouseid);
                            orderObject.setIsout(true);
                            orderObject.setThirdpartyWarehouseid(warehouse.getId());

                            orderObject.setQuantity(quantity);
                            orderObject.setPrice(usdValuePerUnit);
                            orderObject.setPurchaseDate(createdTime);
                            orderObject.setShipFee(BigDecimal.ZERO);
                            orderObject.setReferralFee(BigDecimal.ZERO);
                            orderObject.setReferralRate(BigDecimal.ZERO);
                            orderObject.setOperator(api.getOperator() != null ? api.getOperator().toString() : null);
                            orderObject.setOpttime(new Date());
                            orderService.save(orderObject);
                        } else {
                            orderObject.setShopid(api.getShopid());
                            orderObject.setPlatformId(platformid);
                            orderObject.setOrderId(poNum);
                            orderObject.setSku(sku);
                            if (warehouse != null) {
                                orderObject.setCountry(warehouse.getCountry());
                            }
                            orderObject.setWarehouseid(warehouseid);
                            orderObject.setIsout(true);
                            orderObject.setThirdpartyWarehouseid(warehouse.getId());
                            orderObject.setQuantity(quantity);
                            orderObject.setPrice(usdValuePerUnit);
                            orderObject.setPurchaseDate(createdTime);
                            orderObject.setShipFee(BigDecimal.ZERO);
                            orderObject.setReferralFee(BigDecimal.ZERO);
                            orderObject.setReferralRate(BigDecimal.ZERO);
                            orderObject.setOperator(api.getOperator() != null ? api.getOperator().toString() : null);
                            orderObject.setOpttime(new Date());
                            orderService.update(orderObject, orderQuery);
                        }
                        if (StrUtil.isNotBlank(warehouseid)) {
                            OrderListing ol = new OrderListing();
                            ol.setShopid(api.getShopid());
                            ol.setCountry(orderObject.getCountry());
                            ol.setWarehouseid(warehouseid);
                            ol.setSku(orderObject.getSku());
                            ol.setName(item.getString("productName"));
                            LambdaQueryWrapper<OrderListing> query = new LambdaQueryWrapper<OrderListing>();
                            query.eq(OrderListing::getShopid, ol.getShopid());
                            query.eq(OrderListing::getWarehouseid, ol.getWarehouseid());
                            query.eq(OrderListing::getSku, ol.getSku());
                            OrderListing orderListing = orderListingMapper.selectOne(query);
                            if (orderListing == null) {
                                orderListingMapper.insert(ol);
                            } else {
                                orderListing.setShopid(api.getShopid());
                                orderListing.setCountry(orderObject.getCountry());
                                orderListing.setWarehouseid(warehouseid);
                                orderListing.setSku(orderObject.getSku());
                                orderListing.setName(item.getString("productName"));
                                orderListingMapper.update(orderListing, query);
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

        return null;
    }
}
