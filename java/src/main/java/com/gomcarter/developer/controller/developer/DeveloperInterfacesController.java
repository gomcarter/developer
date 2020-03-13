package com.gomcarter.developer.controller.developer;

import com.gomcarter.developer.dto.InterfacesDetailDto;
import com.gomcarter.developer.dto.JavaDto;
import com.gomcarter.developer.entity.End;
import com.gomcarter.developer.entity.Interfaces;
import com.gomcarter.developer.entity.Java;
import com.gomcarter.developer.params.ArgsParam;
import com.gomcarter.developer.params.InterfacesQueryParam;
import com.gomcarter.developer.service.EndService;
import com.gomcarter.developer.service.InterfacesService;
import com.gomcarter.developer.service.JavaService;
import com.gomcarter.developer.utils.Pair;
import com.gomcarter.frameworks.base.common.CollectionUtils;
import com.gomcarter.frameworks.base.mapper.JsonMapper;
import com.gomcarter.frameworks.base.pager.DefaultPager;
import com.gomcarter.frameworks.interfaces.annotation.Notes;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author gomcarter
 */
@RestController
@RequestMapping("developer/interfaces")
public class DeveloperInterfacesController {

    @Resource
    InterfacesService interfacesService;

    @Resource
    EndService endService;

    @Resource
    JavaService javaService;

    /**
     * 自动生成鉴权 token，配置好鉴权的 jar 包地址，通过反射调用 jar 包里面的生成鉴权的 token 返回到前台
     *
     * @param id interfaces id
     * @return Pair
     * @throws Exception Exception
     */
    @GetMapping(value = "headers/{id}", name = "自动生成headers")
    Pair headers(@PathVariable Long id) throws Exception {
        Interfaces interfaces = this.interfacesService.getById(id);
        End end = this.endService.getById(interfaces.getFkEndId());
        if (StringUtils.isBlank(end.getJarUrl())) {
            return null;
        }
        List<ArgsParam> argsList = JsonMapper.buildNonNullMapper().fromJsonToList(end.getArgs(), ArgsParam.class);
        try {
            Method method = EndService.getMethod(end);
            return new Pair<>(end.getHeader(), method.invoke(null, argsList.stream().map(ArgsParam::getValue).toArray()));
        } catch (Exception e) {
            return null;
        }
    }

    @DeleteMapping(value = "{id}", name = "删除接口")
    void delete(@PathVariable("id") Long id) {
        this.interfacesService.delete(id);
    }

    @GetMapping(value = "simple/list", name = "获取接口地址列表")
    List<InterfacesDetailDto> simpleList(@Notes("查询参数") InterfacesQueryParam params, @Notes("分页器") DefaultPager pager) {
        List<Interfaces> interfacesList = interfacesService.query(params, pager);
        if (CollectionUtils.isEmpty(interfacesList)) {
            return new ArrayList<>();
        }

        Map<Long, Java> javaMap = javaService.getMapByIdList(interfacesList.stream().map(Interfaces::getFkJavaId).collect(Collectors.toSet()));

        return interfacesList.stream()
                .map(s -> {
                    Java java = javaMap.get(s.getFkJavaId());
                    return new InterfacesDetailDto()
                            .setId(s.getId())
                            .setInterfacesId(s.getId())
                            .setHash(s.getHash())
                            .setName(s.getName())
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

    @GetMapping(value = "list", name = "获取接口地址列表")
    List<InterfacesDetailDto> list(@Notes("查询参数") InterfacesQueryParam params, @Notes("分页器") DefaultPager pager) {
        return this.interfacesService.list(params, pager);
    }

    @GetMapping(value = "{id}", name = "获取接口详情")
    InterfacesDetailDto detail(@Notes("查询参数") @PathVariable Long id) {
        return this.interfacesService.detail(id);
    }

    @GetMapping(value = "count", name = "获取接口地址列表总数")
    Integer count(@Notes("查询参数") InterfacesQueryParam params) {
        return interfacesService.count(params);
    }
}
