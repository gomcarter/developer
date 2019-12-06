<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!-- @author ${entity.author} on ${entity.createTime} -->
<mapper namespace="${entity.daoPackageName}.${entity.className}Mapper">
    <resultMap type="${entity.entityPackageName}.${entity.className}" id="${entity.classInstanceName}Map">
        <id column="${entity.idColumn}" property="${entity.idName}"/>
        <#list entity.propList as prop>
            <result column="${prop.column}" property="${prop.propName}"/>
        </#list>
        <#if entity.hasHibernateVersion>
            <result column="version" property="version"/>
        </#if>
    </resultMap>
    <!-- //@NotReplaceableStart -->
    <!-- 重新生成代码时，NotReplaceableStart -> NotReplaceableEnd 中间的内容不会被覆盖 -->


    <!-- //@NotReplaceableEnd  -->
</mapper>
