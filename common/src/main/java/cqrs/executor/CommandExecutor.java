package cqrs.executor;

import server.core.Connector;

public interface CommandExecutor<C extends Command>
{
    void executor(C c);
}
