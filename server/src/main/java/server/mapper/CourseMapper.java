package server.mapper;

import org.springframework.stereotype.Component;
import server.dto.CourseDTO;
import server.model.Course;

@Component
public class CourseMapper {

    public CourseDTO courseToCourseDTO(Course course) {
        if (course == null) {
            return null;
        }

        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setId(course.getId());
        courseDTO.setTitle(course.getTitle());
        courseDTO.setStatus(course.getStatus());

        return courseDTO;
    }

    public Course courseDTOToCourse(CourseDTO courseDTO) {
        if (courseDTO == null) {
            return null;
        }

        Course course = new Course();
        course.setId(courseDTO.getId());
        course.setTitle(courseDTO.getTitle());
        course.setStatus(courseDTO.getStatus());

        return course;
    }
}
