package com.wimoor.amazon.orders.mapper;


 
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.orders.pojo.entity.AmzOrderItem;
import com.wimoor.amazon.orders.pojo.vo.AmazonOrdersDetailVo;

/**
 * <p>
 * 订单备注 Mapper 接口
 * </p>
 *
 * @author wimoor team
 * @since 2022-05-14
 */
@Mapper
public interface AmzOrderItemMapper extends BaseMapper<AmzOrderItem> {

	List<AmazonOrdersDetailVo> selectOrderDetail(@Param("param") Map<String,Object> maps);

	void removeDataArchive(@Param("amazonAuthId") String amazonAuthId);
}
