package com.wimoor.amazon.orders.service;

import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.common.user.UserInfo;

public interface IAmzOrderSolicitationsService {
	/**
	 * 获取订单的Review，调用此API将向买家发送评论邀请
	 * @param auth
	 * @param orderid
	 * @param marketplaceid
	 */
   public void createProductReviewAndSellerFeedbackSolicitation(  UserInfo userinfo,AmazonAuthority auth,String orderid,String marketplaceid);
}
