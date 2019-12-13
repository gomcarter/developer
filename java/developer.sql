CREATE DATABASE developer;
USE developer;

CREATE TABLE `end` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) DEFAULT NULL COMMENT '项目名称',
  `prefix` varchar(50) DEFAULT NULL COMMENT '前缀',
  `jar_url` varchar(255) DEFAULT NULL COMMENT '登录的jar包地址',
  `kls` varchar(255) DEFAULT NULL COMMENT '登录方法对应的类',
  `method` varchar(255) DEFAULT NULL COMMENT '方法名',
  `args` varchar(255) DEFAULT NULL COMMENT 'json字符串格式:   {"java.lang.Long": 1, "java.lang.String": "13928001234"}',
  `header` varchar(100) DEFAULT NULL COMMENT 'header名',
  `mark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `prefix` (`prefix`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='前端项目';


CREATE TABLE `java` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) DEFAULT NULL COMMENT '模块名称',
  `dev_domain` varchar(100) DEFAULT NULL COMMENT '开发环境域名',
  `test_domain` varchar(100) DEFAULT NULL COMMENT '测试环境域名',
  `prev_domain` varchar(100) DEFAULT NULL COMMENT '预发环境域名',
  `online_domain` varchar(100) DEFAULT NULL COMMENT '线上环境域名',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='java模块';


CREATE TABLE `interfaces` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `hash` varchar(100) NOT NULL COMMENT '认定唯一接口标识符',
  `name` varchar(50) DEFAULT NULL COMMENT '接口名称',
  `controller` varchar(100) DEFAULT NULL COMMENT '控制器',
  `url` varchar(100) DEFAULT NULL COMMENT '接口地址，域名后面的一截如：http://g.cpsdb.com/platform/category中的platform/category',
  `method` varchar(10) DEFAULT NULL COMMENT 'GET, POST, PUT, PATCH, DELETE',
  `deprecated` tinyint(1) DEFAULT NULL COMMENT '是否已经废弃',
  `mark` text COMMENT '详细描述：如返回值说明，接口的一些说明等',
  `returns` text COMMENT '返回值数据结构',
  `parameters` text COMMENT '参数数据结构',
  `fk_java_id` bigint(20) DEFAULT NULL COMMENT '属于哪个java项目',
  `fk_end_id` bigint(20) DEFAULT NULL COMMENT '属于哪个前端项目',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `hash` (`hash`),
  KEY `fk_java_id` (`fk_java_id`),
  KEY `fk_end_id` (`fk_end_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='接口';

CREATE TABLE `interfaces_versioned` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `fk_interfaces_id` bigint(20) NOT NULL COMMENT '接口 id',
  `name` varchar(50) DEFAULT NULL COMMENT '接口名称',
  `controller` varchar(100) DEFAULT NULL COMMENT '控制器',
  `url` varchar(100) DEFAULT NULL COMMENT '接口地址，域名后面的一截如：http://g.cpsdb.com/platform/category中的platform/category',
  `method` varchar(10) DEFAULT NULL COMMENT 'GET, POST, PUT, PATCH, DELETE',
  `deprecated` tinyint(1) DEFAULT NULL COMMENT '是否已经废弃',
  `mark` text COMMENT '详细描述：如返回值说明，接口的一些说明等',
  `returns` text COMMENT '返回值数据结构',
  `parameters` text COMMENT '参数数据结构',
  `fk_java_id` bigint(20) DEFAULT NULL COMMENT '属于哪个java项目',
  `fk_end_id` bigint(20) DEFAULT NULL COMMENT '属于哪个前端项目',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_interfaces_id` (`fk_interfaces_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='接口历史版本';



CREATE TABLE `rules` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) DEFAULT NULL COMMENT '规则名称',
  `generator` text COMMENT '自动生成变量的javascript脚本',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='参数自动生成规则表';


CREATE TABLE `test_case` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(40) DEFAULT NULL COMMENT '用例名称',
  `fk_user_id` bigint(20) DEFAULT NULL COMMENT '用户id（谁建的）',
  `user_name` varchar(225) DEFAULT NULL COMMENT '用户名称（谁建的）',
  `mark` text COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='接口用例测试';


CREATE TABLE `test_case_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键，也作为用例接口的执行顺序',
  `name` varchar(40) DEFAULT NULL COMMENT '具体接口',
  `result_handler` text COMMENT '结果处理器，存储javascript脚本，此脚本应该return下一个接口的参数',
  `hash` text COMMENT '接口hash值',
  `config` text COMMENT '配置',
  `fk_interfaces_id` bigint(20) DEFAULT NULL COMMENT '接口外键',
  `fk_test_case_id` bigint(20) DEFAULT NULL COMMENT '接口用例外键',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='选中接口用例测试';



CREATE TABLE `validator` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) DEFAULT NULL COMMENT '规则名称',
  `handler` text COMMENT '验证值的javascript脚本',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='接口返回值校验';


