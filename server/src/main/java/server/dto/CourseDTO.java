package server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDTO {
    private Long id;
    private String title;
    private int status;
    private List<Long> queueItemIds;
    private List<Long> administratorRoleIds;
    private LocalDateTime created_date;
    private String eventType;
    public CourseDTO(Long id,String eventType) {
        this.id = id;
        this.eventType = eventType;
    }
}
