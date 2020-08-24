package com.gomcarter.developer.service;

import com.gomcarter.developer.dao.CustomInterfacesItemMapper;
import com.gomcarter.developer.entity.CustomInterfaces;
import com.gomcarter.developer.entity.CustomInterfacesItem;
import com.gomcarter.developer.holder.UserHolder;
import com.gomcarter.developer.params.CustomInterfacesItemParam;
import com.gomcarter.frameworks.base.common.AssertUtils;
import com.gomcarter.frameworks.base.common.CustomStringUtils;
import com.gomcarter.frameworks.base.exception.CustomException;
import com.gomcarter.frameworks.base.exception.NoPermissionException;
import com.gomcarter.frameworks.base.pager.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * @author 李银 on 2020-08-20 18:09:06
 */
@Service
public class CustomInterfacesItemService {

    @Resource
    private CustomInterfacesItemMapper customInterfacesItemMapper;

    @Resource
    private CustomInterfacesService customInterfacesService;

    public void insert(CustomInterfacesItem customInterfacesItem) {
        customInterfacesItemMapper.insert(customInterfacesItem);
    }

    public void insertNoisy(CustomInterfacesItem customInterfacesItem) {
        customInterfacesItemMapper.insertNoisy(customInterfacesItem);
    }

    public void update(CustomInterfacesItem customInterfacesItem) {
        customInterfacesItemMapper.update(customInterfacesItem);
    }

    public void updateCas(CustomInterfacesItem customInterfacesItem) {
        customInterfacesItemMapper.cas(customInterfacesItem);
    }

    public void updateCasNoisy(CustomInterfacesItem customInterfacesItem) {
        customInterfacesItemMapper.casNoisy(customInterfacesItem);
    }

    public CustomInterfacesItem getById(Long id) {
        return customInterfacesItemMapper.getById(id);
    }

    public List<CustomInterfacesItem> getByIdList(Collection<Long> idList) {
        return customInterfacesItemMapper.getByIdList(idList);
    }

    public <P> List<CustomInterfacesItem> query(P params, Pageable pager) {
        return customInterfacesItemMapper.query(params, pager);
    }

    public <P> Integer count(P params) {
        return customInterfacesItemMapper.count(params);
    }

    public CustomInterfacesItem create(Long interfacesId, String name, String parameters, String javascript, String preParams, String headers) {
        CustomInterfaces cus = this.customInterfacesService.getByInterfacesId(interfacesId);
        if (cus == null) {
            cus = this.customInterfacesService.create(interfacesId);
        }

        if (CustomStringUtils.isBlank(name)) {
            Integer size = this.count(new CustomInterfacesItemParam().setCustomInterfacesId(cus.getId())) + 1;
            name = "用例-" + size;
        }

        CustomInterfacesItem item = new CustomInterfacesItem();
        this.insert(item
                .setName(name)
                .setCustomInterfacesId(cus.getId())
                .setCusParameters(parameters)
                .setCusHeaders(headers)
                .setPreParams(preParams)
                .setJavascript(javascript)
        );

        return item;
    }

    public void update(Long id, String parameters, String javascript, String preParams, String headers) {
        CustomInterfacesItem item = this.customInterfacesItemMapper.getById(id);
        AssertUtils.notNull(item, new CustomException("用例不存在！"));

        CustomInterfaces cus = customInterfacesService.getById(item.getCustomInterfacesId());
        AssertUtils.isTrue(cus.getUsername().equals(UserHolder.name()), new NoPermissionException());

        this.update(item
                .setCusParameters(parameters)
                .setCusHeaders(headers)
                .setPreParams(preParams)
                .setJavascript(javascript)
        );
    }

    public void delete(Long id) {
        this.customInterfacesItemMapper.deleteById(id);
    }
}
