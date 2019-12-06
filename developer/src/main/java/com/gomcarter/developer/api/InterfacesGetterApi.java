package com.gomcarter.developer.api;

import com.gomcarter.developer.entity.Java;
import com.gomcarter.frameworks.httpapi.api.BaseApi;
import com.gomcarter.frameworks.interfaces.dto.ApiInterface;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gomcarter
 */
@Service
public class InterfacesGetterApi extends BaseApi {

    private Map<String, String> urlRouter = new HashMap<>();

    public List<ApiInterface> get(Java java) {
        String testDomain = java.getTestDomain();
        if (!testDomain.endsWith("/")) {
            testDomain += "/";
        }

        this.urlRouter.put(java.getName(), testDomain + "privates/interfaces");

        return this.getList(java.getName(), ApiInterface.class);
    }

    @Override
    protected Map<String, String> getUrlRouter() {
        return urlRouter;
    }
}
