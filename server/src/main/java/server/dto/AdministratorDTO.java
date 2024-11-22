package server.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdministratorDTO {
    private Long userId;
    private Long courseId;
    private LocalDateTime created_date;
}

