package io.github.gomcarter.frameworks.httpapi.auth;

import io.github.gomcarter.frameworks.httpapi.message.request.RequestMessage;

public interface AuthStrategy {
    RequestMessage init(RequestMessage requestMessage);
}
