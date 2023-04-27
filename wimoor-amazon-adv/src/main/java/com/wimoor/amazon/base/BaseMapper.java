package com.wimoor.amazon.base;
 
 
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 继承自己的MyMapper
 *
 * @author felix_liu
 * @since 2016-01-09 17:53
 */
public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T>,InsertListWithKeyMapper<T>{

}
