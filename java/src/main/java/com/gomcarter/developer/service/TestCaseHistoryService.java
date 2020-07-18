package com.gomcarter.developer.service;

import com.gomcarter.developer.dao.TestCaseHistoryMapper;
import com.gomcarter.developer.entity.TestCaseHistory;
import com.gomcarter.frameworks.base.pager.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * @author 李银 on 2020-07-17 21:35:29
 */
@Service
public class TestCaseHistoryService {

    @Resource
    private TestCaseHistoryMapper testCaseHistoryMapper;

    public TestCaseHistory insert(TestCaseHistory testCaseHistory) {
        testCaseHistoryMapper.insert(testCaseHistory);
        return testCaseHistory;
    }

    public void insertNoisy(TestCaseHistory testCaseHistory) {
        testCaseHistoryMapper.insertNoisy(testCaseHistory);
    }

    public void update(TestCaseHistory testCaseHistory) {
        testCaseHistoryMapper.update(testCaseHistory);
    }

    public void updateCas(TestCaseHistory testCaseHistory) {
        testCaseHistoryMapper.cas(testCaseHistory);
    }

    public void updateCasNoisy(TestCaseHistory testCaseHistory) {
        testCaseHistoryMapper.casNoisy(testCaseHistory);
    }

    public TestCaseHistory getById(Long id) {
        return testCaseHistoryMapper.getById(id);
    }

    public List<TestCaseHistory> getByIdList(Collection<Long> idList) {
        return testCaseHistoryMapper.getByIdList(idList);
    }

    public <P> List<TestCaseHistory> query(P params, Pageable pager) {
        return  testCaseHistoryMapper.query(params, pager);
    }

    public <P> Integer count(P params) {
        return  testCaseHistoryMapper.count(params);
    }

    public void delete(Long id) {
        this.testCaseHistoryMapper.deleteById(id);
    }
}
