package com.wimoor.amazon.auth.service;

import com.wimoor.amazon.auth.pojo.entity.AmzAuthApiTimelimit;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 新版本SPI-API使用 服务类
 * </p>
 *
 * @author wimoor team
 * @since 2022-06-24
 */
public interface IAmzAuthApiTimelimitService extends IService<AmzAuthApiTimelimit> {
	public boolean update(AmzAuthApiTimelimit entity);
	AmzAuthApiTimelimit getApiLimit(String amazonauthid, String string);

}
