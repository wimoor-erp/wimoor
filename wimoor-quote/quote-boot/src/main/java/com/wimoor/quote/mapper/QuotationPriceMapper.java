package com.wimoor.quote.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.quote.api.entity.QuotationPrice;
import org.apache.ibatis.annotations.Mapper;
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
}
