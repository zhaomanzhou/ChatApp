package server.core;



public class ServerContext
{

    private  ServerConfiguration configuration;
    private static ServerContext INSTANCE;

    private ServerContext(ServerConfiguration configuration)
    {
        this.configuration = configuration;
    }

    public ServerConfiguration getConfiguration()
    {
        return configuration;
    }

    public static void init(ServerConfiguration configuration)
    {
        if(INSTANCE != null)
            throw new RuntimeException("Context已经初始化过了");
        synchronized (ServerContext.class)
        {
            if(INSTANCE != null)
                throw new RuntimeException("Context已经初始化过了");
            INSTANCE = new ServerContext(configuration);
        }
    }

    public static ServerContext get()
    {
        if(INSTANCE == null)
            throw new RuntimeException("Context还未初始化");
        return INSTANCE;
    }


    public static boolean isInitialized()
    {
        return INSTANCE != null;
    }

}
