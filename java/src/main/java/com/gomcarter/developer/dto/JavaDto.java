package com.gomcarter.developer.dto;

import com.gomcarter.developer.entity.Java;
import com.gomcarter.frameworks.interfaces.annotation.Notes;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author gomcarter
 * @date 2019-06-17 16:41:01
 */
@Data
@Accessors(chain = true)
public class JavaDto {

    @Notes("主键")
    private Long id;

    @Notes("java模块名称")
    private String name;

    @Notes("别名")
    private String alias;

    @Notes("开发环境域名")
    private String devDomain;

    @Notes("测试环境域名")
    private String testDomain;

    @Notes("预发环境域名")
    private String prevDomain;

    @Notes("线上环境域名")
    private String onlineDomain;

    @Notes("创建时间")
    private Date createTime;

    public static JavaDto of(Java java) {
        return new JavaDto()
                .setId(java.getId())
                .setName(java.getName())
                .setAlias(java.getAlias())
                .setDevDomain(java.getDevDomain())
                .setTestDomain(java.getTestDomain())
                .setPrevDomain(java.getPrevDomain())
                .setOnlineDomain(java.getOnlineDomain());
    }
}
