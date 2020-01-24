package com.gomcarter.developer.dao;

import com.gomcarter.developer.entity.TestCaseItem;
import com.gomcarter.frameworks.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author gomcarter
 * @date 2019-06-17 16:41:02
 */
public interface TestCaseItemMapper extends BaseMapper<TestCaseItem> {

    /**
     * 根据用例id获取用例行
     */
    List<TestCaseItem> getByTestCaseId(@Param("fk_test_case_id") Long testCaseId);

    /**
     * 批量插入
     *
     * @param itemList itemList
     */
    void batchInsert(@Param("itemList") List<TestCaseItem> itemList);
}
