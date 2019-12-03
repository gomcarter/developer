package com.gomcarter.frameworks.dubbo.demo;

/**
 * @author gomcarter
 */
public interface DemoApi {
    /**
     * @param id id
     * @return DemoDto
     */
    DemoDto getById(Integer id);
}
