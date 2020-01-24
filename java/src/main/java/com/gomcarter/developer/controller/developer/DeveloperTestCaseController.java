package com.gomcarter.developer.controller.developer;

import com.gomcarter.developer.dto.TestCaseDto;
import com.gomcarter.developer.holder.UserHolder;
import com.gomcarter.developer.params.TestCaseParam;
import com.gomcarter.developer.params.TestCaseQueryParam;
import com.gomcarter.developer.service.TestCaseService;
import com.gomcarter.frameworks.base.pager.DefaultPager;
import com.gomcarter.frameworks.interfaces.annotation.Notes;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author gomcarter on 2019-12-17 16:03:19
 */
@RestController
@RequestMapping("developer/testCase")
public class DeveloperTestCaseController {

    @Resource
    TestCaseService testCaseService;

    @GetMapping(value = "list", name = "获取测试用例列表")
    List<TestCaseDto> list(TestCaseQueryParam params, DefaultPager pager) {
        return this.testCaseService.query(params, pager)
                .stream()
                .map(s -> new TestCaseDto()
                        .setId(s.getId())
                        .setName(s.getName())
                        .setUserName(s.getUserName())
                        .setMark(s.getMark())
                        .setCreateTime(s.getCreateTime())
                        .setModifyTime(s.getModifyTime())
                ).collect(Collectors.toList());
    }

    @GetMapping(value = "count", name = "获取测试用例列表总数")
    Integer count(TestCaseQueryParam params) {
        return this.testCaseService.count(params);
    }

    @PostMapping(value = "", name = "新增测试用例")
    void insert(@RequestBody TestCaseParam testItem) {
        testCaseService.create(UserHolder.name(), testItem);
    }

    @PostMapping(value = "{id}", name = "修改测试用例")
    void update(@Notes("主键") @PathVariable("id") Long id, @RequestBody TestCaseParam testItem) {
        testCaseService.update(id, UserHolder.name(), testItem);
    }

    @GetMapping(value = "{id}", name = "获取测试用例详情")
    TestCaseDto get(@Notes("主键") @PathVariable("id") Long id) {
        return this.testCaseService.detail(id);
    }
}
