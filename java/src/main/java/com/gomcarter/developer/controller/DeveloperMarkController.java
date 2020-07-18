package com.gomcarter.developer.controller;

import com.gomcarter.developer.dto.MarkDto;
import com.gomcarter.developer.params.MarkParam;
import com.gomcarter.developer.service.MarkService;
import com.gomcarter.frameworks.base.pager.DefaultPager;
import com.gomcarter.developer.holder.UserHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author gomcarter on 2020-02-10 16:05:03
 */
@RestController
@RequestMapping("developer/mark")
public class DeveloperMarkController {

    @Resource
    private MarkService markService;

    @GetMapping(value = "/{interfaceId}", name = "通过接口id获取备注列表")
    List<MarkDto> list(@PathVariable("interfaceId") Long interfaceId) {
        return this.markService.query(new MarkParam().setFkInterfacesId(interfaceId), new DefaultPager(Integer.MAX_VALUE, 1))
                .stream()
                .map(s -> new MarkDto()
                        .setId(s.getId())
                        .setUser(s.getUser())
                        .setMark(s.getMark())
                        .setCreateTime(s.getCreateTime())
                )
                .collect(Collectors.toList());
    }

    @PostMapping(value = "/{interfaceId}", name = "给接口id新增取备")
    void add(@PathVariable("interfaceId") Long interfaceId, @RequestParam String mark) {
        this.markService.create(interfaceId, UserHolder.name(), mark);
    }
}
