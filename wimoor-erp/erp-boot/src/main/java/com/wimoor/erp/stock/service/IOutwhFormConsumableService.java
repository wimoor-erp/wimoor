package com.wimoor.erp.stock.service;

import com.wimoor.erp.stock.pojo.entity.OutwhFormConsumable;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【t_erp_outwh_form_consumable】的数据库操作Service
* @createDate 2025-06-07 16:05:46
*/
public interface IOutwhFormConsumableService extends IService<OutwhFormConsumable> {

    void deleteByForm(String id);
}
