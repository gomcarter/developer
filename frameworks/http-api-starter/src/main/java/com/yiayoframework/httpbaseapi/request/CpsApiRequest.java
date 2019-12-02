package com.yiayoframework.httpbaseapi.request;

/**
 * @author 李银 on 2019-11-13 10:27:42
 */
public interface CpsApiRequest {

    CpsApiRequest addHeader(String name, String value);

    CpsApiRequest addParameter(String name, String value);
}
