package tech.ada.products_api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ResponseDTO<T>(String message, LocalDateTime timestamp, T data) {
    public ResponseDTO(String message, T data) {
        this(message, LocalDateTime.now(), data);
    }
}
