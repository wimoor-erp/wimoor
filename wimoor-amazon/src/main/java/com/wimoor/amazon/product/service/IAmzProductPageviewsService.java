package com.wimoor.amazon.product.service;

 
import com.wimoor.amazon.product.pojo.dto.AmzProductPageviewsDTO;
import com.wimoor.amazon.product.pojo.entity.AmzProductPageviews;
import com.wimoor.amazon.product.pojo.vo.AmzProductPageviewsVo;
import com.wimoor.common.user.UserInfo;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 流量报表 服务类
 * </p>
 *
 * @author wimoor team
 * @since 2022-06-28
 */
public interface IAmzProductPageviewsService extends IService<AmzProductPageviews>  {
	public String uploadSessionFile(UserInfo user, String data, String marketplaceid, String sellerid, Date day, String type);
	public String uploadSessionFile(String marketplaceid, String sellerid, String day, List<AmzProductPageviews> pagelist)  ;
	public void refreshDownload();
	public void refreshSummary();
	public IPage<AmzProductPageviewsVo> getPageViewsList(AmzProductPageviewsDTO dto);
	public List<AmzProductPageviews> findPageviews(Map<String, Object> param); 
}
