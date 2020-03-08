package server.handler.connector.output.impl;

import server.handler.connector.AbstractMessageHandler;
import util.FixedLengthEncoderUtil;

public class FixedLengthEncoder extends AbstractMessageHandler
{


    @Override
    public void onMessageArrived(Object o)
    {
        String s = (String)o;
        String encodedString = FixedLengthEncoderUtil.encoder(s);
        deliveryNextHandler(encodedString);
    }
}
