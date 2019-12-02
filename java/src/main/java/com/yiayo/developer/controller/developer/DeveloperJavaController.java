package com.yiayo.developer.controller.developer;

import com.yiayoframework.base.controller.BaseController;
import com.yiayoframework.base.pager.DatagridPager;
import com.yiayo.developer.dto.JJava;
import com.yiayo.developer.entity.Java;
import com.yiayo.developer.params.JJavaQueryParams;
import com.yiayo.developer.service.JavaService;
import com.yiayoframework.liyinapi.annotation.Notes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 李银
 * @ClassName PlatformJavaController
 * @Description:
 * @date: 2017-12-03 15:00:38
 */
@RestController
@RequestMapping("developer/java")
public class DeveloperJavaController extends BaseController {

    @Autowired
    private JavaService javaService;

    @PostMapping(value = "", name = "新增java项目")
    public void list(@Notes("java项目名称") @RequestParam String name,
                     @Notes("开发环境域名") @RequestParam String devDomain,
                     @Notes("测试环境域名") @RequestParam String testDomain,
                     @Notes("预发环境域名") @RequestParam String prevDomain,
                     @Notes("线上环境域名") @RequestParam String onlineDomain) {
        javaService.insert(new Java()
                .setName(name)
                .setName(name)
                .setDevDomain(devDomain)
                .setTestDomain(testDomain)
                .setPrevDomain(prevDomain)
                .setOnlineDomain(onlineDomain)
        );
    }

    @PutMapping(value = "{id}", name = "修改前端项目")
    public void list(@Notes("主键") @PathVariable("id") Long id,
                     @Notes("项目名称") @RequestParam String name,
                     @Notes("开发环境域名") @RequestParam String devDomain,
                     @Notes("测试环境域名") @RequestParam String testDomain,
                     @Notes("预发环境域名") @RequestParam String prevDomain,
                     @Notes("线上环境域名") @RequestParam String onlineDomain) {
        javaService.update(new Java()
                .setId(id)
                .setName(name)
                .setDevDomain(devDomain)
                .setTestDomain(testDomain)
                .setPrevDomain(prevDomain)
                .setOnlineDomain(onlineDomain)
        );
    }

    @GetMapping(value = "{id}", name = "获取java项目详情")
    public JJava get(@Notes("主键") @PathVariable("id") Long id) {
        return this.list(new JJavaQueryParams().setId(id), new DatagridPager()).get(0);
    }

    @GetMapping(value = "list", name = "获取接口地址列表")
    public List<JJava> list(@Notes("查询参数") JJavaQueryParams params, @Notes("分页器") DatagridPager pager) {
        return javaService.query(params, pager)
                .stream()
                .map(s -> new JJava()
                        .setId(s.getId())
                        .setName(s.getName())
                        .setDevDomain(s.getDevDomain())
                        .setTestDomain(s.getTestDomain())
                        .setPrevDomain(s.getPrevDomain())
                        .setOnlineDomain(s.getOnlineDomain())
                        .setCreateTime(s.getCreateTime())
                )
                .collect(Collectors.toList());
    }

    @GetMapping(value = "count", name = "获取接口地址列表总数")
    public Integer count(@Notes("查询参数") JJavaQueryParams params) {
        return javaService.count(params);
    }


}
