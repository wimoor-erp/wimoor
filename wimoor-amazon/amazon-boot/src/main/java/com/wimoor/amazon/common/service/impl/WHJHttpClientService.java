package com.wimoor.amazon.common.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wimoor.amazon.common.mapper.ExchangeRateHisMapper;
import com.wimoor.amazon.common.pojo.entity.ExchangeRate;
import com.wimoor.amazon.common.pojo.entity.ExchangeRateHis;
import com.wimoor.amazon.util.CurrencyConverter;
import com.wimoor.common.GeneralUtil;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class WHJHttpClientService {

    private CloseableHttpClient httpClient;
    @Autowired
    ExchangeRateHisMapper exchangeRateHisMapper;
    @PostConstruct
    public void init() {
        this.httpClient = createUnsafeHttpClient();
    }

    @PreDestroy
    public void destroy() {
        if (httpClient != null) {
            try {
                httpClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private CloseableHttpClient createUnsafeHttpClient() {
        try {
            // 创建信任所有证书的TrustManager
            TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        @Override
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };

            // 创建SSL上下文
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // 创建SSL连接工厂
            SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(
                    sslContext,
                    NoopHostnameVerifier.INSTANCE
            );

            // 创建连接管理器
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                    .<ConnectionSocketFactory>create()
                    .register("https", sslSocketFactory)
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .build();

            PoolingHttpClientConnectionManager connectionManager =
                    new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            connectionManager.setMaxTotal(100);
            connectionManager.setDefaultMaxPerRoute(20);

            // 创建请求配置
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(60000)
                    .setSocketTimeout(60000)
                    .setConnectionRequestTimeout(60000)
                    .build();

            // 创建HTTP客户端
            return HttpClients.custom()
                    .setConnectionManager(connectionManager)
                    .setDefaultRequestConfig(requestConfig)
                    .setSSLSocketFactory(sslSocketFactory)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("创建HTTP客户端失败", e);
        }
    }

    public List<ExchangeRate> fetchDocument(String url) throws Exception {
        // 注意：这里不再关闭HttpClient，只在try-with-resources中关闭response
        List<ExchangeRate> exchangeRates=new ArrayList<>();
        org.apache.http.client.methods.HttpPost httpPost = new org.apache.http.client.methods.HttpPost(url);

        // 设置请求头
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        httpPost.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        httpPost.setHeader("Accept-Encoding", "gzip, deflate");
        httpPost.setHeader("Connection", "keep-alive");
        Calendar c= Calendar.getInstance();
        String endDate = GeneralUtil.formatDate(c.getTime());
        c.add(Calendar.DATE, -10);
        String startDate= GeneralUtil.formatDate(c.getTime());
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("startDate", startDate));
        params.add(new BasicNameValuePair("endDate", endDate));
        params.add(new BasicNameValuePair("queryYN", "true"));
        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        // 执行请求，只关闭response，不关闭httpClient
        try (org.apache.http.client.methods.CloseableHttpResponse response = httpClient.execute(httpPost);
             InputStream inputStream = response.getEntity().getContent()) {
            // 3. 使用 POI 解析 Excel
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            // 获取表头行（第一行），确定币种顺序
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new Exception("Excel 文件无表头");
            }

            List<String> currencies = new ArrayList<>();
            for (int col = 1; col < headerRow.getLastCellNum(); col++) { // 跳过第一列（日期）
                Cell cell = headerRow.getCell(col);
                String currencyName = getCellStringValue(cell);
                if (StrUtil.isNotBlank(currencyName)) {
                    currencies.add(currencyName.trim());
                }
            }

            // 遍历数据行（从第二行开始）
            for (int rowIdx = 1; rowIdx <= sheet.getLastRowNum(); rowIdx++) {
                Row dataRow = sheet.getRow(rowIdx);
                if (dataRow == null) continue;

                String date = getCellStringValue(dataRow.getCell(0));
                if (!isValidDate(date)) continue;
                if(date.equals(endDate)){
                    // 为每个币种创建汇率对象
                    for (int col = 1; col <= currencies.size(); col++) {
                        Cell rateCell = dataRow.getCell(col);
                        String rateStr = getCellStringValue(rateCell);
                        if (StrUtil.isNotBlank(rateStr) && !"-".equals(rateStr)) {
                            String currencyName = currencies.get(col - 1);
                            String currencyCode = CurrencyConverter.toCurrencyCode(currencyName);
                            if (currencyCode != null) {
                                try {
                                    ExchangeRate rate = new ExchangeRate();
                                    rate.setName(currencyCode);
                                    rate.setSymbol(currencyName);
                                    rate.setPrice(new BigDecimal(rateStr));
                                    rate.setUtctime(GeneralUtil.getDatez(date));
                                    rate.setType("CNY(100)/" + currencyCode);
                                    exchangeRates.add(rate);
                                } catch (NumberFormatException e) {
                                    // 忽略无效数字
                                    System.err.println("汇率数值错误: " + currencyName + " = " + rateStr);
                                }
                            }
                        }
                    }
                }else{
                    // 为每个币种创建汇率对象
                    for (int col = 1; col <= currencies.size(); col++) {
                        Cell rateCell = dataRow.getCell(col);
                        String rateStr = getCellStringValue(rateCell);
                        if (StrUtil.isNotBlank(rateStr) && !"-".equals(rateStr)) {
                            String currencyName = currencies.get(col - 1);
                            String currencyCode = CurrencyConverter.toCurrencyCode(currencyName);
                            if (currencyCode != null) {
                                try {
                                    ExchangeRateHis rate = new ExchangeRateHis();
                                    rate.setName(currencyCode);
                                    rate.setSymbol(currencyName);
                                    rate.setPrice(new BigDecimal(rateStr));
                                    rate.setUtctime(GeneralUtil.getDatez(date));
                                    rate.setType("CNY(100)/" + currencyCode);
                                    rate.setByday(GeneralUtil.getDatez(date));
                                    LambdaQueryWrapper<ExchangeRateHis> query=new  LambdaQueryWrapper<ExchangeRateHis>();
                                    query.eq(ExchangeRateHis::getByday,rate.getByday());
                                    query.eq(ExchangeRateHis::getName,rate.getName());
                                    ExchangeRateHis one=exchangeRateHisMapper.selectOne(query);
                                    if(one==null){
                                        exchangeRateHisMapper.insert(rate);
                                    }else{
                                        rate.setId(one.getId());
                                        exchangeRateHisMapper.updateById(rate);
                                    }
                                } catch (NumberFormatException e) {
                                    // 忽略无效数字
                                    System.err.println("汇率数值错误: " + currencyName + " = " + rateStr);
                                }
                            }
                        }
                    }
                }

            }
        }
                return exchangeRates;
    }
        /**
         * 获取单元格的字符串值（支持数字、日期等）
         */
        private String getCellStringValue(Cell cell) {
            if (cell == null) return "";
            switch (cell.getCellType()) {
                case STRING:
                    return cell.getStringCellValue();
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return new SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue());
                    } else {
                        // 保留完整小数，避免科学计数法
                        BigDecimal bd = BigDecimal.valueOf(cell.getNumericCellValue());
                        return bd.stripTrailingZeros().toPlainString();
                    }
                case FORMULA:
                    return cell.getCellFormula();
                default:
                    return "";
            }
        }

        /**
         * 简单日期校验
         */
        private boolean isValidDate(String date) {
            return date != null && date.matches("\\d{4}-\\d{2}-\\d{2}");
        }

    /**
     * 获取HttpClient实例（如果需要直接使用）
     */
    public CloseableHttpClient getHttpClient() {
        return httpClient;
    }
}