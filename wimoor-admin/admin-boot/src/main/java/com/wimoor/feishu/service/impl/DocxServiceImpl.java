package com.wimoor.feishu.service.impl;

import com.lark.oapi.Client;
import com.lark.oapi.core.utils.Jsons;
import com.lark.oapi.service.docx.v1.model.CreateDocumentReq;
import com.lark.oapi.service.docx.v1.model.CreateDocumentReqBody;
import com.lark.oapi.service.docx.v1.model.CreateDocumentResp;
import com.wimoor.feishu.config.FeiShuClientBuilder;
import com.wimoor.feishu.pojo.entity.Auth;
import com.wimoor.feishu.service.IDocxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class DocxServiceImpl implements IDocxService {
    @Autowired
    FeiShuClientBuilder clientBuilder;

  public  void create(Auth auth, String title) throws Exception {
      // 构建client
      Client client = clientBuilder.getClient(auth.getAppId());
      // 发起请求
      CreateDocumentResp resp = client.docx().document()
              .create(CreateDocumentReq.newBuilder()
                      .createDocumentReqBody(CreateDocumentReqBody.newBuilder()
                              .title("title")   // 文档标题
                              .folderToken("")   // 文件夹 token，传空表示在根目录创建文档
                              .build())
                      .build()
              );
      // 处理服务端错误
      if (!resp.success()) {
          System.out.println(String.format("code:%s,msg:%s,reqId:%s", resp.getCode(), resp.getMsg(), resp.getRequestId()));
          return;
      }
      // 业务数据处理
      System.out.println(Jsons.DEFAULT.toJson(resp.getData()));
  }
}