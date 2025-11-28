package com.wimoor.amazon.base;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import com.wimoor.amazon.adv.common.service.IService;
import com.wimoor.common.mvc.BizException;

 

/**
 * Created by liufei on 2017-01-09
 */
@SuppressWarnings("rawtypes")
public abstract class BaseService<T> implements IService<T> {

	@Autowired(required = true)
	protected BaseMapper<T> mapper;

	@Autowired(required = true)
	protected SqlSession sqlSession;

	public BaseMapper<T> getMapper() {
		return mapper;
	}

	public T selectByKey(Object key) {
		return mapper.selectByPrimaryKey(key);
	}

	public List<T> selectAll(Object key) {
		return mapper.selectAll();
	}

 
 

	public int insertUseGeneratedKeys(T entity) throws BizException {
		ValidatorFactory factory;
		Validator validator;
		factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<T>> set = validator.validate(entity);
		for (ConstraintViolation<T> c : set) {
			throw new BizException(c.getMessage());
		}
		return this.mapper.insertUseGeneratedKeys(entity);
	}

	public int save(T entity) throws BizException {
		ValidatorFactory factory;
		Validator validator;
		factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<T>> set = validator.validate(entity);
		for (ConstraintViolation<T> c : set) {
			throw new BizException(c.getMessage());
		}
		return mapper.insert(entity);
	}

	public int delete(Object key) {
		return mapper.deleteByPrimaryKey(key);
	}

	public int updateAll(T entity) throws BizException {
		ValidatorFactory factory;
		Validator validator;
		factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<T>> set = validator.validate(entity);
		for (ConstraintViolation<T> c : set) {
			throw new BizException(c.getMessage());
		}
		return mapper.updateByPrimaryKey(entity);
	}

	public int updateNotNull(T entity) throws BizException {
		Method[] methods = entity.getClass().getMethods(); // 反射获取实体对象中的方法
		Field[] fields = entity.getClass().getDeclaredFields();
		ValidatorFactory factory;
		Validator validator;
		factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		for (Method method : methods) {
			String name = method.getName();
			if (name.indexOf("get") == 0) {
				for (Field field : fields) {
					String fieldname = field.getName();
					Object value = null;
					if (name.toUpperCase().contains(fieldname.toUpperCase())) {
						try {
							value = method.invoke(entity);
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
						if (value != null) {
							Set<ConstraintViolation<T>> set = validator.validateProperty(entity, fieldname);
							for (ConstraintViolation<T> c : set) {
								throw new BizException(c.getMessage());
							}
						}
					}
				}
			}
		}
		return mapper.updateByPrimaryKeySelective(entity);
	}

	public List<T> selectByExample(Object example) {
		List<T> list = mapper.selectByExample(example);
		return list;
	}


	public Class getGenericType(int index) {
		Type genType = getClass().getGenericSuperclass();
		if (!(genType instanceof ParameterizedType)) {
			return Object.class;
		}
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		if (index >= params.length || index < 0) {
			throw new RuntimeException("Index outof bounds");
		}
		if (!(params[index] instanceof Class)) {
			return Object.class;
		}
		return (Class) params[index];
	}

	public Map<String, String> getColumnMap() {
		SqlSession session = sqlSession;
		String table = null;
		Configuration config = session.getConfiguration();
		Class obj = getGenericType(0);
		Collection<ResultMap> map = config.getResultMaps();
		Iterator<ResultMap> mpite = map.iterator();
		ResultMap mp = null;
		while (mpite.hasNext()) {
			try {
				mp = mpite.next();
				if (mp.getType().equals(obj))
					break;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Annotation[] anno = mp.getType().getAnnotations();
		Annotation anno1 = anno[1];
		String annostr = anno1.toString();
		table = annostr.substring(annostr.indexOf("name") + 5, annostr.indexOf(","));

		Map<String, String> propertyMap = new LinkedHashMap<String, String>();
		Map<String, String> fieldName = new LinkedHashMap<String, String>();
		List<ResultMapping> rmap = mp.getPropertyResultMappings();
		for (int i = 0; i < rmap.size(); i++) {
			ResultMapping resultMap = rmap.get(i);
			propertyMap.put(resultMap.getColumn(), resultMap.getProperty());
		}
		PreparedStatement pstmt = null;
		ResultSet result = null;
		try {
			pstmt = sqlSession.getConnection().prepareStatement(
					"select COLUMN_NAME,column_comment from INFORMATION_SCHEMA.Columns where table_name='" + table + "'  ");
			result = pstmt.executeQuery();
			while (result.next()) {
				fieldName.put(propertyMap.get(result.getString("COLUMN_NAME")), result.getString("column_comment"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return fieldName;
	}

	public int deleteByExample(Object example) {
		return mapper.deleteByExample(example);
	}

}
