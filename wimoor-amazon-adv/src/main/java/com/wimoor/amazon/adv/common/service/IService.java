package com.wimoor.amazon.adv.common.service;

import java.util.List;
import java.util.Map;

import com.wimoor.common.mvc.BizException;

 
public interface IService<T> {
	/**
	 * 通过主键查询
	 * @param key
	 * @return
	 */

	T selectByKey(Object key);
	
    public int insertUseGeneratedKeys(T entity) throws BizException;
    /**
     * 通过实体类插入数据
     * @param entity
     * @return
     */
    int save(T entity) throws BizException ;
    
    /**
     * 查询所有数据列表，不需要参数
     * @param null
     * @return
     */
    public List<T> selectAll(Object key) ;
    
    /**
     * 通过主键删除数据
     * @param key
     * @return
     */
    int delete(Object key);

    /**
     * 通过实体类更新所有数据
     * @param entity
     * @return
     */
    int updateAll(T entity) throws BizException;
    
    /**
     * 通过实体类更新数据，为空的字段不更新
     * @param entity
     * @return
     */
    int updateNotNull(T entity) throws BizException ;
 
    /**
     * 查找列名
     * @return
     */
    public Map<String,String> getColumnMap();
    
    /**
     * 按条件查询
     * @param example
     * @return
     */
    public List<T> selectByExample(Object example);
    public int deleteByExample(Object example);
    //TODO 其他...
}