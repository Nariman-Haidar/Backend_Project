package server.repository;


import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import server.model.Access;
import server.model.Administrator;
import server.model.Course;

@Repository
public interface AdministratorRepository extends ReactiveCrudRepository<Administrator, Long> {

    @Query("SELECT c.id, c.title, c.status FROM administrators a " +
           "JOIN courses c ON a.course_id = c.id WHERE a.user_id = :userId")
    Flux<Course> findCoursesByUserId(Long userId);
    Flux<Administrator> findAllByUserId(Long userd);
    Mono<Boolean> existsByUserIdAndCourseId(Long userId, Long courseId);
   
}



