package com.wimoor.amazon.product.service;

import com.amazon.spapi.model.apluscontent.SearchContentPublishRecordsResponse;

public interface IAmzAplusContentService {
    SearchContentPublishRecordsResponse searchContentPublishRecords(String authid, String marketplaceId, String asin, String pageToken) ;
}
