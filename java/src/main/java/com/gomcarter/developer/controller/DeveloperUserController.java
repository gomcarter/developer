package com.gomcarter.developer.controller.developer;

import com.gomcarter.developer.dto.UserDto;
import com.gomcarter.developer.holder.UserHolder;
import com.gomcarter.developer.params.UserParam;
import com.gomcarter.developer.service.UserService;
import com.gomcarter.frameworks.base.common.AssertUtils;
import com.gomcarter.frameworks.base.common.CollectionUtils;
import com.gomcarter.frameworks.base.exception.NoPermissionException;
import com.gomcarter.frameworks.base.pager.DefaultPager;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author gomcarter
 */
@RestController
@RequestMapping("developer/user")
public class DeveloperUserController {

    @Resource
    private UserService userService;

    @GetMapping(value = "list", name = "用户列表")
    List<UserDto> list(UserParam params, DefaultPager pager) {
        return this.userService.query(params, pager)
                .stream()
                .map(s -> new UserDto()
                        .setId(s.getId())
                        .setUsername(s.getUsername())
                        .setName(s.getName())
                        .setMail(s.getMail())
                        .setCellphone(s.getCellphone())
                        .setCreateTime(s.getCreateTime())
                        .setModifyTime(s.getModifyTime())
                )
                .collect(Collectors.toList());
    }

    @GetMapping(value = "count", name = "用户列表计算总数")
    Integer count(UserParam params) {
        return this.userService.count(params);
    }

    @DeleteMapping(value = "{id}", name = "删除用户")
    void delete(@PathVariable("id") Long id) {
        this.userService.delete(id);
    }

    @GetMapping(value = "{id}", name = "获取用户信息")
    UserDto get(@PathVariable("id") Long id) {
        List<UserDto> userList = this.list(new UserParam().setId(id), new DefaultPager());
        return CollectionUtils.isEmpty(userList) ? null : userList.get(0);
    }

    @PutMapping(value = "{id}", name = "修改用户信息")
    void update(@PathVariable("id") Long id,
                String name,
                String cellphone,
                String mail,
                String password) {

        this.userService.update(id, name, cellphone, mail, password);
    }

    @PutMapping(value = "password", name = "修改用户信息")
    void update(@RequestParam String oldPassword, @RequestParam String password) {
        String username = UserHolder.name();
        this.userService.updatePassword(username, oldPassword, password);
    }
    @PostMapping(value = "", name = "添加登录用户")
    void update(@RequestParam String username,
                String name,
                String cellphone,
                String mail,
                @RequestParam String password) {

        AssertUtils.isTrue(UserHolder.admin(), new NoPermissionException());

        this.userService.insert(username, name, cellphone, mail, password);
    }
}
