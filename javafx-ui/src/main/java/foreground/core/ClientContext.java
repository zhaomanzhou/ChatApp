package foreground.core;

import bean.po.User;
import foreground.net.TCPClient;
import foreground.ui.MainStage;

public class ClientContext
{
    private static MainStage mainStage;
    private static User me;
    private static TCPClient tcpClient;

    public static TCPClient getTcpClient()
    {
        return tcpClient;
    }

    public static void setTcpClient(TCPClient tcpClient)
    {
        ClientContext.tcpClient = tcpClient;
    }

    public static User getMe()
    {
        return me;
    }

    public static void setMe(User me)
    {
        ClientContext.me = me;
    }
}
