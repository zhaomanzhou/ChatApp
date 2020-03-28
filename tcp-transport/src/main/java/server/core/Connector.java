package server.core;

import server.handler.connector.HandlerChain;
import server.handler.connector.input.InputHandlerChainFactory;
import server.handler.connector.output.OutputHandlerChainFactory;
import util.ChannelIOUtil;

import java.io.IOException;
import java.nio.channels.SocketChannel;


public class Connector
{
    private SocketChannel channel;

    private HandlerChain inputHandlerChain;
    private HandlerChain outputHandlerChain;
    private String id;
    //TODO
    static int m = 1;




    private Object attach;

    public Connector(SocketChannel channel)
    {
        this.channel = channel;
        this.id =  "" + (Connector.m++);
        try
        {
            inputHandlerChain = InputHandlerChainFactory.handlerChainFromContext(this);
            outputHandlerChain = OutputHandlerChainFactory.handlerChainFromContext(this);
        } catch (IllegalAccessException | InstantiationException e)
        {
            e.printStackTrace();
            throw new RuntimeException("Handler chain 创建失败");
        }

        ServerContext.get().getConfiguration().getConnectorStatusChangedListener().onChannelConnected(this);

    }

    public void close()
    {
        ServerContext.get().getConfiguration().getConnectorStatusChangedListener().onChannelClosed(this);
        ServerContext.get().getConfiguration().getRegistry().unRegisterInput(this);
        try
        {
            channel.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public SocketChannel getSocketChannel()
    {
        return this.channel;
    }


    public void onChannelActive(String msg)
    {
        try
        {
            inputHandlerChain.doChainHandler(msg);
        } catch (Exception e)
        {
            ServerContext.get().getConfiguration().getConnectorStatusChangedListener()
                    .onException(this, e);
        }
    }

    /**
     * 把数据王handler里写
     * @param s
     */
    public void write(String s)
    {
        try
        {
            outputHandlerChain.doChainHandler(s);
        } catch (Exception e)
        {
            //emm......🤔
            ServerContext.get().getConfiguration().getConnectorStatusChangedListener()
                    .onException(this, e);
        }
    }


    /**
     * 真正把数据包送出
     */
    public void sendOut(String s)
    {
        try
        {
            ChannelIOUtil.writeToChannel(s, this.channel);
        } catch (IOException e)
        {
            ServerContext.get().getConfiguration().getConnectorStatusChangedListener()
                    .onException(this, e);
        }
    }


    public String getId()
    {
        return id;
    }

    public Object getAttach()
    {
        return attach;
    }

    public void setAttach(Object attach)
    {
        this.attach = attach;
    }
}
