package com.wimoor.erp.material.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.erp.material.pojo.entity.MaterialCustoms;
@Mapper
public interface MaterialCustomsMapper extends BaseMapper<MaterialCustoms>{
	  @Select({"<script>",
          " SELECT",
          " 	i.* ",
          " FROM",
          " 	t_erp_material_customs i ",
          " left join	t_erp_material  b on b.id=i.materialid",
          " WHERE b.sku= #{msku} and b.shopid=#{shopid} and i.country=#{country}",
          "</script>"})
	MaterialCustoms getCustoms(@Param("shopid")String shopid, @Param("msku")String msku,@Param("country")String country);
     
}