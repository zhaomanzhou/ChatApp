package zmz;

import constants.TCPConstants;
import cqrs.executor.ExecutorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.bootstrap.ServerBootstrap;
import server.core.Connector;
import server.core.ServerConfiguration;
import server.core.ServerContext;
import server.handler.connector.input.impl.SimplePrintHandler;
import server.schedule.impl.ConnectorRegistrySelectImp;
import zmz.bootstrap.UDPProvider;
import zmz.context.ConnectionPool;
import zmz.handler.message.HumanCommandExecutorFactory;
import zmz.handler.server.ServerStatusHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Set;


public class MainServer
{
    private static Logger log = LoggerFactory.getLogger(MainServer.class);

    public static void main(String[] args) throws IOException
    {


        ServerConfiguration configuration = new ServerConfiguration.ServerConfigurationBuilder()
                .port(8888)
                .connectorRegistry(new ConnectorRegistrySelectImp())
                .connectorStatusChangedListener(new ServerStatusHandler())
                //.addInputMessageHandler(FixedLengthDecoder.class)
                .addInputMessageHandler(SimplePrintHandler.class)
                .readBufferSize(1024*1024)
                .scheduleThreadSize(20)
                .inStringCharset(Charset.forName("UTF-8"))
                .build();
        ServerContext.init(configuration);
        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap.start();
        log.info("TCP 服务器启动");


        ExecutorContext.commandExecutorFactory = new HumanCommandExecutorFactory();

        UDPProvider.start(TCPConstants.PORT_SERVER);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String str;
        do {
            str = bufferedReader.readLine();
            Set<Connector> set = ConnectionPool.getConnectorSet();
            for(Connector c: set)
            {
                c.write(str);
            }
        } while (!"q".equalsIgnoreCase(str));

        UDPProvider.stop();
        bootstrap.close();
        log.info("TCP 服务器关闭");
    }
}
