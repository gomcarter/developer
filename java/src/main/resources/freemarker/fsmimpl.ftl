package ${entity.daoPackageName};

import org.springframework.stereotype.Repository;

<#if entity.hasFsmInstance>
import ${entity.basePackageName}.dao.AbstractFsmInstanceDao;
import ${entity.basePackageName}.dao.hibernate.HibernateDao;
import ${entity.basePackageName}.domain.AbstractFsmInstance;
import ${entity.entityPackageName}.${entity.className};
<#else>
    <#if entity.hasFsmLog>
import org.hibernate.criterion.Order;
import java.util.List;
import ${entity.basePackageName}.dao.AbstractFsmLogDao;
import ${entity.basePackageName}.dao.hibernate.HibernateDao;
import ${entity.basePackageName}.domain.AbstractFsmLog;
import ${entity.entityPackageName}.${entity.className};
    </#if>
</#if>

@Repository
public class ${entity.className}DaoImpl
<#if entity.hasFsmInstance>
	extends AbstractFsmInstanceDao<${entity.className}> {
<#else>
    <#if entity.hasFsmLog>
	extends AbstractFsmLogDao<${entity.className}> {
    </#if>
</#if>
}
