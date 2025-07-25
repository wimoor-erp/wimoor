package com.wimoor.sys.gc.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.common.service.impl.BaseServiceImpl;
import com.wimoor.sys.gc.mapper.DatasourceMapper;
import com.wimoor.sys.gc.model.core.BasePage;
import com.wimoor.sys.gc.model.entity.Datasource;
import com.wimoor.sys.gc.model.query.DatasourceQuery;
import com.wimoor.sys.gc.model.vo.DatasourceVO;
import com.wimoor.sys.gc.service.DatasourceService;
import com.wimoor.sys.gc.util.BeanDtoVoUtil;

/**
 * 代码生成数据源维护表
 */
@Service
public class DatasourceServiceImpl extends BaseServiceImpl<DatasourceMapper, Datasource> implements DatasourceService {


    @Override
    public BasePage<DatasourceVO> findPage(DatasourceQuery query) {
        LambdaQueryWrapper<Datasource> queryWrapper = new LambdaQueryWrapper<Datasource>()
                .orderByDesc(Datasource::getCreateTime)
                .select(Datasource.class, info -> !"db_password".equals(info.getColumn()))
                .like(StringUtils.isNotBlank(query.getDbTitle()), Datasource::getDbTitle, query.getDbTitle())
                .like(StringUtils.isNotBlank(query.getDbName()), Datasource::getDbName, query.getDbName());
         return  BeanDtoVoUtil.pageVo(this.page(new Page<>(query.getCurrent(), query.getSize()), queryWrapper), DatasourceVO.class);
    }


    @Override
    public DatasourceVO findId(String id) {
        DatasourceVO vo = BeanDtoVoUtil.convert(this.getById(id), DatasourceVO.class);
        vo.setDbPassword(null);
        return vo;
    }
}
