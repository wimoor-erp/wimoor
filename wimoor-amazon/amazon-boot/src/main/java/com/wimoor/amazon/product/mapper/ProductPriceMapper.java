package com.wimoor.amazon.product.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.amazon.product.pojo.dto.ProductPriceDTO;
import com.wimoor.amazon.product.pojo.entity.ProductPrice;
import com.wimoor.amazon.product.pojo.vo.ProductPriceVo;

@Mapper
public interface ProductPriceMapper extends BaseMapper<ProductPrice>{

	int insert(ProductPrice record);

	ProductPrice selectByAsinMarketPlace(@Param("asin") String asin, @Param("marketplaceid") String marketplaceid);

	List<ProductPrice> findbyProductID(@Param("id") String id);

	ProductPrice findoneBypro(@Param("marketplaceid") String marketplaceid, @Param("sku") String sku,
			@Param("asin") String asin);

	List<ProductPrice> selectBySkuAndSeller(@Param("sku") String sku, @Param("marketplaceid") String marketplaceid,
			@Param("sellerid") String sellerid);

	List<String> selectProductPriceHis(String amazonAuthId);
	
	IPage<ProductPriceVo> priceQueue(Page<?> page,@Param("param") ProductPriceDTO param);
}