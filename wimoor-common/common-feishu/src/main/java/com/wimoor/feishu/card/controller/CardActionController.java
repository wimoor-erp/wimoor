package com.wimoor.feishu.card.controller;
import com.lark.oapi.Client;
import com.lark.oapi.card.CardActionHandler;
import com.lark.oapi.card.model.CardAction;

import com.lark.oapi.core.request.RequestOptions;
import com.lark.oapi.core.utils.Jsons;
import com.lark.oapi.sdk.servlet.ext.ServletAdapter;
import com.lark.oapi.service.im.v1.enums.ReceiveIdTypeEnum;
import com.lark.oapi.service.im.v1.model.CreateMessageReq;
import com.lark.oapi.service.im.v1.model.CreateMessageReqBody;
import com.wimoor.feishu.config.FeiShuClientBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CardActionController {
	
@Autowired
FeiShuClientBuilder feiBuilder;
  //1. 注册卡片处理器
 

  // 2. 注入 ServletAdapter 示例
  @Autowired
  private ServletAdapter servletAdapter;

  //3. 注册服务路由
  @RequestMapping("/webhook/card/{appid}")
  public void card(@PathVariable String appid,HttpServletRequest request, HttpServletResponse response)
      throws Throwable {
    //3.1 回调扩展包卡片行为处理回调
	    CardActionHandler CARD_ACTION_HANDLER = CardActionHandler.newBuilder("v", "e",
		      new CardActionHandler.ICardHandler() {
		        @Override
		        public Object handle(CardAction cardAction) {
		          // 1.1 处理卡片行为
		          System.out.println(Jsons.DEFAULT.toJson(cardAction));
		          System.out.println(cardAction.getRequestId());

		          // 1.2 获取租户 key
		          String tenantKey = cardAction.getTenantKey();
		          // 发送请求
		          Client client=feiBuilder.getClient(appid);
		          try {
					client.im().message().create(CreateMessageReq.newBuilder()
					          .receiveIdType(ReceiveIdTypeEnum.OPEN_ID.getValue())
					          .createMessageReqBody(CreateMessageReqBody.newBuilder()
					              .content("text")
					              .build())
					          .build()
					      , RequestOptions.newBuilder()
					          .tenantKey(tenantKey)
					          .build());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		          return null;
		        }
		      }).build();
    servletAdapter.handleCardAction(request, response, CARD_ACTION_HANDLER);
  }
}
