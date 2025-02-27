package com.wimoor.sys.gc.util;


import lombok.extern.slf4j.Slf4j;

import java.sql.*;

import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.ResultCode;

/**
 * Jdbc数据源连接工具类
 */
@Slf4j
public class JdbcPool {

    /**
     * 数据源
     * @return
     */
    public static Connection getConn(String url, String username, String password) {
        Connection conn = null;
        try {
            String driverClass = "com.mysql.cj.jdbc.Driver";
            String newUrl = "jdbc:mysql://"+url + "?useUnicode=true&characterEncoding=utf-8&useTimezone=true&serverTimezone=GMT%2B8";
            // 当数据库连接异常中断时，是否自动重新连接，不重连,直接进入异常(提示失败)
            newUrl += "&autoReconnect=false";
            String newUsername = username;
            String newPassword = password;
            // 注册驱动
            Class.forName(driverClass);
            conn = DriverManager.getConnection(newUrl, newUsername, newPassword);
        } catch (Exception e) {
            log.info("");
            //log.error(e.toString()); //CommunicationsException ip错误  | SQLNonTransientConnectionException 连接超时
            throw new BizException(ResultCode.GENERATE_CODE_JDBC_ERROR);
        }
        return conn;
    }

    /**
     * 连接数据源 - 写入sql
     *
     * @param sql
     * @return
     */
    public static PreparedStatement getPstmt(String url, String username, String password, String sql) {
        Connection conn = getConn(url, username, password);
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
        } catch (SQLException e) {
            log.error(e.toString());
        }
        return pstmt;
    }

    /**
     * 添加修改后关闭数据连接
     * @param ps
     */
    public static void closeUpdateRes(PreparedStatement ps) {
        if (ps != null) {
            try {
                Connection conn = ps.getConnection();
                ps.close();
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                log.error(e.toString());
            }
        }

    }

    /**
     * 查询后关闭连接
     *
     * @author wangsong
     * @date 2019年6月15日 下午1:26:50
     * @param rs
     */
    public static void closeQueryRes(ResultSet rs) {
        if (rs != null) {
            Statement pstmt;
            try {
                pstmt = rs.getStatement();
                if (pstmt != null) {
                    Connection conn = pstmt.getConnection();
                    rs.close();
                    pstmt.close();
                    if (conn != null) {
                        conn.close();
                    }
                }
            } catch (SQLException e) {
                log.error(e.toString());
            }
        }
    }
}

