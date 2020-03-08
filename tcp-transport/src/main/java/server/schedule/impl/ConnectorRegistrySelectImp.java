package server.schedule.impl;


import server.core.Connector;
import server.core.ServerContext;
import server.schedule.ConnectorRegistry;
import util.ChannelIOUtil;
import util.CloseUtil;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConnectorRegistrySelectImp implements ConnectorRegistry
{


    private  Selector readSelect;

    private final AtomicBoolean isClosed = new AtomicBoolean(true);
    private final AtomicBoolean inRegisterInput = new AtomicBoolean(false);

    private final HashMap<SelectionKey, Connector> keyConnectorMap = new HashMap<>();

    private ExecutorService inputHandlerPool;



    @Override
    public boolean isStarting()
    {
        return !isClosed.get();
    }

    @Override
    public void start()
    {
        if(isClosed.compareAndSet(true, false))
        {
            try
            {
                readSelect = Selector.open();
                int nThreads = ServerContext.get().getConfiguration().getScheduleThreadSize();
                inputHandlerPool = new ThreadPoolExecutor(nThreads, nThreads,
                        0L, TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<Runnable>()
                );
                startRead();
            } catch (IOException e)
            {
                e.printStackTrace();
            }

        }
    }

    private void startRead()
    {
        Thread thread = new Thread("ConnectorRegistry read thread"){
            @Override
            public void run()
            {

                while (!isClosed.get())
                {
                    try
                    {
                        //如果时因为有连接要注册被唤醒，先停止轮询
                        if (readSelect.select() == 0)
                        {
                            synchronized (inRegisterInput)
                            {
                                if (inRegisterInput.get())
                                {
                                    inRegisterInput.wait();
                                }
                            }
                            continue;
                        }
                        Iterator<SelectionKey> it = readSelect.selectedKeys().iterator();
                        while (it.hasNext())
                        {
                            SelectionKey key = it.next();

                            try
                            {
                                SocketChannel channel = (SocketChannel) key.channel();
                                if (key.isValid())
                                {
                                    final Connector connector = keyConnectorMap.get(key);
                                    //取消监听  在connector读完后重新注册
                                    key.interestOps(key.interestOps() & ~SelectionKey.OP_READ);

                                    inputHandlerPool.execute(() ->
                                            {
                                                try
                                                {
                                                    String msg = ChannelIOUtil.readFromChannel(channel);
                                                    key.interestOps(key.readyOps() | SelectionKey.OP_READ);
                                                    connector.onChannelActive(msg);
                                                    key.selector().wakeup();
                                                } catch (Exception e)
                                                {
                                                    connector.close();
                                                }
                                            }
                                    );
                                }

                            } finally
                            {
                                it.remove();
                            }
                        }
                    }
                    catch (InterruptedException | IOException e)
                    {
                        e.printStackTrace();
                    }
                }

            }
        };
        thread.start();
    }

    @Override
    public void registerInput(Connector connector)
    {
        synchronized (inRegisterInput)
        {
            SocketChannel channel = connector.getSocketChannel();
            inRegisterInput.set(true);
            try
            {
                channel.configureBlocking(false);
                readSelect.wakeup();
                SelectionKey key = channel.register(readSelect, SelectionKey.OP_READ);
                keyConnectorMap.put(key, connector);
            } catch (IOException e)
            {
                e.printStackTrace();
            } finally
            {
                inRegisterInput.set(false);
                inRegisterInput.notify();
            }
        }
    }



    @Override
    public void unRegisterInput(Connector connector)
    {
        SocketChannel channel = connector.getSocketChannel();
        if (channel.isRegistered()) {
            SelectionKey key = channel.keyFor(readSelect);
            if (key != null) {
                // 取消监听的方法
                key.cancel();
                keyConnectorMap.remove(key);
                readSelect.wakeup();
            }
        }
    }



    @Override
    public void close()
    {
        if(isClosed.compareAndSet(false, true))
        {
            inputHandlerPool.shutdown();
            readSelect.wakeup();
            keyConnectorMap.clear();
            CloseUtil.close(readSelect);
        }
    }


}
