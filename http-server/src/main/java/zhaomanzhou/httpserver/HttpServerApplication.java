package zhaomanzhou.httpserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories
public class HttpServerApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(HttpServerApplication.class, args);
    }

}
