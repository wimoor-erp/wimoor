package com.wimoor.erp.customer.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.common.service.IExcelDownLoadService;
import com.wimoor.erp.customer.pojo.dto.CustomerDTO;
import com.wimoor.erp.customer.pojo.entity.Customer;
import com.wimoor.erp.customer.service.ICustomerService;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.pojo.vo.MaterialSupplierVO;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.material.service.IMaterialSupplierService;
import com.wimoor.erp.purchase.service.IPurchaseFormEntryService;
import com.wimoor.erp.util.UUIDUtil;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@Api(tags = "供应商接口")
@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

	 
	final ICustomerService customerService;
	final ISerialNumService serialNumService;
	final IMaterialService materialService;
	final IPurchaseFormEntryService purchaseFormEntryService;
	final IExcelDownLoadService excelDownLoadService;
	final IMaterialSupplierService iMaterialSupplierService;

	 
	@GetMapping("/getData")
	public Result<Customer> getData(String id) {
		Customer data = customerService.getById(id);
		return Result.success(data);
	}

	// 添加客户 修改客户
	@PostMapping("/saveData")
	public Result<Map<String,Object>> addData(@RequestBody Customer cust){
		// Map<String,Object> map = new HashMap<String,Object>();
		// String msg = null;
		// 获取当前登录用户，只有用户登录成功才能操作
		Map<String,Object> map=new HashMap<String, Object>();
		UserInfo userinfo = UserInfoContext.get();
		String name = cust.getName();
		String shopid = userinfo.getCompanyid();
		LambdaQueryWrapper<Customer> queryWrapper=new LambdaQueryWrapper<Customer>();
		queryWrapper.eq(Customer::getShopid, shopid);
		queryWrapper.eq(Customer::getName, name);
		List<Customer> list = customerService.list(queryWrapper);
		String number=cust.getNumber();
		if(StrUtil.isEmpty(number)) {
			try {
				number=(serialNumService.readSerialNumber(shopid, "K"));
			} catch (Exception e) {
				e.printStackTrace();
				try {
					number=(serialNumService.readSerialNumber(shopid, "K"));
				} catch (Exception e1) {
					e1.printStackTrace();
					throw new BizException("编码获取失败,请联系管理员");
				}
			}
		}
		String ftype = cust.getFtype();
		if(ftype==null) {
			ftype="0";
		}
		if (ftype.trim().equals("0") || ftype.trim().equals("供应商")) {
			ftype = "supplier";
		} else if (ftype.trim().equals("1") || ftype.trim().equals("采购商")) {
			ftype = "purchaser";
		}
		 
		// 系统当前日期
		Calendar c = Calendar.getInstance();
		Date date = c.getTime();
		// String shopid=request.getParameter("shopid");
		// 当前用户名
		String operator = userinfo.getId();
		cust.setName(name);
		cust.setNumber(number);
		cust.setFtype(ftype);
		cust.setOpttime(date);
		cust.setOperator(operator);
		cust.setShopid(shopid);
		boolean result =false;
		if(StrUtil.isNotEmpty(cust.getId())) {
			//做更新
			result= customerService.updateById(cust);
		}else {
			//做新增
			if(list!=null && list.size()>0){
				throw new BizException("已存在该客户信息，请核对后重新添加！");
			}
			cust.setId(UUIDUtil.getUUIDshort());
			result= customerService.save(cust);
		}
		if (result==true) {
			 map.put("msg", "操作成功");
		} else {
			map.put("msg", "操作失败");
		}
		map.put("cust",cust);
		return Result.success(map);
	}
	 
	
	@GetMapping("/downloadCustomerList")
	public void downloadCustomerListAction(String search,HttpServletResponse response){
		// 创建新的Excel工作薄
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		UserInfo userinfo = UserInfoContext.get();
		// 将数据写入Excel
		String shopid = userinfo.getCompanyid();
		if(StrUtil.isEmpty(search)) {
			search=null;
		}else {
			search="%"+search+"%";
		}
		customerService.setCustomerExcelBook(workbook, search, shopid);
		try {
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=CustomerList" + System.currentTimeMillis() + ".xlsx");// 设置文件名
			ServletOutputStream fOut = response.getOutputStream();
			workbook.write(fOut);
			workbook.close();
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@PostMapping("/list")
	public Result<IPage<Map<String,Object>>> getListData(@RequestBody CustomerDTO dto){
		UserInfo user = UserInfoContext.get();
		String shopid = user.getCompanyid();
		String search=dto.getSearch();
		if(StrUtil.isEmpty(search)) {
			search=null;
		}else {
			search="%"+search+"%";
		}
		String materialid=null;
		if(StrUtil.isNotBlank(dto.getMaterialid())) {
			materialid=dto.getMaterialid();
		}
		IPage<Map<String, Object>> result = customerService.findByCondition(dto.getPage(),search,shopid);
	
		if(!StrUtil.isEmpty(materialid)) {
				if(dto.getCurrentpage()==1) {
					   List<MaterialSupplierVO> list = iMaterialSupplierService.selectSupplierByMainmid(materialid);
						if(list!=null&&list.size()>0) {
                             for(MaterialSupplierVO item:list) {
                            	 Map<String, Object> map = new HashMap<String,Object>();
                            	 map.put("isbackup", true);
                            	 map.put("operator2", item.getUsername());
                            	 map.put("number", item.getNumber());
                            	 map.put("name",item.getName());
                            	 map.put("isdefault", item.getIsdefault());
                            	 map.put("id", item.getSupplierid());
                            	 result.getRecords().add(0, map);
                             }
						}
				}
		}
		if(result!=null&&result.getRecords().size()>0) {
			for(int i=0;i<result.getRecords().size();i++) {
				Map<String, Object> item = result.getRecords().get(i);
				Object supplier = item.get("id");
				if(supplier!=null) {
					Map<String,Object> map=purchaseFormEntryService.summaryBySupplier(shopid,supplier.toString());
					if(map!=null&&map.size()>0) {
						item.putAll(map);
					}
				}
			}
		}

		return Result.success(result);
	}
	
	@GetMapping("/listAll")
	public Result<List<Map<String,Object>>> getListAllData(){
		UserInfo user = UserInfoContext.get();
		List<Map<String, Object>> result = customerService.findByCondition(null,user.getCompanyid());
		return Result.success(result);
	}
	
 
	@GetMapping("/getCustomer")
	public Result<List<Customer>> getCustomerAction(String name){
		UserInfo user = UserInfoContext.get();
		String shopid = user.getCompanyid();
		if (StrUtil.isEmpty(name)) {
			name = null;
		} else {
			name = "%" + name + "%";
		}
		return Result.success(customerService.findByShopId(shopid,name));
	}
	
	@GetMapping("/delete")
	public Result<String> deleteData(String ids){
		String[] idlist = ids.split(",");
		for (int i = 0; i < idlist.length; i++) {
			String id = idlist[i];
			if (StrUtil.isNotEmpty(id)) {
				this.beforeDelete(id);
				customerService.removeById(id);
			}
		}
		return Result.success("OK");
	}
	
	
	
	public void beforeDelete(String id){
		String shopid = UserInfoContext.get().getCompanyid();
		QueryWrapper<Material> queryWrapper=new QueryWrapper<Material>();
		queryWrapper.eq("supplier", id);
		queryWrapper.eq("isDelete", 0);
		queryWrapper.eq("shopid", shopid);
		List<Material> list = materialService.list(queryWrapper);
		if(list != null && list.size() > 0) {
			StringBuffer skulist = new StringBuffer();
			for (int i = 0; i < list.size(); i++) {
				skulist.append(list.get(i).getSku());
				if(i < list.size()-1){
					skulist.append(",");
				}
			}
			throw new BizException("删除失败，该客户存在关联的SKU："+skulist+"。请先更换这些SKU的供应商信息！");
		}
	}
	
	@PostMapping(value = "/uploadCustomerFile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Result<Boolean> uploadExcelAction(@RequestParam("file")MultipartFile file)  {
	       UserInfo user=UserInfoContext.get();
			if (file != null) {
				try {
					InputStream inputStream = file.getInputStream();
					Workbook workbook = WorkbookFactory.create(inputStream);
					Sheet sheet = workbook.getSheetAt(0);
					for (int i = 1; i <= sheet.getLastRowNum(); i++) {
						Row info=sheet.getRow(i);
						if(info.getCell(0)==null) {
							continue;
						}
						excelDownLoadService.uploadCustomerFile(user, info);
					}
					workbook.close();
					return Result.success();
				} catch (IOException e) {
					e.printStackTrace();
					return Result.failed();
				} catch (EncryptedDocumentException e) {
					e.printStackTrace();
				} catch (InvalidFormatException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		return Result.failed();
	}
	
	@GetMapping("/getSupplierByMid")
	public Result<List<MaterialSupplierVO>> getsupplierListAction(String mid){
		return Result.success(iMaterialSupplierService.selectSupplierByMainmid(mid));
	}
	
	

	
	
}
