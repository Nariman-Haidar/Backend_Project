package server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import server.dto.QueueItemDTO;
import server.mapper.QueueItemMapper;
import server.model.QueueItem;
import server.repository.QueueItemRepository;
import server.service.QueueItemService;

@Service
public class QueueItemServiceImpl implements QueueItemService {

    private final QueueItemRepository queueItemRepository;
    private final QueueItemMapper queueItemMapper;

    @Autowired
    public QueueItemServiceImpl(
            QueueItemRepository queueItemRepository,
            QueueItemMapper queueItemMapper) {
        this.queueItemRepository = queueItemRepository;
        this.queueItemMapper = queueItemMapper;
    }

    @Override
    public Flux<QueueItemDTO> getQueueItemsByCourseId(Long courseId) {
        return queueItemRepository.findAllByCourseId(courseId)
                .map(queueItemMapper::queueItemToQueueItemDTO)
                .switchIfEmpty(Mono.empty());
    }

    
    @Override
    public Mono<QueueItemDTO> updateQueueItem(QueueItemDTO queueItemDTO) {
        QueueItem updatedQueueItem = queueItemMapper.queueItemDTOToQueueItem(queueItemDTO);
        return queueItemRepository.findByUserIdAndCourseId(updatedQueueItem.getUser_id(), updatedQueueItem.getCourse_id())
                .flatMap(existingQueueItem -> {
                    existingQueueItem.setId(updatedQueueItem.getId());
                    existingQueueItem.setUser_id(updatedQueueItem.getUser_id());
                    existingQueueItem.setCourse_id(updatedQueueItem.getCourse_id());
                    existingQueueItem.setActive(updatedQueueItem.isActive());
                    existingQueueItem.setComment(updatedQueueItem.getComment());
                    existingQueueItem.setLocation(updatedQueueItem.getLocation());
                    return queueItemRepository.save(existingQueueItem);
                })
                .switchIfEmpty(
                        queueItemRepository.save(updatedQueueItem)
                )
                .map(queueItemMapper::queueItemToQueueItemDTO);
    }






    @Override
    public Mono<QueueItemDTO> addQueueItem(QueueItemDTO queueItemDTO) {
        QueueItem queueItem = queueItemMapper.queueItemDTOToQueueItem(queueItemDTO);

        return queueItemRepository.findByUserIdAndCourseId(queueItem.getUser_id(), queueItem.getCourse_id())
                .flatMap(existingQueueItem -> {
                    if (existingQueueItem != null) {
                        return Mono.error(new QueueItemAlreadyExistsException("QueueItem already exists"));
                    } else {
                        return queueItemRepository.save(queueItem)
                                .map(queueItemMapper::queueItemToQueueItemDTO);
                    }
                })
                .switchIfEmpty(Mono.defer(() -> {
                    return queueItemRepository.save(queueItem)
                            .map(queueItemMapper::queueItemToQueueItemDTO);
                }));
    }



    @Override
    public Mono<Long> deleteQueueItem(Long queueItemId) {
        return queueItemRepository.findById(queueItemId)
                .flatMap(queueItem -> queueItemRepository.deleteById(queueItemId)
                        .thenReturn(queueItem.getId()));
    }
    @Override
    public Mono<QueueItemDTO> getQueueItemById(Long id) {
        return queueItemRepository.findById(id)
                .map(queueItemMapper::queueItemToQueueItemDTO);
    }
    @Override
    public Flux<QueueItemDTO> getAllQueueItems() {
        return queueItemRepository.findAll()
                .map(queueItemMapper::queueItemToQueueItemDTO);
    }

}
 