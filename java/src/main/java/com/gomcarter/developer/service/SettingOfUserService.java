package com.gomcarter.developer.service;

import com.gomcarter.developer.dao.SettingOfUserMapper;
import com.gomcarter.developer.entity.SettingOfUser;
import com.gomcarter.developer.params.SettingOfUserParam;
import com.gomcarter.frameworks.base.pager.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * @author gomcarter on 2020-03-02 10:29:14
 */
@Service
public class SettingOfUserService {

    @Resource
    private SettingOfUserMapper settingOfUserMapper;

    public void insert(SettingOfUser settingOfUser) {
        settingOfUserMapper.insert(settingOfUser);
    }

    public void insertNoisy(SettingOfUser settingOfUser) {
        settingOfUserMapper.insertNoisy(settingOfUser);
    }

    public void update(SettingOfUser settingOfUser) {
        settingOfUserMapper.update(settingOfUser);
    }

    public void updateCas(SettingOfUser settingOfUser) {
        settingOfUserMapper.cas(settingOfUser);
    }

    public void updateCasNoisy(SettingOfUser settingOfUser) {
        settingOfUserMapper.casNoisy(settingOfUser);
    }

    public SettingOfUser getById(Long id) {
        return settingOfUserMapper.getById(id);
    }

    public List<SettingOfUser> getByIdList(Collection<Long> idList) {
        return settingOfUserMapper.getByIdList(idList);
    }

    public <P> List<SettingOfUser> query(P params, Pageable pager) {
        return settingOfUserMapper.query(params, pager);
    }

    public <P> Integer count(P params) {
        return settingOfUserMapper.count(params);
    }

    public SettingOfUser get() {
        return this.settingOfUserMapper.getUnique(new SettingOfUserParam());
    }

    public boolean remote() {
        return remote(this.get());
    }

    public static boolean remote(SettingOfUser setting) {
        return setting != null && setting.getRemote() != null && setting.getRemote();
    }

    /**
     * 开启或者关闭远程用户功能
     *
     * @param remote true-开启；false-关闭
     */
    public void update(boolean remote, String url) {
        SettingOfUser setting = get();
        if (setting == null) {
            this.insert(
                    new SettingOfUser()
                            .setRemote(remote)
                            .setUrl(url)
            );
        } else {
            setting.setRemote(remote)
                    .setUrl(url);
            this.update(setting);
        }
    }
}
