package com.gomcarter.developer.service;

import com.gomcarter.developer.dao.JavaMapper;
import com.gomcarter.developer.entity.Java;
import com.gomcarter.frameworks.base.pager.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gomcarter.developer.params.JJavaQueryParams;

import java.util.Collection;
import java.util.List;

/**
 *
 * @ClassName JavaService
 * @Description
 * @author gomcarter
 * @date 2019-06-17 16:41:01
 */
@Service
public class JavaService {

    @Autowired
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

    public List<Java> query(JJavaQueryParams params, Pageable pager) {
        return  javaMapper.query(params, pager);
    }

    public Integer count(JJavaQueryParams params) {
        return  javaMapper.count(params);
    }
}
