package com.tools;

import com.util.ConfigPropertiesUtil;
import com.util.FileUtil;
import com.util.StringUtil;

import java.util.List;
import java.util.Map;

public class DAOImplGenerator {
	
	private String tableName = null;
	private String modelPackage = null;
	private String mapperPackage = null;
	private String daoImplPackage=null;
	
	/**
	 * 获取package行
	 * 
	 * @param packageStr
	 *           
	 * @return
	 **/
	public String getPackage(String packageStr) {
		return "package " + packageStr + ";\n\n";
	}

	/**
	 * 获取import行
	 * 
	 * @param path
	 * @param path1
	 * @param classNameVar
	 * @return
	 */
	public String getImport(String path, String path1, String classNameVar) {
		return "import java.util.List;\nimport javax.annotation.Resource;\nimport org.apache.ibatis.session.RowBounds;\n"
				+ "import org.apache.ibatis.session.SqlSession;\nimport org.apache.ibatis.session.RowBounds;\nimport org.springframework.stereotype.Repository;\n\n"
				+ "import " + path + "." + StringUtil.toUpperCaseFristOne(classNameVar) + "DAO" + ";\nimport " + path1 + "." + StringUtil.toUpperCaseFristOne(classNameVar) + ";\n\n";
	}

	/**
	 * 获取@Repository行
	 * 
	 * @param classNameVar
	 *          
	 * @return
	 */
	public String getRepositoryAndClassStart(String classNameVar) {
		
		String className = StringUtil.toUpperCaseFristOne(classNameVar);
		
		return "@Repository " + "(value = \"" + classNameVar + "DAO" + "\")\npublic class " + className + "DAOImpl " + "implements " + className + "DAO" + "{\n\n";
	}

	/**
	 * 获取class行
	 * 
	 * @return
	 **/
	public String getClassEnd() {
		return "}";
	}

	/**
	 * 获取@Resource行
	 * 
	 * @param
	 * @return
	 */
	public String getResourceAndPrivate() {
		return "\t@Resource\n\tprivate SqlSession sqlSession;" + "\n\n";
	}

	/**
	 * 获取@Override行
	 * 
	 * @return
	 */
	public String getOverride() {
		return "\t@Override" + "\n";
	}

	/**
	 * 获取queryByPrimarykey行
	 * 
	 */
	public String getQueryByPrimarykey(String classNameVar,
			String primaryKeyStr, String path, String primaryKey) {
		return "\t@Override" + "\n\tpublic "
				+ StringUtil.toUpperCaseFristOne(classNameVar)
				+ " queryByPrimarykey(" + primaryKeyStr + "){\n"+"\t\treturn " + "("
				+ StringUtil.toUpperCaseFristOne(classNameVar) + ") "
				+ "sqlSession.selectOne" + "(" + "\"" + path + "."
				+ StringUtil.toUpperCaseFristOne(classNameVar) + "DAO"
				+ ".queryByPrimarykey" + "\"" + ", " + primaryKey
				+ ");\n\t}\n\n";
		
	}


	/**
	 * 获取queryBySelective行
	 * 
	 */
	public String getQueryBySelective(String classNameVar,String path) {
		String className = StringUtil.toUpperCaseFristOne(classNameVar);
		return "\tpublic List<" + className + "> queryBySelective(" + className
				+ " " + classNameVar + "){\n"+"\t\treturn " + "sqlSession.selectList" + "(\"" + path + "."
				+ StringUtil.toUpperCaseFristOne(classNameVar) + "DAO"
				+ ".queryBySelective" + "\", " + classNameVar + ");\n\t}\n\n";
		
	}

	

	/**
	 * 获取deleteByPrimaryKey行
	 * 
	 * @return
	 */

	public String getDeleteByPrimaryKey(String primaryKeyStr,String path, String classNameVar,
			String primaryKey) {
		return "\tpublic int deleteByPrimaryKey(" + primaryKeyStr + "){\n"
		+"\t\treturn " + "sqlSession.delete" + "(\"" + path + "."
		+ StringUtil.toUpperCaseFristOne(classNameVar) + "DAO"
		+ ".deleteByPrimaryKey" + "\", " + primaryKey + ");\n\t}\n\n";
	}



	/**
	 * 获取insert行
	 * 
	 * @return
	 */

	public String getInsert(String classNameVar,String path) {
		return "\tpublic int insert("
				+ StringUtil.toUpperCaseFristOne(classNameVar) + " "
				+ classNameVar + "){\n"+"\t\treturn " + "sqlSession.insert" + "(\"" + path + "."
				+ StringUtil.toUpperCaseFristOne(classNameVar) + "DAO"
				+ ".insert" + "\", " + classNameVar + ");\n\t}\n\n";
	}

	
	/**
	 *获取insertSelective行
	 * 
	 * @return
	 */
	public String getInsertSelective(String classNameVar,String path) {
		return "\tpublic int insertSelective("
				+ StringUtil.toUpperCaseFristOne(classNameVar) + " "
				+ classNameVar + "){\n"+"\t\treturn " + "sqlSession.insert" + "(\"" + path + "."
				+ StringUtil.toUpperCaseFristOne(classNameVar) + "DAO"
				+ ".insertSelective" + "\", " + classNameVar + ");\n\t}\n\n";
	}

	

	/**
	 *获取queryByPagination行
	 * 
	 * @return
	 */
	public String getQueryByPagination(String classNameVar,String path) {
		String className = StringUtil.toUpperCaseFristOne(classNameVar);
		return "\tpublic List<" + className + "> queryByPagination("
				+ className + " " + classNameVar
				+ ", RowBounds rowBounds){\n"+"\t\treturn " + "sqlSession.selectList" + "(\"" + path + "."
				+ StringUtil.toUpperCaseFristOne(classNameVar) + "DAO"
				+ ".insertSelective" + "\", " + classNameVar + ", "
				+ "rowBounds);\n\t}\n\n";
	}

	

	/**
	 * 获取updateByPrimaryKey行
	 * 
	 * @return
	 */

	public String getUpdateByPrimaryKey(String classNameVar,String path) {
		return "\tpublic int updateByPrimaryKey("
				+ StringUtil.toUpperCaseFristOne(classNameVar) + " "
				+ classNameVar + "){\n"+"\t\treturn " + "sqlSession.update" + "(\"" + path + "."
				+ StringUtil.toUpperCaseFristOne(classNameVar) + "DAO"
				+ ".updateByPrimaryKey" + "\", " + classNameVar + ");\n\t}\n\n";
	}

	

	/**
	 *获取updateByPrimaryKeySelective行
	 * 
	 * @return
	 */
	public String getUpdateByPrimaryKeySelective(String classNameVar,String path) {
		return "\tpublic int updateByPrimaryKeySelective("
				+ StringUtil.toUpperCaseFristOne(classNameVar) + " "
				+ classNameVar + "){\n"+"\t\treturn " + "sqlSession.update" + "(\"" + path + "."
				+ StringUtil.toUpperCaseFristOne(classNameVar) + "DAO"
				+ ".updateByPrimaryKeySelective" + "\", " + classNameVar
				+ ");\n\t}\n\n";
	}

	

	/**
	 * 得到主键以及类型
	 * 
	 * @param fieldMap
	 * @param primaryKeyList
	 * @return
	 */
	public String getPrimaryKeyStr(Map<String, String> fieldMap,
			List<String> primaryKeyList) {
		String result = "";
		String primaryKey = null;
		for (int i = 0; i < primaryKeyList.size(); i++) {
			primaryKey = primaryKeyList.get(i);
			result += ", "
					+ DBType2JavaType.getJaveType(fieldMap.get(primaryKey))
					+ " " + primaryKey;
		}
		if (primaryKeyList.size() > 0) {
			return result.substring(2);
		}
		return result;
	}

	/**
	 * 得到主键
	 * 
	 * @param primaryKeyList
	 * @return
	 */
	public String getPrimaryKeyStr(List<String> primaryKeyList) {
		String result = "";
		String primaryKey = null;
		for (int i = 0; i < primaryKeyList.size(); i++) {
			primaryKey = primaryKeyList.get(i);
			result += "," + primaryKey;
		}
		
		 if (primaryKeyList.size() > 0) { return result.substring(1); }
		 
		return result;
	}
	
	
	
	
	public String getDaoImplStr(DBConnector dbConnector) {

		String queryTableSql = "select * from " + tableName;
		Map<String, String> fieldMap = dbConnector.queryField(queryTableSql);
		List<String> primaryKeyList = dbConnector.queryPrimarykey(tableName);
		String classNameVar = StringUtil.toCamelCase(tableName);
		
		String primaryKeyStr = getPrimaryKeyStr(fieldMap, primaryKeyList); // ��ȡ�����Ӧ��string
		String primaryKey =getPrimaryKeyStr(primaryKeyList);
		
		// 拼接最终的字符串
		StringBuffer sb = new StringBuffer();
		sb.append(getPackage(daoImplPackage));
		sb.append(getImport(mapperPackage, modelPackage, classNameVar));
		sb.append(getRepositoryAndClassStart(classNameVar));
		sb.append(getResourceAndPrivate());
		sb.append(getQueryByPrimarykey(classNameVar, primaryKeyStr,
				mapperPackage, primaryKey));
		sb.append(getOverride());
		sb.append(getQueryBySelective(classNameVar, mapperPackage));
		sb.append(getOverride());
		sb.append(getDeleteByPrimaryKey(primaryKeyStr, mapperPackage, classNameVar, primaryKey));
		sb.append(getOverride());
		sb.append(getInsert(classNameVar,mapperPackage));
		sb.append(getOverride());
		sb.append(getInsertSelective(classNameVar,mapperPackage));
		sb.append(getOverride());
		sb.append(getQueryByPagination(classNameVar,mapperPackage));
		sb.append(getOverride());
		sb.append(getUpdateByPrimaryKey(classNameVar,mapperPackage));
		sb.append(getOverride());
		sb.append(getUpdateByPrimaryKeySelective(classNameVar,mapperPackage));
		sb.append(getClassEnd());
		return sb.toString();
	}
	
	public void writeToFile(String arg){
		String className = StringUtil.toUpperCaseFristOne(StringUtil
				.toCamelCase(tableName));
		
		// 生成并写入进文件
		String daoImplPath = daoImplPackage.replaceAll("\\.", "/") + "/";// �����е�.����/
		String pathname = DAOImplGenerator.class.getResource("").toString();
		pathname = pathname.substring(6, pathname.length() - 4);
		pathname = pathname + daoImplPath + StringUtil.toUpperCaseFristOne(className) + "DAOImpl.java";
		FileUtil.writeToFile(pathname, arg);

	}

	
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setModelPackage(String modelPackage) {
		this.modelPackage = modelPackage;
	}
	public void setDaoImplPackage(String daoImplpackage){
		this.daoImplPackage = daoImplpackage;
	}
	public void setMapperPackage(String mapperPackage){
		this.mapperPackage=mapperPackage;
	}
	
	
	public static void main(String[] args) {
		// 初始化相关数据
		DAOImplGenerator generator = new DAOImplGenerator();
		generator.setTableName(ConfigPropertiesUtil.getProperty("tableName"));
		generator.setModelPackage(ConfigPropertiesUtil.getProperty("modelPackage"));
		generator.setDaoImplPackage(ConfigPropertiesUtil.getProperty("daoImplPackage"));
		generator.setMapperPackage(ConfigPropertiesUtil.getProperty("mapperPackage"));
//		String DaoImplStr=generator.getDaoImplStr();
//		generator.writeToFile(DaoImplStr);
	}

}











