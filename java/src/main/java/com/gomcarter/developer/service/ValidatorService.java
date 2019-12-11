package com.gomcarter.developer.service;

import com.gomcarter.developer.dao.ValidatorMapper;
import com.gomcarter.developer.entity.Validator;
import com.gomcarter.frameworks.mybatis.pager.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * @author gomcarter
 * @date 2019-06-17 16:41:02
 */
@Service
public class ValidatorService {

    @Autowired
    private ValidatorMapper validatorMapper;

    public void insert(Validator validator) {
        validatorMapper.insert(validator);
    }

    public void update(Validator validator) {
        validatorMapper.update(validator);
    }

    public Validator getById(Long id) {
        return validatorMapper.getById(id);
    }

    public List<Validator> getByIdList(Collection<Long> idList) {
        return validatorMapper.getByIdList(idList);
    }

    public <R> List<Validator> query(R params, Pageable pager) {
        return validatorMapper.query(params, pager);
    }

    public <R> Integer count(R params) {
        return validatorMapper.count(params);
    }
}
