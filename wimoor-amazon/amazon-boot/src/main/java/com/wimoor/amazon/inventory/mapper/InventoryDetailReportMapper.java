package com.wimoor.amazon.inventory.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.inventory.pojo.entity.InventoryDetailReport;

import java.util.List;

@Mapper
public interface InventoryDetailReportMapper extends BaseMapper<InventoryDetailReport> {

    void insertBatch(List<InventoryDetailReport> list);
}
