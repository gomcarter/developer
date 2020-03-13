package com.gomcarter.developer.controller.publics;

import com.gomcarter.developer.dto.InterfacesDetailDto;
import com.gomcarter.developer.service.InterfacesService;
import com.gomcarter.frameworks.base.common.AssertUtils;
import com.gomcarter.frameworks.base.common.BlowfishUtils;
import com.gomcarter.frameworks.base.common.CustomStringUtils;
import com.gomcarter.frameworks.base.exception.NoPermissionException;
import com.gomcarter.frameworks.interfaces.dto.ApiInterface;
import org.apache.commons.lang3.StringUtils;
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

    @GetMapping(value = "{key}", name = "公共获取接口详情")
    InterfacesDetailDto detail(@PathVariable("key") String key) throws Exception {
        try {
            String[] encryptAndKey = StringUtils.split(key, "|");
            if (encryptAndKey == null || encryptAndKey.length != 2) {
                throw new NoPermissionException();
            }
            String encrypt = encryptAndKey[0], hash = encryptAndKey[1];
            Long id = CustomStringUtils.parseLong(BlowfishUtils.decrypt(encrypt, hash));
            AssertUtils.notNull(id, new NoPermissionException());

            InterfacesDetailDto detail = this.interfacesService.detail(id);
            AssertUtils.notNull(id, new NoPermissionException());

            AssertUtils.isTrue(StringUtils.equals(detail.getHash(), encryptAndKey[1]), new NoPermissionException());
            return detail;
        } catch (Exception e) {
            return null;
        }
    }

}
