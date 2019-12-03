package com.gomcarter.frameworks.mybatis.annotation;

import com.gomcarter.frameworks.mybatis.NacosMybatisRegistrar;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author gomcarter on 2019-11-09 22:53:32
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(NacosMybatisRegistrar.class)
public @interface EnableNacosMybatis {
    /**
     * nacos data_id
     *
     * @return data_id
     */
    @AliasFor("value")
    String dataId() default "";

    /**
     * data_id
     *
     * @return data_id
     */
    @AliasFor("dataId")
    String value() default "";

    /**
     * nacos group
     *
     * @return group
     */
    String group() default "MYSQL";

    /**
     * 如： classpath:mybatis/ ** / *Mapper.xml
     *
     * @return daoXmlPath
     */
    String daoXmlPath() default "classpath:mybatis/**/*Mapper.xml";

    /**
     * 如： com.company.*.dao
     *
     * @return daoBasePackage
     */
    String[] daoBasePackage() default {"com.**.dao"};

    /**
     * 所有service包下的类的所有方法
     *
     * @return transactionPointcut
     */
    String transactionPointcut() default "execution(* com..*.service..*.*(..))";

    /**
     * 所有service中方法名以此开头的开启写事务，并走主库
     * <p>
     * 否则readonly，并周从库
     *
     * @return transactionRequiredNameMap
     */
    String[] transactionRequiredNameMap() default {
            "add*", "edit*", "remove*", "insert*", "save*", "update*", "modify*", "delete*", "do*", "process*", "on*", "create*"
    };
}
