package com.tools;

import com.util.ConfigPropertiesUtil;
import com.util.FileUtil;
import com.util.StringUtil;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class MapperXMLGenerator {

	private String tableName = null;
	private String modelPackage = null;
	private String mapperxmlPackage = null;
	

	/**
	 *获得表头
	 * 
	 * @return
	 */
	public String getTitle() {
		
		return "<?xml version=\"1.0\"  encoding=\"UTF-8\" ?>\n<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >\n";
	}

	/**
	 * 获取mapper开头行
	 * 
	 * @param mapperPackage
	 * @return
	 */
	public String getMapperStart(String mapperPackage, String classNameVar) {
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("<mapper namespace=\"");
		sb.append(mapperPackage);
		sb.append(".");
		sb.append(StringUtil.toUpperCaseFristOne(classNameVar));
		sb.append("Mapper\">\n\n");
		
		return sb.toString();
	}

	/**
	 * 获取mapper结尾行
	 * 
	 * @return
	 */
	public String getMapperEnd() {
		return "</mapper>";
	}

	/**
	 * 获取ResultMap行
	 * 
	 **/
	public String getPropertyResult(String sql, String classNameVar, String modelPackage, String tableName, DBConnector dbConnector) {
		
		StringBuffer sb = new StringBuffer();
		
		List<String> primaryKeyList = dbConnector.queryPrimarykey(tableName);
		
		sb.append("\t<resultMap id=\"");
		sb.append(classNameVar);
		sb.append("ResultMap\" type=\"");
		sb.append(modelPackage);
		sb.append(".");
		sb.append(StringUtil.toUpperCaseFristOne(classNameVar));
		sb.append("\">\n");
		
		for (Entry<String, String> entry : dbConnector.queryField(sql).entrySet()) {
			
			String key = (String)entry.getKey();
			String propertyType = StringUtil.toCamelCase(key);
			
			for(int i=0; i < primaryKeyList.size(); i++){
				
				if(key.equals(primaryKeyList.get(i))){
					sb.append("\t\t<id property=\"");
					sb.append(propertyType);
					sb.append("\" column=\"");
					sb.append(key);
					sb.append("\" />\n");
				}else{
					sb.append("\t\t<result property=\"");
					sb.append(propertyType);
					sb.append("\" column=\"");
					sb.append(key);
					sb.append("\" />\n");
				}
			}
		}
		
		sb.append("\t</resultMap>\n\n");
		
		return sb.toString();
	}

	/**
	 * 获取SelectClause行
	 * 
	 * @param mapKeyAll
	 * @return
	 */
	public String getSelectClause(String mapKeyAll) {
		
		return "\t<sql id=\"selectClause\">\n\t\t" + mapKeyAll + "\n\t</sql>\n\n";
	}

	/**
	 * 获取SelectWhere行
	 * 
	 * @param sql
	 * @return
	 */
	public String getSelectWhere(String sql, DBConnector dbConnector) {
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("\t<sql id=\"selectWhere\">\n");
		
		for (Entry<String, String> entry : getFieldMap(sql, dbConnector).entrySet()) {
			
			String key = (String) entry.getKey();
			sb.append("\t\t\t<if test=\"");
			sb.append(StringUtil.toCamelCase(key));
			sb.append(" != null\"> \n\t\t\t\tAND ");
			sb.append(key);
			sb.append(" = #{");
			sb.append(StringUtil.toCamelCase(key));
			sb.append("}\n\t\t\t</if>\n");
		}
		
		sb.append("\t</sql>\n\n");
		
		return sb.toString();
	}
	
	/**获取updateset行
	 * 
	 * @param sql
	 * @return
	 */
	public String getUpdateSet(String sql, DBConnector dbConnector) {
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("\t<sql id=\"updateSet\">\n");
		
		for (Entry<String, String> entry : getFieldMap(sql, dbConnector).entrySet()) {
			String key = (String) entry.getKey();
			sb.append("\t\t\t<if test=\"");
			sb.append(StringUtil.toCamelCase(key));
			sb.append(" != null\"> \n\t\t\t\t");
			sb.append(key);
			sb.append(" = #{");
			sb.append(StringUtil.toCamelCase(key));
			sb.append("},\n\t\t\t</if>\n");
		}
		
		sb.append("\t</sql>\n\n");
		
		return sb.toString();
	}
	
	/**
	 * 获取selectCount行
	 * @param primaryKeyList
	 * @return
	 */
	public String getSelectCountById(List<String> primaryKeyList){
		
		String primaryKey = primaryKeyList.get(0);
		
		StringBuffer sb = new StringBuffer();
		sb.append("\t<select id=\"queryCountById\">\n");
		sb.append("\t\tselect count(");
		sb.append(primaryKey);
		sb.append(") from ");
		sb.append(tableName);
		sb.append("\n\t</select>\n");
		return sb.toString();
	}

	
	/**
	 *获取InsertClause行
	 * 
	 * @param sql
	 * @return
	 */
	public String getInsertClause(String sql, DBConnector dbConnector) {
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("\t<sql id=\"insertClause\">");
		Map<String, String> fieldMap = getFieldMap(sql,dbConnector);
		
		for (Entry<String, String> entry : fieldMap.entrySet()) {
			
			String key = (String) entry.getKey();
			sb.append("\n\t\t\t<if test=\"");
			sb.append(StringUtil.toCamelCase(key));
			sb.append(" != null\">\n\t\t\t\t");
			sb.append(key);
			sb.append(",\n\t\t\t</if>");
		}
		
		sb.append("\n\t</sql>\n\n");
		
		return sb.toString();
	}

	/**
	 * 获取InsertValues行
	 * 
	 * @param sql
	 * @return
	 */
	public String getInsertValue(String sql,DBConnector dbConnector) {
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("\t<sql id=\"insertValues\">");
		Map<String, String> fieldMap = getFieldMap(sql,dbConnector);
		
		for (Entry<String, String> entry : fieldMap.entrySet()) {
			
			String key = (String) entry.getKey();
			sb.append("\n\t\t\t<if test=\"");
			sb.append(StringUtil.toCamelCase(key));
			sb.append(" != null\">\n\t\t\t\t#{");
			sb.append(StringUtil.toCamelCase(key));
			sb.append("},\n\t\t\t</if>");
		}
		
		sb.append("\n\t</sql>\n\n");
		
		return sb.toString();
	}

	/**
	 * 获取queryByPrimarykey行
	 * 
	 * @param doubleKeymapper
	 * @param primaryKey
	 * @param classNameVar
	 * @return
	 */
	public String getSelectPrimaryKey(String doubleKeymapper, String primaryKey, String classNameVar) {
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("\t<select id=\"queryByPrimarykey\" ");
		sb.append(doubleKeymapper);
		sb.append(" resultMap=\"");
		sb.append(classNameVar);
		sb.append("ResultMap\">\n\t\tselect\n\t\t\t<include refid=\"selectClause\" />\n\t\tfrom ");
		sb.append(tableName);
		sb.append("\n\t\twhere ");
		sb.append(primaryKey);
		sb.append("\n\t</select>\n\n");
		
		return sb.toString();
	}





	/**
	 * 获取selectqueryBySelective行
	 * 
	 * @param classNameVar
	 * @param modelPackage
	 * @return
	 */
	public String getSelectQuerySelective(String classNameVar, String modelPackage) {
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("\t<select id" + "=\"queryBySelective\" parameterType=\"");
		sb.append(modelPackage);
		sb.append(".");
		sb.append(StringUtil.toUpperCaseFristOne(classNameVar));
		sb.append("\" resultMap=\"");
		sb.append(classNameVar);
		sb.append("ResultMap\">\n\t\tselect\n\t\t\t<include refid=\"selectClause\" />\n\t\tfrom ");
		sb.append(tableName);
		sb.append("\n\t\t<where>\n\t\t\t<include refid=\"selectWhere\" />\n\t\t</where>\n\t</select>\n\n");
		
		return sb.toString();
	}

	public String getQueryByPagination(String classNameVar, String modelPackage) {
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("\t<select id" + "=\"queryByPagination\" parameterType=\"");
		sb.append(modelPackage);
		sb.append(".");
		sb.append(StringUtil.toUpperCaseFristOne(classNameVar));
		sb.append("\" resultMap=\"");
		sb.append(classNameVar);
		sb.append("ResultMap\">\n\t\tselect\n\t\t\t<include refid=\"selectClause\" />\n\t\tfrom ");
		sb.append(tableName);
		sb.append("\n\t\t<where>\n\t\t\t<include refid=\"selectWhere\" />\n\t\t</where>\n\t</select>\n\n");
		
		return sb.toString();
	}
	
	
	/**
	 * 获取DeleteByPrimaryKey行 
	 * 
	 * @return
	 */
	public String getDeleteByPrimaryKey(String classNameVar, String primaryKey) {
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("\t<delete id=\"deleteByPrimaryKey\" >\n\t\tdelete from ");
		sb.append(tableName);
		sb.append("\n\t\twhere ");
		sb.append(primaryKey);
		sb.append("\n\t</delete>\n\n");
		
		return sb.toString();
	}

	/**
	 * 获取insert 行
	 * 
	 * @param sql
	 * @param modelPackage
	 * @param classNameVar
	 * @return
	 */
	public String getInsert(String sql, String modelPackage, String classNameVar,DBConnector dbConnector) {
		
		StringBuffer sb = new StringBuffer();
		String str = "";
		
		Map<String, String> fieldMap = getFieldMap(sql, dbConnector);
		
		sb.append("\t<insert id =\"insert\"  parameterType=\"");
		sb.append(modelPackage);
		sb.append(".");
		sb.append(StringUtil.toUpperCaseFristOne(classNameVar));
		sb.append("\">\n\t\tinsert into ");
		sb.append(tableName);
		sb.append("\n\t\t(<include refid=\"selectClause\" />)\n\t\tvalues\n\t\t(");
		
		for (Entry<String, String> entry : fieldMap.entrySet()) {
			String key = (String) entry.getKey();
			str += ", #{" + StringUtil.toCamelCase(key) + "}";
		}
		
		if (fieldMap.entrySet().size() > 0) {
			sb.append(str.substring(1));
		}
		
		sb.append(")\n\t</insert>\n\n");
		
		return sb.toString();
	}

	/**
	 *获取insertSelective行
	 * 
	 * @param classNameVar
	 * @param modelPackage
	 * @return
	 */
	public String getInsertSelective(String classNameVar, String modelPackage) {
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("\t<insert id=\"insertSelective\" parameterType=\"");
		sb.append(modelPackage);
		sb.append(".");
		sb.append(StringUtil.toUpperCaseFristOne(classNameVar));
		sb.append("\">\n\t\tinsert into ");
		sb.append(tableName);
		sb.append("\n\t\t<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\n\t\t\t<include refid=\"insertClause\" />\n\t\t</trim>\n\t\t<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">\n\t\t\t<include refid=\"insertValues\" />\n\t\t</trim>\n\t</insert>\n\n");
		
		return sb.toString();
	}

	/**
	 *获取getUpdateByPrimaryKey行
	 * 
	 * @param primaryKey
	 * @param modelPackage
	 * @param classNameVar
	 * @param sql
	 * @return
	 */
	public String getUpdateByPrimaryKey(String primaryKey, String modelPackage, String classNameVar, String sql, DBConnector dbConnector) {
		
		StringBuffer sb = new StringBuffer();
		String str = "";
		
		Map<String, String> fieldMap = getFieldMap(sql,dbConnector);
		sb.append("\t<update id=\"updateByPrimaryKey\" parameterType=\"");
		sb.append(modelPackage);
		sb.append(".");
		sb.append(StringUtil.toUpperCaseFristOne(classNameVar));
		sb.append("\">\n\t\tupdate ");
		sb.append(tableName);
		sb.append(" set\n\t\t");
		
		for (Entry<String, String> entry : fieldMap.entrySet()) {
			String key = (String) entry.getKey();
			str += ", " + key + " = " + "#{" + StringUtil.toCamelCase(key) + "}";
		}
		
		if (fieldMap.entrySet().size() > 0) {
			sb.append(str.substring(1));
		}
		
		sb.append("\n\t\twhere ");
		sb.append(primaryKey);
		sb.append("\n\t</update>\n\n");
		
		return sb.toString();
	}

	/**
	 *获取updateByPrimaryKeySelective行
	 * 
	 * @param primaryKey
	 * @param modelPackage
	 * @param classNameVar
	 * @return
	 */
	public String getUpdateByPrimaryKeySelective(String primaryKey,String modelPackage, String classNameVar) {
		
		StringBuffer sb = new StringBuffer();
		sb.append("\t<update id=\"updateByPrimaryKeySelective\" parameterType=\"");
		sb.append(modelPackage);
		sb.append(".");
		sb.append(StringUtil.toUpperCaseFristOne(classNameVar));
		sb.append("\">\n\t\tupdate ");
		sb.append(tableName);
		sb.append("\n\t\t<set>\n\t\t\t<include refid=\"updateSet\" />\n\t\t</set>\n\t\twhere ");
		sb.append(primaryKey);
		sb.append("\n\t</update>\n\n");
		
		return sb.toString();
	}

	/**
	 *获取主键
	 * 
	 * @param primaryKeyList
	 * @return
	 */
	public static String getPrimaryKeyStr(List<String> primaryKeyList) {
		String primaryKey = null;
		String result = "";
		
		for (int i = 0; i < primaryKeyList.size(); i++) {
			primaryKey = primaryKeyList.get(i);
			result += " AND " + primaryKey + " = #{" + StringUtil.toCamelCase(primaryKey) + "}";
		}
		
		if (primaryKeyList.size() > 0) {
			return result.substring(5);
		}

		return result;
	}

	/**
	 * 获取map中的key值
	 *  
	 * @param sql
	 * @return
	 */
	public String getMapKey(String sql,DBConnector dbConnector) {
		
		String key = "";
		
		Map<String, String> fieldMap = getFieldMap(sql,dbConnector);
		
		for (Entry<String, String> entry : fieldMap.entrySet()) {
			key += ", " + (String) entry.getKey();
		}
		
		if (fieldMap.entrySet().size() > 0) {
			return key.substring(1);
		}

		return key;
	}
	
	
	/**
	 * 区分是否带parameterType=HashMap
	 * @param primaryKeyList
	 * @return
	 */
	public String getDublekeymapper(List<String> primaryKeyList) {
		
		if (primaryKeyList.size() > 1) {
			return "parameterType=\"HashMap\"";
		}else {
			return " ";
		}
	}
	
	/**
	 * 获取表中字段及字段所对应类型
	 * @param sql
	 * @return 
	 */
	public Map<String, String> getFieldMap(String sql, DBConnector dbConnector){
		
		Map<String, String> fieldMap = dbConnector.queryField(sql);
		
		return  fieldMap;
	}
	
	public String getMapperXMLStr(DBConnector dbConnector) {

        String queryTableSql = "select * from " + tableName;
		
		List<String> primaryKeyList = dbConnector.queryPrimarykey(tableName);
		String classNameVar = StringUtil.toCamelCase(tableName.toString().substring(2));

		// 获取主键
		String primaryKey = getPrimaryKeyStr(primaryKeyList);
		// 获取双主键的时候的
		String doubleKeymapper = getDublekeymapper(primaryKeyList);
		String mapKeyAll = getMapKey(queryTableSql,dbConnector);

		// 拼接最终的字符串
		StringBuffer sb = new StringBuffer();
		sb.append(getTitle());
		sb.append(getMapperStart(mapperxmlPackage, classNameVar));
		sb.append(getPropertyResult(queryTableSql, classNameVar, modelPackage, tableName,dbConnector));
		sb.append(getSelectClause(mapKeyAll));
		sb.append(getSelectWhere(queryTableSql,dbConnector));
		sb.append(getUpdateSet(queryTableSql,dbConnector));
		sb.append(getInsertClause(queryTableSql,dbConnector));
		sb.append(getInsertValue(queryTableSql,dbConnector));
		sb.append(getSelectPrimaryKey(doubleKeymapper, primaryKey, classNameVar));
		sb.append(getSelectQuerySelective(classNameVar,modelPackage));
		sb.append(getQueryByPagination(classNameVar, modelPackage));
		sb.append(getDeleteByPrimaryKey(classNameVar, primaryKey));
		sb.append(getInsert(queryTableSql, modelPackage, classNameVar,dbConnector));
		sb.append(getInsertSelective(classNameVar, modelPackage));
		sb.append(getUpdateByPrimaryKey(primaryKey, modelPackage, classNameVar, queryTableSql,dbConnector));
		sb.append(getUpdateByPrimaryKeySelective(primaryKey,modelPackage, classNameVar));
		sb.append(getSelectCountById(primaryKeyList));
		sb.append(getMapperEnd());

		return sb.toString();
	}

	/**
	 * 生成并写入进mapper文件
	 * 
	 * @param arg
	 *            要写入的字符串
	 * 
	 * @return
	 **/
	public void writeToFile(String arg) {
		String className = StringUtil.toUpperCaseFristOne(StringUtil
				.toCamelCase(tableName.toString().substring(2)));

		// 生成并写入进文件
		String mapperxmlPath = mapperxmlPackage.replaceAll("\\.", "/") + "/";
        String pathName = System.getProperty("user.dir");
        pathName = pathName + "/src/main/resources/" + mapperxmlPath + className + "_Mapper.xml";

		FileUtil.writeToFile(pathName, arg);
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setMapperxmlPackage(String mapperxmlPackage) {
		this.mapperxmlPackage = mapperxmlPackage;
	}

	public void setmapperxmlPackage(String mapperxmlPackage) {
		this.mapperxmlPackage = mapperxmlPackage;
	}

	public void setModelPackage(String modelPackage) {
		this.modelPackage = modelPackage;
	}

	public static void main(String[] args) {
		
		MapperXMLGenerator generator = new MapperXMLGenerator();
		
		generator.setTableName(ConfigPropertiesUtil.getProperty("tableName"));
		generator.setModelPackage(ConfigPropertiesUtil.getProperty("modelPackage"));
		generator.setMapperxmlPackage(ConfigPropertiesUtil.getProperty("mapperxmlPackage"));
		
//		String mapperXMLStr = generator.getMapperXMLStr();
//
//		generator.writeToFile(mapperXMLStr);
	}
}
