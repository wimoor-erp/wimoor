package com.wimoor.common.mybatis;

import org.apache.ibatis.executor.Executor;

import org.apache.ibatis.mapping.BoundSql;

import org.apache.ibatis.mapping.MappedStatement;

import org.apache.ibatis.mapping.SqlSource;

import org.apache.ibatis.plugin.*;

import org.apache.ibatis.session.ResultHandler;

import org.apache.ibatis.session.RowBounds;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Setter;

import java.util.*;

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
	private String erp; 
	@Setter
	private String amazon; 
	@Setter
	private String admin; 
	@Setter
	private List<String> erpTable;
	@Setter
	private List<String> amazonTable;
	@Setter
	private List<String> adminTable;
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
		if (isReplaceTableName(sql)) {
			if(erp!=null&&!erp.trim().equals("")&&erpTable!=null&&erpTable.size()>0) {
				for ( String table : erpTable) {
					sql = sql.replace(table, erp+"."+table);
				}
			}
			if(amazon!=null&&!amazon.trim().equals("")&&amazonTable!=null&&amazonTable.size()>0) {
				for ( String table : amazonTable) {
					sql = sql.replace(table, amazon+"."+table);
				}
			}
			if(admin!=null&&!admin.trim().equals("")&&adminTable!=null&&adminTable.size()>0) {
				for ( String table : adminTable) {
					sql = sql.replace(table, admin+"."+table);
				}
			}
			//重新生成一个BoundSql对象
			BoundSql bs = new BoundSql(ms.getConfiguration(), sql, boundSql.getParameterMappings(), parameterObject);
			//重新生成一个MappedStatement对象
			MappedStatement newMs = copyMappedStatement(ms, new BoundSqlSqlSource(bs));
			//赋回给实际执行方法所需的参数中
			args[0] = newMs;
		}
		return invocation.proceed();
	}

	private boolean isReplaceTableName(String sql) {
		// TODO Auto-generated method stub
		if(erp!=null&&!erp.trim().equals("")&&erpTable!=null&&erpTable.size()>0&&!sql.contains(erp)) {
			for ( String table : erpTable) {
				if( sql.contains(table)) {
					return true;
				}
			}
		}
		if(amazon!=null&&!amazon.trim().equals("")&&amazonTable!=null&&amazonTable.size()>0&&!sql.contains(amazon)) {
			for ( String table : amazonTable) {
				if( sql.contains(table)) {
					return true;
				}
			}
		}
		if(admin!=null&&!admin.trim().equals("")&&adminTable!=null&&adminTable.size()>0&&!sql.contains(admin)) {
			for ( String table : adminTable) {
				if( sql.contains(table)) {
					return true;
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