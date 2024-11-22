package server.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import server.dto.CourseDTO;
@Service
public interface CourseService {
    Flux<CourseDTO> getAllCourses();
    Mono<CourseDTO> addCourse(CourseDTO courseDTO);
    Mono<Long> deleteCourse(Long courseId);
    Mono<CourseDTO> updateCourse(CourseDTO courseDTO);
}
