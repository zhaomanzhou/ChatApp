package zmz.context;


import cqrs.executor.CommandBus;

public class ExecutorAnnotationProcessor
{
    private CommandBus commandBus;

    private ExecutorAnnotationProcessor()
    {
        System.getProperty("user.dir");
    }

    public CommandBus getCommandBus()
    {


        return commandBus;
    }




    private static class CommandBusHolder
    {
        public static final ExecutorAnnotationProcessor INSTANCE = new ExecutorAnnotationProcessor();
    }


}
