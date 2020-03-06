package com.gomcarter.developer.params;

import com.gomcarter.frameworks.interfaces.annotation.Notes;
import com.gomcarter.frameworks.mybatis.annotation.Condition;
import com.gomcarter.frameworks.mybatis.annotation.MatchType;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import java.util.List;

/**
 * @author gomcarter on 2020-03-06 14:08:36
 */
@Data
@Accessors(chain = true)
public class InterfacesPackageItemParam {

    /**
     * 主键
     */
    @Notes("主键")
    private Long id;

    /**
     * interfaces id
     */
    @Notes("interfaces id")
    private Long interfacesId;
    /**
     * package id
     */
    @Notes("package id")
    private Long interfacesPackageId;

    @Notes("package id")
    @Condition(field = "interfaces_package_id", type = MatchType.IN)
    private List<Long> packageIdList;
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
