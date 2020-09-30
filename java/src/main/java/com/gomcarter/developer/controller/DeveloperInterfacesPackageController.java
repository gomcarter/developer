package com.gomcarter.developer.controller;

import com.gomcarter.developer.dto.InterfacesPackageDto;
import com.gomcarter.developer.entity.InterfacesPackage;
import com.gomcarter.developer.entity.InterfacesPackageItem;
import com.gomcarter.developer.params.InterfacesPackageItemParam;
import com.gomcarter.developer.params.InterfacesPackageParam;
import com.gomcarter.developer.service.InterfacesPackageItemService;
import com.gomcarter.developer.service.InterfacesPackageService;
import com.gomcarter.frameworks.base.common.CollectionUtils;
import com.gomcarter.frameworks.base.pager.DefaultPager;
import com.gomcarter.frameworks.base.streaming.Streamable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author gomcarter on 2020-03-06 14:08:14
 */
@RestController
@RequestMapping("developer/packaged")
public class DeveloperInterfacesPackageController {

    @Resource
    private InterfacesPackageService interfacesPackageService;

    @Resource
    private InterfacesPackageItemService interfacesPackageItemService;

    @GetMapping(value = "list", name = "打包列表")
    List<InterfacesPackageDto> list(InterfacesPackageParam params, DefaultPager pager) {
        List<InterfacesPackage> packageList = this.interfacesPackageService.query(params, pager);
        if (CollectionUtils.isEmpty(packageList)) {
            return new ArrayList<>(0);
        }

        List<Long> packageIdList = packageList.stream().map(InterfacesPackage::getId).collect(Collectors.toList());

        List<InterfacesPackageItem> itemList = this.interfacesPackageItemService.query(new InterfacesPackageItemParam()
                .setPackageIdList(packageIdList), new DefaultPager(Integer.MAX_VALUE, 1));

        Map<Long, List<Long>> map = Streamable.valueOf(itemList).groupby(InterfacesPackageItem::getInterfacesPackageId, InterfacesPackageItem::getInterfacesId).collect();

        return packageList.stream()
                .map(s -> new InterfacesPackageDto()
                        .setId(s.getId())
                        .setName(s.getName())
                        .setMark(s.getMark())
                        .setUserName(s.getUserName())
                        .setConfig(s.getConfig())
                        .setTestCaseId(s.getTestCaseId())
                        .setInterfacesIdList(map.get(s.getId()))
                        .setCreateTime(s.getCreateTime())
                        .setModifyTime(s.getModifyTime())
                ).collect(Collectors.toList());
    }

    @GetMapping(value = "count", name = "打包计算总数")
    Integer count(InterfacesPackageParam params) {
        return this.interfacesPackageService.count(params);
    }

    @GetMapping(value = "{id}", name = "get")
    InterfacesPackageDto get(@PathVariable("id") Long id) {
        List<InterfacesPackageDto> packageList = this.list(new InterfacesPackageParam().setId(id), new DefaultPager());
        return CollectionUtils.isEmpty(packageList) ? null : packageList.get(0);
    }

    @DeleteMapping(value = "{id}", name = "删除")
    void delete(@PathVariable("id") Long id) {
        this.interfacesPackageService.delete(id);
    }

    @PostMapping(value = "", name = "新建打包")
    Long create(@RequestParam String name,
                String mark,
                @RequestParam("interfacesIdList") List<Long> interfacesIdList) {
        return this.interfacesPackageService.create(name, mark, interfacesIdList);
    }

    @PutMapping(value = "{id}", name = "修改打包")
    void update(@PathVariable("id") Long id,
                @RequestParam("interfacesIdList") List<Long> interfacesIdList,
                String config) {
        this.interfacesPackageService.update(id, interfacesIdList, config);
    }
}
