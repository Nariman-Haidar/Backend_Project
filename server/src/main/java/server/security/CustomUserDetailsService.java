package server.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import server.mapper.UserMapper;
import server.service.UserService;


@Service
public class CustomUserDetailsService implements ReactiveUserDetailsService{

    private final UserService userService;
    private final UserMapper userMapper;
    @Autowired
    public CustomUserDetailsService(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userService.getByUsername(username);
    }

}
