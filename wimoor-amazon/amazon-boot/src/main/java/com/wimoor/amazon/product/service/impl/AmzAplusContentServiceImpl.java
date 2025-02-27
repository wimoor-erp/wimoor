package com.wimoor.amazon.product.service.impl;

import com.amazon.spapi.SellingPartnerAPIAA.LWAException;
import com.amazon.spapi.api.AplusContentApi;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.apluscontent.SearchContentPublishRecordsResponse;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.impl.ApiBuildService;
import com.wimoor.amazon.product.service.IAmzAplusContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AmzAplusContentServiceImpl implements IAmzAplusContentService {
    final IAmazonAuthorityService amazonAuthorityService;
    final ApiBuildService apiBuildService;
    @Override
    public SearchContentPublishRecordsResponse searchContentPublishRecords(String amazonAuthId, String marketplaceId, String asin, String pageToken) {
        AplusContentApi api = apiBuildService.getAplusContentApi(amazonAuthorityService.getById(amazonAuthId));
        SearchContentPublishRecordsResponse response = null;
        try {
            response = api.searchContentPublishRecords(marketplaceId, asin, pageToken);
        } catch (ApiException e) {
            throw new RuntimeException(e);
        } catch (LWAException e) {
            throw new RuntimeException(e);
        }
        return response;
    }
}
