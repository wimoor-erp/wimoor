package com.wimoor.erp.warehouse.service.impl;

import com.wimoor.erp.warehouse.pojo.entity.ErpWarehouseAddress;
import com.wimoor.erp.warehouse.mapper.ErpWarehouseAddressMapper;
import com.wimoor.erp.warehouse.service.IErpWarehouseAddressService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2022-10-29
 */
@Service
public class ErpWarehouseAddressServiceImpl extends ServiceImpl<ErpWarehouseAddressMapper, ErpWarehouseAddress> implements IErpWarehouseAddressService {

	@Override
	public ErpWarehouseAddress getByNumber(String companyid, String addressnum) {
		// TODO Auto-generated method stub
		LambdaQueryWrapper<ErpWarehouseAddress> query =new LambdaQueryWrapper<ErpWarehouseAddress>();
		query.eq(ErpWarehouseAddress::getShopid, companyid);
		query.eq(ErpWarehouseAddress::getNumber, addressnum);
		return this.getOne(query);
	}

}
