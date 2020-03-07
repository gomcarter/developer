package com.gomcarter.developer.service;

import com.gomcarter.developer.dao.EndMapper;
import com.gomcarter.developer.entity.End;
import com.gomcarter.developer.params.ArgsParam;
import com.gomcarter.frameworks.base.mapper.JsonMapper;
import com.gomcarter.frameworks.base.pager.Pageable;
import com.gomcarter.frameworks.base.streaming.Streamable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author gomcarter
 * @date 2019-06-17 16:41:01
 */
@Service
public class EndService {

    @Resource
    private EndMapper endMapper;

    public void insert(End end) {
        endMapper.insert(end);
    }

    public void update(End end) {
        endMapper.update(end);
    }

    public End getById(Long id) {
        return endMapper.getById(id);
    }

    public List<End> getByIdList(Collection<Long> idList) {
        return endMapper.getByIdList(idList);
    }

    public <R> List<End> query(R params, Pageable pager) {
        return endMapper.query(params, pager);
    }

    public <R> Integer count(R params) {
        return endMapper.count(params);
    }

    public End getByPrefix(String prefix) {
        if (StringUtils.isBlank(prefix)) {
            return null;
        }
        return this.endMapper.getByPrefix(prefix);
    }

    public End insertOrGetDefault() {
        End end = this.getByPrefix("publics");
        if (end == null) {
            end = new End();
            this.insert(
                    end.setName("公共系统")
                            .setPrefix("publics")
            );
        }
        return end;
    }


    public static Method getMethod(End end) throws Exception {
        URL url = new URL(end.getJarUrl());
        URLClassLoader classLoader = new URLClassLoader(new URL[]{url}, Thread.currentThread().getContextClassLoader());
        Class<?> targetKls = classLoader.loadClass(end.getKls());
        List<ArgsParam> argsList = JsonMapper.buildNonNullMapper().fromJsonToList(end.getArgs(), ArgsParam.class);

        Class[] classes = new Class[argsList.size()];
        int i = 0;
        for (ArgsParam arg : argsList) {
            classes[i++] = arg.getKey();
        }

        return targetKls.getMethod(end.getMethod(), classes);
    }

    public Map<Long, End> getMapByIdList(Set<Long> idList) {
        return Streamable.valueOf(this.getByIdList(idList)).uniqueGroupby(End::getId).collect();
    }
}
