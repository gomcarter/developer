package com.gomcarter.developer.service;

import com.gomcarter.developer.dao.TestCaseMapper;
import com.gomcarter.developer.dto.TestCaseDto;
import com.gomcarter.developer.entity.TestCase;
import com.gomcarter.developer.params.TestCaseParam;
import com.gomcarter.frameworks.base.pager.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author gomcarter
 * @date 2019-06-17 16:41:02
 */
@Service
public class TestCaseService {

    @Resource
    private TestCaseMapper testCaseMapper;

    @Resource
    private InterfacesPackageService interfacesPackageService;

    public TestCase insert(TestCase testCase) {
        testCaseMapper.insert(testCase);
        return testCase;
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

    public TestCaseDto detail(Long id) {
        Objects.requireNonNull(id);

        return Optional.ofNullable(this.getById(id))
                .map(s -> new TestCaseDto()
                        .setId(s.getId())
                        .setName(s.getName())
                        .setPresetParams(s.getPresetParams())
                        .setMark(s.getMark())
                        .setWorkflow(s.getWorkflow())
                        .setUserName(s.getUserName())
                        .setCreateTime(s.getCreateTime())
                        .setModifyTime(s.getModifyTime())
                )
                .orElse(null);
    }

    public void create(String userName, TestCaseParam testCaseParam) {
        TestCase testCase = new TestCase()
                .setName(testCaseParam.getName())
                .setPresetParams(testCaseParam.getPresetParams())
                .setMark(testCaseParam.getMark())
                .setUserName(userName)
                .setWorkflow(testCaseParam.getWorkflow());

        this.insert(testCase);

        if (testCaseParam.getPackageId() != null) {
            interfacesPackageService.updateTestCaseId(testCaseParam.getPackageId(), testCase.getId());
        }
    }

    public void update(Long id, String userName, TestCaseParam testCaseParam) {
        int affectRows = this.testCaseMapper.update(new TestCase()
                .setId(id)
                .setName(testCaseParam.getName())
                .setPresetParams(testCaseParam.getPresetParams())
                .setMark(testCaseParam.getMark())
                .setUserName(userName)
                .setWorkflow(testCaseParam.getWorkflow())
        );

        if (affectRows == 0) {
            throw new RuntimeException("不存在用例：" + id);
        }
    }

    public void delete(Long id) {
        this.testCaseMapper.deleteById(id);
    }
}
