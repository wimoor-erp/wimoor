package com.wimoor.finance.code.mapper;

import java.util.List;
import com.wimoor.finance.code.domain.FinCodeCache;

/**
 * 编码缓存Mapper接口
 * 
 * @author wimoor
 * @date 2025-11-03
 */
public interface FinCodeCacheMapper 
{
    /**
     * 查询编码缓存
     * 
     * @param id 编码缓存主键
     * @return 编码缓存
     */
    public FinCodeCache selectFinCodeCacheById(Long id);

    /**
     * 查询编码缓存列表
     * 
     * @param finCodeCache 编码缓存
     * @return 编码缓存集合
     */
    public List<FinCodeCache> selectFinCodeCacheList(FinCodeCache finCodeCache);

    /**
     * 新增编码缓存
     * 
     * @param finCodeCache 编码缓存
     * @return 结果
     */
    public int insertFinCodeCache(FinCodeCache finCodeCache);

    /**
     * 修改编码缓存
     * 
     * @param finCodeCache 编码缓存
     * @return 结果
     */
    public int updateFinCodeCache(FinCodeCache finCodeCache);

    /**
     * 删除编码缓存
     * 
     * @param id 编码缓存主键
     * @return 结果
     */
    public int deleteFinCodeCacheById(Long id);

    /**
     * 批量删除编码缓存
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinCodeCacheByIds(Long[] ids);
}
