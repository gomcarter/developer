package com.yiayo.developer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统监控专用
 *
 * @author 李银 on 2018年3月30日 09:29:51
 */
@RestController
public class Monitor {

    @GetMapping("")
    String monitor() {
        return "I'm developer and health!";
    }
}
