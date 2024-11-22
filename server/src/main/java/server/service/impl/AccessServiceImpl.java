package server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import server.dto.AccessDTO;
import server.dto.CourseDTO;
import server.dto.UserDTO;
import server.mapper.AccessMapper;
import server.mapper.CourseMapper;
import server.mapper.UserMapper;
import server.model.Access;
import server.repository.AccessRepository;
import server.repository.UserRepository;
import server.service.AccessService;

@Service
public class AccessServiceImpl implements AccessService {

    private final AccessRepository accessRepository;
    private final AccessMapper accessMapper;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final CourseMapper courseMapper;
    @Autowired
    public AccessServiceImpl(UserRepository userRepository, AccessRepository accessRepository, AccessMapper accessMapper, UserMapper userMapper, CourseMapper courseMapper) {
        this.accessRepository = accessRepository;
        this.userRepository = userRepository;
        this.accessMapper = accessMapper;
        this.userMapper = userMapper;
        this.courseMapper = courseMapper;
    }

    @Override
    public Mono<AccessDTO> saveAccess(AccessDTO accessDTO) {
        Access access = accessMapper.accessDTOToAccess(accessDTO);
        return accessRepository.save(access)
                .map(accessMapper::accessToAccessDTO);
    }

    @Override
    public Flux<AccessDTO> getAllAccess() {
        return accessRepository.findAll()
                .map(accessMapper::accessToAccessDTO);
    }

    @Override
    public Flux<Long> getAllCoursesForUser(Long userId) {
        return accessRepository.findAllByUserId(userId)
                .map(Access::getCourseId);
    }

    @Override
    public Mono<Boolean> hasCourseAccess(Authentication authentication, String courseId) {
        String username = authentication.getName();

        return userRepository.findByUsername(username)
                .flatMap(user -> accessRepository.existsByUserIdAndCourseId(user.getId(), Long.valueOf(courseId)))
                .defaultIfEmpty(false);
    }

    @Override
    public Flux<CourseDTO> getCoursesByUserId(Long userId) {
        return accessRepository.findCoursesByUserId(userId)
                .map(courseMapper::courseToCourseDTO);
    }

    @Override
    public Flux<UserDTO> getUsersByCourseId(Long courseId) {
        return accessRepository.findUsersByCourseId(courseId)
                .map(userMapper::userToUserDTO);
    }

    @Override
    public Mono<CourseDTO> deleteAccess(CourseDTO courseDTO, Long userId) {
        return accessRepository.existsByUserIdAndCourseId(userId, courseDTO.getId())
                .flatMap(exists -> {
                    if (exists) {
                        return accessRepository.deleteByUserIdAndCourseId(userId, courseDTO.getId())
                                .then(Mono.just(courseDTO)); 
                    } else {
                        return Mono.empty();
                    }
                });
    }

    @Override
    public Mono<CourseDTO> addAccess(CourseDTO courseDTO, Long userId) {
        Access access = new Access(userId, courseDTO.getId());
        return accessRepository.existsByUserIdAndCourseId(access.getUserId(), access.getCourseId())
                .flatMap(existingAccess -> {
                    if (existingAccess) {
                        return Mono.error(new AlreadyExistsException("Access already exists"));
                    } else {
                        return accessRepository.save(access)
                                .flatMap(savedAccess -> accessRepository.findCoursesByUserId(savedAccess.getUserId())
                                        .map(courseMapper::courseToCourseDTO)
                                        .next());
                    }
                })
                .switchIfEmpty(Mono.defer(() -> {
                    return accessRepository.save(access)
                            .flatMap(savedAccess -> accessRepository.findCoursesByUserId(savedAccess.getUserId())
                                    .map(courseMapper::courseToCourseDTO)
                                    .next());
                }));
    }



}

