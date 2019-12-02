package com.yiayoframework.httpbaseapi.auth;

import com.yiayoframework.httpbaseapi.message.request.RequestMessage;
import com.yiayoframework.httpbaseapi.utils.ApiTokenUtils;

public class CpsApiAuthStrategy implements AuthStrategy {

    ///private static String processName = null;

    @Override
    public RequestMessage init(RequestMessage requestMessage) {
        //放入需要验证的token
        requestMessage.addHeader(ApiTokenUtils.TOKEN_NAME, ApiTokenUtils.getToken());

        ///if (processName == null) {
        ///	processName = System.getProperty("processName");
        ///}

        ///if (StringUtils.isNotBlank(processName)) {
        ///	requestMessage.addHeader("tgVersion", processName);
        ///}

        return requestMessage;
    }
}
