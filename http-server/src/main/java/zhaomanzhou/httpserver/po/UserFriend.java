package zhaomanzhou.httpserver.po;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document("friend")
public class UserFriend
{
    @Id
    private String id;
    private String meName;
    private String friendName;
    private LocalDateTime addDate;
}
