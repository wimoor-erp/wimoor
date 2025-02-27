package com.wimoor.amazon.inboundV2.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.amazon.spapi.model.fulfillmentinboundV20240320.ListShipmentItemsResponse;
import com.wimoor.amazon.inboundV2.pojo.dto.ShipmentItemsDTO;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundShipment;
import com.wimoor.amazon.inboundV2.service.IShipInboundShipmentService;
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.wimoor.amazon.api.ErpClientOneFeignManager;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.inboundV2.pojo.dto.ShipLabelDto;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundPlan;
import com.wimoor.amazon.inboundV2.pojo.vo.ShipInboundItemVo;
import com.wimoor.amazon.inboundV2.pojo.vo.ShipPlanVo;
import com.wimoor.amazon.inboundV2.service.IShipInboundCaseService;
import com.wimoor.amazon.inboundV2.service.IShipInboundPlanQuotaV2Service;
import com.wimoor.amazon.inboundV2.service.IShipInboundPlanService;
import com.wimoor.amazon.product.service.IAmzProductSalesPlanShipItemService;
import com.wimoor.amazon.product.service.IProductInfoService;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.material.pojo.vo.MaterialVO;
import com.wimoor.erp.ship.pojo.dto.QuotaInfoDTO;

import cn.hutool.core.util.StrUtil;
import feign.FeignException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

@Api(tags = "发货单配货")
@RestController
@RequestMapping("/api/v2/shipInboundPlan/quota")
@SystemControllerLog("发货单配货")
@RequiredArgsConstructor
public class ShipInboundPlanQuotaV2Controller {
	
	final IMarketplaceService marketplaceService;
	final IProductInfoService iProductInfoService;
    final IShipInboundPlanService shipInboundPlanV2Service;
    final ISerialNumService serialNumService;
	final ErpClientOneFeignManager erpClientOneFeign;
	final IAmzProductSalesPlanShipItemService iAmzProductSalesPlanShipItemService;
	final IShipInboundCaseService shipInboundCaseV2Service;
    final IShipInboundPlanQuotaV2Service shipInboundPlanQuotaV2Service;
	final IShipInboundShipmentService shipInboundShipmentV2Service;
	@ApiOperation(value = "获取下架信息")
	@ApiImplicitParam(name = "fromid", value = "计划ID", required = true, paramType = "query", dataType = "String")
	@GetMapping("/info/{fromid}")
	public Result<Map<String,Object>> quotainfoAction(@PathVariable("fromid") String formid) {
		    UserInfo user=UserInfoContext.get();
		    ShipPlanVo vo = shipInboundPlanV2Service.getPlanBaseInfo(formid,user);
		    QuotaInfoDTO dto=new QuotaInfoDTO();
		    dto.setFormid(formid);
		    List<MaterialVO> list=new LinkedList<MaterialVO>();
		    for(ShipInboundItemVo item:vo.getItemlist()) {
		    	MaterialVO e=new MaterialVO();
		    	e.setAmount(item.getQuantity());
		    	Map<String, Object> extendInfo=new HashMap<String,Object>();
		    	extendInfo.put("sellersku", item.getSku());
				e.setExtendInfo(extendInfo);
		    	e.setSku(item.getMsku());
				list.add(e);
		    }
			dto.setList(list);
		    dto.setWarehouseid(vo.getWarehouseid());
		    QuotaInfoDTO quotaInfoDto = erpClientOneFeign.quotainfoAction(dto);
		     Map<String,Object> result=new HashMap<String,Object>();
		     result.put("quatoInfo", quotaInfoDto);
		     result.put("shipPlan", vo);
	        return Result.success(result);
	    }
	
	@PostMapping("/quotainfos")
	public Result<Map<String,Object>> quotainfosAction(@RequestBody List<String> formids) {
		UserInfo user=UserInfoContext.get();
		List<ShipPlanVo> volist=new LinkedList<ShipPlanVo>();
		String warehouseid=null;
		Map<String,MaterialVO> entry=new HashMap<String,MaterialVO>();
		for(String formid:formids) {
			ShipPlanVo vo = shipInboundPlanV2Service.getPlanBaseInfo(formid,user);
			if(warehouseid==null) {
				warehouseid=vo.getWarehouseid();
			}else if(!warehouseid.equals(vo.getWarehouseid())) {
				throw new BizException("仓库不一样的订单无法一起下架");
			}
			volist.add(vo);
			 for(ShipInboundItemVo item:vo.getItemlist()) {
			    	MaterialVO e=entry.get(item.getMsku());
			    	if(e==null) {
			    		e=new MaterialVO();
			    		entry.put(item.getMsku(), e);
			    	}
			    	if(e.getAmount()!=null) {
			    		e.setAmount(item.getQuantity()+e.getAmount());
			    	}else {
			    		e.setAmount(item.getQuantity());
			    	}
			    	if(e.getExtendInfo()==null) {
			    		Map<String, Object> extendInfo=new HashMap<String,Object>();
				    	extendInfo.put("sellersku", item.getSellersku());
				    	e.setExtendInfo(extendInfo);
			    	}else {
			    		Map<String, Object> extendInfo=e.getExtendInfo();
			    		if(extendInfo.get("sellersku")!=null) {
			    			String old=extendInfo.get("sellersku").toString();
			    			extendInfo.put("sellersku",old+","+ item.getSellersku());
			    		}else {
			    			extendInfo.put("sellersku", item.getSellersku());
			    		}
			    	}
					
			    	e.setSku(item.getMsku());
			    }
		}
	    
	    QuotaInfoDTO dto=new QuotaInfoDTO();
 
	    List<MaterialVO> list=new LinkedList<MaterialVO>();
	    for(Entry<String, MaterialVO> eitem:entry.entrySet()) {
	    	list.add(eitem.getValue());
	    }
		dto.setList(list);
	    dto.setWarehouseid(warehouseid);
	    QuotaInfoDTO quotaInfoDto = erpClientOneFeign.quotainfoAction(dto);
	    Map<String,Object> result=new HashMap<String,Object>();
	    result.put("quatoInfo", quotaInfoDto);
	    result.put("shipPlan", volist);
        return Result.success(result);
	  }
	
	@ApiOperation(value = "配货批次")
	@PostMapping("/checkinv/{checkid}")
	public Result<Integer> checkinvAction(@PathVariable("checkid") String checkid,@RequestBody List<String> inboundformids) {
		    Integer i=0;
		    for(String item:inboundformids) {
		    	String inboundformid=item;
			    ShipInboundPlan shipplan = shipInboundPlanV2Service.getById(inboundformid);
			    shipplan.setCheckInv(new BigInteger(checkid));
			    if(shipInboundPlanV2Service.updateById(shipplan)) {
			    	i=i+1;
			    }
		    }
	        return Result.success(i);
	}
	
	@ApiOperation(value = "下载配货单")
	@PostMapping("/downPDFShipForm/{ftype}")
	public void downPDFShipFormAction(@PathVariable("ftype") String ftype,
			@ApiParam("货件ID")@RequestBody List<String> formids,
			HttpServletResponse response) {
		String formidstr=null;
		UserInfo user=UserInfoContext.get();
		response.setContentType("application/force-download");// 设置强制下载不打开
		response.addHeader("Content-Disposition", "attachment;fileName=shipForm" + System.currentTimeMillis() + ".pdf");// 设置文件名
		Document document = new Document(PageSize.A4);
		// step 2
		try {
		     
			List<ShipPlanVo> volist=new LinkedList<ShipPlanVo>();
			String warehouseid=null;
			Map<String,MaterialVO> entry=new LinkedMap<String,MaterialVO>();
			for(String formid:formids) {
				ShipPlanVo vo = shipInboundPlanV2Service.getPlanBaseInfo(formid,user);
				formidstr=formidstr==null?formid:formidstr+","+formid;
				if(warehouseid==null) {
					warehouseid=vo.getWarehouseid();
				}else if(!warehouseid.equals(vo.getWarehouseid())) {
					throw new BizException("仓库不一样的订单无法一起下架");
				}
				volist.add(vo);
				 for(ShipInboundItemVo item:vo.getItemlist()) {
				    	MaterialVO e=entry.get(item.getMsku());
				    	if(e==null) {
				    		e=new MaterialVO();
				    		entry.put(item.getMsku(), e);
				    	}
				    	if(e.getAmount()!=null) {
				    		e.setAmount(item.getQuantity()+e.getAmount());
				    	}else {
				    		e.setAmount(item.getQuantity());
				    	}
				    	if(e.getExtendInfo()==null) {
				    		Map<String, Object> extendInfo=new HashMap<String,Object>();
					    	extendInfo.put("sellersku", item.getSku());
					    	extendInfo.put("fnsku", item.getFnsku());
					    	e.setExtendInfo(extendInfo);
				    	}else {
				    		Map<String, Object> extendInfo=e.getExtendInfo();
				    		if(extendInfo.get("sellersku")!=null) {
				    			String old=extendInfo.get("sellersku").toString();
				    			extendInfo.put("sellersku",old+","+item.getSku());
				    			old=extendInfo.get("fnsku").toString();
				    			extendInfo.put("fnsku",old+","+ item.getFnsku());
				    		}else {
				    			extendInfo.put("sellersku", item.getSku());
				    			extendInfo.put("fnsku", item.getFnsku());
				    		}
				    	}
						
				    	e.setSku(item.getMsku());
				    }
			}
		    
		    QuotaInfoDTO dto=new QuotaInfoDTO();
	 
		    List<MaterialVO> list=new LinkedList<MaterialVO>();
		    for(Entry<String, MaterialVO> eitem:entry.entrySet()) {
		    	list.add(eitem.getValue());
		    }
			dto.setList(list);
		    dto.setWarehouseid(warehouseid);
		    dto.setFormid(formidstr);
		    QuotaInfoDTO quotaInfoDto = erpClientOneFeign.quotainfoAction(dto);
			PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
			if("detail".equals(ftype)) {
				shipInboundPlanQuotaV2Service.setPDFDocShipFormDetail(writer,user, document,  quotaInfoDto,volist);
			}else {
				shipInboundPlanQuotaV2Service.setPDFDocShipForm(writer,user,document, quotaInfoDto,volist);
			}
		} catch(FeignException  e) {
			 throw new BizException(BizException.getMessage(e, "获取货件失败"));
	     } catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (document != null && document.isOpen()) {
				document.close();
			}
		}
	}

	@ApiOperation(value = "下载配货单")
	@PostMapping("/downPDFShipmentForm/{ftype}")
	public void downPDFShipmentFormAction(@PathVariable("ftype") String ftype,
									  @ApiParam("货件ID")@RequestBody List<String> shipmentids,
									  HttpServletResponse response) {
		String formidstr=null;
		UserInfo user=UserInfoContext.get();
		response.setContentType("application/force-download");// 设置强制下载不打开
		response.addHeader("Content-Disposition", "attachment;fileName=shipForm" + System.currentTimeMillis() + ".pdf");// 设置文件名
		Document document = new Document(PageSize.A4);
		// step 2
		try {

			List<ShipPlanVo> volist=new LinkedList<ShipPlanVo>();
			String warehouseid=null;
			Map<String,MaterialVO> entry=new HashMap<String,MaterialVO>();

			for(String shipmentid:shipmentids) {
				ShipInboundShipment shipmentClz = shipInboundShipmentV2Service.getById(shipmentid);
				ShipPlanVo vo = shipInboundPlanV2Service.getPlanBaseInfo(shipmentClz.getFormid(),user);
				vo.setRemark(shipmentClz.getRemark());
				formidstr=formidstr==null?shipmentClz.getShipmentConfirmationId():formidstr+","+shipmentClz.getShipmentConfirmationId();
				if(warehouseid==null) {
					warehouseid=vo.getWarehouseid();
				}else if(!warehouseid.equals(vo.getWarehouseid())) {
					throw new BizException("仓库不一样的订单无法一起下架");
				}
				vo.setNumber(shipmentClz.getShipmentConfirmationId());
				vo.setCountryname(vo.getCountryname()+"("+shipmentClz.getDestination()+")");
				volist.add(vo);
				List<Map<String, Object>> items = shipInboundShipmentV2Service.findInboundItemByShipmentId(shipmentid);
				for(Map<String,Object> item:items) {
					MaterialVO e=entry.get(item.get("msku"));
					if(e==null) {
						e=new MaterialVO();
						entry.put(item.get("msku").toString(), e);
					}
					if(e.getAmount()!=null) {
						e.setAmount(new Integer(item.get("QuantityShipped").toString())+e.getAmount());
					}else {
						e.setAmount(new Integer(item.get("QuantityShipped").toString()));
					}
					if(e.getExtendInfo()==null) {
						Map<String, Object> extendInfo=new HashMap<String,Object>();
						extendInfo.put("sellersku", item.get("sellersku"));
						extendInfo.put("fnsku",  item.get("FNSKU"));
						e.setExtendInfo(extendInfo);
					}else {
						Map<String, Object> extendInfo=e.getExtendInfo();
						if(extendInfo.get("sellersku")!=null) {
							String old=extendInfo.get("sellersku").toString();
							extendInfo.put("sellersku",old+","+ item.get("sellersku").toString());
							old=extendInfo.get("fnsku").toString();
							if(item.get("FNSKU")!=null){
								extendInfo.put("fnsku",old+","+  item.get("FNSKU"));
							}
						}else {
							extendInfo.put("sellersku", item.get("sellersku"));
							extendInfo.put("fnsku",item.get("FNSKU"));
						}
					}

					e.setSku(item.get("msku").toString());
				}
			}

			QuotaInfoDTO dto=new QuotaInfoDTO();

			List<MaterialVO> list=new LinkedList<MaterialVO>();
			for(Entry<String, MaterialVO> eitem:entry.entrySet()) {
				list.add(eitem.getValue());
			}
			dto.setList(list);
			dto.setWarehouseid(warehouseid);
			dto.setFormid(formidstr);
			QuotaInfoDTO quotaInfoDto = erpClientOneFeign.quotainfoAction(dto);
			PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
			if("detail".equals(ftype)) {
				shipInboundPlanQuotaV2Service.setPDFDocShipFormDetail(writer,user, document,  quotaInfoDto,volist);
			}else {
				shipInboundPlanQuotaV2Service.setPDFDocShipForm(writer,user,document, quotaInfoDto,volist);
			}
		} catch(FeignException  e) {
			throw new BizException(BizException.getMessage(e, "获取货件失败"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (document != null && document.isOpen()) {
				document.close();
			}
		}
	}
	@ApiOperation(value = "下载产品标签-Excel")
	@PostMapping("/downExcelLabel")
	public void downExcelLabelAction(
			@RequestBody List<ShipLabelDto> dto,
			HttpServletResponse response) {
		UserInfo user=UserInfoContext.get();
		try {
			// 创建新的Excel工作薄
			SXSSFWorkbook workbook = new SXSSFWorkbook();
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=labeltemplate.xlsx");// 设置文件名
			ServletOutputStream fOut = response.getOutputStream();
			// 将数据写入Excel
		    shipInboundPlanQuotaV2Service.setExcelBookShipmentLabel(user.getCompanyid(), workbook,shipInboundPlanQuotaV2Service.getLabelValue(dto));
			workbook.write(fOut);
			workbook.close();
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@ApiOperation(value = "下载产品标签-Excel")
	@GetMapping("/downExcelLabel")
	public void downExcelLabelSimpleAction( String formid, HttpServletResponse response) {
		     UserInfo user=UserInfoContext.get();
			 ShipPlanVo vo = shipInboundPlanV2Service.getPlanBaseInfo(formid,user);
			 List<ShipLabelDto> dto=new ArrayList<ShipLabelDto>();
			    for(ShipInboundItemVo item:vo.getItemlist()) {
			    	ShipLabelDto e=new ShipLabelDto();
			    	e.setAmount(item.getQuantity());
			    	e.setSku(item.getSku());
			    	e.setAmazonauthid(vo.getAmazonauthid());
			    	e.setGroupid(vo.getAmazongroupid());
			    	e.setMarketplaceid(vo.getMarketplaceid());
					e.setNumber(vo.getNumber());
			    	dto.add(e);
			    }
	         this.downExcelLabelAction(dto, response);
	}
	@ApiOperation(value = "下载产品标签-Pdf")
	@GetMapping("/downPDFLabel")
	public void downPDFLabelSimpleAction( String formid,HttpServletResponse response) {
		 UserInfo user=UserInfoContext.get();
		 ShipPlanVo vo = shipInboundPlanV2Service.getPlanBaseInfo(formid,user);
		 List<ShipLabelDto> dto=new ArrayList<ShipLabelDto>();
		    for(ShipInboundItemVo item:vo.getItemlist()) {
		    	ShipLabelDto e=new ShipLabelDto();
		    	e.setAmount(item.getQuantity());
		    	e.setSku(item.getSku());
		    	e.setAmazonauthid(vo.getAmazonauthid());
		    	e.setGroupid(vo.getAmazongroupid());
		    	e.setMarketplaceid(vo.getMarketplaceid());
		    	dto.add(e);
		    }
	      this.downPDFLabelAction(dto, response);
	}
	
	@ApiOperation(value = "下载产品标签-Pdf")
	@PostMapping("/downPDFLabel")
	public void downPDFLabelAction(
			@RequestBody List<ShipLabelDto> dto,
			HttpServletResponse response) {
		response.setContentType("application/force-download");// 设置强制下载不打开
		response.addHeader("Content-Disposition", "attachment;fileName=PDFFBALabel.pdf");// 设置文件名
		Document document = new Document(PageSize.A4);
		try {
		PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
		shipInboundPlanQuotaV2Service.setPDFDocLabel(document, shipInboundPlanQuotaV2Service.getLabelValue(dto), writer);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(document!=null&&document.isOpen()) {
				document.close();
			}
		}
	}
	
}
