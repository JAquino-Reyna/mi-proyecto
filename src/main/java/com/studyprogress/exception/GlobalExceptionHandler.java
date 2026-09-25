package com.studyprogress.exception;

import com.studyprogress.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.dao.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import lombok.extern.slf4j.Slf4j;
import java.time.Instant;
import java.util.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiException.class)
    ResponseEntity<ErrorResponseDTO> api(ApiException ex, HttpServletRequest request) {
        return error(ex.getStatus(), ex.getMessage(), request, Map.of());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponseDTO> validation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> details = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(e -> details.put(e.getField(), e.getDefaultMessage()));
        return error(HttpStatus.BAD_REQUEST, "Revisa los campos enviados", request, details);
    }
    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class,
            ConstraintViolationException.class, org.springframework.data.mapping.PropertyReferenceException.class, IllegalArgumentException.class})
    ResponseEntity<ErrorResponseDTO> malformed(Exception ex, HttpServletRequest request) {
        return error(HttpStatus.BAD_REQUEST, "Solicitud invÃ¡lida", request, Map.of());
    }
    @ExceptionHandler({DataIntegrityViolationException.class, OptimisticLockingFailureException.class})
    ResponseEntity<ErrorResponseDTO> conflict(Exception ex, HttpServletRequest request) {
        return error(HttpStatus.CONFLICT, "El recurso ya existe, estÃ¡ en uso o fue modificado. Actualiza e intenta nuevamente", request, Map.of());
    }
    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<ErrorResponseDTO> forbidden(Exception ex, HttpServletRequest request) {
        return error(HttpStatus.FORBIDDEN, "No tienes permiso para esta operaciÃ³n", request, Map.of());
    }
    @ExceptionHandler(NoResourceFoundException.class)
    ResponseEntity<ErrorResponseDTO> missing(Exception ex, HttpServletRequest request) {
        return error(HttpStatus.NOT_FOUND, "Recurso no encontrado", request, Map.of());
    }
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    ResponseEntity<ErrorResponseDTO> method(Exception ex, HttpServletRequest request) {
        return error(HttpStatus.METHOD_NOT_ALLOWED, "MÃ©todo no permitido", request, Map.of());
    }
    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponseDTO> unexpected(Exception ex, HttpServletRequest request) {
        log.error("request_failed path={}", request.getRequestURI(), ex);
        return error(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo procesar la solicitud", request, Map.of());
    }
    private ResponseEntity<ErrorResponseDTO> error(HttpStatus status, String message, HttpServletRequest request, Map<String, String> details) {
        return ResponseEntity.status(status).body(new ErrorResponseDTO(Instant.now(), status.value(), status.getReasonPhrase(), message, request.getRequestURI(), details));
    }
}
