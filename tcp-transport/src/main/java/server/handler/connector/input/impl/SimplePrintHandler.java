package server.handler.connector.input.impl;


import server.handler.connector.AbstractMessageHandler;

public class SimplePrintHandler extends AbstractMessageHandler
{

    public void onMessageArrived(Object o)
    {
        System.out.println(o);
    }
}
