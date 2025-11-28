package com.wimoor.erp.purchase.controller;

import cn.hutool.core.util.StrUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.pojo.entity.Picture;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.IPictureService;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.api.AdminClientOneFeignManager;
import com.wimoor.erp.material.service.IAssemblyFormEntryService;
import com.wimoor.erp.material.service.IAssemblyService;
import com.wimoor.erp.common.service.IDownloadReportService;
import com.wimoor.erp.finance.pojo.entity.FinAccount;
import com.wimoor.erp.finance.service.IFaccountService;
import com.wimoor.erp.finance.service.IFinanceProjectService;
import com.wimoor.erp.inventory.service.IInventoryHisService;
import com.wimoor.erp.inventory.service.IInventoryService;
import com.wimoor.erp.material.pojo.entity.DimensionsInfo;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.service.*;
import com.wimoor.erp.purchase.alibaba.service.IPurchaseFormEntryAlibabaInfoService;
import com.wimoor.erp.purchase.pojo.entity.PurchaseForm;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormEntry;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormPayment;
import com.wimoor.erp.purchase.pojo.vo.PurchaseFormEntryVo;
import com.wimoor.erp.purchase.service.*;
import com.wimoor.erp.purchase.service.impl.PurchaseFormPaymentServiceImpl;
import com.wimoor.erp.stock.service.IChangeWhFormEntryService;
import com.wimoor.erp.util.LockCheckUtils;
import com.wimoor.erp.warehouse.service.IWarehouseService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Api(tags = "采购单接口")
@RestController
@SystemControllerLog( "采购单接口")
@RequestMapping("/api/v1/purchase_form_ext")
@RequiredArgsConstructor
public class PurchaseFormExtController {
    private final IPurchaseFormService purchaseFormService;
    private final IPurchaseFormEntryService purchaseFormEntryService;
    private final IPurchaseFormEntryAlibabaInfoService purchaseFormEntryAlibabaInfoService;
    private final IMaterialService materialService;
    private final IDimensionsInfoService dimensionsInfoService;
    final IMaterialConsumableService iMaterialConsumableService;
    private final IStepWisePriceService stepWisePriceService;
    final IAssemblyService assemblyService;
    final IAssemblyFormEntryService assemblyFormEntryService;
    final IMaterialSupplierService iMaterialSupplierService;
    final IPurchaseFinanceFormService purchaseFinanceFormService;
    final AdminClientOneFeignManager adminClientOneFeign;
    final IFaccountService iFaccountService;
    final IFinanceProjectService iFinanceProjectService;
    final IInventoryHisService iInventoryHisService;
    final IFaccountService faccountService;
    final IPurchaseFormPaymentService iPurchaseFormPaymentService;
    final IPurchaseFormReceiveService iPurchaseFormReceiveService;
    final IPictureService pictureService;
    final IWarehouseService iWarehouseService;
    final IInventoryService iInventoryService;
    final IDownloadReportService downloadReportService;
    final IChangeWhFormEntryService iChangeWhFormEntryService;
    @PostMapping("/share")
    public Result<List<PurchaseFormEntryVo>> shareFee(@RequestBody PurchaseForm dto) {
        //获取分摊费用 存起来

        List<Map<String, Object>> itemlist = dto.getItemlist();
        List<PurchaseFormEntry> entrylist=new ArrayList<PurchaseFormEntry>();
        for(Map<String, Object> item:itemlist) {
            entrylist.add(purchaseFormEntryService.getById(item.get("id").toString()));
        }
        BigDecimal totalWeight=new BigDecimal("0");
        BigDecimal totalFee= dto.getShipfee();
        Map<String,BigDecimal> itemWeight=new HashMap<String,BigDecimal>();
        for(PurchaseFormEntry item:entrylist) {
            Material material = materialService.getById(item.getMaterialid());
            DimensionsInfo dim = dimensionsInfoService.getById(material.getPkgdimensions());
            BigDecimal weight =BigDecimal.ZERO;
            if(dim!=null){
                 weight =dim.getWeight().multiply(new BigDecimal(item.getAmount()));
            }
            totalWeight=totalWeight.add(weight);
            itemWeight.put(item.getMaterialid(),weight);
        }
        int i=0;
        BigDecimal subPrecent=new BigDecimal("1");
        BigDecimal subPrice=new BigDecimal("0");
        List<PurchaseFormEntryVo> volist=new ArrayList<PurchaseFormEntryVo>();
        for(PurchaseFormEntry item:entrylist) {
            BigDecimal weight= itemWeight.get(item.getMaterialid());
            BigDecimal precent = weight.divide(totalWeight,6, RoundingMode.FLOOR) ;
            subPrecent=subPrecent.subtract(precent);
            i++;
            if(i==entrylist.size()&&subPrecent.floatValue()>0.000001) {
                precent=precent.add(subPrecent);
            }
            BigDecimal itemfee=  totalFee.multiply(precent).setScale(2,RoundingMode.FLOOR);
            subPrice=subPrice.add(itemfee.subtract(itemfee.setScale(0,RoundingMode.FLOOR)).divide(new BigDecimal("100"),6,RoundingMode.FLOOR));
            if(i==entrylist.size()&&subPrice.floatValue()>0.000001) {
                itemfee=itemfee.add(subPrice);
                itemfee=itemfee.setScale(2,RoundingMode.UP);
            }
            Material material = materialService.getById(item.getMaterialid());
            PurchaseFormEntryVo vo= new PurchaseFormEntryVo();
            vo.setId(item.getId());
            vo.setName(material.getName());
            vo.setAmount(item.getAmount());
            vo.setItemprice(itemfee);
            vo.setAmount(item.getAmount());
            vo.setMaterialid(item.getMaterialid());
            vo.setSku(material.getSku());
            Picture picture = pictureService.getById(material.getImage());
            if(picture!=null){
                vo.setImage(picture.getLocation());
            }
            volist.add(vo);
        }
        return Result.success(volist);
    }
    @PostMapping("/autoShipPay/{type}/{acct}")
    @SystemControllerLog(value = "自动付款")
    @Transactional(rollbackFor={Exception.class})
    public Result<Integer> autoShipPayAction(@PathVariable String type, @PathVariable String acct, @RequestBody List<PurchaseFormEntryVo> volist) {
        UserInfo user = UserInfoContext.get();
        LockCheckUtils lock = new LockCheckUtils("purchasepay"+user.getCompanyid());
        int result=0;
        try {
            FinAccount account =null;
            if(StrUtil.isNotBlank(acct)) {
                account=faccountService.getById(acct);
            }
            if(acct==null) {
                if(StrUtil.isNotBlank(type)) {
                    account=faccountService.getAccByMeth(user.getCompanyid(),type);
                }else {
                    account=faccountService.getAccByMeth(user.getCompanyid(),"1");
                }
            }
            account.setIsautopay(true);
            for (int i=0;i<volist.size();i++) {
                PurchaseFormEntryVo vo = volist.get(i);
                PurchaseFormEntry entry = purchaseFormEntryService.getById(vo.getId());
                if(entry.getAuditstatus()<2){
                    throw new BizException("订单状态无法操作！");
                }
                String payacc=account.getId();
                if(entry==null) {
                    continue;
                }
                PurchaseFormPayment paymentcost = new PurchaseFormPayment();
                paymentcost.setFormentryid(entry.getId());
                paymentcost.setAuditstatus(1);
                if(StrUtil.isNotBlank(type)) {
                    paymentcost.setPaymentMethod(Integer.parseInt(type));
                }
                paymentcost.setProjectid(PurchaseFormPaymentServiceImpl.type_ship);
                paymentcost.setPayprice(vo.getItemprice());
                paymentcost.setRemark("分摊运费");
                paymentcost.setOpttime(new Date());
                paymentcost.setAcct(payacc);
                paymentcost.setOperator(user.getId());
                ArrayList<PurchaseFormPayment> list = new ArrayList<PurchaseFormPayment>();
                list.add(paymentcost);
                result+=iPurchaseFormPaymentService.updatePayment(list,entry,account, user);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            Map<String, BigDecimal> projectMap = account.getProjectMap();
            for(Map.Entry<String, BigDecimal> entryProject:projectMap.entrySet()) {
                String projectid = entryProject.getKey();
                BigDecimal amount=entryProject.getValue();
                faccountService.updateFinAfterChange(account, projectid, new Date(), amount, "out");
            }
        }catch(Exception e) {
            e.printStackTrace();
            throw e;
        }finally{
            lock.clear();
        }
        return Result.success(result);
    }
}
