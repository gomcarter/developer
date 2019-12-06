package com.gomcarter.frameworks.mybatis.tools;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 使用，自建一个类
 * <blockquote><pre>
 *  import com.gomcarter.frameworks.mybatis.tools.CodeGenerator;
 *
 *  public class Generator {
 *      public static void main(String[] args) {
 *          // 项目绝对路径，如果不填写则为当前项目地址 classpath
 *          CodeGenerator.PROJECT_ABSOLUTE_DIR = null;
 *          // 生成代码输出的初始包路径
 *          CodeGenerator.BASE_PACKAGE = "com.gomcarter.test";
 *          // 生成代码输出的初始包路径下的子目录，自定义
 *          // 如：abc, 那么文件就输出到  com.gomcarter.test.abc下
 *          CodeGenerator.SUB_PACKAGE = "";
 *          // 数据库连接地址
 *          CodeGenerator.DB_URL = "jdbc:mysql://127.0.0.1:3306/test?useSSL=true";
 *          // 数据库用户
 *          CodeGenerator.DB_USERNAME = "root";
 *          // 数据库密码
 *          CodeGenerator.DB_PASSWORD = "123456";
 *          // 需要自动代码的表名， 为空会则生成整个数据库的所有表
 *          CodeGenerator.TOBE_GENERATE_TABLES = "item";
 *          // 作者信息
 *          CodeGenerator.AUTHOR = "gomcarter";
 *          // 开始执行
 *          CodeGenerator.main(null);
 *      }
 *  }
 * </pre></blockquote>
 *
 * @author gomcarter on 2019-12-05 22:47:06
 */
public class CodeGenerator {
    //以下需要人工填写------------------------------------------------------------------------------
    /**
     * 项目绝对路径，如果不填写则为当前项目地址 classpath
     */
    public static String PROJECT_ABSOLUTE_DIR = null;

    /**
     * 生成代码输出的初始包路径
     */
    public static String BASE_PACKAGE = "com.gomcarter.test";
    /**
     * 生成代码输出的初始包路径下的子目录，自定义
     * 如：abc, 那么文件就输出到  com.gomcarter.test.abc下
     */
    public static String SUB_PACKAGE = "";
    /**
     * 数据库连接地址
     */
    public static String DB_URL = "jdbc:mysql://127.0.0.1:3306/test?useSSL=true";
    /**
     * 数据库用户
     */
    public static String DB_USERNAME = "root";
    /**
     * 数据库密码
     */
    public static String DB_PASSWORD = "123456";
    /**
     * 需要自动代码的表名， 为空会则生成整个数据库的所有表
     */
    public static String TOBE_GENERATE_TABLES = "";
    /**
     * 作者信息
     */
    public static String AUTHOR = "gomcarter";


    //--------------------------------------------------------------------------------------
    /**
     * 外键标志, 如果Column名为， fk_user_id 则会自动生成 user 对象在目标类中
     */
    private final static String foreignPrefix = "fk_";

    /**
     * 初始化包名等
     */
    private static class Config {
        private String subPackage;
        private String entityPackage;
        private String dtoPackage;
        private String paramPackage;
        private String daoPackage;
        private String servicePackage;
        private String actionPackage;
    }

    private static Config config = new Config();

    public static void main(String[] args) {
        config.subPackage = SUB_PACKAGE == null || SUB_PACKAGE.trim().length() == 0 ? "" : ("." + SUB_PACKAGE);
        config.entityPackage = BASE_PACKAGE + config.subPackage + ".entity";
        config.dtoPackage = BASE_PACKAGE + config.subPackage + ".dto";
        config.paramPackage = BASE_PACKAGE + config.subPackage + ".params";
        config.daoPackage = BASE_PACKAGE + config.subPackage + ".dao";
        config.servicePackage = BASE_PACKAGE + config.subPackage + ".service";
        config.actionPackage = BASE_PACKAGE + config.subPackage + ".controller";


        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {

            DatabaseMetaData dbmd = conn.getMetaData();

            List<Entity> entityList = new ArrayList<>();
            if ("".equals(TOBE_GENERATE_TABLES)) {
                entityList = getTableEntities(dbmd);
            } else {
                entityList.add(buildTableEntity(TOBE_GENERATE_TABLES, dbmd));
            }

            for (Entity entity : entityList) {
                generate(config.entityPackage, "/src/main/java/", entity.className + ".java", "entity.ftl", entity, true);
                generate(config.dtoPackage, "/src/main/java/", entity.className + "Dto.java", "entitydto.ftl", entity, false);
                generate(config.paramPackage, "/src/main/java/", entity.className + "Param.java", "entityparam.ftl", entity, false);
                generate(config.actionPackage, "/src/main/java/", entity.className + "Controller.java", "controller.ftl", entity, false);
                generate(config.servicePackage, "/src/main/java/", entity.className + "Service.java", "service.ftl", entity, false);
                generate(config.daoPackage, "/src/main/java/", entity.className + "Mapper.java", "IMapperdao.ftl", entity, false);
                generate("", "/src/main/resources/mybatis/" + SUB_PACKAGE + "/", entity.className + "Mapper.xml", "mapper.xml.ftl", entity, true);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }

        System.out.println("finish");
    }

    /**
     * @param packageName  包
     * @param prefix       prefix
     * @param suffix       suffix
     * @param templateName templateName
     * @param entity       entity
     * @param cover        true/覆盖； false/不覆盖
     * @throws Exception exception
     */
    private static void generate(String packageName, String prefix, String suffix,
                                 String templateName, Entity entity, boolean cover) throws Exception {
        String packagePath = packageName.replace('.', '/');
        String projectDir = PROJECT_ABSOLUTE_DIR == null || PROJECT_ABSOLUTE_DIR.trim().length() == 0 ? System.getProperty("user.dir") : PROJECT_ABSOLUTE_DIR;

        String path = projectDir + prefix + packagePath + "/" + suffix;
        File file = new File(path);
        List<String> lines = null;
        if (file.exists()) {
            if (!cover) {
                return;
            } else {
                lines = readNotReplaceableLines(file);
            }
        }
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        Configuration cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        cfg.setClassForTemplateLoading(CodeGenerator.class, "/mybatisstarterftl/");

        Template hbmTemplate = cfg.getTemplate(templateName);

        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("entity", entity);
        hbmTemplate.process(dataMap, out);

        out.close();

        if (lines != null && lines.size() > 0) {
            replaceFile(file, lines);
        }
    }

    private static List<Entity> getTableEntities(DatabaseMetaData dbmd) throws Exception {
        ResultSet rs = dbmd.getTables(null, null, null, new String[]{"table"});
        List<String> tables = new ArrayList<String>();
        while (rs.next()) {
            System.out.println(rs.getString("TABLE_NAME"));
            tables.add(rs.getString("TABLE_NAME"));
        }
        rs.close();

        List<Entity> entityList = new ArrayList<Entity>();
        for (String table : tables) {
            Entity entity = buildTableEntity(table, dbmd);
            if (!entity.manyToMany) {
                entityList.add(entity);
            }
        }

        return entityList;
    }

    private static Entity buildTableEntity(String table, DatabaseMetaData dbmd) throws Exception {
        Entity entity = new Entity();
        entity.author = AUTHOR;
        entity.createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        entity.basePackageName = BASE_PACKAGE;
        entity.entityPackageName = config.entityPackage;
        entity.dtoPackageName = config.dtoPackage;
        entity.paramPackage = config.paramPackage;
        entity.daoPackageName = config.daoPackage;
        entity.servicePackageName = config.servicePackage;
        entity.actionPackageName = config.actionPackage;
        entity.tableName = table;
        entity.className = fillUpperName(removePrefix(table, ""));
        entity.classInstanceName = fillLowerName(removePrefix(table, ""));

        ResultSet rs = dbmd.getPrimaryKeys(null, null, table);
        while (rs.next()) {
            if (entity.idColumn != null) {
                entity.manyToMany = true;
                return entity;
            }
            entity.idColumn = rs.getString("COLUMN_NAME");
        }
        rs.close();

        rs = dbmd.getImportedKeys(null, null, table);
        while (rs.next()) {
            Entity.AliasProperty aprop = new Entity.AliasProperty();
            aprop.primaryTableName = rs.getString("PKTABLE_NAME");
            aprop.primaryClassName = fillUpperName(removePrefix(aprop.primaryTableName, foreignPrefix));
            aprop.columnName = rs.getString("FKCOLUMN_NAME");
            String str = fillLowerName(aprop.columnName);
            aprop.propName = str.endsWith("Id") ? str.substring(0, str.length() - 2) : str;
            aprop.getMethod = "get" + fillFirstUpper(aprop.propName);
            aprop.setMethod = "set" + fillFirstUpper(aprop.propName);
            entity.aPropList.add(aprop);
        }
        rs.close();

        rs = dbmd.getExportedKeys(null, null, table);
        //		List<String> columnNames = new ArrayList<String>(24);
        //		ResultSetMetaData rsmd = rs.getMetaData();
        //		int columnCount = rsmd.getColumnCount();
        //		System.out.println("column count:" + columnCount);
        //		for (int i = 1; i <= columnCount; i++) {
        //			columnNames.add(rsmd.getColumnName(i));
        //		}
        while (rs.next()) {
            //			for (String columnName : columnNames) {
            //				System.out.println(columnName + ": " + rs.getObject(columnName));
            //			}

            if (table.equals(rs.getString("PKTABLE_NAME"))) {
                Entity.SetProperty sprop = new Entity.SetProperty();

                sprop.foreignTableName = rs.getString("FKTABLE_NAME");
                sprop.foreignClassName = fillUpperName(removePrefix(sprop.foreignTableName, foreignPrefix));
                sprop.foreignColumnName = rs.getString("FKCOLUMN_NAME");
                sprop.propName = fillFirstLower(sprop.foreignClassName) + "Set";
                sprop.getMethod = buildGetMethod(sprop.propName);
                sprop.setMethod = buildSetMethod(sprop.propName);

                buildManyToMany(sprop, dbmd);

                entity.sPropList.add(sprop);
                entity.hasSetType = true;
            }
        }
        rs.close();

        rs = dbmd.getColumns(null, null, table, "%");
        while (rs.next()) {
            String columnName = rs.getString("COLUMN_NAME");
            if ("version".equalsIgnoreCase(columnName)) {
                entity.hasHibernateVersion = true;
                entity.versionLock = "#{version}";
                continue;
            }
            entity.cloumnName = columnName;
            // 对于视图没有主键的问题，默认以第一列为主键 Modified by Squid Hex
            // Now you can generate code by view
            Class kls = typeMap.get(rs.getString("TYPE_NAME"));
            if (kls == null) {
                System.out.println("Unknown Database DataType: " + rs.getString("TYPE_NAME") +
                        ", String.class are replaced default,  you can call CodeGenerator.addTypeMapping(.., ..) to correct it also");
                kls = String.class;
            }

            if (entity.idColumn == null) {
                entity.idColumn = columnName;
                entity.idName = fillLowerName(columnName);

                entity.idType = kls.getName();
                entity.idSimpleType = kls.getSimpleName();
                entity.idGetMethod = buildGetMethod(entity.idName);
                entity.idSetMethod = buildSetMethod(entity.idName);
                entity.idAutoIncrement = !"NO".equals(rs.getString("IS_AUTOINCREMENT"));
            } else {
                if (columnName.equals(entity.idColumn)) {
                    entity.idName = fillLowerName(entity.idColumn);
                    entity.idType = kls.getName();
                    entity.idSimpleType = kls.getSimpleName();
                    entity.idGetMethod = buildGetMethod(entity.idName);
                    entity.idSetMethod = buildSetMethod(entity.idName);
                    entity.idAutoIncrement = !"NO".equals(rs.getString("IS_AUTOINCREMENT"));
                } else {
                    Entity.Property prop = new Entity.Property();
                    prop.column = columnName;
                    prop.propName = fillLowerName(prop.column);
                    prop.propType = kls.getName();
                    prop.simpleType = kls.getSimpleName();
                    if ("BigDecimal".equals(prop.simpleType)) {
                        entity.hasDecimalType = true;
                    } else if ("Date".equals(prop.simpleType)) {
                        entity.hasDateType = true;
                    }
                    prop.notAllowNull = "NO".equals(rs.getString("IS_NULLABLE")) ?
                            (rs.getString("COLUMN_DEF") == null ? "true" : "false")
                            : "false";
                    prop.note = rs.getString("REMARKS");
                    prop.getMethod = buildGetMethod(prop.propName, "Boolean".equals(prop.simpleType));
                    prop.setMethod = buildSetMethod(prop.propName);
                    for (Entity.AliasProperty aprop : entity.aPropList) {
                        if (prop.column.equals(aprop.columnName)) {
                            prop.manyToOne = true;
                            break;
                        }
                    }
                    entity.propList.add(prop);
                }
            }
        }
        rs.close();
        if (!entity.hasHibernateVersion) {
            System.out.println("Warning: table[" + entity.tableName + "] dosn't have a version column, 不需要乐观锁忽略");
        }
        return entity;
    }

    private static List<String> readNotReplaceableLines(File file) throws IOException {
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        List<String> lines = null;

        boolean start = false;
        String line;
        while ((line = br.readLine()) != null) {
            if (line.contains("//@NotReplaceableStart")) {
                lines = new ArrayList<String>();
                start = true;
            }

            if (start) {
                lines.add(line);
            }

            if (line.contains("//@NotReplaceableEnd")) {
                start = false;
            }

        }
        br.close();
        fr.close();

        return lines;
    }

    private static void replaceFile(File file, List<String> lines) throws IOException {
        File destFile = new File(file.getPath() + ".temp");
        if (!file.exists()) {
            return;
        }
        FileReader scrR = new FileReader(file);
        BufferedReader srcB = new BufferedReader(scrR);

        FileWriter fw = new FileWriter(destFile);
        String lineS = null;

        boolean replace = false;
        while ((lineS = srcB.readLine()) != null) {
            if (lineS.contains("//@NotReplaceableStart")) {
                replace = true;

                for (String change : lines) {
                    fw.write(change + "\r\n");
                }
            }

            if (!replace) {
                fw.write(lineS + "\r\n");
            }

            if (lineS.contains("//@NotReplaceableEnd")) {
                replace = false;
            }
        }

        fw.close();
        srcB.close();
        scrR.close();

        FileUtils.copyFile(destFile, file);
        destFile.delete();
    }

    private static void buildManyToMany(Entity.SetProperty sprop, DatabaseMetaData dbmd) throws Exception {
        ResultSet rs = dbmd.getPrimaryKeys(null, null, sprop.foreignTableName);
        int primaryKeyNumber = 0;
        while (rs.next()) {
            primaryKeyNumber++;
        }
        if (primaryKeyNumber > 1) {
            sprop.manyToMany = true;
            rs.close();

            rs = dbmd.getImportedKeys(null, null, sprop.foreignTableName);
            while (rs.next()) {
                if (!sprop.foreignColumnName.equals(rs.getString("FKCOLUMN_NAME"))) {
                    sprop.manyToManyTable = sprop.foreignTableName;
                    sprop.foreignTableName = rs.getString("PKTABLE_NAME");
                    sprop.foreignClassName = fillUpperName(removePrefix(sprop.foreignTableName, ""));
                    sprop.propName = fillFirstLower(sprop.foreignClassName) + "Set";
                    sprop.getMethod = buildGetMethod(sprop.propName);
                    sprop.setMethod = buildSetMethod(sprop.propName);
                    sprop.manyForeignColumnName = rs.getString("FKCOLUMN_NAME");
                    break;
                }
            }
            rs.close();
        }
    }

    private static String fillUpperName(String underline) {
        String lower = underline2camel(underline);
        return fillFirstUpper(lower);
    }

    private static String fillLowerName(String underline) {
        return underline2camel(underline);
    }

    private static String fillFirstUpper(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    private static String fillFirstLower(String name) {
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }

    private static String underline2camel(String underline) {
        StringBuilder sb = new StringBuilder();
        String temp = underline;
        while (true) {
            int index = temp.indexOf("_");
            if (index == -1) {
                sb.append(temp);
                break;
            } else {
                sb.append(temp.substring(0, index));
                sb.append(temp.substring(index + 1, index + 2).toUpperCase());
                temp = temp.substring(index + 2);
            }
        }
        return sb.toString();
    }

    private static String removePrefix(String tableName, String prefix) {
        if (tableName.startsWith(prefix)) {
            return tableName.substring(prefix.length());
        } else {
            return tableName;
        }
    }

    private static String removeSuffix(String tableName, String suffix) {
        if (tableName.endsWith(suffix)) {
            return tableName.substring(0, tableName.length() - suffix.length());
        } else {
            return tableName;
        }
    }

    private static String buildGetMethod(String field, boolean isBoolean) {
        return "get" + field.substring(0, 1).toUpperCase() + field.substring(1);
    }

    private static String buildGetMethod(String field) {
        return buildGetMethod(field, false);
    }

    private static String buildSetMethod(String field) {
        return "set" + field.substring(0, 1).toUpperCase() + field.substring(1);
    }

    private static final Map<String, Class> typeMap = new HashMap<>();

    static {
        typeMap.put("LONGTEXT", String.class);
        typeMap.put("INT", Integer.class);
        typeMap.put("BIGINT", Long.class);
        typeMap.put("DATE", Date.class);
        typeMap.put("DATETIME", Date.class);
        typeMap.put("TEXT", String.class);
        typeMap.put("VARCHAR", String.class);
        typeMap.put("DECIMAL", BigDecimal.class);
        typeMap.put("BIT", Boolean.class);
        typeMap.put("TIMESTAMP", Date.class);
        typeMap.put("FLOAT", Float.class);
        typeMap.put("DOUBLE", Double.class);
        typeMap.put("MEDIUMTEXT", String.class);
        typeMap.put("CHAR", String.class);
        typeMap.put("ENUM", String.class);
        typeMap.put("BLOB", String.class);
        typeMap.put("TINYINT", Boolean.class);
    }

    public void addTypeMapping(String dbType, Class javaType) {
        typeMap.put(dbType, javaType);
    }
}
