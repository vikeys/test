package com.tools;

/**
 * Created with IntelliJ IDEA.
 * User: liwei
 * Date: 15-2-26
 * Time: 下午3:37
 * To change this template use File | Settings | File Templates.
 */
public class GeneratorManager {

    private final static GeneratorManager generatorManager = new GeneratorManager();

    private DBConnector dbConnector;

    public static GeneratorManager getConnection(String driverName, String sqlUrl, String sqlUser, String sqlPassword){

        generatorManager.setDbConnector(new DBConnector(driverName,sqlUrl,sqlUser,sqlPassword));
        return generatorManager;
    }

    public void createModel(String tableName, String modelPackage){
        ModelGenerator modelGenerator = new ModelGenerator();
        modelGenerator.setTableName(tableName);
        modelGenerator.setModelPackage(modelPackage);

        String modelStr = modelGenerator.getModelStr(generatorManager.getDbConnector());
        modelGenerator.writeToFile(modelStr);
    }

    public void createMapper(String tableName, String modelPackage, String mapperPackage){

        MapperGenerator mapperGenerator = new MapperGenerator();
        mapperGenerator.setTableName(tableName);
        mapperGenerator.setModelPackage(modelPackage);
        mapperGenerator.setMapperPackage(mapperPackage);

		String mapperStr = mapperGenerator.getMapperStr(generatorManager.getDbConnector());
		mapperGenerator.writeToFile(mapperStr);
    }

    /**
     * 生成mapperXML.
     * @param tableName 表名
     * @param modelPackage
     * @param mapperxmlPackage
     */
    public void createMapperXML(String tableName, String modelPackage, String mapperxmlPackage){

        MapperXMLGenerator generator=new MapperXMLGenerator();

        generator.setMapperxmlPackage(mapperxmlPackage);
        generator.setModelPackage(modelPackage);
        generator.setTableName(tableName);

		String mapperXMLStr=generator.getMapperXMLStr(generatorManager.getDbConnector());
		generator.writeToFile(mapperXMLStr);
    }

    public DBConnector getDbConnector() {
        return dbConnector;
    }

    public void setDbConnector(DBConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    public static void main(String[] args) {

        getConnection("com.microsoft.sqlserver.jdbc.SQLServerDriver","jdbc:sqlserver://10.0.11.101:1433;DatabaseName=YGT_DF_DB","test-ygt","lezhi1204")
                .createMapperXML("t_house_real","com.model","mapper");
    }
}
