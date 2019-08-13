package zhaomanzhou.httpserver.po;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document("user")
public class User
{
    @Id
    private String id;
    private String avatarUrl;
    private String name;
    private String password;
    private String moto;
    private LocalDateTime registerDate;
}
