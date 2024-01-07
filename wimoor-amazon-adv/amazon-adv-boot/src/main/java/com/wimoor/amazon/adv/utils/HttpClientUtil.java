package com.wimoor.amazon.adv.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;


import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.common.service.IAmzAdvHttpClientResponseHandler;
import com.wimoor.amazon.adv.report.pojo.AmzAdvRequest;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;

import cn.hutool.core.util.StrUtil;
import tk.mybatis.mapper.util.StringUtil;

@SuppressWarnings("deprecation")
public class HttpClientUtil {
	public static final HashSet<Integer> success_code = new HashSet<Integer>() {
		/**
		* 
		*/
		private static final long serialVersionUID = 1L;
		{
			add(100);
			add(101);
			add(102);
			add(200);
			add(201);
			add(202);
			add(203);
			add(204);
			add(205);
			add(206);
			add(207);
			add(300);
			add(301);
			add(302);
			add(303);
			add(304);
			add(305);
			add(306);
			add(307);
		}
	};

	public static String handlerResponse(HttpResponse resp) throws BizException {
		String respContent = null;
		HttpEntity he = (resp == null ? null : resp.getEntity());
		 Header header = resp.getFirstHeader("x-amzn-ErrorType");
		try {
			respContent = (he == null ? null : EntityUtils.toString(he, "UTF-8"));
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (success_code.contains(resp.getStatusLine().getStatusCode())) {
			return respContent;
		} else {
			String errorcode = "";
			String errormsg = "";
			if(header!=null) {
				if(StrUtil.isEmpty(respContent)) {
					throw new BizException(header.getValue());
				}else {
					throw new BizException(respContent);
				}
			}
			if (StringUtil.isNotEmpty(respContent)) {
				JSONObject json = GeneralUtil.getJsonObject(respContent);
				JSONArray jsonarray = json == null ? GeneralUtil.getJsonArray(respContent) : null;
				json = jsonarray != null && jsonarray.size() > 0 ? jsonarray.getJSONObject(0) : json;
				if (json != null) {
					errorcode = json.getString("code");
					errormsg = json.getString("details")!=null?json.getString("details"):json.getString("detail");
					if (StringUtil.isEmpty(errorcode)) {
						errorcode = json.getString("error_code");
						if(json.getString("error_message")!=null) {
							errormsg = json.getString("error_message");
						}else {
							errormsg = json.getString("message");
						}
					}
					if (StringUtil.isEmpty(errorcode)) {
						errorcode = json.getString("error");
						if(json.getString("error_message")!=null) {
							errormsg = json.getString("error_message");
						}else {
							errormsg = json.getString("message");
						}
					}
				} else {
					Document xml = GeneralUtil.getXML(respContent);
					if (xml != null) {
						NodeList codelist = xml.getElementsByTagName("Code");
						errorcode = codelist != null && codelist.getLength() > 0 ? codelist.item(0).getNodeValue() : "";
						NodeList messagelist = xml.getElementsByTagName("Message");
						errormsg = messagelist != null && messagelist.getLength() > 0 ? messagelist.item(0).getNodeValue() : "";
					}
				}
				throw new BaseException(errormsg,errorcode);
			} else {
				throw new BizException("访问异常错误编码：" + resp.getStatusLine().getStatusCode());
			}
		}
	}

	public static String postUrl(String url, Map<String, Object> param, Map<String, String> header) throws BizException {
		HttpPost httpPost = null;
		CloseableHttpClient client = null;
		try {
			httpPost = new HttpPost(url);
			httpPost.setConfig(getConfig());
			client = getClient();
			if (param != null && param.size() > 0) {
				String imgStr = param.get("asset").toString();
				String imageType = param.get("imageType").toString();
				String base64ImgData = imgStr.substring(imgStr.indexOf("base64,") + "base64,".length());// 图片数据
				 
				File file = GeneralUtil.getFileByBytes(GeneralUtil.decryptBASE64(base64ImgData), null, param.get("name").toString(), "." + imageType);
				MultipartEntityBuilder builder = MultipartEntityBuilder.create();
				builder.setCharset(java.nio.charset.Charset.forName("UTF-8"));
			    builder.addBinaryBody("asset",file , ContentType.create("image/"+imageType) , file.getName());
				for (Map.Entry<String, Object> entry : param.entrySet()) {
					if (entry.getValue() == null || "asset".equals(entry.getKey()) || "name".equals(entry.getKey())
							|| "imageType".equals(entry.getKey())) {
						continue;
					}
					builder.addTextBody(entry.getKey(), entry.getValue().toString(), ContentType.APPLICATION_JSON);
				}
				HttpEntity entity = builder.build();
				httpPost.setEntity(entity);
			}
			if (header != null && header.size() > 0) {
				for (Entry<String, String> item : header.entrySet()) {
					httpPost.addHeader(item.getKey(), item.getValue());
				}
			}
			HttpResponse resp = client.execute(httpPost);
			return handlerResponse(resp);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (httpPost != null) {
					httpPost.releaseConnection();
				}
				if (client != null) {
					client.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static String postUrl(String url, String param, Map<String, String> header) throws BizException {
		HttpPost httpPost = null;
		CloseableHttpClient client = null;
		try {
			httpPost = new HttpPost(url);
			httpPost.setConfig(getConfig());
			client = getClient();
			if (StringUtil.isNotEmpty(param)) {
				httpPost.setEntity(new StringEntity(param.toString(), "UTF-8"));
			}
			if (header != null && header.size() > 0) {
				for (Entry<String, String> item : header.entrySet()) {
					httpPost.addHeader(item.getKey(), item.getValue());
				}
			}
			HttpResponse resp = client.execute(httpPost);
			return handlerResponse(resp);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			//Connect to advertising-api-eu.amazon.com:443 [advertising-api-eu.amazon.com/54.239.39.175] failed: Read timed out
			//advertising-api.amazon.com:443 failed to respond
			//Remote host closed connection during handshake
		} finally {
			try {
				if (httpPost != null) {
					httpPost.releaseConnection();
				}
				if (client != null) {
					client.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static String postUrl(String url, Map<String, String> param, Map<String, String> header, String type) throws BizException {
		HttpPost httpPost = null;
		CloseableHttpClient client = null;
		try {
			httpPost = new HttpPost(url);
			httpPost.setConfig(getConfig());
			client = getClient();
			if (param != null && param.size() > 0) {
				if (URLEncodedUtils.CONTENT_TYPE.equals(type)) {
					List<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>();
					for (Entry<String, String> entry : param.entrySet()) {
						pairList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
					}
					httpPost.setEntity(new UrlEncodedFormEntity(pairList, "UTF-8"));
				} else {
					JSONObject json = new JSONObject();
					for (Entry<String, String> entry : param.entrySet()) {
						json.put(entry.getKey(), entry.getValue());
					}
					httpPost.setEntity(new StringEntity(json.toString(), "UTF-8"));
				}
			}
			if (header != null && header.size() > 0) {
				for (Entry<String, String> item : header.entrySet()) {
					httpPost.addHeader(item.getKey(), item.getValue());
				}
			}
			HttpResponse resp;
			resp = client.execute(httpPost);
			return handlerResponse(resp);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (httpPost != null) {
					httpPost.releaseConnection();
				}
				if (client != null) {
					client.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static String postUrl(String url, File param, Map<String, String> header, String type) throws BizException {
		HttpPost httpPost = null;
		CloseableHttpClient client = null;
		try {
			httpPost = new HttpPost(url);
			httpPost.setConfig(getConfig());
			client = getClient();
			if (param != null) {
				MultipartEntityBuilder builder = MultipartEntityBuilder.create();
				FileBody bin = new FileBody(param);
				builder.addPart("file", bin);
				httpPost.setEntity(builder.build());
			}
			if (header != null && header.size() > 0) {
				for (Entry<String, String> item : header.entrySet()) {
					httpPost.addHeader(item.getKey(), item.getValue());
				}
			}
			HttpResponse resp;
			resp = client.execute(httpPost);
			return handlerResponse(resp);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (httpPost != null) {
					httpPost.releaseConnection();
				}
				if (client != null) {
					client.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static String putUrl(String url, Map<String, String> param, Map<String, String> header, String type) throws BizException {
		HttpPut httpPut = null;
		CloseableHttpClient client = null;
		try {
			httpPut = new HttpPut(url);
			httpPut.setConfig(getConfig());
			client = getClient();
			if (param != null && param.size() > 0) {
				if (URLEncodedUtils.CONTENT_TYPE.equals(type)) {
					List<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>();
					for (Entry<String, String> entry : param.entrySet()) {
						pairList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
					}
					httpPut.setEntity(new UrlEncodedFormEntity(pairList, "UTF-8"));
				} else {
					JSONObject json = new JSONObject();
					for (Entry<String, String> entry : param.entrySet()) {
						json.put(entry.getKey(), entry.getValue());
					}
					httpPut.setEntity(new StringEntity(json.toString(), "UTF-8"));
				}
			}
			if (header != null && header.size() > 0) {
				for (Entry<String, String> item : header.entrySet()) {
					httpPut.addHeader(item.getKey(), item.getValue());
				}
			}
			HttpResponse resp;
			resp = client.execute(httpPut);
			return handlerResponse(resp);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (httpPut != null) {
					httpPut.releaseConnection();
				}
				if (client != null) {
					client.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static String putUrl(String url, String param, Map<String, String> header) throws BizException {
		HttpPut httpPut = null;
		CloseableHttpClient client = null;
		try {
			httpPut = new HttpPut(url);
			httpPut.setConfig(getConfig());
			client = getClient();
			if (param != null) {
				httpPut.setEntity(new StringEntity(param, "UTF-8"));
			}
			if (header != null && header.size() > 0) {
				for (Entry<String, String> item : header.entrySet()) {
					httpPut.addHeader(item.getKey(), item.getValue());
				}
			}
			HttpResponse resp;
			resp = client.execute(httpPut);
			return handlerResponse(resp);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (httpPut != null) {
					httpPut.releaseConnection();
				}
				if (client != null) {
					client.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static String deleteUrl(String url, Map<String, String> header) throws BizException {
		HttpDelete httpDelete = null;
		CloseableHttpClient client = null;
		try {
			httpDelete = new HttpDelete(url);
			httpDelete.setConfig(getConfig());
			client = getClient();
			if (header != null && header.size() > 0) {
				for (Entry<String, String> item : header.entrySet()) {
					httpDelete.addHeader(item.getKey(), item.getValue());
				}
			}
			HttpResponse resp;
			resp = client.execute(httpDelete);
			return handlerResponse(resp);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (httpDelete != null) {
					httpDelete.releaseConnection();
				}
				if (client != null) {
					client.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static CloseableHttpClient getClient() {
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
										   cm.setMaxTotal(8000);
										   cm.setDefaultMaxPerRoute(10000);
		CloseableHttpClient client = HttpClients.custom().setConnectionManager(cm).build();
		return client;
	}

	public static RequestConfig getConfig() {
		RequestConfig requestConfig = RequestConfig.custom()
				                                   .setConnectTimeout(60000)
				                                   .setConnectionRequestTimeout(60000)
				                                   .setSocketTimeout(60000)
				                                   .build();
		return requestConfig;
	}

	public static String getUrl(String url, Map<String, String> header) throws BizException {
		HttpGet httpGet = null;
		CloseableHttpClient client = null;
		try {
			httpGet = new HttpGet(url);
			httpGet.setConfig(getConfig());
			client = getClient();
			if (header != null && header.size() > 0) {
				for (Entry<String, String> item : header.entrySet()) {
					httpGet.addHeader(item.getKey(), item.getValue());
				}
			}
			HttpResponse resp;
			resp = client.execute(httpGet);
			return handlerResponse(resp);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			//SSL peer shut down incorrectly.Remote host closed connection during handshake.
			//Received close_notify during handshake
		} finally {
			try {
				if (httpGet != null) {
					httpGet.releaseConnection();
				}
				if (client != null) {
					client.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static String getRedirectInfo(String url, Map<String, String> header) throws BizException {
		HttpGet httpGet = null;
		CloseableHttpClient client = null;
		try {
			httpGet = new HttpGet(url);
			client = getClient();
			if (header != null && header.size() > 0) {
				for (Entry<String, String> item : header.entrySet()) {
					httpGet.addHeader(item.getKey(), item.getValue());
				}
			}
			HttpParams params = httpGet.getParams();
			params.setParameter(ClientPNames.HANDLE_REDIRECTS, false);
			HttpResponse response = client.execute(httpGet);
			String redirectUrl = "";
			if (success_code.contains(response.getStatusLine().getStatusCode())) {
				HttpEntity entity = response.getEntity();
				if (null != entity) {
					Header[] headers = response.getHeaders("Location");
					if (headers != null && headers.length > 0) {
						redirectUrl = headers[0].getValue();
					}
				}
			} else {
				int code = response.getStatusLine().getStatusCode();
				redirectUrl = null;
				if(code==400) {
					BaseException http = new BaseException("Snapshot is expired");
					http.setCode(BaseException.Expired);	
					throw 	http;			
				}else {
					throw new BizException("异常编码：[" + code + "]" + response.toString());
				}
			}
			return redirectUrl;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			//SSL peer shut down incorrectly
		} finally {
			try {
				if (httpGet != null) {
					httpGet.releaseConnection();
				}
				if (client != null) {
					client.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static HttpResponse getResponseUrl(String url, Map<String, String> header) {
		HttpGet httpGet = null;
		CloseableHttpClient client = null;
		try {
			httpGet = new HttpGet(url);
			httpGet.setConfig(getConfig());
			client = getClient();
			if (header != null && header.size() > 0) {
				for (Entry<String, String> item : header.entrySet()) {
					httpGet.addHeader(item.getKey(), item.getValue());
				}
			}
			return client.execute(httpGet);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (httpGet != null) {
					httpGet.releaseConnection();
				}
				if (client != null) {
					client.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static String  getGZFileUrl(String url) {
		GZIPInputStream ungzip = null;
		BufferedReader read = null;
		InputStream in = null;
		HttpGet httpGet = null;
		CloseableHttpClient client = null;
		String result = null;
		try {
			httpGet = new HttpGet(url);
			httpGet.setProtocolVersion(HttpVersion.HTTP_1_0);
			httpGet.addHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_CLOSE);
			httpGet.setConfig(getConfig());
			httpGet.addHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");
			client = HttpClients.createDefault();
			HttpResponse resp = client.execute(httpGet);
			if (resp != null && success_code.contains(resp.getStatusLine().getStatusCode())) {
				HttpEntity entity = resp.getEntity();
				in = entity.getContent();
				ungzip = new GZIPInputStream(in);
			    BufferedReader br = new BufferedReader(new InputStreamReader(ungzip, "UTF-8"));
			    StringBuilder sb = new StringBuilder();
			    String line;
			    while ((line = br.readLine()) != null) {
			                    sb.append(line);
			                }
			    result=sb.toString();
			    br.close();
			    ungzip.close();
			    in.close();
			} else {
				httpGet.abort();
				if(resp != null){
					int code = resp.getStatusLine().getStatusCode();
					throw new BaseException("请求异常，编码：[" + code + "]" + resp.toString());
				}
			} 
		} catch (ClientProtocolException e) {
			httpGet.abort();
			e.printStackTrace();
		} catch (IOException e) {
			httpGet.abort();
			e.printStackTrace();
			//SSL peer shut down incorrectly
			//Connect to amazon-advertising-api-reports-prod-euamazon.s3.amazonaws.com:443 [amazon-advertising-api-reports-prod-euamazon.s3.amazonaws.com/54.231.82.26] failed: Read timed out
		} finally {
			try {
				if (read != null) {
					read.close();
				}
				if (ungzip != null) {
					ungzip.close();
				}
				if (in != null) {
					in.close();
				}
				if (httpGet != null) {
					httpGet.abort();
					httpGet.releaseConnection();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static boolean getGZFileUrl(String url, AmzAdvProfile profile, AmzAdvRequest request,
			IAmzAdvHttpClientResponseHandler amzAdvHttpClientResponseHandler) {
		GZIPInputStream ungzip = null;
		BufferedReader read = null;
		JSONReader jsonReader = null;
		InputStream in = null;
		HttpGet httpGet = null;
		CloseableHttpClient client = null;
		boolean result = false;
		try {
			httpGet = new HttpGet(url);
			httpGet.setProtocolVersion(HttpVersion.HTTP_1_0);
			httpGet.addHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_CLOSE);
			httpGet.setConfig(getConfig());
			httpGet.addHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");
			client = HttpClientUtil.getClient();
			HttpResponse resp = client.execute(httpGet);
			if (resp != null && success_code.contains(resp.getStatusLine().getStatusCode())) {
				HttpEntity entity = resp.getEntity();
				in = entity.getContent();
				ungzip = new GZIPInputStream(in);
				read = new BufferedReader(new InputStreamReader(ungzip, "UTF-8"));
				jsonReader = new JSONReader(read);
				if (jsonReader != null) {
					result = amzAdvHttpClientResponseHandler.treatReport(profile, request, jsonReader);
				}
			} else {
				httpGet.abort();
				if(resp != null){
					int code = resp.getStatusLine().getStatusCode();
					throw new BaseException("请求异常，编码：[" + code + "]" + resp.toString());
				}
			} 
		} catch (ClientProtocolException e) {
			httpGet.abort();
			e.printStackTrace();
		} catch (IOException e) {
			httpGet.abort();
			e.printStackTrace();
			//SSL peer shut down incorrectly
			//Connect to amazon-advertising-api-reports-prod-euamazon.s3.amazonaws.com:443 [amazon-advertising-api-reports-prod-euamazon.s3.amazonaws.com/54.231.82.26] failed: Read timed out
		} finally {
			try {
				if (read != null) {
					read.close();
				}
				if (ungzip != null) {
					ungzip.close();
				}
				if (in != null) {
					in.close();
				}
				if (httpGet != null) {
					httpGet.abort();
					httpGet.releaseConnection();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
