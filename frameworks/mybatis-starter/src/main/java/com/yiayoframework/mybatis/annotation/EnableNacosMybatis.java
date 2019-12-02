package com.yiayoframework.mybatis.annotation;

import com.yiayoframework.mybatis.NacosMybatisRegistrar;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author 李银 on 2019-11-09 22:53:32
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(NacosMybatisRegistrar.class)
public @interface EnableNacosMybatis {
    /**
     * nacos data_id
     */
    @AliasFor("value")
    String dataId() default "";

    /**
     * data_id
     */
    @AliasFor("dataId")
    String value() default "";

    /**
     * nacos group
     */
    String group() default "MYSQL";

    /**
     * 如： classpath:mybatis/ ** / *Mapper.xml
     */
    String daoXmlPath() default "classpath:mybatis/**/*Mapper.xml";

    /**
     * 如： com.yiayo.*.dao
     */
    String[] daoBasePackage() default {"com.**.dao"};

    /**
     * 所有service包下的类的所有方法
     */
    String transactionPointcut() default "execution(* com..*.service..*.*(..))";

    /**
     * 所有service中方法名以此开头的开启写事务，并走主库
     * <p>
     * 否则readonly，并周从库
     */
    String[] transactionRequiredNameMap() default {
            "add*", "edit*", "remove*", "insert*", "save*", "update*", "modify*", "delete*", "do*", "process*", "on*", "create*"
    };
}
