package com.gomcarter.frameworks.interfaces.utils;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gomcarter.frameworks.interfaces.dto.ApiInterface;
import com.gomcarter.frameworks.interfaces.annotation.Notes;
import com.gomcarter.frameworks.interfaces.dto.ApiBean;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;
import sun.reflect.generics.reflectiveObjects.TypeVariableImpl;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author gomcarter on 2019-12-02 09:23:09
 */
public class InterfacesRegister implements ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(InterfacesRegister.class);

    /**
     * the cache for fields default value
     */
    private Map<Class, Object> instanceMap = new HashMap<>();
    /**
     * the class counter for recursion depth
     */
    private Map<String, Integer> keyClassMap = new HashMap<>();

    public final static int RECURSION_DEPTH = 0;

    /**
     * <p></p>
     *
     * <b style="color:green">comment：</b>
     * <p>
     * 1、{@link org.springframework.web.bind.annotation.RequestMapping}，such as：
     * <blockquote>
     * {@code @GetMapping(value = "list",name = "name of this interface")}
     * </blockquote>
     * <blockquote>
     * {@code @RequestMapping(value = "list",name = "name of this interface")}
     * </blockquote>
     * <blockquote style="color:red">
     * {@code mark： if the name of RequestMapping is blank，then skip it —— which means the interface has no name}
     * </blockquote>
     * <p>
     * 2、some comment for this interface{@link Notes}，如：
     * </p>
     * <blockquote><pre>
     * {@code @Notes("this interface for user login")} <br>
     * {@code public void login() {...} }
     * </pre></blockquote>
     * 或
     * <blockquote><pre>
     *     public class Params {
     *         {@code @Notes("property comment")}
     *          private long id;
     *     }
     * </pre></blockquote>
     * 或
     * <blockquote><pre>
     *     {@code public void login(@Notes("parameter comment") String cellphone) {...} }
     * </pre></blockquote>
     * <p>
     * 3、mar this parameter whether required or not，mark on it as {@link org.springframework.web.bind.annotation.RequestParam}, such as：
     * <blockquote>
     * {@code public void login(@RequestParam String cellphone) {...} }
     * </blockquote>
     * mark on the value{@link Notes}，such as：
     * <blockquote><pre>
     *     public class Params {
     *         {@code @Notes(value="the id", notNull=true)}
     *          private long id;
     *     }
     * </pre></blockquote>
     * <blockquote>
     * {@code public class Params }
     * </blockquote>
     *
     * <blockquote>
     * {@code }
     * </blockquote>
     * <p>
     *
     * </p>
     *
     * @return list of ApiInterface
     */
    @Notes("interface of register the interfaces to the interfaces center")
    public static List<ApiInterface> register() throws Exception {
        return new InterfacesRegister().register0();
    }

    private List<ApiInterface> register0() throws Exception {
        RequestMappingHandlerMapping bean = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = bean.getHandlerMethods();

        List<ApiInterface> interfacesList = new ArrayList<>(handlerMethods.size());
        for (Map.Entry<RequestMappingInfo, HandlerMethod> kvEntry : handlerMethods.entrySet()) {
            try {
                RequestMappingInfo rmi = kvEntry.getKey();
                HandlerMethod handlerMethod = kvEntry.getValue();
                Method m = handlerMethod.getMethod();

                String interfaceName = rmi.getName();
                // if the name of RequestMapping is blank，then skip it —— which means the interface has no name.
                if (interfaceName == null || interfaceName.trim().length() == 0) {
                    continue;
                }

                boolean deprecated = handlerMethod.getBeanType().getAnnotation(Deprecated.class) != null;

                String url = StringUtils.join(rmi.getPatternsCondition().getPatterns(), ",");
                ApiInterface interfaces = new ApiInterface()
                        .setMethod(StringUtils.join(rmi.getMethodsCondition().getMethods(), ","))
                        .setUrl(url)
                        .setController(m.getDeclaringClass().getSimpleName())
                        .setDeprecated(deprecated || m.getAnnotation(Deprecated.class) != null)
                        .setName(interfaceName)
                        .setMark(Optional.ofNullable(m.getAnnotation(Notes.class)).map(Notes::value).orElse(null));
                interfacesList.add(interfaces);

                // get the returns
                ApiBean returns = new ApiBean();
                keyClassMap.clear();
                Type genericReturnType = handlerMethod.getBeanType().getDeclaredMethod(m.getName(), m.getParameterTypes()).getGenericReturnType();
                this.generateChildrenBean(url, returns, null, genericReturnType);
                interfaces.setReturns(returns);

                // get the parameters
                List<ApiBean> parameters = new ArrayList<>();
                MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
                String[] parameterNames = Optional.ofNullable(new DefaultParameterNameDiscoverer().getParameterNames(m)).orElse(new String[methodParameters.length]);

                for (int i = 0; i < methodParameters.length; ++i) {

                    MethodParameter mp = methodParameters[i];
                    Notes notes = mp.getParameterAnnotation(Notes.class);
                    RequestParam requestParam = mp.getParameterAnnotation(RequestParam.class);
                    RequestBody requestBody = mp.getParameterAnnotation(RequestBody.class);
                    // the logic of parameter is required or not
                    boolean notNull = mp.getParameterAnnotation(PathVariable.class) != null
                            || Optional.ofNullable(requestParam).map(RequestParam::required).orElse(false)
                            || Optional.ofNullable(requestBody).map(RequestBody::required).orElse(false)
                            || Optional.ofNullable(notes).map(Notes::notNull).orElse(false);

                    String parameterName = Optional.ofNullable(requestParam).map(RequestParam::value).orElse(null);
                    parameterName = StringUtils.isBlank(parameterName) ? parameterNames[i] : parameterName;

                    ApiBean parameter = new ApiBean()
                            .setComment(Optional.ofNullable(notes).map(Notes::value).orElse(null))
                            .setNotNull(notNull)
                            .setKey(parameterName)
                            .setBody(requestBody != null)
                            .setDefaults(Optional.ofNullable(requestParam)
                                    .map(RequestParam::defaultValue)
                                    .filter(s -> !"\n\t\t\n\t\t\n\ue000\ue001\ue002\n\t\t\t\t\n".equals(s))
                                    .orElse(null)
                            );


                    generateChildrenBean(url, parameter, parameterName, mp.getGenericParameterType());

                    parameters.add(parameter);

                    keyClassMap.clear();
                }

                interfaces.setParameters(parameters);
            } catch (Exception ignore) {
                // if failed， skip it
            } finally {

            }
        }

        // finally clear the cache
        instanceMap.clear();
        keyClassMap.clear();

        return interfacesList;
    }

    private void generateChildrenBean(String url, ApiBean parent, String key, Type parentType) {
        try {
            if (terminal(key, parentType)) {
                return;
            }

            if (parentType instanceof ParameterizedType) {
                if (DataType.get(((ParameterizedTypeImpl) parentType).getRawType()) != DataType.collection) {
                    throw new RuntimeException("【" + url + "】simple POJO or Iterable only for parameters and returns");
                }
                ApiBean child = new ApiBean();
                parent.setType(List.class.getSimpleName())
                        .addChild(child);

                Type[] actualTypeArguments = ((ParameterizedType) parentType).getActualTypeArguments();
                Type childType = actualTypeArguments[0];

                generateChildrenBean(url, child, key, childType);
            } else if (parentType instanceof TypeVariableImpl) {
                parent.setType(Object.class.getSimpleName());
            } else {
                Class parentKls = (Class) parentType;

                if (parentKls.isArray()) {
                    throw new RuntimeException("【" + url + "】simple POJO or Iterable only for parameters and returns, you have to make it as: List，Set or Collection");
                }

                DataType dataType = DataType.get(parentKls);
                if (dataType == DataType.file) {
                    parent.setType(File.class.getSimpleName());
                } else if (dataType == DataType.object) {
                    Object instance = this.newInstance(parentKls);

                    List<ApiBean> children = Arrays.stream(this.getFields(parentKls))
                            .filter(field -> !field.getName().contains("this$"))
                            .map(field -> {
                                if (field.getAnnotation(JsonIgnore.class) != null) {
                                    return null;
                                }

                                Notes notes = field.getAnnotation(Notes.class);
                                ApiBean child = new ApiBean()
                                        .setKey(field.getName())
                                        .setNotNull(Optional.ofNullable(notes).map(Notes::notNull).orElse(false))
                                        .setComment(Optional.ofNullable(notes).map(Notes::value).orElse(null))
                                        .setDefaults(getFieldValue(instance, field));

                                generateChildrenBean(url, child, StringUtils.defaultString(key, field.getName()), field.getGenericType());
                                return child;
                            })
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());

                    parent.setChildren(children)
                            .setType(Object.class.getSimpleName());
                } else if (dataType == DataType.simple) {
                    parent.setType(parentKls.getSimpleName());
                }
            }
        } catch (Exception e) {
            logger.error("generate failed, url: {}, key: {}}, parentType: {}", url, key, parentType.getTypeName(), e);
            throw new RuntimeException("oops, the【" + url + "】is failed: " + e.getMessage());
        }
    }

    private boolean terminal(String key, Type type) {
        if (type instanceof ParameterizedType
                || type instanceof TypeVariableImpl
                || DataType.get((Class) type) != DataType.object) {
            return false;
        }

        String k = key + "_" + type.getTypeName();
        Integer times = keyClassMap.getOrDefault(k, 0);
        if (times > RECURSION_DEPTH) {
            return true;
        }
        keyClassMap.put(k, ++times);
        return false;
    }

    private Field[] getFields(Class kls) {
        Field[] fields = kls.getDeclaredFields();
        Class superKls = kls;
        while ((superKls = superKls.getSuperclass()) != null) {
            Field[] superFields = superKls.getDeclaredFields();
            int newLength = superFields.length;
            if (newLength > 0) {
                Field[] original = fields;
                fields = new Field[fields.length + newLength];

                System.arraycopy(superFields, 0, fields, 0, newLength);
                System.arraycopy(original, 0, fields, newLength, original.length);
            }
        }
        return fields;
    }

    private Object newInstance(Class kls) {
        try {
            Object instance = instanceMap.get(kls);
            if (instance == null) {
                instanceMap.put(kls, kls.newInstance());
            }
            return instanceMap.get(kls);
        } catch (Exception e) {
            return null;
        }
    }

    private static Object getFieldValue(Object instance, Field field) {
        if (instance != null && DataType.get(field.getType()) == DataType.simple) {
            try {
                field.setAccessible(true);
                return field.get(instance);
            } catch (Exception e) {
            }
        }
        return null;
    }

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        applicationContext = ac;
    }
}
