package com.wimoor.erp.change.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wimoor.common.mvc.FileUpload;
import com.wimoor.common.pojo.entity.Picture;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.IPictureService;
import com.wimoor.common.service.impl.PictureServiceImpl;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.assembly.pojo.dto.AssemblySaveDTO;
import com.wimoor.erp.assembly.pojo.entity.Assembly;
import com.wimoor.erp.assembly.pojo.entity.AssemblyEntryInstock;
import com.wimoor.erp.assembly.pojo.entity.AssemblyForm;
import com.wimoor.erp.assembly.pojo.entity.AssemblyFormEntry;
import com.wimoor.erp.assembly.service.IAssemblyEntryInstockService;
import com.wimoor.erp.assembly.service.IAssemblyFormService;
import com.wimoor.erp.assembly.service.IAssemblyService;
import com.wimoor.erp.change.mapper.PurchaseFormEntryChangeAttachmentMapper;
import com.wimoor.erp.change.pojo.dto.PurchaseFormEntryChangeDTO;
import com.wimoor.erp.change.pojo.entity.PurchaseFormEntryChangeAttachment;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.service.IMaterialService;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.change.mapper.PurchaseFormEntryChangeMapper;
import com.wimoor.erp.change.pojo.entity.PurchaseFormEntryChange;
import com.wimoor.erp.change.service.IPurchaseFormEntryChangeService;
import com.wimoor.erp.common.pojo.entity.EnumByInventory;
import com.wimoor.erp.common.pojo.entity.Operate;
import com.wimoor.erp.common.pojo.entity.Status;
import com.wimoor.erp.inventory.pojo.entity.InventoryParameter;
import com.wimoor.erp.inventory.service.IInventoryFormAgentService;
import com.wimoor.erp.inventory.service.IInventoryService;
import com.wimoor.erp.purchase.alibaba.mapper.PurchaseFormEntryLogisticsMapper;
import com.wimoor.erp.warehouse.service.IWarehouseService;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2024-03-12
 */
@Service("purchaseFormEntryChangeService")
@RequiredArgsConstructor
public class PurchaseFormEntryChangeServiceImpl extends ServiceImpl<PurchaseFormEntryChangeMapper, PurchaseFormEntryChange> implements IPurchaseFormEntryChangeService {
	
	final IInventoryFormAgentService inventoryFormAgentService;

	final ISerialNumService serialNumService;
	
	final IWarehouseService warehouseService;
	
	final PurchaseFormEntryLogisticsMapper purchaseFormEntryLogisticsMapper;
	final IPictureService pictureService;
	final IInventoryService iInventoryService;
	final PurchaseFormEntryChangeAttachmentMapper purchaseFormEntryChangeAttachmentMapper;
	final FileUpload fileUpload;
	final IAssemblyFormService assemblyFormService;
	final IMaterialService iMaterialService;
	final IAssemblyService iAssemblyService;
	final IAssemblyEntryInstockService assemblyEntryInstockService;
	public IPage<Map<String, Object>> findByCondition(Page<Object> page, Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.baseMapper.findByCondition(page,map);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> saveAction(PurchaseFormEntryChangeDTO inWarehouseForm,   UserInfo user, InputStream input, String fileName) throws BizException {

		return saveForm(inWarehouseForm,  user,input,fileName);
	}
	public void saveFormAttachments(UserInfo user,PurchaseFormEntryChange entry, InputStream in,String filename) throws IOException {
		if(in==null){
			return ;
		}
		String filePath = PictureServiceImpl.purchanseChangeFilePath + user.getCompanyid()+"/"+entry.getId();
		int len = filename.lastIndexOf(".");
		if(len==-1){
			return ;
		}
		String imageName=filename;
		String imgtype=filename.substring(len, filename.length());
		filename=entry.getId()+"-"+System.currentTimeMillis()+imgtype;

		Picture picture = pictureService.uploadPicture(in, null, filePath, filename, null);
		PurchaseFormEntryChangeAttachment attachment = new PurchaseFormEntryChangeAttachment();
		attachment.setEntrychangeid(entry.getId());
		attachment.setImage(picture.getId());
		attachment.setOperator(user.getId());
		attachment.setOpttime(new Date());
		attachment.setName(imageName);
		purchaseFormEntryChangeAttachmentMapper.insert(attachment);
	}
	public void deleteFormAttachments(String id) throws IOException {
		PurchaseFormEntryChangeAttachment attachment=purchaseFormEntryChangeAttachmentMapper.selectById(id);
		pictureService.removeById(attachment.getImage());
		purchaseFormEntryChangeAttachmentMapper.deleteById(id);
	}

	@Override
	public List<PurchaseFormEntryChangeAttachment> getAttachments(String id) {
		LambdaQueryWrapper<PurchaseFormEntryChangeAttachment> query=new LambdaQueryWrapper<PurchaseFormEntryChangeAttachment>();
		query.eq(PurchaseFormEntryChangeAttachment::getEntrychangeid,id);
		List<PurchaseFormEntryChangeAttachment> list = purchaseFormEntryChangeAttachmentMapper.selectList(query);
		for(PurchaseFormEntryChangeAttachment item:list){
			Picture picture = pictureService.getById(item.getImage());
			item.setImage(fileUpload.getPictureImage(picture.getLocation()));
		}
		return list;
	}

	public void handleAssemblyMaterial(PurchaseFormEntryChange dto, UserInfo user) {
		if(StrUtil.isBlank(dto.getMainid())){
			return;
		}
		List<Assembly> assemblyList =iAssemblyService.lambdaQuery().eq(Assembly::getMainmid,dto.getMainid()).list();
		Map<String,Integer> map = new HashMap<String, Integer>();
		for(Assembly item:assemblyList){
			map.put(item.getSubmid(),item.getSubnumber());
		}
        Integer totalqty=	dto.getAmount()/ map.get(dto.getMaterialid());
		if(totalqty==null){
			throw new BizException("无法找到对于的组装关系");
		}
		String materialid = dto.getMaterialid();
		String remark = dto.getRemark();
		String warehouseid = dto.getWarehouseid();
		String shopid=user.getCompanyid();
		AssemblyForm disform = new AssemblyForm();
		try {
			disform.setNumber(serialNumService.readSerialNumber(shopid, "MO"));
		} catch (Exception e) {
			e.printStackTrace();
			try {
				disform.setNumber(serialNumService.readSerialNumber(shopid, "MO"));
			} catch (Exception e1) {
				e1.printStackTrace();
				throw new BizException("编码获取失败,请联系管理员");
			}
		}
		disform.setShopid(shopid);
		disform.setId(null);
		disform.setCreatedate(new Date());
		disform.setAudittime(new Date());
		disform.setOpttime(new Date());
		if (totalqty>0) {
			disform.setAmount(totalqty);
			disform.setAmountHandle(0);
		}else {
			throw new BizException("操作数量必须大于0");
		}
		disform.setFtype("dis");
		disform.setAuditstatus(2);
		disform.setCreator(user.getId());
		disform.setAuditor(user.getId());
		disform.setOperator(user.getId());
		disform.setWarehouseid(warehouseid);
		disform.setMainmid(dto.getMainid());
		disform.setRemark(remark);
		for (int i = 0; i < assemblyList.size(); i++) {
			AssemblyFormEntry entry = new AssemblyFormEntry();
			entry.setId(null);
			Assembly item = assemblyList.get(i);
			String wareid = warehouseid;
			String skuid = item.getSubmid();
			entry.setAmount(item.getSubnumber()*totalqty);
			entry.setMaterialid(skuid);
			entry.setWarehouseid(wareid);
			entry.setFormid(disform.getId());
			disform.addEntry(entry);
		}
	    assemblyFormService.saveAssemblyForm(disform, user);
		Map<String, Object> msgMap = new HashMap<String, Object>();
		AssemblyEntryInstock assembRecord = new AssemblyEntryInstock();
		assembRecord.setAmount(disform.getAmount());
		assembRecord.setFormid(disform.getId());
		assembRecord.setOperator(user.getId());
		assembRecord.setOpttime(new Date());
		if(StrUtil.isNotEmpty(remark)) {
			assembRecord.setRemark(remark);
		}
		assemblyEntryInstockService.saveMineAndinStock(assembRecord, user);
		AssemblyForm assform = new AssemblyForm();
		BeanUtil.copyProperties(disform,assform);
		try {
			assform.setNumber(serialNumService.readSerialNumber(shopid, "MO"));
		} catch (Exception e) {
			e.printStackTrace();
			try {
				assform.setNumber(serialNumService.readSerialNumber(shopid, "MO"));
			} catch (Exception e1) {
				e1.printStackTrace();
				throw new BizException("编码获取失败,请联系管理员");
			}
		}
		assform.setFtype("ass");
		assform.setId(null);
		assform.getMyEntrylist().clear();
		for (int i = 0; i < assemblyList.size(); i++) {
			AssemblyFormEntry entry = new AssemblyFormEntry();
			entry.setId(null);
			Assembly item = assemblyList.get(i);
			String wareid = warehouseid;
			String skuid = item.getSubmid();
			entry.setAmount(item.getSubnumber()*totalqty);
			entry.setMaterialid(skuid);
			entry.setWarehouseid(wareid);
			entry.setFormid(disform.getId());
			assform.addEntry(entry);
		}
		assform.setCreatedate(new Date());
		assform.setAudittime(new Date());
		assform.setOpttime(new Date());
		assemblyFormService.saveAssemblyForm(assform, user);

		dto.setAssemblyFormId(assform.getId());
		dto.setDisassembleFormId(disform.getId());
		updateById(dto);
	}

	@Override
	public void handleInventory(PurchaseFormEntryChange entry, UserInfo user) {
		// 入库，变动库存
		InventoryParameter parameter = new InventoryParameter();
		parameter.setShopid(entry.getShopid());
		parameter.setFormid(entry.getId());
		parameter.setFormtype("change");
		parameter.setNumber(entry.getNumber());
		parameter.setWarehouse(entry.getWarehouseid());
		parameter.setOperator(entry.getOperator());
		parameter.setMaterial(entry.getMaterialid());
		parameter.setAmount(entry.getAmount());
		//可用库存减少
		iInventoryService.outStockByDirect(parameter);
		//把库存回到待入库
		parameter.setStatus(EnumByInventory.Ready);
		iInventoryService.AddStockByStatus(parameter, Status.inbound, Operate.in) ;
		this.handleAssemblyMaterial(entry, user);
	}

	public Map<String, Object> saveForm(PurchaseFormEntryChangeDTO inWarehouseForm,  UserInfo user, InputStream input, String fileName) throws BizException {
		int result = 0;
		String msg = "";
		Map<String, Object> map = new HashMap<String, Object>();
		if (inWarehouseForm.getSkumap() != null && inWarehouseForm.getSkumap().size() > 0) {
			for (String skuId : inWarehouseForm.getSkumap().keySet()) {
				PurchaseFormEntryChange inWarehouseFormEntry = new PurchaseFormEntryChange();
				try {
					inWarehouseFormEntry.setNumber(serialNumService.readSerialNumber(user.getCompanyid(), "SO"));
				} catch (Exception e) {
					e.printStackTrace();
				}
				inWarehouseFormEntry.setMaterialid(skuId);
				inWarehouseFormEntry.setId(null);
				inWarehouseFormEntry.setAmount(Integer.parseInt(inWarehouseForm.getSkumap().get(skuId).toString()));
				inWarehouseFormEntry.setSupplierid(inWarehouseForm.getSupplierid());
				inWarehouseFormEntry.setAuditstatus(1);
				inWarehouseFormEntry.setCreatetime(new Date());
				inWarehouseFormEntry.setCreator(user.getId());
				inWarehouseFormEntry.setOperator(user.getId());
				inWarehouseFormEntry.setOpttime(new Date());
				inWarehouseFormEntry.setRemark(inWarehouseForm.getRemark());
				inWarehouseFormEntry.setShopid(user.getCompanyid());
				inWarehouseFormEntry.setWarehouseid(inWarehouseForm.getWarehouseid());
				inWarehouseFormEntry.setTotalin(0);
				inWarehouseFormEntry.setMainid(inWarehouseForm.getMainid());
				inWarehouseFormEntry.setWithoutInv(inWarehouseForm.getWithoutInv());
				if(StrUtil.isNotEmpty(inWarehouseForm.getEntryid())) {
					inWarehouseFormEntry.setEntryid(inWarehouseForm.getEntryid());
				}
				this.baseMapper.insert(inWarehouseFormEntry);// 在前端传来map，在后端保存入库清单
				result++;
                try {
                    saveFormAttachments(  user,  inWarehouseFormEntry,   input,  fileName) ;
                } catch (IOException e) {
                    throw new BizException("图片保存失败",e);
                }
            }
			 
		}
		if (result > 0) {
			msg += "操作成功！";
		} else {
			msg += "操作失败！";
		}
		map.put("msg", msg);
		return map;
	}

	@Override
	public void examineChange(PurchaseFormEntryChange entry, int banlance, UserInfo user) {
		//直接完成换货单  把待入库的差值扣减
		InventoryParameter parameter = new InventoryParameter();
		parameter.setShopid(entry.getShopid());
		parameter.setFormid(entry.getId());
		parameter.setFormtype("change");
		parameter.setNumber(entry.getNumber());
		parameter.setWarehouse(entry.getWarehouseid());
		parameter.setOperator(user.getId());
		parameter.setMaterial(entry.getMaterialid());
		parameter.setAmount(banlance);
		parameter.setStatus(EnumByInventory.Ready);
		iInventoryService.SubStockByStatus(parameter, Status.inbound, Operate.stop) ;
	}

	//重启单据
	@Override
	public void resetForm(PurchaseFormEntryChange entry) {
		int amount=entry.getAmount();//单据操作量
		int totalin=entry.getTotalin();//总共入的量
		if(totalin<amount) {
			int need = amount-totalin;
			InventoryParameter parameter = new InventoryParameter();
			parameter.setShopid(entry.getShopid());
			parameter.setFormid(entry.getId());
			parameter.setFormtype("change");
			parameter.setNumber(entry.getNumber());
			parameter.setWarehouse(entry.getWarehouseid());
			parameter.setOperator(entry.getOperator());
			parameter.setMaterial(entry.getMaterialid());
			parameter.setAmount(need);
			parameter.setStatus(EnumByInventory.Ready);
			iInventoryService.AddStockByStatus(parameter, Status.inbound, Operate.readyin) ;
		} 
		entry.setAuditstatus(1);
		this.updateById(entry);
	}
	
 


}
