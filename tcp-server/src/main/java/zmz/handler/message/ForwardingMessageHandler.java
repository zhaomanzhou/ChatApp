package zmz.handler.message;

import bean.vo.Message;
import com.alibaba.fastjson.JSON;
import cqrs.executor.Command;
import cqrs.executor.CommandBus;
import cqrs.executor.DefaultCommandBus;
import lombok.extern.slf4j.Slf4j;
import server.handler.connector.AbstractMessageHandler;
import zmz.handler.message.cmd.SelfMessageCommand;
import zmz.handler.message.cmd.SimpleMessageCommand;

/**
 * 对消息业务逻辑进行处理
 */
@Slf4j
public class ForwardingMessageHandler extends AbstractMessageHandler
{
    private CommandBus commandBus = DefaultCommandBus.getInstance();

    @Override
    public void onMessageArrived(Object o)
    {
        String msg = (String)o;
        log.info("来自" + connector.getId() + "的新消息: " + msg);
        Message message = JSON.parseObject(msg, Message.class);


        switch (message.getType())
        {
            case SELF:
            {
                Command command = new SelfMessageCommand(getConnector());
                commandBus.send(command);
            }
            case SIMPLE_MESSAGE:
            {
                Command command = new SimpleMessageCommand();
                commandBus.send(command);
            }
        }

    }
}
