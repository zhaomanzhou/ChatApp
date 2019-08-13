package bean.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 客户端与服务器发送消息的bean
 */
@Data
public class Message implements Comparable<Message>
{
    private String fromId;
    private String toId;
    private String message;
    private LocalDateTime date;
    private MessageType type;
    private Integer fileNum;

    @Override
    public int compareTo(Message o)
    {
        return date.compareTo(o.getDate());
    }
}
