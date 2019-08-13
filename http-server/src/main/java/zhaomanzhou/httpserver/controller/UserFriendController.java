package zhaomanzhou.httpserver.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import zhaomanzhou.httpserver.po.User;
import zhaomanzhou.httpserver.po.UserFriend;
import zhaomanzhou.httpserver.repository.UserFriendRepository;
import zhaomanzhou.httpserver.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/userfriend")
public class UserFriendController
{

    @Autowired
    private UserFriendRepository userFriendRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/")
    public Mono<UserFriend> insertOne(UserFriend userFriend)
    {
        userFriend.setId(null);
        userFriend.setAddDate(LocalDateTime.now());
        return userFriendRepository.save(userFriend);
    }

    @GetMapping("/{name}")
    public Flux<User> getMyFriend(@PathVariable String name)
    {
        return userFriendRepository.findByMeNameOrFriendName(name, name).
                map(i -> {
                    if(i.getMeName().equals(name))
                    {
                        return i.getFriendName();
                    }else
                    {
                        return i.getMeName();
                    }
                }).distinct()
                .flatMap((userRepository::findByName));

    }





}
