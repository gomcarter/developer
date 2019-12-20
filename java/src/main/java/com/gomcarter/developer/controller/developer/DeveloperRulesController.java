package com.gomcarter.developer.controller.developer;

import com.gomcarter.developer.entity.Rules;
import com.gomcarter.developer.params.RulesParam;
import com.gomcarter.developer.service.RulesService;
import com.gomcarter.frameworks.base.pager.DefaultPager;
import com.gomcarter.frameworks.interfaces.annotation.Notes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author gomcarter on 2019-12-18 17:59:17
 */
@RestController
@RequestMapping("developer/rules")
public class DeveloperRulesController {

    @Autowired
    private RulesService rulesService;

    @GetMapping(value = "", name = "获取脚本列表")
    List<Rules> list(RulesParam params, DefaultPager pager) {
        return this.rulesService.query(params, pager);
    }

    @GetMapping(value = "count", name = "获取脚本总数")
    Integer count(RulesParam params) {
        return this.rulesService.count(params);
    }

    @PostMapping(value = "", name = "新增脚本")
    public void insert(@Notes("脚本名称") @RequestParam String name,
                       @Notes("脚本备注") @RequestParam String mark,
                       @Notes("javascript脚本") @RequestParam String generator) {
        rulesService.insert(new Rules()
                .setName(name)
                .setMark(mark)
                .setGenerator(generator)
        );
    }

    @PutMapping(value = "{id}", name = "修改脚本")
    public void update(@Notes("主键") @PathVariable("id") Long id,
                       @Notes("脚本名称") @RequestParam String name,
                       @Notes("脚本备注") @RequestParam String mark,
                       @Notes("javascript脚本") @RequestParam String generator) {
        rulesService.update(new Rules()
                .setId(id)
                .setName(name)
                .setMark(mark)
                .setGenerator(generator)
        );
    }

    @GetMapping(value = "{id}", name = "获取脚本详情")
    public Rules get(@Notes("主键") @PathVariable("id") Long id) {
        return this.list(new RulesParam().setId(id), new DefaultPager()).get(0);
    }

}
