package it.gomcarter.frameworks.httpapi.impl.handler;

import it.gomcarter.frameworks.httpapi.exception.HttpStatusNotOkException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author cn40387 on 15/5/11.
 */
public class DefaultResponseHandler implements ResponseHandler<String> {

    @Override
    public String handleResponse(HttpResponse response) throws IOException {
        StatusLine statusLine = response.getStatusLine();

        if (statusLine.getStatusCode() != HttpStatus.SC_OK) {
            throw new HttpStatusNotOkException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
        }

        HttpEntity entity = response.getEntity();
        if (entity == null) {
            throw new ClientProtocolException("Response no content return.");
        }

        return EntityUtils.toString(entity, Charset.forName("UTF-8"));
    }
}
