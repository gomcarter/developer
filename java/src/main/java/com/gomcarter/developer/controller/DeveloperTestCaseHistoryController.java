package com.gomcarter.developer.controller;

import com.gomcarter.developer.dto.TestCaseHistoryDto;
import com.gomcarter.developer.entity.TestCaseHistory;
import com.gomcarter.developer.holder.UserHolder;
import com.gomcarter.developer.params.TestCaseHistoryParam;
import com.gomcarter.developer.params.TestCaseHistoryQueryParam;
import com.gomcarter.developer.service.TestCaseHistoryService;
import com.gomcarter.frameworks.base.common.BeanUtils;
import com.gomcarter.frameworks.base.pager.DefaultPager;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author gomcarter on 2019-12-17 16:03:19
 */
@RestController
@RequestMapping("developer/testCase/history")
public class DeveloperTestCaseHistoryController {
    @Resource
    TestCaseHistoryService testCaseHistoryService;

    @PostMapping(value = "", name = "保存测试用例执行结果")
    Long saveHistory(@RequestBody TestCaseHistoryParam param) {
        TestCaseHistory history = this.testCaseHistoryService.insert(
                new TestCaseHistory()
                        .setTestCaseId(param.getTestCaseId())
                        .setEnv(param.getEnv())
                        .setName(param.getName())
                        .setUserName(UserHolder.name())
                        .setTotal(param.getTotal())
                        .setSuccess(param.getSuccess())
                        .setFailed(param.getTotal() - param.getSuccess())
                        .setResult(param.getResult())
        );
        return history.getId();
    }

    @GetMapping(value = "{id}", name = "获取测试用例执行结果详情")
    TestCaseHistoryDto detail(@PathVariable Long id) {
        return BeanUtils.copyObject(this.testCaseHistoryService.getById(id), new TestCaseHistoryDto());
    }

    @GetMapping(value = "list", name = "测试用例执行结果列表")
    List<TestCaseHistoryDto> list(TestCaseHistoryQueryParam param, DefaultPager pager) {
        return this.testCaseHistoryService.query(param.setUserName(UserHolder.name()), pager)
                .stream()
                .map(s -> new TestCaseHistoryDto()
                        .setId(s.getId())
                        .setTestCaseId(s.getTestCaseId())
                        .setEnv(s.getEnv())
                        .setName(s.getName())
                        .setUserName(s.getUserName())
                        .setTotal(s.getTotal())
                        .setSuccess(s.getSuccess())
                        .setFailed(s.getFailed())
                        .setCreateTime(s.getCreateTime())
                        .setModifyTime(s.getModifyTime())
                )
                .collect(Collectors.toList());
    }

    @GetMapping(value = "count", name = "测试用例执行结果计算总数")
    Integer count(TestCaseHistoryQueryParam param) {
        return this.testCaseHistoryService.count(param.setUserName(UserHolder.name()));
    }

    @DeleteMapping(value = "{id}", name = "删除测试用例执行结果")
    void delete(@PathVariable Long id) {
        this.testCaseHistoryService.delete(id);
    }

}
