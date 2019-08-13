package zhaomanzhou.httpserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import zhaomanzhou.httpserver.po.User;
import zhaomanzhou.httpserver.repository.UserRepository;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/user")
public class UserController
{

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/")
    public Flux<User> getAll()
    {
        return userRepository.findAll();
    }


    //spring data jpa 里修改和删除都是save方法， 有id为修改， id为空为新增
    @PostMapping("/")
    public Mono<User> createUser(User user)
    {

        user.setId(null);
        user.setRegisterDate(LocalDateTime.now());
        //userRepository.findByName(user.getName())
        return userRepository.save(user);
    }

    @GetMapping("/{name}")
    public Mono<User> getUser(@PathVariable String name)
    {
        return userRepository.findByName(name).last();
    }



    @GetMapping("/test")
    public Mono<String> test()
    {
        return Mono.fromSupplier(()-> "ddd");
    }
}
