package com.gomcarter.developer.controller.developer;

import com.gomcarter.developer.dto.JTestCase;
import com.gomcarter.developer.entity.TestCase;
import com.gomcarter.developer.params.JTestCaseQueryParams;
import com.gomcarter.developer.service.TestCaseService;
import com.gomcarter.frameworks.base.pager.DefaultPager;
import com.gomcarter.frameworks.interfaces.annotation.Notes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author gomcarter on 2019-12-17 16:03:19
 */
@RestController
@RequestMapping("developer/testCase")
public class DeveloperTestCaseController {

    @Autowired
    private TestCaseService testCaseService;

    @GetMapping(value = "", name = "获取测试用例列表")
    List<JTestCase> list(JTestCaseQueryParams params, DefaultPager pager) {
        return this.testCaseService.query(params, pager)
                .stream()
                .map(s -> new JTestCase()
                        .setId(s.getId())
                        .setName(s.getName())
                        .setFkUserId(s.getFkUserId())
                        .setUserName(s.getUserName())
                        .setMark(s.getMark())
                        .setCreateTime(s.getCreateTime())
                        .setModifyTime(s.getModifyTime())
                ).collect(Collectors.toList());
    }

    @GetMapping(value = "count", name = "获取测试用例列表总数")
    Integer count(JTestCaseQueryParams params) {
        return this.testCaseService.count(params);
    }

    @PostMapping(value = "", name = "新增测试用例")
    public void insert(@Notes("用例名称") @RequestParam String name,
//                       @Notes("用户id") @RequestParam Long fkUserId,
//                       @Notes("用户名称") @RequestParam String userName,
                       @Notes("备注") @RequestParam String mark) {
        testCaseService.insert(new TestCase()
                .setName(name)
//                .setFkUserId(fkUserId)
//                .setUserName(userName)
                .setMark(mark)

        );
    }

    @PutMapping(value = "{id}", name = "修改测试用例")
    public void update(@Notes("主键") @PathVariable("id") Long id,
                       @Notes("用例名称") @RequestParam String name,
//                       @Notes("用户id") @RequestParam Long fkUserId,
//                       @Notes("用户名称") @RequestParam String userName,
                       @Notes("备注") @RequestParam String mark) {
        testCaseService.update(new TestCase()
                .setId(id)
                .setName(name)
//                .setFkUserId(fkUserId)
//                .setUserName(userName)
                .setMark(mark)
        );
    }

    @GetMapping(value = "{id}", name = "获取java项目详情")
    public JTestCase get(@Notes("主键") @PathVariable("id") Long id) {
        return this.list(new JTestCaseQueryParams().setId(id), new DefaultPager()).get(0);
    }


}
