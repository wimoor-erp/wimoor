package com.wimoor.amazon.product.service;



import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.product.pojo.entity.ProductFollow;

public interface IProductFollowHandlerService extends IService<ProductFollow> {
	public String getSellerName(String sellerid, String marketplaceid);
	public void sendProductFollowWxMsg(String sellerid);
	void recordFollowOfferSellerName() throws InterruptedException;
}
