package server.handler.connector.input.impl;

import server.core.ServerContext;
import server.handler.connector.AbstractMessageHandler;

import java.nio.charset.Charset;

public class FixedLengthDecoder extends AbstractMessageHandler
{

    private String headBuffer = "";

    private byte[] contentBuffer;

    private int contentLength;

    private int contentPosition = 0;

    private PacketStatus packetStatus = PacketStatus.HEAD_RECEIVING;

    private Charset charset = ServerContext.get().getConfiguration().getInStringCharset();

    @Override
    public void onMessageArrived(Object o)
    {
        String s = (String)o;



        while (s != null && s.length() > 0)
        {
            switch (packetStatus)
            {
                case HEAD_RECEIVING:
                {
                    //消息还不到一个消息头，先缓存
                    if(s.length() + headBuffer.length() < 20)
                    {
                        headBuffer += s;
                        s = "";
                    }else
                    {
                        //处理消息头
                        int sHeadLength = headBuffer.length();
                        headBuffer += s.substring(0, 20- sHeadLength);
                        s = s.substring(20-sHeadLength);

                        //获取消息长度
                        contentLength =Integer.parseInt(headBuffer.substring(0, 10).trim());
                        contentBuffer = new byte[contentLength];
                        contentPosition = 0;

                        headBuffer = "";
                        packetStatus = PacketStatus.CONTENT_RECEIVING;
                    }
                    break;
                }

                case CONTENT_RECEIVING:
                {
                    //消息不够填满一个消息体
                    if(contentPosition + s.getBytes().length < contentLength)
                    {
                        System.arraycopy(s.getBytes(), 0, contentBuffer,contentPosition,s.getBytes().length);
                        contentPosition += s.getBytes().length;
                        s = "";

                    }else
                    {
                        byte[] bytes = s.getBytes();
                        int byteUsed = contentLength - contentPosition;
                        System.arraycopy(bytes, 0, contentBuffer, contentPosition, byteUsed);
                        String content = new String(contentBuffer, charset);
                        deliveryNextHandler(content);

                        contentPosition = 0;
                        packetStatus = PacketStatus.HEAD_RECEIVING;
                        s = new String(bytes, byteUsed, bytes.length - byteUsed, charset);
                    }
                    break;
                }
            }
        }



    }


}
