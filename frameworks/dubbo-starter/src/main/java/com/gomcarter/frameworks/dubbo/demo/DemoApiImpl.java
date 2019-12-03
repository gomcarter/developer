package com.gomcarter.frameworks.dubbo.demo;

/**
 * @author gomcarter
 */
public class DemoApiImpl implements DemoApi {

    /**
     * @param id id
     * @return DemoDto
     */
    @Override
    public DemoDto getById(Integer id) {
        return new DemoDto()
                .setId(id)
                .setName("name" + id);
    }
}
