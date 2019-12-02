package com.yiayoframework.dubbo.demo;

/**
 * @author liyin
 */
public class DemoApiImpl implements DemoApi {

    @Override
    public DemoDto getById(Integer id) {
        return new DemoDto()
                .setId(id)
                .setName("name" + id);
    }
}
