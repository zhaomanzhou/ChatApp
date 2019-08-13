package foreground.net;



import bean.ServerInfo;
import bean.po.User;
import bean.vo.Message;
import bean.vo.MessageType;
import com.alibaba.fastjson.JSON;
import foreground.core.ClientContext;
import util.CloseUtils;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.List;

public class TCPClient
{
    private final Socket socket;
    private final ReadHandler readHandler;
    private final PrintStream printStream;
    private static OnMessageArrivedCallback callback;

    public static OnMessageArrivedCallback getCallback()
    {
        return callback;
    }

    public static void setCallback(OnMessageArrivedCallback callback)
    {
        TCPClient.callback = callback;
    }

    public TCPClient(Socket socket, ReadHandler readHandler) throws IOException {
        this.socket = socket;
        this.readHandler = readHandler;
        this.printStream = new PrintStream(socket.getOutputStream());
    }

    public void exit() {
        readHandler.exit();
        CloseUtils.close(printStream);
        CloseUtils.close(socket);
    }

    public void send(String msg) {
        printStream.println(msg);
    }

    public static TCPClient startWith(ServerInfo info) throws IOException {
        Socket socket = new Socket();
        // 超时时间
        socket.setSoTimeout(3000);

        // 连接本地，端口2000；超时时间3000ms
        socket.connect(new InetSocketAddress(Inet4Address.getByName(info.getAddress()), info.getPort()), 3000);

        System.out.println("已发起服务器连接，并进入后续流程～");
        System.out.println("客户端信息：" + socket.getLocalAddress() + " P:" + socket.getLocalPort());
        System.out.println("服务器信息：" + socket.getInetAddress() + " P:" + socket.getPort());

        try {
            ReadHandler readHandler = new ReadHandler(socket.getInputStream());
            readHandler.start();
            TCPClient tcpClient = new TCPClient(socket, readHandler);
            Message message = new Message();
            message.setFromId(ClientContext.getMe().getId());
            message.setType(MessageType.SELF);
            message.setMessage(JSON.toJSONString(ClientContext.getMe()));
            tcpClient.send(JSON.toJSONString(message));
            //向服务器发送自身信息
            return tcpClient;
        } catch (Exception e) {
            System.out.println("连接异常");
            CloseUtils.close(socket);
        }

        return null;
    }


     static class ReadHandler extends Thread {
        private boolean done = false;
        private final InputStream inputStream;

        ReadHandler(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public void run() {
            super.run();
            try {
                // 得到输入流，用于接收数据
                BufferedReader socketInput = new BufferedReader(new InputStreamReader(inputStream));

                do {
                    String str;
                    try {
                        // 客户端拿到一条数据
                        str = socketInput.readLine();
                    } catch (SocketTimeoutException e) {
                        continue;
                    }
                    if (str == null) {
                        System.out.println("连接已关闭，无法读取数据！");
                        break;
                    }
                    // 打印到屏幕
                    System.out.println("服务器返回的信息：" + str);
                    handleMessage(str);
                } while (!done);
            } catch (Exception e) {
                if (!done) {
                    System.out.println("连接异常断开：" + e.getMessage());
                    //TODO
                    e.printStackTrace();
                }
            } finally {
                // 连接关闭
                CloseUtils.close(inputStream);
            }
        }


        //处理服务器
        void handleMessage(String msg)
        {
            Message message = JSON.parseObject(msg, Message.class);
            switch (message.getType())
            {
                case REPLY_ONLINE_USER: handleReplyOnLineUser(message.getMessage()); return;
                case SIMPLE_MESSAGE:TCPClient.callback.onNewMessageArrived(message);return;
            }
        }

        /**
         * 处理服务器返回的在线用户
         * @param content 在线用户数组的json字符串
         */
        void handleReplyOnLineUser(String content)
        {
            List<User> users = JSON.parseArray(content, User.class);

            System.out.println("在线用户：" + users);

            TCPClient.callback.onOnlineUserArrived(users);
        }






        void exit() {
            done = true;
            CloseUtils.close(inputStream);
        }
    }


    public interface OnMessageArrivedCallback
    {
        void onOnlineUserArrived(List<User> users);

        void onNewMessageArrived(Message message);
    }
}
