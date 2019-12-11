package com.gomcarter.developer.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author gomcarter
 * @date 2019-06-17 16:41:02
 */
@Data
@Accessors(chain = true)
public class JTestCaseItem {

    /**
     * 主键
     */
    private Long id;

    /**
     * 具体接口
     */
    private String name;
    /**
     * 结果处理器，存储javascript脚本，此脚本应该return下一个接口的参数
     */
    private String resultHandler;
    /**
     * 接口hash值
     */
    private String hash;
    /**
     * 配置
     */
    private String config;
    /**
     * 接口外键
     */
    private Long fkInterfacesId;
    /**
     * 接口用例外键
     */
    private Long fkTestCaseId;
    /**
     *
     */
    private Date createTime;
    /**
     *
     */
    private Date modifyTime;

}
