package com.wimoor.amazon.inventory.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.amazon.inventory.pojo.dto.InventoryPlanningDTO;
import com.wimoor.amazon.inventory.pojo.entity.AmzInventoryPlanning;
import com.wimoor.amazon.inventory.pojo.vo.AmzInventoryPlanningVo;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wimoor team
 * @since 2022-12-07
 */
@Mapper
public interface AmzInventoryPlanningMapper extends BaseMapper<AmzInventoryPlanning> {
	
	IPage<AmzInventoryPlanningVo> selectPageList(Page<?> page,@Param("param") InventoryPlanningDTO param);
	List<AmzInventoryPlanningVo> selectPageList(@Param("param") InventoryPlanningDTO param);
	AmzInventoryPlanningVo selectPageListSummary(@Param("param")InventoryPlanningDTO param);
}
