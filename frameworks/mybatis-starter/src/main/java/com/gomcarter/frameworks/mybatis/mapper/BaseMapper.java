package com.gomcarter.frameworks.mybatis.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gomcarter.frameworks.base.common.ReflectionUtils;
import com.gomcarter.frameworks.mybatis.pager.DefaultPager;
import com.gomcarter.frameworks.mybatis.pager.Pageable;
import com.gomcarter.frameworks.mybatis.utils.MapperUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

/**
 * Mapper 继承该接口后，无需编写 mapper.xml 文件，即可获得CRUD功能
 * 也支持 写 mapper.xml 文件
 *
 * @author gomcarter on 2019-12-05 11:51:27
 */
public interface BaseMapper<T> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T> {

    /**
     * we have to insert success, throw exception if failed
     *
     * @param entity entity
     * @return the entity
     */
    default T insertNoisy(T entity) {
        int inserted = insert(entity);
        if (inserted <= 0) {
            throw new RuntimeException("insert failed.");
        }
        return entity;
    }

    /**
     * compare version and set new entity， throw exception if failed
     *
     * @param entity entity
     */
    default void casNoisy(T entity) {

        if (cas(entity) <= 0) {
            throw new RuntimeException("update failed, the data is expired.");
        }
    }

    /**
     * compare version and set new entity
     *
     * @param entity entity
     * @return affect rows
     */
    default int cas(T entity) {
        Field version;
        try {
            version = entity.getClass().getField("version");
        } catch (NoSuchFieldException e) {
            return 0;
        }

        Integer oldValue = (Integer) ReflectionUtils.getFieldValue(entity, version);
        Integer newValue = oldValue + 1;

        ReflectionUtils.setField(entity, version, newValue);
        int affectRow = update(entity, new QueryWrapper<T>().eq("version", oldValue));

        if (affectRow <= 0) {
            // 更新失败，把 version 还原
            ReflectionUtils.setField(entity, version, oldValue);
        }

        return affectRow;
    }

    /**
     * 根据 ID 修改
     *
     * @param entity 实体对象
     * @return affect rows
     */
    default int update(T entity) {
        return updateById(entity);
    }

    /**
     * 根据 ID 查询
     *
     * @param id 主键ID
     * @return the entity
     */
    default T getById(Serializable id) {
        return selectById(id);
    }

    /**
     * 通过 idList 查询
     *
     * @param idList 主键ID
     * @return the list of entity
     */
    default List<T> getByIdList(Collection<? extends Serializable> idList) {
        return this.selectList(
                new QueryWrapper<T>()
                        .in("id", idList)
        );
    }

    /**
     * 复杂查询，只返回第一条数据
     *
     * @param params 查询参数
     * @param <R>    参数类型
     * @return the list of entity
     */
    default <R> T getUnique(R params) {
        List<T> result = this.query(params, new DefaultPager(1, 1));
        if (result == null || result.size() <= 0) {
            return null;
        }

        return result.get(0);
    }

    /**
     * 分页查询
     * <p>
     * 根据params 构建一个 queryWrapper，具体规则见：{@link com.gomcarter.frameworks.mybatis.annotation.Condition}
     *
     * @param params   查询参数
     * @param pageable 分页器
     * @param <R>      参数类型
     * @return the list of entity
     */
    default <R> List<T> query(R params, Pageable pageable) {
        Page<T> page = MapperUtils.buildPage(pageable);

        IPage<T> result = this.selectPage(page, MapperUtils.buildQueryWrapper(params));

        return result.getRecords();
    }

    /**
     * 查询总数
     * 根据params 构建一个 queryWrapper，具体规则见：{@link com.gomcarter.frameworks.mybatis.annotation.Condition}
     *
     * @param params 查询参数
     * @param <R>    参数类型
     * @return 总数
     */
    default <R> Integer count(R params) {
        return this.selectCount(MapperUtils.buildQueryWrapper(params));
    }
}
