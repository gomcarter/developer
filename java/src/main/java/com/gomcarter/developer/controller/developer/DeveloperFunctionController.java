package com.gomcarter.developer.controller.developer;

import com.gomcarter.developer.dto.FunctionDto;
import com.gomcarter.developer.entity.Function;
import com.gomcarter.developer.holder.UserHolder;
import com.gomcarter.developer.params.CustomFunctionParam;
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
        params.setCustomFunctionParam(new CustomFunctionParam()
                .setUserName(UserHolder.name())
                .setIsPublic(true)
        );

        return this.functionService.query(params, pager)
                .stream()
                .map(s -> new FunctionDto()
                        .setId(s.getId())
                        .setUserName(s.getUserName())
                        .setName(s.getName())
                        .setMark(s.getMark())
                        .setIsPublic(s.getIsPublic())
                        .setScriptText(s.getScriptText())
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
                @Notes("脚本备注") @RequestParam(required = false) String mark,
                @Notes("脚本备注") @RequestParam(value = "isPublic", defaultValue = "false") Boolean isPublic) {

        functionService.insert(new Function()
                .setName(name)
                .setIsPublic(isPublic)
                .setUserName(UserHolder.name())
                .setMark(mark)
                .setScriptText(scriptText)
        );
    }

    @PutMapping(value = "{id}", name = "修改脚本")
    void update(@Notes("主键") @PathVariable("id") Long id,
                @Notes("脚本名称") @RequestParam String name,
                @Notes("javascript脚本") @RequestParam String scriptText,
                @Notes("脚本备注") @RequestParam String mark,
                @Notes("脚本备注") @RequestParam(value = "isPublic", defaultValue = "false") Boolean isPublic) {
        functionService.update(new Function()
                .setId(id)
                .setName(name)
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
                        .setUserName(s.getUserName())
                        .setIsPublic(s.getIsPublic())
                        .setScriptText(s.getScriptText())
                )
                .orElse(null);
    }
}
