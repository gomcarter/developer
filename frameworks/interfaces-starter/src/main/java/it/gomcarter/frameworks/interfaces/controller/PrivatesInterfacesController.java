package it.gomcarter.frameworks.interfaces.controller;

import it.gomcarter.frameworks.interfaces.dto.ApiInterface;
import it.gomcarter.frameworks.interfaces.utils.InterfacesRegister;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author gomcarter on 2019-12-02 09:23:09
 */
@RestController
@RequestMapping("privates/interfaces")
@Order
public class PrivatesInterfacesController {
    @GetMapping
    List<ApiInterface> interfaces() throws Exception {
        return InterfacesRegister.register();
    }
}
