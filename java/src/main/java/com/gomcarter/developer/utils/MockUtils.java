package com.gomcarter.developer.utils;

import com.gomcarter.frameworks.base.mapper.JsonMapper;
import com.gomcarter.frameworks.interfaces.dto.ApiBean;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gomcarter
 */
public class MockUtils {

    public static Object mock(String returns) {
        if (returns == null) {
            return null;
        }

        ApiBean bean = JsonMapper.buildNonNullMapper().fromJson(returns, ApiBean.class);

        return generateReturns(bean);
    }

    private static Object generateReturns(ApiBean node) {
        String type = node.getType();
        if (List.class.getSimpleName().equals(type)) {
            return Collections.singletonList(generateReturns(node.getChildren().get(0)));
        } else if (Object.class.getSimpleName().equals(type)) {
            if (node.getChildren() != null) {
                Map<String, Object> map = new HashMap<>(node.getChildren().size(), 1);
                for (ApiBean s : node.getChildren()) {
                    map.put(s.getKey(), generateReturns(s));
                }
                return map;
            } else {
                return new HashMap<>(0);
            }
        } else if (void.class.getSimpleName().equals(type) || type == null) {
            return null;
        } else {
            return node.getDefaults();
        }
    }

    public static void main(String[] args) {
        System.out.println(JsonMapper.buildNonNullMapper().toJson(mock("{\"notNull\":false,\"body\":false,\"type\":\"List\",\"children\":[{\"notNull\":false,\"body\":false,\"type\":\"Object\",\"children\":[{\"key\":\"id\",\"notNull\":false,\"body\":false,\"defaults\":10,\"comment\":\"规格键id\",\"type\":\"Long\"},{\"key\":\"name\",\"notNull\":false,\"body\":false,\"defaults\":\"规格键名称\",\"comment\":\"规格键名称\",\"type\":\"String\"}]}]}")));
//        System.out.println(JsonMapper.buildNonNullMapper().toJson(mock("[[1],2,3]")));
//        System.out.println(JsonMapper.buildNonNullMapper().toJson(mock("[[1],[2,3]]")));
//        System.out.println(JsonMapper.buildNonNullMapper().toJson(mock("[{\"a\":1}, [2,3]]")));
//        System.out.println(JsonMapper.buildNonNullMapper().toJson(mock("{\"a\":1,\"b\":[2],\"c\":3}")));
    }
}
