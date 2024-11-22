package server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import server.dto.AdministratorDTO;
import server.dto.CourseDTO;
import server.service.AdministratorService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/administrator")
public class AdministratorController {
    private final Map<Long, Sinks.Many<Long>> administratorEventSinks = new ConcurrentHashMap<>();
    private final Map<Long, Sinks.Many<CourseDTO>> coursesEventSinks = new ConcurrentHashMap<>();
    private final Sinks.Many<Long> administratorEventSink;
    private final AdministratorService administratorService;
    @Autowired
    public AdministratorController(Sinks.Many<Long> administratorEventSink, AdministratorService administratorService) {
        this.administratorEventSink = administratorEventSink;
        this.administratorService = administratorService;
    }


    @GetMapping
    public Flux<AdministratorDTO> getAllAdministrators() {
        return administratorService.getAllAdministrators();
    }


    @GetMapping(value = "/by-user/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<CourseDTO> getCoursesByUserId(@PathVariable Long userId) {
      
        Sinks.Many<CourseDTO> localSink = Sinks.many().unicast().onBackpressureBuffer();

        getOrCreateCourseEventSink(userId).asFlux().subscribe(localSink::tryEmitNext);
        administratorService.getCoursesByUserId(userId).subscribe(localSink::tryEmitNext);
        return localSink.asFlux();
    }

    @GetMapping(value = "/courses/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Long> getAllCoursesIdByUserId(@PathVariable Long userId) {
        Sinks.Many<Long> localSink = Sinks.many().unicast().onBackpressureBuffer();
        getOrCreateAdministratorEventSink(userId).asFlux().subscribe(localSink::tryEmitNext);
        administratorService.getAllCoursesForUser(userId).subscribe(localSink::tryEmitNext);
        return localSink.asFlux();
    }
 
    private Sinks.Many<Long>  getOrCreateAdministratorEventSink(Long courseId) {
        return administratorEventSinks.computeIfAbsent(courseId, key -> Sinks.many().multicast().onBackpressureBuffer());
    }

    private Sinks.Many<CourseDTO>  getOrCreateCourseEventSink(Long courseId) {
        return coursesEventSinks.computeIfAbsent(courseId, key -> Sinks.many().multicast().onBackpressureBuffer());
    }
}
