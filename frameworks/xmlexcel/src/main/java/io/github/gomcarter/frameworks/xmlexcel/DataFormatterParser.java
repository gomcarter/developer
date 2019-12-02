package io.github.gomcarter.frameworks.xmlexcel;

import io.github.gomcarter.frameworks.xmlexcel.config.Header;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


/**
 * @author gomcarter on 2018年5月7日 14:49:47
 */
public class DataFormatterParser {

    private static class DataGetter {
        private Method getter;
        private DataGetter subGetter;

        public Object getResult(Object target) {
            if (target == null || getter == null) {
                return null;
            }

            Object r = null;
            try {
                r = getter.invoke(target);
            } catch (Exception e) {
            }

            if (this.subGetter != null) {
                return this.subGetter.getResult(r);
            }
            return r;
        }

        public static DataGetter parse(String key, Class kls) {
            if (key == null || key.trim().length() == 0) {
                return null;
            }

            Class cls = kls;
            try {
                DataGetter parent = null;
                DataGetter cache = null;
                for (String column : key.split("\\.")) {
                    Method method = cls.getDeclaredMethod("get" + column.substring(0, 1).toUpperCase() + column.substring(1, column.length()));
                    method.setAccessible(true);
                    cls = method.getReturnType();

                    if (parent == null) {
                        parent = new DataGetter();
                        parent.getter = method;

                        cache = parent;
                    } else {
                        DataGetter sub = new DataGetter();
                        sub.getter = method;
                        cache.setSubGetter(sub);

                        cache = sub;
                    }
                }
                return parent;
            } catch (Exception e) {
                return null;
            }
        }


        public static void main(String[] args) {
            String column = "a";
            System.out.println(column.substring(0, 1).toUpperCase() + column.substring(1, column.length()));
        }

        public Method getGetter() {
            return getter;
        }

        public DataGetter setGetter(Method getter) {
            this.getter = getter;
            return this;
        }

        public DataGetter getSubGetter() {
            return subGetter;
        }

        public DataGetter setSubGetter(DataGetter subGetter) {
            this.subGetter = subGetter;
            return this;
        }
    }

    public static void parse(List<Header> headers, Object obj) {
        parse(headers, obj.getClass());
    }

    public static void parse(List<Header> headers, Class kls) {
        if (headers != null) {
            headers.forEach(h -> {
                //已经有formatter了就不parse了
                if (h.getDataFormatter() != null) {
                    return;
                }

                Function formatter = null;
                String key = h.getKey();
                boolean isMap = Arrays.stream(kls.getGenericInterfaces())
                        .anyMatch(s -> ((ParameterizedTypeImpl) s).getRawType() == Map.class);

                if (isMap) {
                    if (key == null || key.trim().length() == 0) {
                        formatter = s -> ((Map) s).get(h.getName());
                    } else {
                        formatter = s -> ((Map) s).get(key);
                    }
                } else {
                    DataGetter getter = DataGetter.parse(key, kls);

                    if (getter != null) {
                        formatter = getter::getResult;
                    }
                }

                h.setDataFormatter(formatter);
            });
        }
    }

}
