package com.tools;

import com.util.ConfigPropertiesUtil;
import com.util.FileUtil;
import com.util.StringUtil;

import java.util.List;
import java.util.Map;

public class MapperGenerator {
	
	private String tableName = null;
	private String modelPackage = null;
	private String mapperPackage = null;

	/**
	 * 获取package行字符串
	 * 
	 * @param packageStr
	 *           包名
	 * @return
	 **/
	public String getPackage(String packageStr) {
		
		return  "package " + packageStr + ";\n\n";
	}

	/**
	 * 获取import行字符串
	 * 
	 * @param classNameVar
	 *           model变量
	 * @return
	 */
	public String getImport(String path, String classNameVar,List<String> primaryKeyList) {
		
		StringBuffer sb=new StringBuffer();
		
		/*if(primaryKeyList.size() > 1){
			sb.append("import java.util.Map;\n");
		}*/
		
		sb.append("import java.util.List;\nimport java.util.Map;\nimport org.apache.ibatis.session.RowBounds;\nimport ");
		sb.append(path);
		sb.append(".");
		sb.append(StringUtil.toUpperCaseFristOne(classNameVar));
		sb.append(";\n\n");
			
		return sb.toString();
	}

	/**
	 * 获取class开始行字符串
	 * 
	 * @param classNameVar
	 *           类名
	 * @return
	 **/
	public String getInterfaceStart(String classNameVar) {
		return "public interface " + StringUtil.toUpperCaseFristOne(classNameVar) + "Mapper {\n\n";
	}

	/** 获取queryByPrimarykey
	 * 
	 * @param classNameVar
	 * @param primaryKeyStr
	 * @return
	 */
	public String getQueryByPrimarykey(String classNameVar, String primaryKeyStr) {
		return "\t" + StringUtil.toUpperCaseFristOne(classNameVar) + " queryByPrimarykey(" + primaryKeyStr + ");\n\n";
	}

	/**获取queryBySelective行
	 * 
	 * @param classNameVar
	 * @return
	 */
	public String getQueryBySelective(String classNameVar) {
		
		String className = StringUtil.toUpperCaseFristOne(classNameVar);
		
		return "\tList<" + className + "> queryBySelective(" + className + " " + classNameVar + ");\n\n";
	}

	/**获取queryByPagination行
	 * 
	 * @param classNameVar
	 * @return
	 */
	public String getQueryByPagination(String classNameVar) {
		
		String className = StringUtil.toUpperCaseFristOne(classNameVar);
		
		return "\tList<" + className + "> queryByPagination(" + className + " " + classNameVar + ",  RowBounds rowBounds);\n\n";
	}

	/**获取insert行
	 * 
	 * @param classNameVar
	 * @return
	 */

	public String getInsert(String classNameVar) {
		
		return "\tint insert(" + StringUtil.toUpperCaseFristOne(classNameVar) + " " + classNameVar + ");\n\n";
	}

	/**获取 insertSelective行
	 * 
	 * @param classNameVar
	 * @return
	 */
	public String getInsertSelective(String classNameVar) {
		return "\tint insertSelective(" + StringUtil.toUpperCaseFristOne(classNameVar) + " " + classNameVar + ");\n\n";
	}

	/**获取updateByPrimaryKey行
	 * 
	 * @param classNameVar
	 * @return
	 */
	 
	public String getUpdateByPrimaryKey(String classNameVar) {
		return "\tint updateByPrimaryKey(" + StringUtil.toUpperCaseFristOne(classNameVar) + " " + classNameVar + ");\n\n";
	}

	/**获取 updateByPrimaryKeySelective行
	 * 
	 * @param classNameVar
	 * @return
	 */
	public String getUpdateByPrimaryKeySelective(String classNameVar) {
		return "\tint updateByPrimaryKeySelective(" + StringUtil.toUpperCaseFristOne(classNameVar) + " " + classNameVar + ");\n\n";
	}

	/**deleteByPrimaryKey行
	 * 
	 * @param primaryKeyStr
	 * @return
	 */

	public String getDeleteByPrimaryKey(String primaryKeyStr) {
		return "\tint deleteByPrimaryKey(" + primaryKeyStr + ");\n\n";
	}
	
	/**
	 * 获取数量
	 * @return
	 */
	public String getQueryByCondition(String classNameVar){
		
		String className = StringUtil.toUpperCaseFristOne(classNameVar);
		
		return "\tList<" + className + ">" + " queryByCondition(Map condition);\n\n";
	}

	/**
	 * 获取多主键
	 * @param fieldMap
	 * @param primaryKeyList
	 * @return
	 */
	public String getPrimaryKeyStr(Map<String, String> fieldMap, List<String> primaryKeyList) {
		
		String result = "";
		String primaryKey = null;
		
		for (int i = 0; i < primaryKeyList.size(); i++) {
			primaryKey = primaryKeyList.get(i);
			result += ", "+ DBType2JavaType.getJaveType(fieldMap.get(primaryKey)) + " " + StringUtil.toCamelCase(primaryKey);
		}
		
		if (primaryKeyList.size() > 1) {
			// return result.substring(2);
			return "Map IdMap ";
		}
		
		if (result.length() > 0) {
			return result.substring(2);
		}
		
		return null;
	}

	/**
	 * 获取class结束行字符串
	 * 
	 * @return
	 **/
	public String getInterfaceEnd() {
		return "}";
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setModelPackage(String modelPackage) {
		this.modelPackage = modelPackage;
	}

	public void setMapperPackage(String mapperPackage) {
		this.mapperPackage = mapperPackage;
	}
	
	/**
	 *获取要写入的mapper的字符串
	 * 
	 * @return
	 **/
	public String getMapperStr(DBConnector dbConnector) {
		

		String queryTableSql = "select * from " + tableName;
		Map<String, String> fieldMap = dbConnector.queryField(queryTableSql);
		List<String> primaryKeyList = dbConnector.queryPrimarykey(tableName);
		String classNameVar = StringUtil.toCamelCase(tableName.toString().substring(2));
		String primaryKeyStr = getPrimaryKeyStr(fieldMap, primaryKeyList); 

		// 拼接最终的字符串
		StringBuffer sb = new StringBuffer();
		sb.append(getPackage(mapperPackage));
		sb.append(getImport(modelPackage, classNameVar,primaryKeyList));
		sb.append(getInterfaceStart(classNameVar));
		sb.append(getQueryByPrimarykey(classNameVar, primaryKeyStr));
		sb.append(getQueryBySelective(classNameVar));
		sb.append(getQueryByPagination(classNameVar));
		sb.append(getInsert(classNameVar));
		sb.append(getInsertSelective(classNameVar));
		sb.append(getUpdateByPrimaryKey(classNameVar));
		sb.append(getUpdateByPrimaryKeySelective(classNameVar));
		sb.append(getDeleteByPrimaryKey(primaryKeyStr));
		sb.append(getQueryByCondition(classNameVar));
		sb.append(getInterfaceEnd());

		return sb.toString();
	}
	
	/**
	 * 生成并写入进mapper文件
	 * 
	 * @param arg
	 * 			要写入的字符串
	 * 
	 * @return
	 **/
	public void writeToFile(String arg){
		
		String className = StringUtil.toUpperCaseFristOne(StringUtil.toCamelCase(tableName.toString().substring(2)));
		
		// 生成并写入进文件
		String mapperPath = mapperPackage.replaceAll("\\.", "/") + "/";

        String pathName = System.getProperty("user.dir");
        pathName = pathName + "/src/main/java/" + mapperPath + StringUtil.toUpperCaseFristOne(className) + "Mapper.java";
		
		FileUtil.writeToFile(pathName, arg);

	}

	public static void main(String[] args) {
		
		MapperGenerator generator = new MapperGenerator();
		
		generator.setTableName(ConfigPropertiesUtil.getProperty("tableName"));
		generator.setModelPackage(ConfigPropertiesUtil.getProperty("modelPackage"));
		generator.setMapperPackage(ConfigPropertiesUtil.getProperty("mapperPackage"));
		
//		String mapperStr = generator.getMapperStr();
		
//		generator.writeToFile(mapperStr);

	}

}
