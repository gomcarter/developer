package com.gomcarter.developer.controller;

import com.gomcarter.developer.dto.EndAuthDto;
import com.gomcarter.developer.dto.EndDto;
import com.gomcarter.developer.entity.End;
import com.gomcarter.developer.holder.UserHolder;
import com.gomcarter.developer.params.EndParam;
import com.gomcarter.developer.service.EndAuthService;
import com.gomcarter.developer.service.EndService;
import com.gomcarter.frameworks.base.pager.DefaultPager;
import com.gomcarter.frameworks.interfaces.annotation.Notes;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author gomcarter
 * @date 2019-06-17 20:58:17
 */
@RestController
@RequestMapping("developer/end")
public class DeveloperEndController {

    @Resource
    EndService endService;

    @Resource
    EndAuthService endAuthService;

    @PostMapping(value = "", name = "新增前端项目")
    void list(@Notes("项目名称") @RequestParam String name,
              @Notes("对应前缀") @RequestParam String prefix,
              @Notes("header") @RequestParam String header,
              @Notes("配置") @RequestParam String config,
              @Notes("备注") String mark) throws Exception {

        End end = new End().setName(name)
                .setPrefix(prefix)
                .setConfig(config)
                .setHeader(header)
                .setMark(mark);

        endService.insert(end);
    }

    @PutMapping(value = "{id}", name = "修改前端项目")
    void update(@Notes("主键") @PathVariable("id") Long id,
                @Notes("项目名称") @RequestParam String name,
                @Notes("对应前缀") @RequestParam String prefix,
                @Notes("header") @RequestParam String header,
                @Notes("配置") @RequestParam String config,
                @Notes("备注") String mark) throws Exception {

        End end = this.endService.getById(id)
                .setName(name)
                .setPrefix(prefix)
                .setHeader(header)
                .setConfig(config)
                .setMark(mark);

        endService.update(end);
    }

    @GetMapping(value = "{id}", name = "获取前端项目详情")
    EndDto get(@Notes("主键") @PathVariable("id") Long id) {
        return this.list(new EndParam().setId(id), new DefaultPager()).get(0);
    }

    @PutMapping(value = "privates/{endId}", name = "绑定个人认证接口")
    void putPrivatesAuthInterface(@PathVariable("endId") Long endId,
                                  @Notes("配置") @RequestParam String config) throws Exception {
        this.endAuthService.update(endId, UserHolder.name(), config);
    }

    @GetMapping(value = "privates/{endId}", name = "获取个人设置的认证接口")
    EndAuthDto getPrivatesAuthInterface(@PathVariable("endId") Long endId) throws Exception {
        return Optional.ofNullable(this.endAuthService.get(endId, UserHolder.name()))
                .map(s -> new EndAuthDto()
                        .setId(s.getId())
                        .setConfig(s.getConfig()))
                .orElse(
                        Optional.ofNullable(this.endService.getById(endId))
                                .map(s -> new EndAuthDto()
                                        .setConfig(s.getConfig())
                                        .setId(s.getId()))
                                .orElse(null)
                );
    }

    @GetMapping(value = "list", name = "获取接口地址列表")
    List<EndDto> list(@Notes("查询参数") EndParam params, @Notes("分页器") DefaultPager pager) {
        return endService.query(params, pager)
                .stream()
                .map(s -> new EndDto()
                        .setId(s.getId())
                        .setName(s.getName())
                        .setPrefix(s.getPrefix())
                        .setHeader(s.getHeader())
                        .setMark(s.getMark())
                        .setConfig(s.getConfig())
                        .setCreateTime(s.getCreateTime())
                )
                .collect(Collectors.toList());
    }

    @GetMapping(value = "count", name = "获取接口地址列表总数")
    Integer count(@Notes("查询参数") EndParam params) {
        return endService.count(params);
    }
}
