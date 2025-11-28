package com.wimoor.amazon.transparency.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.product.service.IProductInfoService;
import com.wimoor.amazon.transparency.mapper.TransparencyTcodeMapper;
import com.wimoor.amazon.transparency.pojo.dto.TransparencyDTO;
import com.wimoor.amazon.transparency.pojo.entity.TransparencyAuthority;
import com.wimoor.amazon.transparency.pojo.entity.TransparencySkuinfo;
import com.wimoor.amazon.transparency.pojo.entity.TransparencyTask;
import com.wimoor.amazon.transparency.pojo.entity.TransparencyTcode;
import com.wimoor.amazon.transparency.service.ITransparencyAuthorityService;
import com.wimoor.amazon.transparency.service.ITransparencySkuinfoService;
import com.wimoor.amazon.transparency.service.ITransparencyTaskService;
import com.wimoor.amazon.transparency.mapper.TransparencyTaskMapper;
import com.wimoor.common.HttpClientUtil;
import com.wimoor.common.user.UserInfo;
import org.apache.http.HttpException;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
* @author liufei
* @description 针对表【t_amz_transparency_task】的数据库操作Service实现
* @createDate 2025-08-08 11:31:24
*/
@Service
public class TransparencyTaskServiceImpl extends ServiceImpl<TransparencyTaskMapper, TransparencyTask>
    implements ITransparencyTaskService {
    @Autowired
    IProductInfoService iProductInfoService;
    @Autowired
    ITransparencyAuthorityService iTransparencyAuthorityService;
    @Autowired
     ITransparencySkuinfoService transparencySkuinfoService;
    @Autowired
    TransparencyTcodeMapper transparencyTcodeMapper;
    @Override
    public TransparencyTask saveTask(UserInfo userinfo, TransparencyTask dto) {
              TransparencySkuinfo info = transparencySkuinfoService.getById(dto.getSkuinfoid());
              TransparencyAuthority auth = iTransparencyAuthorityService.getById(info.getAuthid());
              String response = iTransparencyAuthorityService.sgtin(auth, dto.getGtin(), dto.getCount());
              dto.setLocation(response);
                if(StrUtil.isNotBlank(response)){
                    String[] urls = response.split("/");
                    if(urls.length >0){
                        dto.setJobid(urls[urls.length-1]);
                    }
                }
              dto.setStatus("INPROCESS");
              dto.setCreator(userinfo.getId());
              dto.setCreatetime(new Date());
              this.baseMapper.insert(dto);
              return dto;
    }

    @Override
    public IPage<Map<String, Object>> listTask(UserInfo userinfo, TransparencyDTO dto) {
        dto.setShopid(userinfo.getCompanyid());
        IPage<Map<String, Object>> page = this.baseMapper.listTask(dto.getPage(), dto);
        if(page!=null&&page.getRecords()!=null){
            for(Map<String, Object> map : page.getRecords()){
                String sku=map.get("sku").toString();
                String groupid=map.get("groupid").toString();
                Map<String, Object> item = iProductInfoService.findNameAndPicture(sku, null, groupid);
                if(item!=null){
                    map.putAll(item);
                }
            }
        }
        return page;
    }

    @Override
    public TransparencyTask refreshTask(UserInfo userinfo, TransparencyTask dto) {
        TransparencySkuinfo info = transparencySkuinfoService.getById(dto.getSkuinfoid());
        TransparencyAuthority auth = iTransparencyAuthorityService.getById(info.getAuthid());
        String url=null;
        if(dto.getStatus()==null||!dto.getStatus().equals("COMPLETED")){
            String response = iTransparencyAuthorityService.job(auth,dto.getLocation());
            JSONObject jsonObject = JSONObject.parseObject(response);
            String status=jsonObject.getString("status");
            url=jsonObject.getString("url");
            dto.setStatus(status);
            dto.setUrl(url);
            this.baseMapper.updateById(dto);
        }else{
            url=dto.getUrl();
        }
        if(dto.getStatus().equals("COMPLETED")&&url!=null){
            Map<String,String> header=new HashMap<String,String>();
            header.put(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded;charset=utf-8");
            try {
                String responseTCode=HttpClientUtil.getUrl(dto.getUrl(),header);
                JSONObject jsonObject = JSONObject.parseObject(responseTCode);
                String codeRequestId=jsonObject.getString("codeRequestId");
                JSONArray jsonTcode = jsonObject.getJSONArray("codesList");
                for(int i=0;i<jsonTcode.size();i++){
                    JSONObject tcode = jsonTcode.getJSONObject(i);
                    String gtin=tcode.getString("gtin");
                    String sku=tcode.getString("sku");
                    String typeOfCodes=tcode.getString("typeOfCodes");
                    JSONArray codes=tcode.getJSONArray("codes");
                    for(int j=0;j<codes.size();j++){
                        String code=codes.getString(j);
                        TransparencyTcode ttc=new TransparencyTcode();
                        ttc.setTaskid(dto.getId());
                        ttc.setTcode(code);
                        ttc.setSku(sku);
                        ttc.setGtin(gtin);
                        ttc.setJobid(codeRequestId);
                        ttc.setTypeOfCodes(typeOfCodes);
                        ttc.setCreatetime(new Date());
                        ttc.setOperator(userinfo.getId());
                        TransparencyTcode old=transparencyTcodeMapper.selectById(code);
                        if(old!=null){
                            transparencyTcodeMapper.updateById(ttc);
                        }else{
                            transparencyTcodeMapper.insert(ttc);
                        }
                    }
                }
            } catch (HttpException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        return dto;
    }



}




