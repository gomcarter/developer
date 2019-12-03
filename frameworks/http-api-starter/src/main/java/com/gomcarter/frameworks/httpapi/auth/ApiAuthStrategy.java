package com.gomcarter.frameworks.httpapi.auth;

import com.gomcarter.frameworks.httpapi.message.request.RequestMessage;
import com.gomcarter.frameworks.httpapi.utils.ApiTokenUtils;

/**
 * @author gomcarter
 */
public class ApiAuthStrategy implements AuthStrategy {

    @Override
    public RequestMessage init(RequestMessage requestMessage) {
        //放入需要验证的token
        requestMessage.addHeader(ApiTokenUtils.TOKEN_NAME, ApiTokenUtils.getToken());

        return requestMessage;
    }
}
