package com.wimoor.amazon.product.service;

import com.wimoor.amazon.product.pojo.dto.ProductPriceDTO;
import com.wimoor.amazon.product.pojo.entity.ProductInOpt;
import com.wimoor.amazon.product.pojo.entity.ProductPrice;
import com.wimoor.amazon.product.pojo.vo.ProductPriceVo;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 产品信息 服务类
 * </p>
 *
 * @author wimoor team
 * @since 2022-05-27
 */
public interface IProductInOptService extends IService<ProductInOpt> {

	void refreshAllProductAdv();
	public  IPage<ProductPriceVo>  priceQueue(ProductPriceDTO dto);
	public List<Map<String, Object>> findMaterialSizeByCondition(Map<String, Object> param);
	public List<Map<String, Object>> getProRemarkHis(String pid,String ftype);
	public List<ProductPrice> findPrice(String pid);
	public List<Map<String, Object>> saveTagsByPid(String pid,String tagids,String userid);
	String findProductTagsByPid(String pid);
}
