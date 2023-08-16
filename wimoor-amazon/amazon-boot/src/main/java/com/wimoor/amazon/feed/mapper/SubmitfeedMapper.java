package com.wimoor.amazon.feed.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.feed.pojo.entity.Submitfeed;
 
@Mapper
public interface SubmitfeedMapper extends BaseMapper<Submitfeed> {

	void updateBysubmissionid(Submitfeed sub);

	List<Submitfeed> selectINPROGRESS(@Param("sellerid") String sellerid, @Param("marketplaceid") String marketplaceid);

	List<Submitfeed> selectLastOneHour(@Param("sellerid") String sellerid, @Param("feedtype") String feedtype);

}