package com.wimoor.quote.ship.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.quote.ship.pojo.entity.UserSupplier;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserSupplierMapper extends BaseMapper<UserSupplier> {
    @Select({"<script>",
            " SELECT",
            " 	i.* ",
            " FROM",
            " 	t_user_supplier i ",
            " WHERE i.buyerid= #{buyerid} and ifnull(disabled,false)=false",
            "</script>"})
    List<UserSupplier> listSupplier(String buyerid);
}
