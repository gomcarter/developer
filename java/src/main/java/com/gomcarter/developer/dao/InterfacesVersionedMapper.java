package com.gomcarter.developer.dao;

import com.gomcarter.developer.entity.InterfacesVersioned;
import com.gomcarter.frameworks.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author gomcarter on 2019-12-13 11:24:57
 */
public interface InterfacesVersionedMapper extends BaseMapper<InterfacesVersioned> {

    InterfacesVersioned getByHash(@Param("hash") String hash);
}
