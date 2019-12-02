package com.gomcarter.developer.dao;

import com.gomcarter.developer.entity.Java;
import com.gomcarter.frameworks.base.pager.Pageable;
import com.gomcarter.developer.params.JJavaQueryParams;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 *
 * @ClassName JavaMapper
 * @Description
 * @author gomcarter
 * @date 2019-06-17 16:41:01
 */
public interface JavaMapper {

    Long insert(Java java);

    Integer update(Java java);

    Java getById(Long id);

    List<Java> getByIdList(@Param("idList") Collection<Long> idList);

    List<Java> query(
                @Param("params") JJavaQueryParams params,
                @Param("pager") Pageable pager);

    Integer count(@Param("params") JJavaQueryParams params);
}
