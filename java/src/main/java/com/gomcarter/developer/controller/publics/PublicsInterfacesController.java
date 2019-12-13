package com.gomcarter.developer.controller.publics;

import com.gomcarter.developer.service.InterfacesService;
import com.gomcarter.frameworks.interfaces.dto.ApiInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author gomcarter
 */
@RestController
@RequestMapping("developer/interfaces")
public class PublicsInterfacesController {

    @Autowired
    private InterfacesService interfacesService;

    @PostMapping(value = "", name = "生成接口")
    public Integer insert(@RequestParam Long javaId, @RequestBody List<ApiInterface> interfaceList) {
        return this.interfacesService.insert(javaId, interfaceList);
    }
}
