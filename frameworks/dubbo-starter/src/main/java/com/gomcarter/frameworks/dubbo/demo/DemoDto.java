package com.gomcarter.frameworks.dubbo.demo;

import java.io.Serializable;

/**
 * @author liyin
 */
public class DemoDto implements Serializable {
    private Integer id;

    private String name;

    public Integer getId() {
        return id;
    }

    public DemoDto setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public DemoDto setName(String name) {
        this.name = name;
        return this;
    }
}
