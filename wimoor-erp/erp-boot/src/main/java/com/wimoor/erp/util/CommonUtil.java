package com.wimoor.erp.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.apache.commons.httpclient.methods.PostMethod;
 
 

/**
 * 通用工具类，包括了签名工具、url拼装以及httpResponse的解析
 */
public final class CommonUtil {

    /**
     * 解析http请求的response
     * @param method
     * @return 请求结果
     * @throws IOException
     */
    public static String parserResponse(PostMethod method) throws IOException{
        StringBuffer contentBuffer = new StringBuffer();
        InputStream in = method.getResponseBodyAsStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, method.getResponseCharSet()));
        String inputLine = null;
        while((inputLine = reader.readLine()) != null)
        {
            contentBuffer.append(inputLine);
            contentBuffer.append("/n");
        }
        //去掉结尾的换行符
        contentBuffer.delete(contentBuffer.length() - 2, contentBuffer.length());
        in.close();
        return contentBuffer.toString();
    }
    
    /**
     * 将urlPath和请求参数同时作为签名因子进行签名
     * @param urlPath protocol/version/namespace/name/appKey
     * @param params api请求的各参数键值对
     * @param appSecretKey app签名密钥
     * @return
     */
    public static String signatureWithParamsAndUrlPath(String urlPath, Map<String, String> params, String appSecretKey){
        List<String> paramValueList = new ArrayList<String>();
        if(params != null){
            for (Map.Entry<String, String> entry : params.entrySet()) {
                paramValueList.add(entry.getKey() + entry.getValue());
            }
        }
        final String[] datas = new String[1 + paramValueList.size()];
        datas[0] = urlPath;
        Collections.sort(paramValueList);
        for (int i = 0; i < paramValueList.size(); i++) {
            datas[i+1] = paramValueList.get(i);
        }
        byte[] signature = SecurityUtil.hmacSha1(datas, StringUtil.toBytes(appSecretKey));
        return StringUtil.encodeHexStr(signature);
    }
    
    /**
     * 
     * 仅将请求参数作为签名因子进行签名
     * @param params api请求的各参数键值对
     * @param appSecretKey
     * @return
     */
    public static String signatureWithParamsOnly(Map<String, String> params, String appSecretKey){
        List<String> paramValueList = new ArrayList<String>();
        if(params != null){
            for (Map.Entry<String, String> entry : params.entrySet()) {
                paramValueList.add(entry.getKey() + entry.getValue());
            }
        }
        Collections.sort(paramValueList);
        String[] datas = new String[paramValueList.size()];
        paramValueList.toArray(datas);
        byte[] signature = SecurityUtil.hmacSha1(datas, StringUtil.toBytes(appSecretKey));
        return StringUtil.encodeHexStr(signature);
    }
    
    /**
     * 生成api签名的urlPath，即protocol/version/namespace/name/appKey
     * @param apiNamespace
     * @param apiName
     * @param apiVersion
     * @param protocol
     * @param appKey
     * @return
     */
    public static String buildInvokeUrlPath(String apiNamespace, String apiName, int apiVersion, String protocol, String appkey) {
        String url = protocol + "/" + apiVersion + "/" + apiNamespace + "/" + apiName + "/" + appkey;
        return url;
    }
    
    /**
     * 获取完整的url
     * @param url 请求uri
     * @param params 请求参数
     * @return
     */
    public static String getWholeUrl(String url, Map<String, String> params){
        if(url == null){
            return null;
        }
        if(params == null){
            return url;
        }
        Set<Map.Entry<String, String>> set = params.entrySet();
        if(set.size() <= 0){
            return url;
        }
        url += "?";
        Iterator<Map.Entry<String, String>> it = set.iterator();
        if(it.hasNext()){
            Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
            String param = entry.getKey() + "=" + entry.getValue();
            url += param;
        }
        while(it.hasNext()){
            Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
            String param = entry.getKey() + "=" + entry.getValue();
            url += "&" + param;
        }
        return url;
    }
    
    private CommonUtil(){
    }

}
					