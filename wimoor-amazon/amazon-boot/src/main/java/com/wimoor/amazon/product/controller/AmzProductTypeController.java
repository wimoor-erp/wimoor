package com.wimoor.amazon.product.controller;


import com.wimoor.amazon.product.pojo.dto.DefinitionsProductTypesDTO;
import com.wimoor.common.GeneralUtil;
import org.apache.commons.compress.utils.CharsetNames;
import org.springframework.web.bind.annotation.*;

import com.amazon.spapi.model.definitions.ProductTypeDefinition;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.product.service.IProductProductTypeService;
import com.wimoor.common.result.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-06-17
 */
@Api(tags = "产品类型接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product/amzProductType")
public class AmzProductTypeController {

	final IAmazonAuthorityService amazonAuthorityService;
	final IProductProductTypeService iProductProductTypeService;
	
    
    @ApiOperation(value = "获取产品类型")
    @GetMapping("/refreshProductType")
    public Result<?> captureProductTypeAction(String groupid,String marketplaceid) {
    	AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(groupid, marketplaceid);
	    iProductProductTypeService.captureProductType(auth, marketplaceid);
        return Result.success("");
    }

    @ApiOperation(value = "获取产品类型")
    @PostMapping("/searchDefinitionsProductTypes")
    public Result<?> searchDefinitionsProductTypesAction(@RequestBody DefinitionsProductTypesDTO dto) {
        AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(dto.getGroupid(), dto.getMarketplaceid());
        return Result.success(iProductProductTypeService.searchDefinitionsProductTypes(auth, dto));
    }

    
    @ApiOperation(value = "根据类型名称获取信息")
    @PostMapping("/getProductDefinition")
    public Result<?> getProductDefinitionAction(@RequestBody DefinitionsProductTypesDTO dto) {
    	AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(dto.getGroupid(), dto.getMarketplaceid());
	    ProductTypeDefinition res = iProductProductTypeService.captureTypeDefinition(auth, dto);
        return Result.success(res);
    }


    @ApiOperation(value = "根据类型名称获取信息")
    @GetMapping("/getSchema")
    public Result<?> getgetSchemaAction(String url) {
        String json = loadJson(url);
        return Result.success(json);
    }

    public static String loadJson(String url) {
        StringBuilder json = new StringBuilder();
        BufferedReader in = null;
        try {
            URL urlObject = new URL(url);
            URLConnection uc = urlObject.openConnection();
            in = new BufferedReader(new InputStreamReader(uc.getInputStream(), StandardCharsets.UTF_8));
            String inputLine = null;
            while ((inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return json.toString();
    }

}

