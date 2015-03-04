package com.util;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class ConfigPropertiesUtil {
	
	private static Properties configPro = new Properties();
	
	static {
		loadConfigProperty();
	}
	
	public static void loadConfigProperty() {
		try {
			InputStream is = ConfigPropertiesUtil.class.getClassLoader()
					.getResourceAsStream("config.properties");
			configPro.clear();
			configPro.load(is);
		} catch (IOException e) {
			System.out.println("加载config.properties文件出现错误！");
			e.printStackTrace();
		}
	}
	
	public static String getProperty(String key) {
		return configPro.getProperty(key);
	}
}
