package com.wimoor.amazon.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.auth.service.IRunAmazonService;
import com.wimoor.amazon.product.pojo.entity.DataKioskRequest;
import com.wimoor.common.result.Result;

/**
* @author liufei
* @description 针对表【t_amz_data_kiosk_request】的数据库操作Service
* @createDate 2026-02-03 09:08:03
*/
public interface IDataKioskRequestService extends IService<DataKioskRequest> , IRunAmazonService {

    Result<?> createQuery(String amazonAuthId, String marketplaceId);
    Result<?> getQueries(String amazonAuthId, String queryId);
    Result<?> getQuery(String amazonAuthId, String queryId);
    Result<?> getDocument(String amazonAuthId, String documentId);
    public void refresh();

}
