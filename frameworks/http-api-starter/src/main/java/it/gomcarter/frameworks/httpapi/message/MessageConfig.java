package it.gomcarter.frameworks.httpapi.message;

/**
 * @author cn40387 on 15/5/11.
 */
public class MessageConfig {

    private String urlSchema = "http";

    private String host;

    private int port = 80;

    private String apiPath;

    public String getUrlSchema() {
        return urlSchema;
    }

    public void setUrlSchema(String urlSchema) {
        this.urlSchema = urlSchema;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getApiPath() {
        return apiPath;
    }

    public void setApiPath(String apiPath) {
        this.apiPath = apiPath;
    }
}
