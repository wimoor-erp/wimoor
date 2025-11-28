package com.wimoor.amazon.profit.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.profit.pojo.entity.ReferralFee;
@Mapper
public interface ReferralFeeMapper  extends BaseMapper<ReferralFee> {

    ReferralFee findByTypeId(@Param("id") Integer id,@Param("country") String country);
    //新增方法
    List<ReferralFee> findAllType();
    
    ReferralFee findByPgroup(@Param("pgroup") String  pgroup,@Param("country") String country);
}
