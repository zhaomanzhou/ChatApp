package zmz.context;


import server.core.Connector;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.Consumer;

public class ConnectionPool implements Iterable<Connector>
{
    private static final ConcurrentHashMap<String, Connector> map = new ConcurrentHashMap<>();

    private static final Set<Connector> set = Collections.synchronizedSet(new HashSet<Connector>());

    public static void addAuthenticatedConnector(String id, Connector connector)
    {
        map.put(id, connector);
    }


    public Connector getConnectorByUserId(String id)
    {
        return map.get(id);
    }

    public static void addNewConnection(Connector c)
    {
        set.add(c);
    }

    public static void remove(Connector c)
    {
        set.remove(c);
        map.remove(c);
    }

    public static Map<String, Connector> getConnectorMap()
    {
        return map;
    }

    public static Set<Connector> getConnectorSet()
    {
        return set;
    }

    @Override
    public Iterator<Connector> iterator()
    {
        return set.iterator();
    }

    @Override
    public void forEach(Consumer<? super Connector> action)
    {
        set.forEach(action);
    }

    @Override
    public Spliterator<Connector> spliterator()
    {
        return set.spliterator();
    }
}
