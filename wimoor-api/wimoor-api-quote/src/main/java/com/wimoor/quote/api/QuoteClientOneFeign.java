package com.wimoor.quote.api;

import com.wimoor.common.result.Result;
import com.wimoor.quote.api.dto.QuoteDTO;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@Component
@FeignClient(value = "wimoor-quote")
public interface QuoteClientOneFeign {

    @PostMapping("/quote/api/v1/quote/save")
    public Result<List<Map<String,Object>>> saveAction(@RequestBody QuoteDTO dto);


}