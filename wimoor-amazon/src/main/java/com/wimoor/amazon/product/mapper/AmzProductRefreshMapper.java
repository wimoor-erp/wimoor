package com.wimoor.amazon.product.mapper;

import com.wimoor.amazon.product.pojo.entity.AmzProductRefresh;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wimoor team
 * @since 2022-06-17
 */
@Mapper
public interface AmzProductRefreshMapper extends BaseMapper<AmzProductRefresh> {

	void insertDefault();

	AmzProductRefresh findForDetailRefresh(@Param("amazonauthid") String amazonauthid);
	
	List<AmzProductRefresh> findForCatalogRefresh(@Param("amazonauthid") String amazonauthid);

	List<AmzProductRefresh> findForPriceRefresh(@Param("amazonauthid")String amazonauthid);

}
