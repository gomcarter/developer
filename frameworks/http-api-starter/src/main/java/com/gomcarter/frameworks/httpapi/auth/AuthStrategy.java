package com.gomcarter.frameworks.httpapi.auth;

import com.gomcarter.frameworks.httpapi.message.request.RequestMessage;

/**
 * @author gomcarter
 */
public interface AuthStrategy {

    /**
     * @param requestMessage requestMessage
     * @return RequestMessage
     */
    RequestMessage init(RequestMessage requestMessage);
}
