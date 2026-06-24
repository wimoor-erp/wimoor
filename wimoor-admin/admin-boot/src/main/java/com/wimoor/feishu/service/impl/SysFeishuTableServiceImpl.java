package com.wimoor.feishu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.feishu.mapper.SysFeishuTableMapper;
import com.wimoor.feishu.pojo.entity.SysFeishuTable;
import com.wimoor.feishu.service.ISysFeishuTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysFeishuTableServiceImpl extends ServiceImpl<SysFeishuTableMapper, SysFeishuTable> implements ISysFeishuTableService {
    
    private final SysFeishuTableMapper sysFeishuTableMapper;
    
    @Override
    public List<String> getTypeList(String appId) {
        return sysFeishuTableMapper.getTypeList(appId);
    }
    
}
