package server.service;

import org.springframework.security.core.Authentication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import server.dto.AccessDTO;
import server.dto.CourseDTO;
import server.dto.UserDTO;
import server.model.Access;

public interface AccessService {
    Mono<AccessDTO> saveAccess(AccessDTO accessDTO);
    Flux<AccessDTO> getAllAccess();
    Flux<Long> getAllCoursesForUser(Long userId);
    Flux<CourseDTO> getCoursesByUserId(Long userId);
    Flux<UserDTO> getUsersByCourseId(Long courseId);
    public Mono<CourseDTO> deleteAccess(CourseDTO courseDTO, Long userId);
    public Mono<CourseDTO> addAccess(CourseDTO courseDTO, Long userId);
    public Mono<Boolean> hasCourseAccess(Authentication authentication, String courseId);

}

