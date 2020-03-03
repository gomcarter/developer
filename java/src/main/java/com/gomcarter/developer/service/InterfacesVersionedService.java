package com.gomcarter.developer.service;

import com.gomcarter.developer.dao.InterfacesVersionedMapper;
import com.gomcarter.developer.dto.EndDto;
import com.gomcarter.developer.dto.InterfacesDetailDto;
import com.gomcarter.developer.dto.JavaDto;
import com.gomcarter.developer.entity.End;
import com.gomcarter.developer.entity.Interfaces;
import com.gomcarter.developer.entity.InterfacesVersioned;
import com.gomcarter.developer.entity.Java;
import com.gomcarter.developer.params.InterfacesVersionedParam;
import com.gomcarter.frameworks.base.common.CollectionUtils;
import com.gomcarter.frameworks.base.pager.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author gomcarter on 2019-12-13 11:24:57
 */
@Service
public class InterfacesVersionedService {

    @Resource
    private InterfacesVersionedMapper interfacesVersionedMapper;

    @Resource
    private EndService endService;

    @Resource
    private JavaService javaService;

    public void insert(InterfacesVersioned interfacesVersioned) {
        interfacesVersionedMapper.insert(interfacesVersioned);
    }

    public void insertNoisy(InterfacesVersioned interfacesVersioned) {
        interfacesVersionedMapper.insertNoisy(interfacesVersioned);
    }

    public void update(InterfacesVersioned interfacesVersioned) {
        interfacesVersionedMapper.update(interfacesVersioned);
    }

    public void updateCas(InterfacesVersioned interfacesVersioned) {
        interfacesVersionedMapper.cas(interfacesVersioned);
    }

    public void updateCasNoisy(InterfacesVersioned interfacesVersioned) {
        interfacesVersionedMapper.casNoisy(interfacesVersioned);
    }

    public InterfacesVersioned getById(Long id) {
        return interfacesVersionedMapper.getById(id);
    }

    public List<InterfacesVersioned> getByIdList(Collection<Long> idList) {
        return interfacesVersionedMapper.getByIdList(idList);
    }

    public <P> List<InterfacesVersioned> query(P params, Pageable pager) {
        return interfacesVersionedMapper.query(params, pager);
    }

    public <P> Integer count(P params) {
        return interfacesVersionedMapper.count(params);
    }

    public void insert(Interfaces interfaces, boolean deprecated) {
        this.insert(new InterfacesVersioned()
                .setHash(interfaces.getHash())
                .setFkInterfacesId(interfaces.getId())
                .setUrl(interfaces.getUrl())
                .setController(interfaces.getController())
                .setFkJavaId(interfaces.getFkJavaId())
                .setDeprecated(deprecated)
                .setFkEndId(interfaces.getFkEndId())
                .setMark(interfaces.getMark())
                .setMethod(interfaces.getMethod())
                .setName(interfaces.getName())
                .setReturns(interfaces.getReturns())
                .setParameters(interfaces.getParameters())
        );
    }

    public List<InterfacesDetailDto> list(InterfacesVersionedParam param, Pageable pager) {
        List<InterfacesVersioned> versionedList = this.query(param, pager);

        if (CollectionUtils.isEmpty(versionedList)) {
            return new ArrayList<>();
        }

        Map<Long, Java> javaMap = javaService.getMapByIdList(versionedList.stream().map(InterfacesVersioned::getFkJavaId).collect(Collectors.toSet()));

        Map<Long, End> endMap = endService.getMapByIdList(versionedList.stream().map(InterfacesVersioned::getFkEndId).collect(Collectors.toSet()));

        return versionedList.stream()
                .map(s -> {
                    Java java = javaMap.get(s.getFkJavaId());
                    End end = endMap.get(s.getFkEndId());

                    return new InterfacesDetailDto()
                            .setId(s.getId())
                            .setInterfacesId(s.getFkInterfacesId())
                            .setHash(s.getHash())
                            .setController(s.getController())
                            .setName(s.getName())
                            .setUrl(s.getUrl())
                            .setMethod(s.getMethod())
                            .setReturns(s.getReturns())
                            .setParameters(s.getParameters())
                            .setMark(s.getMark())
                            .setJava(new JavaDto()
                                    .setId(java.getId())
                                    .setName(java.getName())
                                    .setDevDomain(java.getDevDomain())
                                    .setTestDomain(java.getTestDomain())
                                    .setPrevDomain(java.getPrevDomain())
                                    .setOnlineDomain(java.getOnlineDomain())
                            )
                            .setEnd(new EndDto()
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
