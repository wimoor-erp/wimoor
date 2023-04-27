package com.wimoor.erp.material.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.erp.material.pojo.entity.MaterialCustomsFile;
@Mapper
public interface MaterialCustomsFileMapper  extends BaseMapper<MaterialCustomsFile>{

	List<Map<String,Object>> selectCustomsRecordBySku(String materialid);

	MaterialCustomsFile findCustomsByNameAndSku(@Param("materialid")String materialid, @Param("filename")String filename);
 
}