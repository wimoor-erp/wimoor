package com.wimoor.amazon.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wimoor.amazon.product.pojo.dto.ProductListingItemDTO;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;

import feign.FeignException;


 
@Component
public class AmazonClientOneFeignManager {
	@Autowired
	AmazonClientOneFeign api;
	public Map<String,Object> pushAsinAction(ProductListingItemDTO dto){
		try {
			Result<Map<String,Object>> result = api.pushAsinAction(dto);
			if(!Result.isSuccess(result)) {
				throw new BizException("上架Asin失败");
			}
			return result.getData();
		}catch(FeignException e) {
			throw new BizException(BizException.getMessage(e, "上架Asin失败"));
		}
		
	}

	public Map<String,Object> saveAsinAction( ProductListingItemDTO dto){
		try {
			Result<Map<String,Object>> result =api.saveAsinAction(dto);
			if(!Result.isSuccess(result)) {
				throw new BizException("保存Asin失败");
			}
			return result.getData();
		}catch(FeignException e) {
			throw new BizException(BizException.getMessage(e, "保存Asin失败"));
		}
	}

	public void deleteSkuAction(ProductListingItemDTO dto){
		try {
			Result<?> result =api.deleteSkuAction(dto);
			if(!Result.isSuccess(result)) {
				throw new BizException("删除Asin失败");
			}
		}catch(FeignException e) {
			throw new BizException(BizException.getMessage(e, "删除Asin失败"));
		}
	}
	
	public void recordFollowListAction(ProductListingItemDTO dto){
		try {
			Result<?> result =api.recordFollowListAction(dto);
			if(!Result.isSuccess(result)) {
				throw new BizException("删除Asin失败");
			}
		}catch(FeignException e) {
			throw new BizException(BizException.getMessage(e, "删除Asin失败"));
		}
	}
	
	public Map<String,Object> refreshInfoBySKUAction(ProductListingItemDTO dto){
		try {
			@SuppressWarnings("unchecked")
			Result<Map<String,Object>> result =	(Result<Map<String, Object>>) api.refreshItemInfoAction(dto);
			if(!Result.isSuccess(result)) {
				throw new BizException("刷新Asin失败");
			}
			return result.getData();
		}catch(FeignException e) {
			throw new BizException(BizException.getMessage(e, "刷新Asin失败"));
		}
	
	}
    
}