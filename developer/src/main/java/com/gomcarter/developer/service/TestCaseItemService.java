package com.gomcarter.developer.service;

import com.gomcarter.developer.dao.TestCaseItemMapper;
import com.gomcarter.frameworks.base.pager.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gomcarter.developer.entity.TestCaseItem;
import com.gomcarter.developer.params.JTestCaseItemQueryParams;

import java.util.Collection;
import java.util.List;

/**
 *
 * @ClassName TestCaseItemService
 * @Description
 * @author gomcarter
 * @date 2019-06-17 16:41:02
 */
@Service
public class TestCaseItemService {

    @Autowired
    private TestCaseItemMapper testCaseItemMapper;

    public void insert(TestCaseItem testCaseItem) {
        testCaseItemMapper.insert(testCaseItem);
    }

    public void update(TestCaseItem testCaseItem) {
        testCaseItemMapper.update(testCaseItem);
    }

    public TestCaseItem getById(Long id) {
        return testCaseItemMapper.getById(id);
    }

    public List<TestCaseItem> getByIdList(Collection<Long> idList) {
        return testCaseItemMapper.getByIdList(idList);
    }

    public List<TestCaseItem> query(JTestCaseItemQueryParams params, Pageable pager) {
        return  testCaseItemMapper.query(params, pager);
    }

    public Integer count(JTestCaseItemQueryParams params) {
        return  testCaseItemMapper.count(params);
    }
}
