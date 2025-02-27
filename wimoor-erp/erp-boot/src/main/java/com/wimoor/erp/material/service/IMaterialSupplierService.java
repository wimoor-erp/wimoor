package com.wimoor.erp.material.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.pojo.entity.MaterialSupplier;
import com.wimoor.erp.material.pojo.entity.MaterialSupplierStepwise;
import com.wimoor.erp.material.pojo.vo.MaterialSupplierVO;

public interface IMaterialSupplierService  extends IService<MaterialSupplier> {

	Map<String, Object> getPriceBySupplier(String supplierid, String materialid, Integer amount);
	
	public int deleteMaterialSupplierStepwise(String materialid,String supplierid);
	
	void saveOrUpdateSupplier(List<MaterialSupplierVO> supplierlist, UserInfo user, String id, Material material);
	
	List<MaterialSupplier> selectSupplierByMaterialId(String id);

	List<MaterialSupplierVO> selectSupplierByMainmid(String string);
	
	int saveMaterialSupplierStepwise(MaterialSupplierStepwise mss);
	
	List<Map<String, Object>> selectSupplerOtherById(String string);
	
	public MaterialSupplier selectSupplier(String materialid,String supplierid) ;
	
	public void updatePriceOfDefaultSupplier(UserInfo user,List<MaterialSupplierStepwise> prices);
}
