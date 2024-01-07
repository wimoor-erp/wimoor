package com.wimoor.erp.warehouse.service;

import com.wimoor.erp.warehouse.pojo.entity.ErpWarehouseAddress;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wimoor team
 * @since 2022-10-29
 */
public interface IErpWarehouseAddressService extends IService<ErpWarehouseAddress> {

	ErpWarehouseAddress getByNumber(String companyid, String addressnum);

}
