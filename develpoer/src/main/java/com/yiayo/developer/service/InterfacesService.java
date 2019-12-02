package com.yiayo.developer.service;

import com.yiayoframework.base.common.AssertUtils;
import com.yiayoframework.base.exception.CustomException;
import com.yiayoframework.base.mapper.JsonMapper;
import com.yiayoframework.base.pager.Pageable;
import com.yiayo.developer.api.LiyinApi;
import com.yiayo.developer.dao.InterfacesMapper;
import com.yiayo.developer.entity.End;
import com.yiayo.developer.entity.Interfaces;
import com.yiayo.developer.entity.Java;
import com.yiayo.developer.params.JInterfacesQueryParams;
import com.yiayoframework.liyinapi.dto.ApiInterface;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author 李银
 * @ClassName InterfacesService
 * @Description
 * @date 2019-06-17 16:41:01
 */
@Service
public class InterfacesService {

    @Autowired
    private InterfacesMapper interfacesMapper;

    @Autowired
    private JavaService javaService;

    @Autowired
    private EndService endService;

    @Autowired
    private LiyinApi liyinApi;

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

    public List<Interfaces> query(JInterfacesQueryParams params, Pageable pager) {
        return interfacesMapper.query(params, pager);
    }

    public Integer count(JInterfacesQueryParams params) {
        return interfacesMapper.count(params);
    }

    public Integer insert(Long javaId) {
        Java java = javaService.getById(javaId);
        AssertUtils.notNull(java, new CustomException("java项目不正确"));

        List<ApiInterface> interfaceList = liyinApi.get(java);

        // 先把所有接口置为废弃。
        this.interfacesMapper.setDeprecatedByJavaId(javaId);

        Integer success = 0;
        for (ApiInterface s : interfaceList) {
            String url = s.getUrl();
            String prefix = Arrays.stream(url.split("/"))
                    .filter(StringUtils::isNotBlank)
                    .findFirst()
                    .orElse(null);

            End end = endService.getByPrefix(prefix);
            AssertUtils.notNull(end, new CustomException(url + "没有对应的前端系统，请先配置。"));

            String returns = JsonMapper.buildNonNullMapper().toJson(s.getReturns());
            String parameters = JsonMapper.buildNonNullMapper().toJson(s.getParameters());
            String hash = new Md5Hash(
                    StringUtils.join(new String[]{
                            url, javaId.toString(), end.getId().toString(), s.isDeprecated() + "",
                            s.getMark(), s.getMethod(), s.getName(), returns, parameters
                    }, ",")).toHex();

            Interfaces interfaces = this.interfacesMapper.getByUrl(url, s.getMethod());
            // 如果接口没有发生变化，那么对应的hash就是一样的，应该不插入这个接口
            if (interfaces == null) {
                interfaces = new Interfaces()
                        .setUrl(url)
                        .setFkJavaId(javaId)
                        .setHash(hash)
                        .setDeprecated(s.isDeprecated())
                        .setFkEndId(end.getId())
                        .setMark(s.getMark())
                        .setMethod(s.getMethod())
                        .setName(s.getName())
                        .setReturns(returns)
                        .setParameters(parameters);

                this.insert(interfaces);
                success++;
            } else if (!hash.equals(interfaces.getHash())) {
                // 已经存在接口，那么看hash是否改变，改变了就修改，没改变就不修改
                this.update(interfaces.setUrl(url)
                        .setFkJavaId(javaId)
                        .setHash(hash)
                        .setDeprecated(s.isDeprecated())
                        .setFkEndId(end.getId())
                        .setMark(s.getMark())
                        .setMethod(s.getMethod())
                        .setName(s.getName())
                        .setReturns(returns)
                        .setParameters(parameters));

                success++;
            } else {
                this.update(interfaces.setDeprecated(s.isDeprecated()));
            }
        }
        return success;
    }

    public Interfaces getByHash(String hash) {
        return this.interfacesMapper.getByHash(hash);
    }

    public void delete(Long id) {
        this.interfacesMapper.delete(id);
    }
}
