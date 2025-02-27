package com.wimoor.erp.util;
 

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 对外接口请求封装 主流语言封装也要保持参数方法一致
 *
 * @author adu
 */
public class ZmRequest {

    private String appKey;
    private String appSecret;
    private String nonce;
    private String version;

    // token 需要在实例化后和失效后 （getToken方法）手动传入
    private String token;
    private String ts;

    private Integer timeOut = 18000;// 毫秒

    // 调用接口需要的参数，调用接口前一定要设置该参数，如果接口不需要参数 要求置空
    Map<String, Object> datas;

    /**
     * 哲盟发放授权格式：
     * 客户代号:XX0000
     * 账号:XXXXXXX
     * 秘钥:XXXXXXXXXXXXXXXXXXXXXXXXXXXXX
     * 指定路线:义乌FEDEX-X-IP,义乌FEDEX-A-IP
     *
     * @param appKey    账号
     * @param appSecret 秘钥
     * @param nonce     如：slnkda   可以随机
     * @param version   默认1.0
     */
    public ZmRequest(String appKey, String appSecret, String nonce, String version) {
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.ts = this.getTimeStamp();
        this.nonce = nonce;
        this.version = version;
    }

    // 一定要设置
    public void setToken(String token) {
        this.token = token;
    }

    // 一定要设置
    public void setDatas(Map<String, Object> datas) {
        this.datas = datas;
    }

    // 获取 标准token
    @SuppressWarnings("unchecked")
	public String getToken(String url) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("appKey", appKey);
        params.put("appSecret", appSecret);
        String result = http(url, params, null, "POST");
        if (ZmUtils.isNotBlank(result)) {
            Map<String, Object> resultMap = ZmUtils.json2Object(result, Map.class);
            Integer code = ZmUtils.getInteger(resultMap, "result_code");
            if (null != code && code == 0) {
                String token = ZmUtils.getString(resultMap, "body");
                return token;
            } else {
                System.out.println(result);
                throw new RuntimeException(ZmUtils.getString(resultMap, "message"));
            }
        } else {
            throw new RuntimeException("返回空数据");
        }
    }

    /**
     * 获取头信息
     *
     * @return
     */
    public Map<String, String> getHeaders() {
        System.out.println("时间戳:" + this.ts);
        System.out.println("原始token:" + this.token);
        // 2 获取伪装token
        String newToken = this.camouflageToken();
        System.out.println("伪装后的token:" + newToken);
        // 3 计算签名
        String sign = this.genSign(this.datas);
        System.out.println("计算后的签名:" + sign);

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("token", newToken);
        headers.put("version", version);
        headers.put("sign", sign);
        return headers;
    }

    /**
     * 获取时间戳 主要是和.net 保持一致
     *
     * @return
     */
    public String getTimeStamp() {
        return String.valueOf(System.currentTimeMillis());
    }

    /**
     * 主要是和.net 保持一致,实际java版本不需要
     *
     * @param str
     * @return
     */
    public String getMd5(String str) {
        return null;
    }

    /**
     * 伪装token
     *
     * @return
     */
    public String camouflageToken() {
        // 从POS获取原始token
        // String posOriToken = getPosPrintSourceToken(appKey, appSecret);
        System.out.println("从pos获取的原始token：{}" + this.token);
        if (ZmUtils.isBlank(this.token)) {
            throw new RuntimeException("获取token异常");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("timestamp", this.ts);
        map.put("nonce", nonce);
        map.put("token", this.token);
        System.out.println("待生成新token的参数 ：{}" + map.toString());
        String newTokenJson = ZmUtils.toJson(map);
        String newToken = ZmUtils.encodeMixChar(newTokenJson);
        System.out.println("最终生成的新token：{}" + newToken);
        return newToken;
    }

    /**
     * 生成签名
     *
     * @param pds
     * @return
     */
    public String genSign(Map<String, Object> pds) {
        Map<String, Object> signMap = new HashMap<String, Object>();
        if (null != pds && pds.size() > 0) {
            for (Map.Entry<String, Object> entry : pds.entrySet()) {
                signMap.put(entry.getKey(), entry.getValue());
            }
        }
        signMap.put("version", version);
        signMap.put("nonce", nonce);
        signMap.put("timestamp", this.ts);
        signMap.put("token", this.token);
        String sign = ZmUtils.sign(signMap, appSecret);
        return sign;
    }

    /**
     * 暴露哲盟接口调用方法
     *
     * @param url
     * @param method
     * @return
     */
    public String request(String url, String method) {
        Map<String, String> headers = this.getHeaders();
        String result = "";
        result = this.http(url, this.datas, headers, method);
        return result;
    }

    /**
     * POST请求
     *
     * @param requestUrl 请求地址
     * @param params     请求数据
     * @return
     */
    private String http(String requestUrl, Map<String, Object> params, Map<String, String> headers, String method) {
        String param = "";

        if (null != params) {
            param = convertParams(params);
            System.out.println("path:" + requestUrl + "?" + param);
        }
        HttpURLConnection connection = null;
        InputStream is = null;
        OutputStream os = null;
        BufferedReader br = null;
        String result = null;
        try {
            /** 创建远程url连接对象 */
            URL url = new URL(requestUrl);
            /** 通过远程url对象打开一个连接，强制转换为HttpUrlConnection类型 */
            connection = (HttpURLConnection) url.openConnection();
            /** 设置连接方式：POST */
            connection.setRequestMethod(method.toUpperCase());
            connection.setConnectTimeout(timeOut);

            connection.setReadTimeout(timeOut);
            /** 设置是否向httpUrlConnection输出，设置是否从httpUrlConnection读入，此外发送post请求必须设置这两个 */
            // 默认值为：false，当向远程服务器传送数据/写数据时，需要设置为true
            connection.setDoOutput(true);
            // 默认值为：true，当前向远程服务读取数据时，设置为true，该参数可有可无
            connection.setDoInput(true);
            /** 设置通用的请求属性 */
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 设置传入参数的格式:请求参数应该是 name1=value1&name2=value2 的形式
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            if (null != headers) {
                Set<String> set = headers.keySet();
                if (null != set && set.size() > 0) {
                    for (String key : set) {
                        String value = headers.get(key);
                        connection.setRequestProperty(key, value);
                    }
                }
            }
            /** 通过连接对象获取一个输出流 */
            os = connection.getOutputStream();
            /** 通过输出流对象将参数写出去/传输出去，它是通过字节数组写出的 */
            // 若使用os.print(param);则需要释放缓存：os.flush();即使用字符流输出需要释放缓存，字节流则不需要
            if (param != null && param.length() > 0) {
                os.write(param.getBytes());
            }
            /** 请求成功：返回码为200 */
            if (connection.getResponseCode() == 200) {
                /** 通过连接对象获取一个输入流，向远程读取 */
                is = connection.getInputStream();
                /** 封装输入流is，并指定字符集 */
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                /** 存放数据 */
                StringBuffer sbf = new StringBuffer();
                String line = null;
                while ((line = br.readLine()) != null) {
                    sbf.append(line);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            /** 关闭资源 */
            try {
                if (null != br) {
                    br.close();
                }
                if (null != is) {
                    is.close();
                }
                if (null != os) {
                    os.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            /** 关闭远程连接 */
            // 断开连接，最好写上，disconnect是在底层tcp socket链接空闲时才切断。如果正在被其他线程使用就不切断。
            // 固定多线程的话，如果不disconnect，链接会增多，直到收发不出信息。写上disconnect后正常一些
            connection.disconnect();
        }
        return result;
    }

    /**
     * map参数转 url 编码数据
     *
     * @param params
     * @return
     * @throws UnsupportedEncodingException
     */
    private String convertParams(Map<String, Object> params) {
        try {
            // 建立输入流，向指向的URL传入参数
            String queryString = "";
            if (params != null) {
                Set<Entry<String, Object>> entrySet = params.entrySet();
                for (Entry<String, Object> entry : entrySet) {
                    if (queryString.length() > 0)
                        queryString = queryString + "&";
                    queryString += entry.getKey() + "=" + URLEncoder.encode(entry.getValue().toString(), "utf-8");
                }
            }
            return queryString;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
