package com.gomcarter.developer.controller.developer;

import com.gomcarter.developer.dto.FunctionDto;
import com.gomcarter.developer.entity.Function;
import com.gomcarter.developer.params.FunctionParam;
import com.gomcarter.developer.service.FunctionService;
import com.gomcarter.frameworks.base.pager.DefaultPager;
import com.gomcarter.frameworks.interfaces.annotation.Notes;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author gomcarter on 2019-12-18 17:59:17
 */
@RestController
@RequestMapping("developer/function")
public class DeveloperFunctionController {

    @Resource
    FunctionService functionService;

    @GetMapping(value = "list", name = "获取脚本列表")
    List<FunctionDto> list(FunctionParam params, DefaultPager pager) {
        return this.functionService.query(params, pager)
                .stream()
                .map(s -> new FunctionDto()
                        .setId(s.getId())
                        .setName(s.getName())
                        .setMark(s.getMark())
                        .setScript(s.getScript())
                        .setCreateTime(s.getCreateTime())
                        .setModifyTime(s.getModifyTime())
                )
                .collect(Collectors.toList());
    }

    @GetMapping(value = "count", name = "获取脚本总数")
    Integer count(FunctionParam params) {
        return this.functionService.count(params);
    }

    @PostMapping(value = "", name = "新增脚本")
    void insert(@Notes("脚本名称") @RequestParam String name,
                       @Notes("javascript脚本") @RequestParam String script,
                       @Notes("脚本备注") @RequestParam String mark) {

        functionService.insert(new Function()
                .setName(name)
                .setMark(mark)
                .setScript(script)
        );
    }

    @PutMapping(value = "{id}", name = "修改脚本")
    void update(@Notes("主键") @PathVariable("id") Long id,
                       @Notes("脚本名称") @RequestParam String name,
                       @Notes("javascript脚本") @RequestParam String script,
                       @Notes("脚本备注") @RequestParam String mark) {
        functionService.update(new Function()
                .setId(id)
                .setName(name)
                .setMark(mark)
                .setScript(script)
        );
    }

    @GetMapping(value = "{id}", name = "获取脚本详情")
    FunctionDto get(@Notes("主键") @PathVariable("id") Long id) {
        return Optional.ofNullable(functionService.getById(id))
                .map(s -> new FunctionDto()
                        .setId(s.getId())
                        .setName(s.getName())
                        .setMark(s.getMark())
                        .setScript(s.getScript())
                )
                .orElse(null);
    }

}
