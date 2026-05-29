package com.wimoor.amazon.product.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.amazon.api.AmzProductMediaDTO;
import com.wimoor.amazon.product.pojo.entity.AmzProductMedia;
import com.wimoor.amazon.product.service.IAmzProductMediaService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

/**
 * Amazon 商品媒体查询 Controller（供 ERP 通过 Feign 调用）。
 *
 * @author wimoor
 */
@RestController
@RequestMapping("/api/v1/product/media")
@RequiredArgsConstructor
@Api(tags = "Amazon商品媒体")
public class AmzProductMediaController {

    private final IAmzProductMediaService service;

    @GetMapping("/list")
    @ApiOperation("查询指定SKU的Amazon媒体缓存")
    public List<AmzProductMediaDTO> listBySku(@RequestParam("authorityId") String authorityId,
                                              @RequestParam(value = "marketplaceId", required = false) String marketplaceId,
                                              @RequestParam("sku") String sku) {
        List<AmzProductMedia> entities = service.listBySku(authorityId, marketplaceId, sku);
        List<AmzProductMediaDTO> dtos = new ArrayList<>();
        if (entities == null) return dtos;
        for (AmzProductMedia e : entities) {
            AmzProductMediaDTO dto = new AmzProductMediaDTO();
            BeanUtils.copyProperties(e, dto);
            dtos.add(dto);
        }
        return dtos;
    }
}
