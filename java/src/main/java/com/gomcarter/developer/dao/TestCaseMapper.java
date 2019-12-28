package com.gomcarter.developer.dao;

import com.gomcarter.developer.dto.JTestCaseItemDetail;
import com.gomcarter.developer.entity.TestCase;
import com.gomcarter.frameworks.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author gomcarter
 * @date 2019-06-17 16:41:02
 */
public interface TestCaseMapper extends BaseMapper<TestCase> {
    List<JTestCaseItemDetail> listInterfacesDetail(@Param("id") Long id);
}
