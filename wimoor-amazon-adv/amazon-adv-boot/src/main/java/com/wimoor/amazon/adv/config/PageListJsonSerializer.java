package com.wimoor.amazon.adv.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author miemiedev
 */
@SuppressWarnings("rawtypes")
public class PageListJsonSerializer extends JsonSerializer<PageList> {
    ObjectMapper mapper;

    public PageListJsonSerializer(){
        mapper = new ObjectMapper();
    }

    public PageListJsonSerializer(ObjectMapper mapper){
        this.mapper = mapper;
    }
    @Override
    public void serialize(PageList value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        Map<String,Object> map = new HashMap<String, Object>();
        Paginator paginator = value.getPaginator();
        if(paginator!=null) {
       
        map.put("total", paginator.getTotalCount());
 
        map.put("totalPages", paginator.getTotalPages());
        
        map.put("page", paginator.getPage());
        
        map.put("limit", paginator.getLimit());
        map.put("records" ,  value.toArray() );

        map.put("startRow", paginator.getStartRow());
        map.put("endRow", paginator.getEndRow());

        map.put("offset", paginator.getOffset());

        map.put("slider", paginator.getSlider());

        map.put("prePage", paginator.getPrePage());
        map.put("nextPage", paginator.getNextPage());

        map.put("firstPage", paginator.isFirstPage());
        map.put("hasNextPage", paginator.isHasNextPage());
        map.put("hasPrePage", paginator.isHasPrePage());
        map.put("lastPage", paginator.isLastPage());
        }else {
        	  map.put("total", 0);
              map.put("totalPages", 0);
              map.put("page", 0);
              map.put("limit",10);
              map.put("records" ,new ArrayList<String>());
              map.put("startRow", 0);
              map.put("endRow", 0);
              map.put("offset",0);
              map.put("slider", 0);
              map.put("prePage", 0);
              map.put("nextPage", 0);
              map.put("firstPage", 0);
              map.put("hasNextPage", "false");
              map.put("hasPrePage", "false");
              map.put("lastPage",0);
        }
        mapper.writeValue(jgen, map);
    }
}
