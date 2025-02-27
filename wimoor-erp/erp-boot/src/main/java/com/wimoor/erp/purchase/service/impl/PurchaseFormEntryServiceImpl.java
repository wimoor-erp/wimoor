package com.wimoor.erp.purchase.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Sets;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.mvc.FileUpload;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.api.AmazonClientOneFeignManager;
import com.wimoor.erp.assembly.mapper.AssemblyEntryInstockMapper;
import com.wimoor.erp.customer.mapper.CustomerMapper;
import com.wimoor.erp.customer.pojo.entity.Customer;
import com.wimoor.erp.inventory.pojo.vo.MaterialInventoryVo;
import com.wimoor.erp.inventory.service.IInventoryService;
import com.wimoor.erp.purchase.mapper.PurchaseFormEntryHistoryMapper;
import com.wimoor.erp.purchase.mapper.PurchaseFormEntryMapper;
import com.wimoor.erp.purchase.mapper.PurchaseFormMapper;
import com.wimoor.erp.purchase.mapper.PurchaseFormReceiveMapper;
import com.wimoor.erp.purchase.pojo.dto.PurchaseTimeDTO;
import com.wimoor.erp.purchase.pojo.entity.PurchaseForm;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormEntry;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormEntryHistory;
import com.wimoor.erp.purchase.service.IPurchaseFormEntryService;
import com.wimoor.erp.stock.mapper.StockTakingItemMapper;
import com.wimoor.erp.warehouse.pojo.entity.Warehouse;
import com.wimoor.erp.warehouse.service.IWarehouseService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import com.wimoor.erp.stock.service.IChangeWhFormEntryService;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;

@Service("purchaseFormEntryService")
@RequiredArgsConstructor
public class PurchaseFormEntryServiceImpl extends  ServiceImpl<PurchaseFormEntryMapper,PurchaseFormEntry> implements IPurchaseFormEntryService {

	final PurchaseFormMapper purchaseFormMapper;
	final IWarehouseService warehouseService;
	final CustomerMapper customerMapper;
	final PurchaseFormEntryHistoryMapper purchaseFormEntryHistoryMapper;
	final PurchaseFormReceiveMapper purchaseFormReceiveMapper;
	final AssemblyEntryInstockMapper assemblyEntryInstockMapper;
	final IInventoryService iInventoryService;
	final IChangeWhFormEntryService iChangeWhFormEntryService;
	final StockTakingItemMapper stockTakingItemMapper;
	@Autowired
	FileUpload fileUpload;
    @Autowired
    private AmazonClientOneFeignManager amazonClientOneFeignManager;

	@Override
	public Map<String, Object> summaryBySupplier(String shopid,String supplier) {
		return this.baseMapper.summaryBySupplier(shopid,supplier);
	}

	@Override
	public void downloadPurchaseInfoWord(HttpServletResponse response, Map<String, Object> map,UserInfo userinfo) {
		Workbook workbook = null;
		ServletOutputStream fOut = null;
		String supplierid=map.get("supplierid").toString();
		PurchaseForm from = purchaseFormMapper.selectById(map.get("formid").toString());
		List<Map<String, Object>> entrylist = purchaseFormMapper.findEntryByIdAndSupplier(map.get("formid").toString(),supplierid);
		if(entrylist.size()<=0 || entrylist==null) {
			throw new BizException("无产品列表信息!");
		}
		Warehouse warehouse = warehouseService.getById(map.get("warehouseid").toString());
		Customer customer=customerMapper.selectById(supplierid);
		String myCompany=map.get("company").toString();
		String myAddress=warehouse.getAddress();
		String myName=map.get("buyerName").toString();
		 
		String myPhone="";
		if(userinfo.getUserinfo().get("tel")!=null) {
			myPhone=userinfo.getUserinfo().get("tel").toString();
		}else {
			myPhone=userinfo.getAccount();
		}
		String date=map.get("buyerDate").toString();
		String remark="无";
		if(from!=null) {
			remark=from.getRemark();
			if(StrUtil.isEmpty(remark)) {
				remark="无";
			}
		}
		if(StrUtil.isEmpty(remark.trim()))remark="无";
		String deliverydate="        ";
		if(entrylist.get(0).get("deliverydate")!=null&&StrUtil.isNotBlank(entrylist.get(0).get("deliverydate").toString())) {
			deliverydate=entrylist.get(0).get("deliverydate").toString().substring(0, 10);
		}
		String heCompany="";
		String heName="";
		String heAddress="";
		String hePhone="";
		if(customer!=null) {
			heCompany=customer.getName();
			heName=customer.getContacts();
			heAddress=customer.getAddress();
			hePhone=customer.getPhone_num();
		}
		 try {
			 response.setContentType("application/force-download");// 设置强制下载不打开
			 response.addHeader("Content-Disposition", "attachment;fileName=downloadPurchaseInfoWord" + System.currentTimeMillis() +".xlsx");// 设置文件名
			 InputStream is = new ClassPathResource("template/PurchaseFormContract.xlsx").getInputStream();
			// 创建新的Excel工作薄
			 workbook = WorkbookFactory.create(is);
			 Sheet sheet = workbook.getSheet("采购合同");
			 //先添加表格需要的行
			 if(entrylist.size()>1) {
				 int startRowindex=19;
				 for(int k=0;k<entrylist.size()-1;k++) {
					 sheet.shiftRows(startRowindex, sheet.getLastRowNum(), 1, true, false);
					 Row addrow = sheet.createRow(startRowindex);
					 org.apache.poi.ss.usermodel.Cell cell = addrow.createCell(2,CellType.STRING);
					 cell.setCellValue("$image");
					 org.apache.poi.ss.usermodel.Cell cell2 = addrow.createCell(3,CellType.STRING);
					 cell2.setCellValue("$sku");
					 org.apache.poi.ss.usermodel.Cell cell3 = addrow.createCell(4,CellType.STRING);
					 cell3.setCellValue("$skuname");
					 org.apache.poi.ss.usermodel.Cell cell4 = addrow.createCell(5,CellType.STRING);
					 cell4.setCellValue("$specification");
					 org.apache.poi.ss.usermodel.Cell cell5 = addrow.createCell(6,CellType.STRING);
					 cell5.setCellValue("$num");
					 org.apache.poi.ss.usermodel.Cell cell6 = addrow.createCell(7,CellType.STRING);
					 cell6.setCellValue("$itemprice");
					 org.apache.poi.ss.usermodel.Cell cell7 = addrow.createCell(8,CellType.STRING);
					 cell7.setCellValue("$orderprice");
					 org.apache.poi.ss.usermodel.Cell cell8 = addrow.createCell(9,CellType.STRING);
					 cell8.setCellValue("$remark");
				 }
			 }
			 //提前生成样式
			 	CellStyle style=workbook.createCellStyle();
				style.setAlignment(HorizontalAlignment.LEFT);
				org.apache.poi.ss.usermodel.Font font = workbook.createFont();
				font.setFontHeightInPoints((short)10);
				style.setFont(font);
				style.setWrapText(true);
			  //遍历行row
			 for (int i=0;i<=sheet.getLastRowNum();i++){
				 Row sheetRow = sheet.getRow(i);
				 if (sheetRow == null) {
					 continue;
				 }
				//遍历列cell
				for (int cellnum = 0; cellnum <= sheetRow.getLastCellNum(); cellnum++) {
					org.apache.poi.ss.usermodel.Cell cell = sheetRow.getCell(cellnum);
					if (cell == null) {
						continue;
					}
					cell.setCellType(CellType.STRING);
					String cellValue = cell.getStringCellValue().trim();
					if(StrUtil.isNotEmpty(cellValue)) {
						if("$myCompany".equals(cellValue)) {
							cell.setCellValue(myCompany);
						}
						if("$myAddress".equals(cellValue)) {
							cell.setCellValue(myAddress);
						}
						if("$myName".equals(cellValue)) {
							cell.setCellValue(myName);
						}
						if("$myPhone".equals(cellValue)) {
							cell.setCellValue(myPhone);
						}
						if("$date".equals(cellValue)) {
							cell.setCellValue(date);
						}
						if("$heCompany".equals(cellValue)) {
							cell.setCellValue(heCompany);
						}
						if("$heName".equals(cellValue)) {
							cell.setCellValue(heName);
						}
						if("$heAddress".equals(cellValue)) {
							cell.setCellValue(heAddress);
						}
						if("$hePhone".equals(cellValue)) {
							cell.setCellValue(hePhone);
						}
						if("$orderremark".equals(cellValue)) {
							if(remark.contains(";;")) {
								CellStyle cs = workbook.createCellStyle();  
							    cs.setWrapText(true);  //关键
								cell.setCellStyle(cs);
								remark=remark.replaceAll(";;", "\n\r");
								cell.setCellValue(new XSSFRichTextString(remark));
							}else {
								cell.setCellValue(remark);
							}
							
						}
						if("交货期：【$deliverDate】".equals(cellValue)) {
							cell.setCellValue("交货期：【"+deliverydate+"】");
						}
					}
				}
				//处理生成表格数据。。。。 遍历表格的行
				int startIndex=19;
				for(int j=0;j<entrylist.size();j++) {
					 Map<String, Object> item = entrylist.get(j);
						 Row tableRow = sheet.getRow(startIndex-1);
						 if (tableRow == null) {
							 continue;
						 }
						//遍历列cell
						for (int tablecell = 0; tablecell <= tableRow.getLastCellNum(); tablecell++) {
							org.apache.poi.ss.usermodel.Cell tabcell=tableRow.getCell(tablecell);
							if (tabcell == null) {
								continue;
							}
							tabcell.setCellType(CellType.STRING);
							String tabcellValue = tabcell.getStringCellValue();
							if("$image".equals(tabcellValue)) {
								tableRow.setHeightInPoints(60);
								if(item.get("image")!=null) {
									BufferedImage bufferImg = null;
									String localImg=item.get("image").toString();
									localImg=fileUpload.getPictureImage( item.get("image").toString()) ;
									Drawing<?> draw = sheet.createDrawingPatriarch();
									ClientAnchor anchor = draw.createAnchor(50, 50, 255, 255, tabcell.getColumnIndex(), tabcell.getRowIndex(), tabcell.getColumnIndex()+1, tabcell.getRowIndex()+1);
									try {
										ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
										bufferImg = ImageIO.read(new URL(localImg));
										ImageIO.write(bufferImg, "PNG", byteArrayOut);
										Picture pic = draw.createPicture(anchor, workbook.addPicture(byteArrayOut.toByteArray(), Workbook.PICTURE_TYPE_PNG));
										pic.setLineStyleColor(1, 1, 1);
										tabcell.setCellValue("");
									}catch(Exception e) {
										e.printStackTrace();
										tabcell.setCellValue("暂无图片");
									}
								 
								}else {
									tabcell.setCellValue("暂无图片");
								}
							}
							if("$sku".equals(tabcellValue)) {
								if(item.get("sku")!=null)tabcell.setCellValue(item.get("sku").toString());
								else tabcell.setCellValue("暂无数据");
							}
							if("$specification".equals(tabcellValue)) {
								if(item.get("specification")!=null)tabcell.setCellValue(item.get("specification").toString());
								else tabcell.setCellValue("暂无数据");
							}
							if("$remark".equals(tabcellValue)) {
								tabcell.setCellStyle(style);
								if(item.get("notice")!=null)tabcell.setCellValue(item.get("notice").toString());
								else tabcell.setCellValue("无");
							}
							if("$skuname".equals(tabcellValue)) {
								tabcell.setCellStyle(style);
								if(item.get("mname")!=null)tabcell.setCellValue(item.get("mname").toString());
								else tabcell.setCellValue("暂无数据");
							}
							if("$num".equals(tabcellValue)) {
								if(item.get("amount")!=null) {
									double value=Double.parseDouble(item.get("amount").toString());
									tabcell.setCellValue(value);
								}
								else tabcell.setCellValue("暂无数据");
							}
							if("$itemprice".equals(tabcellValue)) {
								if(item.get("itemprice")!=null) {
									double value=Double.parseDouble(item.get("itemprice").toString());
									tabcell.setCellValue(value);
								}
								else tabcell.setCellValue("暂无数据");
							}
							if("$orderprice".equals(tabcellValue)) {
								if(item.get("orderprice")!=null) {
									double value=Double.parseDouble(item.get("orderprice").toString());
									tabcell.setCellValue(value);
								}
								else tabcell.setCellValue("暂无数据");
							}
						}
					 startIndex++;
				}
			 }
			 fOut = response.getOutputStream();
			 workbook.write(fOut);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} finally {
			try {
				if(fOut != null) {
					fOut.flush();
					fOut.close();
				}
				if(workbook != null) {
					workbook.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public PurchaseFormEntry getByNumberSku(String shopid, String number, String sku) {
		// TODO Auto-generated method stub
		return this.baseMapper.getByNumberSku(shopid,number,sku);
	}

	@Override
	public boolean updateById(PurchaseFormEntry entity) {
		// TODO Auto-generated method stub
		PurchaseFormEntryHistory history=new PurchaseFormEntryHistory();
		//把entry先存到history 然后更新entry成为新的
		BeanUtil.copyProperties(entity, history);//左边给右边赋值
		boolean result=super.updateById(entity);
		purchaseFormEntryHistoryMapper.insert(history);
		return result;
	}

	@Override
	public IPage<Map<String, Object>> getPurchaseTimeList(PurchaseTimeDTO dto) {
			List<Map<String, Object>> list =null;
			if(dto.getSort()!=null&&
					(dto.getSort().equals("sku")
							||dto.getSort().equals("name")
							||dto.getSort().equals("number")
							||dto.getSort().equals("amount")
							||dto.getSort().equals("inwaretime")
							||dto.getSort().equals("recqty")
							||dto.getSort().equals("inbound")
							||dto.getSort().equals("outbound")
							||dto.getSort().equals("fulfillable")
					)){
				IPage<Map<String, Object>> page = this.baseMapper.getPurchaseTimeList(dto.getPage(), dto);
				if(page!=null&&page.getRecords()!=null){
					list=page.getRecords();
					handlePurchaseTime( dto,list);
					return page;
				}
			}
			if(list==null){
				list=this.baseMapper.getPurchaseTimeList(dto);
				handlePurchaseTime( dto,list);
				return dto.getListPage(list);
			}
        return null;
    }
	public List<Map<String, Object>> handlePurchaseTime(PurchaseTimeDTO dto,List<Map<String, Object>> list) {
		for(Map<String,Object> item:list){
			Date inwaretime = GeneralUtil.getDate(item.get("inwaretime"));
			String mainmid=item.get("mainmid")==null?"":item.get("mainmid").toString();
			String mainimage=item.get("mainimage")==null?"":item.get("mainimage").toString();
			String mainname=item.get("mainname")==null?"":item.get("mainname").toString();
			String mainsku=item.get("mainsku")==null?"":item.get("mainsku").toString();
			String sku=item.get("sku")==null?"":item.get("sku").toString();
			if(inwaretime!=null){
				    item.put("shopid",dto.getShopid());
					Map<String,Object> subparam=new HashMap<String,Object>();
					subparam.put("materialid",item.get("materialid").toString());
					subparam.put("shopid",item.get("shopid").toString());
					subparam.put("inwaretime",GeneralUtil.formatDate(inwaretime));
					Map<String, Object> assmap = assemblyEntryInstockMapper.getAssInstockBySub(subparam);
					if(assmap!=null){
						item.putAll(assmap);
						if(assmap.get("mainsku")!=null&&mainsku!=null){
							String m_mainsku=assmap.get("mainsku").toString();
							if(m_mainsku.equals(mainsku)){
								item.put("mainsku",assmap.get("mainsku"));
							}else{
								Set<String> skuset=new TreeSet<String>();
								skuset.addAll(Arrays.asList(m_mainsku.split(",")));
								skuset.addAll(Arrays.asList(mainsku.split(",")));
								item.put("mainsku", StrUtil.join(",",skuset));
							}
						}

				}
				Map<String,Object> changeamount=iChangeWhFormEntryService.getChangeAmount(subparam);
				Map<String, Object> stocking = stockTakingItemMapper.getStockingItem(subparam);
				Integer chamount=changeamount==null||changeamount.get("changeAmount")==null?0:Integer.parseInt(changeamount.get("changeAmount").toString());
				if(changeamount!=null&&changeamount.size()>0){
					item.putAll(changeamount);
				}
				if(stocking!=null){
					item.putAll(stocking);
					chamount=chamount+(stocking.get("lossamount")==null?0:Integer.parseInt(stocking.get("lossamount").toString()));
				}
				Map<String,Object> param=new HashMap<String,Object>();
				param.put("inwaretime",GeneralUtil.formatDate(inwaretime));
				if(item.get("mainsku")!=null){
					param.put("msku",item.get("mainsku")+","+sku);
					param.put("shipmentids",null);
					param.put("shopid",dto.getShopid());
				}else{
					param.put("msku",sku);
					param.put("shipmentids",null);
					param.put("shopid",dto.getShopid());
				}
				Map<String, Object> map = amazonClientOneFeignManager.getLastShipmentQty(param);
				if(map!=null){
					item.putAll(map);
				}
				Integer subshipqty=0;
				Map<String,Integer> skusub=new HashMap<String,Integer>();
				if(item.get("skusubnumber")!=null){
					String[] skusublist = item.get("skusubnumber").toString().split(",");
					for(String skuqty:skusublist){
						String[] skuq=skuqty.split(":");
						String subsku=skuq.length>1?skuq[0]:"";
						if(StrUtil.isBlank(subsku)){
							continue;
						}
						String qty=skuq.length>1?skuq[1]:"0";
						skusub.put(subsku,Integer.parseInt(qty));
					}
				}
				if(item.get("subnumber")!=null&&item.get("skushipqty")!=null){
					String[] skuqtylist = item.get("skushipqty").toString().split(",");
					for(String skuqty:skuqtylist){
						String[] skuq=skuqty.split(":");
						String shipsku=skuq.length>1?skuq[1]:"";
						if(StrUtil.isBlank(shipsku)){
							continue;
						}
						Integer qty=skuq.length>2?Integer.parseInt(skuq[2]):0;
						Integer skusubqty=skusub.get(shipsku);
						if(skusubqty!=null&&qty!=null){
							subshipqty=subshipqty+qty*skusubqty;
						}else if(shipsku.equals(sku)){
							subshipqty=subshipqty+qty;
						}

					}
					item.put("subshipqty",subshipqty);
					item.put("outqty",subshipqty+chamount);
				}else{
					Integer shipqty =map!=null&&map.get("shipqty")!=null?Integer.parseInt(map.get("shipqty").toString()):0;
					item.put("outqty",shipqty+chamount);
				}
			}

			if(mainmid!=null){
				List<String> materials = Arrays.asList(mainmid.split(","));
				MaterialInventoryVo vo = iInventoryService.findMaterialsInventory(dto.getShopid(), materials,null);
				if(vo!=null){
					item.put("maininbound", vo.getInbound());
					item.put("mainfulfillable", vo.getFulfillable());
					item.put("mainoutbound", vo.getOutbound());
					item.put("mainimage", vo.getImage());
					item.put("mainskuinbound",vo.getSkuinbound());
					item.put("mainskuoutbound",vo.getSkuoutbound());
					item.put("mainskufulfillable",vo.getSkufulfillable());
				}
			}
			item.put("mainimage",mainimage);
			if(item.get("mainimage")!=null){
				item.put("mainimage",fileUpload.getPictureImage(item.get("mainimage").toString()));
			}
			if(item.get("shiptime")!=null){
				Date shiptime = GeneralUtil.getDate(item.get("shiptime"));
				if(shiptime!=null&&inwaretime!=null){
					item.put("days", GeneralUtil.distanceOfDay(shiptime, inwaretime));
				}
			}
		}

		return list;
	}
	 


	@Override
	public void downloadTimeList(SXSSFWorkbook workbook, PurchaseTimeDTO dto) {
		List<Map<String, Object>>   list=this.baseMapper.getPurchaseTimeList(dto);
		handlePurchaseTime( dto,list);
		Map<Integer, String> title = new HashMap<Integer, String>();
		int i=0;
		title.put(i++, "sku");
		title.put(i++, "采购单");
		title.put(i++, "收货数量");
		title.put(i++, "发货");

		title.put(i++, "产品名称");
		title.put(i++, "产品类型");
		title.put(i++, "仓库名称");
		title.put(i++, "下单时间");
		title.put(i++, "审核时间");
		title.put(i++, "最近入库");
		title.put(i++, "最近组装");
		title.put(i++, "最近发货");
		title.put(i++, "时效");
		title.put(i++, "采购数量");
		title.put(i++, "最近主SKU组装");
		title.put(i++, "最近主SKU发货");
		title.put(i++, "组装单位数量");
		title.put(i++, "最近组装消耗");
		title.put(i++, "最近发货消耗");
		title.put(i++, "换货");
		title.put(i++, "库存-待入库");
		title.put(i++, "库存-可用");
		title.put(i++, "库存-待出库");
		title.put(i++, "主SKU名称");
		title.put(i++, "主SKU");
		title.put(i++, "主SKU库存-待入库");
		title.put(i++, "主SKU库存-可用");
		title.put(i++, "主SKU库存-待出库");

		Map<String, String> titlemap = new HashMap<String, String>();
		titlemap.put("sku","sku");
		titlemap.put("产品类型","issfg");
		titlemap.put("仓库名称","wname");
		titlemap.put("产品名称","name");
		titlemap.put("采购单","number");
		titlemap.put("发货","outqty");
		titlemap.put("下单时间","createdate");
		titlemap.put("审核时间","audittime");
		titlemap.put("最近入库","inwaretime");
		titlemap.put("最近组装","asstime");
		titlemap.put("最近发货","shiptime");
		titlemap.put("时效","days");
		titlemap.put("采购数量","amount");
		titlemap.put("收货数量","recqty");
		titlemap.put("最近主SKU组装","assqty");
		titlemap.put("最近主SKU发货","shipqty");
		titlemap.put("组装单位数量","subnumber");
		titlemap.put("最近组装消耗","subqty");
		titlemap.put("最近发货消耗","subshipqty");
		titlemap.put("换货","changeAmount");
		titlemap.put("库存-待入库","inbound");
		titlemap.put("库存-可用","fulfillable");
		titlemap.put("库存-待出库","outbound");
		titlemap.put("主SKU名称","mainname");
		titlemap.put("主SKU","mainsku");
		titlemap.put("主SKU库存-待入库","maininbound");
		titlemap.put("主SKU库存-可用","mainfulfillable");
		titlemap.put("主SKU库存-待出库","mainoutbound");
		Sheet sheet = workbook.createSheet();
		Row row = sheet.createRow(0);
		for(Integer key: title.keySet()){
			Cell cell = row.createCell(key);
			cell.setCellValue(title.get(key));
		}
		for(i = 0; i < list.size(); i++){
			Map<String, Object> rmap = list.get(i);
			Row crow = sheet.createRow(i+1);
			for(Integer key: title.keySet()){
				Cell cell = crow.createCell(key);
				if(rmap.get(titlemap.get(title.get(key)))==null){
					cell.setCellValue("");
				} else {
					String value=rmap.get(titlemap.get(title.get(key))).toString();
					if(title.get(key).equals("产品类型")){
						if(value.equals("0")){
							value="单独成品";
						}else if(value.equals("1")){
							value="组装成品";
						}else{
							value="半成品";
						}
					}
					cell.setCellValue(value);
				}
			}
		}
	}

}
