package zmz.handler.message.cmd;

import cqrs.executor.Command;
import server.core.Connector;

public class SelfMessageCommand extends Command
{
    public SelfMessageCommand(Connector connector)
    {
        super(connector);
    }
}
