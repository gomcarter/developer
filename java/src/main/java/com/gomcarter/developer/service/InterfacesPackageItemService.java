package com.gomcarter.developer.service;

import com.gomcarter.developer.dao.InterfacesPackageItemMapper;
import com.gomcarter.developer.entity.InterfacesPackageItem;
import com.gomcarter.frameworks.base.common.CollectionUtils;
import com.gomcarter.frameworks.base.exception.CustomException;
import com.gomcarter.frameworks.base.pager.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * @author gomcarter on 2020-03-06 14:08:36
 */
@Service
public class InterfacesPackageItemService {

    @Resource
    private InterfacesPackageItemMapper interfacesPackageItemMapper;

    public void insert(InterfacesPackageItem interfacesPackageItem) {
        interfacesPackageItemMapper.insert(interfacesPackageItem);
    }

    public void insertNoisy(InterfacesPackageItem interfacesPackageItem) {
        interfacesPackageItemMapper.insertNoisy(interfacesPackageItem);
    }

    public void update(InterfacesPackageItem interfacesPackageItem) {
        interfacesPackageItemMapper.update(interfacesPackageItem);
    }

    public void updateCas(InterfacesPackageItem interfacesPackageItem) {
        interfacesPackageItemMapper.cas(interfacesPackageItem);
    }

    public void updateCasNoisy(InterfacesPackageItem interfacesPackageItem) {
        interfacesPackageItemMapper.casNoisy(interfacesPackageItem);
    }

    public InterfacesPackageItem getById(Long id) {
        return interfacesPackageItemMapper.getById(id);
    }

    public List<InterfacesPackageItem> getByIdList(Collection<Long> idList) {
        return interfacesPackageItemMapper.getByIdList(idList);
    }

    public <P> List<InterfacesPackageItem> query(P params, Pageable pager) {
        return  interfacesPackageItemMapper.query(params, pager);
    }

    public <P> Integer count(P params) {
        return  interfacesPackageItemMapper.count(params);
    }

    public void update(Long packageId, List<Long> interfacesIdList) {
        if (CollectionUtils.isEmpty(interfacesIdList)) {
            throw new CustomException("至少一个接口才能打包");
        }

        this.interfacesPackageItemMapper.deleteByPackageId(packageId);

        this.interfacesPackageItemMapper.batchInsert(packageId, interfacesIdList);
    }

    public void delete(Long id) {
        this.interfacesPackageItemMapper.deleteById(id);
    }
}
