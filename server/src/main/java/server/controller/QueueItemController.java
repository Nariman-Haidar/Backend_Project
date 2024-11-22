package server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import server.dto.MessageDTO;
import server.dto.QueueItemDTO;
import server.service.QueueItemService;
import server.service.impl.QueueItemAlreadyExistsException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/queue-items")
public class QueueItemController {

    private final QueueItemService queueItemService;
    private final Map<Long, Sinks.Many<QueueItemDTO>> courseEventSinks = new ConcurrentHashMap<>();
    private final Map<Long, Sinks.Many<MessageDTO>> messageEventSinks = new ConcurrentHashMap<>();

    @Autowired
    public QueueItemController(QueueItemService queueItemService) {
        this.queueItemService = queueItemService;
    }


    @GetMapping(value = "/messages/{courseId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<MessageDTO> getMessagesByCourseId(@PathVariable Long courseId) {
        Sinks.Many<MessageDTO> localMessageSink = Sinks.many().multicast().onBackpressureBuffer();
        getOrCreateMessageEventSink(courseId).asFlux().subscribe(localMessageSink::tryEmitNext);
        return localMessageSink.asFlux();
    }

    @PostMapping("/message/{courseId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> postMessage(@PathVariable Long courseId, @RequestBody MessageDTO messageDTO ) {
        messageDTO.setId(0L);
        messageDTO.setEventType("message");
        getOrCreateMessageEventSink(courseId).tryEmitNext(messageDTO);
        return Mono.empty();
    }

    @GetMapping(value = "/{courseId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
       public Flux<QueueItemDTO> getQueueItemsByCourseId(@PathVariable Long courseId) {
           Sinks.Many<QueueItemDTO> localSink = Sinks.many().unicast().onBackpressureBuffer();

           getOrCreateCourseEventSink(courseId).asFlux().subscribe(localSink::tryEmitNext);
           queueItemService.getQueueItemsByCourseId(courseId).subscribe(localSink::tryEmitNext);

           return localSink.asFlux();
       }


    @PostMapping("/join")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> addQueueItem(@RequestBody QueueItemDTO queueItemDTO) {
        return queueItemService.addQueueItem(queueItemDTO)
                .doOnSuccess(queueItem -> getOrCreateCourseEventSink(queueItem.getCourse_id()).tryEmitNext(queueItem))
                .onErrorResume(QueueItemAlreadyExistsException.class, ex -> {
                    return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "QueueItem already exists", ex));
                })
                .then();
    }

    @DeleteMapping("/delete/{queueItemId}")
    public Mono<Void> deleteQueueItemById(@PathVariable Long queueItemId) {
        return queueItemService.deleteQueueItem(queueItemId)
                .doOnSuccess(QueueItemId ->
                        courseEventSinks.values().forEach(sink ->
                                sink.tryEmitNext(new QueueItemDTO(QueueItemId, "delete"))))
                .then();
    }

    @PutMapping("/update")
    public Mono<Void> updateQueueItemById(@RequestBody QueueItemDTO queueItemDTO) {

        return queueItemService.updateQueueItem(queueItemDTO) 
                .doOnSuccess(updatedQueueItem -> { updatedQueueItem.setEventType("update"); })
                .doOnSuccess(queueItem -> getOrCreateCourseEventSink(queueItem.getCourse_id()).tryEmitNext(queueItem))
                .then();
    }


    @GetMapping(value = "items/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<QueueItemDTO> getQueueItemById(@PathVariable Long id) {
        return queueItemService.getQueueItemById(id)
                .doOnSuccess(queueItemDTO -> {
                    courseEventSinks.values().forEach(sink -> sink.tryEmitNext(queueItemDTO));
                });
    }

    private Sinks.Many<MessageDTO> getOrCreateMessageEventSink(Long courseId) {
        return messageEventSinks.computeIfAbsent(courseId, key -> Sinks.many().multicast().onBackpressureBuffer());
    }
    private Sinks.Many<QueueItemDTO> getOrCreateCourseEventSink(Long courseId) {
        return courseEventSinks.computeIfAbsent(courseId, key -> Sinks.many().multicast().onBackpressureBuffer());
    }
}

