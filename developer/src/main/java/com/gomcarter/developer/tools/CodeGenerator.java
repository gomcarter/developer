package com.gomcarter.developer.tools;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;

public class CodeGenerator {
    /**
     * 外键标志, 如果Column名为， fk_user_id 则会自动生成 user 对象在目标类中
     */
    public final static String foreignPrefix = "fk_";

    //以下需要人工填写------------------------------------------------------------------------------
    /**
     * 项目绝对路径，如果不填写则为当前项目地址
     */
    public final static String hole_projectDir = null;
    public final static String basePackage = "com.gomcarter.developer";
    //子目录，填你想填
    public final static String subPackage = "";

    public final static String dbUrl = "jdbc:mysql://127.0.0.1:3306/developer";
    /* 数据库用户 */
    public final static String dbUsername = "root";
    /* 数据库密码 */
    public final static String dbPassword = "123";
    /* 表名 */
    //为""会则更新整个数据库
    public final static String generateTable = "";

    public final static String author = "gomcarter";

    //--------------------------------------------------------------------------------------
    /**
     * 初始化包名等
     */
    public static String dot = StringUtils.isBlank(subPackage) ? "" : ".";
    public final static String entityPackage = basePackage + dot + subPackage + ".entity";
    public final static String dtoPackage = basePackage + dot + subPackage + ".dto";
    public final static String paramPackage = basePackage + dot + subPackage + ".params";
    public final static String voPackage = basePackage + dot + subPackage + ".json";
    public final static String IDaoPackage = basePackage + dot + subPackage + ".dao";
    public final static String daoPackage = basePackage + dot + subPackage + ".dao";
    public final static String servicePackage = basePackage + dot + subPackage + ".service";
    public final static String serviceImplPackage = basePackage + dot + subPackage + ".service.impl";
    public final static String actionPackage = basePackage + dot + subPackage + ".controller";
    public final static String abstractActionPackage = basePackage + ".action.abs";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);

            DatabaseMetaData dbmd = conn.getMetaData();

            List<Entity> entityList = new ArrayList<Entity>();
            if ("".equals(generateTable)) {
                entityList = getTableEntitys(dbmd);
            } else {
                entityList.add(buildTableEntity(generateTable, dbmd));
            }
            conn.close();

            for (Entity entity : entityList) {
                //				generate(voPackage, "/src/main/java/", "J" + entity.className + ".java", "entityvo.ftl", entity, false);

                /*
                 * 状态机相关，不涉及忽略 if(entity.hasFsmInstance || entity.hasFsmLog) {
                 * if(entity.hasFsmInstance) { generate(servicePackage,
                 * "/src/main/java/", entity.fsmClassName + "FsmService.java",
                 * "fsmservice.ftl", entity, false); } generate(daoPackage,
                 * "/src/main/java/", entity.className + "DaoImpl.java",
                 * "fsmimpl.ftl", entity, false); } else {
                 */

                if (entity.hasFsmInstance || entity.hasFsmLog) {
                    if (entity.hasFsmInstance) {
                        generate(entityPackage, "/src/main/java/", entity.className + ".java", "fsmInstance.ftl", entity, true);
                        generate(IDaoPackage, "/src/main/java/", entity.className + "Mapper.java", "fsmInstanceMapper.ftl", entity, true);
                        generate("", "/src/main/resources/mybatis/" + subPackage + "/", entity.className + "Mapper.xml", "fsmInstanceMapper.xml.ftl", entity, true);
                    }

                    if (entity.hasFsmLog) {
                        generate(entityPackage, "/src/main/java/", entity.className + ".java", "fsmLog.ftl", entity, true);
                        generate(IDaoPackage, "/src/main/java/", entity.className + "Mapper.java", "fsmLogMapper.ftl", entity, true);
                        generate("", "/src/main/resources/mybatis/" + subPackage + "/", entity.className + "Mapper.xml", "fsmLogMapper.xml.ftl", entity, true);
                    }
                } else {
                    generate(entityPackage, "/src/main/java/", entity.className + ".java", "entity.ftl", entity, true);
                    generate(paramPackage, "/src/main/java/", "J" + entity.className + "QueryParams.java", "entityparams.ftl", entity, true);
                    generate(dtoPackage, "/src/main/java/", "J" + entity.className + ".java", "entitydto.ftl", entity, false);
//                    generate(actionPackage, "/src/main/java/", entity.className + "Controller.java", "controller.ftl", entity, false);
                    generate(servicePackage, "/src/main/java/", entity.className + "Service.java", "service.ftl", entity, false);
                    generate(IDaoPackage, "/src/main/java/", entity.className + "Mapper.java", "IMapperdao.ftl", entity, false);
                    generate("", "/src/main/resources/mybatis/" + subPackage + "/", entity.className + "Mapper.xml", "mapper.xml.ftl", entity, true);
                }
            }
        } catch (Throwable t) {
            System.out.println("error:" + t);
        }
        System.out.println("finish");
    }

    /**
     * @param packageName  : 包
     * @param prefix
     * @param suffix
     * @param templateName
     * @param entity
     * @param cover        ： true/覆盖； false/不覆盖
     * @throws IOException
     * @throws TemplateException
     */
    public static void generate(String packageName, String prefix, String suffix,
                                String templateName, Entity entity, boolean cover) throws IOException, TemplateException {
        String packagePath = packageName.replace('.', '/');
        String projectDir = StringUtils.defaultString(hole_projectDir, System.getProperty("user.dir"));

        String path = projectDir + prefix + packagePath + "/" + suffix;
        File file = new File(path);
        List<String> lines = null;
        if (file.exists()) {
            if (cover == false) {
                return;
            } else {
                lines = readNotReplacebleLines(file);
            }
        }
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        Configuration cfg = new Configuration();
        cfg.setDirectoryForTemplateLoading(new File(projectDir + "/src/main/resources/freemarker/"));
        Template hbmTemplate = cfg.getTemplate(templateName);
        Writer out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file), "UTF-8"));
        HashMap<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("entity", entity);
        hbmTemplate.process(dataMap, out);
        out.close();

        if (lines != null && lines.size() > 0) {
            replaceFile(file, lines);
        }
    }

    public static List<Entity> getTableEntitys(DatabaseMetaData dbmd) throws Exception {
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
            if (entity.manyToMany == false) {
                entityList.add(entity);
            }
        }

        return entityList;
    }

    public static Entity buildTableEntity(String table, DatabaseMetaData dbmd) throws Exception {
        Entity entity = new Entity();
        entity.author = author;
        entity.createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        entity.basePackageName = basePackage;
        entity.entityPackageName = entityPackage;
        entity.dtoPackageName = dtoPackage;
        entity.paramPackageName = paramPackage;
        entity.voPackage = voPackage;
        entity.daoPackageName = daoPackage;
        entity.servicePackageName = servicePackage;
        entity.serviceImplPackageName = serviceImplPackage;
        entity.actionPackageName = actionPackage;
        entity.IDaoPackageName = IDaoPackage;
        entity.abstractActionPackageName = abstractActionPackage;
        entity.tableName = table;
        entity.className = fillUpperName(removePrefix(table, ""));
        entity.classInstanceName = fillLowerName(removePrefix(table, ""));

        if (StringUtils.contains(entity.tableName, "_fsm_instance")) {
            entity.hasFsmInstance = true;
            entity.fsmClassName = fillUpperName(removeSuffix(entity.tableName, "_fsm_instance"));
            entity.fsmInstanceClassName = fillLowerName(removeSuffix(entity.tableName, "_fsm_instance"));
        }
        if (StringUtils.contains(entity.tableName, "_fsm_log")) {
            entity.hasFsmLog = true;
        }

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
            if (entity.idColumn == null) {
                entity.idColumn = columnName;
                entity.idName = fillLowerName(columnName);
                entity.idType = typeMap.get(rs.getString("TYPE_NAME"));
                entity.idSimpleType = simpleTypeMap.get(rs
                        .getString("TYPE_NAME"));
                entity.idGetMethod = buildGetMethod(entity.idName);
                entity.idSetMethod = buildSetMethod(entity.idName);
                entity.idAutoIncrement = "NO".equals(rs
                        .getString("IS_AUTOINCREMENT")) ? false : true;
            } else {
                if (columnName.equals(entity.idColumn)) {
                    entity.idName = fillLowerName(entity.idColumn);
                    entity.idType = typeMap.get(rs.getString("TYPE_NAME"));
                    entity.idSimpleType = simpleTypeMap.get(rs.getString("TYPE_NAME"));
                    entity.idGetMethod = buildGetMethod(entity.idName);
                    entity.idSetMethod = buildSetMethod(entity.idName);
                    entity.idAutoIncrement = "NO".equals(rs.getString("IS_AUTOINCREMENT")) ? false : true;
                    /**
                     * } else if(columnName.startsWith(foreignPrefix))
                     * {//如果columnName 开始于fk_， 那么是外键 AliasProperty aprop = new
                     * AliasProperty(); aprop.primaryTableName =
                     * fillLowerName(removeSuffix(removePrefix(columnName,
                     * foreignPrefix), "_id")); aprop.primaryClassName =
                     * fillUpperName(removePrefix(aprop.primaryTableName,
                     * foreignPrefix)); aprop.columnName = columnName;
                     * aprop.propName = aprop.primaryTableName ; aprop.getMethod
                     * = "get" + fillFirstUpper(aprop.propName); aprop.setMethod
                     * = "set" + fillFirstUpper(aprop.propName);
                     * entity.aPropList.add(aprop); entity.hasJoinColumn = true;
                     * entity.hasFetchType = true;
                     */
                } else {
                    Entity.Property prop = new Entity.Property();
                    prop.keep = !columnName.equals("modify_time") && !columnName.equals("create_time");
                    prop.column = columnName;
                    prop.propName = fillLowerName(prop.column);
                    prop.propType = typeMap.get(rs.getString("TYPE_NAME"));
                    if (prop.propType == null) {
                        throw new Exception("Unknow Database DataType: " + rs.getString("TYPE_NAME"));
                    }
                    prop.simpleType = simpleTypeMap.get(rs.getString("TYPE_NAME"));
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

    public static List<String> readNotReplacebleLines(File file) throws IOException {
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        List<String> lines = null;

        boolean start = false;
        String line = null;
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

    public static void replaceFile(File file, List<String> lines) throws IOException {
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

    public static void buildManyToMany(Entity.SetProperty sprop, DatabaseMetaData dbmd) throws Exception {
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

    public static String fillUpperName(String underline) {
        String lower = underline2camel(underline);
        return fillFirstUpper(lower);
    }

    public static String fillLowerName(String underline) {
        return underline2camel(underline);
    }

    public static String fillFirstUpper(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public static String fillFirstLower(String name) {
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }

    public static String underline2camel(String underline) {
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

    public static String removePrefix(String tableName, String prefix) {
        if (tableName.startsWith(prefix)) {
            return tableName.substring(prefix.length());
        } else {
            return tableName;
        }
    }

    public static String removeSuffix(String tableName, String suffix) {
        if (tableName.endsWith(suffix)) {
            return tableName.substring(0, tableName.length() - suffix.length());
        } else {
            return tableName;
        }
    }

    public static String buildGetMethod(String field, boolean isBoolean) {
        return "get" + field.substring(0, 1).toUpperCase() + field.substring(1);
    }

    public static String buildGetMethod(String field) {
        return buildGetMethod(field, false);
    }

    public static String buildSetMethod(String field) {
        return "set" + field.substring(0, 1).toUpperCase() + field.substring(1);
    }

    public static final Map<String, String> typeMap = new HashMap<String, String>();
    public static final Map<String, String> simpleTypeMap = new HashMap<String, String>();

    static {
        typeMap.put("INT UNSIGNED", "java.lang.Integer");
        typeMap.put("LONGTEXT", "java.lang.String");
        typeMap.put("INT", "java.lang.Integer");
        typeMap.put("BIGINT", "java.lang.Long");
        typeMap.put("DATE", "java.util.Date");
        typeMap.put("DATETIME", "java.util.Date");
        typeMap.put("TEXT", "java.lang.String");
        typeMap.put("VARCHAR", "java.lang.String");
        typeMap.put("DECIMAL", "java.math.BigDecimal");
        typeMap.put("BIT", "java.lang.Boolean");
        typeMap.put("TIMESTAMP", "java.util.Date");
        typeMap.put("FLOAT", "java.lang.Float");
        typeMap.put("DOUBLE", "java.lang.Double");
        typeMap.put("MEDIUMTEXT", "java.lang.String");
        typeMap.put("CHAR", "java.lang.String");
        typeMap.put("ENUM", "java.lang.String");
        typeMap.put("BLOB", "java.lang.String");
        typeMap.put("TINYINT", "java.lang.Boolean");

        simpleTypeMap.put("INT UNSIGNED", "Integer");
        simpleTypeMap.put("LONGTEXT", "String");
        simpleTypeMap.put("BLOB", "String");
        simpleTypeMap.put("TINYINT", "Boolean");
        simpleTypeMap.put("INT", "Integer");
        simpleTypeMap.put("BIGINT", "Long");
        simpleTypeMap.put("DATE", "Date");
        simpleTypeMap.put("DATETIME", "Date");
        simpleTypeMap.put("TEXT", "String");
        simpleTypeMap.put("VARCHAR", "String");
        simpleTypeMap.put("DECIMAL", "BigDecimal");
        simpleTypeMap.put("BIT", "Boolean");
        simpleTypeMap.put("TIMESTAMP", "Date");
        simpleTypeMap.put("FLOAT", "Float");
        simpleTypeMap.put("DOUBLE", "Double");
        simpleTypeMap.put("MEDIUMTEXT", "String");
        simpleTypeMap.put("CHAR", "String");
        simpleTypeMap.put("ENUM", "String");
    }
}
