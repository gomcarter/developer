package it.gomcarter.frameworks.base.annotation;

import java.lang.annotation.*;

/**
 *
 * 先在入口Application加上  {@link NacosAutoConfig}
 * @author gomcarter on 2019-11-15 17:34:26
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface QiangDaNacosValue {

    /**
     * 如果配置为：1  =>   表达式：  group.dataId
     * <p>
     * 如果配置内容为： keyXXX=1   =>   表达式：  group.dataId.keyXXX
     *
     * <blockquote><pre>
     *     public class Foo {
     *         {@code @QiangDaNacosValue("CONFIG.ITEM.bar")}
     *          private SomeClass value;
     *     }
     * </pre></blockquote>
     *
     * <blockquote><pre>
     *     public class Bar {
     *         {@code @QiangDaNacosValue("CONFIG.ITEM")}
     *          private SomeClass value;
     *     }
     * </pre></blockquote>
     */
    String value();

    /**
     * 是否自动更新，当nacos中变化之后，对应变量自动更新
     */
    boolean autoRefreshed() default false;
}
