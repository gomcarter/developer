package com.gomcarter.developer.service;

import com.gomcarter.developer.dao.MarkMapper;
import com.gomcarter.developer.entity.Mark;
import com.gomcarter.frameworks.base.common.AssertUtils;
import com.gomcarter.frameworks.base.pager.Pageable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * @author gomcarter on 2020-02-10 16:05:03
 */
@Service
public class MarkService {

    @Resource
    private MarkMapper markMapper;

    public void insert(Mark mark) {
        markMapper.insert(mark);
    }

    public void insertNoisy(Mark mark) {
        markMapper.insertNoisy(mark);
    }

    public void update(Mark mark) {
        markMapper.update(mark);
    }

    public void updateCas(Mark mark) {
        markMapper.cas(mark);
    }

    public void updateCasNoisy(Mark mark) {
        markMapper.casNoisy(mark);
    }

    public Mark getById(Long id) {
        return markMapper.getById(id);
    }

    public List<Mark> getByIdList(Collection<Long> idList) {
        return markMapper.getByIdList(idList);
    }

    public <P> List<Mark> query(P params, Pageable pager) {
        return markMapper.query(params, pager);
    }

    public <P> Integer count(P params) {
        return markMapper.count(params);
    }

    public void create(Long interfaceId, String user, String mark) {
        AssertUtils.isTrue(StringUtils.isNotBlank(mark), "备注不能为空！");
        this.markMapper.insert(new Mark()
                .setFkInterfacesId(interfaceId)
                .setUser(user)
                .setMark(mark)
        );
    }
}
