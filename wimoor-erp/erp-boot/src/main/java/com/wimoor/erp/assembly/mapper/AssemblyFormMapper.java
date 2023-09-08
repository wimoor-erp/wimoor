package com.wimoor.erp.assembly.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.erp.assembly.pojo.dto.AssemblyFormListDTO;
import com.wimoor.erp.assembly.pojo.entity.AssemblyForm;
@Mapper
public interface AssemblyFormMapper extends BaseMapper<AssemblyForm>{
	
	IPage<Map<String, Object>> findByCondition(Page<?>  page,@Param("param")AssemblyFormListDTO dto);
	
	Map<String, Object> findById(String id);
	
	IPage<AssemblyForm> findLastByMaterial(Page<?>  page,@Param("materialid")String materialid);
	
	Integer findSumNeed(String shopid);
	
	Map<String, Object> getLastAssRecord(@Param("shopid")String shopid, @Param("warehouseid")String warehouseid);
	
	List<Map<String, Object>> getAssemblySumReport(Map<String, Object> param);
	
	List<AssemblyForm> getAssemblyFormByWarehouseid(@Param("warehouseid")String warehouseid);
	
	List<Map<String, Object>> getCountNum(Map<String, Object> param);
	
	void refreshInbound(@Param("shopid")String shopid, @Param("warehouseid")String warehouseid, @Param("materialid")String materialid);
	
	List<AssemblyForm> getCanAssemblyFormByMaterial(@Param("shopid")String shopid, @Param("warehouseid")String warehouseid, @Param("materialid")String materialid);

	List<AssemblyForm> findLastByMaterials(@Param("list")List<String> ids);
}
