package com.wimoor.erp.warehouse.service;

import com.wimoor.erp.warehouse.pojo.dto.ShelfInvListDto;
import com.wimoor.erp.warehouse.pojo.entity.WarehouseShelfInventoryOptRecord;
import com.wimoor.erp.warehouse.pojo.vo.WarehouseShelfInventoryOptRecordVo;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 操作记录 服务类
 * </p>
 *
 * @author wimoor team
 * @since 2022-03-19
 */
public interface IWarehouseShelfInventoryOptRecordService extends IService<WarehouseShelfInventoryOptRecord> {

	List<WarehouseShelfInventoryOptRecordVo> getRecordVo(String shopid, String formid, String formtype, String materialid);
	public List<WarehouseShelfInventoryOptRecord> getRecord(String shopid, String formid, String formtype,String shelfid) ;
	IPage<Map<String, Object>> getOptList(ShelfInvListDto condition, String shopid);
}
