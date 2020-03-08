package server.handler.global.imp;

import server.core.Connector;
import server.handler.global.ConnectorStatusChangedListener;

public class SimpleConnectorStatusChangedListener implements ConnectorStatusChangedListener
{
    @Override
    public void onChannelConnected(Connector c)
    {
        System.out.println("新channel 连接 " + c.getId());
    }

    @Override
    public void onChannelClosed(Connector c)
    {
        System.out.println("channel 关闭 " + c.getId());
    }

    @Override
    public void onException(Connector c, Exception e)
    {

    }
}
