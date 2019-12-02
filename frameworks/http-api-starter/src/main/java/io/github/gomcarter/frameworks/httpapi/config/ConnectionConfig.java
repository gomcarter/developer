package io.github.gomcarter.frameworks.httpapi.config;

import org.apache.http.Consts;

/**
 * @author cn40387 on 15/5/8.
 */
public class ConnectionConfig {

    public static final int DEFAULT_CONNECTION_MAX_LINE_LENGTH = 4096;

    public static final int DEFAULT_CONNECTION_MAX_HEADER_COUNT = 1000;

    public static final String DEFAULT_CHARSET = Consts.UTF_8.displayName();

    /**
     * For security
     */
    private int maxLineLength = DEFAULT_CONNECTION_MAX_LINE_LENGTH;

    /**
     * For security
     */
    private int maxHeaderCount = DEFAULT_CONNECTION_MAX_HEADER_COUNT;

    private String connectionCharset = DEFAULT_CHARSET;

    public int getMaxLineLength() {
        return maxLineLength;
    }

    public void setMaxLineLength(int maxLineLength) {
        this.maxLineLength = maxLineLength;
    }

    public int getMaxHeaderCount() {
        return maxHeaderCount;
    }

    public void setMaxHeaderCount(int maxHeaderCount) {
        this.maxHeaderCount = maxHeaderCount;
    }

    public String getConnectionCharset() {
        return connectionCharset;
    }

    public void setConnectionCharset(String connectionCharset) {
        this.connectionCharset = connectionCharset;
    }
}
