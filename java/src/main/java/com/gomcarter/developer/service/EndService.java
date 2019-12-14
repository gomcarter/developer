package com.gomcarter.developer.service;

import com.gomcarter.developer.dao.EndMapper;
import com.gomcarter.developer.entity.End;
import com.gomcarter.frameworks.base.pager.Pageable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * @author gomcarter
 * @date 2019-06-17 16:41:01
 */
@Service
public class EndService {

    @Autowired
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
}
