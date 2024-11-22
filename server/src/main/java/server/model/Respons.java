package server.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Respons<T>  {
    private T token;
    private String message;
    private Long id;
}