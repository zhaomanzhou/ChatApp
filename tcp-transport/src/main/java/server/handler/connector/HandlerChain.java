package server.handler.connector;


import server.core.Connector;

import java.util.LinkedList;

public class HandlerChain
{
    private LinkedList<MessageHandler> handlers= new LinkedList<>();
    private Connector connector;


    public HandlerChain(Connector connector)
    {
        this.connector = connector;
    }

    public HandlerChain addHandler(MessageHandler handler){
        handler.setConnector(connector);
        handlers.add(handler);
        return this;
    }


    public HandlerChain addHandlerFromTail(MessageHandler handler){
        handler.setConnector(connector);
        handlers.addFirst(handler);
        return this;
    }

    public boolean doChainHandler(String msg) {
        if(handlers.size() <= 0)
            return false;
        handlers.get(0).onMessageArrived(msg);
        return true;
    }


    public Connector getConnector()
    {
        return connector;
    }


}
