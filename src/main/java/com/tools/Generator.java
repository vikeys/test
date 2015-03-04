package com.tools;

import com.util.ConfigPropertiesUtil;

public class Generator {

//	private static String tableName = null;
//	private static String modelPackage = null;
//	private static String daoImplPackage = null;
//	private static String mapperPackage = null;
//	private static String mapperxmlPackage=null;

//	static {
//
//		if (tableName == null && modelPackage == null && mapperPackage == null
//				&& daoImplPackage == null && mapperPackage == null && mapperxmlPackage == null) {
//
//			tableName = ConfigPropertiesUtil.getProperty("tableName");
//			modelPackage = ConfigPropertiesUtil.getProperty("modelPackage");
//			mapperPackage = ConfigPropertiesUtil.getProperty("mapperPackage");
//			daoImplPackage = ConfigPropertiesUtil.getProperty("daoImplPackage");
//			mapperPackage = ConfigPropertiesUtil.getProperty("mapperPackage");
//			mapperxmlPackage = ConfigPropertiesUtil.getProperty("mapperxmlPackage");
//		}
//	}

	public void createModel(String tableName, String modelPackage) {
		
		ModelGenerator generator = new ModelGenerator();
		
//		String modelStr = generator.getModelStr(tableName, modelPackage);
//		generator.writeToFile(modelStr, tableName, modelPackage);
	}
	
	public void createMapper(String tableName, String modelPackage, String mapperPackage) {
		
		MapperGenerator generator = new MapperGenerator();
		
		generator.setTableName(tableName);
		generator.setModelPackage(modelPackage);
		generator.setMapperPackage(mapperPackage);
		
//		String mapperStr = generator.getMapperStr();
//		generator.writeToFile(mapperStr);
	}
	
	public void createDaoImpl(String tableName, String mapperPackage, String modelPackage, String daoImplPackage){
		
		DAOImplGenerator generator=new DAOImplGenerator();
		
		generator.setTableName(tableName);
		generator.setModelPackage(modelPackage);
		generator.setMapperPackage(mapperPackage);
		generator.setDaoImplPackage(daoImplPackage);
		
//		String daoimplStr = generator.getDaoImplStr();
//		generator.writeToFile(daoimplStr);
	}
	
	public void createMapperXML(String mapperxmlPackage, String modelPackage, String tableName){
		
		MapperXMLGenerator generator=new MapperXMLGenerator();
		
		generator.setMapperxmlPackage(mapperxmlPackage);
		generator.setModelPackage(modelPackage);
		generator.setTableName(tableName);
		
//		String mapperXMLStr=generator.getMapperXMLStr();
//		generator.writeToFile(mapperXMLStr);
		
	}

	public static void main(String args[]) {
		Generator generator = new Generator();
		
//		generator.createModel();
//		generator.createMapper();
//		generator.createDaoImpl();
//		generator.createMapperXML();
	
	}

}
