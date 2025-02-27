package com.wimoor.amazon.inboundV2.service.impl;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.inboundV2.pojo.dto.ShipLabelDto;
import com.wimoor.amazon.inboundV2.pojo.vo.ShipInboundItemVo;
import com.wimoor.amazon.inboundV2.pojo.vo.ShipPlanVo;
import com.wimoor.amazon.inboundV2.service.IShipInboundPlanQuotaV2Service;
import com.wimoor.amazon.product.pojo.entity.ProductInfo;
import com.wimoor.amazon.product.service.IProductInfoService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.mvc.FileUpload;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.assembly.pojo.vo.AssemblyVO;
import com.wimoor.erp.material.pojo.vo.DimensionsInfoVo;
import com.wimoor.erp.material.pojo.vo.MaterialConsumableVO;
import com.wimoor.erp.material.pojo.vo.MaterialVO;
import com.wimoor.erp.ship.pojo.dto.QuotaInfoDTO;
import com.wimoor.erp.warehouse.pojo.vo.WarehouseShelfInventoryOptRecordVo;

import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import lombok.RequiredArgsConstructor;

@Service("shipInboundPlanQuotaV2Service")
@RequiredArgsConstructor
public class ShipInboundPlanQuotaV2ServiceImpl implements IShipInboundPlanQuotaV2Service{
	@Autowired
	FileUpload fileUpload;
	@Resource
    QrConfig qrconfig;
	final IProductInfoService iProductInfoService;

	final IAmazonAuthorityService amazonAuthorityService;
	
	final IMarketplaceService marketplaceService;
	private void addCell(PdfPTable table ,Object value,com.itextpdf.text.Font font_s,String ftype) {
		String fiedvalue =value!=null?value.toString():" ";
		PdfPCell cell = new PdfPCell(new Phrase(fiedvalue, font_s));
		table.addCell(cell);
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
	 
	public List<List<String>> getData( QuotaInfoDTO quotaInfoDto,String shopid) {
		List<MaterialVO> itemlist = quotaInfoDto.getList();
		List<List<String>> data = new ArrayList<List<String>>();
		String[] tableTitleList = { "图片", "本地SKU", "产品名称", "平台SKU", "可用库存", "拟发货数", "配送数量" };
		data.add(Arrays.asList(tableTitleList));
		for (int i = 0; i < itemlist.size(); i++) {
			MaterialVO item=itemlist.get(i);
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
				if(item.getBoxdimension()!=null) {
					DimensionsInfoVo boxdim = item.getBoxdimension();
					if(boxdim.getWeight()!=null) {
						namecell=namecell+"    重kg:"+boxdim.getWeight().toString();
					}
					if(boxdim.getLength()!=null&&boxdim.getWidth()!=null&&boxdim.getHeight()!=null) {
						namecell=namecell+"\n长*宽*高cm："+boxdim.getLength().toString()+"*"+boxdim.getWidth().toString()+"*"+boxdim.getHeight().toString();
					}
				}
			}
			  List<MaterialConsumableVO> conList = item.getConList();
			if(item!=null&&conList!=null&&conList.size()>0&&skucell!=null) {
				namecell=namecell+"\n\n(耗材SKU):";
				int row=0;
				for(MaterialConsumableVO conitem:conList) {
						if(row==0) {
							namecell=namecell+item.getSku()+"×"+conitem.getAmount().intValue();
						}else {
							namecell=namecell+"，"+item.getSku()+"×"+conitem.getAmount().intValue();
						}
						row++;
				}
			}
			//继续加子产品的库位库存
			if(item.getAssemblyList()!=null && item.getAssemblyList().size()>0) {
				String subskushef="";
				for(AssemblyVO asitem:item.getAssemblyList()) {
					String shelfinvitem="";
					List<WarehouseShelfInventoryOptRecordVo> shelflist =asitem.getShelfInvRecordList();
					if(item!=null&&shelflist!=null&&shelflist.size()>0&&skucell!=null) {
						int row=0;
						for(WarehouseShelfInventoryOptRecordVo shelfitem:shelflist) {
							if(shelfitem.getShelfname()!=null) {
								if(row==0) {
									shelfinvitem=shelfinvitem+shelfitem.getShelfname()+"×"+shelfitem.getQuantity();
								}else {
									shelfinvitem=shelfinvitem+" , "+shelfitem.getShelfname()+"×"+shelfitem.getQuantity();
								}
								row++;
							}
						}
						if(subskushef.equals("")) {
							subskushef=subskushef+asitem.getSku()+":"+shelfinvitem;
						}else {
							subskushef=subskushef+"\n"+asitem.getSku()+":"+shelfinvitem;
						}
					} 
				}
				if(!subskushef.equals("")) {
					namecell=namecell+"\n子SKU库位:\n"+subskushef;
				}
			}
			dataLine.add(namecell);
			String sellersku="";
			if(item.getExtendInfo()!=null) {
				Map<String, Object> ext = item.getExtendInfo();
				if(ext.get("sellersku")!=null) {
					 sellersku = ext.get("sellersku").toString();
				}
				if (ext.get("fnsku")!=null) {
					   sellersku=sellersku+"\n\n(FNSKU)\n" + ext.get("fnsku").toString();
				}  
			}
			
			
			dataLine.add(sellersku);
			String shelfinv = item.getFulfillable()!=null?item.getFulfillable()+"":"0";
			List<WarehouseShelfInventoryOptRecordVo> shelflist =item.getShelfInvRecordList();
			if(item!=null&&shelflist!=null&&shelflist.size()>0&&skucell!=null) {
				shelfinv=shelfinv+"\n库位:\n";
				int row=0;
				for(WarehouseShelfInventoryOptRecordVo shelfitem:shelflist) {
					if(shelfitem.getShelfname()!=null) {
						if(row==0) {
							shelfinv=shelfinv+shelfitem.getShelfname()+"×"+shelfitem.getQuantity()+"\n";
						}else {
							shelfinv=shelfinv+shelfitem.getShelfname()+"×"+shelfitem.getQuantity()+"\n";
						}
						row++;
					}
				}
			}
			dataLine.add(shelfinv);
			if(item.getFulfillable()!=null){
				dataLine.add(item.getAmount().toString());
			}else{
				dataLine.add("0");
			}
			dataLine.add("");
			data.add(dataLine);
		}
		return data;
	}
	
	public void setPDFDocShipFormDetail(PdfWriter writer,UserInfo user, Document document,  QuotaInfoDTO quotaInfoDto,List<ShipPlanVo> volist) {
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
        if(volist!=null&&volist.size()==1) {
        	ShipPlanVo vo = volist.get(0);
        	String content = "wxsc/"+vo.getId();//内容信息
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
  	String number=null;
	Set<String> skuset=new HashSet<String>();
	String createdate=null;
	Integer sumqty=0;
	String groupname=null;
	String warehousename=null;
	String remark=null;
	String auditime =null;
	for(ShipPlanVo vo:volist) {
		number=number==null?vo.getNumber():number+","+vo.getNumber();
		if(createdate==null) {
			createdate= GeneralUtil.formatDate(vo.getCreatetime());
		}
		if(warehousename==null) {
			warehousename=vo.getWarename();
		}else if(!warehousename.equals(vo.getWarename())) {
			throw new BizException("不同仓库无法同时配货");
		}
		if( auditime ==null) {
			  auditime = GeneralUtil.fmtDate(vo.getAuditime());
		}
		if(groupname==null) {
			groupname=vo.getGroupname()+"-"+vo.getCountry();
		}else if(!groupname.contains(vo.getGroupname()+"-"+vo.getCountry())) {
			groupname=groupname+","+vo.getGroupname()+"-"+vo.getCountry();
		}
		if(remark==null) {
			remark= vo.getRemark();
		}else if(!remark.contains(vo.getRemark())) {
			remark=remark+","+vo.getRemark();
		}	
		
		for(ShipInboundItemVo item:vo.getItemlist()) {
			skuset.add(item.getSku());
			sumqty=sumqty+item.getQuantity();
		}
	   }
		PdfPTable table1 = new PdfPTable(3);
		int width1[] = { 35, 35,30 };
		int width2[] = { 35,35,30 };
		table1.setWidths(width1);
		table1.setWidthPercentage(100);
		PdfPCell cell11 = new PdfPCell(new Paragraph("订单编号: " + number, font_s));
		PdfPCell cell12 = new PdfPCell(new Paragraph("创建日期: " +createdate, font_s));
		PdfPCell cell13 = new PdfPCell(new Paragraph("SKU数量: " +skuset.size(), font_s));
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
		if (warehousename!= null && warehousename.contains("-")) {
			warehouse += "发货仓库: " + warehousename.split("-")[0];
		} else {
			warehouse += "发货仓库: " + warehousename;
		}
		PdfPCell cell21= new PdfPCell(new Paragraph(warehouse, font_s));
		PdfPCell cell22 = new PdfPCell(new Paragraph("目的仓库: " +groupname, font_s));
	
		PdfPCell cell23 = new PdfPCell(new Paragraph("发货数量: " + sumqty, font_s));

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
		List<MaterialVO> itemlist = quotaInfoDto.getList();
		if(itemlist==null) {
			return ;
		}
		for (int i = 0; i <itemlist.size(); i++) {
			 MaterialVO item=itemlist.get(i);
			//---------------------图片
 			  List<AssemblyVO> assList = item.getAssemblyList();
 			 
 			   List<MaterialConsumableVO> conList = item.getConList();
		 
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
			String sellersku="";
			if(item.getExtendInfo()!=null) {
				Map<String, Object> ext = item.getExtendInfo();
				if(ext.get("sellersku")!=null) {
					 sellersku = ext.get("sellersku").toString();
				}
				if (ext.get("fnsku")!=null) {
					   sellersku=sellersku+"\n\n(FNSKU)\n" + ext.get("fnsku").toString();
				}  
			}
			fnskubuffer.append(sellersku);
			addCell(table,fnskubuffer.toString(),font_s,ftype);
			//---------------------拟发货数量
			StringBuffer shelfbuffer = new StringBuffer();
			shelfbuffer.append(item.getAmount()+"\n\n库存:"+(item.getFulfillable()!=null?item.getFulfillable()+"":"0"));
			List<WarehouseShelfInventoryOptRecordVo> shelflist  =item.getShelfInvRecordList();
			if(shelflist!=null && shelflist.size()>0) {
				String shelfstr="";
				for(WarehouseShelfInventoryOptRecordVo shelfitem:shelflist) {
					if(shelfitem.getShelfname()!=null) {
						if(shelfstr.equals("")) {
							shelfstr=shelfstr+shelfitem.getShelfname()+"×"+shelfitem.getQuantity();
						}else {
							shelfstr=shelfstr+"\n"+shelfitem.getShelfname()+"×"+shelfitem.getQuantity();
						}
						
					}
				}
				shelfbuffer.append("\n库位:\n"+shelfstr);
			}
			addCell(table,shelfbuffer,font_s,ftype);
			int shipqty =item.getAmount();
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
					 List<WarehouseShelfInventoryOptRecordVo> assshelflist = ass.getShelfInvRecordList();
					if(assshelflist!=null && assshelflist.size()>0) {
						for(WarehouseShelfInventoryOptRecordVo shelfitem:assshelflist) {
							if(shelfitem.getShelfname()!=null) {
								if(assShelfstr.equals("")) {
									assShelfstr=assShelfstr+shelfitem.getShelfname()+"×"+shelfitem.getQuantity();
								}else {
									assShelfstr=assShelfstr+"\n"+shelfitem.getShelfname()+"×"+shelfitem.getQuantity();
								}
							}
						}
						assShelfstr="库位:\n"+assShelfstr.substring(0, assShelfstr.length()) ;
					}
					
					addCell(table,"子SKU："+ass.getSku()+"\n"+ass.getMname(),font_s,ftype);
					//---------------------单位数量
					addCell(table,"单位数量\n×"+ass.getSubnumber(),font_s,ftype);
					 
					//---------------------拟发货数量
					addCell(table,shipqty*ass.getSubnumber()
							+"\n\n库存:"+(ass.getFulfillable()!=null?ass.getFulfillable():0)+"\n"+assShelfstr
							,font_s,ftype);
					//---------------------配送数量
					addCell(table," ",font_s,ftype);
			  
				}
				
			}
            
			if(conList!=null && conList.size()>0) {
				for(int j=0;j<conList.size();j++) {
				    MaterialConsumableVO cons = conList.get(j);
					ftype = "subsku";
					imgpath = cons.getImage();
					cell = new PdfPCell(getImage( imgpath), true);
					cell.setPadding(1);
					cell.setFixedHeight(45);
					table.addCell(cell);
					//---------------------产品信息
					String fiedvalue="耗材SKU："+cons.getSku()+"\n"+cons.getName();
					  cell = new PdfPCell(new Phrase(fiedvalue, font_s));
					  table.addCell(cell);
				    addCell(table,"单位数量\n×"+cons.getAmount(),font_s,ftype);
	 
					//---------------------拟发货数量
				    Integer fulfillable = 0;
				     if(cons!=null&&cons.getFulfillable()!=null) {
				    	 fulfillable=cons.getFulfillable();
				    	 
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

	public void setPDFDocShipForm(PdfWriter writer,UserInfo user, Document document,QuotaInfoDTO quotaInfoDto,List<ShipPlanVo> volist) {
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
        if(volist!=null&&volist.size()==1) {
        	ShipPlanVo vo = volist.get(0);
        	String content = "wxsc/"+vo.getId();//内容信息
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
		String number=null;
		Set<String> skuset=new HashSet<String>();
		String createdate=null;
		Integer sumqty=0;
		String groupname=null;
		String warehousename=null;
		String remark=null;
		String auditime =null;
		for(ShipPlanVo vo:volist) {
			number=number==null?vo.getNumber():number+","+vo.getNumber();
			if(createdate==null) {
				createdate= GeneralUtil.formatDate(vo.getCreatetime());
			}
			if(warehousename==null) {
				warehousename=vo.getWarename();
			}else if(!warehousename.equals(vo.getWarename())) {
				throw new BizException("不同仓库无法同时配货");
			}
			if( auditime ==null) {
				  auditime = GeneralUtil.fmtDate(vo.getAuditime());
			}
			if(groupname==null) {
				groupname=vo.getGroupname()+"-"+vo.getCountryname();
			}else if(!groupname.contains(vo.getGroupname()+"-"+vo.getCountryname())) {
				groupname=groupname+","+vo.getGroupname()+"-"+vo.getCountryname();
			}
			if(remark==null) {
				remark= vo.getRemark();
			}else if(!remark.contains(vo.getRemark())) {
				remark=remark+","+vo.getRemark();
			}	
			
			for(ShipInboundItemVo item:vo.getItemlist()) {
				skuset.add(item.getSku());
				sumqty=sumqty+item.getQuantity();
			}
		}

		PdfPTable table1 = new PdfPTable(2);
		int width1[] = { 50, 50 };
		//int width2[] = { 100 };
		table1.setWidths(width1);
		table1.setWidthPercentage(100);
		
	
		PdfPCell cell11 = new PdfPCell(new Paragraph("发货编号: " + number, font_s));
		PdfPCell cell12 = new PdfPCell(new Paragraph("创建日期: " +createdate, font_s));
		cell11.setBorder(0);
		cell12.setBorder(0);
		table1.addCell(cell11);
		table1.addCell(cell12);
		table1.completeRow();

		PdfPCell cell21 = new PdfPCell(new Paragraph("SKU数量: " +skuset.size(), font_s));
		PdfPCell cell22 = new PdfPCell(new Paragraph("发货数量: " +sumqty, font_s));
		cell21.setBorder(0);
		cell22.setBorder(0);
		table1.addCell(cell21);
		table1.addCell(cell22);
		table1.completeRow();

		PdfPCell cell31 = new PdfPCell(new Paragraph("目的仓库: " + groupname , font_s));
		String warehouse = "";
		if (warehousename!= null && warehousename.contains("-")) {
			warehouse += "发货仓库: " + warehousename.split("-")[0];
		} else {
			warehouse += "发货仓库: " + warehousename;
		}
		PdfPCell cell32 = new PdfPCell(new Paragraph(warehouse, font_s));
		cell31.setBorder(0);
		cell32.setBorder(0);
		table1.addCell(cell31);
		table1.addCell(cell32);
		table1.completeRow();
		
		
		PdfPCell cell41 = new PdfPCell(new Paragraph("备注: " + (remark==null?"无":remark), font_s));
		PdfPCell cell42 = new PdfPCell(new Paragraph("审核时间: " + auditime , font_s));
		cell41.setBorder(0);
		cell42.setBorder(0);
		table1.addCell(cell41);
		table1.addCell(cell42);
		table1.completeRow();
		table1.setSpacingAfter(8);
		document.add(table1);
	 

		List<List<String>> datalist = getData(quotaInfoDto,user.getCompanyid());// 获取table内容
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
	
	public void setExcelBookShipmentLabel(String shopid, SXSSFWorkbook workbook,List<Map<String,Object>> paralist) {
		Sheet sheet = workbook.createSheet("sheet1");
		Map<String, Integer> titlemap = new HashMap<String, Integer>();
		titlemap.put("No.", 0);
		titlemap.put("sku", 1);
		titlemap.put("fnsku", 2);
		titlemap.put("country", 3);
		titlemap.put("conditions", 4);
		titlemap.put("quantity", 5);
		titlemap.put("title", 6);
		titlemap.put("code", 7);
		// 在索引0的位置创建行（最顶端的行）
		Row row = sheet.createRow(0);
		for (String key : titlemap.keySet()) {
			Cell cell = row.createCell(titlemap.get(key)); // 在索引0的位置创建单元格(左上端)
			if (!"No.".equals(key)) {
				cell.setCellValue(key.toUpperCase());
			} else {
				cell.setCellValue(key);
			}
		}
		List<Map<String, Object>> skulist=new ArrayList<Map<String,Object>>();
	    skulist=paralist;

		for (int i = 0; i < skulist.size(); i++) {
			Row skurow = sheet.createRow(i + 1);
			Map<String, Object> skumap = skulist.get(i);
			for (String key : skumap.keySet()) {
				Cell cell = skurow.createCell(titlemap.get(key));
				if ("title".equals(key)) {
					String title = GeneralUtil.getValue(skumap.get(key)) ;
					cell.setCellValue( formatterName(title));
				} else {
					if("code".equals(key)) {
						cell.setCellValue(GeneralUtil.getValue(skumap.get(key)).replace("SF20", ""));
					}else {
						cell.setCellValue(GeneralUtil.getValue(skumap.get(key)));
					}
				}
			}
			Cell cell = skurow.createCell(titlemap.get("No.")); // 在索引0的位置创建单元格(左上端)
			cell.setCellValue(i + 1);
			Cell cell2 = skurow.createCell(titlemap.get("conditions")); // 在索引0的位置创建单元格(左上端)
			cell2.setCellValue("New");
		}
	}
	
	public List<Map<String, Object>> getLabelValue(List<ShipLabelDto> dto) {
        Map<String, Marketplace> market = this.marketplaceService.findMapByMarketplaceId();
        List<Map<String,Object>> result=new ArrayList<Map<String,Object>>() ;
		for (int i = 0; i < dto.size(); i++) {
			Map<String,Object> item=new HashMap<String,Object>();
			ShipLabelDto  skumap = dto.get(i);
			item.put("sku", skumap.getSku());
			if(market!=null&&market.get(skumap.getMarketplaceid())!=null) {
				item.put("country", market.get(skumap.getMarketplaceid()).getMarket());
			}
			if(skumap.getAmazonauthid()==null) {
			  AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(skumap.getGroupid(), skumap.getMarketplaceid());
			  skumap.setAmazonauthid(auth.getId());
			}
			List<ProductInfo> infos = this.iProductInfoService.selectBySku(skumap.getSku(), skumap.getMarketplaceid(), skumap.getAmazonauthid());
			if(infos!=null&&infos.size()>0) {
				ProductInfo info=infos.get(0);
				item.put("fnsku", info.getFnsku());
				item.put("title", info.getName());
				item.put("conditions",info.getPcondition());
				item.put("code",skumap.getNumber());
			}
			item.put("quantity",skumap.getAmount());
			result.add(item);
		}
		return result;
	}
	
	@SuppressWarnings("deprecation")
	public static String formatterName(String title) {
		char[] array = title.toCharArray();
		String path = new ClassPathResource("font/simsun.ttc").getPath();
		Font f = new Font(path, Font.PLAIN, 12);
		FontMetrics fm =Toolkit.getDefaultToolkit().getFontMetrics (f) ;
		String pre = "";
		int begin = 0;
		for (; begin < array.length; begin++) {
			pre = pre + array[begin];
			if (fm.stringWidth(pre) >= 114)
				break;
		}
		String rear = "";
		for (int end = array.length - 1; end > begin; end--) {
			rear = array[end] + rear;
			if (fm.stringWidth(rear) >= 114)
				break;
		}
		return pre + "..." + rear;
	}
	
	public void setPDFDocLabel(Document document, List<Map<String,Object>> skulist, PdfWriter writer){
		document.addTitle("产品标签");
		document.addAuthor("wimoor");
		try {
		// step 3
		document.open();
		// step 4
		BaseFont baseFont = null;
		String path = new ClassPathResource("font/ARIALN.TTF").getPath();
		baseFont = BaseFont.createFont(path, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		com.itextpdf.text.Font font = new com.itextpdf.text.Font(baseFont, 8);
		// 方法一：使用Windows系统字体(TrueType)
		String path2 = new ClassPathResource("font/SIMYOU.TTF").getPath();
		baseFont = BaseFont.createFont(path2, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		com.itextpdf.text.Font font_zh = new com.itextpdf.text.Font(baseFont, 8);
		for (int i = 0; i < skulist.size(); i++) {
			if (skulist.get(i).get("fnsku") == null || skulist.get(i).get("quantity") == null) {
				Paragraph msg = new Paragraph(skulist.get(i).get("sku").toString() + "数据异常", font_zh);
				document.add(msg);
			} else {
				String fnsku = skulist.get(i).get("fnsku").toString().trim();// "X001IWP8ZH"
				Barcode128 code128 = new Barcode128();
				code128.setCode(fnsku);
				code128.setCodeType(Barcode128.CODE128);
				PdfContentByte cb = writer.getDirectContent();
				Image code128Image = code128.createImageWithBarcode(cb, null, null);
				code128Image.scalePercent(100);
				String title = skulist.get(i).get("title").toString();
				title = formatterName(title);
				Paragraph p = new Paragraph(title, font);
				Paragraph p2 = new Paragraph("新品", font_zh);
				p2.setSpacingAfter(10f);
				String quantity = skulist.get(i).get("quantity") == null ? "0" : skulist.get(i).get("quantity").toString();
				for (int j = 0; j < Integer.parseInt(quantity); j++) {
					document.add(code128Image);
					document.add(p);
					document.add(p2);
				}
			}
			}
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new BizException("PDF创建失败"+e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			throw new BizException("PDF创建失败"+e.getMessage());
		}
		
	}


	
	 
}
