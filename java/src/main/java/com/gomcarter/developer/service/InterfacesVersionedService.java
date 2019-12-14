package com.gomcarter.developer.service;

import com.gomcarter.developer.dao.InterfacesVersionedMapper;
import com.gomcarter.developer.entity.Interfaces;
import com.gomcarter.developer.entity.InterfacesVersioned;
import com.gomcarter.frameworks.base.pager.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * @author gomcarter on 2019-12-13 11:24:57
 */
@Service
public class InterfacesVersionedService {

    @Autowired
    private InterfacesVersionedMapper interfacesVersionedMapper;

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
}
