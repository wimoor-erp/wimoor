package com.wimoor.finance.code.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wimoor.finance.code.mapper.FinCodeCacheMapper;
import com.wimoor.finance.code.domain.FinCodeCache;
import com.wimoor.finance.code.service.IFinCodeCacheService;

/**
 * 编码缓存Service业务层处理
 * 
 * @author wimoor
 * @date 2025-11-03
 */
@Service
public class FinCodeCacheServiceImpl implements IFinCodeCacheService 
{
    @Autowired
    private FinCodeCacheMapper finCodeCacheMapper;

    /**
     * 查询编码缓存
     * 
     * @param id 编码缓存主键
     * @return 编码缓存
     */
    @Override
    public FinCodeCache selectFinCodeCacheById(Long id)
    {
        return finCodeCacheMapper.selectFinCodeCacheById(id);
    }

    /**
     * 查询编码缓存列表
     * 
     * @param finCodeCache 编码缓存
     * @return 编码缓存
     */
    @Override
    public List<FinCodeCache> selectFinCodeCacheList(FinCodeCache finCodeCache)
    {
        return finCodeCacheMapper.selectFinCodeCacheList(finCodeCache);
    }

    /**
     * 新增编码缓存
     * 
     * @param finCodeCache 编码缓存
     * @return 结果
     */
    @Override
    public int insertFinCodeCache(FinCodeCache finCodeCache)
    {
        return finCodeCacheMapper.insertFinCodeCache(finCodeCache);
    }

    /**
     * 修改编码缓存
     * 
     * @param finCodeCache 编码缓存
     * @return 结果
     */
    @Override
    public int updateFinCodeCache(FinCodeCache finCodeCache)
    {
        return finCodeCacheMapper.updateFinCodeCache(finCodeCache);
    }

    /**
     * 批量删除编码缓存
     * 
     * @param ids 需要删除的编码缓存主键
     * @return 结果
     */
    @Override
    public int deleteFinCodeCacheByIds(Long[] ids)
    {
        return finCodeCacheMapper.deleteFinCodeCacheByIds(ids);
    }

    /**
     * 删除编码缓存信息
     * 
     * @param id 编码缓存主键
     * @return 结果
     */
    @Override
    public int deleteFinCodeCacheById(Long id)
    {
        return finCodeCacheMapper.deleteFinCodeCacheById(id);
    }
}
