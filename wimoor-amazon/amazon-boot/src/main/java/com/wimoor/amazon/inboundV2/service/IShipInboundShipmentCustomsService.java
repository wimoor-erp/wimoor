package com.wimoor.amazon.inboundV2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundShipmentCustoms;
import com.wimoor.common.user.UserInfo;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

public interface IShipInboundShipmentCustomsService extends IService<ShipInboundShipmentCustoms> {

    void downloadShipTemplateFile(ServletOutputStream fOut, UserInfo userinfo, String shipmentid, String templateid);
}
