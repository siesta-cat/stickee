package cat.siesta.stickee.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class AppControllerAdvice {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handle(ResponseStatusException e) {
        log.debug("Error response sent. Code: {}, message: {}",
                e.getStatusCode(),
                e.getBody().getDetail());
        return ResponseEntity
                .status(e.getStatusCode())
                .body(e.getBody().getDetail());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<String> handle(NoResourceFoundException e) {
        log.debug("Static resource not found: {}", e.getResourcePath());
        return ResponseEntity
                .status(e.getStatusCode())
                .body("404 not found");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handle(Exception e) {
        log.error("Unexpected error", e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("internal server error");
    }
}
