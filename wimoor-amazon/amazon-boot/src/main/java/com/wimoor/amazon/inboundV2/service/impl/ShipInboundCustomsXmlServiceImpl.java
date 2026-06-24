package com.wimoor.amazon.inboundV2.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.auth.pojo.entity.AmazonGroup;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonGroupService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.inboundV2.XmlPojo.OrderItem;
import com.wimoor.amazon.inboundV2.mapper.ShipInboundCustomsXmlMapper;
import com.wimoor.amazon.inboundV2.mapper.ShipInboundShipmentBoxMapper;
import com.wimoor.amazon.inboundV2.pojo.dto.ShipCustomXmlDTO;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundCustomsXml;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundPlan;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundShipment;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundShipmentBox;
import com.wimoor.amazon.inboundV2.service.IShipInboundCustomsXmlService;
import com.wimoor.amazon.inboundV2.service.IShipInboundPlanService;
import com.wimoor.amazon.inboundV2.service.IShipInboundShipmentService;
import com.wimoor.amazon.util.ExcelRowCopyUtil;
import com.wimoor.common.user.UserInfo;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @author liufei
* @description 针对表【t_erp_ship_v2_inboundshipment_customs_xml】的数据库操作Service实现
* @createDate 2025-12-03 13:35:14
*/
@Service
@RequiredArgsConstructor
public class ShipInboundCustomsXmlServiceImpl extends ServiceImpl<ShipInboundCustomsXmlMapper, ShipInboundCustomsXml>
    implements IShipInboundCustomsXmlService {

    final IShipInboundShipmentService shipInboundShipmentService;
    final IShipInboundPlanService shipInboundPlanService;
    final IMarketplaceService marketplaceService;
    final ShipInboundShipmentBoxMapper shipInboundShipmentBoxMapper;
    final IAmazonGroupService amazonGroupService;
    @Override
    public String getNextSequenceNumber(ShipInboundCustomsXml xmlInfo) {
        return this.baseMapper.getNextSequenceNumber(xmlInfo);
    }

    @Override
    public IPage<Map<String, Object>> findList(ShipCustomXmlDTO dto) {
        return this.baseMapper.findList(dto.getPage(), dto);
    }

    @Override
    public Map<String, Object> summaryList(ShipCustomXmlDTO dto) {
        return this.baseMapper.findSummary( dto);
    }

    @Override
    public void setExcelBook(SXSSFWorkbook workbook, ShipCustomXmlDTO dto) {
        List<Map<String, Object>> list = this.baseMapper.findList(dto);
        Sheet sheet=workbook.createSheet();
        Row row=sheet.createRow(0);
        row.createCell(0).setCellValue("申报状态");
        row.createCell(1).setCellValue("操作类型");
        row.createCell(2).setCellValue("申报时间");
        row.createCell(3).setCellValue("申报类型");
        row.createCell(4).setCellValue("申报金额");
        row.createCell(5).setCellValue("申报数量");
        row.createCell(6).setCellValue("申报净重");
        row.createCell(7).setCellValue("申报毛重");
        row.createCell(8).setCellValue("申报货件");
        row.createCell(9).setCellValue("装箱重量");
        row.createCell(10).setCellValue("箱数");
        row.createCell(11).setCellValue("物流重量");
        row.createCell(12).setCellValue("操作时间");
        for(int i=0;i<list.size();i++){
            row=sheet.createRow(i+1);
            Map<String,Object> item=list.get(i);
            if(item.get("app_status")!=null){
                if(item.get("app_status").equals("1")){
                    row.createCell(0).setCellValue("暂存");
                }else if(item.get("app_status").equals("2")){
                    row.createCell(0).setCellValue("申报");
                }else{
                    row.createCell(0).setCellValue("处理中");
                }
            }
            if(item.get("app_type")!=null){
                if(item.get("app_type").equals("1")){
                    row.createCell(1).setCellValue("新增");
                }else if(item.get("app_type").equals("2")){
                    row.createCell(1).setCellValue("变更");
                }else{
                    row.createCell(1).setCellValue("删除");
                }
            }
            if(item.get("app_time")!=null){
                row.createCell(2).setCellValue(item.get("app_time").toString());
            }
            if(item.get("xml_type")!=null){
                if(item.get("xml_type").equals("CEB303")){
                    row.createCell(3).setCellValue("电商订单");
                }else if(item.get("xml_type").equals("CEB603")){
                    row.createCell(3).setCellValue("电商出口清单");
                }else{
                    row.createCell(3).setCellValue("出口报关单");
                }
            }
            if(item.get("total_price")!=null){
                row.createCell(4).setCellValue(parseDoubleSafely(item.get("total_price").toString()));
            }
            if(item.get("total_quantity")!=null){
                row.createCell(5).setCellValue(parseDoubleSafely(item.get("total_quantity").toString()));
            }
            if(item.get("net_weight")!=null){
                row.createCell(6).setCellValue(parseDoubleSafely(item.get("net_weight").toString()));
            }
            if(item.get("gross_weight")!=null){
                row.createCell(7).setCellValue(parseDoubleSafely(item.get("gross_weight").toString()));
            }
            if(item.get("number")!=null){
                row.createCell(8).setCellValue(item.get("number").toString());
            }
            if(item.get("boxweight")!=null){
                row.createCell(9).setCellValue(parseDoubleSafely(item.get("boxweight").toString()));
            }
            if(item.get("boxnum")!=null){
                row.createCell(10).setCellValue(parseDoubleSafely(item.get("boxnum").toString()));
            }
            if(item.get("transweight")!=null){
                row.createCell(11).setCellValue(parseDoubleSafely(item.get("transweight").toString()));
            }
            if(item.get("opttime")!=null){
                row.createCell(12).setCellValue(item.get("opttime").toString());
            }
        }

    }

    @Override
    public void setCustomsExcelBook(Sheet sheet, ShipCustomXmlDTO dto) {
        // 获取订单数据
        String[] numbers = dto.getNumber().split(",");
        String shipmentid = numbers[0];
        ShipInboundShipment shipment = shipInboundShipmentService.lambdaQuery().eq(ShipInboundShipment::getShipmentConfirmationId, shipmentid).one();
        Long totalboxnum=0L;
        BigDecimal totalboxweight=new BigDecimal(0);
        BigDecimal totalweightall=new BigDecimal(0);
        for (String number : numbers) {
            ShipInboundShipment item = shipInboundShipmentService.lambdaQuery().eq(ShipInboundShipment::getShipmentConfirmationId, number).one();
            List<ShipInboundShipmentBox> boxlist = shipInboundShipmentBoxMapper.selectList(new LambdaQueryWrapper<ShipInboundShipmentBox>().eq(ShipInboundShipmentBox::getShipmentid, item.getShipmentid()));
            if(boxlist!=null){
                for(ShipInboundShipmentBox box:boxlist){
                    totalboxweight=totalboxweight.add(box.getWeight());
                    totalboxnum++;
                }
            }
        }
        if(dto.getOrderData()!=null && dto.getOrderData().getOrderItems()!=null){
            for( OrderItem item: dto.getOrderData().getOrderItems()){
                if(item.getItemInfoExt()!=null && item.getItemInfoExt().get("weight")!=null){
                    if(item.getItemInfoExt().get("weight").equals("")){
                        continue;
                    }
                    totalweightall=totalweightall.add(new BigDecimal(item.getItemInfoExt().get("weight").toString()));
                }
            }
        }
        if(shipment!=null){
            String planid = shipment.getFormid();
            ShipInboundPlan plan = shipInboundPlanService.getById(planid);
            if(plan!=null){
                String marketplaceid = plan.getMarketplaceid();
                dto.setMarketplaceid(marketplaceid);
            }
        }
        if (dto.getOrderData() != null) {
            // 遍历所有行，替换占位符
            for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row != null) {
                    for (int colIndex = 0; colIndex < row.getLastCellNum(); colIndex++) {
                        org.apache.poi.ss.usermodel.Cell cell = row.getCell(colIndex);
                        if (cell != null && cell.getCellType() == org.apache.poi.ss.usermodel.CellType.STRING) {
                            String cellValue = cell.getStringCellValue();
                            if (cellValue != null && cellValue.contains("{")) {
                                if (cellValue.contains("{totalbox}")) {
                                    cell.setCellValue(totalboxnum.doubleValue());
                                } else if (cellValue.contains("{totalboxweight}")) {
                                    cell.setCellValue(totalboxweight.doubleValue());
                                } else if (cellValue.contains("{totalweight}")) {
                                    cell.setCellValue(totalweightall.doubleValue());
                                } else {
                                    // 替换占位符
                                    String newValue = replacePlaceholders(cellValue, dto,totalboxnum,totalboxweight,totalweightall);
                                    if (!newValue.equals(cellValue)) {
                                        cell.setCellValue(newValue);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 填充商品列表数据
            try {
                fillOrderItems(sheet, dto.getOrderData().getOrderItems());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 替换单元格中的占位符
     */
    private String replacePlaceholders(String cellValue, ShipCustomXmlDTO dto,Long totalboxnum,BigDecimal totalboxweight,BigDecimal totalweightall) {
        if (dto.getOrderData() == null) {
            return cellValue;
        }
        String country="";
        if(dto.getMarketplaceid()!=null){
            Marketplace marketplace = marketplaceService.getById(dto.getMarketplaceid());
            if(marketplace!=null){
                country=marketplace.getName();
            }
        }

        com.wimoor.amazon.inboundV2.XmlPojo.OrderData orderData = dto.getOrderData();
        String newValue = cellValue;

        // 替换企业信息相关占位符
        if(orderData.getFormInfoExt()!=null){
            //newValue= newValue.replace("{totalweight}", orderData.getFormInfoExt().get("totalweight") != null ? orderData.getFormInfoExt().get("totalweight") : "");
            newValue = newValue.replace("{code}", orderData.getFormInfoExt().get("code") != null ? orderData.getFormInfoExt().get("code") : "");
            newValue = newValue.replace("{exitcustoms}", orderData.getFormInfoExt().get("exitcustoms") != null ? orderData.getFormInfoExt().get("exitcustoms") : ""); // 出境关别
            newValue = newValue.replace("{shiptype}",orderData.getFormInfoExt().get("shiptype") != null ? orderData.getFormInfoExt().get("shiptype") : "" ); // 运输方式
            newValue = newValue.replace("{number}", orderData.getFormInfoExt().get("number") != null ? orderData.getFormInfoExt().get("number") : "");
            newValue = newValue.replace("{portnumber}", orderData.getFormInfoExt().get("portnumber") != null ? orderData.getFormInfoExt().get("portnumber") : "");
        }else{
            newValue = newValue.replace("{code}","");
            newValue = newValue.replace("{exitcustoms}","");
            newValue = newValue.replace("{shiptype}","");
            newValue = newValue.replace("{number}","");
            newValue= newValue.replace("{portnumber}","");
            //newValue= newValue.replace("{totalweight}",  "");
        }
        newValue= newValue.replace("{companyname}", orderData.getEbcName() != null ? orderData.getEbcName() : "");
        newValue = newValue.replace("{shipfee}", orderData.getFreight() != null ? orderData.getFreight().toString() : "0");
        newValue = newValue.replace("{premiumfee}", "0"); // 保费
        newValue = newValue.replace("{otherfee}", "0"); // 杂费
        newValue = newValue.replace("{country}", country); // 默认国家
        newValue = newValue.replace("{exitport}", ""); // 离境口岸

        return newValue;
    }

    /**
     * 填充商品列表数据
     */
    private void fillOrderItems(Sheet sheet, List<OrderItem> orderItems) throws IOException {
        if (orderItems == null || orderItems.isEmpty()) {
            return;
        }

        // 查找商品列表起始位置（包含{list.model}的行）
        int startRow = -1;
        int modelCol = -1;
        int materialCol = -1;
        int weightCol = -1;
        int qtyCol = -1;
        int priceCol = -1;

        for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row != null) {
                for (int colIndex = 0; colIndex < row.getLastCellNum(); colIndex++) {
                    org.apache.poi.ss.usermodel.Cell cell = row.getCell(colIndex);
                    if (cell != null && cell.getCellType() == org.apache.poi.ss.usermodel.CellType.STRING) {
                        String cellValue = cell.getStringCellValue();
                        if (cellValue.contains("{list.model}")) {
                            startRow = rowIndex;
                            modelCol = colIndex;
                        } else if (cellValue.contains("{list.materialcn}")) {
                            materialCol = colIndex;
                        } else if (cellValue.contains("{list.weight}")) {
                            weightCol = colIndex;
                        } else if (cellValue.contains("{list.qty}")) {
                            qtyCol = colIndex;
                        } else if (cellValue.contains("{list.price}") || cellValue.contains("{list.itemprice}")) {
                            priceCol = colIndex;
                        }
                    }
                }
            }
        }

        // 如果找到了商品列表起始位置，填充数据  excel的20行开始填充数据 并且根据orderItems的数量先复制模板行 20 21 22行这3行是商品列表的模板行  每个商品项占3行
        if (startRow != -1) {
            int itemCount = orderItems.size();
            int templateRows = 3; // 每个商品项占3行
            
            // 复制模板行，为每个商品项创建3行 并且保留模板行的样式style
            for (int i = 0; i < itemCount-1; i++) {
                int destStartRow = startRow + templateRows + i * templateRows;
                ExcelRowCopyUtil.copyRowRange(sheet, 19, 21, 22+i*3);
            }
            
            // 填充每个商品项的数据
            for (int i = 0; i < itemCount; i++) {
                    int rowIndex = startRow +  i * 3;
                    Row row1 = sheet.getRow(rowIndex);
                    if (row1 != null) {
                        fillItemRow(row1, orderItems.get(i), i + 1);
                    }
                    Row row2 = sheet.getRow(rowIndex+1);
                    if (row2 != null) {
                        fillItemRow(row2, orderItems.get(i), i + 1);
                    }
                    Row row3 = sheet.getRow(rowIndex+2);
                    if (row3 != null) {
                        fillItemRow(row3, orderItems.get(i), i + 1);
                    }
            }
            //开始行startRow,结束行startRow+itemCount*3-1,列20，21我需要对这两列合并行
            int endRow=startRow+itemCount*3-1;
            mergeColumnRange(  sheet,   20,  startRow, endRow);
            mergeColumnRange(  sheet,   21,  startRow, endRow);

        }
    }
    public static void mergeColumnRange(Sheet sheet, int columnIndex, int startRow, int endRow) throws IOException {
            // 1. 移除该列在目标行范围内所有已有的合并单元格
            removeMergedRegionsInRange(sheet, columnIndex, startRow, endRow);

            // 2. 创建新的合并区域（整块合并）
            CellRangeAddress newMergeRegion = new CellRangeAddress(startRow, endRow, columnIndex, columnIndex);
            sheet.addMergedRegion(newMergeRegion);
    }
    /**
     * 移除指定列在特定行范围内的所有合并区域
     * @param sheet 工作表
     * @param column 列索引
     * @param startRow 起始行
     * @param endRow 结束行
     */
    private static void removeMergedRegionsInRange(Sheet sheet, int column, int startRow, int endRow) {
        int numMerged = sheet.getNumMergedRegions();
        // 从后往前删除，避免索引错位
        for (int i = numMerged - 1; i >= 0; i--) {
            CellRangeAddress region = sheet.getMergedRegion(i);
            // 检查该合并区域是否与目标列、目标行范围有交集
            if (region.getFirstColumn() <= column && region.getLastColumn() >= column) {
                // 存在列交集，再检查行范围是否有重叠
                if (!(region.getLastRow() < startRow || region.getFirstRow() > endRow)) {
                    sheet.removeMergedRegion(i);
                }
            }
        }
    }

    /**
     * 填充单行商品数据
     */
    private void fillItemRow(Row row, com.wimoor.amazon.inboundV2.XmlPojo.OrderItem item, int index) {
        for (int colIndex = 0; colIndex < row.getLastCellNum(); colIndex++) {
            org.apache.poi.ss.usermodel.Cell cell = row.getCell(colIndex);
            if (cell != null && cell.getCellType() == org.apache.poi.ss.usermodel.CellType.STRING) {
                String cellValue = cell.getStringCellValue();
                String newValue = cellValue;

                Map<String, String> itemextMap = item.getItemInfoExt();
                
                if(itemextMap != null){
                    newValue = newValue.replace("{list.model}", itemextMap.get("model") != null ? itemextMap.get("model") .toString() : "");
                    newValue = newValue.replace("{list.weight}", itemextMap.get("weight") != null ? itemextMap.get("weight").toString() : "0");
                    newValue = newValue.replace("{list.qty2}", itemextMap.get("qty2") != null ? itemextMap.get("qty2").toString() : "0");
                    newValue = newValue.replace("{list.currency}", itemextMap.get("currency") != null ? itemextMap.get("currency").toString() : "");
                    newValue = newValue.replace("{list.name}", itemextMap.get("element") != null ? itemextMap.get("element").toString() : "");
                    newValue = newValue.replace("{list.unit}", itemextMap.get("unit") != null ? itemextMap.get("unit").toString() : "");
                    newValue = newValue.replace("{list.unit2}", itemextMap.get("unit2") != null ? itemextMap.get("unit2") .toString() : "");
                }else{
                    newValue = newValue.replace("{list.model}", "");
                    newValue = newValue.replace("{list.weight}", "0");
                    newValue = newValue.replace("{list.qty2}","0");
                    newValue = newValue.replace("{list.currency}", "");
                    newValue = newValue.replace("{list.name}", "");
                    newValue = newValue.replace("{list.unit}",  "");
                    newValue = newValue.replace("{list.unit2}", "");
                }
                newValue = newValue.replace("{list.price}", item.getTotalPrice() != null ? item.getTotalPrice().toString() : "0");
                newValue = newValue.replace("{list.itemprice}", item.getPrice() != null ? item.getPrice().toString() : "0");
                newValue = newValue.replace("{list.materialcn}", item.getItemName() != null ? item.getItemName() : "");
                newValue = newValue.replace("{list.qty}", item.getQty() != null ? item.getQty().toString() : "0");
                newValue = newValue.replace("{list.weightunit}", "KG");
                newValue = newValue.replace("{list.index}", String.valueOf(index));
                
                if (!newValue.equals(cellValue)) {
                    if (cellValue.contains("{list.weight}")) {
                        double weightValue = parseDoubleSafely(itemextMap != null ? itemextMap.get("weight") : null);
                        cell.setCellValue(weightValue);
                    } else if (cellValue.contains("{list.qty2}")) {
                        double qty2Value = parseDoubleSafely(itemextMap != null ? itemextMap.get("qty2") : null);
                        cell.setCellValue(qty2Value);
                    } else if (cellValue.contains("{list.price}")) {
                        double priceValue = item.getTotalPrice() != null ? item.getTotalPrice().doubleValue() : 0.0;
                        cell.setCellValue(priceValue);
                    } else if (cellValue.contains("{list.itemprice}")) {
                        double itempriceValue = item.getPrice() != null ? item.getPrice().doubleValue() : 0.0;
                        cell.setCellValue(itempriceValue);
                    } else if (cellValue.contains("{list.qty}")) {
                        double qtyValue = item.getQty() != null ? item.getQty().doubleValue() : 0.0;
                        cell.setCellValue(qtyValue);
                    } else {
                        cell.setCellValue(newValue);
                    }
                }
            }
        }
    }

    private double parseDoubleSafely(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0.0;
        }
        try {
            return new BigDecimal(value.trim()).doubleValue();
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    @Override
    public Map<String, Object> importReturnStatus(UserInfo user, MultipartFile[] files) {
        Map<String, Object> result = new HashMap<>();
        int totalXmlCount = files.length;
        int successCount = 0;
        int failCount = 0;
        StringBuilder errorMsg = new StringBuilder();
        List<AmazonGroup> groups = amazonGroupService.getGroupByUser(user);
        //下面的代码是获取用户所属的店铺id,groups 为空的话就groupsid也为空集合
        List<String> groupsid=groups.stream().map(AmazonGroup::getId).collect(Collectors.toList());

        for (MultipartFile file : files) {
            try {
                byte[] bytes = file.getBytes();
                // 安全解析XML
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
                dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
                dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
                dbf.setNamespaceAware(true);
                org.w3c.dom.Document doc = dbf.newDocumentBuilder()
                        .parse(new ByteArrayInputStream(bytes));

                // 获取命名空间
                String namespace = "http://www.chinaport.gov.cn/ceb";

                // 提取 orderNo
                String orderNo = getElementText(doc, namespace, "orderNo");
                // 提取 returnStatus
                String returnStatus = getElementText(doc, namespace, "returnStatus");
                // 提取 returnTime
                String returnTime = getElementText(doc, namespace, "returnTime");
                // 提取 returnInfo
                String returnInfo = getElementText(doc, namespace, "returnInfo");

                if (StrUtil.isNotBlank(orderNo) && orderNo.length() >= 12) {
                    String prefix12 = orderNo.substring(0, 12);
                    // 使用 eq 匹配 order_number 字段
                    List<ShipInboundCustomsXml> list = this.lambdaQuery()
                            .eq(ShipInboundCustomsXml::getOrderNumber, prefix12)
                            .list();

                    if (list != null && !list.isEmpty()) {
                        ShipInboundCustomsXml first = list.get(0);
                        if(groupsid.contains(first.getGroupid().toString())){
                            first.setReturnStatus(returnStatus);
                            //20260525145923808 转换为 Date 类型
                            first.setReturnTime(DateUtil.parse(returnTime));
                            first.setReturnInfo(returnInfo);
                            this.updateById(first);
                            successCount++;
                        }
                    } else {
                        failCount++;
                        errorMsg.append("未找到匹配记录: orderNo=").append(orderNo).append("; ");
                    }
                } else {
                    failCount++;
                    errorMsg.append("orderNo无效: ").append(orderNo).append("; ");
                }
            } catch (Exception e) {
                failCount++;
                errorMsg.append("处理文件 ").append(file.getOriginalFilename()).append(" 失败: ").append(e.getMessage()).append("; ");
            }
        }

        result.put("success", true);
        result.put("totalXmlCount", totalXmlCount);
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        if (errorMsg.length() > 0) {
            result.put("msg", "共处理 " + totalXmlCount + " 个XML文件，成功 " + successCount + " 个，失败 " + failCount + " 个。"
                    + (errorMsg.length() > 500 ? errorMsg.substring(0, 500) + "..." : errorMsg.toString()));
        } else {
            result.put("msg", "共处理 " + totalXmlCount + " 个XML文件，成功 " + successCount + " 个，失败 " + failCount + " 个。");
        }
        return result;
    }

    /**
     * 从XML文档中提取指定标签名的文本内容（支持命名空间）
     */
    private String getElementText(org.w3c.dom.Document doc, String namespace, String tagName) {
        org.w3c.dom.NodeList nodeList = doc.getElementsByTagNameNS(namespace, tagName);
        if (nodeList.getLength() == 0) {
            // 尝试不带命名空间查找
            nodeList = doc.getElementsByTagName(tagName);
        }
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return null;
    }
}