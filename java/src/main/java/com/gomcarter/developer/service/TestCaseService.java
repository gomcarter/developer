package com.gomcarter.developer.service;

import com.gomcarter.developer.dao.TestCaseMapper;
import com.gomcarter.developer.entity.TestCase;
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
public class TestCaseService {

    @Autowired
    private TestCaseMapper testCaseMapper;

    public void insert(TestCase testCase) {
        testCaseMapper.insert(testCase);
    }

    public void update(TestCase testCase) {
        testCaseMapper.update(testCase);
    }

    public TestCase getById(Long id) {
        return testCaseMapper.getById(id);
    }

    public List<TestCase> getByIdList(Collection<Long> idList) {
        return testCaseMapper.getByIdList(idList);
    }

    public <R> List<TestCase> query(R params, Pageable pager) {
        return testCaseMapper.query(params, pager);
    }

    public <R> Integer count(R params) {
        return testCaseMapper.count(params);
    }
}
