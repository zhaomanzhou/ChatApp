package zhaomanzhou.httpserver.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import zhaomanzhou.httpserver.po.User;
import zhaomanzhou.httpserver.repository.UserRepository;

@RestController
@RequestMapping("/login")
public class LoginController
{

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/")
    public Mono<Boolean> login(User user)
    {

        return userRepository.findByName(user.getName())
                .filter(i->i.getPassword().equals(user.getPassword()))
                .flatMap(i->Mono.just(true))
                .single()
                .defaultIfEmpty(false)
                .onErrorReturn(false);




    }
}
