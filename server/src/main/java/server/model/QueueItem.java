package server.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "queueitem")
public class QueueItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   @ManyToOne
   @JoinColumn(name = "user_id")
    private Long user_id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Long course_id;

    private String location;

    private boolean active;

    private String comment;

 

}

