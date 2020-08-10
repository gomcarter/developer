package com.gomcarter.developer.dao;

import com.gomcarter.developer.entity.Favorite;
import com.gomcarter.frameworks.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 李银 on 2020-07-30 09:37:40
 */
public interface FavoriteMapper extends BaseMapper<Favorite> {

    List<Favorite> getByOwner(@Param("owner") String owner);

    Favorite queryByParentIdAndName(@Param("parentId") Long parentId,
                                    @Param("name") String name,
                                    @Param("favoriteId") Long favoriteId);

    /**
     * 根据父ID获取子分类最大的code
     *
     * @param parentId
     * @return
     */
    String getMaxCode(@Param("parentId") Long parentId, @Param("owner") String owner);

    /**
     * 更新排序
     */
    void updateSort(@Param("parentId") Long parentId,
                    @Param("owner") String owner,
                    @Param("min") Integer min,
                    @Param("max") Integer max,
                    @Param("index") Integer index);
}
