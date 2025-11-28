package com.wimoor.erp.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.erp.order.mapper.ErpOverseaCycleMapper;
import com.wimoor.erp.order.pojo.entity.ErpOverseaCycle;
import com.wimoor.erp.order.service.IErpOverseaCycleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ErpOverseaCycleServiceImpl extends ServiceImpl<ErpOverseaCycleMapper, ErpOverseaCycle> implements IErpOverseaCycleService {
}
