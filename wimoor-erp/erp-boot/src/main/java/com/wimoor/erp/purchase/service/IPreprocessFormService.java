package com.wimoor.erp.purchase.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.purchase.pojo.dto.PreprocessFormListDTO;
import com.wimoor.erp.purchase.pojo.entity.PreprocessForm;
import com.wimoor.erp.ship.pojo.dto.ShipInboundShipmenSummarytVo;

import java.util.List;
import java.util.Map;


public interface IPreprocessFormService extends IService<PreprocessForm> {

    void editRecItem(UserInfo user,PreprocessForm form, PreprocessFormListDTO dto);

    PreprocessForm getRun(UserInfo user, String warehouseid,String type);


    void setPDFConsumableFormDetail(PdfWriter writer, UserInfo user, Document document, String runid );

    void setPDFAssemblyFormDetail(List<String> formids, PdfWriter writer, UserInfo user, Document document);

    IPage<Map<String, Object>> getFormList(UserInfo user, PreprocessFormListDTO dto);

    List<Map<String, Object>> getSkuListByRunid(String runid,String shopid);

    void updateForm(UserInfo user,PreprocessForm preprocessForm,String donetype);

    List<String> getFormReceiveById(UserInfo user, String planid);

    ShipInboundShipmenSummarytVo receiveQuotainfos(UserInfo user,String  planid);

    void clearRunsForm(UserInfo user, String formid);

    List<Map<String,Object>> getItemsFormConsumable(String formid);

    void doneAssemblyForm(UserInfo user, String formid);
}
