package server.bootstrap;

import server.core.Connector;
import server.core.ServerContext;
import util.CloseUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServerBootstrap extends Thread
{

    public ServerBootstrap()
    {
        super("Server select thread");
    }

    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private AtomicBoolean runningStatus = new AtomicBoolean(false);


    @Override
    public void run()
    {
        if(ServerContext.get() == null)
            throw new RuntimeException("The ServerContex has not been initialized!");
        try{
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(ServerContext.get().getConfiguration().getPort()));
            serverSocketChannel.configureBlocking(false);
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            runningStatus.set(true);
            ServerContext.get().getConfiguration().getRegistry().start();
            listenConnect();
        }catch (IOException e)
        {
            e.printStackTrace();
        }


    }


    private void listenConnect()
    {
        while (runningStatus.get())
        {
            try{
                if (selector.select() > 0)
                {
                    Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                    while (it.hasNext())
                    {
                        SelectionKey key = it.next();
                        if(key.isAcceptable())
                        {
                            ServerSocketChannel channel = ((ServerSocketChannel) key.channel());
                            SocketChannel accept = channel.accept();
                            Connector connector = new Connector(accept);
                            ServerContext.get().getConfiguration().getRegistry().registerInput(connector);
                        }
                        it.remove();
                    }
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }




    }


    public void close()
    {
        runningStatus.compareAndSet(true, false);
        ServerContext.get().getConfiguration().getRegistry().close();
        selector.wakeup();
        CloseUtil.close(serverSocketChannel);
    }


}
