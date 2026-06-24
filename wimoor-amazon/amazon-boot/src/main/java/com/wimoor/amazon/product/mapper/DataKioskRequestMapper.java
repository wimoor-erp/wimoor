package com.wimoor.amazon.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.product.pojo.entity.DataKioskRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author liufei
* @description 针对表【t_amz_data_kiosk_request】的数据库操作Mapper
* @createDate 2026-02-03 09:08:03
* @Entity com.wimoor.amazon.product.pojo.entity.DataKioskRequest
*/
@Mapper
public interface DataKioskRequestMapper extends BaseMapper<DataKioskRequest> {

    List<DataKioskRequest> getDocument();

    List<DataKioskRequest> getDocId();
}




