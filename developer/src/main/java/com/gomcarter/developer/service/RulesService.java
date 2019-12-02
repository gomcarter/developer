package com.gomcarter.developer.service;

import com.gomcarter.developer.dao.RulesMapper;
import com.gomcarter.developer.entity.Rules;
import com.gomcarter.frameworks.base.pager.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gomcarter.developer.params.JRulesQueryParams;

import java.util.Collection;
import java.util.List;

/**
 *
 * @ClassName RulesService
 * @Description
 * @author gomcarter
 * @date 2019-06-17 16:41:01
 */
@Service
public class RulesService {

    @Autowired
    private RulesMapper rulesMapper;

    public void insert(Rules rules) {
        rulesMapper.insert(rules);
    }

    public void update(Rules rules) {
        rulesMapper.update(rules);
    }

    public Rules getById(Long id) {
        return rulesMapper.getById(id);
    }

    public List<Rules> getByIdList(Collection<Long> idList) {
        return rulesMapper.getByIdList(idList);
    }

    public List<Rules> query(JRulesQueryParams params, Pageable pager) {
        return  rulesMapper.query(params, pager);
    }

    public Integer count(JRulesQueryParams params) {
        return  rulesMapper.count(params);
    }
}
