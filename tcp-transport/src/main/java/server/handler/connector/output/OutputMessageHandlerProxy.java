package server.handler.connector.output;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import server.handler.connector.MessageHandler;

import java.lang.reflect.Method;

public class OutputMessageHandlerProxy implements MethodInterceptor
{

    private MessageHandler nexthandler;

    public OutputMessageHandlerProxy(MessageHandler nexthandler)
    {
        this.nexthandler = nexthandler;
    }

    public <T> T getProxy(Class<T> cls){
        return (T) Enhancer.create(cls, this);
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] arg,
                            MethodProxy proxy) throws Throwable {
        if(method.getName().equals("deliveryNextHandler"))
            nexthandler.onMessageArrived(arg[0]);
        Object result = proxy.invokeSuper(obj, arg);
        return result;
    }

}
