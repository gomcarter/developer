package com.gomcarter.developer.params;

import com.gomcarter.frameworks.base.common.CustomDateUtils;
import com.gomcarter.frameworks.interfaces.annotation.Notes;
import com.gomcarter.frameworks.mybatis.annotation.Condition;
import com.gomcarter.frameworks.mybatis.annotation.MatchType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author gomcarter on 2020-03-02 10:16:26
 */
@Data
@Accessors(chain = true)
public class UserParam {

    @Notes("主键")
    private Long id;

    @Notes("登录账户名")
    @Condition(type = MatchType.LIKE)
    private String username;

    @Notes("姓名")
    @Condition(type = MatchType.LIKE)
    private String name;

    @Notes("邮箱")
    @Condition(type = MatchType.LIKE)
    private String mail;

    @Notes("联系电话")
    @Condition(type = MatchType.LIKE)
    private String cellphone;

    @Notes("创建时间大于等于这个时间")
    @Condition(field = "create_time", type = MatchType.GE)
    private Date createTimeGE;

    @Notes("创建时间小于等于这个时间")
    @Condition(field = "create_time", type = MatchType.LE)
    private Date createTimeLE;

    @Notes("上次修改时间小于等于这个时间")
    @Condition(field = "modify_time", type = MatchType.GE)
    private Date modifyTimeGE;

    @Notes("上次修改时间小于等于这个时间")
    @Condition(field = "modify_time", type = MatchType.LE)
    private Date modifyTimeLE;

    public UserParam setCreateTimeLE(Date createTimeLE) {
        this.createTimeLE = CustomDateUtils.addDays(createTimeLE, 1);
        return this;
    }

    public UserParam setModifyTimeLE(Date modifyTimeLE) {
        this.modifyTimeLE = CustomDateUtils.addDays(modifyTimeLE, 1);
        return this;
    }
}
