package server.handler.connector;

import server.core.Connector;

public interface MessageHandler
{
    /**
     * 消息到达处理
     * @param o  消息类型
     */
    void onMessageArrived(Object o);

    /**
     * 把消息交给下层处理
     * @param o
     */
    void deliveryNextHandler(Object o);

    /**
     * 获取当前处理的Connector
     * @return
     */
    Connector getConnector();


    /**
     * 设置当前Connector
     * @param connector
     */
    void setConnector(Connector connector);


}
