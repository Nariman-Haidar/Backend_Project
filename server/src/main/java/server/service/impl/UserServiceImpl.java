package server.service.impl;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import server.dto.UserDTO;
import server.mapper.UserMapper;
import server.model.Role;
import server.model.User;
import server.repository.UserRepository;
import server.service.UserService;
import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.util.Date;
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    public String passwordEncoder(String username) {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(username);
    }
    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public Mono<UserDTO> findByUsername(String username) {
        return userRepository.findByUsername(username).map(userMapper::userToUserDTO);
    }

    @Override
    public Mono<UserDetails> getByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    @Override
    public Flux<UserDTO> getAllUsers() {
        return userRepository.findAll().map(userMapper::userToUserDTO);
    }

    @Override
    public Mono<UserDTO> addUser(UserDTO userDTO) {
        User newUser = userMapper.userDTOToUser(userDTO);
        newUser.setPassword(passwordEncoder((userDTO.getPassword())));
        newUser.setRole(Role.USER);
        newUser.setCreated_date(LocalDateTime.now());
        newUser.setUpdated_date(LocalDateTime.now());
        return userRepository.save(newUser).map(userMapper::userToUserDTO);
    }

    @Override
    public Mono<Long> deleteUser(Long userId) {
        return userRepository.findById(userId)
                .flatMap(user -> userRepository.deleteById(userId).thenReturn(user.getId()));
    }

    @Override
    public Mono<Long> findUserIdByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(User::getId)
                .switchIfEmpty(Mono.error(new UserNotFoundException("User not found for username: " + username)));
    }



    @Override
    public Mono<UserDTO> registerUser(UserDTO userDTO) {
        if (userDTO == null || userDTO.getUsername() == null || userDTO.getPassword() == null) {
            return Mono.error(new IllegalArgumentException("Invalid user information"));
        }
        User newUser = userMapper.userDTOToUser(userDTO);
        newUser.setPassword(passwordEncoder((userDTO.getPassword())));
        newUser.setRole(Role.USER);
        newUser.setCreated_date(LocalDateTime.now());
        newUser.setUpdated_date(LocalDateTime.now());
        System.out.println("newUser = "+newUser);
        return userRepository.findByUsername(userDTO.getUsername())
                .flatMap(existingUser -> {
                    if (existingUser!= null) {
                       
                        return Mono.error(new AlreadyExistsException("QueueItem already exists"));
                    } else {
                       
                        return userRepository.save(newUser).map(userMapper::userToUserDTO);
                    }
                })
                .switchIfEmpty(Mono.defer(() -> {
                    return userRepository.save(newUser).map(userMapper::userToUserDTO);
                }));
    }
}
