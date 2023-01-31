package com.wimoor.erp.material.service;


import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
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
import com.wimoor.erp.material.pojo.entity.MaterialSupplier;
import com.wimoor.erp.material.pojo.entity.MaterialSupplierStepwise;
import com.wimoor.erp.material.pojo.vo.MaterialConsumableVO;
import com.wimoor.erp.material.pojo.vo.MaterialInfoVO;
import com.wimoor.erp.material.pojo.vo.MaterialSupplierVO;
import com.wimoor.erp.material.pojo.vo.MaterialVO;
import com.wimoor.erp.ship.pojo.dto.ConsumableOutFormDTO;

public interface IMaterialService extends IService<Material> {
	MaterialVO findMaterialById(String id);

	boolean saveMark(String materialid, String type, String content, String userid) throws ERPBizException;

	IPage<Map<String, Object>> findByCondition(Page<?> page,Map<String, Object> map, MaterialDTO dto);
	public boolean delete(String id) ;
	String getNotice(String id);

	List<Material> selectAllSKUForSelect(String sku, String shopid);

	public List<MaterialCategory> selectAllCateByShopid(String shopid);

	void saveAllInfo(MaterialInfoVO vo, MultipartFile file, UserInfo userinfo) throws  ERPBizException;

	Map<String, Object> findDimAndAsinBymid(String sku, String shopid, String marketplaceid, String groupid);

	public List<Map<String, Object>> getForSum(String shopid,String groupid);

	String getImage(Material material);

	List<Map<String, Object>> selectAllSKUForLabel(String sku, String shopid);

	public Material findBySKU(String sku, String shopid);

	Map<String, BigDecimal> findDimensionsInfoBySKU(String value, String shopid);

	List<Map<String,Object>> getOwnerList(String shopid);
	
	Map<String,Object>  findMaterialMapBySku(String sku, String shopid);
	
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

	List<MaterialConsumableVO> selectConsumableByMainmid(String string,String shopid);

	List<MaterialSupplierVO> selectSupplierByMainmid(String string);
	
	List<Map<String, Object>> selectSupplerOtherById(String string);
	
	List<MaterialSupplier> selectSupplierByMaterialId(String id);
	public int deleteMaterialSupplierStepwise(String materialid,String supplierid);

	int saveMaterialSupplierStepwise(MaterialSupplierStepwise mss);

	List<Map<String, Object>> findConsumableDetailByShipment(String shopid, String shipmentid);
	
	public 	List<Map<String, Object>> selectConsumableByMainSKU(String shopid,String warehouseid,List<Map<String,Object>> skulist);
	  
	public List<Map<String, Object>> findConsumableDetailList(Map<String,Object> maps);

	List<Map<String, Object>> findConsumableHistory(String shopid, String shipmentid);

	List<Map<String, Object>> selectProPriceHisById(String id);

	List<Map<String, Object>> copyDimsForProduct(List<Map<String, Object>> list, UserInfo user);
	public List<MaterialConsumableVO> selectConsumableByMainmid(String id,String warehouseid ,String shopid) ;

	MaterialCustoms selectCustomsByMaterialId(String id);

	Map<String, Object> updateMaterialCustoms(String id, String addfee, String material,String ftype);

	public Map<String, Object> getRealityPrice(String materialid);

	int uploadMaterialImg(UserInfo userinfo, String materialid, InputStream inputStream, String originalFilename);

	List<Material> getMaterialByInfo(String shopid, String sku, String name);

	public List<String> getmskuList(List<String> list);

	public List<String> getTagsIdsListByMsku(String msku, String shopid);

	List<Map<String, Object>> saveTagsByMid(String mid, String ids, String userid);

	String findMaterialTagsByMid(String mid);

	public List<MaterialCustomsItem> selectCustomsItemListById(String id);

	 List<Map<String,Object>> findInventoryByMsku(PlanDTO dto);

	Material getBySku(String shopid, String sku);

	int saveInventoryConsumable(UserInfo user, ConsumableOutFormDTO dto);

	Map<String,String> getTagsIdsListByMsku(String shopid, List<String> mskulist);
}
