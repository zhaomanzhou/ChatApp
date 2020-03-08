package server.handler.connector.output.impl;

import server.handler.connector.AbstractMessageHandler;

public class LastOutputHandler extends AbstractMessageHandler
{
    @Override
    public void onMessageArrived(Object o)
    {
        if(!(o instanceof String))
            throw new RuntimeException("用户设置的最后一个OutHandler输出类型不是String");
        getConnector().sendOut((String) o);
    }
}
