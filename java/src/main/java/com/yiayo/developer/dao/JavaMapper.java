package com.yiayo.developer.dao;

import com.yiayoframework.base.pager.Pageable;
import com.yiayo.developer.entity.Java;
import com.yiayo.developer.params.JJavaQueryParams;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 *
 * @ClassName JavaMapper
 * @Description
 * @author 李银
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
