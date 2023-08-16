package com.wimoor.erp.ship.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.mvc.FileUpload;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.api.AmazonClientOneFeignManager;
import com.wimoor.erp.assembly.pojo.vo.AssemblyVO;
import com.wimoor.erp.assembly.service.IAssemblyFormService;
import com.wimoor.erp.common.pojo.entity.EnumByInventory;
import com.wimoor.erp.inventory.pojo.entity.InventoryParameter;
import com.wimoor.erp.inventory.service.IInventoryFormAgentService;
import com.wimoor.erp.inventory.service.IInventoryService;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.pojo.entity.MaterialConsumable;
import com.wimoor.erp.material.service.IMaterialConsumableService;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.ship.mapper.ShipPlanItemMapper;
import com.wimoor.erp.ship.pojo.dto.ShipAmazonShipmentDTO;
import com.wimoor.erp.ship.pojo.dto.ShipInboundItemDTO;
import com.wimoor.erp.ship.pojo.dto.ShipInboundItemVo;
import com.wimoor.erp.ship.pojo.dto.ShipInboundPlanDTO;
import com.wimoor.erp.ship.pojo.dto.ShipInboundShipmenSummarytVo;
import com.wimoor.erp.ship.pojo.dto.ShipInboundShipmentDTO;
import com.wimoor.erp.ship.service.IShipAmazonFormService;
import com.wimoor.erp.warehouse.pojo.vo.WarehouseShelfInventoryOptRecordVo;
import com.wimoor.erp.warehouse.service.IWarehouseService;
import com.wimoor.erp.warehouse.service.IWarehouseShelfInventoryOptRecordService;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import feign.FeignException;

@Service
public class ShipAmazonFormServiceImpl implements IShipAmazonFormService{
	@Autowired
	IInventoryService inventoryService;
	 
	@Autowired
	IWarehouseService warehouseService;
	 
	@Autowired
	IInventoryFormAgentService inventoryFormAgentService;
	
	@Autowired
	IMaterialService materialService;
	
	@Autowired
	IAssemblyFormService assemblyFormService;
	
	@Autowired
	AmazonClientOneFeignManager amazonClientOneFeign;
	
	@Autowired
	FileUpload fileUpload;
	
	@Autowired
	IMaterialConsumableService materialConsumableService;
	
	@Autowired
	IWarehouseShelfInventoryOptRecordService iWarehouseShelfInventoryOptRecordService;
	
	@Autowired
	ShipPlanItemMapper shipPlanItemMapper;
	@Resource
    QrConfig qrconfig;
	 
	public void handleInventoryAction(UserInfo user, ShipInboundShipmentDTO shipmentobj) {
		if("WORKING".equals(shipmentobj.getShipmentstatus())&&(shipmentobj.getSyncInv()==null||shipmentobj.getSyncInv()==0)) {
			fulfillableToOutbound(user,shipmentobj);
		}else {
			fulfillableOut(user,shipmentobj.getInboundplan(), shipmentobj);
		}
	}

	public void fulfillableToOutbound(UserInfo user, ShipInboundShipmentDTO shipmentobj) {
		for (int i = 0; i < shipmentobj.getItemList().size(); i++) {
			 ShipInboundItemDTO item = shipmentobj.getItemList().get(i);
			int shipqty =item.getQuantityshipped();
		    String sellersku=item.getSellersku();
			String materialid=item.getMaterialid();
			if(materialid==null) {
				Material m = materialService.getBySku(user.getCompanyid(),item.getMsku());
				if(m==null) {
					throw new BizException("平台SKU【"+item.getSellersku()+"】对应本地SKU【"+item.getMsku()+"】无法找到对应信息");
				}
				materialid=m.getId();
				item.setMaterialid(m.getId());
			}
			String warehouseid=shipmentobj.getInboundplan().getWarehouseid();
			 Map<String, Object> map = inventoryService.findInvDetailById(materialid, warehouseid, user.getCompanyid());
			 int fulfillable=0;
			 if(map!=null&&map.containsKey("fulfillable")) {
				 Object fulobj = map.get("fulfillable");
                if(fulobj!=null&&StrUtil.isNotEmpty(fulobj.toString())) {
                	fulfillable = Integer.parseInt(fulobj.toString());
                }
			 }
			 if(fulfillable<shipqty) {
				 Material material = materialService.getById(materialid);
				 if(material==null) {
					 throw new BizException("平台SKU["+sellersku+"]无法对应本地产品");
				 }
				 else if(!"1".equals(material.getIssfg())) {
					 throw new BizException("平台SKU["+sellersku+"]对应本地产品SKU["+material.getSku()+"]库存不足无法发货");
				 }
				 int needassembly = shipqty-fulfillable;
				 assemblyFormService.createAssemblyFormByShipment(user,warehouseid,material,shipmentobj,needassembly); 
			 }
			addMaterialFullfillableToOutbound(user, shipmentobj,materialid ,shipqty);
		  
		}
	};

	private void addMaterialFullfillableToOutbound(UserInfo user, ShipInboundShipmentDTO shipmentobj, String material, Integer amount) {
		InventoryParameter para = new InventoryParameter();
		para.setAmount(amount);
		para.setFormid(shipmentobj.getInboundplan().getId());
		para.setMaterial(material);
		EnumByInventory statusinv = EnumByInventory.Ready;
		para.setStatus(statusinv);
		para.setOperator(user.getId());
		para.setOpttime(new Date());
		para.setWarehouse(shipmentobj.getInboundplan().getWarehouseid());
		para.setFormtype("outstockform");
		para.setNumber(shipmentobj.getShipmentid());
		para.setShopid(shipmentobj.getInboundplan().getShopid());
	    inventoryFormAgentService.outStockByRead(para);
	}

	public void addFullfillableToOutboundChange(String userid, ShipInboundShipmentDTO ship, String material, Integer amount) {
		InventoryParameter para = new InventoryParameter();
		para.setAmount(amount);
		para.setFormid(ship.getInboundplan().getId());
		para.setMaterial(material);
		EnumByInventory statusinv = EnumByInventory.Ready;
		para.setStatus(statusinv);
		para.setOperator(userid);
		para.setOpttime(new Date());
		para.setWarehouse(ship.getInboundplan().getWarehouseid());
		para.setFormtype("outstockform");
		para.setNumber(ship.getShipmentid());
		para.setShopid(ship.getInboundplan().getShopid());
        inventoryFormAgentService.outStockByReadChange(para);
	}

 
	private void cancelfulfillable(UserInfo user, ShipInboundShipmentDTO shipment) throws BizException {
		ShipInboundPlanDTO plan = shipment.getInboundplan();
		 List<ShipInboundItemDTO> listmap = shipment.getItemList();
		for (int i = 0; i < listmap.size(); i++) {
			ShipInboundItemDTO item = listmap.get(i);
			if(item.getMaterialid()==null) {
				if(item.getMsku()==null) {
					item.setMsku(item.getSellersku());
				}
				Material m = this.materialService.getBySku(user.getCompanyid(), item.getMsku());
				item.setMaterialid(m.getId());
			}
			InventoryParameter para = new InventoryParameter();
			Integer amount = item.getQuantityshipped();
			para.setAmount(amount);
			para.setMaterial(item.getMaterialid());
			EnumByInventory statusinv = EnumByInventory.Ready;
			para.setStatus(statusinv);
			para.setFormid(plan.getId());
			para.setOpttime(new Date());
			para.setWarehouse(plan.getWarehouseid());
			para.setFormtype("outstockform");
			para.setShopid(plan.getShopid());
			para.setNumber(shipment.getShipmentid());
			para.setOperator(user.getId());
			inventoryFormAgentService.outStockDirectCancel(para);
		 
		}
	}

 
	 
	private void subMaterialFulfillableToOutbound(UserInfo user, ShipInboundShipmentDTO ship, String material, Integer amount) {
		InventoryParameter para = new InventoryParameter();
		para.setAmount(amount);
		para.setMaterial(material);
		EnumByInventory statusinv = EnumByInventory.Ready;
		para.setStatus(statusinv);
		para.setFormid(ship.getInboundplan().getId());
		para.setOpttime(new Date());
		para.setOperator(user.getId());
		para.setWarehouse(ship.getInboundplan().getWarehouseid());
		para.setFormtype("outstockform");
		para.setShopid(ship.getInboundplan().getShopid());
		para.setNumber(ship.getShipmentid());
	    inventoryFormAgentService.outStockReadyCancel(para);
	  
	}

	public void subFulfillableToOutboundChange(String userid, ShipInboundShipmentDTO ship, String material, Integer amount) {
		InventoryParameter para = new InventoryParameter();
		para.setAmount(amount);
		para.setMaterial(material);
		EnumByInventory statusinv = EnumByInventory.Ready;
		para.setStatus(statusinv);
		para.setFormid(ship.getInboundplan().getId());
		para.setOpttime(new Date());
		para.setOperator(userid);
		para.setWarehouse(ship.getInboundplan().getWarehouseid());
		para.setFormtype("outstockform");
		para.setShopid(ship.getInboundplan().getShopid());
		para.setNumber(ship.getShipmentid());
	    inventoryFormAgentService.outStockReadyChange(para);
	}
	
	private void fulfillableOut(UserInfo user, ShipInboundPlanDTO plan, ShipInboundShipmentDTO shipment) throws BizException {
		  List<ShipInboundItemDTO> listmap = shipment.getItemList();
		for (int i = 0; i < listmap.size(); i++) {
			  ShipInboundItemDTO item = listmap.get(i);
			if(item.getMaterialid()!=null && item.getQuantityshipped()!=null) {
				subFulfillableOut(user, shipment, item.getMaterialid(), item.getQuantityshipped());
			}
		}
	}
	
	private void subFulfillableOut(UserInfo user, ShipInboundShipmentDTO plan, String material, Integer amount) {
		InventoryParameter para = new InventoryParameter();
		para.setAmount(amount);
		para.setMaterial(material);
		para.setFormid(plan.getInboundplan().getId());
		para.setOpttime(new Date());
		para.setOperator(user.getId());
		para.setWarehouse(plan.getInboundplan().getWarehouseid());
		para.setFormtype("outstockform");
		para.setShopid(plan.getInboundplan().getShopid());
		para.setNumber(plan.getShipmentid());
	    inventoryFormAgentService.outStockByDirect(para);
	}
	
	private void cancelfulfillableToOutbound(UserInfo user, ShipInboundShipmentDTO shipment) throws BizException {
		List<ShipInboundItemDTO> itemlist = shipment.getItemList();
		for (int i = 0; i < itemlist.size(); i++) {
			ShipInboundItemDTO item = itemlist.get(i);
			if(item.getMaterialid()==null) {
				if(item.getMsku()==null) {
					item.setMsku(item.getSellersku());
				}
				Material m = this.materialService.getBySku(user.getCompanyid(), item.getMsku());
				item.setMaterialid(m.getId());
			}
			subMaterialFulfillableToOutbound(user, shipment,item.getMaterialid(),item.getQuantityshipped());
		}
	}
	
	public List<List<String>> getData(List<ShipInboundItemVo> itemlist,String shopid) {
		List<List<String>> data = new ArrayList<List<String>>();
		String[] tableTitleList = { "图片", "本地SKU", "产品名称", "平台SKU", "可用库存", "拟发货数", "配送数量" };
		data.add(Arrays.asList(tableTitleList));
		for (int i = 0; i < itemlist.size(); i++) {
			ShipInboundItemVo item=itemlist.get(i);
			List<String> dataLine = new ArrayList<String>();
			dataLine.add(item.getImage());
			String skucell=item.getSku();
			if(item!=null&&item.getAssemblyList()!=null&&skucell!=null) {
				skucell=skucell+"\n(子SKU):";
				for(AssemblyVO asitem:item.getAssemblyList()) {
					skucell=skucell+"\n"+asitem.getSku()+" ×"+asitem.getSubnumber();
				}
			}
			
			dataLine.add(skucell);
			String namecell=item.getName();
	
			if(item!=null&&item.getBoxnum()!=null&&item.getBoxnum()>0) {
				namecell=namecell+"\n箱规 >\n单箱个数："+item.getBoxnum();
				if(item.getBoxlength()!=null&&item.getBoxweight()!=null) {
					namecell=namecell+"    重kg:"+item.getBoxweight();
					namecell=namecell+"\n长*宽*高cm："
				     +item.getBoxlength()+
				     "*"+item.getBoxwidth()+"*"+item.getBoxheight();
				}
			}
			List<MaterialConsumable> conList = materialConsumableService.selectByMainmid(item.getMaterialid());
			if(item!=null&&conList!=null&&conList.size()>0&&skucell!=null) {
				namecell=namecell+"\n\n(耗材SKU):";
				int row=0;
				for(MaterialConsumable conitem:conList) {
					if(conitem.getSubmaterialid()!=null) {
						Material consku = materialService.getById(conitem.getSubmaterialid());
						if(row==0) {
							namecell=namecell+consku.getSku()+" ×"+conitem.getAmount().intValue();
						}else {
							namecell=namecell+"，"+consku.getSku()+" ×"+conitem.getAmount().intValue();
						}
						row++;
					}
				}
			}
			//继续加子产品的库位库存
			if(item.getAssemblyList()!=null && item.getAssemblyList().size()>0) {
				namecell=namecell+"\n子SKU库位:\n";
				for(AssemblyVO asitem:item.getAssemblyList()) {
					namecell=namecell+asitem.getSku()+":";
					List<WarehouseShelfInventoryOptRecordVo> shelflist = iWarehouseShelfInventoryOptRecordService.getRecordVo(shopid, item.getId(),
							"outstockform", asitem.getSubmid());
					if(item!=null&&shelflist!=null&&shelflist.size()>0&&skucell!=null) {
						int row=0;
						for(WarehouseShelfInventoryOptRecordVo shelfitem:shelflist) {
							if(shelfitem.getShelfname()!=null) {
								if(row==0) {
									namecell=namecell+shelfitem.getShelfname()+" ×"+shelfitem.getQuantity()+"\n";
								}else {
									namecell=namecell+shelfitem.getShelfname()+" ×"+shelfitem.getQuantity()+"\n";
								}
								row++;
							}
						}
					}else {
						namecell=namecell+"无\n";
					}
				}
			}
			dataLine.add(namecell);
			String sellersku = item.getSellersku();
			if (item.getFNSKU()!=null) {
			   sellersku=sellersku+"\n\n(FNSKU)\n" + item.getFNSKU();
			}  
			dataLine.add(sellersku);
			String shelfinv = item.getInvquantity().toString();
			List<WarehouseShelfInventoryOptRecordVo> shelflist = iWarehouseShelfInventoryOptRecordService.getRecordVo(shopid, item.getId(),
					"outstockform", item.getMaterialid());
			if(item!=null&&shelflist!=null&&shelflist.size()>0&&skucell!=null) {
				shelfinv=shelfinv+"\n库位:\n";
				int row=0;
				for(WarehouseShelfInventoryOptRecordVo shelfitem:shelflist) {
					if(shelfitem.getShelfname()!=null) {
						if(row==0) {
							shelfinv=shelfinv+shelfitem.getShelfname()+" ×"+shelfitem.getQuantity()+"\n";
						}else {
							shelfinv=shelfinv+shelfitem.getShelfname()+" ×"+shelfitem.getQuantity()+"\n";
						}
						row++;
					}
				}
			}
			dataLine.add(shelfinv);
			if(item.getInvquantity()!=null)
			dataLine.add(item.getQuantityShipped().toString());
			dataLine.add("");
			data.add(dataLine);
		}
		return data;
	}
	@Override
	@Transactional
	public String updateDisable(UserInfo user, String shipmentid, boolean isDelAmz, String disableShipment) {
		// TODO Auto-generated method stub
			Result<ShipInboundShipmentDTO> shipmentResult = amazonClientOneFeign.getShipmentidAction(shipmentid);
			ShipInboundShipmentDTO shipment = shipmentResult.getData();
			Integer oldstatus = shipment.getStatus();
			if(shipment.getStatus()==-1||shipment.getStatus()==1) {
				throw new BizException("该货件已被取消或者失效"); 
			}
			shipment.setStatus0date(new Date());
			shipment.setShipmentstatus(ShipInboundShipmentDTO.ShipmentStatus_CANCELLED);
			shipment.setOperator(user.getId());
			shipment.setOpttime(new Date());
			if(shipment.getStatus()!=0) {
				if(shipment.getSyncInv()==null||shipment.getSyncInv()==0||shipment.getSyncInv()==2) {
					if (oldstatus >= 5||shipment.getSyncInv()==2) {
						cancelfulfillable(user, shipment);
					} else {
						cancelfulfillableToOutbound(user, shipment);
					}
					if("1".equals(disableShipment)) {
						assemblyFormService.cancelByShipment(user,shipment);
					}
				}
			} 
		   shipment.setStatus(0);
				try {
					if(!isDelAmz) {
						shipment.setActiontype("updateself");
					}else {
						shipment.setActiontype("updateAmz");
					}
					Result<String> result = amazonClientOneFeign.updateShipmentAction(shipment);
					if(Result.isSuccess(result)) {
						if(!result.getData().equals(shipmentid)) {
							 throw new BizException("亚马逊操作失败");
						}
					}else {
						 throw new BizException(result.getMsg());
					}
				}catch (FeignException e) {
					 throw new BizException(BizException.getMessage(e, "货件删除失败"));
				}catch (Exception e) {
					 throw new BizException(e.getMessage());
				}
			return "success";
	}

 

	public void updateItemQty(UserInfo user, ShipAmazonShipmentDTO shipmentAmz) {
		List<ShipInboundItemDTO> itemlist = shipmentAmz.getItemList();
		if(itemlist==null||itemlist.size()==0)return;
		Result<ShipInboundShipmentDTO> shipmentResult = amazonClientOneFeign.getShipmentidAction(itemlist.get(0).getShipmentid());
		ShipInboundShipmentDTO shipment = shipmentResult.getData();
		Map<String,Integer> skuQuantity=new HashMap<String,Integer>();
		itemlist.forEach(item->{
			skuQuantity.put(item.getSellersku(), item.getQuantityshipped());
		});
		for (int i = 0; i <shipment.getItemList().size(); i++) {
			ShipInboundItemDTO shipInboundItem = shipment.getItemList().get(i);
			if(shipInboundItem.getMaterialid()==null) {
				if(shipInboundItem.getMsku()==null) {
					shipInboundItem.setMsku(shipInboundItem.getSellersku());
				}
				Material m = this.materialService.getBySku(user.getCompanyid(), shipInboundItem.getMsku());
				shipInboundItem.setMaterialid(m.getId());
			}
			Integer qty = shipInboundItem.getQuantityshipped();
			Integer newqty = skuQuantity.get(shipInboundItem.getSellersku());
	              if (newqty!=null&&qty != newqty) {
					if (qty < newqty) {// 修改数量大于计划数量,changeadd
						addFullfillableToOutboundChange(user.getId(),shipment, shipInboundItem.getMaterialid(), newqty - qty);
					} else {// 修改数量小于计划数量,changesub
						subFulfillableToOutboundChange(user.getId(),shipment,  shipInboundItem.getMaterialid(), qty - newqty);
					}
		 
				}
	         shipInboundItem.setQuantityshipped(newqty);
		}
		shipment.setStatus(3);
		shipment.setStatus3date(new Date());
		shipment.setOperator(user.getId());
		shipment.setOpttime(new Date());
		if(StrUtil.isNotBlank(shipmentAmz.getIntendedBoxContentsSource())) {
			shipment.setIntendedBoxContentsSource(shipmentAmz.getIntendedBoxContentsSource());
		}
		shipment.setActiontype("updateqty");
		try {
	     	amazonClientOneFeign.updateShipmentAction(shipment);
		}catch(FeignException  e) {
	 		 throw new BizException(BizException.getMessage(e, "更新货件失败"));
	 	}
	}

	
    private Image getImage(String imgpath) {
    	if(imgpath==null)return null;
		Image img = null;
	 
		try {
		    imgpath =  fileUpload.getPictureImage(imgpath);
			img = Image.getInstance(imgpath);
		} catch (Exception e) {
		   e.printStackTrace();
		   return null;
		}
		if(img!=null) {
			img.setScaleToFitHeight(true);
		}
		return img;
    }
	private void addCell(PdfPTable table ,Object value,com.itextpdf.text.Font font_s,String ftype) {
		String fiedvalue =value!=null?value.toString():" ";
		PdfPCell cell = new PdfPCell(new Phrase(fiedvalue, font_s));
		table.addCell(cell);
	}
	@Override
	public void setPDFDocShipFormDetail(PdfWriter writer,UserInfo user, Document document, ShipInboundShipmenSummarytVo data) {
	 try {
		document.addTitle("配货单");
	    document.addAuthor("wimoor");
		// step 3
		document.open();
		// step 4
		BaseFont baseFont = null;
		// 方法一：使用Windows系统字体(TrueType)
		String path = new ClassPathResource("font/SIMYOU.TTF").getPath();
		baseFont = BaseFont.createFont(path, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		com.itextpdf.text.Font font = new com.itextpdf.text.Font(baseFont);
		com.itextpdf.text.Font font_s = new com.itextpdf.text.Font(baseFont, 10, com.itextpdf.text.Font.NORMAL);
		Paragraph p = new Paragraph("配货单", font);// 设置字体，解决中文显示不了的问题
		p.setAlignment(Element.ALIGN_CENTER);
		p.setSpacingAfter(10);
		document.add(p);

        PdfContentByte cd = writer.getDirectContent();
      if(data.getShipmentid().indexOf("FBA")>-1) {
      	String content = "https://www.wimoor.com/wxsc/"+data.getShipmentid();//内容信息
  		try {
  			qrconfig.setWidth(70);
  			qrconfig.setHeight(70);
  			BufferedImage imagebuffer =	QrCodeUtil.generate(content, qrconfig);
  			ByteArrayOutputStream out=new ByteArrayOutputStream();
  		    ImageIO.write(imagebuffer, "jpg", out);
  			Image img =Image.getInstance(out.toByteArray()); 
  			img.setScaleToFitHeight(true);
  		    img.setAbsolutePosition(480 , 730);
  			//document.add(img);
  			cd.addImage(img);
  		} catch (Exception e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
      }
	 
		PdfPTable table1 = new PdfPTable(3);
		int width1[] = { 35, 35,30 };
		int width2[] = { 35,35,30 };
		table1.setWidths(width1);
		table1.setWidthPercentage(100);
		PdfPCell cell11 = new PdfPCell(new Paragraph("货件ID: " + data.getShipmentid(), font_s));
		PdfPCell cell12 = new PdfPCell(new Paragraph("创建日期: " +GeneralUtil.formatDate(data.getCreatedate()), font_s));
		PdfPCell cell13 = new PdfPCell(new Paragraph("SKU数量: " + data.getSkuamount().toString(), font_s));
		cell11.setBorder(0);
		cell12.setBorder(0);
		cell13.setBorder(0);
		table1.addCell(cell11);
		table1.addCell(cell12);
		table1.addCell(cell13);
		table1.setSpacingAfter(5);
		document.add(table1);

		PdfPTable table2 = new PdfPTable(3);
		table2.setWidths(width1);
		table2.setWidthPercentage(100);
		String warehouse = "";
		if (data.getWarehouse() != null && data.getWarehouse().toString().contains("-")) {
			warehouse += "发货仓库: " + data.getWarehouse().toString().split("-")[0];
		} else {
			warehouse += "发货仓库: " + data.getWarehouse();
		}
		PdfPCell cell21= new PdfPCell(new Paragraph(warehouse, font_s));
		PdfPCell cell22 = new PdfPCell(new Paragraph("目的仓库: " + data.getGroupname() + "-"
				+ data.getCountry() + "(" + data.getCenter() + ")", font_s));
	
		PdfPCell cell23 = new PdfPCell(new Paragraph("发货数量: " + data.getSumQuantity(), font_s));

		cell21.setBorder(0);
		cell22.setBorder(0);
		cell23.setBorder(0);
		table2.addCell(cell21);
		table2.addCell(cell22);
		table2.addCell(cell23);
		table2.setSpacingAfter(5);
		document.add(table2);

		PdfPTable table4 = new PdfPTable(3);
		table4.setWidths(width2);
		table4.setWidthPercentage(100);
		String remark =data.getRemark();
		String auditime = GeneralUtil.fmtDate(data.getAuditime());
		PdfPCell cell41 = new PdfPCell(new Paragraph("备注: " + (remark==null?"无":remark) , font_s));
		PdfPCell cell42 = new PdfPCell(new Paragraph("审核时间: " + auditime , font_s));
		PdfPCell cell43 = new PdfPCell(new Paragraph("" , font_s));
		cell41.setBorder(0);
		cell42.setBorder(0);
		cell43.setBorder(0);
		table4.addCell(cell41);
		table4.addCell(cell42);
		table4.addCell(cell43);
		table4.setSpacingAfter(8);
		document.add(table4);
 
		PdfPTable table = new PdfPTable(5);
		float[] widths = { 45f, 199f, 40f, 50f, 35f };
		table.setWidths(widths); // 设置宽度
		table.setWidthPercentage(100);
		 
		String[] tableTitleList = { "图片", "产品信息", "平台SKU", "拟发货数", "配送数量" };
		for(int i=0;i<tableTitleList.length;i++) {
			PdfPCell cell = new PdfPCell(new Phrase(tableTitleList[i], font_s));
			if(i==0) {
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_CENTER);
			}
			cell.setFixedHeight(20);
			table.addCell(cell);
		}
		PdfPCell cell =null;
		String ftype="";
		for (int i = 0; i < data.getItemList().size(); i++) {
			 ShipInboundItemVo item = data.getItemList().get(i);
			//---------------------图片
 			  List<AssemblyVO> assList = item.getAssemblyList();
 			 
 			 List<MaterialConsumable> conList = materialConsumableService.selectByMainmid(item.getMaterialid());
		 
			ftype="mainsku";
			String imgpath = item.getImage();
			Image image = getImage( imgpath);
			if(image!=null) {
				cell = new PdfPCell(image, true);
			}else {
				cell = new PdfPCell();
			}
			
			cell.setPadding(1);
			cell.setFixedHeight(45);
			table.addCell(cell);
            //---------------------SKU
			String value2=item.getName();
			String profix="SKU:";
			if(assList!=null&&assList.size()>0) {
				if(conList!=null&&conList.size()>0) {
				    profix="主SKU(带耗材):";
				}else {
					profix="主SKU:";
				}
			} else if(conList!=null&&conList.size()>0) {
				profix="SKU(带耗材):";
			}
			
			com.itextpdf.text.Font mfont = new com.itextpdf.text.Font(baseFont, 10, com.itextpdf.text.Font.BOLD);
			Chunk a2 = new Chunk(profix+item.getSku(), mfont);
			a2.setLineHeight(10);
			Chunk a3 = new Chunk(value2, font_s);
			a3.setLineHeight(10);
			 cell = new PdfPCell();
			 cell.addElement(a2);
			 cell.addElement(a3);
			table.addCell(cell);
			//---------------------平台SKU
			StringBuffer fnskubuffer = new StringBuffer();
			fnskubuffer.append(item.getSellersku());
			if (item.getFNSKU() == null) {
				fnskubuffer.append("");
			} else { 
				fnskubuffer.append("\n\n(FNSKU)\n" + item.getFNSKU());
			}
			addCell(table,fnskubuffer.toString(),font_s,ftype);
			//---------------------拟发货数量
			StringBuffer shelfbuffer = new StringBuffer();
			shelfbuffer.append(item.getQuantity()+"\n\n库存:"+item.getInvquantity());
			List<WarehouseShelfInventoryOptRecordVo> shelflist = iWarehouseShelfInventoryOptRecordService.getRecordVo(user.getCompanyid(), item.getId(),
					"outstockform", item.getMaterialid());
			if(shelflist!=null && shelflist.size()>0) {
				String shelfstr="";
				for(WarehouseShelfInventoryOptRecordVo shelfitem:shelflist) {
					if(shelfitem.getShelfname()!=null) {
						shelfstr=shelfstr+shelfitem.getShelfname()+" ×"+shelfitem.getQuantity()+",";
					}
				}
				shelfbuffer.append("\n库位:\n"+shelfstr);
			}
			addCell(table,shelfbuffer,font_s,ftype);
			int shipqty =item.getQuantity();
			//---------------------配送数量
			addCell(table," ",font_s,ftype);
			//---------------------组装清单
			if (assList!=null) {
				for(int j=0;j<assList.size();j++) {
					AssemblyVO ass = assList.get(j);
					ftype = "subsku";
					imgpath =ass.getImage();
					cell = new PdfPCell(getImage( imgpath), true);
					cell.setPadding(1);
					cell.setFixedHeight(45);
					table.addCell(cell);
					String assShelfstr="";
					 List<WarehouseShelfInventoryOptRecordVo> assshelflist = iWarehouseShelfInventoryOptRecordService.
							 getRecordVo(user.getCompanyid(), item.getId(),"outstockform", ass.getSubmid());
					if(assshelflist!=null && assshelflist.size()>0) {
						for(WarehouseShelfInventoryOptRecordVo shelfitem:assshelflist) {
							if(shelfitem.getShelfname()!=null) {
								assShelfstr=assShelfstr+shelfitem.getShelfname()+" ×"+shelfitem.getQuantity()+",";
							}
						}
						assShelfstr="库位:\n"+assShelfstr.substring(0, assShelfstr.length()-1) ;
					}
					
					addCell(table,"子SKU："+ass.getSku()+"\n"+ass.getMname(),font_s,ftype);
					//---------------------单位数量
					addCell(table,"单位数量\nx"+ass.getSubnumber(),font_s,ftype);
					 
					//---------------------拟发货数量
					addCell(table,shipqty*ass.getSubnumber()
							+"\n\n库存:"+ass.getFulfillable()+"\n"+assShelfstr
							,font_s,ftype);
					//---------------------配送数量
					addCell(table," ",font_s,ftype);
			  
				}
				
			}
            
			if(conList!=null && conList.size()>0) {
				for(int j=0;j<conList.size();j++) {
				    MaterialConsumable cons = conList.get(j);
					ftype = "subsku";
					Material subm = materialService.getById(cons.getSubmaterialid());
					imgpath = subm.getImage();
							
					cell = new PdfPCell(getImage( imgpath), true);
					cell.setPadding(1);
					cell.setFixedHeight(45);
					table.addCell(cell);
					//---------------------产品信息
					String fiedvalue="耗材SKU："+subm.getSku()+"\n"+subm.getName();
					  cell = new PdfPCell(new Phrase(fiedvalue, font_s));
					  table.addCell(cell);
				    addCell(table,"单位数量\nx"+cons.getAmount(),font_s,ftype);
	 
					//---------------------拟发货数量
				    Map<String, Object> inv = inventoryService.findInvDetailById(subm.getId(), data.getWarehouseid(), data.getShopid());
				    Integer fulfillable = 0;
				     if(inv!=null&&inv.get("fulfillable")!=null&&StrUtil.isNotBlank(inv.get("fulfillable").toString())) {
				    	 fulfillable=Integer.parseInt(inv.get("fulfillable").toString());
				    	 
				     }
					addCell(table,(shipqty*cons.getAmount().floatValue())
							+"\n\n库存:"+fulfillable
							,font_s,ftype);
					//---------------------配送数量
					addCell(table," ",font_s,ftype);
				}
			}
			
		}
		//---------------------耗材清单

			document.add(table);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("PDF创建失败"+e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("PDF创建失败"+e.getMessage());
		}
	}

	@Override
	public void setPDFDocShipForm(PdfWriter writer,UserInfo user, Document document, ShipInboundShipmenSummarytVo data) {
		// TODO Auto-generated method stub
		try {
		document.addTitle("配货单");
		document.addAuthor("wimoor");
		// step 3
		document.open();
		// step 4
		BaseFont baseFont = null;
		// 方法一：使用Windows系统字体(TrueType)
		String path = new ClassPathResource("font/SIMYOU.TTF").getPath();
		baseFont = BaseFont.createFont(path, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		com.itextpdf.text.Font font = new com.itextpdf.text.Font(baseFont);
		com.itextpdf.text.Font font_s = new com.itextpdf.text.Font(baseFont, 10, com.itextpdf.text.Font.NORMAL);
		Paragraph p = new Paragraph("配货单", font);// 设置字体，解决中文显示不了的问题
		p.setAlignment(Element.ALIGN_CENTER);
		p.setSpacingAfter(10);
		document.add(p);

          PdfContentByte cd = writer.getDirectContent();
        if(data.getShipmentid().indexOf("FBA")>-1) {
        	String content = "https://www.wimoor.com/wxsc/"+data.getShipmentid();//内容信息
    		try {
    			qrconfig.setWidth(70);
    			qrconfig.setHeight(70);
    			BufferedImage imagebuffer =	QrCodeUtil.generate(content, qrconfig);
    			ByteArrayOutputStream out=new ByteArrayOutputStream();
    		    ImageIO.write(imagebuffer, "jpg", out);
    			Image img =Image.getInstance(out.toByteArray()); 
    			img.setScaleToFitHeight(true);
    		    img.setAbsolutePosition(480 , 730);
    			//document.add(img);
    			cd.addImage(img);
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        }
		

		PdfPTable table1 = new PdfPTable(2);
		int width1[] = { 50, 50 };
		//int width2[] = { 100 };
		table1.setWidths(width1);
		table1.setWidthPercentage(100);
		PdfPCell cell11 = new PdfPCell(new Paragraph("货件ID: " + data.getShipmentid(), font_s));
		PdfPCell cell12 = new PdfPCell(new Paragraph("创建日期: " + GeneralUtil.formatDate(data.getCreatedate()), font_s));
		cell11.setBorder(0);
		cell12.setBorder(0);
		table1.addCell(cell11);
		table1.addCell(cell12);
		table1.completeRow();

		PdfPCell cell21 = new PdfPCell(new Paragraph("SKU数量: " +data.getSkuamount(), font_s));
		PdfPCell cell22 = new PdfPCell(new Paragraph("发货数量: " +data.getSumQuantity(), font_s));
		cell21.setBorder(0);
		cell22.setBorder(0);
		table1.addCell(cell21);
		table1.addCell(cell22);
		table1.completeRow();

		PdfPCell cell31 = new PdfPCell(new Paragraph("目的仓库: " + data.getGroupname() + "-"
						+ data.getCountry() + "(" + data.getCenter() + ")", font_s));
		String warehouse = "";
		if (data.getWarehouse() != null && data.getWarehouse().contains("-")) {
			warehouse += "发货仓库: " + data.getWarehouse().split("-")[0];
		} else {
			warehouse += "发货仓库: " + data.getWarehouse();
		}
		PdfPCell cell32 = new PdfPCell(new Paragraph(warehouse, font_s));
		cell31.setBorder(0);
		cell32.setBorder(0);
		table1.addCell(cell31);
		table1.addCell(cell32);
		table1.completeRow();
		
		String remark = data.getRemark();
		String auditime = GeneralUtil.fmtDate(data.getAuditime());
		PdfPCell cell41 = new PdfPCell(new Paragraph("备注: " + (remark==null?"无":remark), font_s));
		PdfPCell cell42 = new PdfPCell(new Paragraph("审核时间: " + auditime , font_s));
		cell41.setBorder(0);
		cell42.setBorder(0);
		table1.addCell(cell41);
		table1.addCell(cell42);
		table1.completeRow();
		table1.setSpacingAfter(8);
		document.add(table1);
	 

		List<List<String>> datalist = getData(data.getItemList(),user.getCompanyid());// 获取table内容
		PdfPTable table = new PdfPTable(7);
		float[] widths = { 45f, 50f, 135f, 40f, 32f, 32f, 35f };
		table.setWidths(widths); // 设置宽度
		table.setWidthPercentage(100);
		for (int i = 0; i < datalist.size(); i++) {
			for (int j = 0; j < datalist.get(i).size(); j++) {
				PdfPCell cell = new PdfPCell();
				if (i > 0 && j == 0) {
						String imgpath = datalist.get(i).get(j);
						cell = new PdfPCell(getImage( imgpath), true);
						cell.setPadding(5);
				 
				} else {
					cell = new PdfPCell(new Phrase(datalist.get(i).get(j), font_s));
				}
				if (i == 0) {
					if (j == 0) {
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell.setVerticalAlignment(Element.ALIGN_CENTER);
					}
					cell.setFixedHeight(20);
				}
				table.addCell(cell);
			}
		}
		document.add(table);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("PDF创建失败"+e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("PDF创建失败"+e.getMessage());
		}
	}

	@Override
	public List<Map<String, Object>> findPlanSubDetail(String planid, String marketplaceid, String warehouseid) {
		return shipPlanItemMapper.findPlanSubDetail(planid, marketplaceid, warehouseid);
	}
	
	@Override
	public List<Map<String, Object>> findPlanEuSubDetail(String planid, String marketplaceid, String warehouseid) {
		return shipPlanItemMapper.findPlanEuSubDetail(planid, marketplaceid, warehouseid);
	}
 
}
