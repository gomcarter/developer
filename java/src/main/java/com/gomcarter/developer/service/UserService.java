package com.gomcarter.developer.service;

import com.gomcarter.developer.dao.UserMapper;
import com.gomcarter.developer.entity.User;
import com.gomcarter.developer.holder.UserHolder;
import com.gomcarter.developer.utils.PasswordUtils;
import com.gomcarter.frameworks.base.common.AssertUtils;
import com.gomcarter.frameworks.base.exception.CustomException;
import com.gomcarter.frameworks.base.exception.NoPermissionException;
import com.gomcarter.frameworks.base.pager.Pageable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * @author gomcarter on 2020-03-02 10:16:26
 */
@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private SettingOfUserService settingOfUserService;

    public void insert(User user) {
        userMapper.insert(user);
    }

    public void insertNoisy(User user) {
        userMapper.insertNoisy(user);
    }

    public void update(User user) {
        userMapper.update(user);
    }

    public void updateCas(User user) {
        userMapper.cas(user);
    }

    public void updateCasNoisy(User user) {
        userMapper.casNoisy(user);
    }

    public User getById(Long id) {
        return userMapper.getById(id);
    }

    public List<User> getByIdList(Collection<Long> idList) {
        return userMapper.getByIdList(idList);
    }

    public <P> List<User> query(P params, Pageable pager) {
        return userMapper.query(params, pager);
    }

    public <P> Integer count(P params) {
        return userMapper.count(params);
    }

    /**
     * 不能删除admin账户
     *
     * @param id 待删除账号id
     */
    public void delete(Long id) {
        User user = this.getById(id);
        AssertUtils.isTrue(!UserHolder.ADMIN.equals(user.getUsername()),
                new CustomException("不能删除" + UserHolder.ADMIN + "账号"));
        this.userMapper.deleteById(id);
    }

    /**
     * @param username 账号
     * @param password 密码
     * @return 成功或者失败
     */
    public boolean validate(String username, String password) {
        User user = this.userMapper.getByUsername(username);
        AssertUtils.notNull(user, new CustomException("账号不存在"));

        return PasswordUtils.validate(password, user.getRandom(), user.getPassword());
    }

    public void insert(String username, String name, String cellphone, String mail, String password) {
        AssertUtils.notNull(username, new CustomException("账号不能为空！"));
        AssertUtils.isNull(this.userMapper.getByUsername(username), new CustomException("该账号已经存在：" + username));

//        AssertUtils.isTrue(PasswordUtils.reslove(password) != PasswordUtils.Level.low,
//                new CustomException("密码过于简单，请重新设置一个长度6-20且带数字、字母组合的密码！"));

        String random = PasswordUtils.random();
        User user = new User()
                .setUsername(username)
                .setName(name)
                .setRandom(random)
                .setCellphone(cellphone)
                .setMail(mail)
                .setPassword(PasswordUtils.encrypt(password, random));

        this.insert(user);
    }

    public void update(Long id, String name, String cellphone, String mail, String password) {
        User user = this.userMapper.getById(id);
        AssertUtils.notNull(user, new CustomException("当前账号不存在！"));
        // admin 能修改所有的， 自己能修改自己的 = 要么是admin，要么是自己
        AssertUtils.isTrue(UserHolder.admin() || user.getUsername().equals(UserHolder.name()),
                new NoPermissionException());

        user.setName(name)
                .setCellphone(cellphone)
                .setMail(mail);

        // 密码为空表示没有修改密码
        if (StringUtils.isNotBlank(password)) {
//            AssertUtils.isTrue(PasswordUtils.reslove(password) != PasswordUtils.Level.low,
//                    new CustomException("密码过于简单，请重新设置一个长度6-20且带数字、字母组合的密码！"));

            String random = PasswordUtils.random();
            user.setRandom(random)
                    .setPassword(PasswordUtils.encrypt(password, random));
        }

        this.userMapper.update(user);
    }

    public void updatePassword(String username, String oldPassword, String password) {
        AssertUtils.isTrue(!this.settingOfUserService.remote(), new CustomException("当前已开启外部登录，修改密码功能被禁用！"));

        User user = this.userMapper.getByUsername(username);
        AssertUtils.notNull(user, new NoPermissionException());

        AssertUtils.isTrue(PasswordUtils.validate(oldPassword, user.getRandom(), user.getPassword()), new CustomException("原密码错误！"));

        user.setPassword(PasswordUtils.encrypt(password, user.getRandom()));
        this.update(user);
    }
}
