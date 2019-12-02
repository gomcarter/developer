package com.yiayo.developer.dao;

import com.yiayoframework.base.pager.Pageable;
import com.yiayo.developer.entity.Rules;
import com.yiayo.developer.params.JRulesQueryParams;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 *
 * @ClassName RulesMapper
 * @Description
 * @author 李银
 * @date 2019-06-17 16:41:01
 */
public interface RulesMapper {

    Long insert(Rules rules);

    Integer update(Rules rules);

    Rules getById(Long id);

    List<Rules> getByIdList(@Param("idList") Collection<Long> idList);

    List<Rules> query(
                @Param("params") JRulesQueryParams params,
                @Param("pager") Pageable pager);

    Integer count(@Param("params") JRulesQueryParams params);
}
