package com.wimoor.amazon.product.service;


import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.product.pojo.dto.ProductListDTO;
import com.wimoor.amazon.product.pojo.dto.ProductListQuery;
import com.wimoor.amazon.product.pojo.entity.ProductInfo;
import com.wimoor.amazon.product.pojo.entity.ProductInfoStatusDefine;
import com.wimoor.amazon.product.pojo.vo.AmzProductListVo;
import com.wimoor.amazon.product.pojo.vo.ProductInfoListVo;
import com.wimoor.common.user.UserInfo;

/**
 * <p>
 * 产品信息 服务类
 * </p>
 *
 * @author wimoor team
 * @since 2022-05-27
 */
public interface IProductInfoService extends IService<ProductInfo> {

	public List<String> getPidListByTagList(List<String> taglist,Object shopid,Object amazonAuthId,Object groupid,Object groupList,Object marketplace) ;
	
	List<ProductInfo> selectBySku(String sku, String marketplaceid, String amazonAuthId);

	ProductInfo productOnlyone(String amazonAuthId, String sku, String marketplaceid);

	List<Map<String, Object>> findShopSku(String shopid, String sku);

	List<ProductInfo> selectByAsin(String amazonAuthId, String asin, String marketplaceid);

	String getMSKU(String amazonAuthId, String marketplaceid, String sku);

	IPage<AmzProductListVo> getListByUser(UserInfo userinfo, ProductListQuery query, Map<String, Object> parameter, String key);

	IPage<ProductInfoListVo> findByCondition(ProductListDTO dto);
 
	Map<String, Object> findNameAndPicture(String sku_p, String marketplaceid, String groupid);

	List<ProductInfoStatusDefine> getProStatusList(String shopid);

	Map<String, Object> updateProductPrice(UserInfo user, Map<String, Object> map, String ftype);

	int updateUpdateRemark(String id, String remark, String ftype, String userid);

	Map<String, Object> saveProductPriceLocked(String pid, String userid, String price, String days);

	Map<String, Object> deleteProductPriceLocked(String pid, String userid);

	List<ProductInfo> selectByMSku(String sku, String marketplaceid,String groupid ,String shopid);

	void showProfitDetial(Map<String, Object> map);

	Map<String, Object> productSimpleInfoOnlyone(String amazonAuthId, String sku, String marketplaceid);

	public String findMSKUBySKUMarket(String psku, String marketplaceid, String id);

	public int productDisable(UserInfo user, String id);

	public int productUnDisable(UserInfo user, String id);

	public List<Map<String, Object>> productFollow(String followid);

	public IPage<Map<String, Object>> getProductInfoWithFnSKU(Page<Object> page, Map<String, Object> map);

	public IPage<Map<String, Object>> getAsinList(Page<?> page, Map<String, Object> parameter);
	
	public void optProductPrice(Map<String, Object> map);

	public List<Map<String, Object>> getInfoSimple( Map<String, Object> parameter);

	public boolean updateProInvlidByPid(String pid);
 
}
