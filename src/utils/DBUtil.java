package utils;

import java.sql.Connection;
import java.sql.SQLException;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * 获取数据库连接
 * @author lilq2
 *
 */
public class DBUtil {
	
	public static void main(String[] agrs) {
		
	}
	
	/**
	 * 获取数据库连接
	 * @return
	 */
    public static Connection getDBConnection() {
    	DruidDataSource ds=(DruidDataSource) ApplicationContextHelper.getBean("dataSource");
    	try {
			Connection conn=ds.getConnection().getConnection();
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
    
    /**
     * 关闭数据库连接
     * @param conn
     */
    public static void closeConnection(Connection conn) {
    	if(conn!=null){
    		try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
	}
    
    
}
