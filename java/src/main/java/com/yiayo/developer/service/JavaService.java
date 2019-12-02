package com.yiayo.developer.service;

import com.yiayoframework.base.pager.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yiayo.developer.dao.JavaMapper;
import com.yiayo.developer.entity.Java;
import com.yiayo.developer.service.JavaService;
import com.yiayo.developer.params.JJavaQueryParams;

import java.util.Collection;
import java.util.List;

/**
 *
 * @ClassName JavaService
 * @Description
 * @author 李银
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
