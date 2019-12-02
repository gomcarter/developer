package com.yiayoframework.httpbaseapi.auth;

import com.yiayoframework.httpbaseapi.message.request.RequestMessage;

public interface AuthStrategy {
    RequestMessage init(RequestMessage requestMessage);
}
