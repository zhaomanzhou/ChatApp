package cqrs.executor;

import server.core.Connector;

public abstract class Command
{
    private Connector connector;

    public Command()
    {
    }

    public Command(Connector connector)
    {
        this.connector = connector;
    }

    public Connector getConnector()
    {
        return connector;
    }

    public void setConnector(Connector connector)
    {
        this.connector = connector;
    }
}
