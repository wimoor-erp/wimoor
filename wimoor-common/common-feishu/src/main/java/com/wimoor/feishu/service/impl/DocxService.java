package com.wimoor.feishu.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lark.oapi.Client;
import com.lark.oapi.core.utils.Jsons;
import com.lark.oapi.service.docx.v1.model.CreateDocumentReq;
import com.lark.oapi.service.docx.v1.model.CreateDocumentReqBody;
import com.lark.oapi.service.docx.v1.model.CreateDocumentResp;
import com.wimoor.feishu.config.FeiShuClientBuilder;
@Service
public class DocxService {
    @Autowired
    private FeiShuClientBuilder clientBuilder;
  public  void test(String appid) throws Exception {
    // 构建client
 
	Client client=clientBuilder.getClient(appid);
    // 发起请求
    CreateDocumentResp resp = client.docx().document()
        .create(CreateDocumentReq.newBuilder()
            .createDocumentReqBody(CreateDocumentReqBody.newBuilder()
                .title("title")
                .folderToken("fldcniHf40Vcv1DoEc8SXeuA0Zd")
                .build())
            .build()
        );

    // 处理服务端错误
    if (!resp.success()) {
      System.out.println(String.format("code:%s,msg:%s,reqId:%s"
          , resp.getCode(), resp.getMsg(), resp.getRequestId()));
      return;
    }

    // 业务数据处理
    System.out.println(Jsons.DEFAULT.toJson(resp.getData()));
  }
}