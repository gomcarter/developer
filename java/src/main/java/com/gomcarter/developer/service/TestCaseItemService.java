package com.gomcarter.developer.service;

import com.gomcarter.developer.dao.TestCaseItemMapper;
import com.gomcarter.developer.entity.TestCaseItem;
import com.gomcarter.frameworks.base.pager.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
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

    public <R> List<TestCaseItem> query(R params, Pageable pager) {
        return testCaseItemMapper.query(params, pager);
    }

    public <R> Integer count(R params) {
        return testCaseItemMapper.count(params);
    }
}
