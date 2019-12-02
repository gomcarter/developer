package it.gomcarter.frameworks.base.annotation;

import it.gomcarter.frameworks.base.nacos.NacosFieldValueProcessor;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 自动注入配置到变量
 * <p>
 * 如：{@link QiangDaNacosValue}
 *
 * @author gomcarter on 2019-11-15 17:34:26
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(NacosFieldValueProcessor.class)
public @interface NacosAutoConfig {

}
