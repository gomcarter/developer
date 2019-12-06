package com.gomcarter.frameworks.mybatis.utils;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gomcarter.frameworks.base.common.CustomStringUtils;
import com.gomcarter.frameworks.base.common.ReflectionUtils;
import com.gomcarter.frameworks.mybatis.annotation.Condition;
import com.gomcarter.frameworks.mybatis.annotation.MatchType;
import com.gomcarter.frameworks.mybatis.pager.Pageable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

/**
 * @author gomcarter
 */
public class MapperUtils {

    /**
     * 构建分页器
     *
     * @param pageable 外部传来的参数
     * @param <T>      要查询的对象
     * @return page
     */
    public static <T> Page<T> buildPage(Pageable pageable) {
        String split = ",";
        // 构造分页器
        Page<T> page = new Page<>(pageable.getStartNum() / pageable.getPageCount() + 1, pageable.getPageCount(), false);
        String orderColumn = pageable.getOrderColumn();
        if (orderColumn != null && (orderColumn = orderColumn.trim()).length() > 0) {
            String[] sortby = orderColumn.split(split);
            String[] orderTypes = (pageable.getOrderType() == null ? "" : pageable.getOrderType())
                    .trim().split(split);

            List<OrderItem> orderItemList = new ArrayList<>();
            for (int i = 0; i < sortby.length; ++i) {
                String sort = sortby[i];
                boolean asc = orderTypes.length >= i + 1 && "asc".equalsIgnoreCase(orderTypes[i]);

                OrderItem orderItem = new OrderItem();
                orderItem.setColumn(sort);
                orderItem.setAsc(asc);

                orderItemList.add(orderItem);
            }

            page.setOrders(orderItemList);
        }

        return page;
    }

    /**
     * 根据params 构建一个 queryWrapper，具体规则见：{@link com.gomcarter.frameworks.mybatis.annotation.Condition}
     *
     * @param params 参数
     * @param <T>    查询实体类型
     * @param <R>    参数类型
     * @return wrapper
     */
    public static <T, R> Wrapper<T> buildQueryWrapper(R params) {
        return null == params ? null : buildQueryWrapper(new QueryWrapper<>(), params);
    }

    private static WeakHashMap<Class, List<Field>> FIELD_CACHE_MAP = new WeakHashMap<>();

    public static <T, R> QueryWrapper<T> buildQueryWrapper(QueryWrapper<T> wrapper, R params) {
        // cache
        List<Field> fields = FIELD_CACHE_MAP.get(params.getClass());
        if (fields == null) {
            fields = ReflectionUtils.findAllField(params.getClass());
            FIELD_CACHE_MAP.put(params.getClass(), fields);
        }

        // fields never be null
        for (Field field : fields) {
            Object value = ReflectionUtils.getFieldValue(params, field);
            // 如果为 null，则跳过此字段
            if (value == null) {
                continue;
            }

            // 根据字段类型获取默认的匹配类型
            MatchType type = MatchType.getDefaultType(field.getType());
            String fieldName = null;

            if (field.isAnnotationPresent(Condition.class)) {
                Condition condition = field.getAnnotation(Condition.class);
                // 如果字段标记了@Condition，那么取出匹配类型，如果类型是 EQ 那么还是使用getDefaultType的值
                if (condition.type() != MatchType.EQ) {
                    type = condition.type();
                }
                fieldName = condition.field();
            }
            if (fieldName == null || fieldName.length() == 0) {
                fieldName = CustomStringUtils.camelToUnderline(field.getName());
            }

            // 开始包装
            type.wrap(wrapper, fieldName, value);
        }

        return wrapper;
    }
}
