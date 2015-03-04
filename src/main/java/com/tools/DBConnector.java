package com.tools;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DBConnector {

	private static String driverName;
	private static String sqlUrl;
	private static String sqlUser;
	private static String sqlPassword;
//
//
//
//	static {
//
//		if (driverName == null && sqlUrl == null && sqlUser == null && sqlPassword == null) {
//
//			driverName = ConfigPropertiesUtil.getProperty("driverName");
//			sqlUrl = ConfigPropertiesUtil.getProperty("sqlUrl");
//			sqlUser = ConfigPropertiesUtil.getProperty("sqlUser");
//			sqlPassword = ConfigPropertiesUtil.getProperty("sqlPassword");
//		}
//	}

    public DBConnector(String driverName, String sqlUrl, String sqlUser, String sqlPassword){
        this.driverName = driverName;
        this.sqlUrl = sqlUrl;
        this.sqlUser = sqlUser;
        this.sqlPassword = sqlPassword;
    }

	public static Connection startConnect() throws ClassNotFoundException, SQLException {
		
		Class.forName(DBConnector.getDriverName());
		return DriverManager.getConnection(DBConnector.getSqlUrl(), DBConnector.getSqlUser(), DBConnector.getSqlPassword());

	}

	public static void disconnect(Connection conn, Statement stmt, ResultSet rs) {
		
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 根据表名获取主键
     * @param tableName
     * @return
     */
	public List<String> queryPrimarykey(String tableName) {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<String> result = new ArrayList<String>();

		try {
			con = DriverManager.getConnection(sqlUrl, sqlUser, sqlPassword);
			DatabaseMetaData dmd = con.getMetaData();

			rs = dmd.getPrimaryKeys(null, null, tableName.toUpperCase());
			
			while (rs.next()) {
				result.add(rs.getString(4));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}  finally {
			DBConnector.disconnect(con, stmt, rs);
		}
		
		return result;
	}
	
	/**
	 * 获取字段和字段对应类型
	 * @param sql
	 * @return
	 */
	public Map<String, String> queryField(String sql) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		Map<String, String> resultMap = new LinkedHashMap<String, String>();

		try {
			conn = DBConnector.startConnect(); 
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery(sql);
			ResultSetMetaData data = rs.getMetaData();
			
			for (int i = 1; i <= data.getColumnCount(); i++) {
				resultMap.put(data.getColumnName(i), data.getColumnTypeName(i));
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnector.disconnect(conn, stmt, rs);
		}

		return resultMap;
	}
	
	public Map<String, String> getPrimaryKeyField(String tableName, String sql) {
		
		Map<String, String> primaryKeyMap = new LinkedHashMap<String, String>();
		List<String> primaryKeyList = new ArrayList<String>();
		
		for(int i=0; i < primaryKeyList.size(); i++){
			primaryKeyMap.put(primaryKeyList.get(i), queryField(sql).get(primaryKeyList.get(i)));
		}
		
		return primaryKeyMap;
	}

    public static String getDriverName() {
        return driverName;
    }

    public static void setDriverName(String driverName) {
        DBConnector.driverName = driverName;
    }

    public static String getSqlUrl() {
        return sqlUrl;
    }

    public static void setSqlUrl(String sqlUrl) {
        DBConnector.sqlUrl = sqlUrl;
    }

    public static String getSqlUser() {
        return sqlUser;
    }

    public static void setSqlUser(String sqlUser) {
        DBConnector.sqlUser = sqlUser;
    }

    public static String getSqlPassword() {
        return sqlPassword;
    }

    public static void setSqlPassword(String sqlPassword) {
        DBConnector.sqlPassword = sqlPassword;
    }
}
