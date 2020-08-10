package com.gomcarter.developer.controller;

import com.gomcarter.developer.dto.JavaDto;
import com.gomcarter.developer.entity.Java;
import com.gomcarter.developer.params.JavaQueryParam;
import com.gomcarter.developer.service.JavaService;
import com.gomcarter.frameworks.base.pager.DefaultPager;
import com.gomcarter.frameworks.interfaces.annotation.Notes;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author gomcarter
 */
@RestController
@RequestMapping("developer/java")
public class DeveloperJavaController {

    @Resource
    JavaService javaService;

    @PostMapping(value = "", name = "新增java项目")
    void list(@Notes("java项目名称") @RequestParam String name,
              @Notes("alias") @RequestParam String alias,
              @Notes("开发环境域名") @RequestParam String devDomain,
              @Notes("测试环境域名") @RequestParam String testDomain,
              @Notes("预发环境域名") @RequestParam String prevDomain,
              @Notes("线上环境域名") @RequestParam String onlineDomain) {
        javaService.insert(new Java()
                .setName(name)
                .setAlias(alias)
                .setDevDomain(devDomain)
                .setTestDomain(testDomain)
                .setPrevDomain(prevDomain)
                .setOnlineDomain(onlineDomain)
        );
    }

    @PutMapping(value = "{id}", name = "修改前端项目")
    void list(@Notes("主键") @PathVariable("id") Long id,
              @Notes("项目名称") @RequestParam String name,
              @Notes("alias") @RequestParam String alias,
              @Notes("开发环境域名") @RequestParam String devDomain,
              @Notes("测试环境域名") @RequestParam String testDomain,
              @Notes("预发环境域名") @RequestParam String prevDomain,
              @Notes("线上环境域名") @RequestParam String onlineDomain) {
        javaService.update(new Java()
                .setId(id)
                .setName(name)
                .setAlias(alias)
                .setDevDomain(devDomain)
                .setTestDomain(testDomain)
                .setPrevDomain(prevDomain)
                .setOnlineDomain(onlineDomain)
        );
    }

    @GetMapping(value = "{id}", name = "获取java项目详情")
    JavaDto get(@Notes("主键") @PathVariable("id") Long id) {
        return this.list(new JavaQueryParam().setId(id), new DefaultPager()).get(0);
    }

    @GetMapping(value = "list", name = "获取java项目列表")
    List<JavaDto> list(@Notes("查询参数") JavaQueryParam params, @Notes("分页器") DefaultPager pager) {
        return javaService.query(params, pager)
                .stream()
                .map(JavaDto::of)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "count", name = "获取java项目列表总数")
    Integer count(@Notes("查询参数") JavaQueryParam params) {
        return javaService.count(params);
    }
}
