package com.wimoor.amazon.transparency.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.product.service.IProductInfoService;
import com.wimoor.amazon.transparency.pojo.dto.TransparencyDTO;
import com.wimoor.amazon.transparency.pojo.entity.TransparencySkuinfo;
import com.wimoor.amazon.transparency.service.ITransparencySkuinfoService;
import com.wimoor.amazon.transparency.mapper.TransparencySkuinfoMapper;
import com.wimoor.common.user.UserInfo;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author liufei
* @description 针对表【t_amz_transparency_skuinfo】的数据库操作Service实现
* @createDate 2025-08-08 11:31:18
*/
@Service
public class TransparencySkuinfoServiceImpl extends ServiceImpl<TransparencySkuinfoMapper, TransparencySkuinfo>
    implements ITransparencySkuinfoService {
    @Autowired
    IProductInfoService iProductInfoService;
    @Override
    public TransparencySkuinfo saveSkuinfo(UserInfo userinfo, TransparencySkuinfo dto) {
        TransparencySkuinfo old = this.baseMapper.selectById(dto.getId());
        dto.setDisabled(false);
        if(old != null){
            dto.setOpttime(new Date());
            dto.setCreatetime(old.getCreatetime());
            dto.setCreator(old.getCreator());
            dto.setOperator(userinfo.getId());
            this.baseMapper.updateById(dto);
        }else{
            dto.setOpttime(new Date());
            dto.setCreatetime(new Date());
            dto.setCreator(userinfo.getId());
            dto.setOperator(userinfo.getId());
            this.baseMapper.insert(dto);
        }
        return dto;
    }

    @Override
    public IPage<Map<String, Object>> listSkuinfo(UserInfo userinfo, TransparencyDTO dto) {
        dto.setShopid(userinfo.getCompanyid());
        IPage<Map<String, Object>> page = this.baseMapper.listSkuinfo(dto.getPage(), dto);
        if(page!=null&&page.getRecords()!=null){
            for(Map<String, Object> map : page.getRecords()){
                String sku=map.get("sku").toString();
                String groupid=map.get("groupid").toString();
                Map<String, Object> item = iProductInfoService.findNameAndPicture(sku, null, groupid);
                if(item!=null){
                    map.putAll(item);
                }
            }
        }
        return page;
    }

    @Override
    public void uploadExcelAction(MultipartFile file, UserInfo user, String groupid, String authid) {
        try {
            List<TransparencySkuinfo> list = readExcel(file, TransparencySkuinfo.class);
            for(TransparencySkuinfo skuinfo:list){
                skuinfo.setGroupid(groupid);
                skuinfo.setAuthid(authid);
                skuinfo.setDisabled(false);
                TransparencySkuinfo old = this.lambdaQuery().eq(TransparencySkuinfo::getSku, skuinfo.getSku())
                        .eq(TransparencySkuinfo::getGroupid, skuinfo.getGroupid())
                        .eq(TransparencySkuinfo::getAuthid, skuinfo.getAuthid())
                        .eq(TransparencySkuinfo::getAsin, skuinfo.getAsin())
                        .eq(TransparencySkuinfo::getGtin, skuinfo.getGtin())
                        .one();
                if(old!=null){
                    skuinfo.setId(old.getId());
                }
                this.saveSkuinfo(user, skuinfo);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private List<TransparencySkuinfo> readExcel(MultipartFile file, Class<TransparencySkuinfo> transparencySkuinfoClass) throws IOException {
        InputStream inputStream = file.getInputStream();
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        List<TransparencySkuinfo> list=new ArrayList<TransparencySkuinfo>();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row info=sheet.getRow(i);
            if(info.getCell(0)==null) {
                continue;
            }
            TransparencySkuinfo skuinfo=new TransparencySkuinfo();
            skuinfo.setSku(info.getCell(0).getStringCellValue());
            skuinfo.setAsin(info.getCell(1).getStringCellValue());
            skuinfo.setGtin(info.getCell(2).getStringCellValue());
            list.add(skuinfo);
        }
        return list;
    }
}




