package it.gomcarter.frameworks.httpapi.auth;

import it.gomcarter.frameworks.httpapi.message.request.RequestMessage;

public interface AuthStrategy {
    RequestMessage init(RequestMessage requestMessage);
}
