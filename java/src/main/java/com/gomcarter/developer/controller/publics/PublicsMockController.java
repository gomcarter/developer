package com.gomcarter.developer.controller.publics;

import com.gomcarter.developer.entity.Interfaces;
import com.gomcarter.developer.entity.InterfacesVersioned;
import com.gomcarter.developer.service.InterfacesService;
import com.gomcarter.developer.service.InterfacesVersionedService;
import com.gomcarter.developer.utils.MockUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author gomcarter
 */
@RestController
@RequestMapping("publics/mock")
public class PublicsMockController {

    @Resource
    InterfacesService interfacesService;

    @Resource
    InterfacesVersionedService interfacesVersionedService;

    @GetMapping(value = "{hash}", name = "测试")
    Object mock(@PathVariable("hash") String hash) {
        // 先从当前版本找，没找到就去历史版本找，找不到就是null
        String returns = Optional.ofNullable(interfacesService.getByHash(hash)).map(Interfaces::getReturns)
                .orElse(Optional.ofNullable(interfacesVersionedService.getByHash(hash)).map(InterfacesVersioned::getReturns).orElse(null));

        if (returns == null) {
            return null;
        }

        return MockUtils.mock(returns);
    }
}
