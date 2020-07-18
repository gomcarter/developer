package com.gomcarter.developer.dao;

import com.gomcarter.developer.entity.Interfaces;
import com.gomcarter.frameworks.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @author gomcarter on 2019-06-17 16:41:01
 */
public interface InterfacesMapper extends BaseMapper<Interfaces> {

    Interfaces getByHash(@Param("hash") String hash);

    Interfaces getByUrl(@Param("javaId") Long javaId, @Param("url") String url, @Param("method") String method);

    void setDeprecatedByJavaId(@Param("javaId") Long javaId);
}
