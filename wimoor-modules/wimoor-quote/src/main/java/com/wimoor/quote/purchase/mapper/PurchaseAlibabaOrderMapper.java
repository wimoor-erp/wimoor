package com.wimoor.quote.purchase.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.quote.purchase.pojo.entity.PurchaseAlibabaOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* @author liufei
* @description 针对表【t_purchase_alibaba_order】的数据库操作Mapper
* @createDate 2025-06-07 11:30:18
* @Entity com.wimoor.quote.purchase.pojo.entity.PurchaseAlibabaOrder
*/
@Mapper
public interface PurchaseAlibabaOrderMapper extends BaseMapper<PurchaseAlibabaOrder> {

    IPage<Map<String, Object>> findByCondition(Page<Object> page,@Param("search") String search,
                                               @Param("buyerid") String buyerid, @Param("fromDate") String fromDate,@Param("toDate") String toDate);
}





