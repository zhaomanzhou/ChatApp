package zmz.handler.message.executor;

import cqrs.executor.CommandExecutor;
import zmz.handler.message.cmd.SimpleMessageCommand;

public class SimpleMessageCommandExecutor implements CommandExecutor<SimpleMessageCommand>
{
    @Override
    public void executor(SimpleMessageCommand simpleMessageCommand)
    {

    }
}
