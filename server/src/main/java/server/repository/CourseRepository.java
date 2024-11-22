package server.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import server.model.Course;

@Repository
public interface CourseRepository extends ReactiveCrudRepository<Course, Long> {
}

