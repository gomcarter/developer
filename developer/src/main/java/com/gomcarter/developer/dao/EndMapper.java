package com.gomcarter.developer.dao;

import com.gomcarter.developer.entity.End;
import com.gomcarter.frameworks.mybatis.mapper.BaseMapper;

/**
 * @author gomcarter on 2019-06-17 16:41:01
 */
public interface EndMapper extends BaseMapper<End> {

    End getByPrefix(String prefix);
}
