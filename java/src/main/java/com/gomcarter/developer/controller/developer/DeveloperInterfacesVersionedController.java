package com.gomcarter.developer.controller.developer;

import com.gomcarter.developer.dto.JEnd;
import com.gomcarter.developer.dto.JInterfacesDetail;
import com.gomcarter.developer.dto.JJava;
import com.gomcarter.developer.entity.End;
import com.gomcarter.developer.entity.InterfacesVersioned;
import com.gomcarter.developer.entity.Java;
import com.gomcarter.developer.params.InterfacesVersionedParam;
import com.gomcarter.developer.service.EndService;
import com.gomcarter.developer.service.InterfacesVersionedService;
import com.gomcarter.developer.service.JavaService;
import com.gomcarter.frameworks.base.common.CollectionUtils;
import com.gomcarter.frameworks.base.pager.DefaultPager;
import com.gomcarter.frameworks.base.streaming.Streamable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author gomcarter on 2019-12-13 11:24:57
 */
@RestController
@RequestMapping("developer/versioned")
public class DeveloperInterfacesVersionedController {

    @Autowired
    private InterfacesVersionedService interfacesVersionedService;

    @Autowired
    private EndService endService;

    @Autowired
    private JavaService javaService;

    @GetMapping(value = "of/{interfacesId}", name = "获取接口地址列表")
    public List<JInterfacesDetail> list(@PathVariable("interfacesId") Long interfacesId) {
        List<InterfacesVersioned> versionedList = interfacesVersionedService.query(
                new InterfacesVersionedParam().setFkInterfacesId(interfacesId),
                new DefaultPager(50, 1)
        );

        if (CollectionUtils.isEmpty(versionedList)) {
            return new ArrayList<>();
        }

        List<Java> javaList = javaService.getByIdList(versionedList.stream().map(InterfacesVersioned::getFkJavaId).collect(Collectors.toSet()));
        Map<Long, Java> javaMap = Streamable.valueOf(javaList).uniqueGroupby(Java::getId).collect();

        List<End> endList = endService.getByIdList(versionedList.stream().map(InterfacesVersioned::getFkEndId).collect(Collectors.toSet()));
        Map<Long, End> endMap = Streamable.valueOf(endList).uniqueGroupby(End::getId).collect();

        return versionedList.stream()
                .map(s -> {
                    Java java = javaMap.get(s.getFkJavaId());
                    End end = endMap.get(s.getFkEndId());

                    return new JInterfacesDetail()
                            .setId(s.getId())
                            .setController(s.getController())
                            .setName(s.getName())
                            .setUrl(s.getUrl())
                            .setMethod(s.getMethod())
                            .setReturns(s.getReturns())
                            .setParameters(s.getParameters())
                            .setMark(s.getMark())
                            .setJava(new JJava()
                                    .setId(java.getId())
                                    .setName(java.getName())
                                    .setDevDomain(java.getDevDomain())
                                    .setTestDomain(java.getTestDomain())
                                    .setPrevDomain(java.getPrevDomain())
                                    .setOnlineDomain(java.getOnlineDomain())
                            )
                            .setEnd(new JEnd()
                                    .setId(end.getId())
                                    .setName(end.getName())
                                    .setHeader(end.getHeader())
                                    .setPrefix(end.getPrefix())
                                    .setMark(end.getMark())
                            )
                            .setDeprecated(s.getDeprecated())
                            .setCreateTime(s.getCreateTime())
                            .setModifyTime(s.getModifyTime());
                })
                .collect(Collectors.toList());
    }
}
