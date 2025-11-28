package com.wimoor.erp.thirdparty.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.erp.thirdparty.pojo.dto.ThirdpartyWarehouseInvDTO;
import com.wimoor.erp.thirdparty.pojo.entity.ThirdPartyWarehouseInventoryXL;
import com.wimoor.erp.thirdparty.pojo.entity.ThirdPartyWarehouseInventoryYC;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ThirdPartyWarehouseInventoryMapperYC extends BaseMapper<ThirdPartyWarehouseInventoryYC> {
    List<Map<String, Object>> findByDto(ThirdpartyWarehouseInvDTO dto);
}
