package com.wimoor.erp.inventory.mapper;



import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.erp.inventory.pojo.entity.InventoryRecord;
@Mapper
public interface InventoryRecordMapper  extends BaseMapper<InventoryRecord> {
  	//根据信息确认当前SKU的record对象
}