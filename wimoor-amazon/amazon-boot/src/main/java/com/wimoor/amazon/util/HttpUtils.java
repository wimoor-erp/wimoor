package com.wimoor.amazon.util;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.alibaba.fastjson.JSONObject;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
 

/**
 * <pre>
 * Http工具，包含：
 * 高级http工具(使用net.sourceforge.htmlunit获取完整的html页面,即完成后台js代码的运行)
 * </pre>
 * Created by felix at 2020/8/12 11:12.
 */
public class HttpUtils {
    /**
     * 请求超时时间,默认20000ms
     */
    private int timeout = 20000;
    /**
     * 等待异步JS执行时间,默认20000ms
     */
    private int waitForBackgroundJavaScript = 20000;

    private static HttpUtils httpUtils;

    private HttpUtils() {
    }

    /**
     * 获取实例
     *
     * @return
     */
    public static HttpUtils getInstance() {
        if (httpUtils == null)
            httpUtils = new HttpUtils();
        return httpUtils;
    }

    public int getTimeout() {
        return timeout;
    }

    /**
     * 设置请求超时时间
     *
     * @param timeout
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getWaitForBackgroundJavaScript() {
        return waitForBackgroundJavaScript;
    }

    /**
     * 设置获取完整HTML页面时等待异步JS执行的时间
     *
     * @param waitForBackgroundJavaScript
     */
    public void setWaitForBackgroundJavaScript(int waitForBackgroundJavaScript) {
        this.waitForBackgroundJavaScript = waitForBackgroundJavaScript;
    }

    /**
     * 将网页返回为解析后的文档格式
     * 
     * @param html
     * @return
     * @throws Exception
     */
    public static Document parseHtmlToDoc(String html) throws Exception {
        return removeHtmlSpace(html);
    }

    private static Document removeHtmlSpace(String str) {
        Document doc = Jsoup.parse(str);
        String result = doc.html().replace("&nbsp;", "");
        return Jsoup.parse(result);
    }

    /**
     * 获取页面文档字串(等待异步JS执行)
     *
     * @param url 页面URL
     * @return
     * @throws Exception
     */
    Map<String,Set<Cookie>> mycookie=new HashMap<String,Set<Cookie>>();
    public String getHtmlPageResponse(String url,JSONObject jsonData) throws Exception {
        String result = "";
        String marketpont = url.split("/product-reviews/")[0];   
          WebClient webClient = null;
        if("127.0.0.1".equals(jsonData.getString("ip"))) {
        	 webClient = new WebClient(BrowserVersion.CHROME);
        }else {
        	 webClient = new WebClient(BrowserVersion.CHROME,jsonData.getString("ip"),jsonData.getInteger("port"));//BrowserVersion.CHROME
        }
        
        webClient.getCookieManager().setCookiesEnabled(true);
        if(  mycookie.get(marketpont)!=null) {
        	Set<Cookie> cookies = mycookie.get(marketpont);
        	for(Cookie item:cookies) {
        		webClient.getCookieManager().addCookie(item);	
        	}
        }
     
        webClient.getOptions().setThrowExceptionOnScriptError(false);//当JS执行出错的时候是否抛出异常
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);//当HTTP的状态非200时是否抛出异常
        webClient.getOptions().setActiveXNative(false);
        
        webClient.getOptions().setCssEnabled(false);//是否启用CSS
        webClient.getOptions().setJavaScriptEnabled(true); //很重要，启用JS
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());//很重要，设置支持AJAX
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setTimeout(timeout);//设置“浏览器”的请求超时时间
        webClient.setJavaScriptTimeout(timeout);//设置JS执行的超时时间
        HtmlPage page;
        try {
            page = webClient.getPage(url);
            Set<Cookie> cookies = webClient.getCookieManager().getCookies();
            mycookie.put(marketpont,cookies);
        } catch (Exception e) {
            webClient.close();
            throw e;
        }
        webClient.waitForBackgroundJavaScript(waitForBackgroundJavaScript);//该方法阻塞线程
        result = page.asXml();
        webClient.close();

        return result;
    }
    /**
     * 获取页面文档字串(等待异步JS执行)
     *
     * @param url 页面URL
     * @return
     * @throws Exception
     */
    public String getHtmlPageResponse(String url) throws Exception {
        String result = "";
       // String IP=GeneralUtil.loadJson("http://http.tiqu.alicdns.com/getip3?num=1&type=2&pro=&city=0&yys=0&port=11&time=1&ts=0&ys=0&cs=0&lb=1&sb=0&pb=5&mr=1&regions=");
    
        String marketpont = url.split("/sp")[0];   
        final WebClient webClient = new WebClient(BrowserVersion.CHROME);
        //String agent = GeneralUtil.getAgent();
        //webClient.addRequestHeader("User-Agent", agent);
        //webClient.addCookie("",new URL(url),"getHtmlPageResponse");
        webClient.getCookieManager().setCookiesEnabled(true);
        if(  mycookie.get(marketpont)!=null) {
        	Set<Cookie> cookies = mycookie.get(marketpont);
        	for(Cookie item:cookies) {
        		webClient.getCookieManager().addCookie(item);	
        	}
        }
     
        webClient.getOptions().setThrowExceptionOnScriptError(false);//当JS执行出错的时候是否抛出异常
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);//当HTTP的状态非200时是否抛出异常
        webClient.getOptions().setActiveXNative(false);
        webClient.getOptions().setCssEnabled(false);//是否启用CSS
        webClient.getOptions().setJavaScriptEnabled(true); //很重要，启用JS
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());//很重要，设置支持AJAX

        webClient.getOptions().setTimeout(timeout);//设置“浏览器”的请求超时时间
        webClient.setJavaScriptTimeout(timeout);//设置JS执行的超时时间
        webClient.waitForBackgroundJavaScript(waitForBackgroundJavaScript);//该方法阻塞线程
        HtmlPage page;
        try {
            page = webClient.getPage(url);
            Set<Cookie> cookies = webClient.getCookieManager().getCookies();
            mycookie.put(marketpont,cookies);
        } catch (Exception e) {
            webClient.close();
            throw e;
        }
        webClient.waitForBackgroundJavaScript(waitForBackgroundJavaScript);//该方法阻塞线程
        result = page.asXml();
        webClient.close();
        return result;
    }
    /**
     * 获取页面文档Document对象(等待异步JS执行)
     *
     * @param url 页面URL
     * @return
     * @throws Exception
     */
    public Document getHtmlPageResponseAsDocument(String url,JSONObject jsonData) throws Exception {
        return parseHtmlToDoc(getHtmlPageResponse(url,jsonData));
    }
    public Document getHtmlPageResponseAsDocument(String url) throws Exception {
        return parseHtmlToDoc(getHtmlPageResponse(url));
    }
}
 