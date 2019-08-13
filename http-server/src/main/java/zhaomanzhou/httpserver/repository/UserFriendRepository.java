package zhaomanzhou.httpserver.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import zhaomanzhou.httpserver.po.UserFriend;

public interface UserFriendRepository extends ReactiveMongoRepository<UserFriend, String>
{
    Flux<UserFriend> findByMeNameOrFriendName(String name1, String name2);
}
