package com.wimoor.erp.thirdparty.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.HttpClientUtil;
import com.wimoor.erp.thirdparty.mapper.ThirdPartyAPIMapper;
import com.wimoor.erp.thirdparty.mapper.ThirdPartyWarehouseInventoryMapperK5;
import com.wimoor.erp.thirdparty.mapper.ThirdPartyWarehouseMapper;
import com.wimoor.erp.thirdparty.pojo.dto.ThirdpartyWarehouseInvDTO;
import com.wimoor.erp.thirdparty.pojo.entity.ThirdPartyAPI;
import com.wimoor.erp.thirdparty.pojo.entity.ThirdPartyWarehouseInventoryK5;
import com.wimoor.erp.thirdparty.pojo.entity.ThirdPartyWarehouse;
import com.wimoor.erp.thirdparty.service.IWarehouseK5Service;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("warehouseK5Service")
@RequiredArgsConstructor
public class WarehouseK5ServiceImpl implements IWarehouseK5Service {
    final ThirdPartyAPIMapper thirdPartyAPIMapper;
    final ThirdPartyWarehouseMapper thirdPartyWarehouseMapper;
    final ThirdPartyWarehouseInventoryMapperK5 thirdPartyWarehouseInventoryK5Mapper;
    final static  String postMethod="/PostInterfaceService?method=";
    public Map<String,String> getHeader(){
        Map<String,String> header=new HashMap<String,String>();
        header.put("Content-Type","application/json;charset=UTF-8");
        return header;
    }
    public JSONObject getParam(ThirdPartyAPI api){
        JSONObject param = new JSONObject();
        JSONObject verify = new JSONObject();
        verify.put("Clientid",api.getAppkey());
        verify.put("Token",api.getAppsecret());
        param.put("Verify",verify);
        return param;
    }
    public List<Map<String, Object>> searchStock(ThirdPartyAPI api, ThirdpartyWarehouseInvDTO dto) throws HttpException {
        String houseId=dto.getHouseid();
        String sku=dto.getSku();
        if(StrUtil.isNotBlank(dto.getAction())&&dto.getAction().equals("sync")){
            String url=api.getApi()+postMethod+"searchStock";
            JSONObject param=getParam(api);
            if(StrUtil.isNotBlank(houseId)){
                param.put("houseId",houseId);
            }
            if(StrUtil.isNotBlank(sku)){
                param.put("sku",sku);
            }
            String response=  HttpClientUtil.postUrl(url,param.toString(),getHeader());
            JSONObject myresult = GeneralUtil.getJsonObject(response);
            JSONArray returnDatas =myresult!=null?myresult.getJSONArray("returnDatas"):null;
            LambdaQueryWrapper<ThirdPartyWarehouseInventoryK5> query=new LambdaQueryWrapper<ThirdPartyWarehouseInventoryK5>();
            query.eq(ThirdPartyWarehouseInventoryK5::getApi,api.getId());
            query.eq(ThirdPartyWarehouseInventoryK5::getShopid,api.getShopid());
            if(StrUtil.isNotBlank(houseId)){
                query.eq(ThirdPartyWarehouseInventoryK5::getHouseid,houseId);
            }
            if(StrUtil.isNotBlank(sku)){
                query.eq(ThirdPartyWarehouseInventoryK5::getSku,sku);
            }

             thirdPartyWarehouseInventoryK5Mapper.delete(query);
            for(int i=0;returnDatas!=null&&i<returnDatas.size();i++){
                JSONObject data = (JSONObject)returnDatas.get(i);
                String itemsku=data.getString("sku");
                String houseid=data.getString("houseid");
                ThirdPartyWarehouseInventoryK5 inventory=new ThirdPartyWarehouseInventoryK5();
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
            }
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
        String url=api.getApi()+postMethod+"searchStartHouse";
        JSONObject param=getParam(api);
        String response=  HttpClientUtil.postUrl(url,param.toString(),getHeader());
        JSONObject myresult = GeneralUtil.getJsonObject(response);
        JSONArray returnDatas =myresult!=null?myresult.getJSONArray("returnDatas"):null;
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
