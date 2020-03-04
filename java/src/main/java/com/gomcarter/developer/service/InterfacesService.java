package com.gomcarter.developer.service;

import com.alibaba.nacos.client.config.utils.MD5;
import com.gomcarter.developer.dao.InterfacesMapper;
import com.gomcarter.developer.dto.EndDto;
import com.gomcarter.developer.dto.InterfacesDetailDto;
import com.gomcarter.developer.dto.JavaDto;
import com.gomcarter.developer.entity.End;
import com.gomcarter.developer.entity.Interfaces;
import com.gomcarter.developer.entity.Java;
import com.gomcarter.developer.params.InterfacesQueryParam;
import com.gomcarter.frameworks.base.common.AssertUtils;
import com.gomcarter.frameworks.base.common.CollectionUtils;
import com.gomcarter.frameworks.base.exception.CustomException;
import com.gomcarter.frameworks.base.mapper.JsonMapper;
import com.gomcarter.frameworks.base.pager.Pageable;
import com.gomcarter.frameworks.interfaces.dto.ApiInterface;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author gomcarter
 * @date 2019-06-17 16:41:01
 */
@Service
public class InterfacesService {

    @Resource
    private InterfacesMapper interfacesMapper;

    @Resource
    private JavaService javaService;

    @Resource
    private EndService endService;

    @Resource
    private InterfacesVersionedService interfacesVersionedService;

    public void insert(Interfaces interfaces) {
        interfacesMapper.insert(interfaces);
    }

    public void update(Interfaces interfaces) {
        interfacesMapper.update(interfaces);
    }

    public Interfaces getById(Long id) {
        return interfacesMapper.getById(id);
    }

    public List<Interfaces> getByIdList(Collection<Long> idList) {
        return interfacesMapper.getByIdList(idList);
    }

    public <R> List<Interfaces> query(R params, Pageable pager) {
        return interfacesMapper.query(params, pager);
    }

    public <R> Integer count(R params) {
        return interfacesMapper.count(params);
    }

    public Integer insert(Long javaId, List<ApiInterface> interfaceList) {
        if (CollectionUtils.isEmpty(interfaceList)) {
            return 0;
        }

        Java java = javaService.getById(javaId);
        AssertUtils.notNull(java, new CustomException("java项目不正确"));

        // 先把所有接口置为废弃。
        this.interfacesMapper.setDeprecatedByJavaId(javaId);

        Integer success = 0;
        for (ApiInterface api : interfaceList) {
            String url = api.getUrl();
            String prefix = Arrays.stream(url.split("/"))
                    .filter(StringUtils::isNotBlank)
                    .findFirst()
                    .orElse(null);

            End end = endService.getByPrefix(prefix);
            if (end == null) {
                end = endService.insertOrGetDefault();
            }

            String returns = JsonMapper.buildNonNullMapper().toJson(api.getReturns());
            String parameters = JsonMapper.buildNonNullMapper().toJson(api.getParameters());
            String hash = MD5.getInstance().getMD5String(
                    StringUtils.join(new String[]{
                            url, javaId.toString(), end.getId().toString(), api.isDeprecated() + "",
                            api.getMark(), api.getMethod(), api.getName(), returns, parameters, api.getController()
                    }, ","));

            Interfaces interfaces = this.interfacesMapper.getByUrl(url, api.getMethod());
            // 如果接口没有发生变化，那么对应的hash就是一样的，应该不插入这个接口
            if (interfaces == null) {
                interfaces = new Interfaces()
                        .setUrl(url)
                        .setController(api.getController())
                        .setFkJavaId(javaId)
                        .setHash(hash)
                        .setDeprecated(api.isDeprecated())
                        .setFkEndId(end.getId())
                        .setMark(api.getMark())
                        .setMethod(api.getMethod())
                        .setName(api.getName())
                        .setReturns(returns)
                        .setParameters(parameters);

                this.insert(interfaces);
                success++;
            } else if (!hash.equals(interfaces.getHash())) {
                // 插入历史版本
                this.interfacesVersionedService.insert(interfaces, api.isDeprecated());

                // 已经存在接口，那么看hash是否改变，改变了就修改，没改变就不修改
                this.update(interfaces.setUrl(url)
                        .setFkJavaId(javaId)
                        .setHash(hash)
                        .setController(api.getController())
                        .setDeprecated(api.isDeprecated())
                        .setFkEndId(end.getId())
                        .setMark(api.getMark())
                        .setMethod(api.getMethod())
                        .setName(api.getName())
                        .setReturns(returns)
                        .setParameters(parameters)
                );

                success++;
            } else {
                // 接口没有任何变化
                this.update(interfaces.setDeprecated(api.isDeprecated()));
            }
        }
        return success;
    }

    public Interfaces getByHash(String hash) {
        return this.interfacesMapper.getByHash(hash);
    }

    public void delete(Long id) {
        this.interfacesMapper.deleteById(id);
    }

    public List<InterfacesDetailDto> list(InterfacesQueryParam param, Pageable pager) {
        List<Interfaces> interfacesList = this.query(param, pager);

        if (CollectionUtils.isEmpty(interfacesList)) {
            return new ArrayList<>();
        }

        Map<Long, Java> javaMap = this.javaService.getMapByIdList(interfacesList.stream().map(Interfaces::getFkJavaId).collect(Collectors.toSet()));
        Map<Long, End> endMap = endService.getMapByIdList(interfacesList.stream().map(Interfaces::getFkEndId).collect(Collectors.toSet()));

        return interfacesList.stream()
                .map(s -> {
                    Java java = javaMap.get(s.getFkJavaId());
                    End end = endMap.get(s.getFkEndId());

                    return new InterfacesDetailDto()
                            .setId(s.getId())
                            .setInterfacesId(s.getFkEndId())
                            .setController(s.getController())
                            .setHash(s.getHash())
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
