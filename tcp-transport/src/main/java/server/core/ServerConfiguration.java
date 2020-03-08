package server.core;

import lombok.Getter;
import server.handler.connector.MessageHandler;
import server.handler.global.ConnectorStatusChangedListener;
import server.schedule.ConnectorRegistry;

import java.nio.charset.Charset;
import java.util.LinkedList;

@Getter
public class ServerConfiguration
{

    private int port;

    private ConnectorRegistry registry;

    private ConnectorStatusChangedListener connectorStatusChangedListener;

    private LinkedList<Class<? extends MessageHandler>> inputMessageHandlers;

    private LinkedList<Class<? extends MessageHandler>> outputMessageHandlers;

    private int scheduleThreadSize = 6;

    /**
     * 读缓冲区大小
     */
    private int readBufferSize = 1024*1024;


    /**
     * 写出去的字符串编码
     */
    private Charset outStringEncoding = Charset.defaultCharset();

    /**
     * 默认读取的字符串
     */
    private Charset inStringCharset = Charset.defaultCharset();


    public static class ServerConfigurationBuilder
    {
        private ServerConfiguration configuration = new ServerConfiguration();
        private LinkedList<Class<? extends MessageHandler>> inputMessageHandlers = new LinkedList<Class<? extends MessageHandler>>();
        private LinkedList<Class<? extends MessageHandler>> outputMessageHandlers = new LinkedList<Class<? extends MessageHandler>>();


        public ServerConfigurationBuilder port(int port)
        {
            configuration.port = port;
            return this;
        }

        public ServerConfigurationBuilder connectorRegistry(ConnectorRegistry registry)
        {
            configuration.registry = registry;
            return this;
        }

        public ServerConfigurationBuilder connectorStatusChangedListener(ConnectorStatusChangedListener listener)
        {
            configuration.connectorStatusChangedListener = listener;
            return this;
        }


        public ServerConfigurationBuilder addInputMessageHandler(Class<? extends MessageHandler> handlerClass)
        {
            inputMessageHandlers.addLast(handlerClass);
            return this;
        }

        public ServerConfigurationBuilder addOutMessageHandler(Class<? extends MessageHandler> handlerClass)
        {
            outputMessageHandlers.addLast(handlerClass);
            return this;
        }

        public ServerConfigurationBuilder readBufferSize(int size)
        {
            configuration.readBufferSize = size;
            return this;
        }

        public ServerConfigurationBuilder outStringEncoding(Charset charset)
        {
            configuration.outStringEncoding = charset;
            return this;
        }

        public ServerConfigurationBuilder inStringCharset(Charset charset)
        {
            configuration.inStringCharset = charset;
            return this;
        }

        public ServerConfigurationBuilder scheduleThreadSize(int size)
        {
            configuration.scheduleThreadSize = size;
            return this;
        }

        public ServerConfiguration build()
        {
            this.configuration.inputMessageHandlers = inputMessageHandlers;
            this.configuration.outputMessageHandlers = outputMessageHandlers;
            return configuration;
        }

    }



}
