package com.wimoor.quote.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.quote.api.entity.UserSupplier;
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
            " left join	t_supply_relationship  b on b.supplierid=i.id",
            " WHERE b.buyerid= #{buyerid} ",
            "</script>"})
    List<UserSupplier> listSupplier(String buyerid);
}
