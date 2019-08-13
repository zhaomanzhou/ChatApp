package bean.vo;

import javafx.beans.property.SimpleStringProperty;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Data
public class UserTabVo
{
    private String userId;
    private String avatarUrl;
    private String name;
    private SimpleStringProperty latestMessage;
    private LocalDateTime lastestSendDate;
    private SimpleStringProperty messageNum;
    private SimpleStringProperty messageDelimeter = new SimpleStringProperty();

    public SimpleStringProperty getMessageDelimeter()
    {
        long l = Duration.between(LocalDateTime.now(), lastestSendDate).getSeconds() / 60;
        messageDelimeter.set(1 + "");
        return messageDelimeter;
    }
}
