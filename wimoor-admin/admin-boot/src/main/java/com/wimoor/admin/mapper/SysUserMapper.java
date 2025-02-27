package com.wimoor.admin.mapper;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.admin.pojo.dto.UserDTO;
import com.wimoor.admin.pojo.entity.SysUser;
import com.wimoor.admin.pojo.entity.SysUserWechatMP;
import com.wimoor.admin.pojo.vo.UserVO;
 

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
	BigInteger getShortUUID();
	
    Integer getDIFFReport();
    
	List<SysUser> findByAccountOrEmail(String account);

	List<Map<String, Object>> findExcludedUrls(String userid);

	String findShopIdByUserId(String id);

	Map<String, Object> findUserInfoById(String id);

	SysUserWechatMP getUserWechatMP(String openid);

	IPage<UserVO> listQuery(Page<?> page, UserDTO dto);
	
	List<Map<String, Object>> findOwnerAll(@Param("shopid")String shopid,@Param("search")String search);
	
	void mergeAccountData(@Param("fromuser")String shopid,@Param("touser")String search);
}
