package com.wimoor.erp.assembly.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.erp.assembly.pojo.vo.AssemblyVO;
import com.wimoor.erp.assembly.pojo.entity.Assembly;
@Mapper
public interface AssemblyMapper  extends BaseMapper<Assembly> {

	List<AssemblyVO> selectByMainmid(@Param("mainmid")String mainmid);

	void deleteIsNull();
	
    List<Map<String,Object>> selectMainsDetailBySku(Map<String,Object> param);
    
	IPage<Assembly> findByCondition(Page<?>  page,String search, String shopid);

	void deleteByMainmid(String mainmid);
	
	List<Map<String,Object>>  selectByMainDetailmid(@Param("mainmid")String mainmid,@Param("warehouseid")String warehouseid);
	List<Assembly> selectAssemblySub(@Param("mainmid")String mainmid);

	List<Map<String,Object>> selectMainBySubid(@Param("submid")String submid,@Param("shopid")String shopid);

	Integer findCanAssembly(@Param("materialid")String materialid,@Param("warehouseid") String warehouseid,@Param("shopid") String shopid);
	
	Integer findCanAssemblyByInventory(@Param("materialid")String materialid,@Param("warehouseid") String warehouseid,@Param("shopid") String shopid);
}