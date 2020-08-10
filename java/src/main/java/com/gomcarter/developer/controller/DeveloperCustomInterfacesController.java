package com.gomcarter.developer.controller;

import com.gomcarter.developer.dto.CustomInterfacesDetailDto;
import com.gomcarter.developer.entity.CustomInterfaces;
import com.gomcarter.developer.holder.UserHolder;
import com.gomcarter.developer.params.CustomInterfacesQueryParam;
import com.gomcarter.developer.service.CustomInterfacesService;
import com.gomcarter.frameworks.base.pager.DefaultPager;
import com.gomcarter.frameworks.interfaces.annotation.Notes;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author gomcarter
 */
@RestController
@RequestMapping("developer/cusinterfaces")
public class DeveloperCustomInterfacesController {

    @Resource
    CustomInterfacesService customInterfacesService;

    @GetMapping(value = "list", name = "获取我的接口地址列表")
    List<CustomInterfacesDetailDto> list(@Notes("查询参数") CustomInterfacesQueryParam params, @Notes("分页器") DefaultPager pager) {
        return this.customInterfacesService.list(params, pager);
    }

    @GetMapping(value = "count", name = "获取接口地址列表总数")
    Integer count(@Notes("查询参数") CustomInterfacesQueryParam params) {
        return customInterfacesService.getCount(params);
    }

    @DeleteMapping(value = "{id}", name = "删除我的接口列表")
    void delete(@PathVariable("id") Long id) {
        this.customInterfacesService.delete(id);
    }

    @GetMapping(value = "{id}", name = "获取接口详情")
    CustomInterfaces detail(@Notes("查询参数") @PathVariable Long id) {
        return this.customInterfacesService.getByInterfacesId(id);
    }

    @GetMapping(value = "favorites", name = "获取收藏ID列表")
    List<Long> getFavoritesIdList(@Notes("查询参数") @RequestParam("interfacesIdList") List<Long> interfacesIdList) {
        return this.customInterfacesService.getFavoritesIdList(interfacesIdList);
    }

    @PutMapping(value = "bind/{id}", name = "接口绑定到收藏夹")
    void bindFavorite(@PathVariable("id") Long id, String favoriteCode) {
        this.customInterfacesService.updateFavoriteCode(id, favoriteCode, UserHolder.username());
    }

}
