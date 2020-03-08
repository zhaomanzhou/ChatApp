package cqrs.executor;

public interface CommandBus
{
    void send(Command command);
}
