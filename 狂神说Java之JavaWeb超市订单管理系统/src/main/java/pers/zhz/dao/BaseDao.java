package pers.zhz.dao;


import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * 操作数据库的公共类
 */
public class BaseDao {
    private static String driver;
    private static String url;
    private static String username;
    private static String password;

    /**
     * 静态代码块，类加载的时候初始化
     */
    static {
        Properties properties = new Properties();
        // 通过类加载器读取资源：资源转为流
        InputStream is = BaseDao.class.getClassLoader().getResourceAsStream("db.properties");
        try {
            properties.load(is);
            driver = properties.getProperty("driver");
            url = properties.getProperty("url");
            username = properties.getProperty("username");
            password = properties.getProperty("password");
            Class.forName(driver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据库连接
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 公共查询
     */
    public static ResultSet execute(Connection connection,String sql,PreparedStatement ps,Object[] params,ResultSet rs) throws SQLException {
        ps = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            // 占位符从1开始，数组从0开始
            ps.setObject(i+1,params[i]);
        }
        rs = ps.executeQuery();
        return rs;
    }

    /**
     * 公共增删改
     */
    public static int execute(Connection connection,String sql,PreparedStatement ps,Object[] params) throws SQLException {
        ps = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            // 占位符从1开始，数组从0开始
            ps.setObject(i+1,params[i]);
        }
        // 影响的行数
        int updateRows = ps.executeUpdate();
        return updateRows;
    }

    /**
     * 释放资源
     */
    public static boolean close(Connection connection,PreparedStatement ps,ResultSet rs) {
        boolean flag = true;
        if (rs != null) {
            try {
                rs.close();
                // GC回收
                rs = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }
        if (ps != null) {
            try {
                ps.close();
                // GC回收
                ps = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }
        if (connection != null) {
            try {
                connection.close();
                // GC回收
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }
        return flag;
    }
}
