package com.yiayo.developer.service;

import com.yiayoframework.base.pager.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yiayo.developer.dao.TestCaseMapper;
import com.yiayo.developer.entity.TestCase;
import com.yiayo.developer.service.TestCaseService;
import com.yiayo.developer.params.JTestCaseQueryParams;

import java.util.Collection;
import java.util.List;

/**
 *
 * @ClassName TestCaseService
 * @Description
 * @author 李银
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
