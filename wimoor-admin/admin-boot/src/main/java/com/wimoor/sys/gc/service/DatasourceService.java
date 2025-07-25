package com.wimoor.sys.gc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.sys.gc.model.core.BasePage;
import com.wimoor.sys.gc.model.entity.Datasource;
import com.wimoor.sys.gc.model.query.DatasourceQuery;
import com.wimoor.sys.gc.model.vo.DatasourceVO;

/**
 * 代码生成数据源维护表
 */
public interface DatasourceService extends IService<Datasource> {

    /**
     * 列表查询
     *
     * @param query query
     * @version 1.0.0
     */
    BasePage<DatasourceVO> findPage(DatasourceQuery query);

    /**
     * id查询
     *
     * @param id id
     * @version 1.0.0
     */
    DatasourceVO findId(String id);
}

