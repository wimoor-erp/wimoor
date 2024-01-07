package com.wimoor.amazon.adv.config;

import org.apache.ibatis.executor.Executor;

import org.apache.ibatis.mapping.BoundSql;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;

import org.apache.ibatis.plugin.*;

import org.apache.ibatis.session.ResultHandler;

import org.apache.ibatis.session.RowBounds;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Setter;

import java.util.*;
import java.util.Map.Entry;

/**
* @description: 动态替换表名***
*/
//method = "query"拦截select方法、而method = "update"则能拦截insert、update、delete的方法
@Intercepts({
		        @Signature(type = Executor.class, method = "query", 
				  args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class }) 
})
@Component
@ConfigurationProperties(prefix = "db")
public class ReplaceTableInterceptor  implements Interceptor  {
	@Setter
	private List<String> otherTables;

	Map<String,HashSet<String>> db_table_map=null;
	
	private Map<String,HashSet<String>> getMap() {
	   if(db_table_map==null) {
		   db_table_map=new HashMap<String,HashSet<String>>();
		   if(otherTables!=null&&otherTables.size()>0) {
			   for(String replaceTable:otherTables) {
				   if(replaceTable!=null) {
					   String[] dbtable = replaceTable.split("\\.");
					   if(dbtable.length==2) {
						   String db=dbtable[0];
						   String table=dbtable[1];
						   HashSet<String> dbtablelist = db_table_map.get(db);  
						   if(dbtablelist==null) {
							   dbtablelist=new HashSet<String>();
						   }
						   dbtablelist.add(table);
						   db_table_map.put(db,dbtablelist);  
					   }
				   }
			   }
		   }
	   }
	   return db_table_map;
	}
	
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object[] args = invocation.getArgs();
		//获取MappedStatement对象
		MappedStatement ms = (MappedStatement) args[0];
		//获取传入sql语句的参数对象
		Object parameterObject = args[1];
		BoundSql boundSql = ms.getBoundSql(parameterObject);
		//获取到拥有占位符的sql语句
		String sql = boundSql.getSql();
        //判断是否需要替换表名
		Map<String, HashSet<String>> map = getMap();
		if (map!=null&&map.size()>0&&isReplaceTableName(sql)) {
			for(Entry<String, HashSet<String>> entry:map.entrySet()) {
				  String db_name=entry.getKey();
				  HashSet<String> tables = entry.getValue();
				if(db_name!=null&&!db_name.trim().equals("")&&tables!=null&&tables.size()>0) {
					for ( String table : tables) {
						sql = sql.replace(table+" ", db_name+"."+table+" ");
					}
				}
			}
			 
			//重新生成一个BoundSql对象
			BoundSql bs = new BoundSql(ms.getConfiguration(), sql, boundSql.getParameterMappings(), parameterObject);
		    for (ParameterMapping mapping : boundSql.getParameterMappings()) {
	            String prop = mapping.getProperty();
	            if (boundSql.hasAdditionalParameter(prop)) {
	                bs.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
	            }
	        }
			//重新生成一个MappedStatement对象
			MappedStatement newMs = copyMappedStatement(ms, new BoundSqlSqlSource(bs));
			//赋回给实际执行方法所需的参数中
			args[0] = newMs;
		}
		return invocation.proceed();
	}

	private boolean isReplaceTableName(String sql) {
		// TODO Auto-generated method stub
		Map<String, HashSet<String>> map = getMap();
		if(map!=null&&map.size()>0) {
			for(Entry<String, HashSet<String>> entry:map.entrySet()) {
				 String db_name=entry.getKey();
				  HashSet<String> tables = entry.getValue();
				  if(db_name!=null&&!db_name.trim().equals("")&&tables!=null&&tables.size()>0&&!sql.contains(db_name)) {
					for ( String table : tables) {
						if( sql.contains(table)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) { }

	/***
	 * 
	 * 复制一个新的MappedStatement
	 * 
	 * @param ms
	 * 
	 * @param newSqlSource
	 * 
	 * @return
	 * 
	 */

	private MappedStatement copyMappedStatement(MappedStatement ms, SqlSource newSqlSource) {

		MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource,
				ms.getSqlCommandType());

		builder.resource(ms.getResource());

		builder.fetchSize(ms.getFetchSize());

		builder.statementType(ms.getStatementType());

		builder.keyGenerator(ms.getKeyGenerator());

		if (ms.getKeyProperties() != null && ms.getKeyProperties().length > 0) {

			builder.keyProperty(String.join(",", ms.getKeyProperties()));

		}

		builder.timeout(ms.getTimeout());

		builder.parameterMap(ms.getParameterMap());

		builder.resultMaps(ms.getResultMaps());

		builder.resultSetType(ms.getResultSetType());

		builder.cache(ms.getCache());

		builder.flushCacheRequired(ms.isFlushCacheRequired());

		builder.useCache(ms.isUseCache());

		return builder.build();

	}

	/***
	 * 
	 * MappedStatement构造器接受的是SqlSource
	 * 
	 * 实现SqlSource接口，将BoundSql封装进去
	 * 
	 */
	public static class BoundSqlSqlSource implements SqlSource {
		private BoundSql boundSql;

		public BoundSqlSqlSource(BoundSql boundSql) {
			this.boundSql = boundSql;
		}

		@Override
		public BoundSql getBoundSql(Object parameterObject) {
			return boundSql;
		}

	}

}