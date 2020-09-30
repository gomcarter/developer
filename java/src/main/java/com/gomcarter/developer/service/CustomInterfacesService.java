package com.gomcarter.developer.service;

import com.gomcarter.developer.dao.CustomInterfacesMapper;
import com.gomcarter.developer.dto.CustomInterfacesDetailDto;
import com.gomcarter.developer.dto.CustomInterfacesTopDto;
import com.gomcarter.developer.dto.EndDto;
import com.gomcarter.developer.dto.JavaDto;
import com.gomcarter.developer.entity.CustomInterfaces;
import com.gomcarter.developer.entity.End;
import com.gomcarter.developer.entity.Java;
import com.gomcarter.developer.holder.UserHolder;
import com.gomcarter.developer.params.CustomInterfacesQueryParam;
import com.gomcarter.frameworks.base.common.AssertUtils;
import com.gomcarter.frameworks.base.common.CollectionUtils;
import com.gomcarter.frameworks.base.exception.NoPermissionException;
import com.gomcarter.frameworks.base.pager.DefaultPager;
import com.gomcarter.frameworks.base.pager.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class CustomInterfacesService {

    @Resource
    private CustomInterfacesMapper customInterfacesMapper;

    @Resource
    private JavaService javaService;

    @Resource
    private EndService endService;

    public <R> List<CustomInterfaces> query(R params, Pageable pager) {
        return customInterfacesMapper.query(params, pager);
    }

    public Integer getCount(CustomInterfacesQueryParam params) {
        params.setUsername(UserHolder.username());
        return customInterfacesMapper.getCount(params);
    }

    public void delete(Long id) {
        this.customInterfacesMapper.deleteById(id);
    }

    public CustomInterfaces getById(Serializable id) {
        return this.customInterfacesMapper.getById(id);
    }

    public List<CustomInterfacesDetailDto> list(CustomInterfacesQueryParam param, Pageable pager) {
        param.setUsername(UserHolder.username());
        List<CustomInterfacesDetailDto> customInterfacesList = customInterfacesMapper.getList(param, pager);
        if (CollectionUtils.isEmpty(customInterfacesList)) {
            return new ArrayList<>();
        }

        Map<Long, Java> javaMap = this.javaService.getMapByIdList(
                customInterfacesList.stream().map(CustomInterfacesDetailDto::getJavaId).collect(Collectors.toSet())
        );
        Map<Long, End> endMap = endService.getMapByIdList(
                customInterfacesList.stream().map(CustomInterfacesDetailDto::getEndId).collect(Collectors.toSet())
        );

        return customInterfacesList.stream()
                .map(s -> {
                    Java java = javaMap.get(s.getJavaId());
                    End end = endMap.get(s.getEndId());

                    return s.setJava(JavaDto.of(java))
                            .setEnd(EndDto.of(end));
                })
                .collect(Collectors.toList());
    }

    public CustomInterfaces create(Long interfacesId) {
        CustomInterfaces c = customInterfacesMapper.getByInterfacesId(interfacesId, UserHolder.username());
        if (c != null) {
            return c;
        }

        CustomInterfaces customInterfaces = new CustomInterfaces()
                .setInterfacesId(interfacesId)
                .setUsername(UserHolder.username());

        this.customInterfacesMapper.insert(customInterfaces);
        return customInterfaces;
    }

    public CustomInterfaces getByInterfacesId(Long interfacesId) {
        return this.customInterfacesMapper.getByInterfacesId(interfacesId, UserHolder.username());
    }

    public CustomInterfacesDetailDto detail(Long id) {
        List<CustomInterfacesDetailDto> list = this.list(new CustomInterfacesQueryParam().setInterfacesId(id).setUsername(UserHolder.username()), new DefaultPager(1, 1));
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    public List<Long> getFavoritesIdList(List<Long> interfacesIdList) {
        return this.customInterfacesMapper.getFavoritesIdList(interfacesIdList, UserHolder.username());
    }

    public void updateFavoriteCode(List<Long> editingIdList, String favoriteCode, String username) {
        for (Long id : editingIdList) {
            CustomInterfaces ci = this.customInterfacesMapper.getById(id);
            AssertUtils.isTrue(username.equals(ci.getUsername()), new NoPermissionException());

            ci.setFavoriteCode(favoriteCode);

            this.customInterfacesMapper.update(ci);
        }
    }

    public List<CustomInterfacesTopDto> getTop() {
        return this.customInterfacesMapper.queryTop();
    }

}
