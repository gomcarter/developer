package com.gomcarter.frameworks.base.common;

import com.google.common.collect.Sets;

import java.util.*;

/**
 * @author gomcarter 2017年12月2日 08:10:35
 */
public class CollectionUtils {

    /**
     * 判断集合是否不为空
     *
     * @param collection collection for validate
     * @return true for collection is not empty
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * 判断集合是否为空
     *
     * @param collection collection for validate
     * @return true for collection is empty
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 根据sortList顺序排序list
     *
     * @param list         list to be sorted
     * @param propertyName sort by
     * @param sortList     sample
     * @param <T>          some class
     * @param <K>          some class
     */
    public static <T, K> void sort(List<T> list, final String propertyName, final List<K> sortList) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        try {
            list.sort((o1, o2) -> {
                try {
                    int index1;
                    index1 = sortList.indexOf(ReflectionUtils.invokeGetterMethod(o1, propertyName));
                    if (index1 < 0) {
                        return 1;
                    }
                    int index2 = sortList.indexOf(ReflectionUtils.invokeGetterMethod(o2, propertyName));
                    if (index2 < 0) {
                        return -1;
                    }
                    if (index1 == index2) {
                        return 0;
                    }
                    return index1 - index2;
                } catch (IllegalArgumentException | SecurityException e) {
                    throw new RuntimeException("排序失败");
                }
            });
        } catch (SecurityException e) {
            throw new RuntimeException("排序失败");
        }
    }

    /**
     * 两个集合取交集，并去重
     *
     * @param a   one  of the collection
     * @param b   anothor collection
     * @param <T> some class
     * @return result
     */
    public static <T> Set<T> retainAll(Collection<T> a, Collection<T> b) {
        Set<T> set;
        Collection<T> c;
        if (a.size() <= b.size()) {
            set = Sets.newHashSet(b);
            c = a;
        } else {
            set = Sets.newHashSet(a);
            c = b;
        }

        Set<T> result = Sets.newHashSetWithExpectedSize(c.size());
        for (T o : c) {
            if (set.contains(o)) {
                result.add(o);
            }
        }

        return result;
    }
}
