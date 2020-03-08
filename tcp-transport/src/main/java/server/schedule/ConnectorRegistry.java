package server.schedule;

import server.core.Connector;

public interface ConnectorRegistry
{
    public void start();
    public boolean isStarting();

    public void registerInput(Connector connector);
    public void unRegisterInput(Connector connector);

    public void close();


}
