package com.wimoor.amazon.product.service;



import com.amazon.spapi.model.productpricing.OfferDetailList;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.product.pojo.entity.ProductFollow;

public interface IProductFollowHandlerService extends IService<ProductFollow> {
	public String getSellerName(String sellerid, String marketplaceid);
	void recordFollowOfferSellerName() throws InterruptedException;
	public void recordFollowOfferChange(OfferDetailList offers, String asin, String marketplaceid);
}
