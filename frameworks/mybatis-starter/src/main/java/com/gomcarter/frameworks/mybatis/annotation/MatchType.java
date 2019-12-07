package com.gomcarter.frameworks.mybatis.annotation;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gomcarter.frameworks.mybatis.utils.MapperUtils;
import org.springframework.beans.BeanUtils;

import java.util.Collection;

/**
 * 前提标记的字段本身不能 null，为 null 则跳过这个条件
 */
public enum MatchType {
    /**
     * where name = name
     */
    EQ {
        @Override
        public <T> void wrap(QueryWrapper<T> wrapper, String fieldName, Object value) {
            wrapper.eq(fieldName, value);
        }
    },
    /**
     * where name &lt;&gt; name
     */
    NE {
        @Override
        public <T> void wrap(QueryWrapper<T> wrapper, String fieldName, Object value) {
            wrapper.ne(fieldName, value);
        }
    },
    /**
     * where name LIKE "%name%"
     */
    LIKE {
        @Override
        public <T> void wrap(QueryWrapper<T> wrapper, String fieldName, Object value) {
            wrapper.like(fieldName, value);
        }
    },
    /**
     * where name NOT LIKE "%name%"
     */
    NOTLIKE {
        @Override
        public <T> void wrap(QueryWrapper<T> wrapper, String fieldName, Object value) {
            wrapper.notLike(fieldName, value);
        }
    },
    /**
     * where name LIKE "name%"
     */
    RIGHTLIKE {
        @Override
        public <T> void wrap(QueryWrapper<T> wrapper, String fieldName, Object value) {
            wrapper.likeRight(fieldName, value);
        }
    },
    /**
     * where name LIKE "%name"
     */
    LEFTLIKE {
        @Override
        public <T> void wrap(QueryWrapper<T> wrapper, String fieldName, Object value) {
            wrapper.likeLeft(fieldName, value);
        }
    },
    /**
     * where name &gt; name
     */
    GT {
        @Override
        public <T> void wrap(QueryWrapper<T> wrapper, String fieldName, Object value) {
            wrapper.gt(fieldName, value);
        }
    },
    /**
     * where name &gt;= name
     */
    GE {
        @Override
        public <T> void wrap(QueryWrapper<T> wrapper, String fieldName, Object value) {
            wrapper.ge(fieldName, value);
        }
    },
    /**
     * where name &lt; name
     */
    LT {
        @Override
        public <T> void wrap(QueryWrapper<T> wrapper, String fieldName, Object value) {
            wrapper.lt(fieldName, value);
        }
    },
    /**
     * where name &lt;= name
     */
    LE {
        @Override
        public <T> void wrap(QueryWrapper<T> wrapper, String fieldName, Object value) {
            wrapper.le(fieldName, value);
        }
    },
    /**
     * where name is null
     */
    NULL {
        @Override
        public <T> void wrap(QueryWrapper<T> wrapper, String fieldName, Object value) {
            wrapper.isNull(fieldName);
        }
    },
    /**
     * where name is not null
     */
    NOTNULL {
        @Override
        public <T> void wrap(QueryWrapper<T> wrapper, String fieldName, Object value) {
            wrapper.isNotNull(fieldName);
        }
    },
    /**
     * where name in (...) : name 字段必须是 iterable 或者 array，且模板类必须是基础类型
     * <p>
     * 特别的：而且如果字段本身是字段命名为  nameList，nameSet 那么不需要打此标签，默认就是 in
     */
    IN {
        @Override
        public <T> void wrap(QueryWrapper<T> wrapper, String fieldName, Object value) {
            Class kls = value.getClass();
            if (BeanUtils.isSimpleValueType(kls) || kls == Object.class) {
                // 判断 kls 是否是基本类型，如果是则直接 in 了
                wrapper.in(fieldName, value);
            } else if (Iterable.class.isAssignableFrom(kls)) {
                wrapper.in(fieldName, (Collection<?>) value);
            } else {
                wrapper.in(fieldName, value);
            }
        }
    },
    /**
     * where name not in (...) :nameNOTIN 必须是 iterable 或者 array，且模板类必须是基础类型；
     */
    NOTIN {
        @Override
        public <T> void wrap(QueryWrapper<T> wrapper, String fieldName, Object value) {
            wrapper.notIn(fieldName, value);
        }
    },
    /**
     * where name in (...): nameINSQL是一个 sql 语句： select id from table where id &lt; 3
     * <p>
     * 尽量不要使用这个，有 sql注入 的风险
     */
    INSQL {
        @Override
        public <T> void wrap(QueryWrapper<T> wrapper, String fieldName, Object value) {
            wrapper.inSql(fieldName, value + "");
        }
    },
    /**
     * where name not in (...): nameINSQL是一个 sql 语句： select name from table where id &lt; 3
     * <p>
     * 尽量不要使用这个，有 sql注入 的风险
     */
    NOTINSQL {
        @Override
        public <T> void wrap(QueryWrapper<T> wrapper, String fieldName, Object value) {
            wrapper.notInSql(fieldName, value + "");
        }
    },
    /**
     * 自定义可以不标注，默认就是 and
     * 当此字段是一个自定义类： where (XX = XX AND YY = YY OR ZZ = ZZ) and other ———— XX,YY,ZZ 是此字段类里面的字段
     */
    AND {
        @Override
        public <T> void wrap(QueryWrapper<T> wrapper, String fieldName, Object value) {
            Class kls = value.getClass();

            if (BeanUtils.isSimpleValueType(kls) || kls == Object.class) {
                // 判断 kls 是否是基本类型，如果是则直接 or 了
                wrapper.and(w -> w.eq(fieldName, value));
            } else if (kls.isArray() || Iterable.class.isAssignableFrom(kls)) {
                // 如果是数组或者是Iterable
                wrapper.and(w -> IN.wrap(w, fieldName, value));
            } else {
                // 否则去 or 这个类对应下面的所有字段的匹配结果
                wrapper.and(w -> MapperUtils.buildQueryWrapper(w, value));
            }
        }
    },
    /**
     * 当此字段是基础类型： where name = name or other
     * 当此字段是一个自定义类：where (XX = XX AND YY == YY OR ZZ = ZZ) or other ———— XX,YY,ZZ 是此字段类里面的字段
     */
    OR {
        @Override
        public <T> void wrap(QueryWrapper<T> wrapper, String fieldName, Object value) {
            Class kls = value.getClass();

            if (BeanUtils.isSimpleValueType(kls) || kls == Object.class) {
                // 判断 kls 是否是基本类型，如果是则直接 or 了
                wrapper.or(w -> w.eq(fieldName, value));
            } else if (kls.isArray() || Iterable.class.isAssignableFrom(kls)) {
                // 如果是数组或者是Iterable
                wrapper.or(w -> IN.wrap(w, fieldName, value));
            } else {
                // 否则去 or 这个类对应下面的所有字段的匹配结果
                wrapper.or(w -> MapperUtils.buildQueryWrapper(w, value));
            }
        }
    };

    public abstract <T> void wrap(QueryWrapper<T> wrapper, String fieldName, Object value);

    public static MatchType getDefaultType(Class kls) {
        // 基本类型，默认使用 EQ
        if (BeanUtils.isSimpleValueType(kls) || kls == Object.class) {
            return EQ;
        } else if (kls.isArray() || Iterable.class.isAssignableFrom(kls)) {
            // 数组或者Iterable默认使用 in
            return IN;
        }
        // 复杂类默认使用 and
        return AND;
    }
}
