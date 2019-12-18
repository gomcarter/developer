package com.gomcarter.developer.params;

import com.gomcarter.frameworks.interfaces.annotation.Notes;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;

/**
 * @author gomcarter on 2019-12-18 16:13:58
 */
@Data
@Accessors(chain = true)
public class TestCaseItemParam {

    /**
     * 主键
     */
    @Notes("主键")
    private Long id;

    /**
     * 具体接口
     */
    @Notes("具体接口")
    private String name;
    /**
     * 结果处理器，存储javascript脚本，此脚本应该return下一个接口的参数
     */
    @Notes("结果处理器，存储javascript脚本，此脚本应该return下一个接口的参数")
    private String resultHandler;
    /**
     * 接口hash值
     */
    @Notes("接口hash值")
    private String hash;
    /**
     * 配置
     */
    @Notes("配置")
    private String config;
    /**
     * 接口外键
     */
    @Notes("接口外键")
    private Long fkInterfacesId;
    /**
     * 接口用例外键
     */
    @Notes("接口用例外键")
    private Long fkTestCaseId;
    /**
     * 
     */
    @Notes("")
    private Date createTime;
    /**
     * 
     */
    @Notes("")
    private Date modifyTime;
}
