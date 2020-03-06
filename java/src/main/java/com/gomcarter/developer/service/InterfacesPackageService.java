package com.gomcarter.developer.service;

import com.gomcarter.developer.dao.InterfacesPackageMapper;
import com.gomcarter.developer.entity.InterfacesPackage;
import com.gomcarter.developer.holder.UserHolder;
import com.gomcarter.frameworks.base.common.AssertUtils;
import com.gomcarter.frameworks.base.exception.NoPermissionException;
import com.gomcarter.frameworks.base.pager.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * @author gomcarter on 2020-03-06 14:08:14
 */
@Service
public class InterfacesPackageService {

    @Resource
    private InterfacesPackageMapper interfacesPackageMapper;

    @Resource
    private InterfacesPackageItemService interfacesPackageItemService;

    public void insert(InterfacesPackage interfacesPackage) {
        interfacesPackageMapper.insert(interfacesPackage);
    }

    public void insertNoisy(InterfacesPackage interfacesPackage) {
        interfacesPackageMapper.insertNoisy(interfacesPackage);
    }

    public void update(InterfacesPackage interfacesPackage) {
        interfacesPackageMapper.update(interfacesPackage);
    }

    public void updateCas(InterfacesPackage interfacesPackage) {
        interfacesPackageMapper.cas(interfacesPackage);
    }

    public void updateCasNoisy(InterfacesPackage interfacesPackage) {
        interfacesPackageMapper.casNoisy(interfacesPackage);
    }

    public InterfacesPackage getById(Long id) {
        return interfacesPackageMapper.getById(id);
    }

    public List<InterfacesPackage> getByIdList(Collection<Long> idList) {
        return interfacesPackageMapper.getByIdList(idList);
    }

    public <P> List<InterfacesPackage> query(P params, Pageable pager) {
        return interfacesPackageMapper.query(params, pager);
    }

    public <P> Integer count(P params) {
        return interfacesPackageMapper.count(params);
    }

    public void delete(Long id) {
        this.interfacesPackageMapper.deleteById(id);
    }

    public void create(String name, String mark, List<Long> interfacesIdList) {
        InterfacesPackage packaged = new InterfacesPackage()
                .setName(name)
                .setMark(mark)
                .setUserName(UserHolder.name());

        this.interfacesPackageMapper.insert(packaged);

        // 插入行
        this.interfacesPackageItemService.update(packaged.getId(), interfacesIdList);
    }

    public void update(Long id, String name, String mark, List<Long> interfacesIdList) {
        InterfacesPackage packaged = this.interfacesPackageMapper.getById(id);
        AssertUtils.notNull(packaged, new NoPermissionException());

        packaged.setName(name)
                .setMark(mark);

        this.interfacesPackageMapper.update(packaged);

        this.interfacesPackageItemService.update(id, interfacesIdList);
    }
}
