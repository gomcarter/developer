package com.gomcarter.developer.controller.publics;

import com.gomcarter.developer.service.InterfacesService;
import com.gomcarter.frameworks.interfaces.dto.ApiInterface;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author gomcarter
 */
@RestController
@RequestMapping("publics/interfaces")
public class PublicsInterfacesController {

    @Resource
    private InterfacesService interfacesService;

    @PostMapping(value = "", name = "生成接口")
    public Integer insert(@RequestParam Long javaId, @RequestBody List<ApiInterface> interfaceList) {
        return this.interfacesService.insert(javaId, interfaceList);
    }
}
