package com.wimoor.manager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.manager.pojo.dto.CustomerOrderListDTO;
import com.wimoor.manager.pojo.dto.CustomerOrderQueryDTO;
import com.wimoor.manager.pojo.entity.SysCustomerOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *   Mapper 接口
 * </p>
 *
 * @author wimoor team
 * @since 2022-09-27
 */
@Mapper
public interface SysCustomerOrderMapper extends BaseMapper<SysCustomerOrder> {

    IPage<CustomerOrderListDTO> selectOrderList(IPage<CustomerOrderListDTO> page, @Param("query") CustomerOrderQueryDTO query);

    IPage<CustomerOrderListDTO> selectOrderAllList(IPage<CustomerOrderListDTO> page,@Param("query")  CustomerOrderQueryDTO query);
}