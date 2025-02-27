package com.wimoor.admin.mapper;

import java.math.BigInteger;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.admin.pojo.entity.SysUserShop;
 
@Mapper
public interface SysUserShopMapper extends BaseMapper<SysUserShop> {
    @Select("<script>" +
            "select u.id from t_user_shop t "+
    		"left join t_user u on u.id=t.user_id "+
            "where t.shop_id=#{shopid} and u.`disable`=0 and u.logicDelete=0 and u.losingEffect>curdate() "+
            "</script>")
    BigInteger findByCompanyId(BigInteger CompanyId);
}