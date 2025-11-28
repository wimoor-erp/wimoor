package com.wimoor.erp.stock.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.erp.stock.pojo.entity.OutwhFormConsumable;
import com.wimoor.erp.stock.service.IOutwhFormConsumableService;
import com.wimoor.erp.stock.mapper.OutwhFormConsumableMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【t_erp_outwh_form_consumable】的数据库操作Service实现
* @createDate 2025-06-07 16:05:46
*/
@Service
@RequiredArgsConstructor
public class OutwhFormConsumableServiceImpl extends ServiceImpl<OutwhFormConsumableMapper, OutwhFormConsumable>
    implements IOutwhFormConsumableService {

    @Override
    public void deleteByForm(String id) {
        LambdaQueryWrapper<OutwhFormConsumable> query=new LambdaQueryWrapper<>();
        query.eq(OutwhFormConsumable::getFormid,id);
        this.baseMapper.delete(query);
    }
}




