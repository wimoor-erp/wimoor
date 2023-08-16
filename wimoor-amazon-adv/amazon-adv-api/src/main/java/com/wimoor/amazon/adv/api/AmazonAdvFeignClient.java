package com.wimoor.amazon.adv.api;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author haoxr
 * @description TODO
 * @createTime 2021/3/13 11:59
 */
@FeignClient("wimoor-amazon-adv")
public interface AmazonAdvFeignClient {
}
