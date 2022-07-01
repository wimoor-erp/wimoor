package com.wimoor.erp.inventory.service.impl;

 
import org.springframework.stereotype.Service;

import com.wimoor.common.mvc.BizException;
import com.wimoor.erp.inventory.pojo.entity.InventoryParameter;
import com.wimoor.erp.inventory.service.IInventoryFormAgentService;
import com.wimoor.erp.inventory.service.IInventoryService;

import lombok.RequiredArgsConstructor;
 
//库存操作 
@Service("inventoryFormAgentService")
@RequiredArgsConstructor
public class InventoryFormAgentServiceImpl implements IInventoryFormAgentService {
	 
	IInventoryService inventoryService;
 

	public Integer inStockByDirect(InventoryParameter invpara) throws BizException {
		int result1 = inventoryService.inStockByDirect(invpara);
		return result1;
	}

	public Integer inStockByRead(InventoryParameter invpara) throws BizException {
		int result1 = inventoryService.inStockByReady(invpara);
		return result1;
	}

	public Integer outStockByDirect(InventoryParameter invpara) throws BizException {
		int result1 = inventoryService.outStockByDirect(invpara);
		return result1;
	}

	public Integer outStockByRead(InventoryParameter invpara) throws BizException {
		int result1 = inventoryService.outStockByReady(invpara);
		return result1;
	}

	public Integer outStockByReadChange(InventoryParameter invpara) throws BizException {
		int result1 = inventoryService.outStockByReadyChange(invpara);
		return result1;
	}

	public Integer outStockReadyChange(InventoryParameter invpara) throws BizException {
		int result1 = inventoryService.outStockReadyChange(invpara);
		return result1;
	}

	public Integer inStockDirectCancel(InventoryParameter invpara) throws BizException {
		int result1 = inventoryService.inStockDirectCancel(invpara);
		return result1;
	}

	public Integer outStockDirectCancel(InventoryParameter invpara) throws BizException {
		int result1 = inventoryService.outStockDirectCancel(invpara);
		return result1;
	}

	public Integer outStockReadyCancel(InventoryParameter invpara) throws BizException {
		int result1 = inventoryService.outStockReadyCancel(invpara);
		return result1;
	}
	public Integer inStockReadyCancel(InventoryParameter invpara) throws BizException {
		int result1 = inventoryService.inStockReadyCancel(invpara);
		return result1;
	}
	
	// 入库操作 根据单据去循环添加多个inventory对象和多个record对象
//	public Integer inStockByForm(ErpBaseForm form, FormType formtype) throws BizException {
//		// 去根据单据构造inventory和record的参数
//		// (状态为直接入库)操作record和inventory两表
//		// 单据的sku个数
//		int resultCount = 0;
//		try {
//			int length = form.getEntryList().size();
//			int result = 0;
//			for (int i = 0; i < length; i++) {
//				// 未提交(直接入库的)
//				if (form.getAuditstatus() == 0) {
//					InventoryParameter invpara = setInvenPara(form, i);
//					result = inStockByDirect(invpara);
//
//					if (result < 2) {
//						return -1;
//					} else {
//						resultCount = result + resultCount;
//					}
//				}
//				// 已提交未审核
//				if (form.getAuditstatus() == 1) {
//					InventoryParameter invpara = setInvenPara(form, i);
//					result = inStockByRead(invpara);
//					if (result < 2) {
//						return -1;
//					} else {
//						resultCount = result + resultCount;
//					}
//				}
//				// 已审核
//				if (form.getAuditstatus() == 2) {
//					InventoryParameter invpara = setInvenPara(form, i);
//					result = inStockByRead(invpara);
//					if (result < 2) {
//						return -1;
//					} else {
//						resultCount = result + resultCount;
//					}
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return resultCount;
//	}

	// 出库操作
//	public Integer outStockByForm(ErpBaseForm form, FormType formtype) throws BizException {
//		int resultCount = 0;// 记录总操作条数
//		try {
//			// 单据内sku个数
//			int length = form.getEntryList().size();
//			int result = 0;
//			for (int i = 0; i < length; i++) {
//				// 未提交(直接出库的)
//				if (form.getAuditstatus() == 0) {
//					InventoryParameter invpara = setInvenPara(form, i);
//					result = outStockByDirect(invpara);
//					if (result < 2) {
//						return -1;
//					} else {
//						resultCount = result + resultCount;
//					}
//				}
//				// 已提交未审核
//				if (form.getAuditstatus() == 1) {
//					InventoryParameter invpara = setInvenPara(form, i);
//					int result1 = inventoryService.outStockByReady(invpara);
//					result = result1;
//					if (result < 2) {
//						return -1;
//					} else {
//						resultCount = result + resultCount;
//					}
//				}
//				// 已审核
//				if (form.getAuditstatus() == 2) {
//					InventoryParameter invpara = setInvenPara(form, i);
//					int result1 = inventoryService.outStockByReady(invpara);
//					result = result1;
//					if (result < 2) {
//						return -1;
//					} else {
//						resultCount = result + resultCount;
//					}
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return resultCount;
//	}

	// 撤销入库操作
//	public Integer cancelInStockByForm(ErpBaseForm form, FormType formtype) throws BizException {
//		int resultCount = 0;
//		try {
//			// 单据内sku个数
//			int length = form.getEntryList().size();
//			int result = 0;
//			for (int i = 0; i < length; i++) {
//				// 未提交
//				if (form.getAuditstatus() == 0) {
//					InventoryParameter invpara = setInvenPara(form, i);
//					int result1 = inventoryService.inStockDirectCancel(invpara);
//					invpara.setAmount(invpara.getAmount() * -1);
//					result = result1;
//					if (result < 2) {
//						return -1;
//					} else {
//						resultCount = result + resultCount;
//					}
//				}
//				// 已提交未审核
//				if (form.getAuditstatus() == 1) {
//					InventoryParameter invpara = setInvenPara(form, i);
//					int result1 = inventoryService.inStockReadyCancel(invpara);
//					invpara.setAmount(invpara.getAmount() * -1);
//					result = result1;
//					if (result < 2) {
//						return -1;
//					} else {
//						resultCount = result + resultCount;
//					}
//				}
//				// 已审核
//				if (form.getAuditstatus() == 2) {
//					InventoryParameter invpara = setInvenPara(form, i);
//					int result1 = inventoryService.inStockReadyCancel(invpara);
//					invpara.setAmount(invpara.getAmount() * -1);
//					result = result1;
//					if (result < 2) {
//						return -1;
//					} else {
//						resultCount = result + resultCount;
//					}
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return resultCount;
//	}

	// 撤销出库操作
//	public Integer cancelOutStockByForm(ErpBaseForm form, FormType formtype) throws BizException {
//		int resultCount = 0;
//		try {
//			// 单据内sku个数
//			int length = form.getEntryList().size();
//			int result = 0;
//			for (int i = 0; i < length; i++) {
//				// 未提交
//				if (form.getAuditstatus() == 0) {
//					InventoryParameter invpara = setInvenPara(form, i);
//					int result1 = inventoryService.outStockDirectCancel(invpara);
//					invpara.setAmount(invpara.getAmount() * -1);
//					result = result1;
//					if (result < 2) {
//						return -1;
//					} else {
//						resultCount = result + resultCount;
//					}
//				}
//				// 已提交未审核
//				if (form.getAuditstatus() == 1) {
//					InventoryParameter invpara = setInvenPara(form, i);
//					int result1 = inventoryService.outStockReadyCancel(invpara);
//					invpara.setAmount(invpara.getAmount() * -1);
//					result = result1;
//					if (result < 2) {
//						return -1;
//					} else {
//						resultCount = result + resultCount;
//					}
//				}
//				// 已审核
//				if (form.getAuditstatus() == 2) {
//					InventoryParameter invpara = setInvenPara(form, i);
//					int result1 = inventoryService.outStockReadyCancel(invpara);
//					result = result1;
//					if (result < 2) {
//						return -1;
//					} else {
//						resultCount = result + resultCount;
//					}
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return resultCount;
//	}

}
