package com.wimoor.erp.material.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.erp.material.pojo.entity.MaterialCustom;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MaterialCustomMapper extends BaseMapper<MaterialCustom>{
	  @Select({"<script>",
          " SELECT",
          " 	i.* ",
          " FROM",
          " 	t_erp_material_custom i ",
          " left join	t_erp_material  b on b.id=i.materialid",
          " WHERE b.sku= #{msku} and b.shopid=#{shopid}  ",
          "</script>"})
	MaterialCustom getCustom(@Param("shopid")String shopid, @Param("msku")String msku );
     
}