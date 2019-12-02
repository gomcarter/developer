package it.gomcarter.frameworks.httpapi.impl;

import org.apache.http.conn.HttpClientConnectionManager;

import java.util.concurrent.TimeUnit;

/**
 * @author cn40387 on 15/5/8.
 */
public class IdleConnectionMonitorTask implements Runnable {

    private final HttpClientConnectionManager connectionManager;

    private final int idleDurationToClose;

    public IdleConnectionMonitorTask(HttpClientConnectionManager connectionManager, int idleDurationToClose) {
        this.connectionManager = connectionManager;
        this.idleDurationToClose = idleDurationToClose;
    }

    @Override
    public void run() {
        connectionManager.closeExpiredConnections();

        connectionManager.closeIdleConnections(idleDurationToClose, TimeUnit.MILLISECONDS);
    }

    public HttpClientConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public int getIdleDurationToClose() {
        return idleDurationToClose;
    }
}
