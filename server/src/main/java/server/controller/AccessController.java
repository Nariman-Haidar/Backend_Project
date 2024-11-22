package server.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import server.dto.CourseDTO;
import server.dto.QueueItemDTO;
import server.dto.UserDTO;
import server.service.AccessService;
import server.service.impl.AlreadyExistsException;
import server.service.impl.QueueItemAlreadyExistsException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/access")  //  "/api/test/hasAccess/{courseId}"
public class AccessController {
    private final Map<Long, Sinks.Many<Long>> accessEventSinks = new ConcurrentHashMap<>();
    private final Map<Long, Sinks.Many<CourseDTO>> coursesEventSinks = new ConcurrentHashMap<>();
    private final Map<Long, Sinks.Many<UserDTO>> usersEventSinks = new ConcurrentHashMap<>();
    private final AccessService accessService;
    private final Sinks.Many<CourseDTO> coursesEventSink;
    public AccessController(AccessService accessService, Sinks.Many<CourseDTO> coursesEventSink) {
        this.accessService = accessService;
        this.coursesEventSink = coursesEventSink;

    }

    @GetMapping(value = "/courses/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Long> getCoursesIdByUserId(@PathVariable Long userId) {
        // Create a new sink for each subscriber
        Sinks.Many<Long> localSink = Sinks.many().unicast().onBackpressureBuffer();

        // Subscribe to both courseEventSink and queueItemService
        getOrCreateAccessEventSink(userId).asFlux().subscribe(localSink::tryEmitNext);
        accessService.getAllCoursesForUser(userId).subscribe(localSink::tryEmitNext);

        return localSink.asFlux();
    }

    @GetMapping(value = "/by-user/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<CourseDTO> getCoursesByUserId(@PathVariable Long userId) {
        // Create a new sink for each subscriber
        Sinks.Many<CourseDTO> localSink = Sinks.many().unicast().onBackpressureBuffer();

        // Subscribe to both courseEventSink and queueItemService
        getOrCreateCourseEventSink(userId).asFlux().subscribe(localSink::tryEmitNext);
        accessService.getCoursesByUserId(userId).subscribe(localSink::tryEmitNext);
        return localSink.asFlux();
    }

    @GetMapping(value = "/by-course/{courseId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<UserDTO> getUsersByCourseId(@PathVariable Long courseId) {
        // Create a new sink for each subscriber
        Sinks.Many<UserDTO> localSink = Sinks.many().unicast().onBackpressureBuffer();
        // Subscribe to both courseEventSink and queueItemService
        getOrCreateUserEventSink(courseId).asFlux().subscribe(localSink::tryEmitNext);
        accessService.getUsersByCourseId(courseId).subscribe(localSink::tryEmitNext);
        return localSink.asFlux();
    }
    @DeleteMapping(value ="/delete/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<Void> deleteAccess(@RequestBody CourseDTO courseDTO, @PathVariable Long userId) {
        return accessService.deleteAccess(courseDTO, userId)
                .doOnSuccess(course -> {
                    if (course != null) {
                        course.setEventType("delete");
                        coursesEventSinks.values().forEach(sink -> sink.tryEmitNext(course));
                    }
                })
                .then();
    }

    @PostMapping(value ="/add/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> addQueueItem(@RequestBody CourseDTO courseDTO, @PathVariable Long userId) {
        System.out.println("add  "+ userId +  "   " +  courseDTO);
        return accessService.addAccess(courseDTO,userId)
                .doOnSuccess(course -> {
                    if (course != null) {
                        course.setEventType("add");
                        coursesEventSinks.values().forEach(sink -> sink.tryEmitNext(course));
                    }
                })
                .onErrorMap(AlreadyExistsException.class,
                        ex -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Access already exists", ex))
                .then();
    }

    @GetMapping("/course/{courseId}")
    public Mono<ResponseEntity<String>> checkAccess(Authentication authentication, @PathVariable String courseId) {
        return accessService.hasCourseAccess(authentication, courseId)
                .map(hasAccess -> hasAccess
                        ? ResponseEntity.ok("Access granted")
                        : ResponseEntity.status(403).body("Access denied"));
    }

    private Sinks.Many<Long>  getOrCreateAccessEventSink(Long courseId) {
        return accessEventSinks.computeIfAbsent(courseId, key -> Sinks.many().multicast().onBackpressureBuffer());
    }

    private Sinks.Many<CourseDTO> getOrCreateCourseEventSink(Long courseId) {
        return coursesEventSinks.computeIfAbsent(courseId, key -> Sinks.many().multicast().onBackpressureBuffer());
    }

    private Sinks.Many<UserDTO> getOrCreateUserEventSink(Long courseId) {
        return usersEventSinks.computeIfAbsent(courseId, key -> Sinks.many().multicast().onBackpressureBuffer());
    }
}
