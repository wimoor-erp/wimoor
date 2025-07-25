package com.wimoor.amazon.adv.api;

import com.wimoor.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * @author haoxr
 * @description TODO
 * @createTime 2021/3/13 11:59
 */
@Component
@FeignClient(value = "wimoor-amazon-adv")
public interface AmazonAdvFeignClient {
    @PostMapping("/amazonadv/api/v1/ads/invoices/getInvoicesSummary")
    public Result<Map<String,Object>> getInvoicesSummaryAction(@RequestBody Map<String,Object> param);
}
