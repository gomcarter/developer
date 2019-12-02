package com.gomcarter.developer.dao;

import com.gomcarter.frameworks.base.pager.Pageable;
import com.gomcarter.developer.entity.End;
import com.gomcarter.developer.params.JEndQueryParams;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 *
 * @author gomcarter
 * @date 2019-06-17 16:41:01
 */
public interface EndMapper {

    Long insert(End end);

    Integer update(End end);

    End getById(Long id);

    List<End> getByIdList(@Param("idList") Collection<Long> idList);

    List<End> query(
                @Param("params") JEndQueryParams params,
                @Param("pager") Pageable pager);

    Integer count(@Param("params") JEndQueryParams params);

    End getByPrefix(String prefix);
}
