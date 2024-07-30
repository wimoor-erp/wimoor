package com.wimoor.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.common.pojo.entity.Picture;
@Mapper
public interface PictureMapper extends BaseMapper<Picture>{
	List<Picture> selectByImageName(String location);
}