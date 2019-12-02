package com.yiayoframework.base.common;

import com.google.common.collect.Sets;

import java.util.*;

/**
 * @authher 李银 2017年12月2日 08:10:35
 */
public class CollectionUtils {

	public static boolean isNotEmpty(Collection<?> collection) {
		return collection != null && !collection.isEmpty();
	}

	public static boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}

	public static <T> List<T> newList(T... values) {
		List<T> result = new ArrayList<T>();
		for (T object : values) {
			result.add(object);
		}
		return result;
	}

	/**
	 * 根据sortList顺序排序list
	 * @param list
	 * @param propertyName
	 * @param sortList
	 */
	public static <T, K> void sort(List<T> list, final String propertyName, final List<K> sortList) {
		if (CollectionUtils.isEmpty(list)) {
			return;
		}

		try {
			Collections.sort(list, new Comparator<T>() {
				@Override
				public int compare(T o1, T o2) {
					try {
						int index_1;
						index_1 = sortList.indexOf(ReflectionUtils.invokeGetterMethod(o1, propertyName));
						if (index_1 < 0) {
							return 1;
						}
						int index_2 = sortList.indexOf(ReflectionUtils.invokeGetterMethod(o2, propertyName));
						if (index_2 < 0) {
							return -1;
						}
						if (index_1 == index_2) {
							return 0;
						}
						return index_1 - index_2;//Integer.compare(index_1, index_2);
					} catch (IllegalArgumentException e) {
						throw new RuntimeException("排序失败");
					} catch (SecurityException e){
						throw new RuntimeException("排序失败");
					}
				}
			});
		} catch (SecurityException e) {
			throw new RuntimeException("排序失败");
		}
	}

	/**
	 * 两个集合取交集，并去重
	 */
	public static Set retainAll(Collection a, Collection b) {
		Set set;
		Collection c;
		if (a.size() <= b.size()) {
			set = Sets.newHashSet(b);
			c = a;
		} else {
			set = Sets.newHashSet(a);
			c = b;
		}

		Set result = Sets.newHashSetWithExpectedSize(c.size());
		for (Object o : c) {
			if (set.contains(o)) {
				result.add(o);
			}
		}

		return result;
	}

//	public static void main(String[] args) {
//
//		List<JsonData> jsonList = new ArrayList<JsonData>();
//		for(int i = 0 ; i<10;i++){
//			JsonData json = new JsonData();
//			json.setCode(i);
//			jsonList.add(json);
//		}
//
//		sort(jsonList, "code", newList(9,8,11,1,2,3,4,0,7));
//
//		for(JsonData json:jsonList){
//			System.out.println(json.getCode());
//		}
//	}
}
