package com.yiayoframework.mybatis;

import com.yiayoframework.base.common.AssertUtils;
import com.yiayoframework.base.common.BeanRegistrationUtils;
import com.yiayoframework.base.exception.CustomException;
import com.yiayoframework.mybatis.annotation.EnableDiamondMybatis;
import com.yiayoframework.mybatis.factory.DiamondReadWriteDataSourceFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotationMetadata;

import java.util.HashMap;

/**
 * @author 李银 on 2019-11-09 23:31:48
 */
@Order
@Deprecated
public class DiamondMybatisRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata,
                                        BeanDefinitionRegistry registry) {

        AnnotationAttributes attributes = AnnotationAttributes
                .fromMap(annotationMetadata.getAnnotationAttributes(EnableDiamondMybatis.class.getName()));

        AssertUtils.notNull(attributes, new CustomException("未配置：EnableDiamondMybatis"));

        String dataId = attributes.getString("dataId");
        AssertUtils.isTrue(StringUtils.isNotBlank(dataId), new CustomException("未配置：dataId"));

        MybatisConfigHolder.DAO_XML_PATH = attributes.getString("daoXmlPath");
        MybatisConfigHolder.DAO_BASE_PACKAGE = attributes.getStringArray("daoBasePackage");
        MybatisConfigHolder.TRANSACTION_POINTCUT_EXPRESSION = attributes.getString("transactionPointcut");
        MybatisConfigHolder.TRANSACTION_REQUIRED_NAME_MAP = attributes.getStringArray("transactionRequiredNameMap");

        // 注入datasource
        BeanRegistrationUtils.registerBeanDefinitionIfNotExists(registry, "readWriteDataSource",
                DiamondReadWriteDataSourceFactory.class, new HashMap<String, Object>(2, 1) {{
                    put("dataId", dataId);
                    put("group", attributes.getString("group"));
                }});

        // 注入事务切面，主从选择切面，mapper注入等
        BeanRegistrationUtils.registerBeanDefinitionIfNotExists(registry, "mybatisConfiguration", MybatisConfiguration.class);
    }


}
