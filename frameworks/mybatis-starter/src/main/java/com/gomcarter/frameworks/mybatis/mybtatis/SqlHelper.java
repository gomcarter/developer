package com.gomcarter.frameworks.mybatis.mybtatis;

import com.google.common.collect.Maps;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mybatis - 获取Mybatis查询sql工具
 *
 * @author liuzh/abel533/isea533
 */
public class SqlHelper implements ApplicationContextAware {

    private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();

    private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();

    private static final ReflectorFactory DEFAULT_REFLECTOR_FACTORY = new DefaultReflectorFactory();

    /**
     * 反射对象，增加对低版本Mybatis的支持
     *
     * @param object 反射对象
     * @return MetaObject
     */
    public static MetaObject forObject(Object object) {
        return MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);
    }

    /**
     * 通过接口获取sql
     *
     * @param mapper     mapper
     * @param methodName methodName
     * @param args       args
     * @return String
     */
    public static String getMapperSql(Object mapper, String methodName,
                                      Object... args) {
        MetaObject metaObject = forObject(mapper);
        SqlSessionFactory session = (SqlSessionFactory) metaObject.getValue("h.sqlSession");
        Class<?> mapperInterface = (Class<?>) metaObject.getValue("h.mapperInterface");
        String fullMethodName = mapperInterface.getCanonicalName() + "." +
                methodName;
        if (args == null || args.length == 0) {
            return getNamespaceSql(session, fullMethodName, null);
        } else {
            return getMapperSql(session, mapperInterface, methodName, args);
        }
    }


    /**
     * 通过Mapper方法名获取sql
     *
     * @param session              session
     * @param fullMapperMethodName fullMapperMethodName
     * @param args                 args
     * @return sql String
     */
    public static String getMapperSql(SqlSessionFactory session, String fullMapperMethodName, Object... args) {
        if (args == null || args.length == 0) {
            return getNamespaceSql(session, fullMapperMethodName, null);
        }
        String methodName = fullMapperMethodName.substring(fullMapperMethodName.lastIndexOf('.') + 1);
        Class<?> mapperInterface = null;
        try {
            mapperInterface = Class.forName(fullMapperMethodName.substring(0, fullMapperMethodName.lastIndexOf('.')));
            return getMapperSql(session, mapperInterface, methodName, args);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("参数" + fullMapperMethodName + "无效！");
        }
    }

    /**
     * @param sfb                  sessionFactoryBean ID
     * @param fullMapperMethodName fullMapperMethodName
     * @param args                 args
     * @return sql
     */
    public static String getMapperSql(String sfb, String fullMapperMethodName, Object... args) {

        SqlSessionFactory dssf = (SqlSessionFactory) applicationContext.getBean(sfb);

        return getMapperSql(dssf, fullMapperMethodName, args);
    }

    /**
     * 通过Mapper接口和方法名
     *
     * @param session         session
     * @param mapperInterface mapperInterface
     * @param methodName      methodName
     * @param args            args
     * @return sql
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static String getMapperSql(SqlSessionFactory session, Class<?> mapperInterface, String methodName, Object... args) {
        String fullMapperMethodName = mapperInterface.getCanonicalName() + "." + methodName;
        if (args == null || args.length == 0) {
            return getNamespaceSql(session, fullMapperMethodName, null);
        }
        Method method = getDeclaredMethods(mapperInterface, methodName);
        Map<String, Object> params = Maps.newHashMap();
        final Class<?>[] argTypes = method.getParameterTypes();
        for (int i = 0; i < argTypes.length; i++) {
            if (!RowBounds.class.isAssignableFrom(argTypes[i]) && !ResultHandler.class.isAssignableFrom(argTypes[i])) {
                String paramName = "param" + String.valueOf(params.size() + 1);
                paramName = getParamNameFromAnnotation(method, i, paramName);
                params.put(paramName, i >= args.length ? null : args[i]);
            }
        }
        if (args != null && args.length == 1) {
            Object _params = wrapCollection(args[0]);
            if (_params instanceof Map) {
                params.putAll((Map) _params);
            }
        }
        return getNamespaceSql(session, fullMapperMethodName, params);
    }

    /**
     * 通过Mapper接口和方法名
     *
     * @param sfb             sfb
     * @param mapperInterface mapperInterface
     * @param methodName      methodName
     * @param args            args
     * @return sql
     */
    public static String getMapperSql(String sfb, Class<?> mapperInterface, String methodName, Object... args) {

        SqlSessionFactory dssf = (SqlSessionFactory) applicationContext.getBean(sfb);

        return getMapperSql(dssf, mapperInterface, methodName, args);
    }

    /**
     * @param mapperInterface mapperInterface
     * @param methodName      methodName
     * @param args            args
     * @return sql
     */
    public static String getMapperSql(Class<?> mapperInterface, String methodName, Object... args) {
        return getMapperSql("sqlSessionFactory", mapperInterface, methodName, args);
    }

    /**
     * 通过命名空间方式获取sql
     *
     * @param session   session
     * @param namespace namespace
     * @return sql
     */
    public static String getNamespaceSql(SqlSessionFactory session, String namespace) {
        return getNamespaceSql(session, namespace, null);
    }

    /**
     * 通过命名空间方式获取sql
     *
     * @param session   session
     * @param namespace namespace
     * @param params    params
     * @return sql
     */
    public static String getNamespaceSql(SqlSessionFactory session, String namespace, Object params) {
        params = wrapCollection(params);
        Configuration configuration = session.getConfiguration();
        MappedStatement mappedStatement = configuration.getMappedStatement(namespace);
        TypeHandlerRegistry typeHandlerRegistry = mappedStatement.getConfiguration().getTypeHandlerRegistry();
        BoundSql boundSql = mappedStatement.getBoundSql(params);
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        String sql = boundSql.getSql();
        if (parameterMappings != null) {
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    Object value;
                    String propertyName = parameterMapping.getProperty();
                    if (boundSql.hasAdditionalParameter(propertyName)) {
                        value = boundSql.getAdditionalParameter(propertyName);
                    } else if (params == null) {
                        value = null;
                    } else if (typeHandlerRegistry.hasTypeHandler(params.getClass())) {
                        value = params;
                    } else {
                        MetaObject metaObject = configuration.newMetaObject(params);
                        value = metaObject.getValue(propertyName);
                    }
                    JdbcType jdbcType = parameterMapping.getJdbcType();
                    if (value == null && jdbcType == null) {
                        jdbcType = configuration.getJdbcTypeForNull();
                    }
                    sql = replaceParameter(sql, value, jdbcType, parameterMapping.getJavaType());
                }
            }
        }
        return sql;
    }

    /**
     * 根据类型替换参数 仅作为数字和字符串两种类型进行处理，需要特殊处理的可以继续完善这里
     *
     * @param sql      sql
     * @param value    value
     * @param jdbcType jdbcType
     * @param javaType javaType
     * @return sql
     */
    private static String replaceParameter(String sql, Object value, JdbcType jdbcType, Class<?> javaType) {
        String strValue = String.valueOf(value);
        if (jdbcType != null) {
            switch (jdbcType) {
                //数字
                case BIT:
                case TINYINT:
                case SMALLINT:
                case INTEGER:
                case BIGINT:
                case FLOAT:
                case REAL:
                case DOUBLE:
                case NUMERIC:
                case DECIMAL:
                    break;
                //日期
                case DATE:
                case TIME:
                case TIMESTAMP:
                    //其他，包含字符串和其他特殊类型
                default:
                    strValue = "'" + strValue + "'";

            }
        } else if (Number.class.isAssignableFrom(javaType)) {
            //不加单引号
        } else {
            strValue = "'" + strValue + "'";
        }
        return sql.replaceFirst("\\?", strValue);
    }

    /**
     * 获取指定的方法
     *
     * @param clazz      clazz
     * @param methodName methodName
     * @return sql
     */
    private static Method getDeclaredMethods(Class<?> clazz, String methodName) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        throw new IllegalArgumentException("方法" + methodName + "不存在！");
    }

    /**
     * 获取参数注解名
     *
     * @param method    method
     * @param i         v
     * @param paramName paramName
     * @return sql
     */
    private static String getParamNameFromAnnotation(Method method, int i, String paramName) {
        final Annotation[] paramAnnos = method.getParameterAnnotations()[i];
        for (Annotation paramAnno : paramAnnos) {
            if (paramAnno instanceof Param) {
                paramName = ((Param) paramAnno).value();
            }
        }
        return paramName;
    }

    /**
     * 简单包装参数
     *
     * @param object object
     * @return object
     */
    private static Object wrapCollection(final Object object) {
        if (object instanceof List) {
            Map<String, Object> map = new HashMap<>();
            map.put("list", object);
            return map;
        } else if (object != null && object.getClass().isArray()) {
            Map<String, Object> map = new HashMap<>();
            map.put("array", object);
            return map;
        }
        return object;
    }


    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        applicationContext = ac;
    }
}
