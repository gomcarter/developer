package com.yiayo.developer.service;

import com.yiayoframework.base.pager.Pageable;
import com.yiayo.developer.dao.EndMapper;
import com.yiayo.developer.entity.End;
import com.yiayo.developer.params.JEndQueryParams;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 *
 * @ClassName EndService
 * @Description
 * @author 李银
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

    public List<End> query(JEndQueryParams params, Pageable pager) {
        return  endMapper.query(params, pager);
    }

    public Integer count(JEndQueryParams params) {
        return  endMapper.count(params);
    }

    public End getByPrefix(String prefix) {
        if (StringUtils.isBlank(prefix)) {
            return null;
        }
        return this.endMapper.getByPrefix(prefix);
    }
}
