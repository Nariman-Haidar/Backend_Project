package server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QueueItemDTO {
    private Long id;
    private Long user_id;
    private Long course_id;
    private String location;
    private boolean active;
    private String comment;
    private String eventType;
    public QueueItemDTO(Long id,String eventType) {
        this.id = id;
        this.eventType = eventType;
    }
}
