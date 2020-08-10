package com.gomcarter.developer.controller;

import com.gomcarter.developer.dto.FavoriteTree;
import com.gomcarter.developer.entity.Favorite;
import com.gomcarter.developer.holder.UserHolder;
import com.gomcarter.developer.service.FavoriteService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 李银 on 2020-07-30 09:37:40
 */
@RestController
@RequestMapping("developer/favorite")
public class DeveloperFavoriteController {

    @Resource
    private FavoriteService favoriteService;

    @GetMapping(value = "tree", name = "获取收藏夹树")
    List<FavoriteTree> tree() {
        return favoriteService.tree(UserHolder.username());
    }

    @PutMapping(value = "{id}", name = "修改收藏夹信息")
    public void updateName(@PathVariable("id") Long id, @RequestParam("name") String name) {
        favoriteService.updateName(id, name, UserHolder.username());
    }

    @PutMapping(value = "sort/{id}", name = "修改收藏夹排序")
    public void updateSort(@PathVariable("id") Long id, @RequestParam("sort") Integer sort) {
        favoriteService.updateSort(id, sort, UserHolder.username());
    }

    @PostMapping(value = "", name = "添加收藏夹")
    public FavoriteTree create(Long parentId, @RequestParam("name") String name) {
        Favorite favorite = this.favoriteService.create(parentId, name, UserHolder.username());
        return new FavoriteTree()
                .setId(favorite.getId())
                .setText(favorite.getName())
                .setIsLeaf(favorite.getIsLeaf())
                .setCode(favorite.getCode())
                .setParentId(favorite.getFkFavoriteId());
    }
}
