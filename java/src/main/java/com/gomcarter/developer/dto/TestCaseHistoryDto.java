package com.gomcarter.developer.dto;

import com.gomcarter.frameworks.interfaces.annotation.Notes;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author 李银 on 2020-07-17 21:35:29
 */
@Data
@Accessors(chain = true)
public class TestCaseHistoryDto {
    @Notes("主键")
    private Long id;

    @Notes("测试用例id")
    private Long testCaseId;

    @Notes("环境")
    private String env;

    @Notes("用例名称")
    private String name;

    @Notes("用户名称（谁执行的）")
    private String userName;

    @Notes("总共执行次数")
    private Integer total;

    @Notes("成功次数")
    private Integer success;

    @Notes("失败次数")
    private Integer failed;

    @Notes("执行结果")
    private String result;

    @Notes("")
    private Date createTime;

    @Notes("")
    private Date modifyTime;
}
