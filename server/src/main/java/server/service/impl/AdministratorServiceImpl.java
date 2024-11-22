package server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import server.dto.AdministratorDTO;
import server.dto.CourseDTO;
import server.mapper.AdministratorMapper;
import server.mapper.CourseMapper;
import server.model.Access;
import server.model.Administrator;
import server.repository.AdministratorRepository;
import server.repository.UserRepository;
import server.service.AdministratorService;

@Service
@Transactional
public class AdministratorServiceImpl implements AdministratorService {

    private final AdministratorRepository administratorRepository;
    private final AdministratorMapper administratorMapper;
    private final UserRepository userRepository;
    private final CourseMapper courseMapper;

    @Autowired
    public AdministratorServiceImpl(
            AdministratorRepository administratorRepository,
            AdministratorMapper administratorMapper
            , UserRepository userRepository, CourseMapper courseMapper) {
        this.administratorRepository = administratorRepository;
        this.administratorMapper = administratorMapper;
        this.userRepository = userRepository;
        this.courseMapper = courseMapper;
    }


        @Override
        public Flux<CourseDTO> getCoursesByUserId(Long userId) {
            return administratorRepository.findCoursesByUserId(userId)
                    .map(courseMapper::courseToCourseDTO);
        }

    @Override
    public Flux<Long> getAllCoursesForUser(Long userId) {
        return administratorRepository.findAllByUserId(userId)
                .map(Administrator::getCourseId);
    }



    @Override
    public Flux<AdministratorDTO> getAllAdministrators() {

        return administratorRepository.findAll()
                .doOnNext(administrator -> {
                    System.out.println("Administrator data: " + administrator);

                })
                .map(administratorMapper::administratorToAdministratorDTO);
    }

    @Override
    public Mono<Boolean> hasCourseAccess(Authentication authentication, String courseId) {
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .flatMap(user -> administratorRepository.existsByUserIdAndCourseId(user.getId(), Long.valueOf(courseId)))
                .defaultIfEmpty(false);
    }
}
