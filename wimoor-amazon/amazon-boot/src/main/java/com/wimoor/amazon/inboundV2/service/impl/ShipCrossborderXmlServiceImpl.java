package com.wimoor.amazon.inboundV2.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wimoor.amazon.api.AdminClientOneFeignManager;
import com.wimoor.amazon.api.ErpClientOneFeignManager;
import com.wimoor.amazon.auth.pojo.entity.AmazonGroup;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonGroupService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundTrans;
import com.wimoor.amazon.inbound.service.IShipInboundTransService;
import com.wimoor.amazon.inboundV2.XmlGenerator.CustomsDeclarationGenerator;
import com.wimoor.amazon.inboundV2.XmlGenerator.InventoryXmlGenerator;
import com.wimoor.amazon.inboundV2.XmlGenerator.OrderXmlGenerator;
import com.wimoor.amazon.inboundV2.XmlPojo.*;
import com.wimoor.amazon.inboundV2.mapper.ShipInboundShipmentBoxMapper;
import com.wimoor.amazon.inboundV2.pojo.entity.*;
import com.wimoor.amazon.inboundV2.service.*;
import com.wimoor.amazon.inboundV2.util.XmlHelper;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ShipCrossborderXmlServiceImpl implements IShipCrossborderXmlSevice {


    @Autowired
    IShipInboundCustomsXmlService shipInboundCustomsXmlService;
    @Autowired
    AdminClientOneFeignManager adminClientOneFeignManager;
    @Autowired
    IShipInboundShipmentService shipInboundShipmentService;
    @Autowired
    IShipInboundPlanService shipInboundPlanService;
    @Autowired
    IAmazonGroupService amazonGroupService;
    @Autowired
    IMarketplaceService marketplaceService;
    @Autowired
    IShipInboundShipmentCustomService shipInboundShipmentCustomService;
    @Autowired
    IShipInboundTransService shipInboundTransService;
    @Autowired
    ShipInboundShipmentBoxMapper shipInboundShipmentBoxMapper;
    @Autowired
    ICustomDataService customDataService;
    @Autowired
    ErpClientOneFeignManager erpClientOneFeignManager;
    @Autowired
    IShipInboundCustomsXmlItemService shipInboundCustomsXmlItemService;



    public ShipInboundCustomsXml createOrder(UserInfo userinfo, ShipInboundCustomsXml xmlInfo) throws Exception {
        // 创建订单数据
        String[] shipmentIds=xmlInfo.getNumber().split(",");
        ShipInboundShipment shipmentOne = shipInboundShipmentService.lambdaQuery().eq(ShipInboundShipment::getShipmentConfirmationId,shipmentIds[0]).one();
        ShipInboundPlan inboundPlan = shipInboundPlanService.getById(shipmentOne.getFormid());
        AmazonGroup group = amazonGroupService.getById(inboundPlan.getGroupid());
        Marketplace market=marketplaceService.selectByPKey(inboundPlan.getMarketplaceid());
        OrderData orderData = new OrderData();
        // 设置其他信息
        String oldPath = xmlInfo.getFilePath();
        xmlInfo.setOrderNumber(shipmentOne.getShipmentConfirmationId());
        xmlInfo.setDisabled(Boolean.FALSE);
        if (StrUtil.isBlankOrUndefined(xmlInfo.getGuid())) {
            xmlInfo.setGuid(XmlHelper.generateGuid());
            orderData.setGuid(xmlInfo.getGuid());
            orderData.setAppTime(XmlHelper.getCurrentAppTime());
            xmlInfo.setAppTime(XmlHelper.TIMESTAMP_FORMAT.parse(orderData.getAppTime()));
        } else {
            orderData.setGuid(xmlInfo.getGuid());
            orderData.setAppTime(XmlHelper.TIMESTAMP_FORMAT.format(xmlInfo.getAppTime()));
        }
        String dateNo="";
        String exitcustoms="";
        String shiptype="";
        //dateNo是当前日期的yyyyMMdd格式字符串
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        dateNo = sdf.format(new Date());
        xmlInfo.setGroupid(new BigInteger(group.getId()));
        orderData.setAppType(xmlInfo.getAppType());
        orderData.setAppStatus(xmlInfo.getAppStatus());
        orderData.setOrderType("W");
        orderData.setOrderNo(shipmentOne.getShipmentConfirmationId());
        orderData.setEbpCode("NULL");//企业的海关编码
        orderData.setEbpName("Amazon");
        orderData.setEbcCode(group.getCustomNumber());
        orderData.setEbcName(group.getCompany());
        orderData.setFreight(new BigDecimal("0"));
        orderData.setNote("FBA批次："+xmlInfo.getNumber());
        orderData.setCopCode(group.getCustomNumber());
        orderData.setCopName(group.getCompany());
        orderData.setDxpId(group.getDxpid());
        orderData.setBaseNote("FBA批次："+xmlInfo.getNumber());
        Map<String, String> formExtMap=new HashMap<String, String>();
        formExtMap.put("exitcustoms",exitcustoms);//出境关别  大鹏海关
        formExtMap.put("shiptype",shiptype);//运输方式  水路运输
        formExtMap.put("number",dateNo);//合同协议号
        formExtMap.put("portnumber",group.getCustomNumber());//许可证号---  海关注册登记号
        formExtMap.put("code",group.getTaxNumber());//税号
        BigDecimal totalPrice = BigDecimal.ZERO;
        Map<String,OrderItem> itemMap = new LinkedHashMap<>();
        for(String shipmentId:shipmentIds) {
            ShipInboundShipment shipment = shipInboundShipmentService.lambdaQuery().eq(ShipInboundShipment::getShipmentConfirmationId,shipmentId).one();
            List<Map<String, Object>> shipmentItems = shipInboundShipmentService.findInboundItemByShipmentId(shipment.getShipmentid());
            // 添加商品项
            for (Map<String, Object> itemvo : shipmentItems) {
                String sellersku = itemvo.get("sellersku").toString();
                OrderItem item = itemMap.get(sellersku);
                String id = itemvo.get("id").toString();
                String quantity = itemvo.get("quantity").toString();
                String unit =itemvo.get("unit") != null&&!StrUtil.isBlankOrUndefined(itemvo.get("unit").toString())  ? itemvo.get("unit").toString() : "套";
                BigDecimal price = itemvo.get("infoprice") != null ? new BigDecimal(itemvo.get("infoprice").toString()) : new BigDecimal("0");
                String image=itemvo.get("image")!=null?itemvo.get("image").toString():"";
                if(item==null) {
                    Map<String, String> itemExtMap=new HashMap<String, String>();
                    itemExtMap.put("image",image);
                    ShipInboundShipmentCustom custom = shipInboundShipmentCustomService.getById(id);
                    if(custom==null){
                        custom = new ShipInboundShipmentCustom();
                        Map<String, Object> mcustom = erpClientOneFeignManager.getCustomsAction(itemvo.get("msku").toString(), "CN");
//                        custom.setCode(mcustom.get("code").toString());
//                        custom.setCname(mcustom.get("cname").toString());
                          BeanUtil.copyProperties(mcustom,custom);
                    }
                    itemExtMap.put("element",custom.getElements());//商品海关要素
                    itemExtMap.put("model",custom.getCode());//商品名称
                    itemExtMap.put("itemprice",price.toString());//商品单价
                    itemExtMap.put("unit",unit);//单位：套
                    itemExtMap.put("unit2",itemvo.get("unit2")!=null?itemvo.get("unit2").toString():"");//单位2：个
                    itemExtMap.put("unitrate",itemvo.get("unitrate")!=null?itemvo.get("unitrate").toString():"");//单位比例
                    itemExtMap.put("currency",market.getCurrency());//币制：人民币
                    if(itemvo.get("unitrate")!=null&&!StrUtil.isBlankOrUndefined(itemvo.get("unitrate").toString())){
                        itemExtMap.put("qty2",Integer.parseInt(quantity)*Integer.parseInt(itemvo.get("unitrate").toString())+"");
                    }else{
                        itemExtMap.put("qty2","");
                    }
                    itemExtMap.put("quantity",quantity);

                    if(itemvo.get("weight")!=null){
                        BigDecimal totalweight=new BigDecimal(itemvo.get("weight").toString());
                        totalweight=totalweight.multiply(new BigDecimal(quantity));
                        itemExtMap.put("weight",totalweight.toString());
                    }
                    itemExtMap.put("totalprice",price.multiply(new BigDecimal(quantity)).toString());
                    item = new OrderItem(
                            0,                      // 序号
                            sellersku,              // 商品货号
                            custom.getCname(),               // 商品名称
                            customDataService.getByTypeName("unit", unit),                  // 单位：套
                            customDataService.getByTypeCode("currency", market.getCurrency()),                  // 币制：人民币
                            new BigDecimal(quantity),    // 数量
                            price,// 单价
                            image
                    );
                    itemExtMap.put("pname",itemvo.get("pname")!=null?itemvo.get("pname").toString():"");
                    item.setItemInfoExt(itemExtMap);
                    item.setBarCode(itemvo.get("FNSKU")!=null?itemvo.get("FNSKU").toString():"");
                    itemMap.put(sellersku, item);
                }else{
                    ShipInboundShipmentCustom custom = shipInboundShipmentCustomService.getById(id);
                    Map<String, String> itemExtMap=item.getItemInfoExt()==null?new HashMap<String, String>():item.getItemInfoExt();
                    itemExtMap.put("image",image);
                    if(custom!=null){
                        itemExtMap.put("element",custom.getElements());//商品海关要素
                        itemExtMap.put("model",custom.getCode());//商品名称
                    }
                    itemExtMap.put("itemprice",price.toString());//商品单价
                    itemExtMap.put("unit",unit);//单位：套
                    itemExtMap.put("unit2",itemvo.get("unit2")!=null?itemvo.get("unit2").toString():"");//单位2：个
                    itemExtMap.put("unitrate",itemvo.get("unitrate")!=null?itemvo.get("unitrate").toString():"");//单位比例
                    itemExtMap.put("currency",market.getCurrency());//币制：人民币
                    item.setQty(item.getQty().add(new BigDecimal(quantity)));

                    if(itemvo.get("unitrate")!=null&&!StrUtil.isBlankOrUndefined(itemvo.get("unitrate").toString())){
                        itemExtMap.put("qty2",item.getQty().intValue()*Integer.parseInt(itemvo.get("unitrate").toString())+"");
                    }else{
                        itemExtMap.put("qty2","");
                    }
                    itemExtMap.put("quantity",quantity);

                    if(itemvo.get("weight")!=null){
                        String oldtotalWeight = itemExtMap.get("weight")==null?"0":itemExtMap.get("weight");
                        BigDecimal totalweight=new BigDecimal(itemvo.get("weight").toString());
                        totalweight=totalweight.multiply(new BigDecimal(quantity));
                        itemExtMap.put("weight",totalweight.add(new BigDecimal(oldtotalWeight)).toString());
                    }
                    itemExtMap.put("totalprice",price.multiply(new BigDecimal(quantity)).toString());
                    itemExtMap.put("pname",itemvo.get("pname")!=null?itemvo.get("pname").toString():"");
                    item.setItemInfoExt(itemExtMap);
                    item.setImage(image);

                    item.setTotalPrice(item.getTotalPrice().add(price.multiply(new BigDecimal(quantity))));
                }
                totalPrice = totalPrice.add(price.multiply(new BigDecimal(quantity)));
            }
        }
        List<OrderItem> items = new ArrayList<>();
        items.addAll(itemMap.values());
        int gnum=1;
        for(OrderItem item:items) {
            item.setGnum(gnum++);
        }
        orderData.setOrderItems(items);
        orderData.setCurrency(customDataService.getByTypeCode("currency", market.getCurrency()));
        orderData.setGoodsValue(totalPrice);
        orderData.setFormInfoExt(formExtMap);
        // 生成XML
        xmlInfo.setOrderData(orderData);
        return xmlInfo;
    }
    public String getFileName(ShipInboundCustomsXml xmlInfo){
        // 保存文件
        String sequenceNumber =shipInboundCustomsXmlService.getNextSequenceNumber(xmlInfo);
        AmazonGroup group = amazonGroupService.getById(xmlInfo.getGroupid());
        xmlInfo.setAppTime(new Date());
        ShipInboundCustomsXml old = this.shipInboundCustomsXmlService.getById(xmlInfo.getGuid());
        if(old!=null){
                if(StrUtil.isNotBlank(old.getFilePath())){
                    adminClientOneFeignManager.deleteFile("customs",old.getFilePath());
                        xmlInfo.setFilePath(null);
                        xmlInfo.setFileName(null);
                    shipInboundCustomsXmlService.updateById(xmlInfo);
                }
        }
        if("dxpId".equals(xmlInfo.getOptType())){
            if(CrossBorderXmlMessageType.ORDER.getValue().equals(xmlInfo.getXmlType())){
                return XmlHelper.generateFileNameExpid(CrossBorderXmlMessageType.ORDER.getValue(), group.getDxpid(), sequenceNumber);
            }else if(CrossBorderXmlMessageType.INVENTORY.getValue().equals(xmlInfo.getXmlType())){
                return XmlHelper.generateFileNameExpid(CrossBorderXmlMessageType.INVENTORY.getValue(), group.getDxpid(), sequenceNumber);
            }else if(CrossBorderXmlMessageType.DECLARATION.getValue().equals(xmlInfo.getXmlType())){
                return XmlHelper.generateFileNameExpid(CrossBorderXmlMessageType.DECLARATION.getValue(), group.getDxpid(), sequenceNumber);
            }else if(CrossBorderXmlMessageType.DECLARATION_DRAFT.getValue().equals(xmlInfo.getXmlType())){
                return XmlHelper.generateFileNameExpid(CrossBorderXmlMessageType.DECLARATION_DRAFT.getValue(), group.getDxpid(), sequenceNumber);
            }
        }else{
            if(CrossBorderXmlMessageType.ORDER.getValue().equals(xmlInfo.getXmlType())){
                return XmlHelper.generateFileNameEdiCode(CrossBorderXmlMessageType.ORDER.getValue(),"EDI"+ group.getCustomNumber(), sequenceNumber);
            }else if(CrossBorderXmlMessageType.INVENTORY.getValue().equals(xmlInfo.getXmlType())){
                return XmlHelper.generateFileNameEdiCode(CrossBorderXmlMessageType.INVENTORY.getValue(), "EDI"+group.getCustomNumber(), sequenceNumber);
            }else if(CrossBorderXmlMessageType.DECLARATION.getValue().equals(xmlInfo.getXmlType())){
                return XmlHelper.generateFileNameEdiCode(CrossBorderXmlMessageType.DECLARATION.getValue(), "EDI"+group.getCustomNumber(), sequenceNumber);
            }else if(CrossBorderXmlMessageType.DECLARATION_DRAFT.getValue().equals(xmlInfo.getXmlType())){
                return XmlHelper.generateFileNameEdiCode(CrossBorderXmlMessageType.DECLARATION_DRAFT.getValue(), "EDI"+group.getCustomNumber(), sequenceNumber);
            }
        }
        return null;
    }

    //从item统计对应这些ShipInboundCustomsXml中的这些字段
    //     * 订单金额
    //     */
    //    @TableField(value = "total_price")
    //    private BigDecimal totalPrice;
    //
    //    /**
    //     * 订单商品数量
    //     */
    //    @TableField(value = "total_quantity")
    //    private Integer totalQuantity;
    //
    //    /**
    //     * 订单商品总重量
    //     */
    //    @TableField(value = "net_weight")
    //    private BigDecimal netWeight;
    //
    //    /**
    //     * 订单商品总重量
    //     */
    //    @TableField(value = "gross_weight")
    //    private BigDecimal grossWeight;
    //同时提炼Item内容存储到ShipInboundCustomsXmlItem中
    void calcShipmentCustomsXmlItem(ShipInboundCustomsXml xmlInfo){
        shipInboundCustomsXmlItemService.lambdaUpdate().eq(ShipInboundCustomsXmlItem::getGuid,xmlInfo.getGuid()).remove();
        if(CrossBorderXmlMessageType.ORDER.getValue().equals(xmlInfo.getXmlType())){
            List<ShipInboundCustomsXmlItem> items = new ArrayList<ShipInboundCustomsXmlItem>();
            for(OrderItem item:xmlInfo.getOrderData().getOrderItems()){
                ShipInboundCustomsXmlItem xmlItem = new ShipInboundCustomsXmlItem();
                xmlItem.setGuid(xmlInfo.getGuid());
                xmlItem.setSku(item.getItemNo());
                xmlItem.setName(item.getItemName());
                xmlItem.setQuantity(item.getQty().intValue());
                xmlItem.setPrice(item.getPrice());
                xmlItem.setTotalPrice(item.getPrice().multiply(BigDecimal.valueOf(item.getQty().intValue())));
                items.add(xmlItem);
            }
            xmlInfo.setTotalQuantity(items.stream().mapToInt(ShipInboundCustomsXmlItem::getQuantity).sum());
            xmlInfo.setTotalPrice(items.stream().map(ShipInboundCustomsXmlItem::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
            shipInboundCustomsXmlItemService.saveBatch(items);
        }
        if(CrossBorderXmlMessageType.INVENTORY.getValue().equals(xmlInfo.getXmlType())){
            List<ShipInboundCustomsXmlItem> items = new ArrayList<ShipInboundCustomsXmlItem>();
            for(InventoryItem item:xmlInfo.getInventoryData().getInventoryItems()){
                ShipInboundCustomsXmlItem xmlItem = new ShipInboundCustomsXmlItem();
                xmlItem.setGuid(xmlInfo.getGuid());
                xmlItem.setSku(item.getItemNo());
                xmlItem.setName(item.getItemName());
                xmlItem.setQuantity(item.getQty().intValue());
                xmlItem.setPrice(item.getPrice());
                xmlItem.setTotalPrice(item.getPrice().multiply(BigDecimal.valueOf(item.getQty().intValue())));
                items.add(xmlItem);
            }
            xmlInfo.setTotalQuantity(items.stream().mapToInt(ShipInboundCustomsXmlItem::getQuantity).sum());
            xmlInfo.setTotalPrice(items.stream().map(ShipInboundCustomsXmlItem::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
            xmlInfo.setNetWeight(xmlInfo.getInventoryData().getNetWeight());
            xmlInfo.setGrossWeight(xmlInfo.getGrossWeight());
            shipInboundCustomsXmlItemService.saveBatch(items);
        }
        if(CrossBorderXmlMessageType.DECLARATION.getValue().equals(xmlInfo.getXmlType())){
            List<ShipInboundCustomsXmlItem> items = new ArrayList<ShipInboundCustomsXmlItem>();
            for(DeclarationItem item:xmlInfo.getCustomsDeclaration().getItems()){
                ShipInboundCustomsXmlItem xmlItem = new ShipInboundCustomsXmlItem();
                xmlItem.setGuid(xmlInfo.getGuid());
                xmlItem.setSku(item.getSku());
                xmlItem.setName(item.getGName());
                xmlItem.setQuantity(item.getGQty().intValue());
                xmlItem.setPrice(item.getDeclPrice());
                xmlItem.setTotalPrice(item.getDeclTotal());
                items.add(xmlItem);
            }
            xmlInfo.setTotalQuantity(items.stream().mapToInt(ShipInboundCustomsXmlItem::getQuantity).sum());
            xmlInfo.setTotalPrice(items.stream().map(ShipInboundCustomsXmlItem::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
            xmlInfo.setNetWeight(xmlInfo.getCustomsDeclaration().getHeader().getNetWt());
            xmlInfo.setGrossWeight(xmlInfo.getCustomsDeclaration().getHeader().getGrossWet());
            shipInboundCustomsXmlItemService.saveBatch(items);
        }
        if(CrossBorderXmlMessageType.DECLARATION_DRAFT.getValue().equals(xmlInfo.getXmlType())){
            List<ShipInboundCustomsXmlItem> items = new ArrayList<ShipInboundCustomsXmlItem>();
            for(DeclarationItem item:xmlInfo.getCustomsDeclaration().getItems()){
                ShipInboundCustomsXmlItem xmlItem = new ShipInboundCustomsXmlItem();
                xmlItem.setGuid(xmlInfo.getGuid());
                xmlItem.setSku(item.getSku());
                xmlItem.setName(item.getGName());
                xmlItem.setQuantity(item.getGQty().intValue());
                xmlItem.setPrice(item.getDeclPrice());
                xmlItem.setTotalPrice(item.getDeclTotal());
                items.add(xmlItem);
            }
            xmlInfo.setTotalQuantity(items.stream().mapToInt(ShipInboundCustomsXmlItem::getQuantity).sum());
            xmlInfo.setTotalPrice(items.stream().map(ShipInboundCustomsXmlItem::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
            xmlInfo.setNetWeight(xmlInfo.getCustomsDeclaration().getHeader().getNetWt());
            xmlInfo.setGrossWeight(xmlInfo.getCustomsDeclaration().getHeader().getGrossWet());
            shipInboundCustomsXmlItemService.saveBatch(items);
        }
    }
    public void markShipment(ShipInboundCustomsXml xmlInfo){
        if(xmlInfo.getDisabled()==null||!xmlInfo.getDisabled()){
            calcShipmentCustomsXmlItem( xmlInfo);
        }
        String[] shipmentIdsArray = xmlInfo.getNumber().split(",");
        for(String shipmentId:shipmentIdsArray){
                ShipInboundShipment shipment = shipInboundShipmentService.lambdaQuery().eq(ShipInboundShipment::getShipmentConfirmationId,shipmentId).one();
                if(CrossBorderXmlMessageType.ORDER.getValue().equals(xmlInfo.getXmlType())){
                    if(xmlInfo.getDisabled()){
                        shipment.setOrdguid(null);
                    }else{
                        shipment.setOrdguid(xmlInfo.getGuid());
                    }
                    shipInboundShipmentService.updateById(shipment);
                }
                if(CrossBorderXmlMessageType.INVENTORY.getValue().equals(xmlInfo.getXmlType())){
                    if(xmlInfo.getDisabled()){
                        shipment.setInvguid(null);
                    }else{
                        shipment.setInvguid(xmlInfo.getGuid());
                    }
                    shipInboundShipmentService.updateById(shipment);
                }
                if(CrossBorderXmlMessageType.DECLARATION.getValue().equals(xmlInfo.getXmlType())){
                    if(xmlInfo.getDisabled()){
                        shipment.setDecguid(null);
                    }else{
                        shipment.setDecguid(xmlInfo.getGuid());
                    }
                    shipInboundShipmentService.updateById(shipment);
                }
        }
    }
    /**
     * 示例2：生成电子订单
     */
    public String generateOrder(UserInfo userinfo, ShipInboundCustomsXml xmlInfo,OrderData orderData) throws Exception {
        // 创建订单数据
        String[] shipmentIds=xmlInfo.getNumber().split(",");
        ShipInboundShipment shipment = shipInboundShipmentService.lambdaQuery().eq(ShipInboundShipment::getShipmentConfirmationId,shipmentIds[0]).one();
        ShipInboundPlan inboundPlan = shipInboundPlanService.getById(shipment.getFormid());
        AmazonGroup group = amazonGroupService.getById(inboundPlan.getGroupid());
        if("save".equals(xmlInfo.getOptType())){
            xmlInfo.setOrderData(orderData);
            xmlInfo.setOpttime(new Date());
            xmlInfo.setOperator(new BigInteger(userinfo.getId()));
            if(CrossBorderXmlMessageType.ORDER.getValue().equals(xmlInfo.getXmlType())){
                xmlInfo.setContent(JSONUtil.toJsonStr(xmlInfo.getOrderData()));
            }
            markShipment(xmlInfo);
            shipInboundCustomsXmlService.saveOrUpdate(xmlInfo);
            return xmlInfo.getGuid();
        }else{
            // 设置其他信息
            String oldPath=xmlInfo.getFilePath();
            // 生成XML
            String xmlDoc = OrderXmlGenerator.generateOrderXml(orderData);
            // 保存文件
            if(StrUtil.isBlank(xmlInfo.getFileName())){
                String sequenceNumber =shipInboundCustomsXmlService.getNextSequenceNumber(xmlInfo);
                String fileName = XmlHelper.generateFileNameExpid(CrossBorderXmlMessageType.ORDER.getValue(), group.getDxpid(), sequenceNumber);
                xmlInfo.setFileName(fileName);
            }

            String path=saveXmlToFile(xmlInfo.getFileName(), xmlDoc,xmlInfo);
            if(StrUtil.isNotBlank(oldPath)){
                adminClientOneFeignManager.deleteFile("customs",oldPath);
            }
            return xmlInfo.getGuid();
        }
    }

        /**
         * 示例2：生成电子订单
         */
        public String generateOrder(UserInfo userinfo, ShipInboundCustomsXml xmlInfo) throws Exception {
            // 创建订单数据
            if(xmlInfo.getOrderData()!=null){
                return generateOrder(userinfo,xmlInfo,xmlInfo.getOrderData());
            }
            String[] shipmentIds=xmlInfo.getNumber().split(",");
            ShipInboundShipment shipment = shipInboundShipmentService.lambdaQuery().eq(ShipInboundShipment::getShipmentConfirmationId,shipmentIds[0]).one();
            ShipInboundPlan inboundPlan = shipInboundPlanService.getById(shipment.getFormid());
            AmazonGroup group = amazonGroupService.getById(inboundPlan.getGroupid());
            createOrder(userinfo, xmlInfo);
            // 生成XML
            String oldPath=xmlInfo.getFilePath();
            String xmlDoc = OrderXmlGenerator.generateOrderXml(xmlInfo.getOrderData());
            // 保存文件
            if(StrUtil.isBlank(xmlInfo.getFileName())){
                String sequenceNumber =shipInboundCustomsXmlService.getNextSequenceNumber(xmlInfo);
                String fileName = XmlHelper.generateFileNameExpid(CrossBorderXmlMessageType.ORDER.getValue(), group.getDxpid(), sequenceNumber);
                xmlInfo.setFileName(fileName);
            }

           String path= saveXmlToFile(xmlInfo.getFileName(), xmlDoc,xmlInfo);
            if(StrUtil.isNotBlank(oldPath)){
                adminClientOneFeignManager.deleteFile("customs",oldPath);
            }
            return xmlInfo.getGuid();
        }

    /**
     * 示例4：生成出口清单
     */
    public ShipInboundCustomsXml createInventory(UserInfo userinfo, ShipInboundCustomsXml xmlInfo) throws Exception {
        String[] shipmentIds = xmlInfo.getNumber().split(",");
        ShipInboundShipment shipment = shipInboundShipmentService.lambdaQuery().eq(ShipInboundShipment::getShipmentConfirmationId, shipmentIds[0]).one();
        String formid = shipment.getFormid();
        ShipInboundPlan inboundplan = shipInboundPlanService.getById(formid);
        AmazonGroup group = amazonGroupService.getById(inboundplan.getGroupid());
        Marketplace market = marketplaceService.selectByPKey(inboundplan.getMarketplaceid());

        List<Map<String, Object>> shipmentItems = shipInboundShipmentService.findInboundItemByShipmentId(shipment.getShipmentid());
        xmlInfo.setDisabled(Boolean.FALSE);
        String logisticsName="深圳市XX国际物流有限公司";
        String logisticsCode="440317XXXX";
        String logisticsNo=null;
        // 创建清单数据
        InventoryData inventoryData =
                new InventoryData(
                        "5301",                  // 申报地海关代码（深圳海关）
                        "NULL",                                // 电商平台代码
                        "Amazon",                          // 电商平台名称
                        shipment.getShipmentConfirmationId(),// 订单号
                        logisticsCode,                        // 物流企业代码
                        logisticsName,              // 物流企业名称
                        null,                      // 物流运单号
                        group.getTaxNumber(),                  // 企业唯一编号
                        group.getTaxNumber(),                // 电商企业代码
                        group.getCompany(),                  // 电商企业名称
                        group.getTaxNumber(),                // 生产销售单位代码（同电商企业）
                        group.getCompany()                   // 生产销售单位名称
                );
        if (xmlInfo.getGuid() == null) {
            xmlInfo.setGuid(XmlHelper.generateGuid());
            inventoryData.setAppTime(XmlHelper.getCurrentAppTime());
            xmlInfo.setAppTime(XmlHelper.TIMESTAMP_FORMAT.parse(inventoryData.getAppTime()));
        } else {
            inventoryData.setAppTime(XmlHelper.TIMESTAMP_FORMAT.format(xmlInfo.getAppTime()));
        }
        xmlInfo.setGroupid(new BigInteger(group.getId()));
        inventoryData.setGuid(xmlInfo.getGuid());
        inventoryData.setAppType(xmlInfo.getAppType());
        inventoryData.setAppStatus(xmlInfo.getAppStatus());
        // 设置其他信息
        inventoryData.setAgentCode(group.getTaxNumber()); // 报关企业代码
        inventoryData.setAgentName(group.getCompany());
        inventoryData.setCountry(customDataService.getByTypeName("country", market.getName()));
        // 运抵国：英国
        inventoryData.setICurrency(customDataService.getByTypeCode("currency","CNY"));
        inventoryData.setFCurrency(customDataService.getByTypeCode("currency","CNY"));
        inventoryData.setPortCode("5317");      // 出口口岸
        inventoryData.setPod("3410");           // 出口口岸
        inventoryData.setFFlag("3");

        // 设置传输信息
        inventoryData.setCopCode(group.getTaxNumber());
        inventoryData.setCopName(group.getCompany());
        inventoryData.setDxpId(group.getDxpid());
        inventoryData.setBaseNote("FBA批次：" + xmlInfo.getNumber());
        inventoryData.setNote("FBA批次：" + xmlInfo.getNumber());
        int totalBoxNum = 0;
        BigDecimal totalPrice = BigDecimal.ZERO;
        BigDecimal totalWeight = BigDecimal.ZERO;
        BigDecimal totalGrossWeight = BigDecimal.ZERO;
        Map<String, InventoryItem> itemMap = new LinkedHashMap<>();
        for (String shipmentId : shipmentIds) {
            shipment = shipInboundShipmentService.lambdaQuery().eq(ShipInboundShipment::getShipmentConfirmationId, shipmentId).one();
            Long boxNum = shipInboundShipmentBoxMapper.selectCount(new LambdaQueryWrapper<ShipInboundShipmentBox>().eq(ShipInboundShipmentBox::getShipmentid, shipment.getShipmentid()));
            totalBoxNum = totalBoxNum + boxNum.intValue();
            ShipInboundTrans trans = shipInboundTransService.lambdaQuery().eq(ShipInboundTrans::getShipmentid,shipment.getShipmentConfirmationId()).one();
            String ordernum = "";
            if (trans != null) {
                logisticsNo=trans.getOrdernum()!=null?trans.getOrdernum():logisticsNo;
                Map<String, Object> transDetail = erpClientOneFeignManager.getShipTransChannelDetial(trans.getChannel());
                ordernum = trans.getOrdernum();
                inventoryData.setLogisticsCode(ordernum);
                if(transDetail!=null&&transDetail.get("cname")!=null) {
                    inventoryData.setLogisticsName(transDetail.get("cname").toString());
                }
                inventoryData.setLogisticsNo(StrUtil.isBlankOrUndefined(ordernum)?ordernum:shipment.getShipmentConfirmationId());
                inventoryData.setFCurrency(customDataService.getByTypeCode("currency","CNY"));
            }
            if (trans != null) {
                totalGrossWeight = totalGrossWeight.add(trans.getTransweight());
            }
            shipmentItems = shipInboundShipmentService.findInboundItemByShipmentId(shipment.getShipmentid());
            for (Map<String, Object> itemvo : shipmentItems) {
                String sellersku = itemvo.get("sellersku").toString();
                String id = itemvo.get("id").toString();
                String quantity = itemvo.get("quantity").toString();
                BigDecimal price = itemvo.get("infoprice") != null ? new BigDecimal(itemvo.get("infoprice").toString()) : new BigDecimal("0");
                String name = itemvo.get("name").toString();
                String unit = itemvo.get("unit") != null && !StrUtil.isBlankOrUndefined(itemvo.get("unit").toString()) ? itemvo.get("unit").toString() : "套";
                String weight = itemvo.get("weight") != null ? itemvo.get("weight").toString() : "0";
                InventoryItem item = itemMap.get(sellersku);
                if (item != null) {
                    item.setQty(item.getQty().add(new BigDecimal(quantity)));
                    item.setTotalPrice(item.getTotalPrice().add(price.multiply(new BigDecimal(quantity))));
                } else {
                    ShipInboundShipmentCustom custom = shipInboundShipmentCustomService.getById(id);
                    if (custom == null) {
                        custom = new ShipInboundShipmentCustom();
                        Map<String, Object> mcustom = erpClientOneFeignManager.getCustomsAction(itemvo.get("msku").toString(), "CN");
                        custom.setCode(mcustom.get("code").toString());
                        custom.setCname(mcustom.get("cname").toString());
                    }
                    item = new InventoryItem(
                            0,                     // 序号
                            sellersku,              // 企业商品编号
                            custom.getCode(),           // 海关商品编码（手机）
                            custom.getCname(),               // 商品名称
                            customDataService.getByTypeName("country", market.getName()),                  // 最终目的国：英国
                            customDataService.getByTypeCode("currency", market.getCurrency()),                  // 币制：人民币
                            new BigDecimal(quantity),    // 数量
                            customDataService.getByTypeCode("unit", unit),                  // 单位：套
                            price // 单价
                    );
                    item.setBarCode(itemvo.get("FNSKU").toString());
                }
                itemMap.put(sellersku, item);
                totalPrice = totalPrice.add(price.multiply(new BigDecimal(quantity)));
                totalWeight = totalWeight.add(new BigDecimal(weight).multiply(new BigDecimal(quantity)));
            }
        }
        // 添加商品项
        int i = 1;
        inventoryData.setPackNo(totalBoxNum);
        List<InventoryItem> items = new ArrayList<>();
        items.addAll(itemMap.values());
        for (InventoryItem item : items) {
            item.setGnum(i++);
        }
        inventoryData.setInventoryItems(items);
        if (totalGrossWeight.intValue() != 0) {
            inventoryData.setGrossWeight(totalGrossWeight);
        } else {
            inventoryData.setGrossWeight(totalWeight);
        }
        inventoryData.setNetWeight(totalWeight);
        inventoryData.setFreight(new BigDecimal("0"));
        // 生成XML
        xmlInfo.setInventoryData(inventoryData);
        return xmlInfo;
    }


    /**
     * 示例4：生成出口清单
     */
    public String generateInventory(UserInfo userinfo, ShipInboundCustomsXml xmlInfo) throws Exception {
        // 创建订单数据
        if(xmlInfo.getInventoryData()!=null){
            return generateInventory(userinfo,xmlInfo,xmlInfo.getInventoryData());
        }
        String[] shipmentIds=xmlInfo.getNumber().split(",");
        ShipInboundShipment shipment = shipInboundShipmentService.lambdaQuery().eq(ShipInboundShipment::getShipmentConfirmationId,shipmentIds[0]).one();
        ShipInboundPlan inboundPlan = shipInboundPlanService.getById(shipment.getFormid());
        AmazonGroup group = amazonGroupService.getById(inboundPlan.getGroupid());
        createInventory(userinfo, xmlInfo);
        // 生成XML
        String oldPath=xmlInfo.getFilePath();
        String xmlDoc = InventoryXmlGenerator.generateInventoryXml(xmlInfo.getInventoryData());
        // 保存文件
        if(StrUtil.isBlank(xmlInfo.getFileName())){
            String sequenceNumber =shipInboundCustomsXmlService.getNextSequenceNumber(xmlInfo);
            String fileName = XmlHelper.generateFileNameExpid(CrossBorderXmlMessageType.INVENTORY.getValue(), group.getDxpid(), sequenceNumber);
            xmlInfo.setFileName(fileName);
        }
        String path= saveXmlToFile(xmlInfo.getFileName(), xmlDoc,xmlInfo);
        if(StrUtil.isNotBlank(oldPath)){
            adminClientOneFeignManager.deleteFile("customs",oldPath);
        }
        return xmlInfo.getGuid();
    }

    /**
     * 示例2：生成电子订单
     */
    public String generateInventory(UserInfo userinfo, ShipInboundCustomsXml xmlInfo,InventoryData inventoryData) throws Exception {
        // 创建订单数据
        String[] shipmentIds=xmlInfo.getNumber().split(",");
        ShipInboundShipment shipment = shipInboundShipmentService.lambdaQuery().eq(ShipInboundShipment::getShipmentConfirmationId,shipmentIds[0]).one();
        ShipInboundPlan inboundPlan = shipInboundPlanService.getById(shipment.getFormid());
        AmazonGroup group = amazonGroupService.getById(inboundPlan.getGroupid());
        if("save".equals(xmlInfo.getOptType())){
            xmlInfo.setInventoryData(inventoryData);
            xmlInfo.setOpttime(new Date());
            xmlInfo.setOperator(new BigInteger(userinfo.getId()));
            if(CrossBorderXmlMessageType.ORDER.getValue().equals(xmlInfo.getXmlType())){
                xmlInfo.setContent(JSONUtil.toJsonStr(xmlInfo.getInventoryData()));
            }
            markShipment(xmlInfo);
            shipInboundCustomsXmlService.saveOrUpdate(xmlInfo);
            return xmlInfo.getGuid();
        }else{
            // 设置其他信息
            String oldPath=xmlInfo.getFilePath();
            // 生成XML
            String xmlDoc = InventoryXmlGenerator.generateInventoryXml(inventoryData);
            // 保存文件
            if(StrUtil.isBlank(xmlInfo.getFileName())){
                String sequenceNumber =shipInboundCustomsXmlService.getNextSequenceNumber(xmlInfo);
                String fileName = XmlHelper.generateFileNameExpid(CrossBorderXmlMessageType.INVENTORY.getValue(), group.getDxpid(), sequenceNumber);
                xmlInfo.setFileName(fileName);
            }
            String path=saveXmlToFile(xmlInfo.getFileName(), xmlDoc,xmlInfo);
            if(StrUtil.isNotBlank(oldPath)){
                adminClientOneFeignManager.deleteFile("customs",oldPath);
            }
            return xmlInfo.getGuid();
        }
    }

    /**
     * 根据海关编码合并商品列表
     * @param originalItemMap 原始商品列表（以SKU为key）
     * @return 合并后的商品列表（以海关编码为key）
     */
    private Map<String, DeclarationItem> mergeItemsByCustomCode(Map<String, DeclarationItem> originalItemMap) {
        Map<String, DeclarationItem> mergedItemMap = new LinkedHashMap<>();

        // 遍历原始商品列表
        for (DeclarationItem originalItem : originalItemMap.values()) {
            String customCode = originalItem.getCodeTS(); // 获取海关编码
            DeclarationItem mergedItem = mergedItemMap.get(customCode);

            if (mergedItem == null) {
                // 如果是新的海关编码，创建新的商品项
                mergedItem = new DeclarationItem();

                // 复制基本信息
                mergedItem.setGNo(0);
                mergedItem.setCodeTS(customCode);
                mergedItem.setGName(originalItem.getGName());
                mergedItem.setGModel(originalItem.getGModel());
                mergedItem.setGUnit(originalItem.getGUnit());
                mergedItem.setFirstUnit(originalItem.getFirstUnit());
                mergedItem.setOriginCountry(originalItem.getOriginCountry());
                mergedItem.setTradeCurr(originalItem.getTradeCurr());
                mergedItem.setDeclPrice(originalItem.getDeclPrice());
                mergedItem.setUseTo(originalItem.getUseTo());
                mergedItem.setDutyMode(originalItem.getDutyMode());
                mergedItem.setDestinationCountry(originalItem.getDestinationCountry());
                mergedItem.setCiqCode(originalItem.getCiqCode());
                mergedItem.setDeclGoodsEname(originalItem.getDeclGoodsEname());
                mergedItem.setDistrictCode(originalItem.getDistrictCode());

                // 将SKU设置为海关编码
                mergedItem.setSku(customCode);

                // 初始化数量和总价
                mergedItem.setGQty(originalItem.getGQty());
                mergedItem.setFirstQty(originalItem.getFirstQty());
                mergedItem.setDeclTotal(originalItem.getDeclTotal());

                mergedItemMap.put(customCode, mergedItem);
            } else {
                // 如果已经存在相同海关编码的商品，合并数量和总价
                mergedItem.setGQty(mergedItem.getGQty().add(originalItem.getGQty()));
                mergedItem.setFirstQty(mergedItem.getFirstQty().add(originalItem.getFirstQty()));
                mergedItem.setDeclTotal(mergedItem.getDeclTotal().add(originalItem.getDeclTotal()));
            }
        }

        return mergedItemMap;
    }
public ShipInboundCustomsXml createDeclaration(UserInfo userinfo, ShipInboundCustomsXml xmlInfo){
        try {
            String[] shipmentIds=xmlInfo.getNumber().split(",");
            ShipInboundShipment shipment = shipInboundShipmentService.lambdaQuery().eq(ShipInboundShipment::getShipmentConfirmationId,shipmentIds[0]).one();
            String formid = shipment.getFormid();
            ShipInboundPlan inboundplan = shipInboundPlanService.getById(formid);
            AmazonGroup group = amazonGroupService.getById(inboundplan.getGroupid());
            Marketplace market=marketplaceService.selectByPKey(inboundplan.getMarketplaceid());

            xmlInfo.setDisabled(Boolean.FALSE);
            // 创建报关单XML生成器
            CustomsDeclaration helper = new CustomsDeclaration();
            // ====================== 1. 设置报关单表头 ======================

            DeclarationHeader header =
                    new DeclarationHeader(
                            "5301",         // 申报地海关（深圳海关）
                            "5317",                     // 进出口岸
                            group.getCustomNumber(),   // 申报单位代码
                            group.getCompany(),     // 申报单位名称
                            group.getCustomNumber(),   // 境内收发货人代码
                            group.getCompany()      // 境内收发货人名称
                    );
            SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
            String sequenceNumber =shipInboundCustomsXmlService.getNextSequenceNumber(xmlInfo);
            // 设置其他信息
            String oldPath=xmlInfo.getFilePath();
            if(StrUtil.isBlankOrUndefined(xmlInfo.getGuid())){
                xmlInfo.setGuid(XmlHelper.generateGuid());
                xmlInfo.setAppTime(new Date());
            }

            // 设置其他表头信息
            //seqNo 18位字符的随机码
            header.setSeqNo(TIMESTAMP_FORMAT.format(new Date())+sequenceNumber);                // 首次申报为空，系统生成
            header.setCopCode(group.getCustomNumber());
            header.setCopName(group.getCompany());
            header.setOwnerCode(group.getTaxNumber());
            header.setOwnerName(group.getCompany());
            header.setBillNo(shipment.getShipmentConfirmationId());
            header.setContrNo("CONTRACT001");
            header.setTrafMode("6");            // 6-邮件
            header.setTradeCountry("303");      // 英国
            header.setTradeMode("9610");        // 跨境电商零售出口
            header.setDistinatePort("3410");    // 伦敦
            header.setGrossWet(new BigDecimal("2.5"));
            header.setNetWt(new BigDecimal("2.0"));
            header.setPackNo(1);
            header.setTypistNo("8950000038782");
            header.setInputerName(userinfo.getUserName());
            header.setEntryType("M");           // 无纸化通关

            // 跨境电商专用字段
            header.setTradeAreaCode("303");     // 贸易国别：英国
            header.setMarkNo("N/M");
            header.setDespPortCode("5317");     // 启运港
            header.setEntyPortCode("5317");     // 入境口岸
            header.setGoodsPlace("深圳保税区");
            header.setDeclareName(userinfo.getUserName());

            // 境外企业信息(进口）
//            header.setOverseasConsignorCode("GB123456789");
//            header.setOverseasConsignorCname("英国电商公司");
//            header.setOverseasConsignorEname("UK E-commerce Co., Ltd.");
//            header.setOverseasConsigneeCode("US987654321");
//            header.setOverseasConsigneeEname("US Customer Inc.");

            header.setNoteS("FBA批次："+xmlInfo.getNumber());

            // ====================== 2. 设置报关单表体 ======================


            BigDecimal totalPrice=new BigDecimal("0");
            BigDecimal totalWeight=new BigDecimal("0");
            BigDecimal totalGrossWeight=new BigDecimal("0");
            Map<String,DeclarationItem> itemMap=new LinkedHashMap<>();
            for(String shipmentId:shipmentIds) {
                ShipInboundTrans trans = shipInboundTransService.getById(shipment.getShipmentConfirmationId());
                List<Map<String, Object>> shipmentItems = shipInboundShipmentService.findInboundItemByShipmentId(shipment.getShipmentid());
                BigDecimal totalShipmentWeight=new BigDecimal("0");
                for (Map<String, Object> itemvo : shipmentItems) {
                    String sellersku = itemvo.get("sellersku").toString();
                    String id = itemvo.get("id").toString();
                    String quantity = itemvo.get("quantity").toString();
                    BigDecimal price = itemvo.get("infoprice") != null ? new BigDecimal(itemvo.get("infoprice").toString()) : new BigDecimal("0");

                    String unit = itemvo.get("unit") != null&&!StrUtil.isBlankOrUndefined(itemvo.get("unit").toString())  ? itemvo.get("unit").toString() : "套";
                    String weight = itemvo.get("weight") != null ? itemvo.get("weight").toString() : "0";
                    // 商品1：智能手机
                    DeclarationItem item1 = itemMap.get(sellersku);
                    if(item1==null){
                        ShipInboundShipmentCustom custom = shipInboundShipmentCustomService.getById(id);
                        if(custom==null){
                            custom = new ShipInboundShipmentCustom();
                            Map<String, Object> mcustom = erpClientOneFeignManager.getCustomsAction(itemvo.get("msku").toString(), "CN");
                            custom.setCode(mcustom.get("code").toString());
                            custom.setCname(mcustom.get("cname").toString());
                            custom.setElements(mcustom.get("elements")!=null?mcustom.get("elements").toString():"");
                            custom.setEname(mcustom.get("ename").toString());
                        }
                        String name = custom.getCname();
                        String ename = custom.getEname();
                        String code = custom.getCode();
                        item1 =
                            new DeclarationItem(
                                    0,                          // 商品序号
                                    code,               // 商品编码（手机）
                                    name,                  // 商品名称
                                    new BigDecimal(quantity),        // 数量
                                    customDataService.getByTypeName("unit", unit),                        // 单位：个
                                    price   // 单价
                            );
                        item1.setSku(sellersku);
                        item1.setGModel(custom.getElements());
                        item1.setFirstQty(new BigDecimal(quantity));
                        item1.setFirstUnit(customDataService.getByTypeName("unit", unit));
                        item1.setOriginCountry("142");      // 原产国：中国
                        item1.setDestinationCountry(customDataService.getByTypeName("country", market.getName())); // 最终目的国：英国
                        item1.setUseTo("01");               // 用途：外贸自营内销
                        item1.setExgNo(sellersku);
                        item1.setCiqCode("100");            // CIQ代码
                        item1.setDeclGoodsEname(ename);
                        item1.setDistrictCode("44035");     // 境内货源地：深圳
                        itemMap.put(sellersku, item1);
                    }else{
                        item1.setGQty(item1.getGQty().add(new BigDecimal(quantity)));
                        item1.setFirstQty(item1.getFirstQty().add(new BigDecimal(quantity)));
                        item1.setDeclTotal(item1.getDeclTotal().add(new BigDecimal(quantity).multiply(price)));
                    }


                    // 添加许可证信息（如果需要）
//                GoodsLimit limit1 =
//                        new GoodsLimit(
//                                i,                  // 商品序号
//                                "325",                      // 许可证类别代码
//                                "20231020001"               // 许可证编号
//                        );
//                item1.getGoodsLimits().add(limit1);


                    totalPrice = totalPrice.add(price.multiply(new BigDecimal(quantity)));
                    totalWeight = totalWeight.add(new BigDecimal(weight).multiply(new BigDecimal(quantity)));
                    totalShipmentWeight=totalShipmentWeight.add(new BigDecimal(weight).multiply(new BigDecimal(quantity)));
                }
                if(trans!=null&&trans.getTransweight()!=null){
                    totalGrossWeight=totalGrossWeight.add(trans.getTransweight());
                }else{
                    totalGrossWeight=totalGrossWeight.add(totalShipmentWeight);
                }
            }
            // 在items.addAll(itemMap.values())之前调用合并函数
            itemMap = mergeItemsByCustomCode(itemMap);
            header.setNetWt(totalWeight);
            header.setGrossWet(totalGrossWeight);
            helper.setHeader(header);
            List<DeclarationItem> items = new ArrayList<>();
            items.addAll(itemMap.values());
            int i=1;
            for(DeclarationItem item:items){
                item.setGNo(i++);
            }
            helper.setItems(items);
            // ====================== 3. 设置集装箱信息 ======================
            List<ContainerInfo> containers = new ArrayList<>();
            ContainerInfo container =
                    new ContainerInfo(
                            "CBHU3202732",   // 集装箱号
                            "13",                       // 集装箱规格
                            "1"                         // 商品项号
                    );
            container.setGoodsContaWt(new BigDecimal("2.5"));
            containers.add(container);
            helper.setContainers(containers);
            // ====================== 4. 设置随附单证 ======================
            List<LicenseDocu> docus = new ArrayList<>();
            docus.add(new LicenseDocu(
                    "Y",                 // 单证代码
                    "<14>I2023-0166475"            // 单证编号
            ));

            helper.setLicenseDocus(docus);

            // ====================== 5. 设置申请单证信息 ======================
            List<RequestCert> certs = new ArrayList<>();
            RequestCert cert =
                    new RequestCert();
            cert.setAppCertCode("14");
            cert.setApplOri("1");
            cert.setApplCopyQuan("2");
            certs.add(cert);

            helper.setRequestCerts(certs);

            // ====================== 6. 设置企业资质信息 ======================
            List<CopLimit> limits = new ArrayList<>();
            CopLimit copLimit =
                    new CopLimit();
            copLimit.setEntQualifNo("5A");
            copLimit.setEntQualifTypeCode("300");
            limits.add(copLimit);

            helper.setCopLimits(limits);

            // ====================== 7. 设置自由文本信息 ======================
            FreeText freeText =
                    new FreeText();
            freeText.setDecNo("22100590");      // 报关员编码
            freeText.setDecBpNo("13800138000"); // 报关员联系方式
            helper.setFreeText(freeText);

            // ====================== 8. 设置签名信息 ======================
            SignInfo signInfo =
                    new SignInfo(
                            "8950000038782",            // 操作员IC卡号
                            "张三",                     // 操作员姓名
                            "DEC202310200001"           // 客户端报关单编号
                    );
            signInfo.setCopCode("440315XXXX");
            signInfo.setNote("跨境电商出口申报");
            signInfo.setBillSeqNo("DEC202310200001");

            helper.setSignInfo(signInfo);

            // ====================== 9. 设置电子随附单据 ======================
            EdocRelation edocRelation =
                    new EdocRelation();
            edocRelation.setEdocId("DEC202310200001.pdf");
            edocRelation.setEdocCode("00000001");
            edocRelation.setOpNote("跨境电商申报单据");
            edocRelation.setEdocOwnerCode(group.getTaxNumber());
            edocRelation.setEdocOwnerName(group.getCompany());
            edocRelation.setEdocSize("1024");

            helper.setEdocRelation(edocRelation);
            xmlInfo.setCustomsDeclaration(helper);
           return xmlInfo;

        } catch (Exception e) {
            e.printStackTrace();
        }
    return null;
}

    /**
     * 示例4：生成出口清单
     */
    public String generateDeclaration(UserInfo userinfo, ShipInboundCustomsXml xmlInfo) throws Exception {
        // 创建订单数据
        if(xmlInfo.getCustomsDeclaration()!=null){
            return generateDeclaration(userinfo,xmlInfo,xmlInfo.getCustomsDeclaration());
        }
        String[] shipmentIds=xmlInfo.getNumber().split(",");
        ShipInboundShipment shipment = shipInboundShipmentService.lambdaQuery().eq(ShipInboundShipment::getShipmentConfirmationId,shipmentIds[0]).one();
        ShipInboundPlan inboundPlan = shipInboundPlanService.getById(shipment.getFormid());
        AmazonGroup group = amazonGroupService.getById(inboundPlan.getGroupid());
        createDeclaration(userinfo, xmlInfo);
        // 生成XML
        String oldPath=xmlInfo.getFilePath();
        String xmlDoc = CustomsDeclarationGenerator.generateDeclarationXml(xmlInfo.getCustomsDeclaration());
        // 保存文件
        if(StrUtil.isBlank(xmlInfo.getFileName())){
            String sequenceNumber =shipInboundCustomsXmlService.getNextSequenceNumber(xmlInfo);
            String fileName = XmlHelper.generateFileNameExpid(xmlInfo.getXmlType(), group.getDxpid(), sequenceNumber);
            xmlInfo.setFileName(fileName);
        }
        String path= saveXmlToFile(xmlInfo.getFileName(), xmlDoc,xmlInfo);
        if(StrUtil.isNotBlank(oldPath)){
            adminClientOneFeignManager.deleteFile("customs",oldPath);
        }
        return xmlInfo.getGuid();
    }

    /**
     * 示例2：生成电子订单
     */
    public String generateDeclaration(UserInfo userinfo, ShipInboundCustomsXml xmlInfo,CustomsDeclaration customsDeclaration) throws Exception {
        // 创建订单数据
        String[] shipmentIds=xmlInfo.getNumber().split(",");
        ShipInboundShipment shipment = shipInboundShipmentService.lambdaQuery().eq(ShipInboundShipment::getShipmentConfirmationId,shipmentIds[0]).one();
        ShipInboundPlan inboundPlan = shipInboundPlanService.getById(shipment.getFormid());
        AmazonGroup group = amazonGroupService.getById(inboundPlan.getGroupid());
        if("save".equals(xmlInfo.getOptType())){
            xmlInfo.setCustomsDeclaration(customsDeclaration);
            xmlInfo.setOpttime(new Date());
            xmlInfo.setOperator(new BigInteger(userinfo.getId()));
            if(CrossBorderXmlMessageType.DECLARATION.getValue().equals(xmlInfo.getXmlType())){
                xmlInfo.setContent(JSONUtil.toJsonStr(xmlInfo.getCustomsDeclaration()));
            }
            markShipment(xmlInfo);
            shipInboundCustomsXmlService.saveOrUpdate(xmlInfo);
            return xmlInfo.getGuid();
        }else{
            // 设置其他信息
            String oldPath=xmlInfo.getFilePath();
            // 生成XML
            String xmlDoc = CustomsDeclarationGenerator.generateDeclarationXml(customsDeclaration);
            // 保存文件
            if(StrUtil.isBlank(xmlInfo.getFileName())){
                String sequenceNumber =shipInboundCustomsXmlService.getNextSequenceNumber(xmlInfo);
                String fileName = XmlHelper.generateFileNameExpid(xmlInfo.getXmlType(), group.getDxpid(), sequenceNumber);
                xmlInfo.setFileName(fileName);
            }
            String path=saveXmlToFile(xmlInfo.getFileName(), xmlDoc,xmlInfo);
            if(StrUtil.isNotBlank(oldPath)){
                adminClientOneFeignManager.deleteFile("customs",oldPath);
            }
            return xmlInfo.getGuid();
        }
    }

/**
         * 保存XML到文件
         *
         * @return
         */
        public String saveXmlToFile(String fileName, String xmlContent, ShipInboundCustomsXml xml) {
            try {
                // 这里可以根据需要保存到文件系统
                String path = adminClientOneFeignManager.uploadFile("customs", convert(xmlContent, fileName));
                UserInfo userInfo = UserInfoContext.get();
                xml.setFilePath(path);
                xml.setFileName(fileName);
                xml.setOpttime(new Date());
                xml.setOperator(new BigInteger(userInfo.getId()));
                if(CrossBorderXmlMessageType.ORDER.getValue().equals(xml.getXmlType())){
                    xml.setContent(JSONUtil.toJsonStr(xml.getOrderData()));
                }else if(CrossBorderXmlMessageType.INVENTORY.getValue().equals(xml.getXmlType())){
                    xml.setContent(JSONUtil.toJsonStr(xml.getInventoryData()));
                }else if(CrossBorderXmlMessageType.DECLARATION.getValue().equals(xml.getXmlType())){
                    xml.setContent(JSONUtil.toJsonStr(xml.getCustomsDeclaration()));
                }else if(CrossBorderXmlMessageType.DECLARATION_DRAFT.getValue().equals(xml.getXmlType())){
                    xml.setContent(JSONUtil.toJsonStr(xml.getCustomsDeclaration()));
                }

                Boolean result=shipInboundCustomsXmlService.saveOrUpdate(xml);
                if(result){
                    markShipment(xml);
                }
                return xml.getGuid();
            } catch (Exception e) {
                System.err.println("保存文件失败：" + e.getMessage());
            }
            return null;
        }


    public static MultipartFile convert(String content, String filename) throws IOException {
        // 创建DiskFileItemFactory
        DiskFileItemFactory factory = new DiskFileItemFactory(1024, null);
        // 创建DiskFileItem
        DiskFileItem fileItem = (DiskFileItem) factory.createItem(
                "file",          // fieldName
                "text/plain",    // contentType
                false,           // isFormField
                filename         // fileName
        );
        fileItem.setDefaultCharset("UTF-8");
        // 写入内容
        try (InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
             OutputStream outputStream = fileItem.getOutputStream()) {
             long l = inputStream.transferTo(outputStream);
        }
        // 转换为CommonsMultipartFile
        return new CommonsMultipartFile(fileItem);
    }

        //下载文件
        @Override
        public String downloadShipXmlFile(UserInfo userinfo, ShipInboundCustomsXml xml) throws Exception {
            if(CrossBorderXmlMessageType.ORDER.getValue().equals(xml.getXmlType())){
                return generateOrder(userinfo,xml);
            }else if(CrossBorderXmlMessageType.INVENTORY.getValue().equals(xml.getXmlType())){
                return generateInventory(userinfo,xml);
            }else if(CrossBorderXmlMessageType.DECLARATION.getValue().equals(xml.getXmlType())){
                return generateDeclaration(userinfo,xml);
            }else if(CrossBorderXmlMessageType.DECLARATION_DRAFT.getValue().equals(xml.getXmlType())){
                return generateDeclaration(userinfo,xml);
            }
            return null;
        }


}