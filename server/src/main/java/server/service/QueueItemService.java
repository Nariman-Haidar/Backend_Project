package server.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import server.dto.QueueItemDTO;

public interface QueueItemService {
    Mono<QueueItemDTO> getQueueItemById(Long id);
    Flux<QueueItemDTO> getQueueItemsByCourseId(Long courseId);
    Flux<QueueItemDTO> getAllQueueItems();
    Mono<QueueItemDTO> addQueueItem(QueueItemDTO queueItemDTO);
    Mono<Long> deleteQueueItem(Long queueItemId);
    Mono<QueueItemDTO> updateQueueItem(QueueItemDTO queueItemDTO);
}
