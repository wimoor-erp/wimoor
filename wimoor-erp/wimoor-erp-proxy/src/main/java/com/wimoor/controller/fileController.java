package com.wimoor.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONException;
import com.wimoor.config.HttpClientUtil;
import com.wimoor.config.HttpException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HTTP;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping("/api/v1/file")
@Controller
public class fileController {

	@RequestMapping("/postUrl")
	public void postUrlAction(HttpServletRequest request, HttpServletResponse response) {
		String s = null;
		String url = request.getParameter("url");
		String contentType = "application/x-www-form-urlencoded;charset=utf-8";
		ServletOutputStream out = null;
		try {
			System.out.println(url);
			HttpGet httpGet = new HttpGet(url);
			httpGet.setProtocolVersion(HttpVersion.HTTP_1_0);
			httpGet.addHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_CLOSE);
			httpGet.setConfig(getConfig());
			httpGet.addHeader(HTTP.CONTENT_TYPE, contentType);
			CloseableHttpClient client = HttpClientUtil.getClient();
			HttpResponse resp = client.execute(httpGet);
			if (resp != null &&resp.getEntity()!=null) {
				HttpEntity entity = resp.getEntity();
				InputStream in = entity.getContent();
				out = response.getOutputStream();
				copy(in, out);
				in.close();
			}
		}  catch (ClientProtocolException e) {
            e.printStackTrace();
        }  catch (IOException e) {
			e.printStackTrace();
        } finally {
			try {
				if(out != null) {
					out.flush();
					out.close();
				}
			} catch (IOException e3) {
				e3.printStackTrace();
			}
		}
	}
	public static RequestConfig getConfig() {
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(600000)
				.setConnectionRequestTimeout(600000)
				.setSocketTimeout(600000)
				.build();
		return requestConfig;
	}
	public static void copy(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[4096]; // 创建一个缓冲区
		int bytesRead;
		while ((bytesRead = in.read(buffer)) != -1) {
			out.write(buffer, 0, bytesRead);
		}
	}

}
