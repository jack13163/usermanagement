package jack.smartbi.util;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 数据访问帮助类
 */
public class DBUtil {
    private static String driver;
    private static String url;
    private static String username;
    private static String password;

    //使用静态代码块，在类加载时即完成对属性文件的读取
    static {
        //动态获取属性配置文件的流对象
        InputStream in = DBUtil.class.getResourceAsStream("../db.properties");
        //创建Properties对象
        Properties p = new Properties();
        //加载
        try {
            //会将属性配置文件的所有数据存储到Properties对象中
            p.load(in);
            //将读取的jdbc参数赋值给全局变量
            driver = p.getProperty("driver");
            url = p.getProperty("url");
            username = p.getProperty("username");
            password = p.getProperty("password");
            //加载驱动
            Class.forName(driver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据库连接
     *
     * @return
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 关闭数据库连接
     *
     * @param connection
     * @param ptStatement
     * @param resultSet
     */
    public static void closeAll(Connection connection, PreparedStatement ptStatement, ResultSet resultSet) {
        try {
            if (connection != null) {
                connection.close();
            }
            if (ptStatement != null) {
                ptStatement.close();
            }
            if (resultSet != null) {
                ptStatement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 封装增加删除修改的通用工具方法
     *
     * @param sql  SQL语句
     * @param objs SQL语句占位符实参，如果没有参数则传入null
     * @return 返回增删改的结果，类型为int
     */
    public static int executeDML(String sql, Object... objs) {
        // 声明jdbc变量
        Connection conn = getConnection();

        if (conn == null) {
            return -1;
        }
        PreparedStatement ps = null;
        int i = -1;
        try {
            // 获取连接对象
            conn = DBUtil.getConnection();
            // 开启事务管理
            conn.setAutoCommit(false);

            // 创建SQL命令对象，如果是插入操作，返回唯一主键
            if (sql.toLowerCase().contains("insert")) {
                ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            } else {
                ps = conn.prepareStatement(sql);
            }

            // 给占位符赋值
            if (objs != null) {
                for (int j = 0; j < objs.length; j++) {
                    ps.setObject(j + 1, objs[j]);
                }
            }
            // 执行SQL
            i = ps.executeUpdate();

            // 对于插入操作，返回唯一主键
            if (sql.toLowerCase().contains("insert")) {
                // 获得主键的自增Id
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    // Id在结果集中的第一位
                    i = generatedKeys.getInt(1);
                }
            }
            conn.commit();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(conn, ps, null);
        }
        return i;
    }

    /**
     * 反射机制 返回单条记录
     *
     * @param sql
     * @param params
     * @param clazz
     * @return
     * @throws Exception
     */
    public static <T> T selectSimpleResult(String sql, List<Object> params, Class<T> clazz) {
        if (sql == null || clazz == null) {
            return null;
        }
        // 获取数据库连接失败
        Connection connection = getConnection();
        if (connection == null) {
            return null;
        }
        T resultObject = null;
        int index = 1;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = getConnection().prepareStatement(sql);
            if (params != null && !params.isEmpty()) {
                for (int i = 0; i < params.size(); i++) {
                    preparedStatement.setObject(index, params.get(i));
                    index++;
                }
            }
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                // 通过反射的到一个泛型对象
                resultObject = getObject(clazz, resultSet);
            }
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(connection, preparedStatement, resultSet);
        }
        return resultObject;
    }

    /**
     * 查询多条记录
     *
     * @param sql    将要执行的sql语句
     * @param params
     * @param clazz
     * @return
     * @throws Exception
     */
    public static <T> List<T> selectMoreResult(String sql, List<Object> params, Class<T> clazz) {
        Connection connection = getConnection();

        if (sql == null || clazz == null) {
            return null;
        }
        List<T> list = new ArrayList<T>();
        int index = 1;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(sql);
            if (params != null && !params.isEmpty()) {
                for (int i = 0; i < params.size(); i++) {
                    System.out.println(index + ":" + params.get(i));
                    preparedStatement.setObject(index, params.get(i));
                    index++;
                }
            }
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                // 通过反射创建实体
                T resultObject = getObject(clazz, resultSet);
                list.add(resultObject);
            }
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(connection, preparedStatement, resultSet);
        }
        return list;
    }

    /**
     * 从元数据中提取对象
     *
     * @param clazz
     * @param resultSet
     * @param <T>
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws SQLException
     * @throws NoSuchFieldException
     */
    private static <T> T getObject(Class<T> clazz, ResultSet resultSet) throws InstantiationException, IllegalAccessException, SQLException, NoSuchFieldException {

        T resultObject;

        // 因为部分基础数据类型的封装类没有无参构造函数所以单独考虑
        if (clazz.equals(Integer.class) || clazz.equals(Double.class) || clazz.equals(Long.class)) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            String colName = metaData.getColumnName(1);
            resultObject = (T) resultSet.getObject(colName);
            return resultObject;
        }

        resultObject = clazz.newInstance();

        ResultSetMetaData metaData = resultSet.getMetaData();
        int colLength = metaData.getColumnCount();

        for (int i = 0; i < colLength; i++) {
            String colName = metaData.getColumnName(i + 1);
            Object colValue = resultSet.getObject(colName);
            if (colValue == null) {
                colValue = "";
            }
            Field field = clazz.getDeclaredField(colName);
            field.setAccessible(true);
            field.set(resultObject, colValue);
        }
        return resultObject;
    }
}
