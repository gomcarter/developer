package io.github.gomcarter.frameworks.httpapi.request;

/**
 * @author gomcarter on 2019-11-13 10:27:42
 */
public interface ApiRequest {

    ApiRequest addHeader(String name, String value);

    ApiRequest addParameter(String name, String value);
}
