package com.wimoor.erp.assembly.service;


import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.erp.assembly.pojo.vo.AssemblyVO;
import com.wimoor.erp.assembly.pojo.entity.Assembly;

public interface IAssemblyService extends IService<Assembly> {

	List<AssemblyVO> selectByMainmid(String id);
	IPage<Assembly> findByCondition(Page<?> page ,String search, String shopid);
	/**
	 * 删除没有绑定mainmid的数据
	 */
	void deleteIsNull();

	void deleteByMainmid(String id);
	
	
	List<Assembly> selectAssemblySub(String mainmid);
	public List<Assembly> selectBySubid(String materialid);
	List<Map<String, Object>> selectBySubid(String materialid,String shopid);
	List<Map<String, Object>> selectByMainDetailmid(String materialid,String warehouseid);
	Integer findCanAssembly(String materialid, String warehouseid, String shopid);

}
