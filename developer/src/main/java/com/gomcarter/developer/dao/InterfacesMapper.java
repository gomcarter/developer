package com.gomcarter.developer.dao;

import com.gomcarter.frameworks.base.pager.Pageable;
import com.gomcarter.developer.entity.Interfaces;
import com.gomcarter.developer.params.JInterfacesQueryParams;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 *
 * @ClassName InterfacesMapper
 * @Description
 * @author gomcarter
 * @date 2019-06-17 16:41:01
 */
public interface InterfacesMapper {

    Long insert(Interfaces interfaces);

    Integer update(Interfaces interfaces);

    Interfaces getById(Long id);

    List<Interfaces> getByIdList(Collection<Long> idList);

    List<Interfaces> query(
                @Param("params") JInterfacesQueryParams params,
                @Param("pager") Pageable pager);

    Integer count(@Param("params") JInterfacesQueryParams params);

    Interfaces getByHash(@Param("hash") String hash);

    Interfaces getByUrl(@Param("url") String url, @Param("method") String method);

    void setDeprecatedByJavaId(@Param("javaId") Long javaId);

    void delete(@Param("id") Long id);
}
