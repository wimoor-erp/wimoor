package com.wimoor.sys.gc.service.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wimoor.sys.gc.config.GenerateProperties;
import com.wimoor.sys.gc.model.entity.Datasource;
import com.wimoor.sys.gc.model.vo.TableFieldVO;
import com.wimoor.sys.gc.model.vo.TableVO;
import com.wimoor.sys.gc.service.DataBaseService;
import com.wimoor.sys.gc.service.DatasourceService;
import com.wimoor.sys.gc.util.Base64Util;
import com.wimoor.sys.gc.util.JdbcPool;
import com.wimoor.sys.gc.util.PropUtil;
import com.wimoor.sys.gc.util.ValidUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * DataBase数据库操作，这里继承 BaseAdminServiceImpl 只是为了使用 mapper.dataBaseDao
 */
@Service
@Slf4j
public class DataBaseServiceImpl   implements DataBaseService {

    @Autowired
    private DatasourceService adminDatasourceService;
    @Autowired
    private GenerateProperties generateProperties;

    private String dbUrl;
    private String dbUserName;
    private String dbPassWord;


    private void init() {
        if (StringUtils.isEmpty(this.dbUrl)) {
            Object url = PropUtil.findByKey("spring.datasource.url");
            Object username = PropUtil.findByKey("spring.datasource.username");
            Object password = PropUtil.findByKey("spring.datasource.password");
            Object gcUrl = PropUtil.findByKey("gc.db.url");
            Object gcUsername = PropUtil.findByKey("gc.db.username");
            Object gcPassword = PropUtil.findByKey("gc.db.password");
            if (gcUrl != null && gcUsername != null && gcPassword != null) {
                dbUrl = gcUrl.toString();
                dbUserName = gcUsername.toString();
                dbPassWord = gcPassword.toString();
                log.info("代码生成: 当前数据源: {}", dbUrl);
            } else if (url != null && username != null && password != null) {
                dbUrl = url.toString();
                dbUserName = username.toString();
                dbPassWord = password.toString();
                log.info("代码生成: 当前数据源: {}", dbUrl);
            } else {
                log.warn("代码生成: 没有配置 gc 数据源[gc.db.url], 并且没有找到默认数据源[spring.datasource.url] 相关配置");
            }
        }
    }




    @Override
    public List<TableVO> findTable(String dataSourceId) {
        this.init();
        return this.findJdbcTable(dataSourceId);
    }

    /**
     * 查询数据库下指定表的数据-字段名/类型/备注
     *
     */
    @Override
    public List<TableFieldVO> findTableField(String table, String dataSourceId) {
        this.init();
        return this.findJdbcTableField(table, dataSourceId);
        // return dataBaseMapper.findTableField(getDbName());
    }


    /**
     * 使用jdbc 查询使用数据表
     *
     * @return java.util.List<io.github.wslxm.others.generatecode.model.vo.TableVO>
     */
    private List<TableVO> findJdbcTable(String dataSourceId) {
        this.init();
        Datasource datasource = adminDatasourceService.getById(dataSourceId);
        // 1、判断使用默认数据源还是动态数据源来获取数据库名称
        String dbName = getDbName(dataSourceId, datasource);
        // 2、拼接 sql
        String sql = "SELECT TABLE_NAME ,TABLE_COMMENT FROM information_schema.TABLES WHERE table_schema='" + dbName + "'";
        // 3、判断使用默认数据源还是动态数据源来执行sql
        PreparedStatement pstmt = getPreparedStatement(dataSourceId, datasource, sql);
        // 4、处理返回sql
        ResultSet rs = null;
        List<TableVO> vos = new ArrayList<>();
        try {
            rs = pstmt.executeQuery();
            //游标向下移动
            while (rs.next()) {
                //获得查询出来的数据
                TableVO vo = new TableVO();
                vo.setName(rs.getString("TABLE_NAME"));
                vo.setComment(rs.getString("TABLE_COMMENT"));
                vos.add(vo);
            }
        } catch (SQLException e) {
            log.error(e.toString());
        } finally {
            JdbcPool.closeQueryRes(rs);
        }
        return vos;
    }


    /**
     * 使用jdbc  查询数据库下指定表的数据-字段名/类型/备注
     */
    private List<TableFieldVO> findJdbcTableField(String table, String dataSourceId) {
        Datasource datasource = adminDatasourceService.getById(dataSourceId);
        // 1、判断使用默认数据源还是动态数据源来获取数据库名称
        String dbName = getDbName(dataSourceId, datasource);
        // 2、拼接sql
        String sql = "select " +
                " column_name name," +
                " data_type type," +
                " column_comment `desc`," +
                " column_type typeDetail," +
                " is_nullable as isNull," +
                " column_default as defaultVal " +
                " from information_schema.columns " +
                " where " +
                " table_name = '" + table + "'" +
                " and table_schema= '" + dbName + "'" +
                " order by ordinal_position asc";
        // 3、判断使用默认数据源还是动态数据源来执行sql
        PreparedStatement pstmt = getPreparedStatement(dataSourceId, datasource, sql);
        ResultSet rs = null;
        List<TableFieldVO> vos = new ArrayList<>();
        try {
            rs = pstmt.executeQuery();
            //游标向下移动
            while (rs.next()) {
                // 获得查询出来的数据
                TableFieldVO vo = new TableFieldVO();
                vo.setName(rs.getString("name"));
                vo.setType(rs.getString("type"));
                vo.setDesc(rs.getString("desc"));
                vo.setTypeDetail(rs.getString("typeDetail"));
                vo.setDefaultVal(rs.getString("defaultVal"));
                vo.setIsNull(rs.getString("isNull"));
                vos.add(vo);
            }
        } catch (SQLException e) {
            log.error(e.toString());
        } finally {
            JdbcPool.closeQueryRes(rs);
        }
        if (datasource != null && StringUtils.isNotBlank(datasource.getBaseFields())) {
            // 使用数据源配置的通用字段 (当使用了其他数据源和配置了通用字段时使用)
            List<String> fields = Arrays.asList(datasource.getBaseFields().split(","));
            for (TableFieldVO tableFieldVO : vos) {
                // 判断是否为通用字段
                if (fields.contains(tableFieldVO.getName())) {
                    tableFieldVO.setIsChecked(false);
                } else {
                    tableFieldVO.setIsChecked(true);
                }
                // 判断空串
                if ("CURRend_timeSTAMP".equals(tableFieldVO.getDefaultVal())) {
                    tableFieldVO.setDefaultVal("当前时间");
                }
            }
        } else {
            // 获取通用字段
            String basefields = generateProperties.getBasefields();
            List<String> basefieldsList = Arrays.asList(basefields.split(","));
            // 使用默认通用字段
            for (TableFieldVO tableFieldVO : vos) {
                // 判断是否为通用字段
                if (basefieldsList.contains(tableFieldVO.getName())) {
                    tableFieldVO.setIsChecked(false);
                } else {
                    tableFieldVO.setIsChecked(true);
                }
                // 判断空串
                if ("CURRend_timeSTAMP".equals(tableFieldVO.getDefaultVal())) {
                    tableFieldVO.setDefaultVal("当前时间");
                }
            }
        }
        return vos;
    }


    private String getDbName(String dataSourceId, Datasource datasource) {
        String dbName = "";
        if (StringUtils.isBlank(dataSourceId)) {
            ValidUtil.isTrue(StringUtils.isBlank(dbUrl), "在多数据源下请配置 gc.db.url 参数");
            // 使用 yml 配置的数据源
            dbUrl = dbUrl.replace("jdbc:mysql://", "");
            int index = dbUrl.indexOf("?");
            if (index == -1) {
                dbName = dbUrl.substring(dbUrl.lastIndexOf("/") + 1);
            } else {
                dbName = dbUrl.substring(dbUrl.lastIndexOf("/") + 1, index);
            }
        } else {
            // 使用动态选择的数据源
            dbName = datasource.getDbName();
        }
        return dbName;
    }

    private PreparedStatement getPreparedStatement(String dataSourceId, Datasource datasource, String sql) {
        PreparedStatement pstmt = null;
        if (StringUtils.isBlank(dataSourceId)) {
            ValidUtil.isTrue(StringUtils.isBlank(dbUrl), "在多数据源下请配置 gc.db.url 参数");
            ValidUtil.isTrue(StringUtils.isBlank(dbUserName), "没有配置数据源账号");
            ValidUtil.isTrue(StringUtils.isBlank(dbPassWord), "没有配置数据源密码");
            int index = dbUrl.indexOf("?");
            if (index == -1) {
                pstmt = JdbcPool.getPstmt(dbUrl, dbUserName, dbPassWord, sql);
            } else {
                pstmt = JdbcPool.getPstmt(dbUrl.substring(0, index), dbUserName, dbPassWord, sql);
            }
        } else {
            pstmt = JdbcPool.getPstmt(datasource.getDbUrl(),
                    datasource.getDbUsername(),
                    Base64Util.decrypt(datasource.getDbPassword()),
                    sql);
        }
        return pstmt;
    }
 
}
