package server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.model.Role;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserDTO {
    private Long id;
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private Role role;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String token;
    private String eventType;
    public UserDTO(Long id) {
        this.id = id;
    }
    public UserDTO(Long id, String eventType) {
        this.id = id;
        this.eventType = eventType;
    }
}
