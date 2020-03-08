package zmz.handler.server;

import lombok.extern.slf4j.Slf4j;
import server.core.Connector;
import server.handler.global.ConnectorStatusChangedListener;
import zmz.context.ConnectionPool;

@Slf4j
public class ServerStatusHandler implements ConnectorStatusChangedListener
{
    @Override
    public void onChannelConnected(Connector c)
    {
        log.info("新连接：" + c.getId());
        ConnectionPool.addNewConnection(c);
    }

    @Override
    public void onChannelClosed(Connector c)
    {
        log.info("连接关闭：" + c.getId());
        ConnectionPool.remove(c);
    }

    @Override
    public void onException(Connector c, Exception e)
    {
        e.printStackTrace();
    }
}
