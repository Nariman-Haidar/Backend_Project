package server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import server.dto.CourseDTO;
import server.dto.QueueItemDTO;
import server.dto.UserDTO;
import server.mapper.CourseMapper;
import server.service.CourseService;
import reactor.core.publisher.Sinks;
@RestController
@RequestMapping("/api/course")
@CrossOrigin(origins = "*")
public class CourseController {

    private final CourseService courseService;
    private final CourseMapper courseMapper;
    private final Sinks.Many<CourseDTO> courseEventSink;

    @Autowired
    public CourseController(CourseService courseService, CourseMapper courseMapper, Sinks.Many<CourseDTO> courseEventSink) {
        this.courseService = courseService;
        this.courseMapper = courseMapper;
        this.courseEventSink = courseEventSink;
    }
    int count = 0;
    @GetMapping(value = "/courses", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<CourseDTO> getAllCourses() {
      
        Sinks.Many<CourseDTO> localSink = Sinks.many().unicast().onBackpressureBuffer();

        courseEventSink.asFlux().subscribe(localSink::tryEmitNext);
        courseService.getAllCourses().subscribe(localSink::tryEmitNext);
        return localSink.asFlux();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> addCourse(@RequestBody CourseDTO courseDTO) {
        return courseService.addCourse(courseDTO)
                .doOnSuccess(courseEventSink::tryEmitNext)
                .doOnSuccess(course -> System.out.println("Course added: " + course))
                .then();

    }

    @DeleteMapping("/{courseId}")
    public Mono<Void> deleteCourse(@PathVariable Long courseId) {
        return courseService.deleteCourse(courseId)
                .doOnSuccess(deletedCourseId -> {
                    courseEventSink.tryEmitNext(new CourseDTO(deletedCourseId, "delete"));
                })
                .then();
    }

    @PutMapping("/update")
    public Mono<Void> updateCourse(@RequestBody CourseDTO courseDTO) {
        return courseService.updateCourse(courseDTO) 
                .doOnSuccess(updatedCourse -> { updatedCourse.setEventType("update"); })
                .doOnSuccess(courseEventSink::tryEmitNext)

                .then();
    }

}

