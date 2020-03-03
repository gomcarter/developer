package com.gomcarter.developer.dto;

import com.gomcarter.frameworks.interfaces.annotation.Notes;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author gomcarter on 2020-03-02 10:29:14
 */
@Data
@Accessors(chain = true)
public class SettingOfUserDto {

    /**
     * 是否使用远程登录: 1-调用远程登录接口；2-使用本地user数据库
     */
    @Notes("是否使用远程登录: 1-调用远程登录接口；2-使用本地user数据库")
    private Boolean remote;
    /**
     * 远程登录地址
     */
    @Notes("远程登录地址")
    private String url;
}
