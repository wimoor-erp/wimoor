package com.wimoor.erp.purchase.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.assembly.pojo.entity.AssemblyForm;
import com.wimoor.erp.assembly.service.IAssemblyFormService;
import com.wimoor.erp.material.pojo.vo.MaterialVO;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.purchase.pojo.dto.PreprocessFormListDTO;
import com.wimoor.erp.purchase.pojo.entity.PreprocessForm;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormReceive;
import com.wimoor.erp.purchase.service.IPreprocessFormService;
import com.wimoor.erp.ship.pojo.dto.ShipInboundItemVo;
import com.wimoor.erp.ship.pojo.dto.ShipInboundShipmenSummarytVo;
import com.wimoor.erp.warehouse.service.IWarehouseService;
import com.wimoor.erp.warehouse.service.IWarehouseShelfInventoryService;
import feign.FeignException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Api(tags = "预处理计划接口")
@RestController
@SystemControllerLog( "预处理计划接口")
@RequestMapping("/api/v1/purchase/preprocess")
@RequiredArgsConstructor
public class PreprocessFormController {
    final IPreprocessFormService iPreprocessFormService;
    final IMaterialService iMaterialService;
    final IWarehouseShelfInventoryService iWarehouseShelfInventoryService;
    final IWarehouseService warehouseService;
    final IAssemblyFormService assemblyFormService;
    @GetMapping("/update")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "formid", value = "预处理计划id", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "donetype", value = "checkinv:下架,dispatch:调库,consumable:出耗材,down:下载配货单,assembly:自动组装,close:结束", paramType = "query", dataType = "String"),
    })
    @ApiOperation(value = "预处理计划更新")
    @Transactional
    public Result<?> updateFormAction(  String formid, String donetype) {
        UserInfo user = UserInfoContext.get();
        PreprocessForm form =iPreprocessFormService.getById(formid);
        iPreprocessFormService.updateForm(user,form,donetype);
        return Result.success("ok");
    }


    @GetMapping("/getRunsForm")
    public Result<?> getRunsFormAction(String warehouseid,String type) {
        UserInfo user = UserInfoContext.get();
        PreprocessForm form =iPreprocessFormService.getRun(user,warehouseid,type);
        return Result.success(form);
    }



    //通过planid获取receive列表
    @GetMapping("/getFormReceiveById")
    public Result<?> getFormReceiveByIdAction(String planid) {
        UserInfo user = UserInfoContext.get();
        List<String> list =iPreprocessFormService.getFormReceiveById(user,planid);
        return Result.success(list);
    }


    @GetMapping("/receiveQuotainfos")
    public Result<?> receiveQuotainfosAction( String planid) {
        UserInfo user = UserInfoContext.get();
        ShipInboundShipmenSummarytVo itemsum=this.iPreprocessFormService.receiveQuotainfos(user,planid);
        for(ShipInboundItemVo item:itemsum.getItemList()) {
            MaterialVO material = this.iMaterialService.findMaterialById(item.getMaterialid());
            if(material!=null&&material.getImage()!=null) {
                itemsum.setShopid(material.getShopid());
                item.setImage(material.getImage());
            }
        }
        itemsum.setFormType("purchase");
        ShipInboundShipmenSummarytVo data = iWarehouseShelfInventoryService.formInvAssemblyShelf(itemsum);
        if(StrUtil.isBlank(data.getCheckinv())||data.getCheckinv().equals("0")) {
            data.setCheckinv(warehouseService.getUUID());
        }
        return Result.success(data);
    }

    @ApiOperation(value = "下载组装配货单")
    @PostMapping("/downPDFAssemblyForm")
    public void downPDFAssemblyFormAction(
            @RequestBody List<String> formids,
            HttpServletResponse response) {
        UserInfo user=UserInfoContext.get();
        response.setContentType("application/force-download");// 设置强制下载不打开
        response.addHeader("Content-Disposition", "attachment;fileName=assemblyForm" + System.currentTimeMillis() + ".pdf");// 设置文件名
        Document document = new Document(PageSize.A4);
        // step 2
        try {
            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
            iPreprocessFormService.setPDFAssemblyFormDetail(formids,writer,user, document);
        } catch(FeignException e) {
            throw new BizException(BizException.getMessage(e, "获取货件失败"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (document != null && document.isOpen()) {
                document.close();
            }
        }
    }

    @ApiOperation(value = "下载组装配货单")
    @GetMapping("/downPDFAssemblyFormId")
    public void downPDFAssemblyFormAction(  String  formid,    HttpServletResponse response) {
        UserInfo user=UserInfoContext.get();
        response.setContentType("application/force-download");// 设置强制下载不打开
        response.addHeader("Content-Disposition", "attachment;fileName=assemblyForm" + System.currentTimeMillis() + ".pdf");// 设置文件名
        Document document = new Document(PageSize.A4);
        // step 2
        try {
            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
            List<AssemblyForm> forms = assemblyFormService.lambdaQuery().eq(AssemblyForm::getCheckInv, formid).list();
            if(forms!=null&&forms.size()>0){
                List<String> formids=forms.stream().map(AssemblyForm::getId).collect(Collectors.toList());
                iPreprocessFormService.setPDFAssemblyFormDetail(formids,writer,user, document);
            }

        } catch(FeignException e) {
            throw new BizException(BizException.getMessage(e, "获取货件失败"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (document != null && document.isOpen()) {
                document.close();
            }
        }
    }

    //查询runid的skulist
    @ApiOperation(value = "查询当前runid的sku列表")
    @GetMapping("/getSkuListByRunid")
    public Result<?> getSkuListByRunid(String runid) {
        UserInfo user=UserInfoContext.get();
        List<Map<String,Object>> skulist=iPreprocessFormService.getSkuListByRunid(runid,user.getCompanyid());
        return Result.success(skulist);
    }


    @ApiOperation(value = "下载包装配货单")
    @GetMapping("/downRecPDFForm")
    public void downRecPDFFormAction(
            String runid,
            HttpServletResponse response) {
        UserInfo user=UserInfoContext.get();
        response.setContentType("application/force-download");// 设置强制下载不打开
        response.addHeader("Content-Disposition", "attachment;fileName=consumableForm" + System.currentTimeMillis() + ".pdf");// 设置文件名
        Document document = new Document(PageSize.A4);
        // step 2
        try {
            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
            iPreprocessFormService.setPDFConsumableFormDetail(writer,user, document,runid);
        } catch(FeignException e) {
            throw new BizException(BizException.getMessage(e, "获取货件失败"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (document != null && document.isOpen()) {
                document.close();
            }
        }
    }


    @PostMapping("/getFormList")
    public Result<?> getFormListAction(@RequestBody PreprocessFormListDTO dto) {
        UserInfo user = UserInfoContext.get();
        IPage<Map<String, Object>> formList = iPreprocessFormService.getFormList(user,dto);
        return Result.success(formList);
    }

    @GetMapping("/getItemsFormConsumable")
    public Result<?> getItemsFormConsumableAction(String formid) {
        List<Map<String, Object>> result = iPreprocessFormService.getItemsFormConsumable(formid);
        return Result.success(result);
    }


    @GetMapping("/clearRunsForm")
    @Transactional
    public Result<?> clearRunsFormAction(String formid) {
        UserInfo user = UserInfoContext.get();
        iPreprocessFormService.clearRunsForm(user,formid);
        return Result.success("ok");
    }

    @GetMapping("/doneAssemblyForm")
    @Transactional
    public Result<?> doneAssemblyFormAction(String formid) {
        UserInfo user = UserInfoContext.get();
        iPreprocessFormService.doneAssemblyForm(user,formid);
        return Result.success("ok");
    }

    @ApiOperation(value = "包装计划列表")
    @PostMapping("/saveItem")
    @Transactional
    public Result<?> saveItemAction(@RequestBody PreprocessFormListDTO dto) {
        UserInfo user = UserInfoContext.get();
        PreprocessForm form=iPreprocessFormService.getById(dto.getRunid());
        iPreprocessFormService.editRecItem(user,form,dto);
        return Result.success("ok");
    }




}
