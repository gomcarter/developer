package com.gomcarter.developer.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * @author gomcarter on 2020-03-06 14:08:14
 */
@Data
@Accessors(chain = true)
public class InterfacesPackageDto {

    private Long id;

    private String name;

    private String mark;

    private Long testCaseId;

    private String userName;

    private Date createTime;

    private Date modifyTime;

    private List<Long> interfacesIdList;

}
