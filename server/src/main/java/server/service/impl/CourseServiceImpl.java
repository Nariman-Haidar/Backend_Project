package server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import server.dto.CourseDTO;
import server.mapper.*;
import server.model.Course;
import server.repository.CourseRepository;
import server.service.CourseService;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, CourseMapper courseMapper) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
    }

    @Override
    public Flux<CourseDTO> getAllCourses() {
        return courseRepository.findAll()
                .map(courseMapper::courseToCourseDTO);
    }

    @Override
    public Mono<CourseDTO> addCourse(CourseDTO courseDTO) {
        Course course = courseMapper.courseDTOToCourse(courseDTO);
        return courseRepository.save(course).map(courseMapper::courseToCourseDTO);

    }

    @Override
    public Mono<Long> deleteCourse(Long courseId) {
        return courseRepository.deleteById(courseId)
                .thenReturn(courseId);
    }
    @Override
    public Mono<CourseDTO> updateCourse(CourseDTO courseDTO) {
        return courseRepository.findById(courseDTO.getId())
                .flatMap(existingCourse -> {
                    existingCourse.setStatus(courseDTO.getStatus());
                    return courseRepository.save(existingCourse);
                })
                .map(courseMapper::courseToCourseDTO);
    }

}


