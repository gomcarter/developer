package com.gomcarter.developer.service;

import com.gomcarter.developer.dao.FunctionMapper;
import com.gomcarter.developer.entity.Function;
import com.gomcarter.frameworks.base.pager.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * @author gomcarter on 2020-01-06 10:09:59
 */
@Service
public class FunctionService {

    @Resource
    private FunctionMapper functionMapper;

    public void insert(Function function) {
        functionMapper.insert(function);
    }

    public void insertNoisy(Function function) {
        functionMapper.insertNoisy(function);
    }

    public void update(Function function) {
        functionMapper.update(function);
    }

    public void updateCas(Function function) {
        functionMapper.cas(function);
    }

    public void updateCasNoisy(Function function) {
        functionMapper.casNoisy(function);
    }

    public Function getById(Long id) {
        return functionMapper.getById(id);
    }

    public List<Function> getByIdList(Collection<Long> idList) {
        return functionMapper.getByIdList(idList);
    }

    public <P> List<Function> query(P params, Pageable pager) {
        return  functionMapper.query(params, pager);
    }

    public <P> Integer count(P params) {
        return  functionMapper.count(params);
    }
}
