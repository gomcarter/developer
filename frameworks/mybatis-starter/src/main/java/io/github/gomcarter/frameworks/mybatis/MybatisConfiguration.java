package io.github.gomcarter.frameworks.mybatis;

import io.github.gomcarter.frameworks.mybatis.datasource.ReadWriteDataSource;
import io.github.gomcarter.frameworks.mybatis.datasource.ReadWriteDataSourceProcessor;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.aop.aspectj.*;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.Collections;

/**
 * @Description: mybatis配置
 * @Author: gomcarter
 */
@Aspect
@Component
public class MybatisConfiguration {

    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(ReadWriteDataSource source) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(source);
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MybatisConfigHolder.DAO_XML_PATH));
        return factoryBean;
    }

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setBasePackage(StringUtils.join(MybatisConfigHolder.DAO_BASE_PACKAGE, ","));
        configurer.setSqlSessionFactoryBeanName("sqlSessionFactoryBean");
        return configurer;
    }

    @Bean
    public NameMatchTransactionAttributeSource nameMatchTransactionAttributeSource() {
        /*只读事务，不做更新操作*/
        RuleBasedTransactionAttribute readOnlyTx = new RuleBasedTransactionAttribute();
        readOnlyTx.setReadOnly(true);
        readOnlyTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        /*当前存在事务就使用当前事务，当前不存在事务就创建一个新的事务*/
        RuleBasedTransactionAttribute requiredTx = new RuleBasedTransactionAttribute();
        requiredTx.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
        requiredTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        NameMatchTransactionAttributeSource attributeSource = new NameMatchTransactionAttributeSource();
        for (String pattern : MybatisConfigHolder.TRANSACTION_REQUIRED_NAME_MAP) {
            attributeSource.addTransactionalMethod(pattern, requiredTx);
        }
        attributeSource.addTransactionalMethod("*", readOnlyTx);

        return attributeSource;
    }

    @Bean
    public ReadWriteDataSourceProcessor readWriteDataSourceProcessor(NameMatchTransactionAttributeSource attributeSource) {
        ReadWriteDataSourceProcessor processor = new ReadWriteDataSourceProcessor();
        processor.setForceChoiceReadWhenWrite(false);
        processor.postProcessAfterInitialization(attributeSource, "nameMatchTransactionAttributeSource");
        return processor;
    }

    /**
     * 事务拦截器
     */
    @Bean
    public AspectJExpressionPointcutAdvisor transactionAdvisor(ReadWriteDataSource dataSource, NameMatchTransactionAttributeSource attributeSource) {
        /* 事务管理器 */
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);

        /* 事务切面 */
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setAdvice(new TransactionInterceptor(transactionManager, attributeSource));
        advisor.setExpression(MybatisConfigHolder.TRANSACTION_POINTCUT_EXPRESSION);

        return advisor;
    }

    /**
     * 主库、从库选择AOP
     */
    @Bean
    public AspectJExpressionPointcutAdvisor determineReadOrWriteDBAdvisor(ReadWriteDataSourceProcessor readWriteDataSourceProcessor) throws NoSuchMethodException {
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setExpression(MybatisConfigHolder.TRANSACTION_POINTCUT_EXPRESSION);
        advisor.setOrder(Integer.MIN_VALUE);

        AspectInstanceFactory aspectInstanceFactory = new SingletonAspectInstanceFactory(readWriteDataSourceProcessor);
        AspectJAroundAdvice aroundAdvice = new AspectJAroundAdvice(ReadWriteDataSourceProcessor.class.getMethod("determineReadOrWriteDB", ProceedingJoinPoint.class),
                (AspectJExpressionPointcut) advisor.getPointcut(),
                aspectInstanceFactory);

        advisor.setAdvice(aroundAdvice);
        return advisor;
    }
}
