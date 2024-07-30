package com.wimoor.common.mvc;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.wimoor.common.result.Result;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;

public class DictSerializer extends StdSerializer<Object> implements ContextualSerializer {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3760186239175316241L;
	/** 字典注解 */
    private Dict dict;
    
    public DictSerializer() {
        super(Object.class);
    }
 
    
    public DictSerializer(Dict dict) {
        super(Object.class);
        this.dict = dict;
    }
    private String dictCode;
    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (Objects.isNull(value)) {
            gen.writeObject(value);
            return;
        }
    	System.out.println("serialize---"+value);
        if (Objects.nonNull(dict)){
        	dictCode = dict.dictCode();
        }
        // 通过数据字典类型和value获取name

        gen.writeObject(value);
        gen.writeFieldName(gen.getOutputContext().getCurrentName()+"Name");
        Object label=changeLabel(dictCode,value.toString());
        gen.writeObject(label);
    }
    
    private static String changeLabel(String type,String code) {
        if(code.indexOf(",") > -1) {
            String[] strs = code.split(",");
            if (strs.length > 1) {
                StringBuilder sb = new StringBuilder();
                for (String str : strs) {
                    // 从缓存中获取字典。如果不行，通过SpringUtil.getBean(); 获取服务处理
                    sb.append(getDictDataOptions(type, str)).append(",");
                }
                return sb.substring(0, sb.length() - 1);
            }
        }
        // 从缓存中获取字典。如果不行，通过SpringUtil.getBean(); 获取服务处理
        return getDictDataOptions(type, code);
    }
    
    @SuppressWarnings("null")
	@Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty beanProperty) throws JsonMappingException {
        if (Objects.isNull(beanProperty)){
            return prov.findValueSerializer(beanProperty.getType(), beanProperty);
        }
        Dict dict = beanProperty.getAnnotation(Dict.class);
        if (Objects.nonNull(dict)){
        	dictCode = dict.dictCode();
            return this;
        }
        return prov.findNullValueSerializer(null);
    }

    public static String getDictDataOptions(String typeCode,String value) {
    	RedisTemplate<String, Object> redisTemplate=SpringUtil.getBean("redisTemplate");
    	if(redisTemplate==null) {
    		return null;
    	}
        if (redisTemplate.hasKey("dict:"+typeCode+":"+value)){
            return (String) redisTemplate.opsForValue().get("dict:"+typeCode+":"+value);
        }
        RestTemplate restTemplate=SpringUtil.getBean("restTemplateApi");
     	if(restTemplate==null) {
    		return null;
    	}
        Result<?> result =null;
        try {
        	result= restTemplate.getForObject("http://wimoor-admin/admin/api/v1/dict-items/select_list/"+typeCode,Result.class);
        }catch(Exception e) {
        	e.printStackTrace();
        }
       
        if(result!=null&&Result.isSuccess(result)&&result.getData()!=null) {
        	List<?> dictDataList = (List<?>) result.getData();
            if(CollUtil.isNotEmpty(dictDataList)) {
            	 for (Object dictDataOptions : dictDataList) {
            		 Map<String, Object> map = BeanUtil.beanToMap(dictDataOptions);
            		 if(map!=null&&map.get("value")!=null)
                     redisTemplate.opsForValue().set("dict:"+typeCode+":"+map.get("value"),map.get("name"));
                 }
            }
            if (redisTemplate.hasKey("dict:"+typeCode+":"+value)){
                return (String) redisTemplate.opsForValue().get("dict:"+typeCode+":"+value);
            }
        }
        
        return null;
    }
}