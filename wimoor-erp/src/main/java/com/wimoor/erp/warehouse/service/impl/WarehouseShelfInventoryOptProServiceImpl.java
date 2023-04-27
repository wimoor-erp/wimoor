package com.wimoor.erp.warehouse.service.impl;

import com.wimoor.erp.warehouse.pojo.entity.WarehouseShelfInventoryOptPro;
import com.wimoor.erp.warehouse.mapper.WarehouseShelfInventoryOptProMapper;
import com.wimoor.erp.warehouse.service.IWarehouseShelfInventoryOptProService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 预操作，此表内不存储任何记录。当预操作结束后自动删除 服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2022-03-19
 */
@Service
public class WarehouseShelfInventoryOptProServiceImpl extends ServiceImpl<WarehouseShelfInventoryOptProMapper, WarehouseShelfInventoryOptPro> implements IWarehouseShelfInventoryOptProService {

}
