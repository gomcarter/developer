package com.gomcarter.developer.controller.developer;

import com.gomcarter.developer.dto.JEnd;
import com.gomcarter.developer.entity.End;
import com.gomcarter.developer.params.JEndQueryParams;
import com.gomcarter.developer.service.EndService;
import com.gomcarter.frameworks.interfaces.annotation.Notes;
import com.gomcarter.frameworks.mybatis.pager.DefaultPager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author gomcarter
 * @date 2019-06-17 20:58:17
 */
@RestController
@RequestMapping("developer/end")
public class DeveloperEndController {

    @Autowired
    private EndService endService;

    @PostMapping(value = "", name = "新增前端项目")
    public void list(@Notes("项目名称") @RequestParam String name,
                     @Notes("对应前缀") @RequestParam String prefix,
                     @Notes("登录使用的jar包地址") String jarUrl,
                     @Notes("登录使用的类名") String kls,
                     @Notes("登录使用的方法") String method,
                     @Notes("登录使用的jar对应方法的参数: json字符串格式:[{\"key\":\"java.lang.Long\", \"value\": 6}], key是参数的类,value是对应的值") String args,
                     @Notes("header值是什么") String header,
                     @Notes("备注") String mark) {
        endService.insert(
                new End().setName(name)
                        .setPrefix(prefix)
                        .setJarUrl(jarUrl)
                        .setKls(kls)
                        .setMethod(method)
                        .setArgs(args)
                        .setHeader(header)
                        .setMark(mark)
        );
    }

    @PutMapping(value = "{id}", name = "修改前端项目")
    public void list(@Notes("主键") @PathVariable("id") Long id,
                     @Notes("项目名称") @RequestParam String name,
                     @Notes("对应前缀") @RequestParam String prefix,
                     @Notes("登录使用的jar包地址") String jarUrl,
                     @Notes("登录使用的类名") String kls,
                     @Notes("登录使用的方法") String method,
                     @Notes("登录使用的jar对应方法的参数: json字符串格式:[{\"key\":\"java.lang.Long\", \"value\": 6}], key是参数的类,value是对应的值") String args,
                     @Notes("header值是什么") String header,
                     @Notes("备注") String mark) {
        endService.update(
                new End().setId(id)
                        .setName(name)
                        .setPrefix(prefix)
                        .setJarUrl(jarUrl)
                        .setKls(kls)
                        .setMethod(method)
                        .setArgs(args)
                        .setHeader(header)
                        .setMark(mark)
        );
    }

    @GetMapping(value = "{id}", name = "获取前端项目详情")
    public JEnd get(@Notes("主键") @PathVariable("id") Long id) {
        return this.list(new JEndQueryParams().setId(id), new DefaultPager()).get(0);
    }

    @GetMapping(value = "list", name = "获取接口地址列表")
    public List<JEnd> list(@Notes("查询参数") JEndQueryParams params, @Notes("分页器") DefaultPager pager) {
        return endService.query(params, pager)
                .stream()
                .map(s -> new JEnd()
                        .setId(s.getId())
                        .setName(s.getName())
                        .setPrefix(s.getPrefix())
                        .setJarUrl(s.getJarUrl())
                        .setKls(s.getKls())
                        .setMethod(s.getMethod())
                        .setArgs(s.getArgs())
                        .setHeader(s.getHeader())
                        .setMark(s.getMark())
                        .setCreateTime(s.getCreateTime())
                )
                .collect(Collectors.toList());
    }

    @GetMapping(value = "count", name = "获取接口地址列表总数")
    public Integer count(@Notes("查询参数") JEndQueryParams params) {
        return endService.count(params);
    }
}
