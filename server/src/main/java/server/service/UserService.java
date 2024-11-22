package server.service;

import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import server.dto.UserDTO;
import server.model.User;

public interface UserService {
    Mono<UserDTO> findByUsername(String username);
    Mono<UserDetails> getByUsername(String username);
    Flux<UserDTO> getAllUsers();

    Mono<UserDTO> addUser(UserDTO userDTO);

    Mono<Long>deleteUser(Long userId);
    Mono<Long> findUserIdByUsername(String username) ;
    Mono<UserDTO> registerUser(UserDTO userDTO);
}
