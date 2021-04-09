package com.gomcarter.developer.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;

public class DefaultResponseHandler implements ResponseHandler<String> {

    @Override
    public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
        StatusLine statusLine = response.getStatusLine();

        if (statusLine.getStatusCode() != HttpStatus.SC_OK) {
            throw new RuntimeException("statusCode=" + statusLine.getStatusCode() + ",reason:" + statusLine.getReasonPhrase());
        }

        HttpEntity entity = response.getEntity();
        if (entity == null) {
            throw new ClientProtocolException("Response no content return.");
        }

        return EntityUtils.toString(entity, Charset.forName("UTF-8"));
    }
}

