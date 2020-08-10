package com.gomcarter.developer.dao;

import com.gomcarter.developer.dto.CustomInterfacesDetailDto;
import com.gomcarter.developer.entity.CustomInterfaces;
import com.gomcarter.developer.params.CustomInterfacesQueryParam;
import com.gomcarter.frameworks.base.pager.Pageable;
import com.gomcarter.frameworks.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface CustomInterfacesMapper extends BaseMapper<CustomInterfaces> {

    CustomInterfaces getByInterfacesId(@Param("interfacesId") Long interfacesId, @Param("username") String username);

    List<CustomInterfacesDetailDto> getList(@Param("param") CustomInterfacesQueryParam param, @Param("pager") Pageable pager);

    Integer getCount(@Param("param") CustomInterfacesQueryParam param);

    List<Long> getFavoritesIdList(@Param("interfacesIdList") List<Long> interfacesIdList, @Param("username") String username);

}
