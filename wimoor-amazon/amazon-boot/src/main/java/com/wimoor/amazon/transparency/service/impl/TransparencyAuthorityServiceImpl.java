package com.wimoor.amazon.transparency.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.transparency.pojo.entity.TransparencyAuthority;
import com.wimoor.amazon.transparency.pojo.entity.TransparencyTask;
import com.wimoor.amazon.transparency.service.ITransparencyAuthorityService;
import com.wimoor.amazon.transparency.mapper.TransparencyAuthorityMapper;
import com.wimoor.amazon.util.HttpUtils;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.HttpClientUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;
import lombok.RequiredArgsConstructor;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
* @author liufei
* @description 针对表【t_amz_transparency_authority】的数据库操作Service实现
* @createDate 2025-08-07 10:19:02
*/
@Service
@RequiredArgsConstructor
public class TransparencyAuthorityServiceImpl extends ServiceImpl<TransparencyAuthorityMapper, TransparencyAuthority>
    implements ITransparencyAuthorityService{

    public TransparencyAuthority refreshToken(TransparencyAuthority auth) {
        String url="https://tpncy-web-services.auth.us-east-1.amazoncognito.com/oauth2/token";
        Map<String,String> map=new HashMap<String,String>();
        map.put("grant_type","client_credentials");
        map.put("client_id",auth.getClientId());
        map.put("client_secret",auth.getClientSecret());
        Map<String, String> header=new HashMap<String,String>();
        header.put("Content-Type","application/x-www-form-urlencoded");
        try {
            String response = HttpClientUtil.postUrl(url, map, header, URLEncodedUtils.CONTENT_TYPE);
            JSONObject result=GeneralUtil.getJsonObject(response);
            String access_token= result.getString("access_token");
            String expires_in= result.getString("expires_in");
            auth.setToken(access_token);
            Calendar c= Calendar.getInstance();
            c.add(Calendar.MINUTE,30);
            auth.setExpiry(c.getTime());
            this.updateById(auth);
        } catch (HttpException e) {
            throw new RuntimeException(e);
        }
        return auth;
    }

    TransparencyAuthority getAuthority(TransparencyAuthority auth){
        if(auth.getExpiry().before(new Date())){
            return refreshToken(auth);
        }else{
            return auth;
        }
    }
    TransparencyAuthority getAuthority(String authorityid){
        TransparencyAuthority auth = this.getById(authorityid);
        if(auth.getExpiry().after(new Date())){
            return refreshToken(auth);
        }else{
            return auth;
        }
    }
    @Override
    public String sgtin(TransparencyAuthority authority,String gtin,Integer count) {
        TransparencyAuthority auth =getAuthority(authority);
        String url="https://api.transparency.com/v1.2/serial/sgtin";
        JSONObject  map=new JSONObject();
        map.put("gtin",gtin);
        map.put("count",count);
        Map<String, String> header=new HashMap<String,String>();
        header.put("Content-Type","application/json");
        header.put("Authorization",auth.getToken());
        try {
            HttpResponse response = HttpClientUtil.postResponseUrl(url, map.toString(), header);
            if (HttpClientUtil.success_code.contains(response.getStatusLine().getStatusCode())) {
                Header responseHeader = response.getFirstHeader("Location");
                return responseHeader.getValue();
            }else{
                HttpEntity he = (response == null ? null : response.getEntity());
                String respContent = (he == null ? null : EntityUtils.toString(he, "UTF-8"));
                throw new BizException(respContent);
            }
        } catch (HttpException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TransparencyAuthority saveAuthority(UserInfo userinfo, TransparencyAuthority authority) {
            authority.setShopid(userinfo.getCompanyid());
            authority.setCreatetime(new Date());
            authority.setCreator(userinfo.getId());
            TransparencyAuthority  old=this.getById(authority.getClientId());
            if(old!=null){
                this.updateById(authority);
            }else{
                this.save(authority);
            }
            this.refreshToken(authority);
            return authority;
    }

    @Override
    public String job(TransparencyAuthority authority, String location) {
        TransparencyAuthority auth =getAuthority(authority);
        Map<String, String> header=new HashMap<String,String>();
        header.put("Content-Type","application/json");
        header.put("Authorization",auth.getToken());
        try {
            return HttpClientUtil.getUrl(location, header);
        } catch (HttpException e) {
            throw new RuntimeException(e);
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}




