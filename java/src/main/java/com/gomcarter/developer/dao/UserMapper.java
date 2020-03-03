package com.gomcarter.developer.dao;

import com.gomcarter.developer.entity.User;
import com.gomcarter.frameworks.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author gomcarter on 2020-03-02 10:16:26
 */
public interface UserMapper extends BaseMapper<User> {

    User getByUsername(@Param("username") String username);
}
