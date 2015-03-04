package com.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DBTypePropertiesUtils {
	
	private static Properties dbType = new Properties();
	
	static {
		loadDBTypeProperty();
	}
	
	public static void loadDBTypeProperty() {
		try {
			InputStream is = ConfigPropertiesUtil.class.getClassLoader()
					.getResourceAsStream("dbType.properties");
			dbType.clear();
			dbType.load(is);
		} catch (IOException e) {
			System.out.println("加载dbType.properties文件出现错误！");
			e.printStackTrace();
		}
	}
	
	public static String getProperty(String key) {
		return dbType.getProperty(key);
	}

}
