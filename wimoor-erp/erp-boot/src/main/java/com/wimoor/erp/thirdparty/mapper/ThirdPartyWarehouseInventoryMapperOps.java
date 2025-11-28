package com.wimoor.erp.thirdparty.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.erp.thirdparty.pojo.dto.ThirdpartyWarehouseInvDTO;
import com.wimoor.erp.thirdparty.pojo.entity.ThirdPartyWarehouseInventoryOps;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ThirdPartyWarehouseInventoryMapperOps extends BaseMapper<ThirdPartyWarehouseInventoryOps> {
    List<Map<String, Object>> findByDto(ThirdpartyWarehouseInvDTO dto);
}
