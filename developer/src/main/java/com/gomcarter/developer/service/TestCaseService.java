package com.gomcarter.developer.service;

import com.gomcarter.developer.dao.TestCaseMapper;
import com.gomcarter.frameworks.base.pager.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gomcarter.developer.entity.TestCase;
import com.gomcarter.developer.params.JTestCaseQueryParams;

import java.util.Collection;
import java.util.List;

/**
 *
 * @ClassName TestCaseService
 * @Description
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

    public List<TestCase> query(JTestCaseQueryParams params, Pageable pager) {
        return  testCaseMapper.query(params, pager);
    }

    public Integer count(JTestCaseQueryParams params) {
        return  testCaseMapper.count(params);
    }
}
