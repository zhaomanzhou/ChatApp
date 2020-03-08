package server.handler.connector;

import server.core.Connector;

public abstract  class AbstractMessageHandler implements MessageHandler
{
    protected Connector connector;

    public Connector getConnector()
    {
        return connector;
    }

    public void setConnector(Connector connector)
    {
        this.connector = connector;
    }



    @Override
    public void deliveryNextHandler(Object o)
    {

    }
}
