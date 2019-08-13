package bean.po;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User
{
    private String id;
    private String avatarUrl;
    private String name;
    private String password;
    private String moto;
    private LocalDateTime registerDate;
    private Boolean legal;

}
