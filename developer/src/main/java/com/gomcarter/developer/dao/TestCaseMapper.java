package com.gomcarter.developer.dao;

import com.gomcarter.frameworks.base.pager.Pageable;
import com.gomcarter.developer.entity.TestCase;
import com.gomcarter.developer.params.JTestCaseQueryParams;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 *
 * @ClassName TestCaseMapper
 * @Description
 * @author gomcarter
 * @date 2019-06-17 16:41:02
 */
public interface TestCaseMapper {

    Long insert(TestCase testCase);

    Integer update(TestCase testCase);

    TestCase getById(Long id);

    List<TestCase> getByIdList(@Param("idList") Collection<Long> idList);

    List<TestCase> query(
                @Param("params") JTestCaseQueryParams params,
                @Param("pager") Pageable pager);

    Integer count(@Param("params") JTestCaseQueryParams params);
}
