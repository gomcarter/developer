package com.gomcarter.developer.dao;

import com.gomcarter.developer.entity.InterfacesPackageItem;
import com.gomcarter.frameworks.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author gomcarter on 2020-03-06 14:08:36
 */
public interface InterfacesPackageItemMapper extends BaseMapper<InterfacesPackageItem> {

    void batchInsert(@Param("packageId") Long packageId, @Param("interfacesIdList") List<Long> interfacesIdList);

    void deleteByPackageId(@Param("packageId") Long packageId);
}
