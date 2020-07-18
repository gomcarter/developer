package com.gomcarter.developer.service;

import com.gomcarter.developer.dao.CustomInterfacesMapper;
import com.gomcarter.developer.dto.CustomInterfacesDetailDto;
import com.gomcarter.developer.dto.EndDto;
import com.gomcarter.developer.dto.JavaDto;
import com.gomcarter.developer.entity.CustomInterfaces;
import com.gomcarter.developer.entity.End;
import com.gomcarter.developer.entity.Interfaces;
import com.gomcarter.developer.entity.Java;
import com.gomcarter.developer.params.CustomInterfacesQueryParam;
import com.gomcarter.frameworks.base.common.AssertUtils;
import com.gomcarter.frameworks.base.common.CollectionUtils;
import com.gomcarter.frameworks.base.exception.CustomException;
import com.gomcarter.frameworks.base.pager.DefaultPager;
import com.gomcarter.frameworks.base.pager.Pageable;
import com.gomcarter.developer.holder.UserHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class CustomInterfacesService {

    @Resource
    private CustomInterfacesMapper customInterfacesMapper;

    @Resource
    private InterfacesService interfacesService;

    @Resource
    private JavaService javaService;

    @Resource
    private EndService endService;

    @Resource
    private InterfacesVersionedService interfacesVersionedService;


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


    public List<CustomInterfacesDetailDto> list(CustomInterfacesQueryParam param, Pageable pager) {
        param.setUsername(UserHolder.username());
        List<CustomInterfaces> customInterfacesList = customInterfacesMapper.getList(param, pager);
        if (CollectionUtils.isEmpty(customInterfacesList)) {
            return new ArrayList<>();
        }

        Map<Long, Java> javaMap = this.javaService.getMapByIdList(customInterfacesList.stream().map(CustomInterfaces::getFkJavaId).collect(Collectors.toSet()));
        Map<Long, End> endMap = endService.getMapByIdList(customInterfacesList.stream().map(CustomInterfaces::getFkEndId).collect(Collectors.toSet()));

        return customInterfacesList.stream()
                .map(s -> {
                    Java java = javaMap.get(s.getFkJavaId());
                    End end = endMap.get(s.getFkEndId());

                    return new CustomInterfacesDetailDto()
                            .setId(s.getId())
                            .setInterfacesId(s.getInterfacesId())
                            .setController(s.getController())
                            .setHash(s.getHash())
                            .setName(s.getName())
                            .setUsername(s.getUsername())
                            .setUrl(s.getUrl())
                            .setParameters(s.getParameters())
                            .setComplexName("【" + end.getName() + "】【" + java.getName() + "】- " + s.getName())
                            .setReturns(s.getReturns())
                            .setMethod(s.getMethod())
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
                                    .setPrefix(end.getPrefix())
                                    .setHeader(end.getHeader())
                                    .setMark(end.getMark())
                            )
                            .setDeprecated(s.getDeprecated())
                            .setCreateTime(s.getCreateTime())
                            .setModifyTime(s.getModifyTime())
                            .setCusParameters(s.getCusParameters());
                })
                .collect(Collectors.toList());
    }


    public void add(Interfaces interfaces, String parameters, String javascript,String preParams) {
        Long interfacesId = interfaces.getId();
        CustomInterfaces c = customInterfacesMapper.getByInterfacesId(interfacesId, UserHolder.username());
        if (c != null && parameters != null) {
            this.customInterfacesMapper.update(c.setCusParameters(parameters).setJavascript(javascript).setPreParams(preParams));
        } else {
            AssertUtils.isNull(c, new CustomException("你已经收藏过了"));
            CustomInterfaces customInterfaces = new CustomInterfaces()
                    .setInterfacesId(interfacesId)
                    .setUsername(UserHolder.username())
                    .setCusParameters(parameters)
                    .setJavascript(javascript)
                    .setPreParams(preParams);
            this.customInterfacesMapper.insert(customInterfaces);
        }
    }

    public CustomInterfaces getByInterfacesId(Long interfacesId) {
        return this.customInterfacesMapper.getByInterfacesId(interfacesId, UserHolder.username());
    }

    public CustomInterfacesDetailDto detail(Long id) {
        List<CustomInterfacesDetailDto> list = this.list(new CustomInterfacesQueryParam().setInterfacesId(id).setUsername(UserHolder.username()), new DefaultPager(1, 1));
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }
}
