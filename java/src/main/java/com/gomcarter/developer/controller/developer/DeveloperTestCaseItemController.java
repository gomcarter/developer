package com.gomcarter.developer.controller.developer;

import com.gomcarter.developer.entity.TestCaseItem;
import com.gomcarter.developer.params.TestCaseItemParam;
import com.gomcarter.developer.service.TestCaseItemService;
import com.gomcarter.frameworks.base.pager.DefaultPager;
import com.gomcarter.frameworks.interfaces.annotation.Notes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author gomcarter on 2019-12-18 16:13:58
 */
@RestController
@RequestMapping("developer/testCaseItem")
public class DeveloperTestCaseItemController {

    @Autowired
    private TestCaseItemService testCaseItemService;

    @GetMapping(value = "", name = "获取接口用例地址列表")
    List<TestCaseItem> list(TestCaseItemParam params, DefaultPager pager) {
        return this.testCaseItemService.query(params, pager);
    }

    @GetMapping(value = "count", name = "获取接口用例地址总数")
    Integer count(TestCaseItemParam params) {
        return this.testCaseItemService.count(params);
    }

    @PostMapping(value = "", name = "新增接口用例")
    public void insert(@Notes("接口用例名称") @RequestParam String name,
                       @Notes("用户id") @RequestParam String resultHandler,
//                       @Notes("用户id") @RequestParam String config,
                       @Notes("接口外键") @RequestParam Long fkInterfacesId,
                       @Notes("接口用例外键") @RequestParam Long fkTestCaseId,
                       @Notes("排序") @RequestParam Integer sort,
                       @Notes("入参") @RequestParam String parmConfig) {
        testCaseItemService.insert(new TestCaseItem()
                .setName(name)
                .setResultHandler(resultHandler)
//                .setConfig(config)
                .setFkInterfacesId(fkInterfacesId)
                .setFkTestCaseId(fkTestCaseId)
                .setSort(sort)
                .setParmConfig(parmConfig)
        );
    }

    @PutMapping(value = "{id}", name = "修改接口用例")
    public void update(@Notes("主键") @PathVariable("id") Long id,
                       @Notes("接口用例名称") @RequestParam String name,
                       @Notes("用户id") @RequestParam String resultHandler,
//                       @Notes("用户id") @RequestParam String config,
                       @Notes("接口外键") @RequestParam Long fkInterfacesId,
                       @Notes("接口用例外键") @RequestParam Long fkTestCaseId,
                       @Notes("排序") @RequestParam Integer sort,
                       @Notes("入参") @RequestParam String parmConfig) {
        testCaseItemService.update(new TestCaseItem()
                .setId(id)
                .setName(name)
                .setResultHandler(resultHandler)
//                .setConfig(config)
                .setFkInterfacesId(fkInterfacesId)
                .setFkTestCaseId(fkTestCaseId)
                .setSort(sort)
                .setParmConfig(parmConfig)
        );
    }

    @GetMapping(value = "{id}", name = "获取接口用例详情")
    public TestCaseItem get(@Notes("主键") @PathVariable("id") Long id) {
        return this.list(new TestCaseItemParam().setId(id), new DefaultPager()).get(0);
    }

}
