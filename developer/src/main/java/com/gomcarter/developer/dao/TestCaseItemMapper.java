package com.gomcarter.developer.dao;

import com.gomcarter.frameworks.base.pager.Pageable;
import com.gomcarter.developer.entity.TestCaseItem;
import com.gomcarter.developer.params.JTestCaseItemQueryParams;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 *
 * @ClassName TestCaseItemMapper
 * @Description
 * @author gomcarter
 * @date 2019-06-17 16:41:02
 */
public interface TestCaseItemMapper {

    Long insert(TestCaseItem testCaseItem);

    Integer update(TestCaseItem testCaseItem);

    TestCaseItem getById(Long id);

    List<TestCaseItem> getByIdList(@Param("idList") Collection<Long> idList);

    List<TestCaseItem> query(
                @Param("params") JTestCaseItemQueryParams params,
                @Param("pager") Pageable pager);

    Integer count(@Param("params") JTestCaseItemQueryParams params);
}
