package com.wimoor.erp.thirdparty.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.HttpClientUtil;
import com.wimoor.erp.thirdparty.mapper.ThirdPartyAPIMapper;
import com.wimoor.erp.thirdparty.mapper.ThirdPartyWarehouseInventoryMapperOps;
import com.wimoor.erp.thirdparty.mapper.ThirdPartyWarehouseMapper;
import com.wimoor.erp.thirdparty.pojo.dto.ThirdpartyWarehouseInvDTO;
import com.wimoor.erp.thirdparty.pojo.entity.ThirdPartyAPI;
import com.wimoor.erp.thirdparty.pojo.entity.ThirdPartyWarehouseInventoryK5;
import com.wimoor.erp.thirdparty.pojo.entity.ThirdPartyWarehouse;
import com.wimoor.erp.thirdparty.pojo.entity.ThirdPartyWarehouseInventoryOps;
import com.wimoor.erp.thirdparty.service.IWarehouseOPSService;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("warehouseOPSService")
@RequiredArgsConstructor
public class WarehouseOPSServiceImpl implements IWarehouseOPSService {
    final ThirdPartyAPIMapper thirdPartyAPIMapper;
    final ThirdPartyWarehouseMapper thirdPartyWarehouseMapper;
    final ThirdPartyWarehouseInventoryMapperOps thirdPartyWarehouseInventoryOpsMapper;
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
                inventory.setHousename(warehouse.getName());
                    thirdPartyWarehouseInventoryOpsMapper.insert(inventory);
            }
        }

        return thirdPartyWarehouseInventoryOpsMapper.findByDto(dto);
    }
    // 辅助方法设置Warehouse属性，提高代码复用性和清晰度
    private void setWarehouseProperties(ThirdPartyWarehouse warehouse, JSONObject data, ThirdPartyAPI api) {
        warehouse.setCode(data.getString("code"));
        warehouse.setName(data.getString("name"));
        warehouse.setCountry(data.getString("whType"));
        warehouse.setApi(api.getId());
        warehouse.setShopid(api.getShopid());
        warehouse.setExt(data.getString("address"));
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
