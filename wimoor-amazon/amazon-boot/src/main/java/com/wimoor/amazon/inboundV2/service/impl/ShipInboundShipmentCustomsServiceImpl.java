package com.wimoor.amazon.inboundV2.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.api.ErpClientOneFeignManager;
import com.wimoor.amazon.auth.mapper.AmazonGroupMapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonGroup;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.inbound.pojo.entity.ShipAddress;
import com.wimoor.amazon.inbound.pojo.entity.ShipAddressTo;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundTrans;
import com.wimoor.amazon.inbound.service.IShipAddressService;
import com.wimoor.amazon.inbound.service.IShipAddressToService;
import com.wimoor.amazon.inbound.service.IShipInboundTransService;
import com.wimoor.amazon.inboundV2.mapper.ShipInboundDestinationAddressMapper;
import com.wimoor.amazon.inboundV2.mapper.ShipInboundShipmentBoxItemMapper;
import com.wimoor.amazon.inboundV2.mapper.ShipInboundShipmentTemplateFileMapper;
import com.wimoor.amazon.inboundV2.pojo.entity.*;
import com.wimoor.amazon.inboundV2.service.*;
import com.wimoor.amazon.inboundV2.mapper.ShipInboundShipmentCustomsMapper;
import com.wimoor.amazon.product.mapper.ProductInfoMapper;
import com.wimoor.amazon.product.pojo.entity.ProductInfo;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.mvc.FileUpload;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.IPictureService;
import com.wimoor.common.service.OSSObjectHandler;
import com.wimoor.common.service.impl.StorageService;
import com.wimoor.common.user.UserInfo;
import lombok.RequiredArgsConstructor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service("shipInboundShipmentCustomsService")
@RequiredArgsConstructor
public class ShipInboundShipmentCustomsServiceImpl extends ServiceImpl<ShipInboundShipmentCustomsMapper, ShipInboundShipmentCustoms> implements IShipInboundShipmentCustomsService {


    final IShipInboundShipmentService shipInboundShipmentService;
    final IShipInboundPlanService shipInboundPlanService;
    final AmazonGroupMapper amazonGroupMapper;
    final IMarketplaceService marketplaceService;
    final IShipAddressToService shipAddressToService;
    final IShipAddressService shipAddressService;
    final IShipInboundBoxService shipInboundBoxService;
    final IShipInboundTransService shipInboundTransService;
    final IShipInboundCaseService shipInboundCaseService;
    final ProductInfoMapper productInfoMapper;
    final ErpClientOneFeignManager erpClientOneFeignManager;
    final ShipInboundShipmentBoxItemMapper shipInboundShipmentBoxItemMapper;
    final FileUpload fileUpload;
    final ShipInboundDestinationAddressMapper shipInboundDestinationAddressMapper;
    final ShipInboundShipmentTemplateFileMapper shipInboundShipmentTemplateFileMapper;
    final IPictureService pictureService;
    final StorageService storageService;
    @Override
    public void downloadShipTemplateFile(ServletOutputStream fOut, UserInfo userinfo, String shipmentid, String templateid) {
        // TODO Auto-generated method stub
        HashMap<String,Object> mainparan=new HashMap<String,Object>();
        List<Map<String,Object>> plist=new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> boxlist=new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> pboxlist=new ArrayList<Map<String,Object>>();
        if(!"#".equals(shipmentid)){
            generateParam(userinfo,shipmentid,mainparan,plist,boxlist,pboxlist);
        }
        List<File> srcFiles=new ArrayList<File>();
                if(StrUtil.isEmpty(templateid)) {
                    return;
                }
                ShipInboundShipmentTemplateFile shipfile = shipInboundShipmentTemplateFileMapper.selectById(templateid);
                com.wimoor.common.pojo.entity.Picture picture = pictureService.getById(shipfile.getFilepath());
                if(picture!=null && picture.getLocation()!=null) {
                    String location = picture.getLocation().replace("wimoor-file/","");
                    storageService.getOSSObject(storageService.getBucketName(), location, new OSSObjectHandler() {
                        @Override
                        public void treatReader(InputStream is, Map<String, Object> param)   {
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            Workbook workbook = null;
                            try {
                                workbook = WorkbookFactory.create(is);
                                CellStyle style=workbook.createCellStyle();
                                style.setAlignment(HorizontalAlignment.LEFT);
                                org.apache.poi.ss.usermodel.Font font = workbook.createFont();
                                font.setFontHeightInPoints((short)10);
                                style.setFont(font);
                                style.setWrapText(true);
                                if(!"#".equals(shipmentid)){
                                    treatShipmentTemplate(workbook,mainparan,plist,boxlist,pboxlist);
                                }
                                workbook.write(fOut);
                                workbook.close();
                            } catch (InvalidFormatException | IOException e) {
                                throw new RuntimeException(e);
                            }
                            // 创建新的Excel工作薄

                        }
                    },null);

                }
    }
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
    private void treatShipmentTemplate(Workbook workbook, Map<String,Object> param,List<Map<String,Object>> plist,List<Map<String,Object>> boxlist,List<Map<String,Object>> pboxlist)  {
        for(int sheetindex=0;sheetindex<workbook.getNumberOfSheets();sheetindex++) {
            int pRowIndex = 0;
            int boxRowIndex = 0;
            int pBoxRowIndex = 0;
            Sheet sheet = workbook.getSheetAt(sheetindex);
            boolean hasPlistRow = false;
            boolean hasBoxlistRow = false;
            boolean hasPBoxlistRow = false;
            Row oldsheetRow=null;

            //第一步：列表填充，将列表行模板复制
            for (int i=0;i<=sheet.getLastRowNum();i++){
                Row sheetRow = sheet.getRow(i);
                boolean mhasPlistRow = false;
                boolean mhasBoxlistRow = false;
                boolean mhasPBoxlistRow = false;
                if (sheetRow != null) {
                    for (int cellnum = 0; cellnum <= sheetRow.getLastCellNum(); cellnum++) {
                        Cell cell = sheetRow.getCell(cellnum);
                        if(cell==null)continue;
                        String cellValue =null;
                        try{
                            cellValue = cell.getStringCellValue().trim();
                        }catch(Exception e){
                            cellValue= String.valueOf(cell.getNumericCellValue());
                        }

                        if(cellValue.contains("{商品列表")){
                            mhasPlistRow=true;
                            break;
                        } else if(cellValue.contains("{箱子列表")){
                            mhasBoxlistRow=true;
                            break;
                        } else  if(cellValue.contains("{箱内商品列表")){
                            mhasPBoxlistRow=true;
                            break;
                        }
                    }
                }
                if(mhasPlistRow==true) {
                    hasPlistRow=true;oldsheetRow=sheetRow;continue;
                }else if(hasPlistRow==true) {
                    if(i<sheet.getLastRowNum()) {
                        if(plist.size()>1) {
                            sheet.shiftRows(i, sheet.getLastRowNum(), plist.size()-1,true,false);
                        }
                    }
                    for(int rowaddnum=0;rowaddnum<plist.size()-1;rowaddnum++) {
                        Row row = sheet.createRow(i+rowaddnum);
                        row.setRowStyle(oldsheetRow.getRowStyle());
                        for (int cellnum = 0; cellnum <= oldsheetRow.getLastCellNum(); cellnum++) {
                            Cell oldcell = oldsheetRow.getCell(cellnum);
                            Cell mycell = row.createCell(cellnum);
                            if(oldcell!=null) {
                                mycell.setCellValue(oldcell.getRichStringCellValue());
                                mycell.setCellStyle(oldcell.getCellStyle());
                            }else {
                                mycell.setCellValue("");
                            }

                        }
                    }
                    i=i+plist.size()-1;
                    hasPlistRow=false;
                }
                if(mhasBoxlistRow==true) {
                    hasBoxlistRow=true;oldsheetRow=sheetRow;continue;
                }else if(hasBoxlistRow==true) {
                    if(i<sheet.getLastRowNum()) {
                        if(boxlist.size()>0) {
                            sheet.shiftRows(i, sheet.getLastRowNum(), boxlist.size()-1,true,false);
                        }
                    }
                    for(int rowaddnum=0;rowaddnum<boxlist.size()-1;rowaddnum++) {
                        Row row = sheet.createRow(i+rowaddnum);
                        row.setRowStyle(oldsheetRow.getRowStyle());
                        for (int cellnum = 0; cellnum <= oldsheetRow.getLastCellNum(); cellnum++) {
                            Cell oldcell = oldsheetRow.getCell(cellnum);
                            Cell mycell = row.createCell(cellnum);
                            if(oldcell!=null) {
                                try{
                                    mycell.setCellValue(oldcell.getRichStringCellValue());
                                    mycell.setCellStyle(oldcell.getCellStyle());
                                }catch(Exception e){
                                    try{
                                        mycell.setCellValue(oldcell.getNumericCellValue());
                                        mycell.setCellStyle(oldcell.getCellStyle());
                                    }catch(Exception e1){
                                        mycell.setCellValue("");
                                        e1.printStackTrace();
                                    }
                                }
                            } else {
                                mycell.setCellValue("");
                            }
                        }
                    }
                    i=i+boxlist.size()-1;
                    hasBoxlistRow=false;
                }
                if(mhasPBoxlistRow==true) {
                    hasPBoxlistRow=true;oldsheetRow=sheetRow;continue;
                }else if(hasPBoxlistRow==true) {
                    if(i<sheet.getLastRowNum()) {
                        if(pboxlist.size()>1) {
                            sheet.shiftRows(i, sheet.getLastRowNum(), pboxlist.size()-1,true,false);
                        }
                    }
                    for(int rowaddnum=0;rowaddnum<pboxlist.size()-1;rowaddnum++) {
                        Row row = sheet.createRow(i+rowaddnum);
                        row.setRowStyle(oldsheetRow.getRowStyle());
                        for (int cellnum = 0; cellnum <= oldsheetRow.getLastCellNum(); cellnum++) {
                            Cell oldcell = oldsheetRow.getCell(cellnum);
                            Cell mycell = row.createCell(cellnum);
                            if(oldcell!=null) {
                                try{
                                    mycell.setCellValue(oldcell.getRichStringCellValue());
                                    mycell.setCellStyle(oldcell.getCellStyle());
                                }catch(Exception e){
                                    try{
                                        mycell.setCellValue(oldcell.getNumericCellValue());
                                        mycell.setCellStyle(oldcell.getCellStyle());
                                    }catch(Exception e1){
                                        mycell.setCellValue("");
                                        e1.printStackTrace();
                                    }
                                }
                            } else {
                                mycell.setCellValue("");
                            }
                        }
                    }
                    i=i+pboxlist.size()-1;
                    hasPBoxlistRow=false;
                }
            }

            //第二步：字段填充
            for (int i=0;i<=sheet.getLastRowNum();i++){
                hasPlistRow = false;
                hasBoxlistRow = false;
                hasPBoxlistRow = false;
                Row sheetRow = sheet.getRow(i);
                if (sheetRow == null) {
                    continue;
                }
                for (int cellnum = 0; cellnum <= sheetRow.getLastCellNum(); cellnum++) {
                    Cell cell = sheetRow.getCell(cellnum);
                    if (cell == null) {
                        continue;
                    }
                    String cellValue =null;
                    try{
                        cellValue = cell.getStringCellValue().trim();
                    }catch(Exception e){
                        cellValue= String.valueOf(cell.getNumericCellValue());
                    }
                    if(StrUtil.isNotEmpty(cellValue)) {
                        cell.setCellType(CellType.STRING);
                        if(param.get(cellValue)!=null) {
                            cell.setCellValue(param.get(cellValue).toString());
                        }else {
                            if(cellValue.contains("{商品列表")){
                                Map<String, Object> pmap = plist.get(pRowIndex);
                                if(setCellValue(workbook,sheet,cell,pmap,cellValue)) {
                                    hasPlistRow=true;
                                }
                            }
                            else if(cellValue.contains("{箱子列表")){
                                Map<String, Object> bmap = boxlist.get(boxRowIndex);
                                if(setCellValue(workbook,sheet,cell,bmap,cellValue)) {
                                    hasBoxlistRow=true;
                                }
                            } else  if(cellValue.contains("{箱内商品列表")){
                                Map<String, Object> pbmap =pboxlist.get(pBoxRowIndex);
                                if(setCellValue(workbook,sheet,cell,pbmap,cellValue)) {
                                    hasPBoxlistRow=true;
                                }
                            } else if(cellValue.contains("{")) {
                                setCellValue(workbook,sheet,cell,param,cellValue);
                            }

                        }

                    }

                }
                if(hasPlistRow) {
                    if(pRowIndex+1<plist.size()) {
                        pRowIndex++;
                    }else {
                        pRowIndex=0;
                    }
                }
                if(hasBoxlistRow) {
                    if(boxRowIndex+1<boxlist.size()) {
                        boxRowIndex++;
                    }else {
                        boxRowIndex=0;
                    }
                }
                if(hasPBoxlistRow) {
                    if(pBoxRowIndex+1<pboxlist.size()) {
                        pBoxRowIndex++;
                    }else {
                        pBoxRowIndex=0;
                    }
                }
            }
        }
    }

    private List<String> splitValue(String value) {
        List<String> ls=new ArrayList<String>();
        Pattern pattern = Pattern.compile("\\{([^}]*)\\}");
        Matcher matcher = pattern.matcher(value);
        while(matcher.find()) {
            ls.add(matcher.group());
        }
        return ls;
    }
    boolean setCellValue(Workbook workbook,Sheet sheet,Cell cell,Map<String, Object> map,String cellValue){
        Object value =map.get(cellValue);
        if(value!=null) {
            if(cellValue.contains("图片")) {
                Row imgrow = cell.getRow();
                float nums=50;
                imgrow.setHeightInPoints(nums);
                String image =value!=null?fileUpload.getPictureImage(value):"";
                Drawing<?> draw = sheet.createDrawingPatriarch();
                ClientAnchor anchor = draw.createAnchor(0, 0, 0, 0, cell.getColumnIndex(), cell.getRowIndex(), cell.getColumnIndex()+1, cell.getRowIndex()+1);
                ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
                BufferedImage bufferImg;
                try {
                    bufferImg = ImageIO.read(new URL(image));
                    ImageIO.write(bufferImg, "JPEG", byteArrayOut);
                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                org.apache.poi.ss.usermodel.Picture picexcel = draw.createPicture(anchor, workbook.addPicture(byteArrayOut.toByteArray(), Workbook.PICTURE_TYPE_JPEG));
                picexcel.setLineStyleColor(1, 1, 1);
                cell.setCellValue("");
            }else {
                cell.setCellValue(value.toString());
            }
            return true;
        }else {
            List<String> cellvalueArr = splitValue(cellValue);
            boolean hasdata=false;
            for(int k=0;k<cellvalueArr.size();k++) {
                String paramkey = cellvalueArr.get(k);
                if(StrUtil.isNotEmpty(paramkey)) {
                    if(map.get(paramkey)!=null) {
                        String paramvalue = map.get(paramkey).toString();
                        cellValue=cellValue.replace( paramkey , paramvalue);
                        cell.setCellValue(cellValue);
                        hasdata=true;
                    }else {
                        String paramvalue = "";
                        cellValue=cellValue.replace( paramkey , paramvalue);
                        cell.setCellValue(cellValue);
                    }
                }
            }
            return hasdata;
        }

    }
    private void generateParam(UserInfo user, String shipmentid, HashMap<String, Object> mainparam,
                               List<Map<String, Object>> plist, List<Map<String, Object>> boxlist, List<Map<String, Object>> pboxlist) {
        // TODO Auto-generated method stub
        ShipInboundShipment shipment = shipInboundShipmentService.getById(shipmentid);
        ShipInboundPlan inboundplan = shipInboundPlanService.getById(shipment.getFormid());
        AmazonGroup amzgroup = amazonGroupMapper.selectById(inboundplan.getGroupid());
        Marketplace marketplace = marketplaceService.findMapByMarketplaceId().get(inboundplan.getMarketplaceid());
        ShipInboundDestinationAddress toaddress = shipInboundDestinationAddressMapper.selectById(shipment.getDestination());
        ShipAddress fromaddress = shipAddressService.getById(inboundplan.getSourceAddress());
        List<Map<String, Object>> listmap = shipInboundShipmentService.findInboundItemByShipmentId(shipmentid);
        List<Map<String, Object>> boxobjlist = shipInboundBoxService.findShipInboundBox(shipmentid);
        List<ShipInboundShipmentCustoms> customsList = this.baseMapper.listByShipmentid(shipmentid);
        LambdaQueryWrapper<ShipInboundTrans> query = new LambdaQueryWrapper<ShipInboundTrans>();
        query.eq(ShipInboundTrans::getShipmentid, shipmentid);
        List<ShipInboundTrans> tranlist=shipInboundTransService.list(query);
        ShipInboundTrans trans = null;
        Map<String, Object> transdetal=null;
        if(customsList==null||customsList.size()==0){
            throw new BizException("海关信息未保存！无法下载！");
        }
        if(tranlist!=null&&tranlist.size()>0) {
            trans = tranlist.get(0);
            transdetal = erpClientOneFeignManager.getShipTransChannelDetial(trans.getChannel());
        }
        List<String> skulist=new ArrayList<String>();
        for(Map<String, Object> item:listmap) {
            String sku=(String) item.get("msku");
            if(sku!=null&&!skulist.contains(sku)) {
                skulist.add(sku);
            }
        }
        Map<String, Map<String, Object>> dimList = erpClientOneFeignManager.getMaterialBySKUAction(inboundplan.getShopid(), skulist);
        mainparam.put("{货件ID}", shipment.getShipmentConfirmationId());
        mainparam.put("{货件状态}", shipment.getStatus());
        mainparam.put("{发货日期}", shipment.getShipedDate());
        mainparam.put("{店铺名称}", amzgroup.getName());
        mainparam.put("{站点名称}", marketplace.getName());
        mainparam.put("{国家编码}", marketplace.getMarket());
        mainparam.put("{仓库编码}", shipment.getDestination());
        mainparam.put("{发货箱数}", boxobjlist.size());
        mainparam.put("{货件ReferenceID}", shipment.getReferenceid());
        mainparam.put("{币种编码}", marketplace.getCurrency());
        mainparam.put("{币种符号}", GeneralUtil.formatCurrency(marketplace.getCurrency()));
        Map<String, Object> fromaddressMap = getFromeAddress(fromaddress);
        if(fromaddressMap!=null) {
            mainparam.putAll(fromaddressMap);
        }

        Map<String, Object> toaddressMap = getToAddress(toaddress);
        if(toaddressMap!=null) {
            mainparam.putAll(toaddressMap);
        }
        int indexnum=1;
        for(Map<String, Object> item:listmap) {
            Map<String, Object> pmap = getProductInfo(item,inboundplan,transdetal,dimList,customsList,"商品列表");
            pmap.put("{商品列表.序号}", indexnum++);
            plist.add(pmap);
        }
        for(Map<String, Object> item:plist) {
            if(mainparam.get("{货件带电}")==null) {
                if("是".equals(item.get("{商品列表.是否带电}"))) {
                    mainparam.put("{货件带电}","是");
                }else {
                    mainparam.put("{货件带电}", "否");
                }
            }
            if(mainparam.get("{货件危险}")==null) {
                if("是".equals(item.get("{商品列表.是否危险}"))) {
                    mainparam.put("{货件危险}","是");
                }else {
                    mainparam.put("{货件危险}", "否");
                }
            }
        }
        if(boxobjlist!=null){
            indexnum=1;
            int indexpboxnum = 1;
            for(Map<String, Object> box:boxobjlist) {
                Map<String, Object> result = getBoxInfo(box,"箱子列表");
                result.put("{箱子列表.序号}", indexnum++);
                boxlist.add(result);
                List<ShipInboundShipmentBoxItem> caseinfolist = shipInboundShipmentBoxItemMapper.findByShipment(shipmentid);
                if(caseinfolist!=null && caseinfolist.size()>0) {
                    for(ShipInboundShipmentBoxItem caseinfo:caseinfolist) {
                        if(caseinfo.getQuantity()<=0||!caseinfo.getBoxid().equals(box.get("boxnum").toString())) {
                            continue;
                        }
                        for(Map<String, Object> pitem:listmap) {
                            if(pitem.get("SellerSKU")!=null&&caseinfo.getSku().equals(pitem.get("SellerSKU").toString())) {
                                result=getProductBoxItem(box,caseinfo,pitem,inboundplan,transdetal,dimList,customsList);
                                result.put("{箱内商品列表.序号}", indexpboxnum++);
                                pboxlist.add(result);
                            }
                        }
                    }
                }


            }
        }

    }

    private Map<String, Object> getProductBoxItem(Map<String, Object> box, ShipInboundShipmentBoxItem caseinfo,
                                                  Map<String, Object> pitem, ShipInboundPlan inboundplan, Map<String,Object> transdetal,
                                                  Map<String, Map<String, Object>> dimlist,List<ShipInboundShipmentCustoms> customsList) {
        // TODO Auto-generated method stub
        Map<String,Object> result=new HashMap<String,Object>();
        Map<String, Object> pmap = getProductInfo(pitem,  inboundplan,   transdetal,dimlist,customsList,"箱内商品列表");
        if(pmap!=null) {
            result.putAll(pmap);
        }
        Map<String, Object> boxmap = getBoxInfo(box,"箱内商品列表");
        if(boxmap!=null) {
            result.putAll(boxmap);
        }
        result.put("{箱内商品列表.箱内数量}", caseinfo.getQuantity());
        return result;
    }


    private Map<String,Object> getFromeAddress(ShipAddress fromaddress){
        if(fromaddress==null)return null;
        Map<String, Object> result=new HashMap<String,Object>();
        result.put("{发货地址.城市}",fromaddress.getCity());
        result.put("{发货地址.国家}",fromaddress.getCountrycode());
        result.put("{发货地址.电话}",fromaddress.getPhone());
        result.put("{发货地址.邮编}",fromaddress.getPostalcode());
        result.put("{发货地址.省份编码}",fromaddress.getStateorprovincecode());
        result.put("{发货地址.地区}",fromaddress.getDistrictorcounty());
        result.put("{发货地址.详情1}",fromaddress.getAddressline1());
        result.put("{发货地址.详情2}",fromaddress.getAddressline2());
        result.put("{发货地址.名称}",fromaddress.getName());
        return result;
    }

    private Map<String,Object> getToAddress(ShipInboundDestinationAddress toaddress){
        if(toaddress==null)return null;
        Map<String, Object> result=new HashMap<String,Object>();
        result.put("{收货地址.城市}",toaddress.getCity());
        result.put("{收货地址.国家}",toaddress.getCountryCode());
        result.put("{收货地址.电话}",toaddress.getPhoneNumber());
        result.put("{收货地址.邮编}",toaddress.getPostalCode());
        result.put("{收货地址.省份编码}",toaddress.getStateOrProvinceCode());
        result.put("{收货地址.地区}",toaddress.getArea());
        result.put("{收货地址.详情1}",toaddress.getAddressLine1());
        result.put("{收货地址.详情2}",toaddress.getAddressLine2());
        result.put("{收货地址.名称}",toaddress.getName());
        return result;
    }

    private Map<String,Object> getBoxInfo(Map<String,Object> box,String prefix){
        if(box==null)return null;
        Map<String, Object> result=new HashMap<String,Object>();
        result.put("{"+prefix+".箱号}",box.get("boxnum"));
        result.put("{"+prefix+".装箱数量}",box.get("qty"));
        result.put("{"+prefix+".箱子重量}",box.get("weight"));
        result.put("{"+prefix+".箱子宽度}",box.get("width"));
        result.put("{"+prefix+".箱子高度}",box.get("height"));
        result.put("{"+prefix+".箱子长度}",box.get("length"));
        result.put("{"+prefix+".箱子体积}", new BigDecimal(box.get("width").toString()).multiply(new BigDecimal(box.get("height").toString())).multiply(new BigDecimal(box.get("length").toString())));
        return result;
    }

//    String getBoxNumber(Object boxnum){
//        if(boxnum==null)return null;
//        int num = Integer.parseInt(boxnum.toString());
//        return String.format("U%06d", num);
//    }

    private Map<String, Object> getProductInfo(Map<String, Object> item,ShipInboundPlan inboundplan, Map<String,Object> transdetal
            ,Map<String, Map<String, Object>> dimmap,List<ShipInboundShipmentCustoms> customsList,String prefix){
        Map<String, Object> pmap=new HashMap<String,Object>();
        String sku=item.get("SellerSKU").toString();
        String msku=item.get("msku").toString();
        Integer qty= Integer.parseInt(item.get("QuantityShipped").toString());
        pmap.put("{"+prefix+".sku}", item.get("SellerSKU"));
        pmap.put("{"+prefix+".发货数量}",qty);
        pmap.put("{"+prefix+".图片}", item.get("image"));
        String materialid=item.get("mid").toString();
        Map<String, Object> materialInfo=null;
        Result<Map<String, Object>> materialResult = erpClientOneFeignManager.findMaterialMapBySku(Arrays.asList(item.get("SellerSKU").toString()));
        if(materialResult!=null && materialResult.getData()!=null){
            materialInfo = materialResult.getData();
        }
        if(materialInfo==null) {return pmap;}
        ShipInboundShipmentCustoms customs=null;
        if(customsList!=null&&customsList.size()>0){
            for (ShipInboundShipmentCustoms items:customsList){
                if(sku.equals(items.getSku())){
                    customs=items;
                }
            }
        }
        Map<String, Object> dimObject =null;
        if(dimmap!=null){
            if(dimmap.get(msku)!=null){
                dimObject = dimmap.get(msku);
            }else{
                throw new BizException("无法本地产品信息，请先维护本地产品信息后下载海关信息");
            }
        }
        if(dimObject!=null && dimObject.get("catename")!=null) {
            pmap.put("{"+prefix+".分类名称}", dimObject.get("catename"));
        }else {
            pmap.put("{"+prefix+".分类名称}","");
        }
        if(dimObject!=null && dimObject.get("boxnum")!=null) {
            pmap.put("{"+prefix+".单箱数量}",dimObject.get("boxnum"));
        }else {
            pmap.put("{"+prefix+".单箱数量}","");
        }

        if(dimObject!=null && dimObject.get("suppliername")!=null) {
            pmap.put("{"+prefix+".供应商名称}",dimObject.get("suppliername"));
            pmap.put("{"+prefix+".供应商地址}",dimObject.get("address"));
            pmap.put("{"+prefix+".供应商电话}",dimObject.get("phonenumber"));
        }else {
            pmap.put("{"+prefix+".供应商名称}","");
            pmap.put("{"+prefix+".供应商地址}","");
            pmap.put("{"+prefix+".供应商电话}","");
        }


        List<ProductInfo> infolist = productInfoMapper.selectBySku(item.get("SellerSKU").toString(), inboundplan.getMarketplaceid(), inboundplan.getAmazonauthid());
        ProductInfo info = infolist.get(0);
        if(customs!=null && StrUtil.isNotEmpty(customs.getCname())) {
            pmap.put("{"+prefix+".中文名}", customs.getCname());
        }else {
            pmap.put("{"+prefix+".中文名}", dimObject.get("name"));
        }
        if(customs!=null && StrUtil.isNotEmpty(customs.getEname())) {
            pmap.put("{"+prefix+".英文名}", customs.getEname());
        }else {
            pmap.put("{"+prefix+".英文名}", info.getName());
        }
        if(customs!=null) {
            if(customs.getPrice()!=null) {
                pmap.put("{"+prefix+".申报单价}",customs.getPrice());
                pmap.put("{"+prefix+".总申报价}",customs.getPrice().multiply(new BigDecimal(qty)));
            }
            pmap.put("{"+prefix+".产品材质}",customs.getMaterial());
            pmap.put("{"+prefix+".产品材质(中文)}",customs.getMaterialcn());
            pmap.put("{"+prefix+".产品型号}",dimObject.get("specification"));
            pmap.put("{"+prefix+".产品品牌}",dimObject.get("brandname"));
            pmap.put("{"+prefix+".产品用途}",customs.getApplication());
            pmap.put("{"+prefix+".海关编码}",customs.getCode());
//            pmap.put("{"+prefix+".是否带电}",booleanToCN(customs.getIselectricity()));
//            pmap.put("{"+prefix+".是否危险}",booleanToCN(customs.getIsdanger()));
        }
        if(customs!=null && customs.getPrice()!=null&&pmap.get("{"+prefix+".申报单价}")==null) {
            pmap.put("{"+prefix+".申报单价}",customs.getPrice());
            pmap.put("{"+prefix+".总申报价}",customs.getPrice().multiply(new BigDecimal(qty)));
        }

        pmap.put("{"+prefix+".售价}",info.getPrice());
        pmap.put("{"+prefix+".单位重量}",dimObject.get("weight"));
        if(dimObject.get("weight")!=null){
            pmap.put("{"+prefix+".总重量}",new BigDecimal(dimObject.get("weight").toString()).multiply(new BigDecimal(item.get("QuantityShipped").toString())));
        }
        pmap.put("{"+prefix+".长度}",dimObject.get("length"));
        pmap.put("{"+prefix+".宽度}",dimObject.get("width"));
        pmap.put("{"+prefix+".高度}",dimObject.get("height"));
        pmap.put("{"+prefix+".asin}",info.getAsin());
        pmap.put("{"+prefix+".fnsku}",info.getFnsku());
        Marketplace marketplace = marketplaceService.findMapByMarketplaceId().get(info.getMarketplaceid());
        pmap.put("{"+prefix+".销售链接}", "https://www."+marketplace.getPointName()+"/dp/"+info.getAsin()+"?th=1&psc=1");
        if(dimObject.get("height")!=null&&dimObject.get("width")!=null&&dimObject.get("length")!=null) {
            if(transdetal!=null&&transdetal.get("drate")!=null) {
                 BigDecimal cbm = new BigDecimal(dimObject.get("height").toString()).multiply(new BigDecimal(dimObject.get("width").toString())).multiply(new BigDecimal(dimObject.get("length").toString()));
                cbm=cbm.divide(new BigDecimal(transdetal.get("drate").toString()), 4, BigDecimal.ROUND_HALF_UP);
                pmap.put("{"+prefix+".单位材积}",String.format("%.2f", cbm));
                pmap.put("{"+prefix+".总材积}",String.format("%.2f", cbm.multiply(new BigDecimal(qty))));
            }else {
                BigDecimal cbm = new BigDecimal(dimObject.get("height").toString()).multiply(new BigDecimal(dimObject.get("width").toString())).multiply(new BigDecimal(dimObject.get("length").toString()));
                cbm=cbm.divide(new BigDecimal("5000"), 4, BigDecimal.ROUND_HALF_UP);
                pmap.put("{"+prefix+".单位材积}",String.format("%.2f", cbm));
                pmap.put("{"+prefix+".总材积}",String.format("%.2f", cbm.multiply(new BigDecimal(qty))));
            }
        }
        return pmap;

    }


}
