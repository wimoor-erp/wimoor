package com.wimoor.amazon.base;


import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.miemiedev.mybatis.paginator.dialect.Dialect;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;
import com.github.miemiedev.mybatis.paginator.support.PropertiesHelper;
import com.github.miemiedev.mybatis.paginator.support.SQLHelp;
@SuppressWarnings({"rawtypes","unchecked"})
@Intercepts({@Signature(type= Executor.class,method = "query",args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class OffsetLimitInterceptor implements Interceptor{
    private static Logger logger = LoggerFactory.getLogger(OffsetLimitInterceptor.class);
	static int MAPPED_STATEMENT_INDEX = 0;
	static int PARAMETER_INDEX = 1;
	static int ROWBOUNDS_INDEX = 2;
	static int RESULT_HANDLER_INDEX = 3;

    static ExecutorService Pool;
    String dialectClass;
    boolean asyncTotalCount = false;
	
    private BoundSql setSQLUnion(MappedStatement ms, Object parameter,  Object[] queryArgs) {
 		   BoundSql boundSql = ms.getBoundSql(parameter);
		    List<ParameterMapping> nweparameterMappings=new ArrayList<ParameterMapping>();
        if(parameter!=null&&parameter instanceof Map) {
			Map<String,Object> param = (Map<String,Object>)parameter;
           if(param.containsKey(UnionTablesSuffix.key)) {
        	Object suffixobj=param.get(UnionTablesSuffix.key);
        	if(suffixobj!=null&&suffixobj instanceof UnionTablesSuffix) {
        		UnionTablesSuffix suffix=(UnionTablesSuffix)suffixobj;
        		    String newSqlSource="";
        		    if(suffix!=null&&suffix.size()>0) {
        		    	List<ParameterMapping> parameterMappings=boundSql.getParameterMappings();
        		    	String fieldsql=null;
        		    	if(suffix.isOnlyOneResult()&&suffix.size()>1) {
        		    		String sql = new String(boundSql.getSql());
    		    			String feldstr = sql.substring(sql.indexOf("select"), sql.indexOf("from"));
    		    			String[] fieldlist = feldstr.split(",");
    		    			for(int i=0;i<fieldlist.length;i++) {
	    		    				String field=fieldlist[i];
	    		    				if(StringUtils.isNoneBlank(field)) {
	    		    				field=field.trim();
	    		    				String fieldforif = field.trim().toLowerCase();
	    		    				if(fieldforif.contains("sum")||fieldforif.contains("count")) {
	    		    					field=field.substring(field.lastIndexOf(" "),field.length());
	    		    					field="sum("+field+") "+field;
	    		    				}else if(fieldforif.contains("max")) {
	    		    					field=field.substring(field.lastIndexOf(" "),field.length());
	    		    					field="max("+field+") "+field;
	    		    				}else if(fieldforif.contains("min")) {
	    		    					field=field.substring(field.lastIndexOf(" "),field.length());
	    		    					field="min("+field+") "+field;
	    		    				}else if(fieldforif.contains("avg")) {
	    		    					field=field.substring(field.lastIndexOf(" "),field.length());
	    		    					field="avg("+field+") "+field;
	    		    				} else {
	    		    					if(fieldforif.lastIndexOf(" ")>=0) {
	    		    						field=field.substring(field.lastIndexOf(" "),field.length());
	    		    					}else if(fieldforif.lastIndexOf(".")>=0)  {
	    		    						field=field.substring(field.lastIndexOf("."),field.length());
	    		    					}else {
	    		    						field=field.substring(0,field.length());
	    		    					}
	    		    				}
	    		    				if(fieldsql==null) {
	    		    					fieldsql="";
	    		    				}
	    		    				if(i==fieldlist.length-1) {
	    		    					fieldsql=fieldsql+field;
	    		    				}else {
	    		    					fieldsql=fieldsql+field+",";
	    		    				}
    		    				}
    		    			}
    		    		}
        		    	for(int i=0;i<suffix.size();i++) {
        		    		String sql = new String(boundSql.getSql());
        		    		  UnionItem tablesuffix = suffix.get(i);
        		    		if(tablesuffix!=null&&tablesuffix.getTableSuffixItem().size()>0) {
        		    			Map<String, String> tablesuffixmap = tablesuffix.getTableSuffixItem();
        		    			for(Entry<String, String> suffixentry:tablesuffixmap.entrySet()  ) {
        		    				if(StringUtils.isNotBlank(suffixentry.getKey())) {
        		    					if(!UnionTablesSuffix.self_table_suffix.equals(suffixentry.getValue())) {
        		    						sql=sql.replaceAll(suffixentry.getKey().trim()+" ", suffixentry.getKey()+suffixentry.getValue().trim()+" ");
        		    					}
        		    				}
        		    			}
        	        		    for(ParameterMapping item:parameterMappings) {
        	          		      org.apache.ibatis.mapping.ParameterMapping.Builder bd = new  ParameterMapping.Builder(ms.getConfiguration(),item.getProperty(),item.getJavaType());
        	          		      bd.jdbcType(item.getJdbcType());
        	          		      bd.expression(item.getExpression());
        	          		      bd.mode(item.getMode());
        	          		      nweparameterMappings.add(bd.build());
        	          		    }
        		    		}
        		    		if(i<suffix.size()-1) {
        		    			newSqlSource=newSqlSource+sql+" "+suffix.getUnionSql()+" ";
        		    		}else {
        		    			newSqlSource=newSqlSource+sql;
        		    		}
        		    	}
        		    	if(fieldsql!=null) {
        		    		newSqlSource="select "+fieldsql+" from ( "+newSqlSource+" ) v";
        		    	}
        		    }
        		       // 重新new一个查询语句对像
        	


        	        BoundSql newBoundSql = copyFromBoundSqlNewParameter(ms, boundSql, newSqlSource,  nweparameterMappings, boundSql.getParameterObject());
        	        queryArgs[MAPPED_STATEMENT_INDEX] = copyFromMappedStatement(ms, new BoundSqlSqlSource(newBoundSql));
        	        return newBoundSql;
        	}
         }
        }
        return boundSql;
    }

	public Object intercept(final Invocation invocation) throws Throwable {
        final Executor executor = (Executor) invocation.getTarget();
          Object[] queryArgs_temp = invocation.getArgs();
        MappedStatement ms_temp = (MappedStatement)queryArgs_temp[MAPPED_STATEMENT_INDEX];
        final Object parameter = queryArgs_temp[PARAMETER_INDEX];
        final RowBounds rowBounds = (RowBounds)queryArgs_temp[ROWBOUNDS_INDEX];
        final PageBounds pageBounds = new PageBounds(rowBounds);
        String classname = "";
        Class cls = parameter!=null?parameter.getClass():null;
        classname=cls!=null?cls.getName():"";
        final  BoundSql boundSql = setSQLUnion(ms_temp,parameter,queryArgs_temp);
        final MappedStatement ms = (MappedStatement)queryArgs_temp[MAPPED_STATEMENT_INDEX];
        final  Object[] queryArgs=queryArgs_temp;
        if(pageBounds.getOffset() == RowBounds.NO_ROW_OFFSET
                && pageBounds.getLimit() == RowBounds.NO_ROW_LIMIT
                && pageBounds.getOrders().isEmpty()||(classname!=null&&classname.contains("tk.mybatis.mapper"))){
            return invocation.proceed();
        }

        final Dialect dialect;
        try {
            Class clazz = Class.forName(dialectClass);
            Constructor constructor = clazz.getConstructor(MappedStatement.class, Object.class, PageBounds.class);
            dialect = (Dialect)constructor.newInstance(new Object[]{ms, parameter, pageBounds});
        } catch (Exception e) {
            throw new ClassNotFoundException("Cannot create dialect instance: "+dialectClass,e);
        }

 
        queryArgs[MAPPED_STATEMENT_INDEX] = copyFromNewSql(ms,boundSql,dialect.getPageSQL(), dialect.getParameterMappings(), dialect.getParameterObject());
        queryArgs[PARAMETER_INDEX] = dialect.getParameterObject();
        queryArgs[ROWBOUNDS_INDEX] = new RowBounds(RowBounds.NO_ROW_OFFSET,RowBounds.NO_ROW_LIMIT);

        Boolean async = pageBounds.getAsyncTotalCount() == null ? asyncTotalCount : pageBounds.getAsyncTotalCount();
        Future<List> listFuture = call(new Callable<List>() {
            public List call() throws Exception {
                return (List)invocation.proceed();
            }
        }, async);


        if(pageBounds.isContainsTotalCount()){
    
			Callable<Paginator> countTask = new Callable() {
                public Object call() throws Exception {
                    Integer count;
                    Cache cache = ms.getCache();
                    if(cache != null && ms.isUseCache() && ms.getConfiguration().isCacheEnabled()){
                        CacheKey cacheKey = executor.createCacheKey(ms,parameter,new PageBounds(),copyFromBoundSql(ms,boundSql,dialect.getCountSQL(), boundSql.getParameterMappings(), boundSql.getParameterObject()));
                        count = (Integer)cache.getObject(cacheKey);
                        if(count == null){
                            count = SQLHelp.getCount(ms,executor.getTransaction(),parameter,boundSql,dialect);
                            cache.putObject(cacheKey, count);
                        }
                    }else{
                        count = SQLHelp.getCount(ms,executor.getTransaction(),parameter,boundSql,dialect);
                    }
                    return new Paginator(pageBounds.getPage(), pageBounds.getLimit(), count);
                }
            };
            Future<Paginator> countFutrue = call(countTask, async);
            return new PageList(listFuture.get(),countFutrue.get());
        }

        return listFuture.get();
	}

    private <T> Future<T> call(Callable callable, boolean async){
        if(async){
             return Pool.submit(callable);
        }else{
            FutureTask<T> future = new FutureTask(callable);
            future.run();
            return future;
        }
    }

    private MappedStatement copyFromNewSql(MappedStatement ms, BoundSql boundSql,
                                           String sql, List<ParameterMapping> parameterMappings, Object parameter){
        BoundSql newBoundSql = copyFromBoundSql(ms, boundSql, sql, parameterMappings, parameter);
        return copyFromMappedStatement(ms, new BoundSqlSqlSource(newBoundSql));
    }

	private BoundSql copyFromBoundSql(MappedStatement ms, BoundSql boundSql,
			String sql, List<ParameterMapping> parameterMappings,Object parameter) {
		BoundSql newBoundSql = new BoundSql(ms.getConfiguration(),sql, parameterMappings, parameter);
		for (ParameterMapping mapping : boundSql.getParameterMappings()) {
		    String prop = mapping.getProperty();
		    if (boundSql.hasAdditionalParameter(prop)) {
		        newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
		    }
		}
		return newBoundSql;
	}
	
	private BoundSql copyFromBoundSqlNewParameter(MappedStatement ms, BoundSql boundSql,
			String sql, List<ParameterMapping> parameterMappings,Object parameter) {
		BoundSql newBoundSql = new BoundSql(ms.getConfiguration(),sql, parameterMappings, parameter);
		for (ParameterMapping mapping : parameterMappings) {
		    String prop = mapping.getProperty();
		    if (boundSql.hasAdditionalParameter(prop)) {
		        newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
		    }
		}
		return newBoundSql;
	}
	//see: MapperBuilderAssistant
	private MappedStatement copyFromMappedStatement(MappedStatement ms,SqlSource newSqlSource) {
		Builder builder = new Builder(ms.getConfiguration(),ms.getId(),newSqlSource,ms.getSqlCommandType());
		
		builder.resource(ms.getResource());
		builder.fetchSize(ms.getFetchSize());
		builder.statementType(ms.getStatementType());
		builder.keyGenerator(ms.getKeyGenerator());
		if(ms.getKeyProperties() != null && ms.getKeyProperties().length !=0){
            StringBuffer keyProperties = new StringBuffer();
            for(String keyProperty : ms.getKeyProperties()){
                keyProperties.append(keyProperty).append(",");
            }
            keyProperties.delete(keyProperties.length()-1, keyProperties.length());
			builder.keyProperty(keyProperties.toString());
		}
		
		//setStatementTimeout()
		builder.timeout(ms.getTimeout());
		
		//setStatementResultMap()
		builder.parameterMap(ms.getParameterMap());
		
		//setStatementResultMap()
        builder.resultMaps(ms.getResultMaps());
		builder.resultSetType(ms.getResultSetType());
	    
		//setStatementCache()
		builder.cache(ms.getCache());
		builder.flushCacheRequired(ms.isFlushCacheRequired());
		builder.useCache(ms.isUseCache());
		
		return builder.build();
	}

	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	public void setProperties(Properties properties) {
        PropertiesHelper propertiesHelper = new PropertiesHelper(properties);
		String dialectClass = propertiesHelper.getRequiredString("dialectClass");
		setDialectClass(dialectClass);

        setAsyncTotalCount(propertiesHelper.getBoolean("asyncTotalCount",false));

        setPoolMaxSize(propertiesHelper.getInt("poolMaxSize",0));

	}
	
	public static class BoundSqlSqlSource implements SqlSource {
		BoundSql boundSql;
		public BoundSqlSqlSource(BoundSql boundSql) {
			this.boundSql = boundSql;
		}
		public BoundSql getBoundSql(Object parameterObject) {
			return boundSql;
		}
	}

    public void setDialectClass(String dialectClass) {
        logger.debug("dialectClass: {} ", dialectClass);
        this.dialectClass = dialectClass;
    }

    public void setAsyncTotalCount(boolean asyncTotalCount) {
        logger.debug("asyncTotalCount: {} ", asyncTotalCount);
        this.asyncTotalCount = asyncTotalCount;
    }

    public void setPoolMaxSize(int poolMaxSize) {

        if(poolMaxSize > 0){
            logger.debug("poolMaxSize: {} ", poolMaxSize);
            Pool = Executors.newFixedThreadPool(poolMaxSize);
        }else{
            Pool = Executors.newCachedThreadPool();
        }


    }
}
