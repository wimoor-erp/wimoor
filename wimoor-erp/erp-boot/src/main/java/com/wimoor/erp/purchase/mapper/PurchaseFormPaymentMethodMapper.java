package com.wimoor.erp.purchase.mapper;


import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormPaymentMethod;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface PurchaseFormPaymentMethodMapper extends BaseMapper<PurchaseFormPaymentMethod>{


    List<PurchaseFormPaymentMethod> getMethodByShopid(@Param("shopid") String shopid);

    void savePaymethodIndex(Map<String, Object> item);
}
