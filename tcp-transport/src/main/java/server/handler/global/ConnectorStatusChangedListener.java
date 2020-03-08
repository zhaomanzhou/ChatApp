package server.handler.global;

import server.core.Connector;

/**
 * 全局一个
 */
public interface ConnectorStatusChangedListener
{
    void onChannelConnected(Connector c);

    void onChannelClosed(Connector c);

    void onException(Connector c, Exception e);
}
