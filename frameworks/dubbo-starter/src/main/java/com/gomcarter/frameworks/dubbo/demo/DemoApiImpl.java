package com.gomcarter.frameworks.dubbo.demo;

/**
 * @author gomcarter
 */
public class DemoApiImpl implements DemoApi {

    @Override
    public DemoDto getById(Integer id) {
        return new DemoDto()
                .setId(id)
                .setName("name" + id);
    }
}
