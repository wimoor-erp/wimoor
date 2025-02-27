package com.wimoor.erp.thirdparty.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.HttpClientUtil;
import com.wimoor.common.result.Result;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.thirdparty.mapper.ThirdPartyAPIMapper;
import com.wimoor.erp.thirdparty.mapper.ThirdPartyWarehouseInventoryMapperK5;
import com.wimoor.erp.thirdparty.mapper.ThirdPartyWarehouseInventoryMapperOps;
import com.wimoor.erp.thirdparty.mapper.ThirdPartyWarehouseMapper;
import com.wimoor.erp.thirdparty.pojo.dto.ThirdpartyWarehouseInvDTO;
import com.wimoor.erp.thirdparty.pojo.entity.ThirdPartyAPI;
import com.wimoor.erp.thirdparty.pojo.entity.ThirdPartyWarehouse;
import com.wimoor.erp.thirdparty.pojo.entity.ThirdPartyWarehouseInventoryK5;
import com.wimoor.erp.thirdparty.pojo.entity.ThirdPartyWarehouseInventoryOps;
import com.wimoor.erp.thirdparty.service.IWarehouseOPSService;
import com.wimoor.erp.thirdparty.service.IWarehouseXLSService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Hex;
import org.apache.http.HttpException;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.*;

import static cn.hutool.crypto.digest.DigestAlgorithm.MD5;

@Service("warehouseXLSService")
@RequiredArgsConstructor
public class WarehouseXLServiceImpl implements IWarehouseXLSService {
    final ThirdPartyAPIMapper thirdPartyAPIMapper;
    final ThirdPartyWarehouseMapper thirdPartyWarehouseMapper;
    final ThirdPartyWarehouseInventoryMapperK5 thirdPartyWarehouseInventoryK5Mapper;
    final IMaterialService materialService;
    final static  String postMethod="";
    public Map<String,String> getHeader(){
        Map<String,String> header=new HashMap<String,String>();
        header.put("Content-Type","application/json;charset=UTF-8");
        return header;
    }
    public Map<String,Object> getParam(ThirdPartyAPI api, JSONObject data){
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("appKey",api.getAppkey());
        param.put("data",data);
        long unixTimestamp = Instant.now().getEpochSecond();
        param.put("reqTime",unixTimestamp);
        return param;
    }
    public Object searchStock(ThirdPartyAPI api, ThirdpartyWarehouseInvDTO dto) throws HttpException {
        String houseId=dto.getHouseid();
        String sku=dto.getSku();
        String url=api.getApi()+postMethod+"/openapi/v1/integratedInventory/pageOpen";
        JSONObject data=new JSONObject();
        if(StrUtil.isNotBlank(dto.getHouseid())){
            data.put("whCodeList",houseId);
        }
        if(StrUtil.isNotBlank(sku)){
            data.put("skuList",sku);
        }
        data.put("page",dto.getCurrentpage());
        data.put("pageSize",dto.getPagesize());
        Map<String,Object> param=getParam(api,data);
        String authcode=null;
        try {
            authcode=Md5Srv2.geAuthCode(api.getAppkey(),api.getAppsecret(),Long.parseLong(param.get("reqTime").toString()),param);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        String response=  HttpClientUtil.postUrl(url+"?authcode="+authcode, JSONUtil.parse(param).toString(),getHeader());
        JSONObject myresult = GeneralUtil.getJsonObject(response);
        if(myresult!=null&&myresult.containsKey("data")){
            JSONObject resultdata = myresult.getJSONObject("data");
            if(resultdata!=null&&resultdata.containsKey("records")){
                JSONArray records = resultdata.getJSONArray("records");
                for(int i=0;i<records.size();i++){
                    JSONObject dataitem = (JSONObject)records.get(i);
                    String itemsku=dataitem.getString("sku");
                    Map<String,Object> mparam=new HashMap<String,Object>();
                    mparam.put("skulist",Arrays.asList(itemsku));
                    mparam.put("shopid", api.getShopid());
                    Map<String, Object> result = materialService.findMaterialMapBySku(mparam);
                    if(result!=null&&result.get(itemsku)!=null){
                        dataitem.put("info",result.get(itemsku));
                    }

                }
            }
        }
        return myresult;

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
        String response=  HttpClientUtil.postUrl(url+"?authcode="+ authcode,param.toString(),getHeader());
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

}
