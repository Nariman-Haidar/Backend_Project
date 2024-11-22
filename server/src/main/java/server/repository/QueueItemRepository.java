package server.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import server.model.QueueItem;

@Repository
public interface QueueItemRepository extends ReactiveCrudRepository<QueueItem, Long> {
    @Query("SELECT * FROM queueitem q  WHERE  q.course_id =:courseId")
    Flux<QueueItem> findAllByCourseId(Long courseId);
    @Query("SELECT q FROM QueueItem q WHERE q.user_id = :userId AND q.course_id = :courseId")
    Mono<QueueItem> findByUserIdAndCourseId(Long userId, Long courseId);
}
