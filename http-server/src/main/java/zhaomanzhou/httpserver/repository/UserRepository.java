package zhaomanzhou.httpserver.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import zhaomanzhou.httpserver.po.User;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String>
{
    Flux<User> findByName(String name);
}
