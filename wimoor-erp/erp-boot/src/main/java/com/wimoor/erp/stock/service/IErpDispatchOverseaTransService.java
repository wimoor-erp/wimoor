package com.wimoor.erp.stock.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.erp.stock.pojo.entity.ErpDispatchOverseaTrans;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wimoor team
 * @since 2023-02-24
 */
public interface IErpDispatchOverseaTransService extends IService<ErpDispatchOverseaTrans> {
    Map<String,Object> getInfo(String id);

    IPage<Map<String, Object>> getShipFeeDetailReport(Page<Object> page, Map<String, Object> param);

    void setShipFeeDetailReport(SXSSFWorkbook workbook, Map<String, Object> params);

    IPage<Map<String, Object>> transSKUFeeShared(Page<Object> page, Map<String, Object> param);

    void setShipFeeReport(SXSSFWorkbook workbook, Map<String, Object> params);

    IPage<Map<String, Object>> getShipFeeReport(Page<Object> page, Map<String, Object> param);

}
