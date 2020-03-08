package cqrs.executor;

import java.util.HashMap;
import java.util.Map;

public class DefaultCommandBus implements CommandBus
{


    private Map<Class, CommandExecutor> commandExecutorMap = new HashMap<>();

    private DefaultCommandBus()
    {
        CommandExecutorFactory commandExecutorFactory = ExecutorContext.commandExecutorFactory;
        commandExecutorMap = commandExecutorFactory.getCommandExecutorMap();
    }

    @Override
    public void send(Command command)
    {
        commandExecutorMap.get(command.getClass()).executor(command);
    }


    public static CommandBus getInstance()
    {
        return CommandBusHolder.INSTANCE;
    }

    private static class CommandBusHolder
    {
        public static CommandBus INSTANCE = new DefaultCommandBus();
    }
}
