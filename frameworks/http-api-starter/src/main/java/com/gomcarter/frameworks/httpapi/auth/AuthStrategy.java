package com.gomcarter.frameworks.httpapi.auth;

import com.gomcarter.frameworks.httpapi.message.request.RequestMessage;

public interface AuthStrategy {
    RequestMessage init(RequestMessage requestMessage);
}
