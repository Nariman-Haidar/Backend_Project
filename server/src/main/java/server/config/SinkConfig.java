package server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;
import server.dto.CourseDTO;
import server.dto.QueueItemDTO;
import server.dto.UserDTO;

@Configuration
public class SinkConfig {

    @Bean
    public Sinks.Many<UserDTO> UserEventSink() {
        return Sinks.many().multicast().onBackpressureBuffer();
    }

    @Bean
    public Sinks.Many<CourseDTO> courseEventSink() {
      
        return Sinks.many().multicast().onBackpressureBuffer();
    }

    @Bean
    public Sinks.Many<QueueItemDTO> QueueItemEventSink() {
        
        return Sinks.many().multicast().onBackpressureBuffer();
    }
    @Bean
    public Sinks.Many<Long> AccessEventSink() {
        
        return Sinks.many().multicast().onBackpressureBuffer();
    }

}

