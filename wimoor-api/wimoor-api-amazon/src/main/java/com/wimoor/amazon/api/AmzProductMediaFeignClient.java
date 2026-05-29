package com.wimoor.amazon.api;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wimoor.common.ServiceNameConstants;

/**
 * Amazon 商品媒体 Feign 客户端：供 ERP 侧 syncFromAmazon 调用。
 *
 * @author wimoor
 */
@FeignClient(contextId = "amzProductMediaClient", value = ServiceNameConstants.AMAZON_SERVICE)
public interface AmzProductMediaFeignClient {

    @GetMapping("/amazon/api/v1/product/media/list")
    List<AmzProductMediaDTO> listBySku(@RequestParam("authorityId") String authorityId,
                                       @RequestParam("marketplaceId") String marketplaceId,
                                       @RequestParam("sku") String sku);
}
