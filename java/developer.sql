CREATE DATABASE developer;
USE developer;

CREATE TABLE `end` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name` varchar(50) DEFAULT NULL COMMENT '项目名称',
    `prefix` varchar(50) DEFAULT NULL COMMENT '前缀',
    `config` varchar(4096) DEFAULT NULL COMMENT '登录配置',
    `header` varchar(100) DEFAULT NULL COMMENT '需要的header',
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
    `url` varchar(100) DEFAULT NULL COMMENT '接口地址，域名后面的一截如：http://g.domain.com/end/category中的end/category',
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

CREATE TABLE `interfaces_package` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name` varchar(50) DEFAULT NULL COMMENT '打包名称',
    `mark` text COMMENT '详细描述：如返回值说明，接口的一些说明等',
    `test_case_id` bigint(20) DEFAULT NULL COMMENT '绑定一个test_case id',
    `user_name` varchar(225) DEFAULT NULL COMMENT '用户名称（谁建的）',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='接口打包';

CREATE TABLE `interfaces_package_item` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `interfaces_id` bigint(20) DEFAULT NULL COMMENT 'interfaces id',
    `interfaces_package_id` bigint(20) DEFAULT NULL COMMENT 'package id',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `interfaces_package_id` (`interfaces_package_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='接口打包行';

CREATE TABLE `interfaces_versioned` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `hash` varchar(100) NOT NULL COMMENT '认定唯一接口标识符',
    `fk_interfaces_id` bigint(20) NOT NULL COMMENT '接口 id',
    `name` varchar(50) DEFAULT NULL COMMENT '接口名称',
    `controller` varchar(100) DEFAULT NULL COMMENT '控制器',
    `url` varchar(100) DEFAULT NULL COMMENT '接口地址，域名后面的一截如：http://g.domain.com/end/category中的end/category',
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
    KEY `fk_interfaces_id` (`fk_interfaces_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='接口历史版本';

CREATE TABLE `custom_function` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name` varchar(50) DEFAULT NULL COMMENT '函数名称',
    `script_text` text COMMENT 'javascript脚本',
    `mark` varchar(2048) DEFAULT NULL COMMENT '脚本备注',
    `user_name` varchar(50) DEFAULT NULL,
    `is_public` tinyint(1) DEFAULT NULL,
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='自定义函数';

CREATE TABLE `test_case` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name` varchar(40) DEFAULT NULL COMMENT '用例名称',
    `user_name` varchar(225) DEFAULT NULL COMMENT '用户名称（谁建的）',
    `preset_params` text COMMENT '预置参数',
    `publics` tinyint(1) DEFAULT '0' COMMENT '是否共有',
    `mark` varchar(1024) DEFAULT NULL COMMENT '备注',
    `workflow` longtext COMMENT '详细接口配置: json数据结构',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='接口用例测试';

CREATE TABLE `mark` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user` varchar(200) DEFAULT NULL COMMENT '用户',
  `mark` text DEFAULT NULL COMMENT '备注内容',
  `fk_interfaces_id` bigint(20) NOT NULL COMMENT '接口 id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_interfaces_id` (`fk_interfaces_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='接口备注';

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(200) DEFAULT NULL COMMENT '登录账户名',
  `name` varchar(200) DEFAULT NULL COMMENT '姓名',
  `mail` varchar(200) DEFAULT NULL COMMENT '邮箱',
  `cellphone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `password` varchar(64) DEFAULT NULL COMMENT '密码',
  `random` varchar(10) NOT NULL COMMENT '随机数',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

# 默认密码 admin123
insert into user (username, name, password, random) values ('admin','admin','dacfab860b226140f3338df0875b9bf8','56174386');

CREATE TABLE `setting_of_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `remote` tinyint(1) DEFAULT NULL COMMENT '是否使用远程登录: 1-调用远程登录接口；2-使用本地user数据库',
  `url` varchar(200) DEFAULT NULL COMMENT '远程登录地址',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户设置表';

CREATE TABLE `end_auth` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user` varchar(50) DEFAULT NULL COMMENT '哪个用户的',
  `fk_end_id` varchar(50) DEFAULT NULL COMMENT '前端项目id',
  `config` varchar(8192) DEFAULT NULL COMMENT '登录配置',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_end_id` (`fk_end_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='前端项目个人账号密码管理';
