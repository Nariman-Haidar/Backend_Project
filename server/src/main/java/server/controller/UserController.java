package server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import server.dto.UserDTO;
import server.security.JwtUtil;
import server.service.UserService;
import server.service.impl.AlreadyExistsException;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private final PasswordEncoder encoder;
    private final UserService userService;
    private final Sinks.Many<UserDTO> userEventSink;
    private final JwtUtil jwtUtil;
    private final ReactiveUserDetailsService customUserDetailsService;
    int count = 0;
    public UserController(PasswordEncoder encoder, UserService userService,
                          Sinks.Many<UserDTO> userEventSink,
                          JwtUtil jwtUtil,
                          ReactiveUserDetailsService customUserDetailsService) {
        this.encoder = encoder;
        this.userService = userService;
        this.userEventSink = userEventSink;
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
    }

    @GetMapping(value = "/users", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<UserDTO> getAllUsers() {
     
        Sinks.Many<UserDTO> localSink = Sinks.many().unicast().onBackpressureBuffer();

        userEventSink.asFlux().subscribe(localSink::tryEmitNext);
        userService.getAllUsers().subscribe(localSink::tryEmitNext);

        return localSink.asFlux();
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> addUser(@RequestBody UserDTO userDTO) {
        return userService.addUser(userDTO)
                .doOnSuccess(userEventSink::tryEmitNext)
                .doOnSuccess(user -> System.out.println("User added: " + user))
                .then();
    }


    @DeleteMapping("/{userId}")
    public Mono<Void> deleteUser(@PathVariable Long userId) {
        return userService.deleteUser(userId)
                .doOnSuccess(deletedUserId ->
                        userEventSink.tryEmitNext(new UserDTO(deletedUserId,"delete")))
                .then();
    }

    @GetMapping("/auth")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserDTO> register(Authentication authentication) {
        System.out.println(authentication);
         return userService.findByUsername(authentication.getName());
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserDTO> login(@RequestBody UserDTO userDTO) {
        return customUserDetailsService.findByUsername(userDTO.getUsername())
                .flatMap(u -> {
                    if (u == null) {
                        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "No user found"));
                    }

                    if (encoder.matches(userDTO.getPassword(), u.getPassword())) {
                        return handleSuccessLogin(userDTO);
                    }

                    return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Credentials"));
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "No user found")));
    }

    private Mono<UserDTO> handleSuccessLogin(UserDTO userDTO) {
        String token = jwtUtil.generateToken(userDTO.getUsername());
        return userService.findByUsername(userDTO.getUsername())
                .map(user -> {
                    user.setToken(token);
                    user.setEventType("Success");
                    return user; 
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "User ID not found")));
    }









    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void>  registerUser(@RequestBody UserDTO userDTO) {
        return userService.registerUser(userDTO)
                .doOnSuccess(userEventSink::tryEmitNext)
                .onErrorResume(AlreadyExistsException.class, ex -> {
                    return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Item already exists", ex));
                })
                .then();
    }
}


