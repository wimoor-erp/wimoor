package com.wimoor.erp.inventory.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.erp.inventory.mapper.InventoryRecordMapper;
import com.wimoor.erp.inventory.pojo.entity.InventoryRecord;
import com.wimoor.erp.inventory.service.IInventoryRecordService;

import lombok.RequiredArgsConstructor;

@Service("inventoryRecordService")
@RequiredArgsConstructor
public class InventoryRecordServiceImpl extends ServiceImpl<InventoryRecordMapper,InventoryRecord> implements IInventoryRecordService {

}
