package it.gomcarter.frameworks.dubbo.factory;

import com.alibaba.nacos.api.PropertyKeyConst;
import it.gomcarter.frameworks.base.common.*;
import it.gomcarter.frameworks.dubbo.annotation.EnableNacosDubbo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author gomcarter on 2019-11-09 23:31:48
 */
@Order
@Slf4j
public class DubboRegistryImporter implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata,
                                        BeanDefinitionRegistry registry) {
        // 某些bug，必须先设置这一句，不然会报错
        System.setProperty("dubbo.application.logger", "slf4j");

        AnnotationAttributes attributes = AnnotationAttributes
                .fromMap(annotationMetadata.getAnnotationAttributes(EnableNacosDubbo.class.getName()));
        AssertUtils.notNull(attributes, new RuntimeException("未配置：EnableNacosDubbo"));

        String dataId = attributes.getString("dataId"),
                group = attributes.getString("group");
        AssertUtils.isTrue(StringUtils.isNotBlank(dataId), new RuntimeException("未配置：dataId"));
        AssertUtils.isTrue(StringUtils.isNotBlank(group), new RuntimeException("未配置：group"));

        RegistryConfig rc = new RegistryConfig();
        ApplicationConfig ac = new ApplicationConfig();
        ProtocolConfig pc = new ProtocolConfig();

        // 从Nacos中读取配置
        Properties properties = NacosClientUtils.getConfigAsProperties(dataId, group);
        // 从nacos读取注册中心地址，如果没有获取到，则直接用配置中心作为注册中心（改一下注册中心的协议前缀即可）
        rc.setAddress(StringUtils.defaultIfBlank(properties.getProperty("dubbo.registry.address"),
                "nacos://" + NacosClientUtils.serverAddr().split("://")[1]));
        // 设置namespace， nacos注册中心有效
        Map<String, String> parameters = new HashMap<>(2, 1);
        parameters.put(PropertyKeyConst.NAMESPACE, NacosClientUtils.namespace());
        rc.setParameters(parameters);

        for (Field field : ReflectionUtils.findAllField(RegistryConfig.class)) {
            String value = properties.getProperty("dubbo.registry." + field.getName());
            if (StringUtils.isNotBlank(value)) {
                ReflectionUtils.setFieldIfNotMatchConvertIt(rc, field, value);
            }
        }

        Class entryClass = ((StandardAnnotationMetadata) annotationMetadata).getIntrospectedClass();
        String entryPackage = entryClass.getPackage().getName();

        // 设置application
        ac.setName(StringUtils.defaultIfBlank(attributes.getString("appName"), entryClass.getName()));
        for (Field field : ReflectionUtils.findAllField(ApplicationConfig.class)) {
            String value = properties.getProperty("dubbo.application." + field.getName());
            if (StringUtils.isNotBlank(value)) {
                ReflectionUtils.setFieldIfNotMatchConvertIt(ac, field, value);
            }
        }

        // 设置proptocol
        pc.setPort(attributes.getNumber("port"));
        for (Field field : ReflectionUtils.findAllField(ProtocolConfig.class)) {
            String value = properties.getProperty("dubbo.protocol." + field.getName());
            if (StringUtils.isNotBlank(value)) {
                ReflectionUtils.setFieldIfNotMatchConvertIt(pc, field, value);
            }
        }

        // 取配置中心的基础包，如果没有配置，则取打了EnableNacosDubbo的类所在的包路径
        String basePackage = StringUtils.defaultIfBlank(properties.getProperty("dubbo.registry.basePackage"), entryPackage);
        // 扫描包，找所有包含了org.apache.dubbo.config.annotation.Service的类，注册为bean
        for (String pkg : basePackage.split(",")) {
            for (Class kls : PackageUtils.getClasses(pkg)) {
                Annotation service = kls.getAnnotation(Service.class);
                if (service != null) {
                    String beanName = kls.getSimpleName().substring(0, 1) + kls.getSimpleName().substring(1);
                    BeanRegistrationUtils.registerBeanDefinitionIfNotExists(registry, beanName, kls);
                }
            }
        }

        // 注册
        BeanRegistrationUtils.registerBeanDefinitionIfNotExists(registry, "dubboApiRegistrar", DubboApiRegistrar.class,
                new HashMap<String, Object>() {{
                    put("rc", rc);
                    put("ac", ac);
                    put("pc", pc);
                }});
    }
}
