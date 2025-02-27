package com.wimoor.erp.order.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.pojo.entity.BasePageQuery;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.order.mapper.OrderMapper;
import com.wimoor.erp.order.pojo.entity.Order;
import com.wimoor.erp.order.pojo.entity.OrderPlatform;
import com.wimoor.erp.order.service.IOrderPlanService;
import com.wimoor.erp.order.service.IOrderPlatformService;
import com.wimoor.erp.order.service.IOrderService;
import com.wimoor.erp.stock.pojo.entity.ChangeWhForm;
import com.wimoor.erp.stock.pojo.entity.OutWarehouseForm;
import com.wimoor.erp.stock.service.IOutWarehouseFormService;
import com.wimoor.erp.warehouse.pojo.entity.Warehouse;
import com.wimoor.erp.warehouse.service.IWarehouseService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {
    private final IOrderPlatformService orderPlatformService;
    private final ISerialNumService iSerialNumService;
    private final IWarehouseService iWarehouseService;
    private final IMaterialService iMaterialsService;
    private final IOutWarehouseFormService iOutWarehouseFormService;
    @Override
    public IPage<Map<String, Object>> findByCondition(BasePageQuery dto) {
        return this.baseMapper.findByCondition(dto.getPage(),dto.getParamother());
    }

    @Override
    public IPage<Map<String, Object>> findOrderByCondition(BasePageQuery dto) {
        return this.baseMapper.findOrderByCondition(dto.getPage(),dto.getParamother());
    }

    @Override
    public List<Map<String, Object>> listOrderByCondition(BasePageQuery dto) {
        return this.baseMapper.findOrderByCondition(dto.getParamother());
    }

    public IPage<Map<String, Object>> findMaterialByCondition(Page<?> page, Map<String, String> map) {
        return  this.baseMapper.findMaterialByCondition(page,map);
    }
    public List<Map<String, Object>> findMaterialBySelect(Map<String, String> map) {
        return  this.baseMapper.findMaterialByCondition(map);
    }

    @Override
    public String uploadOrder(Sheet sheet, UserInfo user) {
        OutWarehouseForm param=new OutWarehouseForm();
        param.setShopid(user.getCompanyid());
        param.setAuditor(user.getId());
        param.setAudittime(new Date());
        param.setAuditstatus(1);
        param.setFtype(3);
        param.setOperator(user.getId());
        param.setOpttime(new Date());
        param.setRemark("多平台订单创建");
        String warehouseid=null;
        String shopid=user.getCompanyid();
        HashMap<String, Object> skuMap = new HashMap<String, Object>();
        Row titleRow = sheet.getRow(0);
        Cell namecell = titleRow.getCell(0);
        String name= namecell.getStringCellValue();
        if(name.equals("平台")){
            for(int i=0;i<sheet.getLastRowNum();i++){
                Row contentrow = sheet.getRow(i+1);
                Cell cell = contentrow.getCell(0);
                cell.setCellType(CellType.STRING);
                String platname = cell.getStringCellValue();
                if(StrUtil.isBlank(platname)){
                    continue;
                }
                Order order=new Order();
                OrderPlatform plat = orderPlatformService.lambdaQuery().eq(OrderPlatform::getName, platname).eq(OrderPlatform::getShopid, user.getCompanyid()).one();
                order.setPlatformId(plat.getId());
                cell = contentrow.getCell(1);
                cell.setCellType(CellType.STRING);
                String warehousename = cell.getStringCellValue();
                Warehouse warehouse = iWarehouseService.lambdaQuery().eq(Warehouse::getName, warehousename).eq(Warehouse::getShopid, user.getCompanyid()).one();
                warehouseid=warehouse.getId();
                order.setWarehouseid(warehouse.getId());
                cell = contentrow.getCell(2);
                cell.setCellType(CellType.STRING);
                String sku = cell.getStringCellValue();
                Material material = iMaterialsService.getBySku(shopid, sku);
                cell = contentrow.getCell(3);
                cell.setCellType(CellType.STRING);
                String orderid = cell.getStringCellValue();
                cell = contentrow.getCell(4);
                Date purchaseDate = cell.getDateCellValue();
                cell = contentrow.getCell(5);
                cell.setCellType(CellType.STRING);
                String quantity = cell.getStringCellValue();
                Integer qty= StrUtil.isNotBlank(quantity)?Integer.parseInt(quantity):null;
                cell = contentrow.getCell(6);
                cell.setCellType(CellType.STRING);
                String priceStr = cell.getStringCellValue();
                BigDecimal price=StrUtil.isNotBlank(priceStr)?new BigDecimal(priceStr):null;
                cell = contentrow.getCell(7);
                cell.setCellType(CellType.STRING);
                String shipStr = cell.getStringCellValue();
                BigDecimal shipfee=StrUtil.isNotBlank(shipStr)?new BigDecimal(shipStr):null;
                cell = contentrow.getCell(8);
                cell.setCellType(CellType.STRING);
                String refStr = cell.getStringCellValue();
                BigDecimal reffee=StrUtil.isNotBlank(refStr)?new BigDecimal(refStr):null;
                cell = contentrow.getCell(9);
                cell.setCellType(CellType.STRING);
                String refRateStr = cell.getStringCellValue();
                BigDecimal refRate=StrUtil.isNotBlank(refRateStr)?new BigDecimal(refRateStr):null;
                order.setShipFee(shipfee);
                order.setQuantity(qty);
                order.setSku(sku);
                order.setOrderId(orderid);
                order.setWarehouseid(warehouseid);
                order.setPurchaseDate(purchaseDate);
                order.setPrice(price);
                order.setReferralFee(reffee);
                order.setReferralRate(refRate);
                order.setShopid(shopid);
                order.setIsout(true);
                order.setOpttime(new Date());
                order.setOperator(user.getId());
                Order old = this.lambdaQuery()
                        .eq(Order::getShopid, shopid)
                        .eq(Order::getSku, order.getSku())
                        .eq(Order::getOrderId, order.getOrderId())
                        .eq(Order::getPlatformId, order.getPlatformId())
                        .one();
                if(old!=null){
                    order.setId(old.getId());
                    this.updateById(order);
                }else{
                    this.save(order);
                }
                skuMap.put(material.getId(), qty);
            }
        }
        try {
            param.setNumber(iSerialNumService.readSerialNumber(shopid, "O"));
        } catch (Exception e) {
            e.printStackTrace();
            try {
                param.setNumber(iSerialNumService.readSerialNumber(shopid, "O"));
            } catch (Exception e1) {
                e1.printStackTrace();
                throw new BizException("编码获取失败,请联系管理员");
            }
        }
        param.setWarehouseid(warehouseid);
        iOutWarehouseFormService.saveForm(param, skuMap, user);
        return "";
    }
}
