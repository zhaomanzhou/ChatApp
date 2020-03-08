package zmz.handler.message;

import cqrs.executor.CommandExecutor;
import cqrs.executor.CommandExecutorFactory;
import zmz.handler.message.cmd.SelfMessageCommand;
import zmz.handler.message.executor.SelfMessageCommandExecutor;

import java.util.HashMap;
import java.util.Map;

public class HumanCommandExecutorFactory implements CommandExecutorFactory
{
    @Override
    public Map<Class, CommandExecutor> getCommandExecutorMap()
    {
        Map<Class, CommandExecutor> map = new HashMap<>();
        map.put(SelfMessageCommand.class, new SelfMessageCommandExecutor());
        return map;
    }
}
