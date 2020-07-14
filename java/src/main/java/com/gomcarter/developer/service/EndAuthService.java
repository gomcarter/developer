package com.gomcarter.developer.service;

import com.gomcarter.developer.dao.EndAuthMapper;
import com.gomcarter.developer.entity.EndAuth;
import com.gomcarter.developer.params.EndAuthParam;
import com.gomcarter.frameworks.base.pager.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * @author gomcarter
 * @date 2019-06-17 16:41:01
 */
@Service
public class EndAuthService {

    @Resource
    private EndAuthMapper endAuthMapper;

    public void insert(EndAuth endAuth) {
        endAuthMapper.insert(endAuth);
    }

    public void update(EndAuth endAuth) {
        endAuthMapper.update(endAuth);
    }

    public EndAuth getById(Long id) {
        return endAuthMapper.getById(id);
    }

    public List<EndAuth> getByIdList(Collection<Long> idList) {
        return endAuthMapper.getByIdList(idList);
    }

    public <R> List<EndAuth> query(R params, Pageable pager) {
        return endAuthMapper.query(params, pager);
    }

    public <R> Integer count(R params) {
        return endAuthMapper.count(params);
    }

    public EndAuth get(Long endId, String user) {
        return this.endAuthMapper.getUnique(new EndAuthParam().setFkEndId(endId).setUser(user));
    }

    public void update(Long endId, String username, String config) {
        EndAuth endAuth = this.get(endId, username);
        if (endAuth == null) {
            this.insert(new EndAuth()
                    .setFkEndId(endId)
                    .setUser(username)
                    .setConfig(config)
            );
        } else {
            this.update(endAuth
                    .setConfig(config)
            );
        }
    }
}
