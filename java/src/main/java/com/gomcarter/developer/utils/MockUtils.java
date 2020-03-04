package com.gomcarter.developer.utils;

import com.gomcarter.frameworks.base.mapper.JsonMapper;

import java.util.Map;

public class MockUtils {

    public static Object mock(String data) {
        if (data == null) {
            return null;
        }

        if (data.startsWith("[")) {
            return JsonMapper.buildNonNullMapper().fromJsonToList(data, Object.class);
        }
        return JsonMapper.buildNonNullMapper().fromJson(data, Map.class);
    }

    public static void main(String[] args) {
        System.out.println(JsonMapper.buildNonNullMapper().toJson(mock("[1,2,3]")));
        System.out.println(JsonMapper.buildNonNullMapper().toJson(mock("[[1],2,3]")));
        System.out.println(JsonMapper.buildNonNullMapper().toJson(mock("[[1],[2,3]]")));
        System.out.println(JsonMapper.buildNonNullMapper().toJson(mock("[{\"a\":1}, [2,3]]")));
        System.out.println(JsonMapper.buildNonNullMapper().toJson(mock("{\"a\":1,\"b\":[2],\"c\":3}")));
    }
}
