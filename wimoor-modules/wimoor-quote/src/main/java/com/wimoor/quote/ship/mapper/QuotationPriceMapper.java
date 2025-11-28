package com.wimoor.quote.ship.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.quote.ship.pojo.entity.QuotationPrice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface QuotationPriceMapper extends BaseMapper<QuotationPrice> {
    @Select({"<script>",
            " SELECT",
            " 	i.*,b.price ",
            " FROM",
            " 	t_user_supplier i ",
            " left join	t_supplier_quotation_price  b on b.supplierid=i.id",
            " WHERE i.orderid= #{orderid} ",
            "</script>"})
    List<Map<String, Object>> list(String orderid);


    List<Map<String, Object>> selectQuoteInfo(@Param("orderid") String orderid,@Param("supplierid") String supplierid);
}
