package com.wimoor.erp.order.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.order.mapper.OrderShipPlanFormEntryMapper;
import com.wimoor.erp.order.mapper.OrderShipPlanFormMapper;
import com.wimoor.erp.order.pojo.entity.OrderShipPlanForm;
import com.wimoor.erp.order.pojo.entity.OrderShipPlanFormEntry;
import com.wimoor.erp.order.service.IOrderShipPlanFormService;
import com.wimoor.erp.stock.pojo.dto.InWarehouseItemDTO;
import com.wimoor.erp.stock.pojo.entity.InWarehouseForm;
import com.wimoor.erp.stock.service.IInWarehouseFormService;
import com.wimoor.erp.warehouse.service.IWarehouseService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderShipPlanFormServiceImpl extends ServiceImpl<OrderShipPlanFormMapper, OrderShipPlanForm> implements IOrderShipPlanFormService {

    @Resource
    private OrderShipPlanFormEntryMapper orderPlanFormEntryMapper;
    @Resource
    private ISerialNumService serialNumService;
    @Resource
    private IWarehouseService warehouseService;
    @Resource
    private IInWarehouseFormService inWarehouseFormService;
    @Override
    public boolean save(OrderShipPlanForm form) {
        boolean result = super.save(form);
        if(result) {
            this.updateEntry(form.getEntryList(), form);
        }
        return result;
    }
    @Override
    public boolean updateById(OrderShipPlanForm entity) {
        boolean result =super.updateById(entity);
        if(result) {
            this.updateEntry(entity.getEntryList(), entity);
        }
        return result;
    }
    public void updateEntry(List<OrderShipPlanFormEntry> entryList, OrderShipPlanForm form){
        for(OrderShipPlanFormEntry entry:entryList){
            entry.setFormid(form.getId());
            entry.setOpttime(new Date());
            entry.setOperator(form.getOperator());
            LambdaQueryWrapper<OrderShipPlanFormEntry> query=new LambdaQueryWrapper<OrderShipPlanFormEntry>();
            query.eq(OrderShipPlanFormEntry::getFormid,form.getId());
            query.eq(OrderShipPlanFormEntry::getMaterialid,entry.getMaterialid());
            OrderShipPlanFormEntry oldone = orderPlanFormEntryMapper.selectOne(query);
            if(oldone!=null){
                   orderPlanFormEntryMapper.update(entry,query);
            }else{
                   orderPlanFormEntryMapper.insert(entry);
            }
        }
    }

    @Override
    public IPage<Map<String, Object>> findPlanFrom(Page<Object> page, Map<String, String> param) {
        IPage<Map<String, Object>> list = this.baseMapper.findPlanForm(page, param);
        for(Map<String, Object> map:list.getRecords()){
            List<Map<String, Object>> entrys = this.baseMapper.findPlanFormEntry(map);
            map.put("entrys", entrys);
        }
        return list;
    }
    public String getValue(Object value) {
        return value==null?"":value.toString();
    }
    @Override
    public Workbook downloadOrderPlanForm(UserInfo user,String id) {
        Workbook workbook = new SXSSFWorkbook();
        Map<String,Object> param=new HashMap<String,Object>();
        param.put("shopid",user.getCompanyid());
        param.put("id",id);
        Sheet sheet = workbook.createSheet("Sheet1");
        List<Map<String, Object>> result = this.baseMapper.findPlanForm(param);
        if(result!=null&&result.size()==1){
            Map<String, Object> orderitem = result.get(0);
            Row row = sheet.createRow(0);
            Cell cell=row.createCell(0);
            cell.setCellValue("订单编号:");
            cell=row.createCell(1);
            cell.setCellValue(orderitem.get("number").toString());
            cell=row.createCell(2);
            cell.setCellValue("仓库:");
            cell=row.createCell(3);
            cell.setCellValue(orderitem.get("warehousename").toString());
            cell=row.createCell(4);
            cell.setCellValue("状态:");
            cell=row.createCell(5);
            if(orderitem.get("auditstatus").toString().equals("0")){
                cell.setCellValue("已删除");
            }else  if(orderitem.get("auditstatus").toString().equals("1")){
                cell.setCellValue("备货中");
            }else  if(orderitem.get("auditstatus").toString().equals("2")){
                cell.setCellValue("已完成");
            }else{
                cell.setCellValue(orderitem.get("auditstatus").toString());
            }
            cell=row.createCell(6);
            cell.setCellValue("创建日期:");
            cell=row.createCell(7);
            cell.setCellValue(GeneralUtil.formatDate(GeneralUtil.getDate(orderitem.get("createtime"))));
            List<Map<String, Object>> entrys = this.baseMapper.findPlanFormEntry(orderitem);
              row = sheet.createRow(1);
              cell=row.createCell(0);
            cell.setCellValue("SKU");
            cell=row.createCell(1);
            cell.setCellValue("名称");
            cell=row.createCell(2);
            cell.setCellValue("库存");
            cell=row.createCell(3);
            cell.setCellValue("箱规(单箱个数)");
            cell=row.createCell(4);
            cell.setCellValue("箱数");
            cell=row.createCell(5);
            cell.setCellValue("计划备货数量");
            cell=row.createCell(6);
            cell.setCellValue("箱子-长");
            cell=row.createCell(7);
            cell.setCellValue("箱子-宽");
            cell=row.createCell(8);
            cell.setCellValue("箱子-高");

            for(int i=0;entrys!=null&&i<entrys.size();i++) {
                Map<String, Object> item = entrys.get(i);
                row = sheet.createRow(i + 2);
                cell = row.createCell(0); // 在索引0的位置创建单元格(左上端) {100-9.9},{500-9.5}
                cell.setCellValue(getValue(item.get("sku")));
                cell = row.createCell(1);
                cell.setCellValue(getValue(item.get("name")));
                cell = row.createCell(2);
                cell.setCellValue(getValue(item.get("qty")));
                cell = row.createCell(3);
                cell.setCellValue(getValue(item.get("boxnum")));
                cell = row.createCell(4);
                cell.setCellValue(getValue(item.get("boxqty")));
                cell = row.createCell(5);
                cell.setCellValue(getValue(item.get("quantity")));
                cell = row.createCell(6);
                cell.setCellValue(getValue(item.get("plength")));
                cell = row.createCell(7);
                cell.setCellValue(getValue(item.get("pwidth")));
                cell = row.createCell(8);
                cell.setCellValue(getValue(item.get("pweight")));
            }
        }
        return workbook;
    }

    @Override
    public void outEntry(OrderShipPlanForm form) {
        LambdaQueryWrapper<OrderShipPlanFormEntry> query=new LambdaQueryWrapper<OrderShipPlanFormEntry>();
        query.eq(OrderShipPlanFormEntry::getFormid,form.getId());
        List<OrderShipPlanFormEntry> entrylist = orderPlanFormEntryMapper.selectList(query);
        com.alibaba.fastjson.JSONObject  sku = new com.alibaba.fastjson.JSONObject();
        for(OrderShipPlanFormEntry entry:entrylist){
            sku.put(entry.getMaterialid(),entry.getQuantity());
        }
        UserInfo user = UserInfoContext.get();
        InWarehouseForm inWarehouseForm = new InWarehouseForm();
        String shopid = user.getCompanyid();
        inWarehouseForm.setShopid(shopid);
        try {
            inWarehouseForm.setNumber(serialNumService.readSerialNumber(shopid, "IN"));
        } catch (Exception e) {
            e.printStackTrace();
            try {
                inWarehouseForm.setNumber(serialNumService.readSerialNumber(shopid, "IN"));
            } catch (Exception e1) {
                e1.printStackTrace();
                throw new BizException("编码获取失败,请联系管理员");
            }
        }
        inWarehouseForm.setId(warehouseService.getUUID());
        inWarehouseForm.setCreator(user.getId());
        inWarehouseForm.setCreatedate(new Date());
        inWarehouseForm.setAuditor(user.getId());
        inWarehouseForm.setAuditstatus(2);// 0：未提交，1：提交未审核，2：已审核
        inWarehouseForm.setOperator(user.getId());
        inWarehouseForm.setOpttime(new Date());
        String warehouseid = form.getWarehouseid();
        String remark = "海外仓备货单";
        inWarehouseForm.setWarehouseid(warehouseid);
        inWarehouseForm.setRemark(remark);
        inWarehouseForm.setAudittime(new Date());
        Map<String, Object> map = inWarehouseFormService.saveAction(inWarehouseForm, sku.toJSONString(), user);
        form.setIsout(true);
        this.baseMapper.updateById(form);
    }
}
