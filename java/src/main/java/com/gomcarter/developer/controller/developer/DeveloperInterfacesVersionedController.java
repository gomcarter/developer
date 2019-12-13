package com.gomcarter.developer.controller.developer;

import com.gomcarter.developer.entity.InterfacesVersioned;
import com.gomcarter.developer.params.InterfacesVersionedParam;
import com.gomcarter.developer.service.InterfacesVersionedService;
import com.gomcarter.frameworks.mybatis.pager.DefaultPager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author gomcarter on 2019-12-13 11:24:57
 */
@RestController
@RequestMapping("developer/versioned")
public class DeveloperInterfacesVersionedController {

	@Autowired
    private InterfacesVersionedService interfacesVersionedService;

    @GetMapping(value = "query", name = "接口")
    List<InterfacesVersioned> query(InterfacesVersionedParam params, DefaultPager pager) {
        return this.interfacesVersionedService.query(params, pager);
    }

    @GetMapping(value = "count", name = "接口")
    Integer count(InterfacesVersionedParam params) {
        return this.interfacesVersionedService.count(params);
    }

}
