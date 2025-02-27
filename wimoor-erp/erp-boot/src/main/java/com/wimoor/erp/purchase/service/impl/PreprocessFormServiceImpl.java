package com.wimoor.erp.purchase.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.mvc.FileUpload;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.assembly.pojo.entity.AssemblyEntryInstock;
import com.wimoor.erp.assembly.pojo.entity.AssemblyForm;
import com.wimoor.erp.assembly.pojo.vo.AssemblyVO;
import com.wimoor.erp.assembly.service.IAssemblyEntryInstockService;
import com.wimoor.erp.assembly.service.IAssemblyFormEntryService;
import com.wimoor.erp.assembly.service.IAssemblyFormService;
import com.wimoor.erp.assembly.service.IAssemblyService;
import com.wimoor.erp.common.pojo.entity.EnumByFormType;
import com.wimoor.erp.inventory.service.IInventoryService;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.pojo.entity.MaterialConsumable;
import com.wimoor.erp.material.service.IMaterialConsumableService;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.material.service.IStepWisePriceService;
import com.wimoor.erp.purchase.mapper.PreprocessFormMapper;
import com.wimoor.erp.purchase.mapper.PurchaseFormEntryMapper;
import com.wimoor.erp.purchase.mapper.PurchaseFormReceiveMapper;
import com.wimoor.erp.purchase.pojo.dto.PreprocessFormListDTO;
import com.wimoor.erp.purchase.pojo.entity.PreprocessForm;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormEntry;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormReceive;
import com.wimoor.erp.purchase.service.IPreprocessFormService;
import com.wimoor.erp.ship.pojo.dto.ShipInboundItemVo;
import com.wimoor.erp.ship.pojo.dto.ShipInboundShipmenSummarytVo;
import com.wimoor.erp.warehouse.pojo.entity.Warehouse;
import com.wimoor.erp.warehouse.pojo.vo.WarehouseShelfInventoryOptRecordVo;
import com.wimoor.erp.warehouse.service.IWarehouseService;
import com.wimoor.erp.warehouse.service.IWarehouseShelfInventoryOptRecordService;
import com.wimoor.erp.warehouse.service.IWarehouseShelfInventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.List;


@Service("preprocessFormService")
@RequiredArgsConstructor
public class PreprocessFormServiceImpl extends ServiceImpl<PreprocessFormMapper, PreprocessForm> implements IPreprocessFormService {

    final IStepWisePriceService iStepWisePriceService;
    final IMaterialService materialService;
    final IAssemblyService assemblyService;
    final IWarehouseService iWarehouseService;
    final IInventoryService inventoryService;
    final IAssemblyFormService assemblyFormService;
    final PreprocessFormMapper preprocessFormMapper;
    final PurchaseFormReceiveMapper purchaseFormReceiveMapper;
    final PurchaseFormEntryMapper purchaseFormEntryMapper;
    final IMaterialConsumableService materialConsumableService;
    final IWarehouseShelfInventoryOptRecordService iWarehouseShelfInventoryOptRecordService;
    final IWarehouseShelfInventoryService iWarehouseShelfInventoryService;
    final ISerialNumService serialNumService;
    final IAssemblyFormEntryService assemblyFormEntryService;
    final IAssemblyEntryInstockService iAssemblyEntryInstockService;
    @Autowired
    FileUpload fileUpload;

    @Override
    public void editRecItem(UserInfo user, PreprocessForm form, PreprocessFormListDTO dto) {
        //先处理form 看看是否存在run的form
        if(dto.getTableList()!=null && dto.getTableList().size()>0){
            for (String id : dto.getTableList()){
                if(dto.getSelectList().contains(id)){
                    continue;
                }
                if(form.getFtype()==0){
                    PurchaseFormReceive receive = purchaseFormReceiveMapper.selectById(id);
                    if(receive!=null){
                        if(receive.getPreprocessingid()==null){
                            continue;
                        }
                        if(receive.getPreprocessingid().equals(dto.getRunid())){
                            receive.setPreprocessingid(null);
                            purchaseFormReceiveMapper.updateById(receive);
                        }
                    }
                }else{
                    AssemblyForm assForm = assemblyFormService.getById(id);
                    if(assForm!=null){
                        if(assForm.getCheckInv()==null){
                            continue;
                        }
                        if(assForm.getCheckInv().equals(dto.getRunid())){
                            assForm.setCheckInv(null);
                            assemblyFormService.updateById(assForm);
                        }
                    }
                }

            }
        }
        //再把选上的添加到run
        if(dto.getSelectList()!=null && dto.getSelectList().size()>0){
            for (String id : dto.getSelectList()){
                if(form.getFtype()==0) {
                    PurchaseFormReceive receive = purchaseFormReceiveMapper.selectById(id);
                    if (receive != null) {
                        receive.setPreprocessingid(form.getId());
                        purchaseFormReceiveMapper.updateById(receive);
                    }
                }else{
                    AssemblyForm assForm = assemblyFormService.getById(id);
                    if (assForm != null) {
                        assForm.setCheckInv(form.getId());
                        assemblyFormService.updateById(assForm);
                    }
                }
            }
        }
    }

    @Override
    public PreprocessForm getRun(UserInfo user, String warehouseid,String ftype) {
        LambdaQueryWrapper<PreprocessForm> query = new LambdaQueryWrapper<PreprocessForm>();
        query.eq(PreprocessForm::getShopid, user.getCompanyid());
        query.eq(PreprocessForm::getWarehouseid, warehouseid);
        query.eq(PreprocessForm::getIsrun, true);
        if(ftype==null){
            throw new BizException("请指定类型是成品还是组合产品");
        }
        int type=0;
        if(ftype.equals("ass")){
            type=1;
        }
        query.eq(PreprocessForm::getFtype, type);
        List<PreprocessForm> list=preprocessFormMapper.selectList(query);
        if(list!=null && list.size()>0){
            return list.get(0);
        }else{
            PreprocessForm form = new PreprocessForm();
            String formid = iWarehouseService.getUUID();
            form.setId(formid);
            form.setCreator(user.getId());
            form.setShopid(user.getCompanyid());
            form.setCreatetime(new Date());
            form.setOperator(user.getId());
            form.setFtype(type);
            form.setOpttime(new Date());
            form.setIsrun(true);
            form.setWarehouseid(warehouseid);
            try {
                form.setNumber(serialNumService.readSerialNumber(user.getCompanyid(), "PS"));
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    form.setNumber(serialNumService.readSerialNumber(user.getCompanyid(), "PS"));
                } catch (Exception e1) {
                    e1.printStackTrace();
                    throw new BizException("编码获取失败,请联系管理员");
                }
            }
            preprocessFormMapper.insert(form);
            return form;
        }
    }

    @Override
    public IPage<Map<String, Object>> getFormList(UserInfo user, PreprocessFormListDTO dto) {
        dto.setShopid(user.getCompanyid());
        IPage<Map<String, Object>> list = preprocessFormMapper.getPreprocessFormList(dto.getPage(),dto);
        return list;
    }

    @Override
    public List<Map<String, Object>> getSkuListByRunid(String runid,String shopid) {
        PreprocessForm form = this.getById(runid);
        if(form.getFtype()==0){
            List<Map<String, Object>> list=purchaseFormReceiveMapper.getSkuListByRunid(runid,shopid);
            return list;
        }else{
            return assemblyFormService.getSkuListByRunid(runid,shopid);
        }

    }



    public void updateForm(UserInfo user,PreprocessForm preprocessForm,String donetype) {
        if(preprocessForm!=null  ){
            if(donetype.equals("checkinv")){//下架
                preprocessForm.setIsCheckInvTime(new Date());
            }else if(donetype.equals("dispatch")){//调库
                preprocessForm.setIsDispatchTime(new Date());
            }else if(donetype.equals("consumable")){//出耗材
                preprocessForm.setIsOutConsumableTime(new Date());
            }else if(donetype.equals("down")){//下载配货单
                preprocessForm.setIsDownTime(new Date());
            }else if(donetype.equals("assembly")){//自动组装
                preprocessForm.setIsAssemblyTime(new Date());
            }else if(donetype.equals("close")){
                preprocessForm.setIsrun(false);
            }
            preprocessForm.setOpttime(new Date());
            preprocessForm.setOperator(user.getId());
            preprocessFormMapper.updateById(preprocessForm);
        }
    }

    @Override
    public List<String> getFormReceiveById(UserInfo user, String planid) {
        List<Map<String, Object>> list=this.purchaseFormReceiveMapper.getSkuListByRunid(planid,user.getCompanyid());
        List<String> recList=null;
        if(list!=null && list.size()>0){
            recList=new ArrayList<String>();
            for (Map<String, Object> map : list){
                if(map.get("recid")!=null){
                    recList.add(map.get("recid").toString());
                }
            }
        }
        return recList;
    }

    @Override
    public void clearRunsForm(UserInfo user,String formid) {
            PreprocessForm form = this.getById(formid);
            if(form!=null){
                if(form.getFtype()==0){
                    LambdaQueryWrapper<PurchaseFormReceive> query2 = new LambdaQueryWrapper<PurchaseFormReceive>();
                    query2.eq(PurchaseFormReceive::getPreprocessingid, form.getId());
                    List<PurchaseFormReceive> reclist = purchaseFormReceiveMapper.selectList(query2);
                    if(reclist!=null && reclist.size()>0){
                        for (PurchaseFormReceive rec : reclist){
                            rec.setPreprocessingid(null);
                            purchaseFormReceiveMapper.updateById(rec);
                        }
                    }
                }else{
                    List<AssemblyForm> list = assemblyFormService.lambdaQuery().eq(AssemblyForm::getCheckInv, formid).list();
                    if(list!=null && list.size()>0){
                        for (AssemblyForm item : list){
                            item.setCheckInv(null);
                            assemblyFormService.updateById(item);
                        }
                    }
                }
            }
    }

    @Override
    public List<Map<String,Object>> getItemsFormConsumable(String formid) {
        PreprocessForm form = this.getById(formid);
        List<Map<String,Object>> result=new LinkedList<Map<String,Object>>();
        if(form.getFtype()==0){
            LambdaQueryWrapper<PurchaseFormReceive> query2 = new LambdaQueryWrapper<PurchaseFormReceive>();
            query2.eq(PurchaseFormReceive::getPreprocessingid, form.getId());
            List<PurchaseFormReceive> reclist = purchaseFormReceiveMapper.selectList(query2);
            if(reclist!=null && reclist.size()>0){
                for (PurchaseFormReceive rec : reclist){
                    Map<String, Object> item=new HashMap<String,Object>();
                    PurchaseFormEntry entry = purchaseFormEntryMapper.selectById(rec.getFormentryid());
                    Material material = materialService.getById(entry.getMaterialid());
                    if(material!=null){
                        item.put("sku", material.getSku());
                    }
                    item.put("amount", rec.getAmount());
                    result.add(item);
                }
            }
        }else{
            LambdaQueryWrapper<AssemblyForm> query2 = new LambdaQueryWrapper<AssemblyForm>();
            query2.eq(AssemblyForm::getCheckInv, form.getId());
            List<AssemblyForm> asslist = this.assemblyFormService.list(query2);
            if(asslist!=null && asslist.size()>0){
                for (AssemblyForm ass : asslist){
                    Map<String, Object> item=new HashMap<String,Object>();
                    Material material = materialService.getById(ass.getMainmid());
                    if(material!=null){
                        item.put("sku", material.getSku());
                    }
                    item.put("amount", ass.getAmount());
                    result.add(item);
                }
            }
        }
        return result;
    }

    @Override
    public void doneAssemblyForm(UserInfo user, String formid) {
        PreprocessForm form = this.getById(formid);
        LambdaQueryWrapper<AssemblyForm> query2 = new LambdaQueryWrapper<AssemblyForm>();
        query2.eq(AssemblyForm::getCheckInv, formid);
        query2.eq(AssemblyForm::getWarehouseid, form.getWarehouseid());
        query2.eq(AssemblyForm::getAuditstatus, 2);
        List<AssemblyForm> asslist = this.assemblyFormService.list(query2);
        if (asslist != null && asslist.size() > 0) {
            for (int i = 0; i < asslist.size(); i++) {
                AssemblyForm item = asslist.get(i);
                this.assemblyFormService.doneAssemblyForm(user, item, form.getNumber(), "打包【" + form.getNumber() + "】批量组装");
            }
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

    private void addCell(PdfPTable table , Object value, com.itextpdf.text.Font font_s, String ftype) {
        String fiedvalue =value!=null?value.toString():" ";
        PdfPCell cell = new PdfPCell(new Phrase(fiedvalue, font_s));
        table.addCell(cell);
    }

    @Override
    public void setPDFConsumableFormDetail(PdfWriter writer, UserInfo user, Document document, String runid ) {
        PreprocessForm runForm=null;
        runForm=preprocessFormMapper.selectById(runid);
        if(runForm!=null){
            List<PurchaseFormReceive> receiveList =purchaseFormReceiveMapper.findReceiveByConsumableForm(runForm.getId());
            if(receiveList==null || receiveList.size()==0){
                return;
            }
            try {
                document.addTitle("包装配货单");
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
                Paragraph p = new Paragraph("包装配货单", font);// 设置字体，解决中文显示不了的问题
                p.setAlignment(Element.ALIGN_CENTER);
                p.setSpacingAfter(10);
                document.add(p);

                PdfPTable table1 = new PdfPTable(3);
                int width1[] = { 35, 35,30 };
                table1.setWidths(width1);
                table1.setWidthPercentage(100);
                String warehouseName = "";
                String warehouseId = receiveList.get(0).getWarehouseid();
                Warehouse warePojo = iWarehouseService.getById(warehouseId);
                if(warePojo!=null){
                    warehouseName += "操作仓库: " + warePojo.getName();
                }
                PdfPCell cell11 = new PdfPCell(new Paragraph( warehouseName, font_s));
                PdfPCell cell12 = new PdfPCell(new Paragraph("创建日期: " +GeneralUtil.formatDate(runForm.getCreatetime()), font_s));
                PdfPCell cell13 = new PdfPCell(new Paragraph("SKU数量: " + receiveList.size(), font_s));
                cell11.setBorder(0);
                cell12.setBorder(0);
                cell13.setBorder(0);
                table1.addCell(cell11);
                table1.addCell(cell12);
                table1.addCell(cell13);
                table1.setSpacingAfter(5);
                document.add(table1);

                PdfPTable table = new PdfPTable(4);
                float[] widths = { 45f, 239f, 50f, 35f };
                table.setWidths(widths); // 设置宽度
                table.setWidthPercentage(100);

                String[] tableTitleList = { "图片", "产品信息", "到货数量", "包装数量" };
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
                for (int i = 0; i < receiveList.size(); i++) {
                    //---------------------图片
                    PurchaseFormReceive item = receiveList.get(i);
                    //查询conList
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
                    String value2=item.getSkuname();
                    String profix="SKU:";
                    if(conList!=null&&conList.size()>0) {
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
                    //---------------------拟发货数量
                    StringBuffer shelfbuffer = new StringBuffer();
                    shelfbuffer.append(item.getAmount()+"\n\n库存:"+(item.getQuantity()!=null?item.getQuantity():"0"));
                    List<WarehouseShelfInventoryOptRecordVo> shelflist = iWarehouseShelfInventoryOptRecordService.getRecordVo(user.getCompanyid(), item.getFormid(),
                            "outstockform", item.getMaterialid());
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
                            Map<String, Object> inv = inventoryService.findInvDetailById(subm.getId(), item.getWarehouseid(), user.getCompanyid());
                            Integer fulfillable = 0;
                            if(inv!=null&&inv.get("fulfillable")!=null&&StrUtil.isNotBlank(inv.get("fulfillable").toString())) {
                                fulfillable=Integer.parseInt(inv.get("fulfillable").toString());

                            }
                            addCell(table,"单位数量\n×"+cons.getAmount()+"\n"+
                                            (shipqty*cons.getAmount().floatValue())
                                            +"\n库存:"+fulfillable
                                    ,font_s,ftype);

                            //---------------------配送数量
                            addCell(table," ",font_s,ftype);
                        }
                    }

                }
                //---------------------耗材清单-----

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


    }

    @Override
    public void setPDFAssemblyFormDetail(List<String> formids, PdfWriter writer, UserInfo user, Document document) {
        if(formids==null || formids.size()==0){
            return;
        }
        try {
            document.addTitle("组装配货单");
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
            Paragraph p = new Paragraph("组装配货单", font);// 设置字体，解决中文显示不了的问题
            p.setAlignment(Element.ALIGN_CENTER);
            p.setSpacingAfter(10);
            document.add(p);

            PdfPTable table1 = new PdfPTable(3);
            int width1[] = { 35, 35,30 };
            table1.setWidths(width1);
            table1.setWidthPercentage(100);
            String warehouseName = "";

            Map<String, Object> assemblyForm = assemblyFormService.findById(formids.get(0));
            String warehouse = assemblyForm.get("warehouse").toString();
            if(warehouse!=null){
                warehouseName += "操作仓库: " + warehouse;
            }
            PdfPCell cell11 = new PdfPCell(new Paragraph( warehouseName, font_s));
            PdfPCell cell12 = new PdfPCell(new Paragraph("创建日期: " +GeneralUtil.formatDate(new Date()), font_s));
            PdfPCell cell13 = new PdfPCell(new Paragraph("主SKU数量: " + formids.size(), font_s));
            cell11.setBorder(0);
            cell12.setBorder(0);
            cell13.setBorder(0);
            table1.addCell(cell11);
            table1.addCell(cell12);
            table1.addCell(cell13);
            table1.setSpacingAfter(5);
            document.add(table1);

            PdfPTable table = new PdfPTable(4);
            float[] widths = { 45f, 239f, 50f, 35f };
            table.setWidths(widths); // 设置宽度
            table.setWidthPercentage(100);

            String[] tableTitleList = { "图片", "产品信息", "操作数量", "加工数量" };
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
            for (String formid : formids){
                Map<String, Object> item = assemblyFormService.findById(formid);
                //查询assemblyList
                String mainmid=item.get("mainmid").toString();
                String sku=item.get("skuname").toString();
                List<AssemblyVO> assList = assemblyService.selectByMainmid(mainmid);
                //查询conList
                List<MaterialConsumable> conList = materialConsumableService.selectByMainmid(mainmid);

                //---------------------图片
                ftype="mainsku";
                String imgpath = item.get("image")!=null?item.get("image").toString():null;
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
                String value2=item.get("mname").toString();
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
                Chunk a2 = new Chunk(profix+sku+" 加工单据:"+item.get("number"), mfont);
                a2.setLineHeight(10);
                Chunk a3 = new Chunk(value2, font_s);
                a3.setLineHeight(10);
                cell = new PdfPCell();
                cell.addElement(a2);
                cell.addElement(a3);
                table.addCell(cell);
                //---------------------拟发货数量
                StringBuffer shelfbuffer = new StringBuffer();
                shelfbuffer.append(item.get("amount")+"\n\n库存:"+(item.get("invqty")!=null?item.get("invqty"):"0"));
                String checkinv=item.get("check_inv")!=null?item.get("check_inv").toString():"0";
                List<WarehouseShelfInventoryOptRecordVo> shelflist = iWarehouseShelfInventoryOptRecordService.getRecordVo(user.getCompanyid(), checkinv,
                        EnumByFormType.assembly.getValue(), mainmid);
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
                int shipqty =Integer.parseInt(item.get("amount").toString());
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
                                getRecordVo(user.getCompanyid(),  checkinv,"outstockform", ass.getSubmid());
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
                            assShelfstr="库位:\n"+assShelfstr ;
                        }

                        addCell(table,"子SKU："+ass.getSku()+"\n"+ass.getMname(),font_s,ftype);
                        //---------------------单位数量
                        addCell(table,"单位数量\n×"+ass.getSubnumber()+"\n"+
                                        shipqty*ass.getSubnumber()
                                        +"\n\n库存:"+(ass.getFulfillable()!=null?ass.getFulfillable():0)+"\n"+assShelfstr
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
                        Map<String, Object> inv = inventoryService.findInvDetailById(subm.getId(), item.get("warehouseid").toString(), user.getCompanyid());
                        Integer fulfillable = 0;
                        if(inv!=null&&inv.get("fulfillable")!=null&&StrUtil.isNotBlank(inv.get("fulfillable").toString())) {
                            fulfillable=Integer.parseInt(inv.get("fulfillable").toString());

                        }
                        addCell(table,"单位数量\n×"+cons.getAmount()+"\n"+
                                        (shipqty*cons.getAmount().floatValue())
                                        +"\n库存:"+fulfillable
                                ,font_s,ftype);

                        //---------------------配送数量
                        addCell(table," ",font_s,ftype);
                    }
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
    public ShipInboundShipmenSummarytVo receiveQuotainfos(UserInfo user,String planid) {
        List<String> recids = getFormReceiveById(user, planid);
        if(recids==null || recids.size()==0){
            throw new BizException("计划内无SKU！");
        }
        ShipInboundShipmenSummarytVo item=this.baseMapper.findSummaryInfo(recids);
        item.setCheckinv(planid);
        List<ShipInboundItemVo> itemlist=this.baseMapper.findItemListByRecids(recids);
        item.setItemList(itemlist);
        item.setCreatedate(new Date());
        ShipInboundShipmenSummarytVo result = iWarehouseShelfInventoryService.formInvAssemblyShelf(item);
        return result;
    }

}
