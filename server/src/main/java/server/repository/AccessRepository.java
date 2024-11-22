package server.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import server.model.Access;
import server.model.Course;
import server.model.User;

public interface AccessRepository extends ReactiveCrudRepository<Access, Long> {
    @Query("SELECT c.id, c.title, c.status FROM access a " +
            "JOIN courses c ON a.course_id = c.id WHERE a.user_id = :userId")
    Flux<Course> findCoursesByUserId(Long userId);
    @Query("SELECT u.id, u.username, u.role FROM access a " +
            "JOIN users u ON a.user_id = u.id WHERE a.course_id = :courseId")
    Flux<User> findUsersByCourseId(Long courseId);
    Flux<Access> findAllByUserId(Long userId);
    Mono<Boolean> existsByUserIdAndCourseId(Long userId, Long courseId);
    Mono<Void> deleteByUserIdAndCourseId(Long userId, Long courseId);

}

