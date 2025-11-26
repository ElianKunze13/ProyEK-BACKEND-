package com.example.demo.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

// GlobalExceptionHandler.java
@ResponseStatus(HttpStatus.BAD_REQUEST)
@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(DataAccessException.class)
  public ResponseEntity<Map<String, Object>> handleDataAccessException(DataAccessException e) {
    Map<String, Object> response = new HashMap<>();
    response.put("message", e.getMessage());
    response.put("description", "Error de acceso a datos");
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Map<String, Object>> handleConstraintViolationException(ConstraintViolationException e) {
    Map<String, Object> response = new HashMap<>();
    response.put("message", e.getMessage());
    response.put("description", "Error de validación");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<Map<String, Object>> notFound(ResourceNotFoundException e) {
    Map<String, Object> response = new HashMap<>();
    response.put("message", e.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }


  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    return ResponseEntity.badRequest().body(errors);

  }

/*
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleException(Exception exception) {
    Map<String, Object> response = new HashMap<>();
    response.put("timestamp", LocalDateTime.now().toString());
    response.put("error", exception.toString());
    response.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
    response.put("message", exception.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }
*/
@ExceptionHandler(Exception.class)
public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
  return ResponseEntity.status(HttpStatus.BAD_REQUEST)
      .body(new ErrorResponse("Unexpected error: " + ex.getMessage()));
}

  // Clase estática interna para representar la respuesta de error
  public static class ErrorResponse {
    private String message;

    public ErrorResponse(String message) {
      this.message = message;
    }

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }
  }

}
