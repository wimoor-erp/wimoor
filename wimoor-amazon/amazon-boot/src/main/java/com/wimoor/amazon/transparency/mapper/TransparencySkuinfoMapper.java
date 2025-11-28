package com.wimoor.amazon.transparency.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.amazon.transparency.pojo.dto.TransparencyDTO;
import com.wimoor.amazon.transparency.pojo.entity.TransparencySkuinfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
* @author liufei
* @description 针对表【t_amz_transparency_skuinfo】的数据库操作Mapper
* @createDate 2025-08-08 11:31:18
* @Entity com.wimoor.amazon.transparency.pojo.entity.TransparencySkuinfo
*/
@Mapper
public interface TransparencySkuinfoMapper extends BaseMapper<TransparencySkuinfo> {

    IPage<Map<String, Object>> listSkuinfo(Page<Object> page, @Param("param") TransparencyDTO dto);
}




