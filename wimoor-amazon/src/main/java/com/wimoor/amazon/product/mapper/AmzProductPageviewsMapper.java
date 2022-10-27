package com.wimoor.amazon.product.mapper;

import com.wimoor.amazon.product.pojo.dto.AmzProductPageviewsDTO;
import com.wimoor.amazon.product.pojo.entity.AmzProductPageviews;
import com.wimoor.amazon.product.pojo.vo.AmzProductPageviewsConditionVo;
import com.wimoor.amazon.product.pojo.vo.AmzProductPageviewsVo;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 流量报表 Mapper 接口
 * </p>
 *
 * @author wimoor team
 * @since 2022-06-28
 */
@Mapper
public interface AmzProductPageviewsMapper extends BaseMapper<AmzProductPageviews> {
	List<AmzProductPageviews> findPageviews(Map<String,Object> paramter);
	void deleteByMarketplaceid(@Param("marketplaceid")String marketplaceid,
							   @Param("amazonauthid")String amazonauthid,
							   @Param("byday")String byday);
	void saveBatch(List<AmzProductPageviews> list);

	List<AmzProductPageviewsConditionVo> downloadAuth();
	IPage<AmzProductPageviewsVo> getPageViewsList(Page<?> page,@Param("dto")AmzProductPageviewsDTO dto);
	void downloadRefresh(AmzProductPageviewsConditionVo item);
	
	void refreshSummaryWeek(Map<String,Object> param);
	void refreshSummaryMonth(Map<String,Object> param);
	
}
