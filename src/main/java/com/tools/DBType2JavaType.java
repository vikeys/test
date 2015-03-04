package com.tools;

import com.util.DBTypePropertiesUtils;

public class DBType2JavaType {

	public static String getJaveType(String fieldType) {
		
		fieldType = fieldType.toUpperCase();
		
		return DBTypePropertiesUtils.getProperty(fieldType);
	}

}
