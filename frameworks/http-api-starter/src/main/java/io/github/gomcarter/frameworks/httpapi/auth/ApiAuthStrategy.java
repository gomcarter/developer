package io.github.gomcarter.frameworks.httpapi.auth;

import io.github.gomcarter.frameworks.httpapi.message.request.RequestMessage;
import io.github.gomcarter.frameworks.httpapi.utils.ApiTokenUtils;

public class ApiAuthStrategy implements AuthStrategy {

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
