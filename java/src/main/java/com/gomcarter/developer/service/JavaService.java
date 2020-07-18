package com.gomcarter.developer.service;

import com.gomcarter.developer.dao.JavaMapper;
import com.gomcarter.developer.entity.Java;
import com.gomcarter.frameworks.base.common.CollectionUtils;
import com.gomcarter.frameworks.base.pager.Pageable;
import com.gomcarter.frameworks.base.streaming.Streamable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gomcarter
 * @date 2019-06-17 16:41:01
 */
@Service
public class JavaService {

    @Resource
    private JavaMapper javaMapper;

    public void insert(Java java) {
        javaMapper.insert(java);
    }

    public void update(Java java) {
        javaMapper.update(java);
    }

    public Java getById(Long id) {
        return javaMapper.getById(id);
    }

    public List<Java> getByIdList(Collection<Long> idList) {
        return javaMapper.getByIdList(idList);
    }

    public <R> List<Java> query(R params, Pageable pager) {
        return javaMapper.query(params, pager);
    }

    public <R> Integer count(R params) {
        return javaMapper.count(params);
    }

    public Map<Long, Java> getMapByIdList(Collection<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return new HashMap<>();
        }
        return Streamable.valueOf(this.getByIdList(idList)).uniqueGroupby(Java::getId).collect();
    }
}
