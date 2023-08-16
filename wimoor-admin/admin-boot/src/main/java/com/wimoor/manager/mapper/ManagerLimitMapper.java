package com.wimoor.manager.mapper;

import com.wimoor.manager.pojo.entity.ManagerLimit;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wimoor team
 * @since 2022-09-27
 */
@Mapper
public interface ManagerLimitMapper extends BaseMapper<ManagerLimit> {
    @Select("<script>" +
            "SELECT max(p.name) NAME,COUNT(l.id) qty,p.id FROM  t_manager_limit l "+
    		" LEFT JOIN t_sys_tariff_packages p ON p.id=l.tariffpackage "+
            " GROUP BY p.id order by p.id"+
            "</script>")
	List<Map<String, Object>> summary();

	IPage<Map<String, Object>> getCompanyList(Page<?> page,@Param("param") Map<String, Object> param);

}
