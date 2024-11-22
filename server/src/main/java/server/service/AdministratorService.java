package server.service;

import org.springframework.security.core.Authentication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import server.dto.AdministratorDTO;
import server.dto.CourseDTO;

public interface AdministratorService {
    Flux<AdministratorDTO> getAllAdministrators();
    Flux<CourseDTO> getCoursesByUserId(Long userId);
    Flux<Long> getAllCoursesForUser(Long userId);
    public Mono<Boolean> hasCourseAccess(Authentication authentication, String courseId);
}
