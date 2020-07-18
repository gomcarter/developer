package com.gomcarter.developer.controller;

import com.gomcarter.developer.dto.InterfacesDetailDto;
import com.gomcarter.developer.dto.JavaDto;
import com.gomcarter.developer.entity.InterfacesVersioned;
import com.gomcarter.developer.entity.Java;
import com.gomcarter.developer.params.InterfacesVersionedParam;
import com.gomcarter.developer.service.InterfacesVersionedService;
import com.gomcarter.developer.service.JavaService;
import com.gomcarter.frameworks.base.common.CustomDateUtils;
import com.gomcarter.frameworks.base.pager.DefaultPager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author gomcarter on 2019-12-13 11:24:57
 */
@RestController
@RequestMapping("developer/versioned")
public class DeveloperInterfacesVersionedController {

    @Resource
    InterfacesVersionedService interfacesVersionedService;

    @Resource
    JavaService javaService;

    @GetMapping(value = "of/{interfacesId}", name = "获取接口地址列表")
    List<InterfacesDetailDto> list(@PathVariable("interfacesId") Long interfacesId) {
        return this.interfacesVersionedService.list(
                new InterfacesVersionedParam().setInterfacesId(interfacesId),
                new DefaultPager(50, 1)
        );
    }

    @GetMapping(value = "simple/list", name = "获取接口地址列表")
    List<InterfacesDetailDto> list(InterfacesVersionedParam params) {
        List<InterfacesVersioned> versionedList = this.interfacesVersionedService.query(params, new DefaultPager(50, 1));
        Map<Long, Java> javaMap = javaService.getMapByIdList(versionedList.stream().map(InterfacesVersioned::getFkJavaId).collect(Collectors.toSet()));

        return versionedList.stream()
                .map(s -> {
                    Java java = javaMap.get(s.getFkJavaId());
                    return new InterfacesDetailDto()
                            .setId(s.getId())
                            .setInterfacesId(s.getFkInterfacesId())
                            .setName(CustomDateUtils.toString(s.getCreateTime()) + " - " + s.getName())
                            .setHash(s.getHash())
                            .setUrl(s.getUrl())
                            .setMethod(s.getMethod())
                            .setDeprecated(s.getDeprecated())
                            .setParameters(s.getParameters())
                            .setReturns(s.getReturns())
                            .setJava(new JavaDto()
                                    .setId(java.getId())
                                    .setName(java.getName())
                                    .setDevDomain(java.getDevDomain())
                                    .setTestDomain(java.getTestDomain())
                                    .setPrevDomain(java.getPrevDomain())
                                    .setOnlineDomain(java.getOnlineDomain())
                            );
                })
                .collect(Collectors.toList());
    }
}
