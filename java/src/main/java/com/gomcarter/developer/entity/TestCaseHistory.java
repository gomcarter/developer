package com.gomcarter.developer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author 李银 on 2020-07-17 21:38:50
 */
@Data
@Accessors(chain = true)
public class TestCaseHistory {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 测试用例id
     */
    private Long testCaseId;

    /**
     * 环境
     */
    private String env;

    /**
     * 用例名称
     */
    private String name;
    /**
     * 用户名称（谁执行的）
     */
    private String userName;
    /**
     * 总共执行次数
     */
    private Integer total;
    /**
     * 成功次数
     */
    private Integer success;
    /**
     * 失败次数
     */
    private Integer failed;
    /**
     * 执行结果
     */
    private String result;
    /**
     *
     */
    private Date createTime;
    /**
     *
     */
    private Date modifyTime;

    //@NotReplaceableStart
    // 重新生成代码时，NotReplaceableStart -> NotReplaceableEnd 中间的内容不会被覆盖


    //@NotReplaceableEnd
}
