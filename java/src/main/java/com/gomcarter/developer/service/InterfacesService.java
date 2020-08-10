package com.gomcarter.developer.service;

import com.gomcarter.developer.dao.InterfacesMapper;
import com.gomcarter.developer.dto.EndDto;
import com.gomcarter.developer.dto.InterfacesDetailDto;
import com.gomcarter.developer.dto.JavaDto;
import com.gomcarter.developer.entity.End;
import com.gomcarter.developer.entity.Interfaces;
import com.gomcarter.developer.entity.Java;
import com.gomcarter.developer.params.InterfacesQueryParam;
import com.gomcarter.frameworks.base.common.AssertUtils;
import com.gomcarter.frameworks.base.common.BlowfishUtils;
import com.gomcarter.frameworks.base.common.CollectionUtils;
import com.gomcarter.frameworks.base.exception.CustomException;
import com.gomcarter.frameworks.base.pager.DefaultPager;
import com.gomcarter.frameworks.base.pager.Pageable;
import com.gomcarter.frameworks.base.streaming.Streamable;
import com.gomcarter.frameworks.config.mapper.JsonMapper;
import com.gomcarter.frameworks.interfaces.dto.ApiBean;
import com.gomcarter.frameworks.interfaces.dto.ApiInterface;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
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

        Integer success = 0;
        for (ApiInterface api : interfaceList) {
            List<String> words = Arrays.stream(api.getUrl().split("/"))
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.toList());

            String prefix = words.get(0);
            String url = "/" + StringUtils.join(words, "/");

            End end = endService.getByPrefix(prefix);
            if (end == null) {
                end = endService.insertOrGetDefault();
            }

            String returns = JsonMapper.buildNonNullMapper().toJson(api.getReturns());
            String parameters = JsonMapper.buildNonNullMapper().toJson(api.getParameters());

            String hash = this.hash(javaId, end.getId(), api);
            Interfaces interfaces = this.interfacesMapper.getByUrl(javaId, url, api.getMethod());
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
                        .setReturnsClassName(api.getReturnClassName())
                        .setParameters(parameters)
                        .setParametersClassName(api.getParameterClassNames());

                this.insert(interfaces);
                success++;
            } else {
                if (!hash.equals(interfaces.getHash())) {
                    // hash是否改变，改变了就插入历史版本
                    this.interfacesVersionedService.insert(interfaces, api.isDeprecated());
                }

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
                        .setReturnsClassName(api.getReturnClassName())
                        .setParameters(parameters)
                        .setParametersClassName(api.getParameterClassNames())
                        .setModifyTime(null)
                );

                success++;
            }
        }
        return success;
    }

    private String hash(Long javaId, Long endId, ApiInterface api) {
        ApiBean returns = api.getReturns()
                .setComment(null)
                .setDefaults(null)
                .setMock(null);
        // 去除无关影响接口的一些值
        clear(returns.getChildren());
        List<ApiBean> parameters = api.getParameters();
        // 去除无关影响接口的一些值
        clear(parameters);

        return new Md5Hash(
                StringUtils.join(new String[]{
                        javaId.toString(), endId.toString(),
                        JsonMapper.buildNonNullMapper().toJson(returns),
                        JsonMapper.buildNonNullMapper().toJson(parameters)
                }, "_")).toHex();
    }

    private void clear(List<ApiBean> beans) {
        if (beans != null) {
            beans.forEach(c -> {
                c.setComment(null)
                        .setDefaults(null)
                        .setMock(null);

                clear(c.getChildren());
            });

        }
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

                    String publicId = null;
                    try {
                        publicId = BlowfishUtils.encrypt(String.valueOf(s.getId()), s.getHash()) + "|" + s.getHash();
                    } catch (Exception ignore) {
                    }

                    return new InterfacesDetailDto()
                            .setId(s.getId())
                            .setPublicId(publicId)
                            .setInterfacesId(s.getId())
                            .setController(s.getController())
                            .setHash(s.getHash())
                            .setName(s.getName())
                            .setComplexName("【" + end.getName() + "】【" + java.getName() + "】- " + s.getName() + "-【" + s.getUrl() + "】")
                            .setUrl(s.getUrl())
                            .setMethod(s.getMethod())
                            .setReturns(s.getReturns())
                            .setParameters(s.getParameters())
                            .setMark(s.getMark())
                            .setJava(JavaDto.of(java))
                            .setEnd(EndDto.of(end))
                            .setDeprecated(s.getDeprecated())
                            .setCreateTime(s.getCreateTime())
                            .setModifyTime(s.getModifyTime());
                })
                .collect(Collectors.toList());
    }

    public InterfacesDetailDto detail(Long id) {
        List<InterfacesDetailDto> list = this.list(new InterfacesQueryParam().setId(id), new DefaultPager(1, 1));
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    public Map<Long, Interfaces> getMapByIdList(Collection<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return new HashMap<>();
        }
        return Streamable.valueOf(this.getByIdList(idList)).uniqueGroupby(Interfaces::getId).collect();
    }
}
