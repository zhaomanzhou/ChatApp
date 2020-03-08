package cqrs.executor;


import java.util.Map;

public interface CommandExecutorFactory
{




    public Map<Class, CommandExecutor> getCommandExecutorMap();
}
