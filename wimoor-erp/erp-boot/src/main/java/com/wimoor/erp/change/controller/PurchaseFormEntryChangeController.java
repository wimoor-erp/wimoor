package com.wimoor.erp.change.controller;


import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.hutool.json.JSONUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.erp.assembly.pojo.entity.AssemblyForm;
import com.wimoor.erp.assembly.service.IAssemblyFormService;
import com.wimoor.erp.change.pojo.entity.PurchaseFormEntryChangeAttachment;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.api.AdminClientOneFeignManager;
import com.wimoor.erp.change.pojo.dto.PurchaseFormEntryChangeDTO;
import com.wimoor.erp.change.pojo.entity.PurchaseFormEntryChange;
import com.wimoor.erp.change.pojo.entity.PurchaseFormEntryChangeReceive;
import com.wimoor.erp.change.service.IPurchaseFormEntryChangeReceiveService;
import com.wimoor.erp.change.service.IPurchaseFormEntryChangeService;
import com.wimoor.erp.customer.pojo.entity.Customer;
import com.wimoor.erp.customer.service.ICustomerService;
import com.wimoor.erp.inventory.pojo.vo.InventoryVo;
import com.wimoor.erp.inventory.service.IInventoryService;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.purchase.pojo.entity.PurchaseForm;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormEntry;
import com.wimoor.erp.purchase.service.IPurchaseFormEntryService;
import com.wimoor.erp.purchase.service.IPurchaseFormService;
import com.wimoor.erp.stock.pojo.dto.DispatchWarehouseFormDTO;
import com.wimoor.erp.warehouse.service.IWarehouseService;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2024-03-12
 */
@Api(tags = "换货单据")
@RestController
@RequestMapping("/api/v1/purchase/purchaseFormEntryChange")
@RequiredArgsConstructor
public class PurchaseFormEntryChangeController {
	@Resource
	IPurchaseFormService purchaseFormService;
	@Resource
	IPurchaseFormEntryService purchaseFormEntryService;
	@Resource
	IPurchaseFormEntryChangeService purchaseFormEntryChangeService;
	@Resource
	IPurchaseFormEntryChangeReceiveService purchaseFormEntryChangeReceiveService;
	@Resource
	ISerialNumService serialNumService;
	@Resource
	ICustomerService customerService;
	@Resource
	IWarehouseService warehouseService;
	@Resource
	IMaterialService materialService;
	@Resource
	IInventoryService inventoryService;
	@Resource
	AdminClientOneFeignManager adminClientOneFeign;
	@Resource
	IAssemblyFormService assemblyFormService;
	@PostMapping("/list")
	public Result<IPage<Map<String, Object>>> getListData(@RequestBody DispatchWarehouseFormDTO dto){
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo user = UserInfoContext.get();
		String shopid = user.getCompanyid();
		String search = dto.getSearch();
		String auditstatus =dto.getAuditstatus();
		if (StrUtil.isEmpty(search)) {
			search = null;
		} else {
			search = search.trim() + "%";
		}
		String fromDate = dto.getFromDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (StrUtil.isNotEmpty(fromDate)) {
			map.put("fromDate", fromDate.trim());
		} else {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, -30);
			fromDate = GeneralUtil.formatDate(cal.getTime(), sdf);
			map.put("fromDate", fromDate);
		}
		String toDate = dto.getToDate();
		if (StrUtil.isNotEmpty(toDate)) {
			map.put("endDate", toDate.trim());
		} else {
			toDate = GeneralUtil.formatDate(new Date(), sdf);
			map.put("endDate", toDate);
		}
		map.put("shopid", shopid);
		map.put("search", search);
		if(StrUtil.isNotEmpty(auditstatus)) {
			map.put("status", Integer.parseInt(auditstatus));
		}else {
			map.put("status", null);
		}
		IPage<Map<String, Object>> list = purchaseFormEntryChangeService.findByCondition(dto.getPage(),map);
		return 	Result.success(list);	
	}
	
	
	@GetMapping("/getData")
	public Result<Map<String, Object>> getData(String id) {
		PurchaseFormEntryChange entry = purchaseFormEntryChangeService.getById(id);
		QueryWrapper<PurchaseFormEntryChangeReceive> queryWrapper=new QueryWrapper<PurchaseFormEntryChangeReceive>();
		queryWrapper.eq("entrychangeid", entry.getId());
		List<PurchaseFormEntryChangeReceive> inFormEntryList = purchaseFormEntryChangeReceiveService.list(queryWrapper);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("changeform", entry);
		map.put("warehouse", warehouseService.getById(entry.getWarehouseid()));
		Material material = materialService.getById(entry.getMaterialid());
		Material mainMaterial = materialService.getById(entry.getMainid());
		map.put("mainMaterial",mainMaterial );
		if(mainMaterial!=null&&entry.getAssemblyFormId()!=null){
			AssemblyForm assform = this.assemblyFormService.getById(entry.getAssemblyFormId());
			AssemblyForm disform = this.assemblyFormService.getById(entry.getDisassembleFormId());
			map.put("assform",assform );
			map.put("disform",disform );
		}
		InventoryVo inventory = inventoryService.getInventory(material.getId(), entry.getWarehouseid(), entry.getShopid());
		String image = materialService.getImage(material);
		material.setImage(image);
		List<Material> materiallist=new ArrayList<Material>();
		materiallist.add(material);
		map.put("material",materiallist);
		if(inventory!=null) {
			map.put("inventory",inventory);
		}
		try {
		     entry.setCreator(adminClientOneFeign.getUserName(entry.getOperator())); 
		   }catch(Exception e) {
			   entry.setOperator("");  
		 }
		if(StrUtil.isNotEmpty( entry.getSupplierid())) {
			Customer supplier = customerService.getById( entry.getSupplierid());
			map.put("supplier", supplier);
		}else{
			if(StrUtil.isNotEmpty( material.getSupplier())) {
				Customer supplier = customerService.getById(material.getSupplier());
				map.put("suggestsupplier", supplier);
			}
		}
		Map<String, String> usernameMap = adminClientOneFeign.getAllUserName();
		if(inFormEntryList!=null && inFormEntryList.size()>0) {
		   for (int i = 0; i < inFormEntryList.size(); i++) {
			   PurchaseFormEntryChangeReceive item = inFormEntryList.get(i);
			   item.setOperator(usernameMap.get(item.getOperator()));
		   }
		}
		map.put("recordList", inFormEntryList);
		if(StrUtil.isNotEmpty(entry.getEntryid())) {
			PurchaseFormEntry pentry = purchaseFormEntryService.getById(entry.getEntryid());
			if(pentry!=null) {
				PurchaseForm form = purchaseFormService.getById(pentry.getFormid());
				if(form!=null) {
					map.put("pnumber", form.getNumber());
				}
			}
		}
		List<PurchaseFormEntryChangeAttachment>  attlist=purchaseFormEntryChangeService.getAttachments(entry.getId());
		map.put("attachments",attlist);
		return Result.success(map);
	}

	@Transactional
	@PostMapping(value="/saveData",consumes = MediaType.MULTIPART_FORM_DATA_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public Result<Map<String, Object>> saveDataAction(@RequestParam(value="file",required=false) MultipartFile file,PurchaseFormEntryChangeDTO inWarehouseForm)   {
		UserInfo user = UserInfoContext.get();
		String shopid = user.getCompanyid();
		inWarehouseForm.setShopid(shopid);
		String id = inWarehouseForm.getId();
		if (StrUtil.isNotEmpty(id)) {
			inWarehouseForm.setId(id);
		} else {
			inWarehouseForm.setCreator(user.getId());
			inWarehouseForm.setCreatetime(new Date());
			inWarehouseForm.setAuditstatus(1);// 1:采购待确认，2、仓库待收货，3：已完成，0：已取消
		}
		if(StrUtil.isNotEmpty(inWarehouseForm.getSkumapstr())){
			inWarehouseForm.setSkumap(JSONUtil.toBean(inWarehouseForm.getSkumapstr(),Map.class));
		}
		inWarehouseForm.setOperator(user.getId());
		inWarehouseForm.setOpttime(new Date());
		if(StrUtil.isNotEmpty(inWarehouseForm.getEntryid())) {
			inWarehouseForm.setEntryid(inWarehouseForm.getEntryid());
		}else{
			inWarehouseForm.setEntryid(null);
		}
		if(StrUtil.isNotEmpty(inWarehouseForm.getSupplierid())) {
			inWarehouseForm.setSupplierid(inWarehouseForm.getSupplierid());
		}else{
			inWarehouseForm.setSupplierid(null);
		}

		InputStream input =null;
		String fileName=null;
        try {
			if(file!=null&&!file.isEmpty()){
				input = file.getInputStream();
				fileName=file.getOriginalFilename();
			}

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

		Map<String, Object> map = purchaseFormEntryChangeService.saveAction(inWarehouseForm, user,input,fileName);
		return Result.success(map);
	}
	@Transactional
	@PostMapping(value="/uploadAttachment",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Result<?> uploadAttachmentAction(@RequestParam("entryid")String entryid,@RequestParam(value="file",required=false) MultipartFile file)   {
		UserInfo user = UserInfoContext.get();
		PurchaseFormEntryChange entry = purchaseFormEntryChangeService.getById(entryid);
        try {
            purchaseFormEntryChangeService.saveFormAttachments(user,entry, file.getInputStream(),file.getOriginalFilename());
        } catch (IOException e) {
			e.printStackTrace();
            throw new BizException("文件上传失败",e);
        }
        return Result.success();
	}

	@Transactional
	@GetMapping(value="/deleteAttachment")
	public Result<?> deleteAttachmentAction(String id)   {
		try {
			purchaseFormEntryChangeService.deleteFormAttachments(id);
		} catch (IOException e) {
			throw new BizException("删除文件失败",e);
		}
		return Result.success();
	}
	//直接完成单据
	@Transactional
	@GetMapping("/examine")
	public Result<String> examineAction(String ids) {
		UserInfo user = UserInfoContext.get();
		PurchaseFormEntryChange entry = purchaseFormEntryChangeService.getById(ids);
		Integer totalin = entry.getTotalin();
		Integer amount = entry.getAmount();
		if(amount>totalin) {
			//说明还有待入库 没入完
			int banlance = amount-totalin;
			if(banlance>0 && entry.getAuditstatus()==2&&entry.getWithoutInv()!=true) {
				//把待入库扣减掉
				purchaseFormEntryChangeService.examineChange(entry,banlance,user);
				entry.setAuditstatus(0);
				entry.setOperator(user.getId());
				entry.setOpttime(new Date());
				purchaseFormEntryChangeService.updateById(entry);
				return Result.success("操作成功");
			}else if( entry.getAuditstatus()==1){
				entry.setAuditstatus(0);
				entry.setOperator(user.getId());
				entry.setOpttime(new Date());
				purchaseFormEntryChangeService.updateById(entry);
				return Result.success("操作成功");
			}
			return Result.success("操作失败");
		}else {
			return Result.success("操作失败");
		}
	}
 
		//重启单据
		@Transactional
		@GetMapping("/resetForm")
		public Result<String> resetFormAction(String id) {
			UserInfo user = UserInfoContext.get();
			PurchaseFormEntryChange entry = purchaseFormEntryChangeService.getById(id);
			 if(entry!=null) {
				 entry.setOperator(user.getId());
				 entry.setOpttime(new Date());
				 purchaseFormEntryChangeService.resetForm(entry);
				 return Result.success("操作成功");
			 }else {
				return Result.failed("操作失败!");
			}
		}
	@Transactional
	@PostMapping("/changeform")
	public Result<String> changeformAction(@RequestBody PurchaseFormEntryChange dto) {
		UserInfo user = UserInfoContext.get();
		PurchaseFormEntryChange entry = purchaseFormEntryChangeService.getById(dto.getId());
		if(entry!=null) {
			if(StrUtil.isNotBlank(dto.getEntryid())){
				entry.setEntryid(dto.getEntryid());
				if(StrUtil.isBlank(entry.getEntryid())){
					entry.setSupplierid(dto.getSupplierid());
				}
			} else if(StrUtil.isNotBlank(dto.getSupplierid())){
				entry.setSupplierid(dto.getSupplierid());
			} else if(StrUtil.isNotBlank(dto.getLogistics())){
				entry.setLogistics(dto.getLogistics());
			}else if(StrUtil.isNotBlank(dto.getRemark())){
				entry.setRemark(dto.getRemark());
			}
			entry.setOperator(user.getId());
			entry.setOpttime(new Date());
			purchaseFormEntryChangeService.updateById(entry);
			return Result.success("操作成功");
		}else {
			return Result.failed("操作失败!");
		}
	}

	@Transactional
	@GetMapping("/purchaseapply")
	public Result<String> purchaseapplyAction(String id) {
		UserInfo user = UserInfoContext.get();
		PurchaseFormEntryChange entry = purchaseFormEntryChangeService.getById(id);
		if(entry!=null) {
			if(StrUtil.isBlank(entry.getSupplierid())){
				Material material = materialService.getById(entry.getMaterialid());
				entry.setSupplierid(material.getSupplier());
			}
			entry.setAuditstatus(2);
			entry.setOperator(user.getId());
			entry.setOpttime(new Date());
			purchaseFormEntryChangeService.updateById(entry);
			if(entry.getWithoutInv()!=true){
				purchaseFormEntryChangeService.handleInventory(entry, user);
			}

			return Result.success("操作成功");
		}else {
			return Result.failed("操作失败!");
		}
	}

}

