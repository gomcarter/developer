package com.gomcarter.developer.controller;

import com.gomcarter.developer.dto.FunctionDto;
import com.gomcarter.developer.entity.Function;
import com.gomcarter.developer.holder.UserHolder;
import com.gomcarter.developer.params.FunctionParam;
import com.gomcarter.developer.service.FunctionService;
import com.gomcarter.frameworks.base.common.AssertUtils;
import com.gomcarter.frameworks.base.exception.NoPermissionException;
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
                        .setUserName(s.getUserName())
                        .setName(s.getName())
                        .setMark(s.getMark())
                        .setScriptText(s.getScriptText())
                        .setIsPublic(s.getIsPublic())
                        .setArguments(s.getArguments())
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
                @Notes("javascript脚本") @RequestParam String scriptText,
                @Notes("示例参数") String arguments,
                @Notes("脚本备注") String mark,
                @Notes("脚本备注") @RequestParam(value = "isPublic", defaultValue = "false") Boolean isPublic) {

        functionService.insert(new Function()
                .setName(name)
                .setIsPublic(isPublic)
                .setArguments(arguments)
                .setUserName(UserHolder.name())
                .setMark(mark)
                .setScriptText(scriptText)
        );
    }

    @PutMapping(value = "{id}", name = "修改脚本")
    void update(@Notes("主键") @PathVariable("id") Long id,
                @Notes("脚本名称") @RequestParam String name,
                @Notes("javascript脚本") @RequestParam String scriptText,
                @Notes("示例参数") String arguments,
                @Notes("脚本备注") String mark,
                @Notes("脚本备注") @RequestParam(value = "isPublic", defaultValue = "false") Boolean isPublic) {
        Function function = this.functionService.getById(id);
        AssertUtils.isTrue(UserHolder.name().equals(function.getUserName()), new NoPermissionException());

        functionService.update(function
                .setName(name)
                .setArguments(arguments)
                .setMark(mark)
                .setIsPublic(isPublic)
                .setScriptText(scriptText)
        );

    }

    @GetMapping(value = "{id}", name = "获取脚本详情")
    FunctionDto get(@Notes("主键") @PathVariable("id") Long id) {
        return Optional.ofNullable(functionService.getById(id))
                .map(s -> new FunctionDto()
                        .setId(s.getId())
                        .setName(s.getName())
                        .setMark(s.getMark())
                        .setArguments(s.getArguments())
                        .setUserName(s.getUserName())
                        .setIsPublic(s.getIsPublic())
                        .setScriptText(s.getScriptText())
                )
                .orElse(null);
    }

}
