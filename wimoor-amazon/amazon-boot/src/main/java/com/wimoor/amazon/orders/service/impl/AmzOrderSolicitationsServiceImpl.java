package com.wimoor.amazon.orders.service.impl;

import java.util.Arrays;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazon.spapi.api.SolicitationsApi;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.Error;
import com.amazon.spapi.model.solicitations.CreateProductReviewAndSellerFeedbackSolicitationResponse;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.impl.ApiBuildService;
import com.wimoor.amazon.orders.mapper.AmzOrdersRemarkMapper;
import com.wimoor.amazon.orders.pojo.entity.AmzOrdersRemark;
import com.wimoor.amazon.orders.service.IAmzOrderSolicitationsService;
import com.wimoor.amazon.util.AmzUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;

import cn.hutool.core.util.StrUtil;
@Service
public class AmzOrderSolicitationsServiceImpl implements IAmzOrderSolicitationsService{
	@Autowired
	ApiBuildService apiBuildService;
	@Resource
	AmzOrdersRemarkMapper amzOrdersRemarkMapper;
	/**
	 * 获取订单的Review，调用此API将向买家发送评论邀请 
	 * @param auth
	 * @param orderid
	 * @param marketplaceid
	 */
   public void createProductReviewAndSellerFeedbackSolicitation(UserInfo userinfo,AmazonAuthority auth,String orderid,String marketplaceid) {
	   SolicitationsApi api = apiBuildService.getSolicitationsApi(auth);
		 String errormsg="";
		   try {
			 CreateProductReviewAndSellerFeedbackSolicitationResponse response = api.createProductReviewAndSellerFeedbackSolicitation(orderid, Arrays.asList(marketplaceid));
			 if(response.getErrors()!=null&&response.getErrors().size()>0) {
				 for(Error error:response.getErrors()) {
					 errormsg=errormsg+ error.getMessage();
				 }
			 }else {
				 AmzOrdersRemark remark = amzOrdersRemarkMapper.selectById(orderid);
				 if(remark!=null) {
					 remark.setReviewSendTime(new Date());
					 remark.setReviewSendOperator(userinfo.getId());
					 amzOrdersRemarkMapper.updateById(remark);
				 }else {
					 remark=new AmzOrdersRemark();
					 remark.setReviewSendTime(new Date());
					 remark.setAmazonOrderId(orderid);
					 remark.setReviewSendOperator(userinfo.getId());
					 amzOrdersRemarkMapper.insert(remark);
				 }
				 
			 }
		   } catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw AmzUtil.getException(e);
		}
		if(StrUtil.isNotBlank(errormsg)) {
			throw new BizException(errormsg);
		}
   }
}
