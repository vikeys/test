package com.tools;

import com.util.ConfigPropertiesUtil;
import com.util.FileUtil;
import com.util.StringUtil;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ModelGenerator {

	private String tableName;
	private String modelPackage;

	/**
	 * 获取package行字符串
	 * 
	 * @param packageStr
	 *            包名
	 * @return
	 **/
	public String getPackage(String packageStr) {
		return "package " + packageStr + ";\n\n";
	}

	/**
	 * 获取要导入的包
	 * 
	 * @param fieldMap
	 *            字段名及对应的类型Map
	 * @return
	 **/
	public String getImport(Map<String, String> fieldMap) {
		
		StringBuffer sb = new StringBuffer();
		Set<String> keySet = new HashSet<String>();
		
		for (String key : fieldMap.keySet()) {
			String type = DBType2JavaType.getJaveType(fieldMap.get(key));
			keySet.add(type);
		}

		for (String javaType : keySet) {
			
			if (javaType.equals("Date")) {
				sb.append("import java.util.Date;\n");
			}
		}
		
		sb.append("\n");

		return sb.toString();
	}

	/**
	 * 获取class开始行字符串
	 * 
	 * @param className
	 *            类名
	 * @return
	 **/
	public String getClassStart(String className) {
		return "public class " + StringUtil.toUpperCaseFristOne(className) + " {\n\n";
	}

	/**
	 * 获取class结束行字符串
	 * 
	 * @return
	 **/
	public String getClassEnd() {
		return "}";
	}

	/**
	 * 获取所有property属性行字符串
	 * 
	 * @param fieldMap
	 *            字段名及对应的类型Map
	 * @return
	 **/
	public String getAllProperty(Map<String, String> fieldMap) {
		
		StringBuffer sb = new StringBuffer();
		
		for (String key : fieldMap.keySet()) {
			sb.append("\tprivate ");
			sb.append(DBType2JavaType.getJaveType(fieldMap.get(key)));
			sb.append(" ");
			sb.append(StringUtil.toCamelCase(key));
			sb.append(";\n\n");
		}

		return sb.toString();
	}

	/**
	 * 获取所有property对应的setter、getter方法
	 * 
	 * @param fieldMap
	 *            字段名及对应的类型Map
	 * @return
	 **/
	public String getAllSetterGetterMethod(Map<String, String> fieldMap) {
		
		StringBuffer sb = new StringBuffer();
		
		for (String key : fieldMap.keySet()) {
			sb.append(getSetterMethod(StringUtil.toCamelCase(key), DBType2JavaType.getJaveType(fieldMap.get(key))));
			sb.append(getGetterMethod(StringUtil.toCamelCase(key), DBType2JavaType.getJaveType(fieldMap.get(key))));
		}

		return sb.toString();
	}

	/**
	 * 获取property对应的set方法
	 * 
	 * @param property
	 *            属性名
	 * @param propertyType
	 *            属性类型
	 * @return
	 **/
	public String getSetterMethod(String property, String propertyType) {
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("\tpublic void set");
		sb.append(StringUtil.toUpperCaseFristOne(property));
		sb.append("(");
		sb.append(propertyType);
		sb.append(" ");
		sb.append(property);
		sb.append(") {\n\t\tthis.");
		sb.append(property);
		sb.append(" = ");
		sb.append(property);
		sb.append(";\n\t}\n\n");
		
		return sb.toString();
	}

	/**
	 * 获取property对应的get方法
	 * 
	 * @param property
	 *            属性名
	 * @param propertyType
	 *            属性类型
	 * @return
	 **/
	public String getGetterMethod(String property, String propertyType) {
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("\tpublic ");
		sb.append(propertyType);
		sb.append(" get");
		sb.append(StringUtil.toUpperCaseFristOne(property));
		sb.append("() {\n\t\treturn ");
		sb.append(property);
		sb.append(";\n\t}\n\n");
		
		return sb.toString();
	}

	/**
	 * 获取要写入的model的字符串
	 * 
	 * @return
	 **/
	public String getModelStr(DBConnector dbConnector) {

		String queryTableSql = "select * from " + tableName;
		Map<String, String> fieldMap = dbConnector.queryField(queryTableSql); // 根据表明获取字段名、字段类型的Map
		String className = StringUtil.toUpperCaseFristOne(StringUtil.toCamelCase(tableName.toString().substring(2)));

		// 拼接最终的字符串
		StringBuffer sb = new StringBuffer();
		sb.append(getPackage(modelPackage));
		sb.append(getImport(fieldMap));
		sb.append(getClassStart(className));
		sb.append(getAllProperty(fieldMap));
		sb.append(getAllSetterGetterMethod(fieldMap));
		sb.append(getClassEnd());

		return sb.toString();
	}
	
	/**
	 * 生成并写入进model文件
	 * 
	 * @param arg 要写入的字符串
	 * 
	 * @return
	 **/
	public void writeToFile(String arg){
		String className = StringUtil.toUpperCaseFristOne(StringUtil.toCamelCase(tableName.toString().substring(2)));
		
		// 生成并写入进文件
		String modelPath = modelPackage.replaceAll("\\.", "/") + "/";
		String pathname = System.getProperty("user.dir");
		pathname = pathname + "/src/main/java/" + modelPath + className + ".java";
		
		FileUtil.writeToFile(pathname, arg);
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setModelPackage(String modelPackage) {
		this.modelPackage = modelPackage;
	}

	public static void main(String[] args) {

        System.out.println(ModelGenerator.class.getResource("/").getPath());
        System.out.println(System.getProperty("user.dir"));
	}
}
