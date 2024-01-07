

package com.wimoor.erp.material.service;


import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.common.pojo.entity.ERPBizException;
import com.wimoor.erp.material.pojo.dto.MaterialDTO;
import com.wimoor.erp.material.pojo.dto.PlanDTO;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.pojo.entity.MaterialCategory;
import com.wimoor.erp.material.pojo.entity.MaterialCustoms;
import com.wimoor.erp.material.pojo.entity.MaterialCustomsItem;
import com.wimoor.erp.material.pojo.vo.MaterialConsumableVO;
import com.wimoor.erp.material.pojo.vo.MaterialInfoVO;
import com.wimoor.erp.material.pojo.vo.MaterialVO;

public interface IMaterialService extends IService<Material> {
	MaterialVO findMaterialById(String id);

	boolean saveMark(String materialid, String type, String content, String userid) throws ERPBizException;

	IPage<Map<String, Object>> findByCondition(Page<?> page,Map<String, Object> map);
	IPage<Map<String, Object>> findConsumableByCondition(Page<?> page,Map<String, Object> map);
	IPage<Map<String, Object>> findPackageByCondition(Page<?> page,Map<String, Object> map);
	List<Map<String, Object>> findPackageByCondition(Map<String, Object> map);
	public boolean delete(String id) ;
	String getNotice(String id);

	List<Material> selectAllSKUForSelect(String sku, String shopid);

	public List<MaterialCategory> selectAllCateByShopid(String shopid);

	String saveAllInfo(MaterialInfoVO vo, MultipartFile file, UserInfo userinfo) throws  ERPBizException;

	Map<String, Object> findDimAndAsinBymid(String sku, String shopid, String marketplaceid, String groupid);

	public List<Map<String, Object>> getForSum(String shopid,String groupid);

	String getImage(Material material);

	List<Map<String, Object>> selectAllSKUForLabel(String sku, String shopid);

	public Material findBySKU(String sku, String shopid);

	Map<String, BigDecimal> findDimensionsInfoBySKU(String value, String shopid);

	List<Map<String,Object>> getOwnerList(String shopid);
	
	Map<String,Object>  findMaterialMapBySku(Map<String,Object> param);
	
	List<String> findMarterialForColorOwner(String key, Map<String,Object> param);
	
	void logicalDeleteMaterial(UserInfo user, Material material);
	
	boolean updateReductionSKUMaterial(UserInfo user, String id, String sku);
	
	boolean updateCycle(UserInfo user, String id, int amount);

	int updateItemMaterialByType(String[] ids, String ftype, String value, UserInfo user);

	int updateItemMaterialByPrice(String[] ids, String priceMapList,UserInfo user);

	String getImageByMaterialid(String materialid);

	List<Map<String, Object>> selectAllMaterialByShop(Map<String, Object> map);

	void getExcelMaterialAllInfoReport(SXSSFWorkbook workbook, List<Map<String, Object>> list);
 
	List<Map<String, Object>> findSKUImageForProduct(Map<String, Object> param);
	
	public List<Map<String, Object>> copyImageForProduct(List<Map<String, Object>> list, UserInfo user);

	/**
	 * /查询t_erp_material中共用图片ID的图片列表
	 * @return
	 */
	List<Map<String, Object>> selectCommonImage();

	List<Material> selectByImage(String image);

	Material selectBySKU(String shopid, String sku);

	List<Map<String, Object>> findAllByCondition(Map<String, Object> map);

	List<Map<String, Object>> selectProPriceHisById(String id);

	List<Map<String, Object>> copyDimsForProduct(List<Map<String, Object>> list, UserInfo user);

	MaterialCustoms selectCustomsByMaterialId(String id);

	Map<String, Object> updateMaterialCustoms(String id, String addfee, String material,String ftype);

	public Map<String, Object> getRealityPrice(String materialid);

	List<Material> getMaterialByInfo(String shopid, String sku, String name);

	public List<String> getmskuList(List<String> list);

	public List<String> getTagsIdsListByMsku(String msku, String shopid);

	List<Map<String, Object>> saveTagsByMid(String mid, String ids, String userid);

	String findMaterialTagsByMid(String mid);

	public List<MaterialCustomsItem> selectCustomsItemListById(String id);

	public List<Map<String, Object>> findInventoryByMsku(PlanDTO dto,String key);

	Material getBySku(String shopid, String sku);

	Map<String,String> getTagsIdsListByMsku(String shopid, List<String> mskulist);

	Workbook setMaterialExcelBook(Workbook workbook, MaterialDTO dto, UserInfo userinfo);

	Map<String,Object> getMaterialInfoBySkuList(PlanDTO dto);

	String uploadMaterialImg(UserInfo userinfo, String materialid, InputStream inputStream, String filename,
			String oldpictureid);
	
	public void saveMaterialConsumable(List<MaterialConsumableVO> list,UserInfo user,String id);

	void updateMaterialType(UserInfo user, String ftype, List<MaterialVO> volist);

	void syncProductList(String shopid);

}