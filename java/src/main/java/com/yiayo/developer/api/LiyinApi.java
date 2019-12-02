package com.yiayo.developer.api;

import com.yiayo.developer.entity.Java;
import com.yiayoframework.httpbaseapi.api.BaseApi;
import com.yiayoframework.liyinapi.dto.ApiInterface;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LiyinApi extends BaseApi {

    private Map<String, String> urlRouter = new HashMap<>();

    public List<ApiInterface> get(Java java) {
        String testDomain = java.getTestDomain();
        if (!testDomain.endsWith("/")) {
            testDomain += "/";
        }

        this.urlRouter.put(java.getName(), testDomain + "privates/interfaces");
//        appendUrlRequestRouter(java.getName(), testDomain + "interfaces");

        return this.getList(java.getName(), ApiInterface.class);
    }

    @Override
    protected Map<String, String> getUrlRouter() {
        return urlRouter;
    }
}
