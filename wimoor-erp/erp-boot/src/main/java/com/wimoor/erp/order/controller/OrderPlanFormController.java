package com.wimoor.erp.order.controller;

import cn.hutool.core.util.StrUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.pojo.entity.BasePageQuery;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.order.pojo.entity.Order;
import com.wimoor.erp.order.pojo.entity.OrderPlan;
import com.wimoor.erp.order.pojo.entity.OrderPlanForm;
import com.wimoor.erp.order.pojo.entity.OrderPlatform;
import com.wimoor.erp.order.service.IOrderPlanFormService;
import com.wimoor.erp.order.service.IOrderPlanService;
import com.wimoor.erp.order.service.IOrderPlatformService;
import com.wimoor.erp.order.service.IOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Api(tags = "订单接口")
@RestController
@RequestMapping("/api/v1/orderform")
@RequiredArgsConstructor
public class OrderPlanFormController {
    private final IOrderService orderService;
    private final IOrderPlatformService orderPlatformService;
    private final IOrderPlanService orderPlanService;
    private final ISerialNumService iSerialNumService;
    private final IOrderPlanFormService orderPlanFormService;

    @PostMapping("/savePlanFrom")
    @Transactional
    public Result<?> savePlanFromAction(@RequestBody OrderPlanForm dto)   {
        UserInfo user = UserInfoContext.get();
        dto.setShopid(user.getCompanyid());
        String sernum="";
        try {
            sernum = iSerialNumService.readSerialNumber(user.getCompanyid(), "MP");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            try {
                sernum = iSerialNumService.readSerialNumber(user.getCompanyid(), "MP");
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        dto.setNumber(sernum);
        dto.setAuditstatus(1);
        dto.setAudittime(new Date());
        dto.setOperator(user.getId());
        dto.setCreator(user.getId());
        dto.setOpttime(new Date());
        dto.setCreatetime(new Date());
        if(dto.idIsNULL()){
            orderPlanFormService.save(dto);
        }else{
            orderPlanFormService.updateById(dto);
        }
        return Result.success(dto);
    }
    @PostMapping("/findPlanFrom")
    public Result<?> findPlanFromAction(@RequestBody BasePageQuery dto) {
        UserInfo user = UserInfoContext.get();
        Map<String, String> param = dto.getParamother();
        if(StrUtil.isBlank(param.get("sku"))) {
            param.put("sku",null);
        }else{
            param.put("sku","%"+param.get("sku").toString().trim()+"%");
        }
        if(StrUtil.isBlank(param.get("platformid"))) {
            param.put("platformid",null);
        }
        if(StrUtil.isBlank(param.get("warehouseid"))) {
            param.put("warehouseid",null);
        }
        if(StrUtil.isBlank(param.get("ischeck"))) {
            param.put("ischeck",null);
        }
        if(StrUtil.isBlank(param.get("isdelete"))) {
            param.put("isdelete",null);
        }
        if(StrUtil.isBlank(param.get("auditstatus"))) {
            param.put("auditstatus",null);
        }
        if(StrUtil.isBlank(param.get("isdelete"))|| param.get("isdelete").equals("false")) {
            param.put("isdelete",null);
        }
        if(StrUtil.isBlank(param.get("categoryid"))) {
            param.put("categoryid",null);
        }
        param.put("shopid",user.getCompanyid());
        return Result.success(orderPlanFormService.findPlanFrom(dto.getPage(),param));
    }

    @PostMapping("/remove")
    public Result<?> removeAction(@RequestBody List<String> ids)   {
        UserInfo user = UserInfoContext.get();
        for(String id:ids){
            OrderPlanForm form = orderPlanFormService.getById(id);
            if(form.getAuditstatus()==1){
                form.setAuditstatus(0);
                form.setAudittime(new Date());
                form.setOpttime(new Date());
                form.setOperator(user.getId());
                orderPlanFormService.updateById(form);
            }
        }
        return Result.success();
    }

    @PostMapping("/done")
    public Result<?> doneAction(@RequestBody List<String> ids)   {
        UserInfo user = UserInfoContext.get();
        for(String id:ids){
            OrderPlanForm form = orderPlanFormService.getById(id);
            if(form.getAuditstatus()==1){
                form.setAuditstatus(2);
                form.setAudittime(new Date());
                form.setOpttime(new Date());
                form.setOperator(user.getId());
                orderPlanFormService.updateById(form);
            }
        }
        return Result.success();
    }


    @ApiOperation(value = "下载feedfile文件")
    @GetMapping("/downloadOrderPlanForm")
    public void downloadOrderPlanFormAction(@RequestParam String id, HttpServletResponse response) {
        try {
            // 创建新的Excel工作薄
            UserInfo user = UserInfoContext.get();
            response.setContentType("application/force-download");// 设置强制下载不打开
            response.addHeader("Content-Disposition", "attachment;fileName=temp");// 设置文件名
            ServletOutputStream fOut = response.getOutputStream();
            fOut = response.getOutputStream();
            //插入记录条
            Workbook workbook=  orderPlanFormService.downloadOrderPlanForm(user,id);
            workbook.write(fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
