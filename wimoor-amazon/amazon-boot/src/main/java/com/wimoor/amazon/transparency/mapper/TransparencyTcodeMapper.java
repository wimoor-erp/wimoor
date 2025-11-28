package com.wimoor.amazon.transparency.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.amazon.transparency.pojo.dto.TransparencyDTO;
import com.wimoor.amazon.transparency.pojo.entity.TransparencyTcode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* @author liufei
* @description 针对表【t_amz_transparency_tcode】的数据库操作Mapper
* @createDate 2025-08-08 11:31:32
* @Entity com.wimoor.amazon.transparency.pojo.entity.TransparencyTcode
*/
@Mapper
public interface TransparencyTcodeMapper extends BaseMapper<TransparencyTcode> {

    IPage<Map<String, Object>> listTcode(Page<Object> page,@Param("param") TransparencyDTO dto);

    List<Map<String,Object>> listTcode(@Param("param") TransparencyDTO dto);
}




