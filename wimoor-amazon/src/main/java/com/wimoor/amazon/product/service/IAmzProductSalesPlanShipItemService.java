package com.wimoor.amazon.product.service;

import com.wimoor.amazon.product.pojo.entity.AmzProductSalesPlanShipItem;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wimoor team
 * @since 2022-12-10
 */
public interface IAmzProductSalesPlanShipItemService extends IService<AmzProductSalesPlanShipItem> {
	Map<String,Object> getSummary(String shopid,String groupid,String warehouseid);
	List<Map<String,Object>> getList(String companyid, String groupid, String warehouseid, String batchnumber);
	  int updateBatch( String id, String batchnumber);
	  public int moveBatch(String shopid,String batchnumber);
}
