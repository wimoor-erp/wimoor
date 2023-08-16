package com.wimoor.erp.material.service.impl;

import com.wimoor.erp.material.pojo.entity.MaterialConsumableInventory;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.material.mapper.MaterialConsumableInventoryMapper;
import com.wimoor.erp.material.service.IMaterialConsumableInventoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2023-05-09
 */
@Service
public class MaterialConsumableInventoryServiceImpl extends ServiceImpl<MaterialConsumableInventoryMapper, MaterialConsumableInventory> implements IMaterialConsumableInventoryService {

	@Override
	public Boolean save(UserInfo user, String materialid, String warehouseid, BigDecimal residue) {
		// TODO Auto-generated method stub
		LambdaQueryWrapper<MaterialConsumableInventory> query=new LambdaQueryWrapper<MaterialConsumableInventory>();
		query.eq(MaterialConsumableInventory::getShopid, user.getCompanyid());
		query.eq(MaterialConsumableInventory::getMaterialid, materialid);
		query.eq(MaterialConsumableInventory::getWarehouseid, warehouseid);
		MaterialConsumableInventory old = this.baseMapper.selectOne(query);
		if(old!=null) {
			old.setQuantity(residue);
			return this.baseMapper.updateById(old)>0;
		}else {
			MaterialConsumableInventory inv=new MaterialConsumableInventory();
			inv.setMaterialid(materialid);
			inv.setWarehouseid(warehouseid);
			inv.setShopid(user.getCompanyid());
			inv.setQuantity(residue);
			return this.baseMapper.insert(inv)>0;
		}
	}

}
