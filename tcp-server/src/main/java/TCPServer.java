import bean.po.User;
import bean.vo.Message;
import bean.vo.MessageType;
import com.alibaba.fastjson.JSON;
import handler.ClientHandler;
import util.CloseUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class TCPServer implements ClientHandler.ClientHandlerCallback {
    private final int port;
    private ClientListener listener;
    private List<ClientHandler> clientHandlerList = new ArrayList<>();
    private final ExecutorService forwardingThreadPoolExecutor;
    private Selector selector;
    private ServerSocketChannel server;

    public TCPServer(int port) {
        this.port = port;
        // 转发线程池
        this.forwardingThreadPoolExecutor = Executors.newSingleThreadExecutor();
    }

    public boolean start() {
        try {
            selector = Selector.open();
            ServerSocketChannel server = ServerSocketChannel.open();
            // 设置为非阻塞
            server.configureBlocking(false);
            // 绑定本地端口
            server.socket().bind(new InetSocketAddress(port));
            // 注册客户端连接到达监听
            server.register(selector, SelectionKey.OP_ACCEPT);

            this.server = server;


            System.out.println("服务器信息：" + server.getLocalAddress().toString());

            // 启动客户端监听
            ClientListener listener = this.listener = new ClientListener();
            listener.start();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void stop() {
        if (listener != null) {
            listener.exit();
        }

        CloseUtils.close(server);
        CloseUtils.close(selector);

        synchronized (TCPServer.this) {
            for (ClientHandler clientHandler : clientHandlerList) {
                clientHandler.exit();
            }

            clientHandlerList.clear();
        }

        // 停止线程池
        forwardingThreadPoolExecutor.shutdownNow();
    }

    public synchronized void broadcast(String str) {
        for (ClientHandler clientHandler : clientHandlerList) {
            clientHandler.send(str);
        }
    }

    @Override
    public synchronized void onSelfClosed(ClientHandler handler) {
        clientHandlerList.remove(handler);
    }

    @Override
    public void onNewMessageArrived(final ClientHandler handler, final String msg) {





        // 异步提交转发任务
        forwardingThreadPoolExecutor.execute(() -> {
            synchronized (TCPServer.this) {

                Message message = JSON.parseObject(msg, Message.class);


                switch (message.getType())
                {
                    case SELF:
                    {
                        handleSelfMessage(handler, message);
                        return;
                    }
                    case SIMPLE_MESSAGE:
                    {
                        handleSimpleMessage(handler, message);
                        return;
                    }
                }


//                for (ClientHandler clientHandler : clientHandlerList) {
//                    if (clientHandler.equals(handler)) {
//                        // 跳过自己
//                        continue;
//                    }
//                    // 对其他客户端发送消息
//                    clientHandler.send(msg);
//                }
            }
        });
    }

    private void handleSimpleMessage(ClientHandler handler, Message message)
    {
        for(ClientHandler ch: clientHandlerList)
        {
            if(ch.getClientId().equals(message.getToId()))
            {
                //回送消息

                ch.send(JSON.toJSONString(message));
            }
        }
    }


    private void handleSelfMessage(final ClientHandler handler, final Message message)
    {
        handler.setClientId(message.getFromId());
        handler.setUser(JSON.parseObject(message.getMessage(), User.class));
        List<User> collect = clientHandlerList.stream()
                .filter(i -> !i.getClientId().equals(message.getFromId()))
                .map(ClientHandler::getUser)
                .collect(Collectors.toList());

        String s = JSON.toJSONString(collect);
        Message m = new Message();
        m.setMessage(s);
        m.setType(MessageType.REPLY_ONLINE_USER);
        handler.send(JSON.toJSONString(m));


        collect.clear();
        collect.add(handler.getUser());

        //通知其他客户端有新用户上线
        s = JSON.toJSONString(collect);
        m.setMessage(s);
        for (ClientHandler clientHandler : clientHandlerList) {
            if (clientHandler.equals(handler)) {
                // 跳过自己
                continue;
            }
            // 对其他客户端发送消息
            clientHandler.send(JSON.toJSONString(m));
        }

    }



























    private class ClientListener extends Thread {
        private boolean done = false;

        @Override
        public void run() {
            super.run();
            Selector selector = TCPServer.this.selector;
            System.out.println("服务器准备就绪～");
            // 等待客户端连接
            do {
                // 得到客户端
                try {
                    if (selector.select() == 0) {
                        if (done) {
                            break;
                        }
                        continue;
                    }

                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        if (done) {
                            break;
                        }

                        SelectionKey key = iterator.next();
                        iterator.remove();

                        // 检查当前Key的状态是否是我们关注的
                        // 客户端到达状态
                        if (key.isAcceptable()) {
                            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                            // 非阻塞状态拿到客户端连接
                            SocketChannel socketChannel = serverSocketChannel.accept();

                            try {
                                // 客户端构建异步线程
                                ClientHandler clientHandler = new ClientHandler(socketChannel, TCPServer.this);
                                // 添加同步处理
                                synchronized (TCPServer.this) {
                                    clientHandlerList.add(clientHandler);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                System.out.println("客户端连接异常：" + e.getMessage());
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } while (!done);

            System.out.println("服务器已关闭！");
        }

        void exit() {
            done = true;
            // 唤醒当前的阻塞
            selector.wakeup();
        }
    }
}
