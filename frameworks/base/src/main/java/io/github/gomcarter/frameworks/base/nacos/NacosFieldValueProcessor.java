package io.github.gomcarter.frameworks.base.nacos;

import io.github.gomcarter.frameworks.base.annotation.QiangDaNacosValue;
import io.github.gomcarter.frameworks.base.common.NacosClientUtils;
import io.github.gomcarter.frameworks.base.common.ReflectionUtils;
import io.github.gomcarter.frameworks.base.spring.BeanFieldAndMethodProcessor;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * @author gomcarter on 2019-11-15 17:43:37
 */
public class NacosFieldValueProcessor extends BeanFieldAndMethodProcessor {

    @Override
    protected final void processField(Object bean, String beanName, Field field) {

        QiangDaNacosValue nacosPrimitiveValue = field.getAnnotation(QiangDaNacosValue.class);
        if (nacosPrimitiveValue == null) {
            return;
        }

        // 获取表达式
        String expression = nacosPrimitiveValue.value();
        String[] expressionArray = expression.split("\\.");
        // 校验
        if (expressionArray.length < 2) {
            throw new RuntimeException(bean.getClass().getName() + "." + field.getName() + "配置的表达式不正确");
        }

        // 获取dataId， group， 还有配置内容里面的key（如果没有key，那么所有配置内容都归这个变量）
        String[] keyArray = new String[expressionArray.length - 2];
        System.arraycopy(expressionArray, 2, keyArray, 0, expressionArray.length - 2);
        String group = expressionArray[0],
                dataId = expressionArray[1],
                key = StringUtils.join(keyArray, ".");

        String value;
        if (StringUtils.isNotBlank(key)) {
            // properties配置中的某一项配置属于这个变量
            Properties properties = NacosClientUtils.getConfigAsProperties(dataId, group);
            value = properties.getProperty(key);

            if (nacosPrimitiveValue.autoRefreshed()) {
                NacosClientUtils.addListenerAsProperties(dataId, group, (p) -> ReflectionUtils.setFieldIfNotMatchConvertIt(bean, field, p.get(key)));
            }
        } else {
            // 整个dataId + group的数据都属于这个变量
            value = NacosClientUtils.getConfigAsString(dataId, group);

            if (nacosPrimitiveValue.autoRefreshed()) {
                NacosClientUtils.addListener(dataId, group, (p) -> ReflectionUtils.setFieldIfNotMatchConvertIt(bean, field, p));
            }
        }

        ReflectionUtils.setFieldIfNotMatchConvertIt(bean, field, value);
    }

    @Override
    protected final void processMethod(Object bean, String beanName, Method method) {
        // do nothing
    }
}
