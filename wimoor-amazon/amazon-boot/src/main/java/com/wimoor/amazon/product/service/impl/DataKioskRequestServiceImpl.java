package com.wimoor.amazon.product.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.amazon.spapi.SellingPartnerAPIAA.LWAException;
import com.amazon.spapi.api.QueriesApi;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.datakiosk.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.auth.service.impl.ApiBuildService;
import com.wimoor.amazon.product.mapper.DataKioskRequestMapper;
import com.wimoor.amazon.product.pojo.entity.DataKioskRequest;
import com.wimoor.amazon.product.pojo.entity.ProductInfo;
import com.wimoor.amazon.product.service.IDataKioskRequestService;
import com.wimoor.amazon.product.service.IProductInfoService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.result.Result;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;
import org.threeten.bp.OffsetDateTime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
* @author liufei
* @description 针对表【t_amz_data_kiosk_request】的数据库操作Service实现
* @createDate 2026-02-03 09:08:03
*/
@Service
@RequiredArgsConstructor
public class DataKioskRequestServiceImpl extends ServiceImpl<DataKioskRequestMapper, DataKioskRequest>
    implements IDataKioskRequestService {

    final IAmazonAuthorityService amazonAuthorityService;
    final IMarketplaceService marketplaceServcie;
    final ApiBuildService apiBuildService;
    final IProductInfoService productInfoService;


    @Override
    public Result<?> createQuery(String amazonAuthId, String marketplaceId) {
        AmazonAuthority auth = this.amazonAuthorityService.getById(amazonAuthId);
        Marketplace marketplace = marketplaceServcie.getById(marketplaceId);
        QueriesApi api = apiBuildService.getQueriesApi(auth);
        CreateQuerySpecification query = new CreateQuerySpecification();
        String startDate=GeneralUtil.getStrDate(new Date());
        String endDate=GeneralUtil.getStrDate(new Date());
        String queryStr = "query MyQuery{analytics_economics_2024_03_15{economics(endDate:\"" + endDate + "\",marketplaceIds:\"" + marketplace.getMarketplaceid() + "\",startDate:\"" + startDate + "\"){parentAsin msku fnsku childAsin}}}";
        query.setQuery(queryStr);
        try {
            CreateQueryResponse response = api.createQuery(query);
            DataKioskRequest kioskRequest = new DataKioskRequest();
            kioskRequest.setAmazonauthid(new BigInteger(amazonAuthId));
            kioskRequest.setMarketplaceid(marketplaceId);
            kioskRequest.setQueryId(new BigInteger(response.getQueryId()));
            kioskRequest.setBeginDate(GeneralUtil.getDate(startDate));
            kioskRequest.setEndDate(GeneralUtil.getDate(endDate));
            kioskRequest.setStatus(0);
            kioskRequest.setLastupdate(new java.util.Date());
            this.saveOrUpdate(kioskRequest);
            return Result.success(response);
        } catch (ApiException e) {
            throw new RuntimeException(e);
        } catch (LWAException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Result<?> getQueries(String amazonAuthId, String queryId) {
        AmazonAuthority auth = this.amazonAuthorityService.getById(amazonAuthId);
        QueriesApi api = apiBuildService.getQueriesApi(auth);
        try {
            List<String> processingStatuses = List.of();
            Integer pageSize = 100;
            OffsetDateTime createdSince = null;
            OffsetDateTime createdUntil = null;
            String paginationToken = "";
            GetQueriesResponse response = api.getQueries(processingStatuses, pageSize, createdSince, createdUntil, paginationToken);
            return Result.success(response);
        } catch (ApiException e) {
            throw new RuntimeException(e);
        } catch (LWAException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Result<?> getQuery(String amazonAuthId, String queryId) {
        AmazonAuthority auth = this.amazonAuthorityService.getById(amazonAuthId);
        QueriesApi api = apiBuildService.getQueriesApi(auth);
        try {
            Query response = api.getQuery(queryId);
            DataKioskRequest kioskRequest = this.getById(new BigInteger(queryId));
            if (kioskRequest != null) {
                kioskRequest.setDataDocumentId(response.getDataDocumentId());
                kioskRequest.setProcessingStatus(response.getProcessingStatus().getValue());
                kioskRequest.setLastupdate(new java.util.Date());
                kioskRequest.setStatus(1);
                this.updateById(kioskRequest);
            }
            return Result.success(response);
        } catch (ApiException e) {
            throw new RuntimeException(e);
        } catch (LWAException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Result<?> getDocument(String amazonAuthId, String documentId) {
        AmazonAuthority auth = this.amazonAuthorityService.getById(amazonAuthId);
        QueriesApi api = apiBuildService.getQueriesApi(auth);
        try {
            GetDocumentResponse response =null;
            DataKioskRequest kioskRequest = this.lambdaQuery().eq(DataKioskRequest::getDataDocumentId, documentId).one();
            if(kioskRequest != null&& StrUtil.isBlankOrUndefined(kioskRequest.getDocumentUrl())) {
                response = api.getDocument(documentId);
                kioskRequest.setDocumentUrl(response.getDocumentUrl());
                kioskRequest.setLastupdate(new java.util.Date());
                kioskRequest.setStatus(2);
                this.updateById(kioskRequest);
            }
            if(kioskRequest!=null&&!StrUtil.isBlankOrUndefined(kioskRequest.getDocumentUrl())) {
                handleDocument(auth, kioskRequest);
            }
            return Result.success(response);
        } catch (ApiException e) {
            throw new RuntimeException(e);
        } catch (LWAException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleDocument(AmazonAuthority auth, DataKioskRequest kioskRequest) {
        Request signedRequest = new Request.Builder().url(kioskRequest.getDocumentUrl()).build(); // Build the request.
        BufferedReader reader =null;
        JSONReader jsonReader =null;
        InputStream in=null;
        try{
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(60, TimeUnit.SECONDS);
            builder.readTimeout(60, TimeUnit.SECONDS);
            builder.writeTimeout(60, TimeUnit.SECONDS);
            OkHttpClient okHttpClient = builder.build();
            Response response = okHttpClient.newCall(signedRequest).execute();
            if (response.isSuccessful()&&response.body() != null) {
                in = response.body().byteStream();
                reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                String line;
                int lineNum = 1;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (!line.isEmpty()) {
                        try {
                            // 解析每一行作为一个JSON对象
                            JSONObject jsonObject = JSON.parseObject(line);
                            String parentAsin = jsonObject.getString("parentAsin");
                            String msku = jsonObject.getString("msku");
                            String childAsin = jsonObject.getString("childAsin");
                            ProductInfo productInfo = productInfoService.productOnlyone(kioskRequest.getAmazonauthid().toString(),  msku,  kioskRequest.getMarketplaceid());
                            if(productInfo==null){
                                continue;
                            }
                            productInfo.setParentAsin(parentAsin);
                            productInfoService.updateById(productInfo);
                        } catch (Exception e) {
                            System.err.println("解析第 " + lineNum + " 行失败: " + line);
                            e.printStackTrace();
                        }
                        lineNum++;
                    }
                }
                reader.close();
            } else {
                System.err.println("HTTP错误: " + response.code());
            }
            response.close();

        } catch (IOException e) {
            e.printStackTrace();
            //SSL peer shut down incorrectly
            //Connect to amazon-advertising-api-reports-prod-euamazon.s3.amazonaws.com:443 [amazon-advertising-api-reports-prod-euamazon.s3.amazonaws.com/54.231.82.26] failed: Read timed out
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (in != null) {
                    in.close();
                }
                kioskRequest.setLastupdate(new java.util.Date());
                kioskRequest.setStatus(3);
                this.updateById(kioskRequest);
            } catch (IOException | JSONException e  ) {
                e.printStackTrace();
            }
        }

    }

    public void refresh(){
        List<DataKioskRequest> list=this.baseMapper.getDocId();
        for(DataKioskRequest kioskRequest:list) {
            getQuery(kioskRequest.getAmazonauthid().toString(),kioskRequest.getQueryId().toString());
        }
        List<DataKioskRequest> list2=this.baseMapper.getDocument();
        for(DataKioskRequest kioskRequest:list2) {
            if(kioskRequest.getDataDocumentId()!=null){
                getDocument(kioskRequest.getAmazonauthid().toString(),kioskRequest.getDataDocumentId().toString());
            }
        }

    }
    @Override
    public void runApi(AmazonAuthority amazonAuthority) {
        List<Marketplace> marketplaces = marketplaceServcie.findbyauth(amazonAuthority.getId());
        for(Marketplace marketplace:marketplaces) {
            createQuery(amazonAuthority.getId(), marketplace.getMarketplaceid());
        }
    }
}


