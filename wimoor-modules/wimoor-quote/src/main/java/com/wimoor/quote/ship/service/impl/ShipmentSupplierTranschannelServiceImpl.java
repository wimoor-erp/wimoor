package com.wimoor.quote.ship.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.quote.ship.mapper.ShipmentTranschannelMapper;
import com.wimoor.quote.ship.mapper.UserSupplierMapper;
import com.wimoor.quote.ship.pojo.dto.ShipmentSupplierTranschannelDTO;
import com.wimoor.quote.ship.pojo.entity.ShipmentSupplierTranschannel;
import com.wimoor.quote.ship.pojo.entity.ShipmentTranschannel;
import com.wimoor.quote.ship.pojo.entity.UserBuyer;
import com.wimoor.quote.ship.pojo.entity.UserSupplier;
import com.wimoor.quote.ship.service.IShipmentSupplierTranschannelService;
import com.wimoor.quote.ship.mapper.ShipmentSupplierTranschannelMapper;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
* @author liufei
* @description 针对表【t_shipment_supplier_transchannel】的数据库操作Service实现
* @createDate 2025-07-22 18:06:30
*/
@Service
@RequiredArgsConstructor
public class ShipmentSupplierTranschannelServiceImpl extends ServiceImpl<ShipmentSupplierTranschannelMapper, ShipmentSupplierTranschannel>
    implements IShipmentSupplierTranschannelService {
    final ShipmentTranschannelMapper shipmentTranschannelMapper;
    final UserSupplierMapper userSupplierMapper;
    @Override
    public IPage<Map<String, Object>> listPage(ShipmentSupplierTranschannelDTO dto) {
        return this.baseMapper.listPage(dto.getPage(),dto);
    }

    @Override
    public String uploadExcel(Sheet sheet,  UserBuyer buyer) {
        Set<String> titlelist=new HashSet<String>();
        titlelist.add("物流公司");
        titlelist.add("渠道类型");
        titlelist.add("渠道");
        String message="";
        Row row = sheet.getRow(0);
        Map<String,Integer> titleMap=new HashMap<String,Integer>();
        for(int j=0;j<row.getLastCellNum();j++){
            Cell cell = row.getCell(j);
            if(titlelist.contains(cell.getStringCellValue())){
                titleMap.put(cell.getStringCellValue(),j);
            }
        }
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row skuRow = sheet.getRow(i);
            Cell cell = skuRow.getCell(titleMap.get("物流公司"));
            cell.setCellType(CellType.STRING);
            String companyname = cell.getStringCellValue();
            if (StrUtil.isEmpty(companyname)) {
                continue;
            }
            cell = skuRow.getCell(titleMap.get("渠道类型"));
            cell.setCellType(CellType.STRING);
            String transname = cell.getStringCellValue();
            cell = skuRow.getCell(titleMap.get("渠道"));
            cell.setCellType(CellType.STRING);
            String channelname = cell.getStringCellValue();
            LambdaQueryWrapper<UserSupplier> supplierQuery=new LambdaQueryWrapper<UserSupplier>();
            supplierQuery.eq(UserSupplier::getBuyerid,buyer.getId());
            supplierQuery.eq(UserSupplier::getName,companyname);
            UserSupplier sp = userSupplierMapper.selectOne(supplierQuery);
            if(sp==null){
                message=message+(companyname+"---无法找到");
                continue;
            }
            LambdaQueryWrapper<ShipmentTranschannel> transQuery=new LambdaQueryWrapper<ShipmentTranschannel>();
            transQuery.eq(ShipmentTranschannel::getBuyerid,buyer.getId());
            transQuery.eq(ShipmentTranschannel::getName,transname);
            ShipmentTranschannel trans = shipmentTranschannelMapper.selectOne(transQuery);
            if(trans==null){
                trans=new ShipmentTranschannel();
                trans.setOpttime(new Date());
                trans.setBuyerid(buyer.getId());
                trans.setName(transname);
                trans.setDisable(false);
                shipmentTranschannelMapper.insert(trans);
            }else{
                if(trans.getDisable()==null||trans.getDisable()==true){
                    trans.setDisable(false);
                    trans.setOpttime(new Date());
                    shipmentTranschannelMapper.updateById(trans);
                }
            }
            ShipmentSupplierTranschannel sst = new ShipmentSupplierTranschannel();
            sst.setDisable(false);
            sst.setOpttime(new Date());
            sst.setBuyerid(buyer.getId());
            sst.setChannelid(trans.getId());
            sst.setSupplierid(sp.getId());
            sst.setName(channelname);
            LambdaQueryWrapper<ShipmentSupplierTranschannel> sstQuery=new LambdaQueryWrapper<ShipmentSupplierTranschannel>();
            sstQuery.eq(ShipmentSupplierTranschannel::getName,sst.getName());
            sstQuery.eq(ShipmentSupplierTranschannel::getSupplierid,sst.getSupplierid());
            ShipmentSupplierTranschannel one = this.baseMapper.selectOne(sstQuery);
            if(one!=null){
                if(one.getDisable()==true){
                    one.setDisable(false);
                    one.setOpttime(new Date());
                    one.setBuyerid(buyer.getId());
                    one.setChannelid(trans.getId());
                    this.baseMapper.updateById(one);
                }
            }else{
                this.baseMapper.insert(sst);
            }
        }
        return message;
    }


    @Override
    public void getTempExcelReport(SXSSFWorkbook workbook) {
        //创建sheet
        Sheet sheet = workbook.createSheet("sheet1");
        List<String> titlelist = new ArrayList<String>();
        titlelist.add("物流公司");
        titlelist.add("渠道类型");
        titlelist.add("渠道");
        // 在索引0的位置创建行（最顶端的行）
        Row row = sheet.createRow(0);
        for (int i = 0; i < titlelist.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(titlelist.get(i));
        }

    }
}




