package com.gomcarter.developer.dto;

import com.gomcarter.frameworks.interfaces.annotation.Notes;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author gomcarter
 * @date 2019-06-17 16:41:02
 */
@Data
@Accessors(chain = true)
public class TestCaseItemDto {
    /**
     * 主键
     */
    @Notes("主键")
    private Long id;
    /**
     * 排序值，值相等：则是并行接口；不相等的：小的先执行，大的后执行
     */
    @Notes("排序值，值相等：则是并行接口；不相等的：小的先执行，大的后执行")
    private Integer sort;
    /**
     * 具体接口名称
     */
    @Notes("具体接口名称")
    private String name;
    /**
     * 接口hash值，可以是历史版本
     */
    @Notes("接口hash值，可以是历史版本")
    private String hash;
    /**
     * 接口更新了，是否取最新接口
     */
    @Notes("接口更新了，是否取最新接口")
    private Boolean autoRefresh;
    /**
     * 入参处理器，存储javascript脚本，对入参进行赋值，校验等等操作
     */
    @Notes("入参处理器，存储javascript脚本，对入参进行赋值，校验等等操作")
    private String paramHandler;
    /**
     * 结果处理器，存储javascript脚本，对当前接口返回数据进行校验、转换等等操作
     */
    @Notes("结果处理器，存储javascript脚本，对当前接口返回数据进行校验、转换等等操作")
    private String resultHandler;
    /**
     * 接口用例外键
     */
    @Notes("接口用例外键")
    private Long testCaseId;

    private Date createTime;

    private Date modifyTime;

    @Notes("接口具体信息")
    private InterfacesDetailDto interfaces;
}
