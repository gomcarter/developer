package com.yiayo.developer.service;

import com.yiayoframework.base.pager.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yiayo.developer.dao.ValidatorMapper;
import com.yiayo.developer.entity.Validator;
import com.yiayo.developer.service.ValidatorService;
import com.yiayo.developer.params.JValidatorQueryParams;

import java.util.Collection;
import java.util.List;

/**
 *
 * @ClassName ValidatorService
 * @Description
 * @author 李银
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

    public List<Validator> query(JValidatorQueryParams params, Pageable pager) {
        return  validatorMapper.query(params, pager);
    }

    public Integer count(JValidatorQueryParams params) {
        return  validatorMapper.count(params);
    }
}
