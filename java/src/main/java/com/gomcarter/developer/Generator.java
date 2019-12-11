package com.gomcarter.developer;


import com.gomcarter.frameworks.mybatis.tools.CodeGenerator;

/**
 * 代码自动生成
 *
 * @author 李银 on 2019-09-02 22:02:08
 */
public class Generator {

    public static void main(String[] args) {
        /*
         * 项目绝对路径，如果不填写则为当前项目地址 classpath
         */
        CodeGenerator.PROJECT_ABSOLUTE_DIR = null;
        /*
         * 生成代码输出的初始包路径
         */
        CodeGenerator.BASE_PACKAGE = Generator.class.getPackage().getName();
        /*
         * 生成代码输出的初始包路径下的子目录，自定义
         * 如：abc, 那么文件就输出到  com.gomcarter.test.abc下
         */
        CodeGenerator.SUB_PACKAGE = "";
        /*
         * 数据库连接地址
         */
        CodeGenerator.DB_URL = "jdbc:mysql://127.0.0.1:3306/item";
        /*
         * 数据库用户
         */
        CodeGenerator.DB_USERNAME = "root";
        /*
         * 数据库密码
         */
        CodeGenerator.DB_PASSWORD = "123456";
        /*
         * 表名， 为空会则生成整个数据库的所有表
         */
        CodeGenerator.TOBE_GENERATE_TABLES = "";
        /*
         * 作者信息
         */
        CodeGenerator.AUTHOR = "gomcarter";

        // 添加数据库字段映射成 java 类
        // CodeGenerator.addTypeMapping("", Integer.class);

        // 开始执行
        CodeGenerator.main(null);
    }
}
