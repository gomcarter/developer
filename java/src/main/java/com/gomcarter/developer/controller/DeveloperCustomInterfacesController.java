package com.gomcarter.developer.controller;

import com.gomcarter.developer.dto.CustomInterfacesDetailDto;
import com.gomcarter.developer.dto.CustomInterfacesItemDto;
import com.gomcarter.developer.entity.CustomInterfaces;
import com.gomcarter.developer.entity.CustomInterfacesItem;
import com.gomcarter.developer.holder.UserHolder;
import com.gomcarter.developer.params.CustomInterfacesItemParam;
import com.gomcarter.developer.params.CustomInterfacesQueryParam;
import com.gomcarter.developer.service.CustomInterfacesItemService;
import com.gomcarter.developer.service.CustomInterfacesService;
import com.gomcarter.frameworks.base.pager.DefaultPager;
import com.gomcarter.frameworks.interfaces.annotation.Notes;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author gomcarter
 */
@RestController
@RequestMapping("developer/cusinterfaces")
public class DeveloperCustomInterfacesController {

    @Resource
    CustomInterfacesService customInterfacesService;

    @Resource
    CustomInterfacesItemService customInterfacesItemService;

    @PostMapping(value = "", name = "收藏接口")
    void create(@RequestParam(name = "interfacesId") Long interfacesId) {
        this.customInterfacesService.create(interfacesId);
    }

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

    @GetMapping(value = "", name = "获取收藏接口详情")
    CustomInterfaces getByInterfacesId(@RequestParam("interfacesId") Long interfacesId) {
        return this.customInterfacesService.getByInterfacesId(interfacesId);
    }

    @GetMapping(value = "{id}", name = "获取收藏接口详情")
    CustomInterfaces get(@Notes("查询参数") @PathVariable Long id) {
        return this.customInterfacesService.getById(id);
    }

    @GetMapping(value = "favorites", name = "获取收藏ID列表")
    List<Long> getFavoritesIdList(@Notes("查询参数") @RequestParam("interfacesIdList") List<Long> interfacesIdList) {
        return this.customInterfacesService.getFavoritesIdList(interfacesIdList);
    }

    @PutMapping(value = "bind", name = "接口绑定到收藏夹")
    void bindFavorite(@RequestParam("editingIdList") List<Long> editingIdList, String favoriteCode) {
        this.customInterfacesService.updateFavoriteCode(editingIdList, favoriteCode, UserHolder.username());
    }

    @PostMapping(value = "item", name = "新增用例")
    CustomInterfacesItem createItem(@RequestParam(name = "interfacesId") Long interfacesId,
                                    String name,
                                    String parameters,
                                    String javascript,
                                    String preParams,
                                    String headers) {
        return this.customInterfacesItemService.create(interfacesId, name, parameters, javascript, preParams, headers);
    }

    @PutMapping(value = "item/{id}", name = "修改收藏接口")
    void modifyItem(@PathVariable(name = "id") Long id,
                    String parameters,
                    String javascript,
                    String preParams,
                    String headers) {
        this.customInterfacesItemService.update(id, parameters, javascript, preParams, headers);
    }

    @GetMapping(value = "item/list", name = "获取列表")
    List<CustomInterfacesItemDto> itemList(CustomInterfacesItemParam params, DefaultPager pager) {
        return this.customInterfacesItemService.query(params, pager)
                .stream()
                .map(s -> new CustomInterfacesItemDto()
                        .setId(s.getId())
                        .setName(s.getName())
                        .setCustomInterfacesId(s.getCustomInterfacesId())
                        .setCusHeaders(s.getCusHeaders())
                        .setCusParameters(s.getCusParameters())
                        .setPreParams(s.getPreParams())
                        .setJavascript(s.getJavascript())
                )
                .collect(Collectors.toList());
    }

    @DeleteMapping(value = "item/{id}", name = "删除用例")
    void deleteItem(@PathVariable("id") Long id) {
        this.customInterfacesItemService.delete(id);
    }
}
