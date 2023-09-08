package com.wimoor.amazon.product.service;

import com.wimoor.amazon.product.pojo.dto.ProductPriceDTO;
import com.wimoor.amazon.product.pojo.entity.ProductInOpt;
import com.wimoor.amazon.product.pojo.entity.ProductPrice;
import com.wimoor.amazon.product.pojo.vo.ProductPriceVo;
import com.wimoor.common.user.UserInfo;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 产品信息 服务类
 * </p>
 *
 * @author wimoor team
 * @since 2022-05-27
 */
public interface IProductInOptService extends IService<ProductInOpt> {

	void refreshAllProductAdv();
	public  IPage<ProductPriceVo>  priceQueue(ProductPriceDTO dto);
	public List<Map<String, Object>> findMaterialSizeByCondition(Map<String, Object> param);
	public List<Map<String, Object>> getProRemarkHis(String pid,String ftype);
	public List<ProductPrice> findPrice(String pid);
	public List<Map<String, Object>> saveTagsByPid(String pid,String tagids,String userid);
	String findProductTagsByPid(String pid);
	String findOwnerById(String pid);
	String updateOwnerById(UserInfo user,String pid, String ownerid);
	public String saveformulaData(UserInfo user, String formuladata);
	void setExcelMskuBook(SXSSFWorkbook workbook, Map<String, Object> param);
	public void uploadMskuFile(UserInfo user, InputStream inputStream, Row info) ;
	void updateProductOwner(UserInfo user, String msku, String owner, String oldowner);
	ProductInOpt getCacheableById(Serializable id);
}
