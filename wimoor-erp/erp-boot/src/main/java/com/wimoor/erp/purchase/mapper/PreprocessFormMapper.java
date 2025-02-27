package com.wimoor.erp.purchase.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.erp.purchase.pojo.dto.PreprocessFormListDTO;
import com.wimoor.erp.purchase.pojo.entity.PreprocessForm;
import com.wimoor.erp.ship.pojo.dto.ShipInboundItemVo;
import com.wimoor.erp.ship.pojo.dto.ShipInboundShipmenSummarytVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface PreprocessFormMapper extends BaseMapper<PreprocessForm> {

    IPage<Map<String, Object>> getPreprocessFormList(Page<Object> page,@Param("param") PreprocessFormListDTO dto);

    ShipInboundShipmenSummarytVo findSummaryInfo(@Param("list")List<String> recids);

    List<ShipInboundItemVo> findItemListByRecids(@Param("list")List<String> recids);
}
